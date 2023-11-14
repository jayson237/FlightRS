/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class CabinClassExistException extends Exception {

    /**
     * Creates a new instance of <code>CabinClassExistException</code> without
     * detail message.
     */
    public CabinClassExistException() {
    }

    /**
     * Constructs an instance of <code>CabinClassExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CabinClassExistException(String msg) {
        super(msg);
    }
}
