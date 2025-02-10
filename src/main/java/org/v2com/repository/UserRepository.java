package org.v2com.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.v2com.Enums.UserStatus;
import org.v2com.entity.UserEntity;

import java.util.List;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class UserRepository {

    @Inject
    EntityManager entityManager;

    public UserEntity findByName(String name) {
        return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.name = :username", UserEntity.class)
                .setParameter("username", name)
                .getSingleResult();
    }

    public UserEntity findById(UUID id){
        return entityManager.find(UserEntity.class, id);
    }

    public List<UserEntity> getAllUsers(){
        return entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    public void persist(UserEntity userEntity){
        entityManager.persist(userEntity);
    }

    public void deleteUserById(UUID id){
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if(userEntity != null){
            entityManager.remove(userEntity);
        }
    }

    public UserEntity changeUserStatus(UserEntity userEntity, UserStatus status){
        userEntity = findById(userEntity.id);
        userEntity.setStatus(status);
        entityManager.merge(userEntity);
        return userEntity;
    }
}
