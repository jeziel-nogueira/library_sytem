package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.v2com.Enums.BookStatus;
import org.v2com.Enums.LoanStatus;
import org.v2com.Enums.UserStatus;
import org.v2com.dto.LoanDTO;
import org.v2com.entity.BookEntity;
import org.v2com.entity.LoanEntity;
import org.v2com.entity.UserEntity;
import org.v2com.exceptions.*;
import org.v2com.repository.BookRepository;
import org.v2com.repository.LoanRepository;
import org.v2com.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class LoanService {

    @Inject
    BookRepository bookRepository;

    @Inject
    LoanRepository loanRepository;

    @Inject
    UserRepository userRepository;

    public List<LoanDTO> getAllLoans() throws Exception {
        try {
            return loanRepository.getAllLoans().stream()
                    .map(LoanDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public LoanDTO getActiveLoanByBookId(UUID bookId) throws Exception {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            if (bookEntity == null) {
                throw new BookNotFoundException();
            }

            LoanEntity loanEntity = loanRepository.findActiveLoanByBookId(bookId);

            if (loanEntity == null) {
                throw new LoanNotFoundException("");
            }
            return LoanDTO.fromEntity(loanEntity);
        } catch (BookNotFoundException | LoanNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public LoanDTO getLoanById(UUID loanId) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(loanId);
            if(loanEntity == null){
                throw new LoanNotFoundException(loanId.toString());
            }
            return LoanDTO.fromEntity(loanEntity);
        } catch (LoanNotFoundException le) {
            throw le;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<LoanDTO> getLoansByUserId(UUID userId) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(userId);
            if(userEntity == null){
                throw new  UserNotFoundException(userId.toString());
            }
            List<LoanDTO> loanDTOS = loanRepository.findLoanByUserId(userId).stream()
                    .map(LoanDTO::fromEntity)
                    .collect(Collectors.toList());
            if (loanDTOS.isEmpty()) {
                throw new LoanNotFoundException("");
            }
            return loanDTOS;
        } catch (UserNotFoundException
                | LoanNotFoundException ex) {
            throw ex;
        }catch (Exception e){
            throw new Exception(e);
        }

    }

    @Transactional
    public LoanDTO loanBook(UUID bookId, UUID userId) throws Exception {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            UserEntity userEntity = userRepository.findById(userId);

            if (bookEntity == null) {
                throw new BookNotFoundException();
            }
            if (bookEntity.getStatus() != BookStatus.AVAILABLE) {
                throw new BookUnavailableToLoanException(bookId.toString());
            }
            if (userEntity == null) {
                throw new UserNotFoundException(userId.toString());
            }
            if (userEntity.getStatus() == UserStatus.INACTIVE) {
                throw new UserInactiveException(userId.toString());
            }

            LoanEntity loanEntity = new LoanEntity();
            loanEntity.setBook_id(bookId);
            loanEntity.setStatus(LoanStatus.PENDING);
            loanEntity.setLoan_date(LocalDate.now());
            loanEntity.setDue_date(LocalDate.now().plusDays(14));
            loanEntity.setUser_id(userEntity.getId());

            bookRepository.changeBookStatus(bookEntity, BookStatus.UNAVAILABLE);
            loanRepository.persist(loanEntity);

            return LoanDTO.fromEntity(loanEntity);

        } catch (BookNotFoundException
                 | BookUnavailableToLoanException
                 | UserNotFoundException
                 | UserInactiveException bo) {
            throw bo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public void endLoan(UUID loanId) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(loanId);
            if (loanEntity == null) {
                throw new LoanNotFoundException(loanId.toString());
            }
            BookEntity bookEntity = bookRepository.findById(loanEntity.getBook_id());
            if (bookEntity == null) {
                throw new BookNotFoundException();
            }

            loanRepository.changeLoanStatus(loanEntity, LoanStatus.RETURNED);
            bookEntity.setStatus(BookStatus.AVAILABLE);

            loanRepository.persist(loanEntity);
        } catch (LoanNotFoundException | BookNotFoundException le) {
            throw le;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public LoanEntity renewBookLoan(UUID loanId, int days) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(loanId);
            if (loanEntity == null) {
                throw new LoanNotFoundException(loanId.toString());
            }

            LocalDate dueDate = loanEntity.getDue_date().plusDays(days);
            loanEntity.setDue_date(dueDate);
            loanRepository.update(loanEntity);
            return loanEntity;
        } catch (LoanNotFoundException lo) {
            throw lo;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
