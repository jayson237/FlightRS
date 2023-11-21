/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import javax.ejb.Local;
import util.exception.AirportNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightRouteSessionBeanLocal {

    public FlightRoute createNewFlightRoute(FlightRoute flightRoute, String originAirportCode, String destinationAirportCode) throws AirportNotFoundException, FlightRouteExistException, GeneralException, InputDataValidationException;

    public FlightRoute retrieveflightRouteById(Long flightRouteId) throws FlightRouteNotFoundException;

    public FlightRoute retrieveflightRouteByAirport(Airport origin, Airport destination) throws FlightRouteNotFoundException;

    public FlightRoute retrieveFlightRouteByFlightId(Long flightId) throws FlightNotFoundException;

}
