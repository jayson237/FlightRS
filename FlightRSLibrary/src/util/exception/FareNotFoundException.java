/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class FareNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FareNotFoundException</code> without
     * detail message.
     */
    public FareNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FareNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FareNotFoundException(String msg) {
        super(msg);
    }
}
