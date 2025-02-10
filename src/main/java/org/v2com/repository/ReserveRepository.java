package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.v2com.Enums.ReserveStatus;
import org.v2com.entity.ReserveEntity;;

import java.util.List;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class ReserveRepository {
    @Inject
    EntityManager entityManager;

    public List<ReserveEntity> getAllReservations(){
        return entityManager.createQuery("SELECT r from ReserveEntity r", ReserveEntity.class)
                .getResultList();
    }

    public void persist(Object entity){
        entityManager.persist(entity);
    }

    public ReserveEntity findReservationById(UUID id){
        return entityManager.find(ReserveEntity.class, id);
    }

    public ReserveEntity changeReservationStatus(UUID id, ReserveStatus status){
        ReserveEntity reserveEntity = findReservationById(id);
        reserveEntity.setStatus(status);
        entityManager.merge(reserveEntity);
        return reserveEntity;
    }

    public ReserveEntity findActiveReserveById(UUID id){
        ReserveEntity result = entityManager.createQuery
                        ("SELECT r FROM ReserveEntity r WHERE r.id = :id", ReserveEntity.class)
                .setParameter("id", id)
                .getSingleResult();
        return result;
    }

    public ReserveEntity findActiveReserveByBookId(UUID bookId){
        try {
            return entityManager.createQuery
                            ("SELECT r FROM ReserveEntity r WHERE r.book_id = :bookId AND r.status = :status", ReserveEntity.class)
                    .setParameter("bookId", bookId)
                    .setParameter("status", ReserveStatus.PENDING)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<ReserveEntity> findActiveReservationsByUserId(UUID userId){
        return entityManager.createQuery
                        ("SELECT r FROM ReserveEntity r WHERE r.user_id = :userId AND r.status = :status", ReserveEntity.class)
                .setParameter("userId", userId)
                .setParameter("status", ReserveStatus.PENDING)
                .getResultList();
    }
}
