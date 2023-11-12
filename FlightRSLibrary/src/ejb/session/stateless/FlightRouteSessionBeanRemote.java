/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.DeleteFlightRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Remote
public interface FlightRouteSessionBeanRemote {

    public FlightRoute createNewFlightRoute(FlightRoute flightRoute, String originAirportCode, String destinationAirportCode) throws AirportNotFoundException, FlightRouteExistException, GeneralException, InputDataValidationException;

    public List<FlightRoute> retrieveAllFlightRoutes();

    public boolean deleteFlightRoute(Long routeId) throws FlightRouteNotFoundException, DeleteFlightRouteException;
    
}
