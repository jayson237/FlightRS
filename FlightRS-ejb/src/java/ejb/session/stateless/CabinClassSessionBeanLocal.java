/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import javax.ejb.Local;
import util.exception.CabinClassExistException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Local
public interface CabinClassSessionBeanLocal {

    public CabinClass createNewCabinClass(CabinClass cc) throws CabinClassExistException, GeneralException, InputDataValidationException;
    
}
