/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AirportNotFoundException;

/**
 *
 * @author jayso
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    public AirportSessionBean() {

    }

    @Override
    public Airport retrieveAirportByCode(String airportCode) throws AirportNotFoundException {
        Query q = em.createQuery("SELECT a FROM Airport a WHERE a.airportCode = :airportCode");
        q.setParameter("airportCode", airportCode);
        try {
            Airport a = (Airport) q.getSingleResult();
            return a;
        } catch (NoResultException ex) {
            throw new AirportNotFoundException("Airport with code: " + airportCode + " deos not exist");
        }
    }

}
