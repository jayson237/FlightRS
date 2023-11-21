/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class AircraftConfigurationNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>AircraftConfigurationNotFoundException</code> without detail
     * message.
     */
    public AircraftConfigurationNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>AircraftConfigurationNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AircraftConfigurationNotFoundException(String msg) {
        super(msg);
    }
}
