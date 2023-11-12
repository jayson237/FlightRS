/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class InputDataValidationException extends Exception {

    /**
     * Creates a new instance of <code>InputDataValidationException</code>
     * without detail message.
     */
    public InputDataValidationException() {
    }

    /**
     * Constructs an instance of <code>InputDataValidationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InputDataValidationException(String msg) {
        super(msg);
    }
}
