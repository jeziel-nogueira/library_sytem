package org.v2com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.v2com.Enums.LoanStatus;
import org.v2com.entity.LoanEntity;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private UUID id;
    private UUID bookId;
    private UUID userId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LoanStatus status;

    public static LoanDTO fromEntity(LoanEntity loanEntity) {
        return new LoanDTO(
                loanEntity.getId(),
                loanEntity.getBook_id(),
                loanEntity.getUser_id(),
                loanEntity.getLoan_date(),
                loanEntity.getDue_date(),
                loanEntity.getStatus()
        );
    }

    public LoanEntity toEntity() {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.id = this.id != null ? this.id : UUID.randomUUID();
        loanEntity.setBook_id(this.bookId);
        loanEntity.setUser_id(this.userId);
        loanEntity.setLoan_date(this.loanDate);
        loanEntity.setDue_date(this.dueDate);
        loanEntity.setStatus(this.status);
        return loanEntity;
    }
}
