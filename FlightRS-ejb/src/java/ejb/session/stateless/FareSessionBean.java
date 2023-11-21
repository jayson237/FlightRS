/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFareException;

/**
 *
 * @author jayso
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FareSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Fare createNewFare(Fare fare, FlightSchedulePlan flightSchedulePlan) throws FareExistException, InputDataValidationException, GeneralException, UnknownPersistenceException {
        Set<ConstraintViolation<Fare>> constraintViolations = validator.validate(fare);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(fare);
                flightSchedulePlan.getFares().add(fare);
                fare.getFlightSchedulePlans().add(flightSchedulePlan);
                return fare;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    
                       
                        throw new FareExistException("Overlap in fare basis codes");
                    
                } else {
                    throw new FareExistException("Overlap in fare basis codes");
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private Fare retrieveFareById(Long fareID) throws FareNotFoundException {
        Fare fare = em.find(Fare.class, fareID);
        if (fare != null) {
            return fare;
        } else {
            throw new FareNotFoundException("Fare " + fareID + " not found!");
        }
    }

    @Override
    public Fare updateFare(long fareID, BigDecimal newCost) throws FareNotFoundException, UpdateFareException {
        try {
            Fare fare = retrieveFareById(fareID);
            fare.setAmount(newCost);
            em.flush();
            return fare;
        } catch (PersistenceException ex) {
            throw new UpdateFareException("Invalid new cost");
        }
    }

    @Override
    public void deleteFares(List<Fare> fares) {
        for (Fare fare : fares) {
            em.remove(fare);
        }
    }

    @Override
    public List<Fare> retrieveFareByPlanId(Long planId) throws FlightSchedulePlanNotFoundException, FareNotFoundException {
        FlightSchedulePlan plan = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanById(planId);
        Query q = em.createQuery("SELECT f FROM Fare f JOIN f.flightSchedulePlans p WHERE p = :plan");
        q.setParameter("plan", plan);
        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            throw new FareNotFoundException("Fare with Flight Schedule Plan id: " + planId + "does not exist");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Fare>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
