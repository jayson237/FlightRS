/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class CabinClassNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CabinClassNotFoundException</code>
     * without detail message.
     */
    public CabinClassNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CabinClassNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CabinClassNotFoundException(String msg) {
        super(msg);
    }
}
