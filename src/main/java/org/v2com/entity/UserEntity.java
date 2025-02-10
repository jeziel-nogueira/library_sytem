package org.v2com.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.v2com.Enums.UserStatus;

import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@Entity
@Table(name = "users")
@Transactional(REQUIRED)
public class UserEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    public UUID id;

    @NotNull
    @Size(min = 3, max = 100)
    public String name;

    @NotNull
    @Size(min = 3, max = 100)
    public String email;

    @Size(min = 3, max = 150)
    public String address;

    @Size(min = 8, max = 20)
    public String phone;

    @Column(name = "status", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    public UserStatus Status;

    @NotNull
    public String password;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }


    public UserStatus getStatus() { return Status;}

    @Transactional
    public void setStatus(UserStatus status) { Status = status;}

    public UUID getId() { return id; }

    @Transactional
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    @Transactional
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    @Transactional
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }

    @Transactional
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }

    @Transactional
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }

    @Transactional
    public void setPassword(String password) { this.password = password; }

}
