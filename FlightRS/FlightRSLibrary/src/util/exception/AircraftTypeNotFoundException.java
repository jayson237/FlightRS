/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class AircraftTypeNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AircraftTypeNotFoundException</code>
     * without detail message.
     */
    public AircraftTypeNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AircraftTypeNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftTypeNotFoundException(String msg) {
        super(msg);
    }
}
