package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    public void persist(LoanEntity entity) {
        entityManager.persist(entity);
    }

    public LoanEntity findLoanById(UUID id) {
        List<LoanEntity> loanEntities = entityManager.createQuery("SELECT l from LoanEntity l where l.id =:id", LoanEntity.class)
                .setParameter("id", id)
                .getResultList();
        return loanEntities.isEmpty() ? null : loanEntities.get(0);
    }

    public void update(@Valid LoanEntity entity) {
        entityManager.merge(entity);
        entityManager.flush();
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
        List<LoanEntity> result = entityManager.createQuery
                        ("SELECT l FROM LoanEntity l WHERE l.book_id = :bookId AND l.status = :returned", LoanEntity.class)
                .setParameter("bookId", bookId)
                .setParameter("returned", LoanStatus.PENDING)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<LoanEntity> findLoanByUserId(UUID user) {
        return entityManager.createQuery
                        ("SELECT l FROM LoanEntity l WHERE l.user_id = :user", LoanEntity.class)
                .setParameter("user", user)
                .getResultList();
    }
}
