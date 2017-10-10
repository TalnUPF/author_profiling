/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upf.taln.profiling.author_profiling.commons.pojos.output;

/**
 *
 * @author rcarlini
 */
public class ProfilingException extends Exception {

    public ProfilingException(String message) {
        super(message);
    }

    public ProfilingException(Throwable cause) {
        super(cause);
    }

    public ProfilingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
