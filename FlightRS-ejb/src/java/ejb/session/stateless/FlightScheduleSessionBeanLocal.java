/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;
import util.enumeration.CabinClassType;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.SeatInventoryNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public FlightSchedule createFlightSchedule(FlightSchedulePlan flightSchedulePlan, FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException, InputDataValidationException;

    public void deleteSchedule(List<FlightSchedule> flightSchedule);

    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;

    public List<FlightSchedule> searchFlightAll(String origin, String destination, Date departureDate, CabinClassType cabin) throws FlightNotFoundException;

    public SeatInventory getSeatInventoryUnmanaged(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException;

    public List<FlightSchedule> searchFlightAllUnmanaged(String origin, String destination, Date departureDate, CabinClassType cabin) throws FlightNotFoundException;

    public List<Pair<FlightSchedule, FlightSchedule>> getConnectingFlightSchedulesUnmanaged(String departure, String destination, Date date, CabinClassType cabin) throws FlightNotFoundException;

}
