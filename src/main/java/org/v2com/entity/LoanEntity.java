package org.v2com.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.v2com.Enums.LoanStatus;

import java.time.LocalDate;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@Entity
@Table(name = "loans")
@Transactional(REQUIRED)
public class LoanEntity extends PanacheEntityBase {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    public UUID id;

    @Column(nullable = false)
    private UUID book_id;

    @Column(nullable = false)
    private UUID user_id;

    @Column(nullable = false)
    private LocalDate loan_date;

    @Column(nullable = false)
    private LocalDate due_date;

    @Column(name = "status", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getBook_id() {
        return book_id;
    }

    @Transactional
    public void setBook_id(UUID book_id) {
        this.book_id = book_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    @Transactional
    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public LocalDate getLoan_date() {
        return loan_date;
    }

    @Transactional
    public void setLoan_date(LocalDate loan_date) {
        this.loan_date = loan_date;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    @Transactional
    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    @Transactional
    public LoanStatus getStatus() {
        return status;
    }

    @Transactional
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
