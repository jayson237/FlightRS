/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FlightSchedulePlanNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>FlightSchedulePlanNotFoundException</code> without detail message.
     */
    public FlightSchedulePlanNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>FlightSchedulePlanNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public FlightSchedulePlanNotFoundException(String msg) {
        super(msg);
    }
}
