/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author timothy
 */
@Entity
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigurationId;
    @Column(length = 32, nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Integer numOfCabinClass;
    @Column(nullable = false)
    private Integer maxSeats;
    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;
    @OneToMany(mappedBy = "aircraftConfiguration")
    private List<CabinClass> cabinClasses;
    @ManyToOne (optional = false)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    public AircraftConfiguration() {
    }

    public AircraftConfiguration(String name, Integer numOfCabinClass, Integer maxSeats) {
        this.name = name;
        this.numOfCabinClass = numOfCabinClass;
        this.maxSeats = maxSeats;
    }
    
    public Long getAircraftConfigurationId() {
        return aircraftConfigurationId;
    }

    public void setAircraftConfigurationId(Long aircraftConfigurationId) {
        this.aircraftConfigurationId = aircraftConfigurationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigurationId != null ? aircraftConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftConfigurationId fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigurationId == null && other.aircraftConfigurationId != null) || (this.aircraftConfigurationId != null && !this.aircraftConfigurationId.equals(other.aircraftConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfiguration[ id=" + aircraftConfigurationId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the numOfCabinClass
     */
    public Integer getNumOfCabinClass() {
        return numOfCabinClass;
    }

    /**
     * @param numOfCabinClass the numOfCabinClass to set
     */
    public void setNumOfCabinClass(Integer numOfCabinClass) {
        this.numOfCabinClass = numOfCabinClass;
    }

    /**
     * @return the maxSeats
     */
    public Integer getMaxSeats() {
        return maxSeats;
    }

    /**
     * @param maxSeats the maxSeats to set
     */
    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    /**
     * @return the flights
     */
    public Flight getFlights() {
        return flight;
    }

    /**
     * @param flights the flights to set
     */
    public void setFlights(Flight flights) {
        this.flight = flight;
    }

    /**
     * @return the cabinClasses
     */
    public List<CabinClass> getCabinClasses() {
        return cabinClasses;
    }

    /**
     * @param cabinClasses the cabinClasses to set
     */
    public void setCabinClasses(List<CabinClass> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    /**
     * @return the aircraft
     */
    public Aircraft getAircraft() {
        return aircraft;
    }

    /**
     * @param aircraft the aircraft to set
     */
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }
    
}
