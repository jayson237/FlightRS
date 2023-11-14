/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;

import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightScheduleSessionBeanLocal {

    public FlightSchedule createFlightSchedules(FlightSchedule flightSchedule, Long planId) throws FlightNotFoundException, FlightSchedulePlanNotFoundException, FlightScheduleExistException, GeneralException, InputDataValidationException;

}
