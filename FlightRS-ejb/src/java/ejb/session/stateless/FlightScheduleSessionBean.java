/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CabinClassType;
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
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private SeatInventorySessionBeanLocal seatsInventorySessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightScheduleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public FlightSchedule createFlightSchedule(FlightSchedulePlan flightSchedulePlan, FlightSchedule flightSchedule) throws FlightScheduleExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<FlightSchedule>> constraintViolations = validator.validate(flightSchedule);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(flightSchedule);

                flightSchedule.setFlightSchedulePlan(flightSchedulePlan);
                if (!flightSchedulePlan.getFlightSchedules().contains(flightSchedule)) {
                    flightSchedulePlan.getFlightSchedules().add(flightSchedule);
                }

                return flightSchedule;

            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightScheduleExistException("Flight schedule already exist");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public void deleteSchedule(List<FlightSchedule> flightSchedule) {

        for (FlightSchedule sched : flightSchedule) {
            seatsInventorySessionBean.deleteSeatInventory(sched.getSeatInventory());
            em.remove(sched);
        }
    }

    @Override
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = em.find(FlightSchedule.class, flightScheduleID);

        if (schedule != null) {
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");
        }

    }

    @Override
    public FlightSchedule updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        FlightSchedule flightSchedule = retrieveFlightScheduleById(flightScheduleId);

        for (FlightSchedulePlan fsp : flightSchedule.getFlightSchedulePlan().getFlight().getFlightSchedulePlans()) {
            for (FlightSchedule fs : fsp.getFlightSchedules()) {
                if (fs.getFlightScheduleId() == flightScheduleId) {
                    continue;
                }
                Date start1 = fs.getDepartureDateTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start1);
                double duration = fs.getEstimatedDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, min);
                Date end1 = calendar.getTime();

                Calendar c = Calendar.getInstance();
                c.setTime(newDepartureDateTime);
                double duration2 = newFlightDuration;
                int hour2 = (int) duration2;
                int min2 = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hour2);
                c.add(Calendar.MINUTE, min2);
                Date end2 = c.getTime();

                if (start1.before(end2) && newDepartureDateTime.before(end1)) {
                    throw new UpdateFlightScheduleException("Updated fight schedule conflicts with existing flight schedules");
                }
            }
        }

        flightSchedule.setDepartureDateTime(newDepartureDateTime);
        flightSchedule.setEstimatedDuration(newFlightDuration);
        em.flush();
        return flightSchedule;
    }

    @Override
    public void deleteFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        FlightSchedule flightSchedule = retrieveFlightScheduleById(flightScheduleId);
        if (!flightSchedule.getFlightReservations().isEmpty()) {
            throw new UpdateFlightScheduleException("Ticket has already been issued for this flight schedule, unable to delete");
        } else {
            flightSchedule.getFlightSchedulePlan().getFlightSchedules().remove(flightSchedule);
            for (SeatInventory seats : flightSchedule.getSeatInventory()) {
                em.remove(seats);
            }
            em.remove(flightSchedule);
        }
    }
    
    @Override
    public List<FlightSchedule> searchFlightAll(String origin, String destination, Date departureDate, CabinClassType cabin) throws FlightNotFoundException {
        List<FlightSchedule> schedules = new ArrayList<>();
        List<Flight> flights = flightSessionBean.retrieveAllFlightByRoute(origin, destination);

        for (Flight flight: flights) {
            for (FlightSchedulePlan flightSchedulePlan: flight.getFlightSchedulePlans()) {
                if (flightSchedulePlan.isDisabled()) {
                    continue;
                } 
                for (FlightSchedule flightSchedule: flightSchedulePlan.getFlightSchedules()) {
                    boolean add = false;
                    if (cabin == null) {
                        add = true;
                    } else {
                        for (SeatInventory seatInventory: flightSchedule.getSeatInventory()) {
                            if (seatInventory.getCabin().getType().equals(cabin)) {
                                add = true;
                            }
                        }
                    }
                    
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(flightSchedule.getDepartureDateTime());
                    c2.setTime(departureDate);
                    boolean isSameDay = c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) &&
                                      c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);       
                    if (!isSameDay){
                        add = false;
                    }
                    if (add) {
                        schedules.add(flightSchedule);
                    }
                }
            } 
        }       
        Collections.sort(schedules, new FlightSchedule.FlightScheduleComparator());
        return schedules;
    }
    
    public List<FlightSchedule> searchFlightAllUnmanaged(String origin, String destination, Date departureDate, CabinClassType cabin) throws FlightNotFoundException {
        List<FlightSchedule> schedules = new ArrayList<>();
        List<Flight> flights = flightSessionBean.retrieveAllFlightByRoute(origin, destination);

        for (Flight flight: flights) {
            for (FlightSchedulePlan flightSchedulePlan: flight.getFlightSchedulePlans()) {
                if (flightSchedulePlan.isDisabled()) {
                    continue;
                } 
                for (FlightSchedule flightSchedule: flightSchedulePlan.getFlightSchedules()) {
                    boolean add = false;
                    if (cabin == null) {
                        add = true;
                    } else {
                        for (SeatInventory seatInventory: flightSchedule.getSeatInventory()) {
                            if (seatInventory.getCabin().getType().equals(cabin)) {
                                add = true;
                            }
                        }
                    }
                    
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(flightSchedule.getDepartureDateTime());
                    c2.setTime(departureDate);
                    boolean isSameDay = c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) &&
                                      c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);       
                    if (!isSameDay){
                        add = false;
                    }
                    if (add) {
                        em.detach(flightSchedule);
                        schedules.add(flightSchedule);
                    }
                }
            } 
        }       
        Collections.sort(schedules, new FlightSchedule.FlightScheduleComparator());
        return schedules;
    }
    
    
    @Override
    public List<Pair<FlightSchedule, FlightSchedule>> getConnectingFlightSchedules(String departure, String destination, Date date, CabinClassType cabin) throws FlightNotFoundException {
        List<Pair<FlightSchedule, FlightSchedule>> schedule = new ArrayList<>();
        List<Flight[]> flight = flightSessionBean.retrieveAllIndirectFlightByFlightRoute(departure, destination);
        
        for (Object[] pair: flight) {
            Flight firstFlight = (Flight) pair[0];
            Flight secondFlight = (Flight) pair[1];
            for (FlightSchedulePlan flightSchedulePlan: firstFlight.getFlightSchedulePlans()) {
                if (flightSchedulePlan.isDisabled()) {
                    continue;
                }
                for (FlightSchedule flightSchedule: flightSchedulePlan.getFlightSchedules()) {
                    for (FlightSchedulePlan flightSchedulePlan2: secondFlight.getFlightSchedulePlans()) {
                        if (flightSchedulePlan2.isDisabled()) {
                            continue;
                        }
                        for (FlightSchedule flightSchedule2: flightSchedulePlan2.getFlightSchedules()) {
                            boolean add = false;
                            if (cabin == null) {
                                add = true;
                            } else {
                                for (SeatInventory seatInventory: flightSchedule.getSeatInventory()) {
                                    for (SeatInventory seatInventory2: flightSchedule2.getSeatInventory()) {
                                        if (seatInventory.getCabin().getType().equals(cabin) && seatInventory2.getCabin().getType().equals(cabin)) {
                                        add = true;
                                        }
                                    }                           
                                }
                            }
                            
                            Calendar ca = Calendar.getInstance();
                            Calendar cc = Calendar.getInstance();
                            ca.setTime(flightSchedule.getDepartureDateTime());
                            cc.setTime(date);
                            boolean sameDay = ca.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) &&
                                              ca.get(Calendar.YEAR) == cc.get(Calendar.YEAR);       
                            if (!sameDay){
                                add = false;
                            }
                            
                            Calendar c = Calendar.getInstance();
                            c.setTime(flightSchedule.getDepartureDateTime());
                            double duration = flightSchedule.getEstimatedDuration();                
                            int hour = (int) duration;
                            int min = (int) (duration % 1 * 60);
                            c.add(Calendar.HOUR_OF_DAY, hour);
                            c.add(Calendar.MINUTE, min);               
                            
                            Calendar c2 = Calendar.getInstance();
                            c2.setTime(flightSchedule2.getDepartureDateTime());
                            long gap = Duration.between(c.toInstant(), c2.toInstant()).toHours();
                            if (gap < 2l || gap > 12l) {
                                add = false;
                            }
                            
                            if (add) {
                                schedule.add(new Pair(flightSchedule, flightSchedule2));
                            }
                            
                        }
                    }
                }
            }
        }
        Collections.sort(schedule, new FlightSchedule.ConnectingFlightScheduleComparator());
        return schedule;
    }
    
    @Override
    public List<Pair<FlightSchedule, FlightSchedule>> getConnectingFlightSchedulesUnmanaged(String departure, String destination, Date date, CabinClassType cabin) throws FlightNotFoundException {
        List<Pair<FlightSchedule, FlightSchedule>> schedule = new ArrayList<>();
        List<Flight[]> flight = flightSessionBean.retrieveAllIndirectFlightByFlightRoute(departure, destination);
        
        for (Object[] pair: flight) {
            Flight firstFlight = (Flight) pair[0];
            Flight secondFlight = (Flight) pair[1];
            for (FlightSchedulePlan flightSchedulePlan: firstFlight.getFlightSchedulePlans()) {
                if (flightSchedulePlan.isDisabled()) {
                    continue;
                }
                for (FlightSchedule flightSchedule: flightSchedulePlan.getFlightSchedules()) {
                    for (FlightSchedulePlan flightSchedulePlan2: secondFlight.getFlightSchedulePlans()) {
                        if (flightSchedulePlan2.isDisabled()) {
                            continue;
                        }
                        for (FlightSchedule flightSchedule2: flightSchedulePlan2.getFlightSchedules()) {
                            boolean add = false;
                            if (cabin == null) {
                                add = true;
                            } else {
                                for (SeatInventory seatInventory: flightSchedule.getSeatInventory()) {
                                    for (SeatInventory seatInventory2: flightSchedule2.getSeatInventory()) {
                                        if (seatInventory.getCabin().getType().equals(cabin) && seatInventory2.getCabin().getType().equals(cabin)) {
                                        add = true;
                                        }
                                    }                           
                                }
                            }
                            
                            Calendar ca = Calendar.getInstance();
                            Calendar cc = Calendar.getInstance();
                            ca.setTime(flightSchedule.getDepartureDateTime());
                            cc.setTime(date);
                            boolean sameDay = ca.get(Calendar.DAY_OF_YEAR) == cc.get(Calendar.DAY_OF_YEAR) &&
                                              ca.get(Calendar.YEAR) == cc.get(Calendar.YEAR);       
                            if (!sameDay){
                                add = false;
                            }
                            
                            Calendar c = Calendar.getInstance();
                            c.setTime(flightSchedule.getDepartureDateTime());
                            double duration = flightSchedule.getEstimatedDuration();          
                            int hour = (int) duration;
                            int min = (int) (duration % 1 * 60);
                            c.add(Calendar.HOUR_OF_DAY, hour);
                            c.add(Calendar.MINUTE, min);               
                            
                            Calendar c2 = Calendar.getInstance();
                            c2.setTime(flightSchedule2.getDepartureDateTime());
                            long gap = Duration.between(c.toInstant(), c2.toInstant()).toHours();
                            if (gap < 2l || gap > 12l) {
                                add = false;
                            }
                            
                            if (add) {
                                em.detach(flightSchedule);
                                em.detach(flightSchedule2);
                                schedule.add(new Pair(flightSchedule, flightSchedule2));
                            }
                            
                        }
                    }
                }
            }
        }
        Collections.sort(schedule, new FlightSchedule.ConnectingFlightScheduleComparator());
        return schedule;
    }
    
    
    
    @Override
    public Fare getSmallestFare(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, CabinClassNotFoundException {
        FlightSchedule resultFlightSchedule = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        List<Fare> fares = resultFlightSchedule.getFlightSchedulePlan().getFares();
        List<Fare> ccfares = new ArrayList<>();
        for (Fare fare: fares) {
            if (fare.getCabinClassType().equals(cabinClassType)) {
                ccfares.add(fare);
            }
        }
        if (ccfares.isEmpty()) {
            throw new CabinClassNotFoundException("Cabin class " + cabinClassType + " not found");
        }
        Fare smallest = ccfares.get(0);
        for (Fare fare : ccfares) {
            if(fare.getAmount().compareTo(smallest.getAmount()) < 0) {
                smallest = fare;
            }
        }
        return smallest;
    }
    
    @Override
    public SeatInventory getSeatInventory(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException {
        FlightSchedule flightScheduleResult = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        for (SeatInventory seat: flightScheduleResult.getSeatInventory()) {
            if (seat.getCabin().getType() == cabinClassType) {
                return seat;
            }
        }
        throw new SeatInventoryNotFoundException("Seat Inventory Not Found");
    }
    
    @Override
    public SeatInventory getSeatInventoryUnmanaged(FlightSchedule flightSchedule, CabinClassType cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException {
        FlightSchedule flightScheduleResult = retrieveFlightScheduleById(flightSchedule.getFlightScheduleId());
        for (SeatInventory seat: flightScheduleResult.getSeatInventory()) {
            if (seat.getCabin().getType() == cabinClassType) {
                em.detach(seat);
                return seat;
            }
        }
        throw new SeatInventoryNotFoundException("No such seat inventory");
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedule>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
