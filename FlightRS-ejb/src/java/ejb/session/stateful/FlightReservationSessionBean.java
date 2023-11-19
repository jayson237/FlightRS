/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatefulEjbClass.java to edit this template
 */
package ejb.session.stateful;

import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.SeatInventorySessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Passenger;
import entity.SeatInventory;
import entity.Transaction;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FlightReservationExistException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.SeatBookedException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jayso
 */
@Stateful
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBean;

    @EJB
    private SeatInventorySessionBeanLocal seatInventorySessionBean;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public long createNewReservation(FlightReservation reservation, List<Passenger> passengers, long flightScheduleId, long transactionId) throws TransactionNotFoundException, FlightReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatInventoryNotFoundException, SeatBookedException, InputDataValidationException {
        Set<ConstraintViolation<FlightReservation>> constraintViolations = validator.validate(reservation);

        if (constraintViolations.isEmpty()) {
            try {
                FlightSchedule flightSchedule = flightScheduleSessionBean.retrieveFlightScheduleById(flightScheduleId);

                Transaction transaction = transactionSessionBean.retrieveTransactionById(transactionId);

                SeatInventory seat = null;
                for (SeatInventory seats : flightSchedule.getSeatInventory()) {
                    if (seats.getCabin().getType() == reservation.getCabinClassType()) {
                        seat = seats;
                    }
                }
                if (seat == null) {
                    throw new SeatInventoryNotFoundException("Seat Inventory for specified cabin class not found");
                }

                em.persist(reservation);

                for (Passenger passenger : passengers) {
                    em.persist(passenger);
                    reservation.getPassengers().add(passenger);
                    seatInventorySessionBean.bookSeat(seat.getSeatInventoryId(), passenger.getSeatNumber());
                }

                flightSchedule.getFlightReservations().add(reservation);
                reservation.setFlightSchedule(flightSchedule);

                reservation.setTransaction(transaction);
                transaction.getFlightReservations().add(reservation);

                em.flush();

                return reservation.getFlightReservationId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightReservationExistException(" Flight Reservation already exist");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    public FlightReservation retrieveReservationById(long fsId) throws FlightReservationNotFoundException {
        FlightReservation fr = em.find(FlightReservation.class, fsId);

        if (fr != null) {
            return fr;
        } else {
            throw new FlightReservationNotFoundException("Reservation does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightReservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
