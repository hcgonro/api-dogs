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

import com.tutorial.apiprueba.exceptions.DogNotFoundException;
import com.tutorial.apiprueba.exceptions.DogTypeNotFoundException;
import com.tutorial.apiprueba.models.Dog;
import com.tutorial.apiprueba.models.DogType;
import com.tutorial.apiprueba.models.NewDog;
import com.tutorial.apiprueba.repositories.DogRepository;
import com.tutorial.apiprueba.repositories.DogTypeRepository;
import com.tutorial.apiprueba.utils.DogModelAssembler;

@RestController
public class DogController {

	@Autowired
	private final DogRepository dogRepository;
	@Autowired
	private final DogTypeRepository dogTypeRepository;
	
	private final DogModelAssembler dogModelAssembler;
	
	DogController(DogRepository dogRepository, DogTypeRepository dogTypeRepository, DogModelAssembler dogModelAssembler) {
		this.dogRepository = dogRepository;
		this.dogTypeRepository = dogTypeRepository;
		this.dogModelAssembler = dogModelAssembler;
	}
	
	@GetMapping(path="dogs")
	public CollectionModel<EntityModel<Dog>> listDogs() {
		
		List<EntityModel<Dog>> dogs = dogRepository.findAll().stream()
		.map(dogModelAssembler::toModel)
		.collect(Collectors.toList());		
		
		return CollectionModel.of(dogs,
			WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DogController.class).listDogs()).withSelfRel());
	}
	
	@GetMapping(path="dogs/{id}")
	public EntityModel<Dog> getDog(@PathVariable int id) {
		
		Dog dog = dogRepository.findById(id)
				.orElseThrow(() -> new DogNotFoundException(id));
		
		return dogModelAssembler.toModel(dog);
	}
	
	@PostMapping(path="dogs")
	public ResponseEntity<EntityModel<Dog>> createDog(@RequestBody NewDog newDog) throws IllegalArgumentException {
		
		DogType dogType = dogTypeRepository.findById(newDog.getTypeId())
			.orElseThrow(() -> new DogTypeNotFoundException(newDog.getTypeId()));
		
		Dog persistedDog = new Dog(newDog, dogType);
		
		EntityModel<Dog> entityModel = dogModelAssembler.toModel(dogRepository.save(persistedDog));
		
		return ResponseEntity
			.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			.body(entityModel);
	}
	
	@PutMapping(path="dogs/{id}")
	public ResponseEntity<EntityModel<Dog>> updateDog(@RequestBody NewDog newDog, @PathVariable int id) {
		
		EntityModel<Dog> entityModel;
		ResponseEntity<EntityModel<Dog>> responseEntity;
		Dog persistedDog;

		DogType dogType = dogTypeRepository.findById(newDog.getTypeId())
			.orElseThrow(() -> new DogTypeNotFoundException(newDog.getTypeId()));
			
		Optional<Dog> persistedDogOptional = dogRepository.findById(id);
		
		if(persistedDogOptional.isPresent()) {
			persistedDog = persistedDogOptional.get();
			persistedDog.updateDog(newDog, dogType);
			
			entityModel = dogModelAssembler.toModel(dogRepository.save(persistedDog));
			responseEntity = ResponseEntity
					.ok()
					.body(entityModel);
		} else {
			persistedDog = new Dog(newDog, dogType) ;
			
			entityModel = dogModelAssembler.toModel(dogRepository.save(persistedDog));
			responseEntity = ResponseEntity
					.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
					.body(entityModel);
		}
		
		return responseEntity;
	}
	
	@DeleteMapping(path="dogs/{id}")
	public ResponseEntity<String> deleteDog(@PathVariable int id) {
		
		Dog dog = dogRepository.findById(id)
			.orElseThrow(() -> new DogNotFoundException(id));
		
		dogRepository.deleteById(dog.getId());
		
		return ResponseEntity
			.ok()
			.body("Dog " +id+ " has been removed successfully");
	}
	
}