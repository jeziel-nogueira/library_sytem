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
    public List<UserDTO> listAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(UUID id) {
        UserEntity userEntity = userRepository.findById(id);
        if (userEntity == null){
            throw new IllegalArgumentException("Usuario não encontrado para o ID fornecido");
        }
        return UserDTO.fromEntity(userEntity);
    }

    public UserDTO finUserByName(String name){
        UserEntity user = userRepository.findByName(name);
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o nome fornecido.");
        }
        return UserDTO.fromEntity(user);
    }

    public UserDTO addUser(@Valid UserDTO userDTO) {
        UserEntity userEntity = userDTO.toEntity();
        userRepository.persist(userEntity);
        return UserDTO.fromEntity(userEntity);
    }

    @Transactional
    public UserDTO updateUser(@Valid UserDTO userDTO) {
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
    }

    public void deleteUserById(UUID id){
        UserEntity existingUser = userRepository.findById(id);

        if (existingUser == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o ID fornecido.");
        }
        userRepository.deleteUserById(id);
    }
}
