package com.tutorial.apiprueba.exceptions;

public class DogNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DogNotFoundException(int id) {
		super("Could not find dog: " +id);
	}
}