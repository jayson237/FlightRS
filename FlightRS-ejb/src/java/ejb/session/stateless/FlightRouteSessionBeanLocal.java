/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightRouteSessionBeanLocal {

    public FlightRoute retrieveflightRouteById(Long flightRouteId) throws FlightRouteNotFoundException;

    public FlightRoute retrieveflightRouteByAirport(Airport origin, Airport destination) throws FlightRouteNotFoundException;

    public FlightRoute retrieveFlightRouteByFlightId(Long flightId) throws FlightNotFoundException;
    
}
