/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author timothy
 */
@Local
public interface PartnerSessionBeanLocal {

    public Partner createNewPartner(Partner partner) throws PartnerExistException, UnknownPersistenceException;

    public long doLogin(String username, String password) throws InvalidLoginException;

    public Partner retrievePartnerByUsername(String email) throws PartnerNotFoundException;

    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException;
    
}
