/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteFlightSchedulePlanException;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jayso
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @EJB
    private SeatInventorySessionBeanLocal seatsInventorySessionBean;

    @EJB
    private FareSessionBeanLocal fareSessionBean;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightSchedulePlanSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan plan, List<Fare> fares, Long flightId, Pair<Date, Double> pair, int recurrent) throws FlightNotFoundException, InputDataValidationException, FareExistException, FlightScheduleExistException, UnknownPersistenceException, FlightSchedulePlanExistException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(plan);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(plan);
                if (recurrent == 0) {
                    FlightSchedule schedule = new FlightSchedule(pair.getKey(), pair.getValue(), calculateEndDate(pair.getKey(), pair.getValue()));
                    FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(plan, schedule);
                    for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                    }
                } else {
                    Date presentDate = pair.getKey();
                    Date endDate = plan.getRecurrentEndDate();

                    while (endDate.compareTo(presentDate) > 0) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(presentDate);

                        FlightSchedule schedule = new FlightSchedule(c.getTime(), pair.getValue(), calculateEndDate(c.getTime(), pair.getValue()));
                        FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(plan, schedule);
                        for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                            SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                            seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                        }
                        c.add(Calendar.DAY_OF_MONTH, recurrent);
                        presentDate = c.getTime();
                    }
                }

                associateFlightToPlan(flightId, plan);

                for (Fare fare : fares) {
                    fareSessionBean.createNewFare(fare, plan);
                }

                em.flush();
                return plan;
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight does not exist");
            } catch (FlightSchedulePlanExistException ex) {
                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
            } catch (FareExistException ex) {
                throw new FareExistException("Fare already exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightSchedulePlanExistException("Flight schedule plan already exists");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public FlightSchedulePlan createNewReturnFlightSchedulePlan(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, Pair<Date, Double> pair, int recurrent) throws FareNotFoundException, FlightNotFoundException, FlightSchedulePlanNotFoundException, InputDataValidationException, FareExistException, FlightScheduleExistException, UnknownPersistenceException, FlightSchedulePlanExistException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(returnPlan);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(returnPlan);
                if (recurrent == 0) {
                    FlightSchedule schedule = new FlightSchedule(pair.getKey(), pair.getValue(), calculateEndDate(pair.getKey(), pair.getValue()));
                    FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(returnPlan, schedule);
                    for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                    }
                } else {
                    Date presentDate = pair.getKey();
                    Date endDate = plan.getRecurrentEndDate();

                    while (endDate.compareTo(presentDate) > 0) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(presentDate);

                        FlightSchedule schedule = new FlightSchedule(c.getTime(), pair.getValue(), calculateEndDate(c.getTime(), pair.getValue()));
                        FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(plan, schedule);
                        for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                            SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                            seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                        }
                        c.add(Calendar.DAY_OF_MONTH, recurrent);
                        presentDate = c.getTime();
                    }
                }

                associateFlightToPlan(flightId, returnPlan);

                List<Fare> returnFares = fareSessionBean.retrieveFareByPlanId(plan.getFlightSchedulePlanId());
                for (Fare fare : returnFares) {
                    returnPlan.getFares().add(fare);
                }

                em.flush();
                return plan;
            } catch (FlightSchedulePlanNotFoundException ex) {
                throw new FlightSchedulePlanExistException("Flight schedule not found");
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight does not exist");
            } catch (FlightSchedulePlanExistException ex) {
                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightSchedulePlanExistException("Flight already exists");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public FlightSchedulePlan createNewFlightSchedulePlanMultiple(FlightSchedulePlan plan, List<Fare> fares, Long flightId, List<Pair<Date, Double>> info) throws FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(plan);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(plan);
                int size = info.size();
                for (int i = 0; i < size; i++) {
                    FlightSchedule schedule = new FlightSchedule(info.get(i).getKey(), info.get(i).getValue(), calculateEndDate(info.get(i).getKey(), info.get(i).getValue()));
                    FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(plan, schedule);
                    for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                    }
                }

                associateFlightToPlan(flightId, plan);

                for (Fare fare : fares) {
                    fareSessionBean.createNewFare(fare, plan);
                }

                em.flush();
                return plan;
            } catch (FlightScheduleExistException ex) {
                throw new FlightScheduleExistException("Flight schedule already exist");
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight does not exist");
            } catch (FlightSchedulePlanExistException ex) {
                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
            } catch (FareExistException ex) {
                throw new FareExistException("Fare already exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightSchedulePlanExistException();
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

    @Override
    public FlightSchedulePlan createNewReturnFlightSchedulePlanMultiple(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, List<Pair<Date, Double>> info) throws FareNotFoundException, FlightSchedulePlanExistException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(returnPlan);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(returnPlan);
                int size = info.size();
                List<CabinClass> cabinClasses = plan.getFlight().getAircraftConfiguration().getCabinClasses();

                for (int i = 0; i < size; i++) {
                    FlightSchedule schedule = new FlightSchedule(info.get(i).getKey(), info.get(i).getValue(), calculateEndDate(info.get(i).getKey(), info.get(i).getValue()));
                    FlightSchedule newSchedule = flightScheduleSessionBean.createFlightSchedule(returnPlan, schedule);

                    for (CabinClass cc : cabinClasses) {
                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                        seatsInventorySessionBean.createSeatInventory(seats, newSchedule, cc);
                    }
                }

                associateFlightToPlan(flightId, returnPlan);

                List<Fare> returnFares = fareSessionBean.retrieveFareByPlanId(plan.getFlightSchedulePlanId());
                for (Fare fare : returnFares) {
                    returnPlan.getFares().add(fare);
                }

                em.flush();
                return returnPlan;

            } catch (FlightSchedulePlanNotFoundException ex) {
                throw new FlightSchedulePlanExistException("Flight schedule not found");
            } catch (FlightScheduleExistException ex) {
                throw new FlightScheduleExistException("Flight schedule already exist");
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight does not exist");
            } catch (FlightSchedulePlanExistException ex) {
                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightSchedulePlanExistException();
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

    @Override
    public FlightSchedulePlan createNewFlightSchedulePlanWeekly(FlightSchedulePlan plan, List<Fare> fares, Long flightId, Pair<Date, Double> pair, int recurrent) throws FlightNotFoundException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(plan);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(plan);
                Date presentDate = pair.getKey();
                Date endDate = plan.getRecurrentEndDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(presentDate);
                FlightSchedule schedule;
                FlightSchedule fs;

                while (cal.get(Calendar.DAY_OF_WEEK) != recurrent) {
                    cal.add(Calendar.DATE, 1);
                }

                cal.add(Calendar.DAY_OF_MONTH, -1);

                while (endDate.compareTo(cal.getTime()) >= 0) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    schedule = new FlightSchedule(cal.getTime(), pair.getValue(), calculateEndDate(cal.getTime(), pair.getValue()));
                    fs = flightScheduleSessionBean.createFlightSchedule(plan, schedule);
                    for (CabinClass cc : plan.getFlight().getAircraftConfiguration().getCabinClasses()) {
                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
                    }
                    cal.add(Calendar.DAY_OF_MONTH, 6);
                }

                associateFlightToPlan(flightId, plan);

                for (Fare fare : fares) {
                    fareSessionBean.createNewFare(fare, plan);
                }

                em.flush();
                return plan;
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight does not exist");
            } catch (FlightSchedulePlanExistException ex) {
                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
            } catch (FareExistException ex) {
                throw new FareExistException("Fare already exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FlightSchedulePlanExistException();
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

//    @Override
//    public FlightSchedulePlan createNewReturnFlightSchedulePlanWeekly(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, Pair<Date, Double> pair, int recurrent) throws FareNotFoundException, FlightSchedulePlanNotFoundException, FlightNotFoundException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException {
//        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(returnPlan);
//        if (constraintViolations.isEmpty()) {
//            try {
//                em.persist(returnPlan);
//                Date presentDate = pair.getKey();
//                Date endDate = returnPlan.getRecurrentEndDate();
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(presentDate);
//                List<CabinClass> cabinClasses = plan.getFlight().getAircraftConfiguration().getCabinClasses();
//                FlightSchedule schedule = new FlightSchedule(cal.getTime(), pair.getValue(), calculateEndDate(cal.getTime(), pair.getValue()));
//                FlightSchedule fs = flightScheduleSessionBean.createFlightSchedule(returnPlan, schedule);
//                for (CabinClass cc : cabinClasses) {
//                    SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
//                    seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
//                }
//
//                boolean condition = false;
//                while (cal.get(Calendar.DAY_OF_WEEK) != recurrent) {
//                    cal.add(Calendar.DATE, 1);
//                    condition = true;
//                }
//
//                if (condition) {
//                    schedule = new FlightSchedule(cal.getTime(), pair.getValue(), calculateEndDate(cal.getTime(), pair.getValue()));
//                    fs = flightScheduleSessionBean.createFlightSchedule(returnPlan, schedule);
//                    for (CabinClass cc : cabinClasses) {
//                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
//                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
//                    }
//                }
//
//                cal.add(Calendar.DAY_OF_MONTH, 7);
//
//                while (endDate.compareTo(cal.getTime()) > 0) {
//                    schedule = new FlightSchedule(cal.getTime(), pair.getValue(), calculateEndDate(cal.getTime(), pair.getValue()));
//                    fs = flightScheduleSessionBean.createFlightSchedule(returnPlan, schedule);
//                    for (CabinClass cc : cabinClasses) {
//                        SeatInventory seats = new SeatInventory(cc.getMaxCapacity(), 0, cc.getMaxCapacity());
//                        seatsInventorySessionBean.createSeatInventory(seats, fs, cc);
//                    }
//                    cal.add(Calendar.DAY_OF_MONTH, 7);
//                }
//
//                associateFlightToPlan(flightId, returnPlan);
//
//                List<Fare> returnFares = fareSessionBean.retrieveFareByPlanId(plan.getFlightSchedulePlanId());
//                for (Fare fare : returnFares) {
//                    returnPlan.getFares().add(fare);
//                }
//
//                em.flush();
//                return returnPlan;
//            } catch (FlightNotFoundException ex) {
//                throw new FlightNotFoundException("Flight does not exist");
//            } catch (FlightSchedulePlanExistException ex) {
//                throw new FlightSchedulePlanExistException("Such flight schedule plan already exist");
//            } catch (PersistenceException ex) {
//                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
//                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
//                        throw new FlightSchedulePlanExistException();
//                    } else {
//                        throw new UnknownPersistenceException(ex.getMessage());
//                    }
//                } else {
//                    throw new UnknownPersistenceException(ex.getMessage());
//                }
//            }
//        } else {
//            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
//        }
//    }
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlan() {
        Query q = em.createQuery("SELECT DISTINCT p FROM FlightSchedulePlan p JOIN p.flightSchedules fs WHERE p.isDisabled = false ORDER BY p.flightNumber ASC, fs.departureDateTime DESC");
        List<FlightSchedulePlan> result = q.getResultList();
        List<FlightSchedulePlan> sorted = new ArrayList<>();
        for (FlightSchedulePlan plan : result) {
            if (!sorted.contains(plan)) {
                sorted.add(plan);
            }

            if (plan.getFlight().getReturnFlightNumber() != null) {
                FlightSchedulePlan returnPlan = findReturnPlan(result, plan);
                if (returnPlan != null && !sorted.contains(returnPlan)) {
                    sorted.add(returnPlan);
                }
            }

        }
        return sorted;
    }

    private FlightSchedulePlan findReturnPlan(List<FlightSchedulePlan> plans, FlightSchedulePlan mainPlan) {
        for (FlightSchedulePlan plan : plans) {
            if (plan.getFlight().getReturnFlightNumber() != null
                    && plan.getFlight().getReturnFlightNumber().equals(mainPlan.getFlightNumber())
                    && plan.getFares() == mainPlan.getFares()) {
                return plan;
            }
        }
        return null;
    }

    @Override
    public boolean deleteFlightSchedulePlan(Long flightSchedulePlanID) throws FlightSchedulePlanNotFoundException, DeleteFlightSchedulePlanException {
        FlightSchedulePlan plan = retrieveFlightSchedulePlanById(flightSchedulePlanID);
        if (plan.getFlightSchedules().stream().allMatch(sched -> sched.getFlightReservations().isEmpty())) {
            flightScheduleSessionBean.deleteSchedule(plan.getFlightSchedules());
            plan.getFlight().getFlightSchedulePlans().remove(plan);
            fareSessionBean.deleteFares(plan.getFares());
            em.remove(plan);
            return true;
        } else {
            plan.setIsDisabled(true);
            throw new DeleteFlightSchedulePlanException("Flight Schedule Plan id: " + flightSchedulePlanID + " is in use and cannot be deleted! It will be disabled");
        }
    }

    @Override
    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long planId) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan plan = em.find(FlightSchedulePlan.class, planId);
        if (plan != null) {
            return plan;
        }
        throw new FlightSchedulePlanNotFoundException("Flight Schedule Plan with id: " + planId + " does not exist");
    }

    @Override
    public List<FlightSchedulePlan> retrieveFlightSchedulePlanByFlightNumber(String number) throws FlightNotFoundException, FlightSchedulePlanNotFoundException {
        Flight flight = flightSessionBean.retrieveFlightByNumber(number);
        if (flight != null) {
            Query q = em.createQuery("SELECT DISTINCT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight = :flight");
            q.setParameter("flight", flight);
            return q.getResultList();
        } else {
            throw new FlightSchedulePlanNotFoundException("There is no existing Flight Schedule Plan record related to " + number);
        }
    }

    @Override
    public List<FlightSchedulePlan> retrieveFlightSchedulePlanByFlightId(Long flightId) throws FlightNotFoundException {
        Flight flight = flightSessionBean.retrieveFlightById(flightId);
        if (flight != null) {
            Query q = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight = :flight");
            q.setParameter("flight", flight);
            return q.getResultList();
        } else {
            throw new FlightNotFoundException("Flight id: " + flightId + " cannot be deleted because it does not exist");
        }
    }

    private void associateFlightToPlan(Long flightId, FlightSchedulePlan flightSchedulePlan) throws FlightNotFoundException, FlightSchedulePlanExistException {

        Flight flight = flightSessionBean.retrieveFlightById(flightId);

        for (FlightSchedulePlan fsp : flight.getFlightSchedulePlans()) {
            for (FlightSchedule fs : fsp.getFlightSchedules()) {
                Date start1 = fs.getDepartureDateTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start1);
                double duration = fs.getEstimatedDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, min);
                Date end1 = calendar.getTime();
                for (FlightSchedule fs2 : flightSchedulePlan.getFlightSchedules()) {
                    Date start2 = fs2.getDepartureDateTime();
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(start2);
                    double duration2 = fs2.getEstimatedDuration();
                    int hour2 = (int) duration2;
                    int min2 = (int) (duration2 % 1 * 60);
                    calendar2.add(Calendar.HOUR_OF_DAY, hour2);
                    calendar2.add(Calendar.MINUTE, min2);
                    Date end2 = calendar2.getTime();

                    if (isOverlapping(start1, end1, start2, end2)) {
                        System.out.println("calling one");
                        throw new FlightSchedulePlanExistException("Flight schedule overlaps with existing flight schedules");
                    }
                }
            }
        }

        List<FlightSchedule> fs = flightSchedulePlan.getFlightSchedules();
        System.out.println("check: " + fs.size());
        for (int i = 0; i < fs.size(); i++) {
            Date start1 = fs.get(i).getDepartureDateTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start1);
            double duration = fs.get(i).getEstimatedDuration();
            int hour = (int) duration;
            int min = (int) (duration % 1 * 60);
            calendar.add(Calendar.HOUR_OF_DAY, hour);
            calendar.add(Calendar.MINUTE, min);
            Date end1 = calendar.getTime();
            for (int j = i + 1; j < fs.size(); j++) {
                Date start2 = fs.get(j).getDepartureDateTime();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(start2);
                double duration2 = fs.get(j).getEstimatedDuration();
                int hour2 = (int) duration2;
                int min2 = (int) (duration2 % 1 * 60);
                calendar2.add(Calendar.HOUR_OF_DAY, hour2);
                calendar2.add(Calendar.MINUTE, min2);
                Date end2 = calendar2.getTime();

                if (isOverlapping(start1, end1, start2, end2)) {
                    System.out.println("calling two");
                    throw new FlightSchedulePlanExistException("Flight schedule overlaps");
                }
            }
        }
        flight.getFlightSchedulePlans().add(flightSchedulePlan);
        flightSchedulePlan.setFlight(flight);
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    public Date calculateEndDate(Date startDate, double durationInHours) {
        long durationInMillis = (long) (durationInHours * 60 * 60 * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MILLISECOND, (int) durationInMillis);
        return calendar.getTime();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
