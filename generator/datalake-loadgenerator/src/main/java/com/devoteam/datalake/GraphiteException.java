package com.devoteam.datalake;

/**
 * Class that catches Exceptions on Graphite writing errors
 * 
 * Devoteam
 *
 */
public class GraphiteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public GraphiteException(String message) {
		super(message);
	}

	public GraphiteException(String message, Throwable cause) {
		super(message, cause);
	}	
	
}
