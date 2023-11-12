/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface FlightSessionBeanRemote {

    public Flight createNewFlight(Flight flight, String originAirport, String destinationAirport, Long aircraftConfigurationId) throws AirportNotFoundException, FlightRouteNotFoundException, AircraftConfigurationNotFoundException, FlightExistException, GeneralException, InputDataValidationException;

    public List<Flight> retrieveAllFlights();

    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException;

    public boolean updateFlight(Flight flight) throws FlightNotFoundException, UpdateFlightException, InputDataValidationException;

    public boolean deleteFlight(Long flightId) throws FlightNotFoundException, DeleteFlightException;

}
