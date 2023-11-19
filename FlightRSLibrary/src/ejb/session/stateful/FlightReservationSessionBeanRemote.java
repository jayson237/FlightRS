/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateful;

import entity.FlightReservation;
import entity.Passenger;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightReservationExistException;
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
@Remote
public interface FlightReservationSessionBeanRemote {

    public long createNewReservation(FlightReservation reservation, List<Passenger> passengers, long flightScheduleId, long transactionId) throws TransactionNotFoundException, FlightReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatInventoryNotFoundException, SeatBookedException, InputDataValidationException;

}
