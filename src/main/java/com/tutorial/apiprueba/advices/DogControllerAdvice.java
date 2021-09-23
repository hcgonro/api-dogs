package com.tutorial.apiprueba.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tutorial.apiprueba.exceptions.DogNotFoundException;

@ControllerAdvice
public class DogControllerAdvice {
	
	@ResponseBody
	@ExceptionHandler(DogNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String dogNotFoundHandler(DogNotFoundException e) {
		return e.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String illegalArgumentHandler(IllegalArgumentException e) {
		return e.getMessage();
	}
}