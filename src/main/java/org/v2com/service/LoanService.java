package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.v2com.Enums.BookStatus;
import org.v2com.Enums.LoanStatus;
import org.v2com.Enums.ReserveStatus;
import org.v2com.Enums.UserStatus;
import org.v2com.dto.LoanDTO;
import org.v2com.entity.BookEntity;
import org.v2com.entity.LoanEntity;
import org.v2com.entity.UserEntity;
import org.v2com.repository.BookRepository;
import org.v2com.repository.LoanRepository;
import org.v2com.repository.ReserveRepository;
import org.v2com.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

    @Inject
    ReserveRepository reserveRepository;


    public List<LoanDTO> getAllLoans() {
        return loanRepository.getAllLoans().stream()
                .map(LoanDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public LoanDTO getActiveLoanByBookId(UUID bookId){
        return LoanDTO.fromEntity(loanRepository.findActiveLoanByBookId(bookId));
    }

    public Optional<LoanDTO> getLoanById(UUID loanId) {
        LoanEntity loanEntity = loanRepository.findLoanById(loanId);

        return Optional.of(LoanDTO.fromEntity(loanEntity));
    }

    public List<LoanDTO> getActiveLoansByUserId(UUID userId){
        return loanRepository.findActiveLoanByUserId(userId).stream()
                .map(LoanDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanDTO loanBook(UUID bookId, UUID userId) throws Exception {
        BookEntity bookEntity = bookRepository.findById(bookId);
        UserEntity userEntity = userRepository.findById(userId);

        if (bookEntity == null) {
            throw new Exception("Livro não encontrado");
        }
        if (bookEntity.getStatus() != BookStatus.AVAILABLE) {
            throw new Exception("Livro indisponivel");
        }
        if (userEntity == null) {
            throw new Exception("Usuário não encontrado");
        }
        if (userEntity.getStatus() == UserStatus.INACTIVE) {
            throw new Exception("Usuario inativo");
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
    }

    @Transactional
    public void endLoan(UUID loanId) throws Exception {
        LoanEntity loanEntity = loanRepository.findLoanById(loanId);
        BookEntity bookEntity = bookRepository.findById(loanEntity.getBook_id());
        if (loanEntity == null) {
            throw new Exception("Empréstimo não encontrado");
        }
        if (bookEntity == null) {
            throw new Exception("Livro não encontrado");
        }

        loanRepository.changeLoanStatus(loanEntity, LoanStatus.RETURNED);
        bookEntity.setStatus(BookStatus.AVAILABLE);

        loanRepository.persist(loanEntity);
    }

    @Transactional
    public LoanEntity renewBookLoan(UUID laonId, int days) {
        LoanEntity loanEntity = loanRepository.findLoanById(laonId);
        loanEntity.setDue_date(LocalDate.now().plusDays(days));
        loanRepository.update(loanEntity);
        return loanEntity;
    }


}
