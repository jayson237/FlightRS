/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.SeatBookedException;
import util.exception.SeatInventoryNotFoundException;

/**
 *
 * @author jayso
 */
@Local
public interface SeatInventorySessionBeanLocal {

    public SeatInventory createSeatInventory(SeatInventory seatInventory, FlightSchedule flightSchedule, CabinClass cabinClass) throws InputDataValidationException;

    public void deleteSeatInventory(List<SeatInventory> seats);

    public SeatInventory retrieveSeatsById(Long seatInventoryID) throws SeatInventoryNotFoundException;

    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatInventoryNotFoundException, SeatBookedException;
    
}
