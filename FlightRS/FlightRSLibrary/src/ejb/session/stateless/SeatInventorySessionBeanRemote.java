/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.SeatInventory;
import javax.ejb.Remote;

/**
 *
 * @author jayso
 */
@Remote
public interface SeatInventorySessionBeanRemote {

    public boolean isBooked(SeatInventory seatInventory, String seatNumber);

}
