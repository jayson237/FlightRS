/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightScheduleExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightScheduleExistException</code>
     * without detail message.
     */
    public FlightScheduleExistException() {
    }

    /**
     * Constructs an instance of <code>FlightScheduleExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightScheduleExistException(String msg) {
        super(msg);
    }
}
