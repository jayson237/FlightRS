/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import java.math.BigDecimal;
import javax.ejb.Remote;
import util.exception.FareNotFoundException;
import util.exception.UpdateFareException;

/**
 *
 * @author jayso
 */
@Remote
public interface FareSessionBeanRemote {

    public Fare updateFare(long fareID, BigDecimal newCost) throws FareNotFoundException, UpdateFareException;
    
}
