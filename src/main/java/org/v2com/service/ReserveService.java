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

    public List<ReservationDTO> getAllReservations() throws Exception {
        try {
            return reserveRepository.getAllReservations().stream()
                    .map(ReservationDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Erro listar livros.");
        }
    }

    public ReservationDTO getReserveById(UUID reserId) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.findActiveReserveById(reserId));
            if (reservationDTO == null) {
                throw new Exception("Reserva não encontrada");
            }
            return reservationDTO;
        } catch (Exception e) {
            throw new Exception("Erro ao buscar por reserva");
        }
    }

    public ReservationDTO getActiveReserveByBookId(UUID bookId) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.findActiveReserveByBookId(bookId));
            if (reservationDTO == null) {
                throw new Exception("Erro ao buscar por reserva");
            }
            return reservationDTO;
        } catch (Exception e) {
            throw new Exception("Erro ao buscar por reserva");
        }
    }

    public List<ReservationDTO> getActiveReserveByUserId(UUID id) throws Exception {
        try {
            List<ReservationDTO> reservationDTOS = reserveRepository.findActiveReservationsByUserId(id).stream()
                                                    .map(ReservationDTO::fromEntity)
                                                    .collect(Collectors.toList());

            return reservationDTOS;
        }catch (Exception e) {
            throw new Exception("Erro ao Listar por reservas");
        }
    }


    @Transactional
    public ReservationDTO createReserve(UUID bookId, UUID userId) throws Exception {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            UserEntity userEntity = userRepository.findById(userId);

            if(reserveRepository.findActiveReserveByBookId(bookId) != null){
                throw new IllegalArgumentException("Livro possui já uma reserva.");
            }

            if (bookEntity == null) {
                throw new IllegalArgumentException("Livro não encontrado.");
            }
            if (bookEntity.getStatus() == BookStatus.AVAILABLE) {
                throw new IllegalArgumentException("Livro disponivel para empréstimo.");
            }
            if (userEntity == null) {
                throw new IllegalArgumentException("Usuário não encontrado.");
            }
            if (userEntity.getStatus() != UserStatus.ACTIVE) {
                throw new IllegalArgumentException("Usuario inativo.");
            }



            ReserveEntity reserveEntity = new ReserveEntity();
            reserveEntity.setBook_id(bookEntity.getId());
            reserveEntity.setUser_id(userId);
            reserveEntity.setReservation_date(LocalDate.now());
            reserveEntity.setStatus(ReserveStatus.PENDING);

            return ReservationDTO.fromEntity(reserveEntity);
        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar reserva.");
        }
    }

    @Transactional
    public ReservationDTO changeReserveStatus(UUID id, ReserveStatus status) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.changeReservationStatus(id, status));
            if (reservationDTO == null) {
                throw new IllegalArgumentException("Reserva não encontrada");
            }
            return reservationDTO;
        } catch (Exception e) {
            throw new Exception("");
        }
    }
}
