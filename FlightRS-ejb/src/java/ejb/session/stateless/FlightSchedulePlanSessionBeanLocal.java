/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public List<FlightSchedulePlan> retrieveFlightScheduleByFlightId(Long flightId) throws FlightNotFoundException;
    
}
