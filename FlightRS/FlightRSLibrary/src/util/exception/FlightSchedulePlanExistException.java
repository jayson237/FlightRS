/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightSchedulePlanExistException extends Exception {

    /**
     * Creates a new instance of <code>FlightSchedulePlanExistException</code>
     * without detail message.
     */
    public FlightSchedulePlanExistException() {
    }

    /**
     * Constructs an instance of <code>FlightSchedulePlanExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulePlanExistException(String msg) {
        super(msg);
    }
}
