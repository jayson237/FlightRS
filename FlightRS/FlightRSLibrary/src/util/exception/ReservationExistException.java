/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class ReservationExistException extends Exception {

    /**
     * Creates a new instance of <code>ReservationExistException</code> without
     * detail message.
     */
    public ReservationExistException() {
    }

    /**
     * Constructs an instance of <code>ReservationExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationExistException(String msg) {
        super(msg);
    }
}
