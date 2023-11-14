/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {

    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long planId) throws FlightSchedulePlanNotFoundException;

    public FlightSchedulePlan createNewFlightSchedulePlan(List<FlightSchedule> flightSchedules, FlightSchedulePlan plan, Long flightId) throws FlightNotFoundException, FlightSchedulePlanNotFoundException, FlightScheduleExistException, FlightSchedulePlanExistException, InputDataValidationException, GeneralException;
}
