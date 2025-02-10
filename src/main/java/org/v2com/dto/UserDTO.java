package org.v2com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.v2com.Enums.UserStatus;
import org.v2com.entity.UserEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private UserStatus status;

    public static UserDTO fromEntity(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getAddress(),
                userEntity.getPhone(),
                userEntity.getStatus()
        );
    }

    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(this.id != null ? this.id : UUID.randomUUID());
        userEntity.setName(this.name);
        userEntity.setEmail(this.email);
        userEntity.setAddress(this.address);
        userEntity.setPhone(this.phone);
        userEntity.setStatus(this.status);
        return userEntity;
    }
}
