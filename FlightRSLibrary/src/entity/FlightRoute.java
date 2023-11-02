/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javafx.util.Pair;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author timothy
 */
@Entity
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;
    @Column(nullable = false, unique = true)
    private Pair<Airport, Airport> oriDestAirport;
    @Column(nullable = false)
    private boolean hasReturn;
    @Column(nullable = false)
    private boolean isDisabled;
    @OneToOne(mappedBy = "flightRoute")
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flights;

    public FlightRoute() {
    }

    public FlightRoute(Pair<Airport, Airport> oriDestAirport, boolean hasReturn, boolean isDisabled) {
        this.oriDestAirport = oriDestAirport;
        this.hasReturn = hasReturn;
        this.isDisabled = isDisabled;
    }

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightRouteId != null ? flightRouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightRouteId fields are not set
        if (!(object instanceof FlightRoute)) {
            return false;
        }
        FlightRoute other = (FlightRoute) object;
        if ((this.flightRouteId == null && other.flightRouteId != null) || (this.flightRouteId != null && !this.flightRouteId.equals(other.flightRouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightRoute[ id=" + flightRouteId + " ]";
    }

    /**
     * @return the oriDestAirport
     */
    public Pair<Airport, Airport> getOriDestAirport() {
        return oriDestAirport;
    }

    /**
     * @param oriDestAirport the oriDestAirport to set
     */
    public void setOriDestAirport(Pair<Airport, Airport> oriDestAirport) {
        this.oriDestAirport = oriDestAirport;
    }

    /**
     * @return the hasReturn
     */
    public boolean isHasReturn() {
        return hasReturn;
    }

    /**
     * @param hasReturn the hasReturn to set
     */
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    /**
     * @return the isDisabled
     */
    public boolean isIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the flights
     */
    public Flight getFlights() {
        return flights;
    }

    /**
     * @param flights the flights to set
     */
    public void setFlights(Flight flights) {
        this.flights = flights;
    }
    
}
