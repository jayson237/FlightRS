/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class UpdateFlightException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFlightException</code> without
     * detail message.
     */
    public UpdateFlightException() {
    }

    /**
     * Constructs an instance of <code>UpdateFlightException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFlightException(String msg) {
        super(msg);
    }
}
