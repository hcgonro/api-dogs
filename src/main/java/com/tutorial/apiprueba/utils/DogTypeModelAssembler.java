package com.tutorial.apiprueba.utils;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.tutorial.apiprueba.controllers.DogController;
import com.tutorial.apiprueba.models.DogType;

public class DogTypeModelAssembler implements RepresentationModelAssembler<DogType, EntityModel<DogType>> {

	@Override
	public EntityModel<DogType> toModel(DogType dogType) {
		
		return EntityModel.of(dogType, 
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogController.class).getDog(dogType.getId())).withSelfRel(),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogController.class).listDogs()).withRel("dogs"));
	}

	
	
}