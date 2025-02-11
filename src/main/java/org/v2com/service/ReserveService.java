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
import org.v2com.exceptions.*;
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
            throw new Exception(e.getMessage());
        }
    }

    public ReservationDTO getReserveById(UUID reserveId) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.findActiveReserveById(reserveId));
            if (reservationDTO == null) {
                throw new ReserveNotFoundException(reserveId.toString());
            }
            return reservationDTO;
        } catch (ReserveNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ReservationDTO getActiveReserveByBookId(UUID bookId) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.findActiveReserveByBookId(bookId));
            if (reservationDTO == null) {
                throw new ReserveNotFoundException("");
            }
            return reservationDTO;
        } catch (ReserveNotFoundException ex){
            throw ex;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ReservationDTO> getActiveReserveByUserId(UUID id) throws Exception {
        try {
            List<ReservationDTO> reservationDTOS = reserveRepository.findActiveReservationsByUserId(id).stream()
                                                    .map(ReservationDTO::fromEntity)
                                                    .collect(Collectors.toList());

            return reservationDTOS;
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Transactional
    public ReservationDTO createReserve(UUID bookId, UUID userId) throws Exception {
        try {
            BookEntity bookEntity = bookRepository.findById(bookId);
            UserEntity userEntity = userRepository.findById(userId);

            if (bookEntity == null) {
                throw new BookNotFoundException();
            }
            if (reserveRepository.findActiveReserveByBookId(bookId) != null ||
                    bookEntity.getStatus() == BookStatus.AVAILABLE
            ) {
                throw new BookUnavailableToReserveException(bookId.toString());
            }


            if (userEntity == null) {
                throw new UserNotFoundException(userId.toString());
            }
            if (userEntity.getStatus() != UserStatus.ACTIVE) {
                throw new UserInactiveException(userId.toString());
            }


            ReserveEntity reserveEntity = new ReserveEntity();
            reserveEntity.setBook_id(bookEntity.getId());
            reserveEntity.setUser_id(userId);
            reserveEntity.setReservation_date(LocalDate.now());
            reserveEntity.setStatus(ReserveStatus.PENDING);

            return ReservationDTO.fromEntity(reserveEntity);
        } catch (BookUnavailableToReserveException
                 | UserInactiveException
                 | UserNotFoundException
                 | BookNotFoundException ex) {
            throw ex;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public ReservationDTO changeReserveStatus(UUID id, ReserveStatus status) throws Exception {
        try {
            ReservationDTO reservationDTO = ReservationDTO.fromEntity(reserveRepository.changeReservationStatus(id, status));
            if (reservationDTO == null) {
                throw new ReserveNotFoundException(id.toString());
            }
            return reservationDTO;
        }catch (ReserveNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
