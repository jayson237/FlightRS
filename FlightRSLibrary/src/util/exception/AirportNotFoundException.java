/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class AirportNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>AirportNotFoundException</code> without
     * detail message.
     */
    public AirportNotFoundException() {
    }

    /**
     * Constructs an instance of <code>AirportNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AirportNotFoundException(String msg) {
        super(msg);
    }
}
