/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Aircraft;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AircraftTypeNotFoundException;

/**
 *
 * @author jayso
 */
@Stateless
public class AircraftSessionBean implements AircraftSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    @Override
    public Aircraft retrieveAircraftByType(String aircraftType) throws AircraftTypeNotFoundException {
        Query q = em.createQuery("SELECT a FROM Aircraft a WHERE a.name = :type");
        q.setParameter("type", aircraftType);
        Aircraft a = (Aircraft) q.getSingleResult();
        if (a != null) {
            return a;
        }
        throw new AircraftTypeNotFoundException("There is no such aircraft under Merlion Airlines");
    }
}
