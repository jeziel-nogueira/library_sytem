package org.v2com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.v2com.Enums.ReserveStatus;
import org.v2com.entity.ReserveEntity;

import java.time.LocalDate;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private UUID id;
    public UUID user_id;
    public UUID book_id;
    public LocalDate reservation_date;
    public ReserveStatus status;

    public static ReservationDTO fromEntity(ReserveEntity reserveEntity){
        return new ReservationDTO(
                reserveEntity.getId(),
                reserveEntity.getBook_id(),
                reserveEntity.getUser_id(),
                reserveEntity.getReservation_date(),
                reserveEntity.getStatus()
        );
    }

    public ReserveEntity toEntity(){
        ReserveEntity reserveEntity = new ReserveEntity();
        reserveEntity.id = this.id != null ? this.id : UUID.randomUUID();
        reserveEntity.user_id = this.user_id;
        reserveEntity.book_id = this.book_id;
        reserveEntity.reservation_date = this.reservation_date;
        reserveEntity.status = this.status;
        return reserveEntity;
    }
}
