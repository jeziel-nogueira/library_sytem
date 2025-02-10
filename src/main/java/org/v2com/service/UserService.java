package org.v2com.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.v2com.Enums.UserStatus;
import org.v2com.dto.UserDTO;
import org.v2com.entity.UserEntity;
import org.v2com.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class UserService {

    @Inject
    UserRepository userRepository;

    @Transactional
    public List<UserDTO> listAllUsers() throws Exception {
        try {
            return userRepository.getAllUsers().stream()
                    .map(UserDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro ao listar usuarios");
        }
    }

    public UserDTO findUserById(UUID id) throws Exception {
        try {
            UserEntity userEntity = userRepository.findById(id);
            if (userEntity == null){
                throw new Exception("Usuario não encontrado para o ID fornecido");
            }
            return UserDTO.fromEntity(userEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao encontrar um usuario");
        }
    }

    public UserDTO finUserByName(String name) throws Exception {
        try {
            UserEntity user = userRepository.findByName(name);
            if (user == null) {
                throw new Exception("Usuário não encontrado para o nome fornecido.");
            }
            return UserDTO.fromEntity(user);
        } catch (Exception e) {
            throw new Exception("Erro ao encontrar um usuario");
        }
    }

    public UserDTO addUser(@Valid UserDTO userDTO) throws Exception {
        try {
            UserEntity userEntity = userDTO.toEntity();
            userRepository.persist(userEntity);
            return UserDTO.fromEntity(userEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao adicionar um usuario");
        }
    }

    @Transactional
    public UserDTO updateUser(@Valid UserDTO userDTO) throws Exception {
        try {
            UserEntity existingUser = userRepository.findById(userDTO.getId());

            if (existingUser == null) {
                throw new IllegalArgumentException("Usuário não encontrado para o ID fornecido.");
            }

            existingUser.setName(userDTO.getName());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setEmail(userDTO.getEmail());
            existingUser.setPhone(userDTO.getPhone());
            existingUser.setStatus(userDTO.getStatus());

            userRepository.persist(existingUser);
            return UserDTO.fromEntity(existingUser);
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar usuario.");
        }
    }

    public void deleteUserById(UUID id){
        UserEntity existingUser = userRepository.findById(id);

        if (existingUser == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o ID fornecido.");
        }
        userRepository.deleteUserById(id);
    }
}
