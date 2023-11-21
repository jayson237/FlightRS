/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jayso
 */
public class ConfigurationExceedCapacity extends Exception {

    /**
     * Creates a new instance of <code>ConfigurationExceedCapacity</code>
     * without detail message.
     */
    public ConfigurationExceedCapacity() {
    }

    /**
     * Constructs an instance of <code>ConfigurationExceedCapacity</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConfigurationExceedCapacity(String msg) {
        super(msg);
    }
}
