package com.tutorial.apiprueba.utils;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.tutorial.apiprueba.controllers.DogController;
import com.tutorial.apiprueba.models.Dog;

@Component
public class DogModelAssembler implements RepresentationModelAssembler<Dog, EntityModel<Dog>> {

	@Override
	public EntityModel<Dog> toModel(Dog dog) {
		
		return EntityModel.of(dog, 
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogController.class).getDog(dog.getId())).withSelfRel(),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogController.class).listDogs()).withRel("dogs"));
	}

	
}