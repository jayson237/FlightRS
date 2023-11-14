/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import java.util.List;
import java.util.Set;
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
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

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
    public FlightSchedulePlan createNewFlightSchedulePlan(List<FlightSchedule> flightSchedules, FlightSchedulePlan plan, Long flightId) throws FlightNotFoundException, FlightSchedulePlanNotFoundException, FlightScheduleExistException, FlightSchedulePlanExistException, InputDataValidationException, GeneralException {
        Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations = validator.validate(plan);
        if (constraintViolations.isEmpty()) {
            try {
                Flight flight = flightSessionBean.retrieveFlightById(flightId);
                for (FlightSchedule schedule : flightSchedules) {
                    flightScheduleSessionBean.createFlightSchedules(schedule, flightId);
                }
                em.persist(plan);
                plan.setFlight(flight);
                flight.getFlightSchedulePlans().add(plan);
                em.flush();
                return plan;
            } catch (FlightNotFoundException ex) {
                throw new FlightNotFoundException("Flight with id: " + flightId + " does not exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightSchedulePlanExistException("Flight schedule plan already exist");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }

        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedulePlan>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
