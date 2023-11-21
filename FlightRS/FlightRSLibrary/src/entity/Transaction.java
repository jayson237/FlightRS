/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author timothy
 */
@Entity
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false, length = 20)
    private String ccNumber;

    @Column(nullable = false, length = 3)
    private String cvv;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date ccExpiryDate;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false)
    private Customer customer;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transaction", cascade = CascadeType.DETACH)
    private List<FlightReservation> flightReservations;

    public Transaction() {
        this.flightReservations = new ArrayList<>();
    }

    public Transaction(String ccNumber, String cvv, Date ccExpiryDate) {
        this.ccNumber = ccNumber;
        this.cvv = cvv;
        this.ccExpiryDate = ccExpiryDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Date getCcExpiryDate() {
        return ccExpiryDate;
    }

    public void setCcExpiryDate(Date ccExpiryDate) {
        this.ccExpiryDate = ccExpiryDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<FlightReservation> getFlightReservations() {
        return flightReservations;
    }

    public void setFlightReservations(List<FlightReservation> flightReservations) {
        this.flightReservations = flightReservations;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionId != null ? transactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionId fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.transactionId == null && other.transactionId != null) || (this.transactionId != null && !this.transactionId.equals(other.transactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Transaction[ id=" + transactionId + " ]";
    }

}
