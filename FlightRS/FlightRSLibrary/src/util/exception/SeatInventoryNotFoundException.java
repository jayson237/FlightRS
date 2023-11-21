/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class SeatInventoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>SeatInventoryException</code> without
     * detail message.
     */
    public SeatInventoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SeatInventoryException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatInventoryNotFoundException(String msg) {
        super(msg);
    }
}
