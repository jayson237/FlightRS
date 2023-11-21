/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;
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
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan plan, List<Fare> fares, Long flightId, Pair<Date, Double> pair, int recurrent) throws FlightNotFoundException, InputDataValidationException, FareExistException, FlightScheduleExistException, UnknownPersistenceException, FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewFlightSchedulePlanWeekly(FlightSchedulePlan plan, List<Fare> fares, Long flightId, Pair<Date, Double> pair, int recurrent) throws FlightNotFoundException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewFlightSchedulePlanMultiple(FlightSchedulePlan plan, List<Fare> fares, Long flightId, List<Pair<Date, Double>> info) throws FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewReturnFlightSchedulePlanMultiple(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, List<Pair<Date, Double>> info) throws FareNotFoundException, FlightSchedulePlanExistException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException;

//    public FlightSchedulePlan createNewReturnFlightSchedulePlanWeekly(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, Pair<Date, Double> pair, int recurrent) throws FareNotFoundException, FlightSchedulePlanNotFoundException, FlightNotFoundException, FlightScheduleExistException, InputDataValidationException, FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException, GeneralException;

    public FlightSchedulePlan createNewReturnFlightSchedulePlan(FlightSchedulePlan returnPlan, FlightSchedulePlan plan, Long flightId, Pair<Date, Double> pair, int recurrent) throws FareNotFoundException, FlightNotFoundException, FlightSchedulePlanNotFoundException, InputDataValidationException, FareExistException, FlightScheduleExistException, UnknownPersistenceException, FlightSchedulePlanExistException, GeneralException;

    public List<FlightSchedulePlan> retrieveFlightSchedulePlanByFlightId(Long flightId) throws FlightNotFoundException;

    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long planId) throws FlightSchedulePlanNotFoundException;

}
