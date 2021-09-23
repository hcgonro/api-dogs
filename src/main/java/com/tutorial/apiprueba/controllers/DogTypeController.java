package com.tutorial.apiprueba.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.apiprueba.exceptions.DogTypeNotFoundException;
import com.tutorial.apiprueba.models.DogType;
import com.tutorial.apiprueba.models.NewDogType;
import com.tutorial.apiprueba.repositories.DogTypeRepository;
import com.tutorial.apiprueba.utils.DogTypeModelAssembler;

@RestController
public class DogTypeController {
	
	@Autowired
	private DogTypeRepository dogTypeRepository;
	
	DogTypeModelAssembler dogTypeModelAssembler;
	
	DogTypeController(DogTypeRepository dogTypeRepository, DogTypeModelAssembler dogTypeModelAssembler) {
		this.dogTypeRepository = dogTypeRepository;
		this.dogTypeModelAssembler = dogTypeModelAssembler;
	}

	@GetMapping(path="dogs/types")
	public CollectionModel<EntityModel<DogType>> listDogTypes() {
		
		List<EntityModel<DogType>> dogTypes = dogTypeRepository.findAll()
				.stream()
				.map(dogTypeModelAssembler::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(dogTypes,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogTypeController.class).listDogTypes()).withSelfRel());
	}
	
	@GetMapping(path="dogs/types/{id}")
	public EntityModel<DogType> getDogType(@PathVariable int id) {
		
		DogType dogType = dogTypeRepository.findById(id)
				.orElseThrow();
		
		return dogTypeModelAssembler.toModel(dogType);
	}
	
	@PostMapping(path="dogs/types")
	public ResponseEntity<EntityModel<DogType>> createDogType(@RequestBody NewDogType newDogType) {
		
		DogType persistedDogType = new DogType(newDogType);
		
		EntityModel<DogType> entityModel = dogTypeModelAssembler.toModel(dogTypeRepository.save(persistedDogType));
		
		return ResponseEntity
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(entityModel);
	}
	
	@PutMapping("dogs/types/{id}")
	public ResponseEntity<EntityModel<DogType>> updateDogType(@RequestBody NewDogType newDogType, @PathVariable int id) {

		EntityModel<DogType> entityModel;
		ResponseEntity<EntityModel<DogType>> responseEntity;
		DogType persistedDogType;
		
		Optional<DogType> persistedDogTypeOptional = dogTypeRepository.findById(id);
		
		if(persistedDogTypeOptional.isPresent()) {
			persistedDogType = persistedDogTypeOptional.get();
			
			entityModel = dogTypeModelAssembler.toModel(dogTypeRepository.save(persistedDogType));
			responseEntity = ResponseEntity
					.ok()
					.body(entityModel);
		} else {
			persistedDogType = new DogType(newDogType);
			
			entityModel = dogTypeModelAssembler.toModel(dogTypeRepository.save(persistedDogType));
			responseEntity = ResponseEntity
					.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.body(entityModel);
		}
		
		return responseEntity;
	}
	
	@DeleteMapping(path="dogs/types/{id}")
	public ResponseEntity<String> deleteDogType(@PathVariable int id) {
		
		DogType dogType = dogTypeRepository.findById(id)
			.orElseThrow(() -> new DogTypeNotFoundException(id));
			
		dogTypeRepository.deleteById(dogType.getId());
		
		return ResponseEntity
			.ok()
			.body("Dog type " +id+ " has been removed succesfully");
	}

}