/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.CabinClassType;

/**
 *
 * @author timothy
 */
@Entity
public class FlightReservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightReservationId;
    @Column(length = 32, nullable = false)
    private String flightNumber;
    @Temporal(TemporalType.DATE)
    private Date flightSchedule;
    @Enumerated(EnumType.STRING)
    private CabinClassType cabinType;
    @Column(length = 32, nullable = false)
    private String seatNumber;
    @Column(length = 32, nullable = false)
    private String passengerFirstName;
    @Column(length = 32, nullable = false)
    private String passengerLastName;
    @Column(length = 32, nullable = false, unique = true)
    private String passport;
    @OneToOne(mappedBy = "flightReservation")
    @JoinColumn(name = "fare_id")
    private Fare fare;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public FlightReservation() {
    }

    public FlightReservation(String flightNumber, Date flightSchedule, CabinClassType cabinType, String seatNumber, String passengerFirstName, String passengerLastName, String passport) {
        this.flightNumber = flightNumber;
        this.flightSchedule = flightSchedule;
        this.cabinType = cabinType;
        this.seatNumber = seatNumber;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
        this.passport = passport;
    }

    public Long getFlightReservationId() {
        return flightReservationId;
    }

    public void setFlightReservationId(Long flightReservationId) {
        this.flightReservationId = flightReservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightReservationId != null ? flightReservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightReservationId fields are not set
        if (!(object instanceof FlightReservation)) {
            return false;
        }
        FlightReservation other = (FlightReservation) object;
        if ((this.flightReservationId == null && other.flightReservationId != null) || (this.flightReservationId != null && !this.flightReservationId.equals(other.flightReservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightReservation[ id=" + flightReservationId + " ]";
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Date getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(Date flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public CabinClassType getCabinType() {
        return cabinType;
    }

    public void setCabinType(CabinClassType cabinType) {
        this.cabinType = cabinType;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
    }

    public String getPassengerLastName() {
        return passengerLastName;
    }

    public void setPassengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
