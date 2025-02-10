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


    public List<LoanDTO> getAllLoans() throws Exception {
        try {
            return loanRepository.getAllLoans().stream()
                    .map(LoanDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao buscar emprestimos", e);
        }
    }

    public LoanDTO getActiveLoanByBookId(UUID bookId) throws Exception {
        try {
            LoanDTO loanDTO = LoanDTO.fromEntity(loanRepository.findActiveLoanByBookId(bookId));
            if (loanDTO == null) {
                throw new IllegalArgumentException("Emprestimo não encontrado");
            }
            return LoanDTO.fromEntity(loanRepository.findActiveLoanByBookId(bookId));
        } catch (Exception e) {
            throw new Exception("Erro ao buscar emprestimo", e);
        }
    }

    public LoanDTO getLoanById(UUID loanId) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(loanId);
            if(loanEntity == null){
                throw new IllegalArgumentException("Emprestimo não encontrado.");
            }
            return LoanDTO.fromEntity(loanEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao buscar emprestimo", e);
        }
    }

    public List<LoanDTO> getActiveLoansByUserId(UUID userId) throws Exception {
        try {
            List<LoanDTO> loanDTOS = loanRepository.findActiveLoanByUserId(userId).stream()
                    .map(LoanDTO::fromEntity)
                    .collect(Collectors.toList());
            if (loanDTOS.isEmpty()) {
                throw new IllegalArgumentException("Nemhum emprestimo encontrado.");
            }
            return loanDTOS;
        }catch (Exception e){
            throw new Exception("Erro ao buscar emprestimos.", e);
        }

    }

    @Transactional
    public LoanDTO loanBook(UUID bookId, UUID userId) throws Exception {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            UserEntity userEntity = userRepository.findById(userId);

            if (bookEntity == null) {
                throw new IllegalArgumentException("Livro não encontrado");
            }
            if (bookEntity.getStatus() != BookStatus.AVAILABLE) {
                throw new IllegalArgumentException("Livro indisponivel");
            }
            if (userEntity == null) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }
            if (userEntity.getStatus() == UserStatus.INACTIVE) {
                throw new IllegalArgumentException("Usuario inativo");
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
        } catch (Exception e) {
            throw new Exception("Erro ao fazer Emprestimo", e);
        }
    }

    @Transactional
    public void endLoan(UUID loanId) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(loanId);
            BookEntity bookEntity = bookRepository.findById(loanEntity.getBook_id());
            if (loanEntity == null) {
                throw new IllegalArgumentException("Empréstimo não encontrado");
            }
            if (bookEntity == null) {
                throw new IllegalArgumentException("Livro não encontrado");
            }

            loanRepository.changeLoanStatus(loanEntity, LoanStatus.RETURNED);
            bookEntity.setStatus(BookStatus.AVAILABLE);

            loanRepository.persist(loanEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao finalizar Emprestimo", e);
        }
    }

    @Transactional
    public LoanEntity renewBookLoan(UUID laonId, int days) throws Exception {
        try {
            LoanEntity loanEntity = loanRepository.findLoanById(laonId);
            LocalDate dueDate = loanEntity.getDue_date().plusDays(days);
            if (loanEntity == null) {
                throw new IllegalArgumentException("Emprestimo não encontrado.");
            }
            loanEntity.setDue_date(dueDate);
            loanRepository.update(loanEntity);
            return loanEntity;
        } catch (Exception e) {
            throw new Exception("Erro ao finalizar Emprestimo", e);
        }
    }


}
