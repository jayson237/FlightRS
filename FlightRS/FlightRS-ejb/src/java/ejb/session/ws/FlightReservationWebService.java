/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import helper.MyPair;
import entity.FlightSchedule;
import entity.Fare;
import entity.SeatInventory;
import entity.FlightReservation;
import entity.Passenger;
import entity.Transaction;
import util.enumeration.CabinClassType;
import ejb.session.stateless.FlightScheduleSessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateful.FlightReservationSessionBeanLocal;
import ejb.session.stateless.SeatInventorySessionBeanLocal;
import util.exception.TransactionExistException;
import util.exception.TransactionNotFoundException;
import util.exception.FlightReservationExistException;
import util.exception.SeatBookedException;
import util.exception.CustomerNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.AirportNotFoundException;
import util.exception.CabinClassNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Timothy
 */
@WebService(serviceName = "FlightReservationWebService")
@Stateless()
public class FlightReservationWebService {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBean;

    @EJB
    private FlightReservationSessionBeanLocal reservationSessionBean;

    @EJB
    private SeatInventorySessionBeanLocal seatsInventorySessionBean;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBean;

    @WebMethod(operationName = "doLogin")
    public long doLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginException {
        return partnerSessionBean.doLogin(username, password);
    }

    @WebMethod(operationName = "getFlightSchedules")
    public List<FlightSchedule> getFlightSchedules(@WebParam(name = "origin") String origin,
            @WebParam(name = "destination") String destination,
            @WebParam(name = "date") String date,
            @WebParam(name = "cabinclasstype") CabinClassType cabinclasstype) throws FlightNotFoundException, AirportNotFoundException, ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date departureDate = inputFormat.parse(date);
        List<FlightSchedule> res = flightScheduleSessionBean.getFlightSchedulesUnmanaged(origin, destination, departureDate, cabinclasstype);
        for (FlightSchedule fs : res) {
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);

            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
            for (Fare fare : fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlans(null);
            }

