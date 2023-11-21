/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.enumeration.CabinClassType;
import util.exception.AirportNotFoundException;
import util.exception.CabinClassNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateFlightScheduleException;

/**
 *
 * @author jayso
 */
@Remote
public interface FlightScheduleSessionBeanRemote {

    public List<FlightSchedule> searchFlightAllUnmanaged(String origin, String destination, Date departureDate, CabinClassType cabin) throws AirportNotFoundException, FlightNotFoundException;

    public void deleteFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;

    public FlightSchedule updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;

    public List<FlightSchedule> searchFlightAll(String origin, String destination, Date departureDate, CabinClassType cabin) throws AirportNotFoundException, FlightNotFoundException;

    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;

    public Fare getSmallestFare(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, CabinClassNotFoundException;

    public List<Pair<FlightSchedule, FlightSchedule>> getConnectingFlightSchedules(String departure, String destination, Date date, CabinClassType cabin) throws FlightNotFoundException;

    public SeatInventory getSeatInventory(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException;

    public FlightSchedule createFlightSchedule(FlightSchedulePlan flightSchedulePlan, FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException, InputDataValidationException;

}
