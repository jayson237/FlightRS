/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author jayso
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
    @Min(1)
    @Max(4)
    private Integer numOfCabinClass;

    @Column(nullable = false)
    private Integer maxSeats;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Aircraft aircraft;

    @OneToMany (cascade = CascadeType.PERSIST)
    private List<CabinClass> cabinClasses;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumOfCabinClass() {
        return numOfCabinClass;
    }

    public void setNumOfCabinClass(Integer numOfCabinClass) {
        this.numOfCabinClass = numOfCabinClass;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }

    public List<CabinClass> getCabinClasses() {
        return cabinClasses;
    }

    public void setCabinClasses(List<CabinClass> cabinClasses) {
        this.cabinClasses = cabinClasses;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
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

}
