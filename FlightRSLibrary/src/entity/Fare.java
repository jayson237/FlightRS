/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import util.enumeration.CabinClassType;

/**
 *
 * @author timothy
 */
@Entity
public class Fare implements Serializable {

    /**
     * @return the flightReservation
     */
    public FlightReservation getFlightReservation() {
        return flightReservation;
    }

    /**
     * @param flightReservation the flightReservation to set
     */
    public void setFlightReservation(FlightReservation flightReservation) {
        this.flightReservation = flightReservation;
    }

    /**
     * @return the cabinClass
     */
    public CabinClassType getCabinClass() {
        return cabinClassType;
    }

    /**
     * @param cabinClass the cabinClass to set
     */
    public void setCabinClass(CabinClassType cabinClass) {
        this.cabinClassType = cabinClass;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fareId;
    @Column(nullable = false, length = 32, unique = true)
    private String code;
    @Column(precision = 11, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private CabinClassType cabinClassType;
    @OneToOne
    @JoinColumn(name = "flight_reservation_id")
    private FlightReservation flightReservation;
    
    

    public Fare() {
    }

    public Fare(String code, BigDecimal amount) {
        this.code = code;
        this.amount = amount;
    }

    public Long getFareId() {
        return fareId;
    }

    public void setFareId(Long fareId) {
        this.fareId = fareId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fareId != null ? fareId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the fareId fields are not set
        if (!(object instanceof Fare)) {
            return false;
        }
        Fare other = (Fare) object;
        if ((this.fareId == null && other.fareId != null) || (this.fareId != null && !this.fareId.equals(other.fareId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Fare[ id=" + fareId + " ]";
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
}