            fs.getFlightSchedulePlan().setFlightSchedules(null);
            fs.setFlightReservations(null);
            for (SeatInventory seats : fs.getSeatInventory()) {
                seats.setFlightSchedule(null);
            }
        }
        return res;
    }

    @WebMethod(operationName = "getBiggestFare")
    public Fare getBiggestFare(@WebParam(name = "flightscheduleentity") FlightSchedule flightscheduleentity,
            @WebParam(name = "cabinclasstype") CabinClassType cabinclastype) throws
            FlightScheduleNotFoundException,
            CabinClassNotFoundException {
        Fare fare = flightScheduleSessionBean.getBiggestFareUnmanaged(flightscheduleentity, cabinclastype);
        fare.setFlightSchedulePlans(null);
        return fare;
    }

    @WebMethod(operationName = "getIndirectFlightSchedules")
    public List<MyPair> getIndirectFlightSchedules(@WebParam(name = "origin") String origin,
            @WebParam(name = "destination") String destination,
            @WebParam(name = "date") String date,
            @WebParam(name = "cabinclasstype") CabinClassType cabinclasstype) throws
            FlightNotFoundException, ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date departureDate = inputFormat.parse(date);
        List<Pair<FlightSchedule, FlightSchedule>> list = flightScheduleSessionBean.getConnectingFlightSchedulesUnmanaged(origin, destination, departureDate, cabinclasstype);
        List<MyPair> newList = new ArrayList<>();
        for (Pair<FlightSchedule, FlightSchedule> pairs : list) {
            MyPair newPair = new MyPair(pairs.getKey(), pairs.getValue());
            newList.add(newPair);
        }
        for (MyPair res : newList) {
            FlightSchedule fs = res.getFs1();

            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);

            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);

            fs.getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
            for (Fare fare : fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlans(null);
            }

            fs.getFlightSchedulePlan().setFlightSchedules(null);
            fs.setFlightReservations(null);
            for (SeatInventory seats : fs.getSeatInventory()) {
                seats.setFlightSchedule(null);
            }

            fs = res.getFs2();
            fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);
            fs.getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
            for (Fare fare : fs.getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlans(null);
            }

            fs.getFlightSchedulePlan().setFlightSchedules(null);
            fs.setFlightReservations(null);
            for (SeatInventory seats : fs.getSeatInventory()) {
                seats.setFlightSchedule(null);
            }

        }

        return newList;
    }

    @WebMethod(operationName = "retrieveFlightScheduleById")
    public FlightSchedule retrieveFlightScheduleById(@WebParam(name = "flightscheduleid") long flightscheduleid) throws FlightScheduleNotFoundException {
        FlightSchedule fs = flightScheduleSessionBean.retrieveFlightScheduleByIdUnmanaged(flightscheduleid);
        fs.getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
        fs.getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);
        fs.getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
        for (Fare fare : fs.getFlightSchedulePlan().getFares()) {
            fare.setFlightSchedulePlans(null);
        }

        fs.getFlightSchedulePlan().setFlightSchedules(null);
        fs.setFlightReservations(null);
        for (SeatInventory seats : fs.getSeatInventory()) {

            seats.setFlightSchedule(null);
        }
        return fs;

    }

    @WebMethod(operationName = "getSeatInventory")
    public SeatInventory getCorrectSeatInventory(@WebParam(name = "flightscheduleentity") FlightSchedule flightscheduleentity,
            @WebParam(name = "cabinclasstype") CabinClassType cabinclasstype) throws
            FlightScheduleNotFoundException,
            SeatInventoryNotFoundException {
        SeatInventory seats = flightScheduleSessionBean.getSeatInventoryUnmanaged(flightscheduleentity, cabinclasstype);
        if (seats.getCabin() != null) {
            seats.setFlightSchedule(null);
        }
        return seats;
    }

    @WebMethod(operationName = "isBooked")
    public boolean checkIfBooked(@WebParam(name = "seatinventoryentity") SeatInventory seatinventoryentity,
            @WebParam(name = "seatnumber") String seatnumber) {
        return seatsInventorySessionBean.isBooked(seatinventoryentity, seatnumber);
    }

    @WebMethod(operationName = "createNewReservation")
    public long createNewReservation(@WebParam(name = "reservationentity") FlightReservation reservationentity,
            @WebParam(name = "passengers") List<Passenger> passengers,
            @WebParam(name = "flightscheduleid") long flightscheduleid,
            @WebParam(name = "transactionid") long transactionid,
            @WebParam(name = "customerId") long customerid) throws
            InputDataValidationException,
            FlightReservationExistException,
            UnknownPersistenceException,
            FlightScheduleNotFoundException,
            SeatInventoryNotFoundException,
            SeatBookedException,
            CustomerNotFoundException,
            TransactionNotFoundException {
        return reservationSessionBean.createNewReservation(reservationentity, passengers, flightscheduleid, transactionid, customerid);
    }

    @WebMethod(operationName = "createNewTransaction")
    public long createNewTransaction(@WebParam(name = "creditcardnumber") String creditcardnumber,
            @WebParam(name = "cvv") String cvv, @WebParam(name = "expiryDate") Date expiryDate,
            @WebParam(name = "userid") long userid) throws
            CustomerNotFoundException,
            InputDataValidationException,
            UnknownPersistenceException,
            TransactionExistException {
        return transactionSessionBean.createNewTransaction(new Transaction(creditcardnumber, cvv, expiryDate), userid).getTransactionId();
    }

    @WebMethod(operationName = "retrieveTransactionsByCustomerId")
    public List<Transaction> retrieveItinerariesByUserId(@WebParam(name = "userid") long userid) {
        List<Transaction> list = transactionSessionBean.retrieveTransactionByCustomerIdUnmanaged(userid);
        for (Transaction transaction : list) {
            transaction.getCustomer().setTransactions(null);
            for (FlightReservation res : transaction.getFlightReservations()) {
                res.setTransaction(null);
                res.getFlightSchedule().setFlightReservations(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);
                res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
                for (Fare fare : res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                    fare.setFlightSchedulePlans(null);
                }
                res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedules(null);
                res.getFlightSchedule().setFlightReservations(null);
                for (SeatInventory seats : res.getFlightSchedule().getSeatInventory()) {
                    seats.setFlightSchedule(null);
                }
            }
        }
        return list;
    }

    @WebMethod(operationName = "retreiveTransactionById")
    public Transaction retrieveTransactionById(@WebParam(name = "transactionid") long transactionid) throws TransactionNotFoundException {
        Transaction transaction = transactionSessionBean.retrieveTransactionByIdUnmanaged(transactionid);
        transaction.getCustomer().setTransactions(null);
        for (FlightReservation res : transaction.getFlightReservations()) {
            res.setTransaction(null);
            res.getFlightSchedule().setFlightReservations(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute().setFlights(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setFlightSchedulePlans(null);
            res.getFlightSchedule().getFlightSchedulePlan().getFlight().setAircraftConfiguration(null);
            for (Fare fare : res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                fare.setFlightSchedulePlans(null);
            }
            res.getFlightSchedule().getFlightSchedulePlan().setFlightSchedules(null);
            res.getFlightSchedule().setFlightReservations(null);
            for (SeatInventory seats : res.getFlightSchedule().getSeatInventory()) {

                seats.setFlightSchedule(null);
            }
        }
        return transaction;
    }
}
