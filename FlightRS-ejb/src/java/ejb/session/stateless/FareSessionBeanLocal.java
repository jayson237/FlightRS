/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Local;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author jayso
 */
@Local
public interface FareSessionBeanLocal {

    public Fare createNewFare(Fare fare, FlightSchedulePlan flightSchedulePlan) throws FareExistException, InputDataValidationException, GeneralException, UnknownPersistenceException;

    public void deleteFares(List<Fare> fares);

    public List<Fare> retrieveFareByPlanId(Long planId) throws FlightSchedulePlanNotFoundException, FareNotFoundException;
    
}
