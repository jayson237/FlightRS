/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Aircraft;
import entity.AircraftConfiguration;
import entity.CabinClass;
import entity.Customer;
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
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @EJB
    private AircraftSessionBeanLocal aircraftSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AircraftConfigurationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public AircraftConfiguration createNewAircraftConfig(String aircraftType, AircraftConfiguration config, List<CabinClass> cabinClasses) throws AircraftTypeNotFoundException, AircraftConfigurationExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<AircraftConfiguration>> constraintViolations = validator.validate(config);
        if (constraintViolations.isEmpty()) {
            try {
                Aircraft a = aircraftSessionBean.retrieveAircraftByType(aircraftType);
                em.persist(config);
                config.setAircraft(a);
                config.setCabinClasses(cabinClasses);
                em.flush();
                return config;
            } catch (AircraftTypeNotFoundException e) {
                throw new AircraftTypeNotFoundException("There is no such aircraft under Merlion Airlines");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new AircraftConfigurationExistException("Aicraft with the same configuration already exist");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<AircraftConfiguration> retrieveAllAirConfigurations() {
        Query q = em.createQuery("SELECT ac FROM AircraftConfiguration ac ORDER BY ac.aircraft.name ASC, ac.name ASC");
        return q.getResultList();
    }

    @Override
    public AircraftConfiguration retrieveAirConfigById(Long aircraftConfigId) throws AircraftConfigurationNotFoundException {
        AircraftConfiguration config = em.find(AircraftConfiguration.class, aircraftConfigId);

        if (config != null) {
            return config;
        }
        throw new AircraftConfigurationNotFoundException("Air configuration with id: " + aircraftConfigId + " does not exist");
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AircraftConfiguration>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
