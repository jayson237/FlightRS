/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author timothy
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    @Override
    public Partner createNewPartner(Partner partner) throws PartnerExistException, UnknownPersistenceException {
        try {
            em.persist(partner);
            em.flush();
            return partner;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerExistException("Partner with the same username already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    
    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, id);
        if(partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Partner id " + id.toString() + " does not exist!");
        }
    }
   
    public Partner retrievePartnerByUsername(String email) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.email = :email");
        query.setParameter("username", email);
        
        try {
        return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner does not exist!");
        }
    }
    
    @Override
    public long doLogin(String email, String password) throws InvalidLoginException {
        try {
            Partner partner = retrievePartnerByUsername(email);
            
            if(partner.getPassword().equals(password)) {
                return partner.getPartnerId();
            } else {
                throw new InvalidLoginException("Password is incorrect. Please try again.\n");
            }
        } catch (PartnerNotFoundException ex) {
                throw new InvalidLoginException("Partner email does not exist.\n");
        }
    }
}
