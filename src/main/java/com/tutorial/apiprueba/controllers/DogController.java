package com.tutorial.apiprueba.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.apiprueba.models.Dog;
import com.tutorial.apiprueba.models.DogType;
import com.tutorial.apiprueba.models.NewDog;
import com.tutorial.apiprueba.repositories.DogRepository;
import com.tutorial.apiprueba.repositories.DogTypeRepository;

@RestController
public class DogController {

	@Autowired
	private DogRepository dogRepository;
	@Autowired
	private DogTypeRepository dogTypeRepository;
	
	@GetMapping(path="dogs")
	public ResponseEntity<List<Dog>> listDogs() {
		
		return new ResponseEntity<>(dogRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(path="dogs/{id}")
	public ResponseEntity<Dog> getDog(@PathVariable int id) {
		
		Optional<Dog> dogOptional = dogRepository.findById(id);
		if(dogOptional.isPresent()) {
			return new ResponseEntity<>(dogOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(path="dogs")
	public ResponseEntity<Dog> createDog(@RequestBody NewDog newDog) {
		
		Optional<DogType> dogTypeOptional = dogTypeRepository.findById(newDog.getTypeId());
		if(dogTypeOptional.isPresent()) {
			Dog persistedDog = new Dog(newDog, dogTypeOptional.get());
			return new ResponseEntity<>(dogRepository.save(persistedDog), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@PutMapping(path="dogs/{id}")
	public ResponseEntity<Dog> updateDog(@RequestBody NewDog newDog, @PathVariable int id) {
		
		Dog persistedDog;
		
		Optional<DogType> dogTypeOptional = dogTypeRepository.findById(newDog.getTypeId());
		if(dogTypeOptional.isPresent()) {
			Optional<Dog> dogOptional = dogRepository.findById(id);
			if(dogOptional.isPresent()) {
				persistedDog = dogOptional.get();
				persistedDog.updateDog(newDog, dogTypeOptional.get());
			} else {
				persistedDog = new Dog(newDog, dogTypeOptional.get());
			}
			return new ResponseEntity<>(dogRepository.save(persistedDog), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@DeleteMapping(path="dogs/{id}")
	public ResponseEntity<Dog> deleteDog(@PathVariable int id) {
		
		Optional<Dog> dogOptional = dogRepository.findById(id);
		if(dogOptional.isPresent()) {
			dogRepository.delete(dogOptional.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
