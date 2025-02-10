package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.v2com.Enums.LoanStatus;
import org.v2com.entity.LoanEntity;

import java.util.List;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class LoanRepository {

    @Inject
    EntityManager entityManager;

    public void persist(Object entity) {
        entityManager.persist(entity);
    }
    public LoanEntity findLoanById(UUID id) {
        return entityManager.find(LoanEntity.class, id);
    }
    public void update(Object entity) {
        entityManager.merge(entity);
    }

    public List<LoanEntity> getAllLoans(){
        return entityManager.createQuery("SELECT l FROM LoanEntity l", LoanEntity.class)
                .getResultList();
    }


    public LoanEntity changeLoanStatus(LoanEntity loanEntity, LoanStatus status){
        loanEntity = findLoanById(loanEntity.id);
        loanEntity.setStatus(status);
        entityManager.merge(loanEntity);
        return loanEntity;
    }

    public LoanEntity findActiveLoanByBookId(UUID bookId) {
        LoanEntity result = entityManager.createQuery
                        ("SELECT l FROM LoanEntity l WHERE l.book_id = :bookId AND l.status = :returned", LoanEntity.class)
                .setParameter("bookId", bookId)
                .setParameter("returned", LoanStatus.PENDING)
                .getSingleResult();
        return result;
    }

    public List<LoanEntity> findActiveLoanByUserId(UUID user) {
        return entityManager.createQuery
                        ("SELECT l FROM LoanEntity l WHERE l.user_id = :user AND l.status != :returned", LoanEntity.class)
                .setParameter("user", user)
                .setParameter("returned", LoanStatus.RETURNED)
                .getResultList();
    }
}
