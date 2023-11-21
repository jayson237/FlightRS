/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class UpdateFlightScheduleException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFlightScheduleException</code>
     * without detail message.
     */
    public UpdateFlightScheduleException() {
    }

    /**
     * Constructs an instance of <code>UpdateFlightScheduleException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFlightScheduleException(String msg) {
        super(msg);
    }
}
