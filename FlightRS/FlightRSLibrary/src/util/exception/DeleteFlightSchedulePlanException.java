/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class DeleteFlightSchedulePlanException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFlightSchedulePlanException</code>
     * without detail message.
     */
    public DeleteFlightSchedulePlanException() {
    }

    /**
     * Constructs an instance of <code>DeleteFlightSchedulePlanException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFlightSchedulePlanException(String msg) {
        super(msg);
    }
}
