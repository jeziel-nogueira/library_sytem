package org.v2com.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.v2com.Enums.BookStatus;
import org.v2com.Enums.ReserveStatus;
import org.v2com.Enums.UserStatus;
import org.v2com.dto.ReservationDTO;
import org.v2com.entity.BookEntity;
import org.v2com.entity.ReserveEntity;
import org.v2com.entity.UserEntity;
import org.v2com.repository.BookRepository;
import org.v2com.repository.LoanRepository;
import org.v2com.repository.ReserveRepository;
import org.v2com.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class ReserveService {

    @Inject
    ReserveRepository reserveRepository;

    @Inject
    BookRepository bookRepository;

    @Inject
    LoanRepository loanRepository;

    @Inject
    UserRepository userRepository;

    public List<ReservationDTO> getAllReservations(){
        return reserveRepository.getAllReservations().stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ReservationDTO getReserveById(UUID reserId){
        return ReservationDTO.fromEntity(reserveRepository.findActiveReserveById(reserId));
    }

    public ReservationDTO getActiveReserveByBookId(UUID bookId){
        return ReservationDTO.fromEntity(reserveRepository.findActiveReserveByBookId(bookId));
    }

    public List<ReservationDTO> getActiveReserveByUserId(UUID id){
        return reserveRepository.findActiveReservationsByUserId(id).stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional
    public ReservationDTO createReserve(UUID bookId, UUID userId) throws Exception {
        BookEntity bookEntity = bookRepository.findById(bookId);
        UserEntity userEntity = userRepository.findById(userId);

        if(reserveRepository.findActiveReserveByBookId(bookId) != null){
            throw new Exception("Livro possui já uma reserva.");
        }

        if (bookEntity == null) {
            throw new Exception("Livro não encontrado.");
        }
        if (bookEntity.getStatus() == BookStatus.AVAILABLE) {
            throw new Exception("Livro disponivel para empréstimo.");
        }
        if (userEntity == null) {
            throw new Exception("Usuário não encontrado.");
        }
        if (userEntity.getStatus() != UserStatus.ACTIVE) {
            throw new Exception("Usuario inativo.");
        }



        ReserveEntity reserveEntity = new ReserveEntity();
        reserveEntity.setBook_id(bookEntity.getId());
        reserveEntity.setUser_id(userId);
        reserveEntity.setReservation_date(LocalDate.now());
        reserveEntity.setStatus(ReserveStatus.PENDING);

        return ReservationDTO.fromEntity(reserveEntity);
    }

    @Transactional
    public ReservationDTO changeReserveStatus(UUID id, ReserveStatus status){
        return ReservationDTO.fromEntity(reserveRepository.changeReservationStatus(id, status));
    }
}
