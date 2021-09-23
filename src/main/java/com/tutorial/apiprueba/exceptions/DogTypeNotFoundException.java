package com.tutorial.apiprueba.exceptions;

public class DogTypeNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DogTypeNotFoundException(int id) {
		super("Could not find dog type: " +id);
	}
}