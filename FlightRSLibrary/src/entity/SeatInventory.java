 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jayso
 */
@Entity
public class SeatInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatInventoryId;

    @Column(nullable = false)
    @Min(0)
    @NotNull
    private Integer availableSeats;

    @Column(nullable = false)
    @Min(0)
    @NotNull
    private Integer reservedSeats;

    @Column(nullable = false)
    @Min(0)
    @NotNull
    private Integer balanceSeats;

    @Column(nullable = false)
    @NotNull
    private char[][] seats;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private CabinClass cabin;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private FlightSchedule flightSchedule;

    public SeatInventory() {

    }

    public SeatInventory(Integer availableSeats, Integer reservedSeats, Integer balanceSeats) {
        this.availableSeats = availableSeats;
        this.reservedSeats = reservedSeats;
        this.balanceSeats = balanceSeats;
    }

    public Long getSeatInventoryId() {
        return seatInventoryId;
    }

    public void setSeatInventoryId(Long seatInventoryId) {
        this.seatInventoryId = seatInventoryId;
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

    public char[][] getSeats() {
        return seats;
    }

    public void setSeats(char[][] seats) {
        this.seats = seats;
    }

    public CabinClass getCabin() {
        return cabin;
    }

    public void setCabin(CabinClass cabin) {
        this.cabin = cabin;
    }

    public FlightSchedule getFlightSchedule() {
        return flightSchedule;
    }

    public void setFlightSchedule(FlightSchedule flightSchedule) {
        this.flightSchedule = flightSchedule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seatInventoryId != null ? seatInventoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeatInventory)) {
            return false;
        }
        SeatInventory other = (SeatInventory) object;
        if ((this.seatInventoryId == null && other.seatInventoryId != null) || (this.seatInventoryId != null && !this.seatInventoryId.equals(other.seatInventoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SeatInventory[ id=" + seatInventoryId + " ]";
    }

}
