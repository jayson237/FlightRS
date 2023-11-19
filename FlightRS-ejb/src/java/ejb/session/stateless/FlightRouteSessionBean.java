/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.Flight;
import entity.FlightRoute;
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
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @EJB
    private FlightSessionBeanLocal flightSessionBean;

    @EJB
    private AirportSessionBeanLocal airportSessionBean;

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightRouteSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public FlightRoute createNewFlightRoute(FlightRoute flightRoute, String originAirportCode, String destinationAirportCode) throws AirportNotFoundException, FlightRouteExistException, GeneralException, InputDataValidationException {
        Set<ConstraintViolation<FlightRoute>> constraintViolations = validator.validate(flightRoute);
        if (constraintViolations.isEmpty()) {
            try {
                Airport origin = airportSessionBean.retrieveAirportByCode(originAirportCode);
                Airport destination = airportSessionBean.retrieveAirportByCode(destinationAirportCode);

                em.persist(flightRoute);
                flightRoute.setOriginAirport(origin);
                flightRoute.setDestinationAirport(destination);

                em.flush();
                return flightRoute;
            } catch (AirportNotFoundException ex) {
                throw new AirportNotFoundException("There is no such origin or destination airport");
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new FlightRouteExistException("Flight route with the same origin and destination airports already exist");
                } else {
                    throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<FlightRoute> retrieveAllFlightRoutes() {
        Query q = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.isDisabled = false ORDER BY fr.originAirport.airportCode ASC");
        List<FlightRoute> flightRoutes = q.getResultList();
        List<FlightRoute> sortedRoutes = new ArrayList<>();

        for (FlightRoute route : flightRoutes) {
            if (!sortedRoutes.contains(route)) {
                sortedRoutes.add(route);
            }

            if (route.isHasReturnFlight()) {
                FlightRoute returnRoute = findReturnRoute(flightRoutes, route);
                if (returnRoute != null && !sortedRoutes.contains(returnRoute)) {
                    sortedRoutes.add(returnRoute);
                }
            }

        }
        return sortedRoutes;
    }

    @Override
    public FlightRoute retrieveflightRouteById(Long flightRouteId) throws FlightRouteNotFoundException {
        FlightRoute flightRoute = em.find(FlightRoute.class, flightRouteId);

        if (flightRoute != null) {
            return flightRoute;
        } else {
            throw new FlightRouteNotFoundException("Flight Route with flightRoute ID " + flightRouteId + " does not exist");
        }
    }

    @Override
    public FlightRoute retrieveflightRouteByAirport(Airport origin, Airport destination) throws FlightRouteNotFoundException {
        Query q = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr.originAirport = :origin AND fr.destinationAirport = :destination");
        q.setParameter("origin", origin);
        q.setParameter("destination", destination);
        try {
            FlightRoute flightRoute = (FlightRoute) q.getSingleResult();
            return flightRoute;
        } catch (NoResultException ex) {
            throw new FlightRouteNotFoundException("Flight route with origin airport " + origin.getAirportCode() + " and " + "destination airport " + destination.getAirportCode() + " does not exist");
        }
    }

    @Override
    public FlightRoute retrieveFlightRouteByFlightId(Long flightId) throws FlightNotFoundException {
        Flight flight = flightSessionBean.retrieveFlightById(flightId);
        Query q = em.createQuery("SELECT fr FROM FlightRoute fr WHERE fr = :flight");
        q.setParameter("flight", flight.getFlightRoute());
        try {
            FlightRoute fr = (FlightRoute) q.getSingleResult();
            return fr;
        } catch (NoResultException ex) {
            throw new FlightNotFoundException("Flight id: " + flightId + " cannot be deleted because it does not exist");
        }
    }

    @Override
    public boolean deleteFlightRoute(String origin, String destination) throws AirportNotFoundException, FlightRouteNotFoundException, DeleteFlightRouteException {
        Airport originAirport = airportSessionBean.retrieveAirportByCode(origin);
        Airport destinationAirport = airportSessionBean.retrieveAirportByCode(destination);
        FlightRoute flightRoute = retrieveflightRouteByAirport(originAirport, destinationAirport);
        if (flightRoute == null) {
            throw new FlightRouteNotFoundException("Flight Route with route " + origin + " to " + destination + " does not exist");
        }
        List<Flight> flights = flightSessionBean.retrieveFlightByFlightRouteId(flightRoute.getFlightRouteId());

        if (flights.isEmpty() && !flightRoute.isHasReturnFlight()) {
            em.remove(flightRoute);
            return true;
        } else {
            flightRoute.setIsDisabled(true);
            throw new DeleteFlightRouteException("Flight route with route " + origin + " to " + destination + " is in use and cannot be deleted! It will be disabled");
        }
    }

    private FlightRoute findReturnRoute(List<FlightRoute> routes, FlightRoute mainRoute) {
        for (FlightRoute route : routes) {
            if (route.isHasReturnFlight()
                    && route.getOriginAirport().getAirportCode().equals(mainRoute.getDestinationAirport().getAirportCode())
                    && route.getDestinationAirport().getAirportCode().equals(mainRoute.getOriginAirport().getAirportCode())) {
                return route;
            }
        }
        return null;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightRoute>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
