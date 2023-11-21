    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.CabinClass;
import entity.FlightSchedule;
import entity.SeatInventory;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.SeatBookedException;
import util.exception.SeatInventoryNotFoundException;

/**
 *
 * @author jayso
 */
@Stateless
public class SeatInventorySessionBean implements SeatInventorySessionBeanRemote, SeatInventorySessionBeanLocal {

    @PersistenceContext(unitName = "FlightRS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SeatInventorySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public SeatInventory createSeatInventory(SeatInventory seatInventory, FlightSchedule flightSchedule, CabinClass cabinClass) throws InputDataValidationException {

        int noOfRows = cabinClass.getNumOfRows();
        int noOfSeatsAbreast = cabinClass.getNumOfSeatsAbreast();
        char[][] seats = new char[noOfRows][noOfSeatsAbreast];

        for (int i = 0; i < noOfRows; i++) {
            for (int j = 0; j < noOfSeatsAbreast; j++) {
                seats[i][j] = '-';
            }
        }
        seatInventory.setSeats(seats);
        Set<ConstraintViolation<SeatInventory>> constraintViolations = validator.validate(seatInventory);
        if (constraintViolations.isEmpty()) {
            em.persist(seatInventory);
            seatInventory.setCabin(cabinClass);
            seatInventory.setFlightSchedule(flightSchedule);
            flightSchedule.getSeatInventory().add(seatInventory);

            return seatInventory;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public SeatInventory retrieveSeatsById(Long seatInventoryID) throws SeatInventoryNotFoundException{
        SeatInventory seat = em.find(SeatInventory.class, seatInventoryID);
        
        if(seat != null) {
            return seat;
        } else {
            throw new SeatInventoryNotFoundException("Seat inventory does not exist!");
        }
    }

    @Override
    public void bookSeat(long seatInventoryId, String seatNumber) throws SeatInventoryNotFoundException, SeatBookedException {

        SeatInventory seatInventory = retrieveSeatsById(seatInventoryId);

        int col = seatNumber.charAt(0) - 'A';
        int row = Integer.parseInt(seatNumber.substring(1)) - 1;

        char[][] seats = seatInventory.getSeats();
        if (seats[row][col] == '-') {
            seats[row][col] = 'X';
            seatInventory.setSeats(seats);
        } else {
            throw new SeatBookedException("Seat already booked");
        }
        seatInventory.setReservedSeats(seatInventory.getReservedSeats() + 1);
        seatInventory.setBalanceSeats(seatInventory.getBalanceSeats() - 1);
    }
    
    @Override
    public boolean isBooked(SeatInventory seatInventory, String seatNumber) {
        try {
            SeatInventory seat = retrieveSeatsById(seatInventory.getSeatInventoryId());
            char[][] mtx = seat.getSeats();
            int col = seatNumber.charAt(0) - 'A';
            int row = Integer.parseInt(seatNumber.substring(1)) - 1;
            
            if(mtx[row][col] == 'X') {
                return true;
            } 
        } catch (SeatInventoryNotFoundException ex) {
            System.out.println(ex.getMessage());        
        }
        return false;
    }

    @Override
    public void deleteSeatInventory(List<SeatInventory> seats) {
        for (SeatInventory seat : seats) {
            em.remove(seat);
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SeatInventory>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

}
