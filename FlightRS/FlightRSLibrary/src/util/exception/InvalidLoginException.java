/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class InvalidLoginException extends Exception {

    /**
     * Creates a new instance of <code>InvalidLoginException</code> without
     * detail message.
     */
    public InvalidLoginException() {
    }

    /**
     * Constructs an instance of <code>InvalidLoginException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidLoginException(String msg) {
        super(msg);
    }
}
