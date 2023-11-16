/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanLocal {

    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBean;

    @EJB
    private CabinClassSessionBeanLocal cabinClassSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightScheduleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public FlightSchedule createFlightSchedules(FlightSchedule flightSchedule, Long planId) throws FlightNotFoundException, FlightSchedulePlanNotFoundException, FlightScheduleExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<FlightSchedule>> constraintViolations = validator.validate(flightSchedule);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(flightSchedule);
                List<CabinClass> cc = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanById(planId).getFlight().getAircraftConfiguration().getCabinClasses();
                for (CabinClass cabin : cc) {
                    try {
                        cabinClassSessionBean.createNewCabinClass(cabin);
                        flightSchedule.getCabinClasses().add(cabin);
                    } catch (Exception e) {
                        System.out.println("Cabin Class exist");
                    }

                }
                em.flush();
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

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedule>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
