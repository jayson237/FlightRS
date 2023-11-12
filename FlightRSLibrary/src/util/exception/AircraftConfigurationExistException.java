/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class AircraftConfigurationExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>AircraftConfigurationExistException</code> without detail message.
     */
    public AircraftConfigurationExistException() {
    }

    /**
     * Constructs an instance of
     * <code>AircraftConfigurationExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public AircraftConfigurationExistException(String msg) {
        super(msg);
    }
}
