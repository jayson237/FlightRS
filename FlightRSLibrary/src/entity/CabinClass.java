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
    private String seatingConfiguration;

    @Column(nullable = false)
    private Integer maxCapacity;

    @OneToMany(mappedBy = "cabinClass")
    private List<Fare> fares;

    @OneToOne(optional = false)
    private SeatInventory inventory;

    public CabinClass() {
    }

    public CabinClass(CabinClassType type, Integer numberOfAisles, Integer numOfRows, Integer numOfSeatsAbreast, String seatingConfiguration, Integer maxCapacity) {
        this.type = type;
        this.numberOfAisles = numberOfAisles;
        this.numOfRows = numOfRows;
        this.numOfSeatsAbreast = numOfSeatsAbreast;
        this.maxCapacity = maxCapacity;
        this.seatingConfiguration = seatingConfiguration;
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

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public SeatInventory getInventory() {
        return inventory;
    }

    public void setInventory(SeatInventory inventory) {
        this.inventory = inventory;
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

}
