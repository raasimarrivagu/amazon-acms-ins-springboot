package org.amazon.ins.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when the corresponding resource is not found.
 * @author Raasi 
 *
 */

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException() {
        super();
    }
	
    public ResourceNotFoundException( String msg) {
    	super(msg);
    }

    public ResourceNotFoundException( String msg,Throwable cause) {
        super(msg,cause);
    }
    
}
