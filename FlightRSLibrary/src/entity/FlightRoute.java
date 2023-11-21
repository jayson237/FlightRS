/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jayso
 */
@Entity
@Table(
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"originAirport_id", "destinationAirport_id"})
        }
)
public class FlightRoute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightRouteId;

    @Column(nullable = false)
    @NotNull
    private boolean hasReturnFlight;

    @Column(nullable = false)
    @NotNull
    private boolean isDisabled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport originAirport;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Airport destinationAirport;

    @OneToMany(mappedBy = "flightRoute")
    private List<Flight> flights;

    public FlightRoute() {
        this.flights = new ArrayList<>();
    }

    public FlightRoute(boolean hasReturnFlight, boolean isDisabled) {
        this();
        this.hasReturnFlight = hasReturnFlight;
        this.isDisabled = isDisabled;
    }

    public Long getFlightRouteId() {
        return flightRouteId;
    }

    public void setFlightRouteId(Long flightRouteId) {
        this.flightRouteId = flightRouteId;
    }

    public boolean isHasReturnFlight() {
        return hasReturnFlight;
    }

    public void setHasReturnFlight(boolean hasReturnFlight) {
        this.hasReturnFlight = hasReturnFlight;
    }

    public boolean isIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public Airport getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(Airport originAirport) {
        this.originAirport = originAirport;
    }

    public Airport getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(Airport destinationAirport) {
        this.destinationAirport = destinationAirport;
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

}
