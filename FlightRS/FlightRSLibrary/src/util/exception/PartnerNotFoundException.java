/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class PartnerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PartnerNotFoundException</code> without
     * detail message.
     */
    public PartnerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PartnerNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerNotFoundException(String msg) {
        super(msg);
    }
}
