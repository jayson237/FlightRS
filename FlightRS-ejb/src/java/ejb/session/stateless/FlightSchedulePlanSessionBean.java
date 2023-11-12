/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FlightNotFoundException;

/**
 *
 * @author jayso
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

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
    
   
//    @Override
//    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan plan, Long flightId) {
//        
//    }

    @Override
    public List<FlightSchedulePlan> retrieveFlightScheduleByFlightId(Long flightId) throws FlightNotFoundException {
        Flight flight = flightSessionBean.retrieveFlightById(flightId);
        if (flight != null) {
            Query q = em.createQuery("SELECT fsp FROM FlightSchedulePlan fsp WHERE fsp.flight = :flight");
            q.setParameter("flight", flight);
            return q.getResultList();
        } else {
            throw new FlightNotFoundException("Flight id: " + flightId + " cannot be deleted because it does not exist");
        }
    }

}
