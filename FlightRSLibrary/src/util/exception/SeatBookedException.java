/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class SeatBookedException extends Exception {

    /**
     * Creates a new instance of <code>SeatBookedException</code> without detail
     * message.
     */
    public SeatBookedException() {
    }

    /**
     * Constructs an instance of <code>SeatBookedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SeatBookedException(String msg) {
        super(msg);
    }
}
