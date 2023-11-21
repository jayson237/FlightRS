/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class PartnerExistException extends Exception {

    /**
     * Creates a new instance of <code>PartnerExistException</code> without
     * detail message.
     */
    public PartnerExistException() {
    }

    /**
     * Constructs an instance of <code>PartnerExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerExistException(String msg) {
        super(msg);
    }
}
