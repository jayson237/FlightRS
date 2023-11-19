/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
import entity.FlightSchedulePlan;
import java.util.ArrayList;
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
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFlightException;

/**
 *
 * @author jayso
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @EJB
    private FlightSchedulePlanSessionBeanLocal flightSchedulePlanSessionBean;

    @EJB
    private AircraftConfigurationSessionBeanLocal aircraftConfigurationSessionBean;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    @EJB
    private FlightRouteSessionBeanLocal flightRouteSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Flight createNewFlight(Flight flight, String originAirport, String destinationAirport, String aircraftConfig) throws AirportNotFoundException, FlightRouteNotFoundException, AircraftConfigurationNotFoundException, FlightExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);
        if (constraintViolations.isEmpty()) {
            try {
                Airport origin = airportSessionBean.retrieveAirportByCode(originAirport);
                Airport destination = airportSessionBean.retrieveAirportByCode(destinationAirport);
                FlightRoute flightRoute = flightRouteSessionBean.retrieveflightRouteByAirport(origin, destination);
                AircraftConfiguration config = aircraftConfigurationSessionBean.retrieveAirConfigByName(aircraftConfig);

                if (!flightRoute.isIsDisabled()) {
                    em.persist(flight);
                    flight.setAircraftConfiguration(config);
                    flight.setFlightRoute(flightRoute);
                    em.flush();
                }
                return flight;
            } catch (FlightRouteNotFoundException ex) {
                throw new FlightRouteNotFoundException("Flight route with origin airport " + originAirport + " and " + "destination airport " + destinationAirport + " does not exist");
            } catch (AirportNotFoundException ex) {
                throw new AirportNotFoundException("Either one or both airport codes does/do not exist");
            } catch (AircraftConfigurationNotFoundException ex) {
                throw new AircraftConfigurationNotFoundException("Aircraft Configuration name: " + aircraftConfig + " does not exist");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightExistException("Flight already exists");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }


    @Override
    public boolean retrieveRouteStatus(String origin, String destination) throws AirportNotFoundException, FlightRouteNotFoundException {
        Airport originAirport = airportSessionBean.retrieveAirportByCode(origin);
        Airport destinationAirport = airportSessionBean.retrieveAirportByCode(destination);
        FlightRoute flightRoute = flightRouteSessionBean.retrieveflightRouteByAirport(originAirport, destinationAirport);
        return flightRoute.isHasReturnFlight();
    }

    @Override
    public List<Flight> retrieveAllFlights() {
        Query q = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC");
        List<Flight> flights = (List<Flight>) q.getResultList();
        List<Flight> sortedFlights = new ArrayList<>();

        for (Flight flight : flights) {
            if (!sortedFlights.contains(flight)) {
                sortedFlights.add(flight);
            }

            if (flight.getFlightRoute().isHasReturnFlight()) {
                Flight returnFlight = findReturnFlight(flights, flight);
                if (returnFlight != null && !sortedFlights.contains(returnFlight)) {
                    sortedFlights.add(returnFlight);
                }
            }
        }

        return sortedFlights;
    }
    
    @Override
    public List<Flight> retrieveAllFlightByRoute(String origin, String destination) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.disabled = false AND f.flightRoute.originAirport.airportCode = :origin AND f.flightRoute.destinationAirport.airportCode = :destination ORDER BY SUBSTRING(f.flightNum, 3) ASC");
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<Flight> flightResult =  query.getResultList();
        if (flightResult.isEmpty()) {
            throw new FlightNotFoundException("No flights with flight route from " + origin + " to " +  destination + " found");
        } else {
            return flightResult;
        }
    }
    
    @Override
    public List<Flight[]> retrieveAllIndirectFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f1, f2 FROM Flight f1, Flight f2 WHERE f1.disabled = false AND f2.disabled = false AND "
                + "f1.flightRoute.originAirport.airportCode = :origin AND f2.flightRoute.destinationAirport.airportCode= :destination AND "
                + "f1.flightRoute.destinationAirport.airportCode = f2.flightRoute.originAirport.airportCode");
        query.setParameter("origin", originIATACode);
        query.setParameter("destination", destinationIATACode);
        List<Flight[]> resultFlight =  query.getResultList();
        if (resultFlight.isEmpty()) {
            throw new FlightNotFoundException("No connecting flights with flight route from " + originIATACode + " to " +  destinationIATACode + " found");
        }
        return resultFlight;
    }

    private Flight findReturnFlight(List<Flight> flights, Flight flight) {
        for (Flight f : flights) {
            if (f.getFlightRoute().getDestinationAirport().getAirportCode().equals(flight.getFlightRoute().getOriginAirport().getAirportCode())
                    && f.getFlightRoute().getOriginAirport().getAirportCode().equals(flight.getFlightRoute().getDestinationAirport().getAirportCode())) {
                return f;
            }
        }
        return null;
    }

    @Override
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight != null) {
            return flight;
        }
        throw new FlightNotFoundException("Flight with id: " + flightId + " does not exist");
    }

    @Override
    public Flight retrieveFlightByNumber(String flightNum) throws FlightNotFoundException {
        Query q = em.createQuery("SELECT f FROM Flight f WHERE f.flightNumber = :number");
        q.setParameter("number", flightNum);
        try {
            Flight flight = (Flight) q.getSingleResult();
            return flight;
        } catch (NoResultException ex) {
            throw new FlightNotFoundException("Flight with number: " + flightNum + " does not exist");
        }

    }

    @Override
    public boolean updateFlight(Flight flight) throws FlightNotFoundException, UpdateFlightException, InputDataValidationException {
        if (flight != null && flight.getFlightId() != null) {
            Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);
            if (constraintViolations.isEmpty()) {
                Flight flightToUpdate = retrieveFlightById(flight.getFlightId());
                if (flightToUpdate.getFlightNumber().equals(flight.getFlightNumber())) {
                    flightToUpdate.setFlightNumber(flight.getFlightNumber());
                    flightToUpdate.setIsDisabled(flight.getIsDisabled());
                    return true;
                } else {
                    throw new UpdateFlightException("Flight number of the existing flight does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new FlightNotFoundException("Flight ID not provided for product to be updated");
        }
    }

    @Override
    public boolean deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException {
        Flight flightToRemove = retrieveFlightById(flightId);
        if (flightToRemove == null) {
            throw new FlightNotFoundException("Flight id: " + flightId + " cannot be deleted because it does not exist");
        }

        List<FlightSchedulePlan> schedulePlan = flightSchedulePlanSessionBean.retrieveFlightSchedulePlanByFlightId(flightId);

        if (schedulePlan.isEmpty() && flightToRemove.getReturnFlightNumber() == null) {
            em.remove(flightToRemove);
            return true;
        } else {
            flightToRemove.setIsDisabled(true);
            throw new DeleteFlightException("Flight id: " + flightId + " is in use and cannot be deleted! It will be disabled");
        }
    }

    @Override
    public List<Flight> retrieveFlightByFlightRouteId(Long routeId) throws FlightRouteNotFoundException {
        FlightRoute flightRoute = flightRouteSessionBean.retrieveflightRouteById(routeId);
        if (flightRoute != null) {
            Query q = em.createQuery("SELECT f FROM Flight f WHERE f.flightRoute = :fr");
            q.setParameter("fr", flightRoute);
            return (List<Flight>) q.getResultList();
        } else {
            throw new FlightRouteNotFoundException("Flight with route ID " + routeId + " does not exist");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Flight>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
