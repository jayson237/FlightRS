/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightNotFoundException;
import util.exception.FlightRouteNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface FlightSessionBeanLocal {

    public List<Flight> retrieveFlightByFlightRouteId(Long routeId) throws FlightRouteNotFoundException;

    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException;

    public List<Flight> retrieveAllFlightByRoute(String origin, String destination) throws FlightNotFoundException;

    public List<Flight[]> retrieveAllIndirectFlightByFlightRoute(String originIATACode, String destinationIATACode) throws FlightNotFoundException;

}
