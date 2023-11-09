/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import util.enumeration.CabinClassType;

/**
 *
 * @author jayso
 */
@Entity
public class CabinClass implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cabinClassId;

    @Enumerated(EnumType.STRING)
    private CabinClassType type;

    @Column(nullable = false)
    @Min(0)
    @Max(2)
    private Integer numberOfAisles;

    @Column(nullable = false)
    private Integer numOfRows;

    @Column(nullable = false)
    private Integer numOfSeatsAbreast;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private String seatingConfiguration;

    @Column(nullable = false)
    private Integer reservedSeats;

    @Column(nullable = false)
    private Integer balanceSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @OneToOne
    private FlightSchedule flightSchedule;

    @OneToMany(mappedBy = "cabinClass")
    private List<Seat> seats;

    @OneToMany
    private List<Fare> fares;

    public CabinClass() {
    }

    public CabinClass(CabinClassType type, Integer numberOfAisles, Integer numOfRows, Integer numOfSeatsAbreast, Integer maxCapacity, String seatingConfiguration, Integer balanceSeats) {
        this.type = type;
        this.numberOfAisles = numberOfAisles;
        this.numOfRows = numOfRows;
        this.numOfSeatsAbreast = numOfSeatsAbreast;
        this.maxCapacity = maxCapacity;
        this.seatingConfiguration = seatingConfiguration;
        this.reservedSeats = 0;
        this.balanceSeats = balanceSeats;
        this.availableSeats = maxCapacity - balanceSeats - reservedSeats;
    }

    public Long getCabinClassId() {
        return cabinClassId;
    }

    public void setCabinClassId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    public CabinClassType getType() {
        return type;
    }

    public void setType(CabinClassType type) {
        this.type = type;
    }

    public Integer getNumberOfAisles() {
        return numberOfAisles;
    }

    public void setNumberOfAisles(Integer numberOfAisles) {
        this.numberOfAisles = numberOfAisles;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Integer numOfRows) {
        this.numOfRows = numOfRows;
    }

    public Integer getNumOfSeatsAbreast() {
        return numOfSeatsAbreast;
    }

    public void setNumOfSeatsAbreast(Integer numOfSeatsAbreast) {
        this.numOfSeatsAbreast = numOfSeatsAbreast;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getSeatingConfiguration() {
        return seatingConfiguration;
    }

    public void setSeatingConfiguration(String seatingConfiguration) {
        this.seatingConfiguration = seatingConfiguration;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassId != null ? cabinClassId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassId fields are not set
        if (!(object instanceof CabinClass)) {
            return false;
        }
        CabinClass other = (CabinClass) object;
        if ((this.cabinClassId == null && other.cabinClassId != null) || (this.cabinClassId != null && !this.cabinClassId.equals(other.cabinClassId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + cabinClassId + " ]";
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(Integer reservedSeats) {
        this.reservedSeats = reservedSeats;
    }

    public Integer getBalanceSeats() {
        return balanceSeats;
    }

    public void setBalanceSeats(Integer balanceSeats) {
        this.balanceSeats = balanceSeats;
    }

}
