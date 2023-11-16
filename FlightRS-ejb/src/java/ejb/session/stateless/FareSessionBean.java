/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;

/**
 *
 * @author jayso
 */
@Stateless
public class FareSessionBean implements FareSessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    @Override
    public Fare createNewFare(Fare fare) throws InputDataValidationException, GeneralException {
        em.persist(fare);
        em.flush();
        return fare;
    }

}
