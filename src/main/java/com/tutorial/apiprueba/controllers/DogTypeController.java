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

import com.tutorial.apiprueba.models.DogType;
import com.tutorial.apiprueba.models.NewDogType;
import com.tutorial.apiprueba.repositories.DogTypeRepository;

@RestController
public class DogTypeController {
	
	@Autowired
	private DogTypeRepository dogTypeRepository;

	@GetMapping(path="dogs/types")
	public ResponseEntity<List<DogType>> listDogTypes() {
		
		return new ResponseEntity<>(dogTypeRepository.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(path="dogs/types/{id}")
	public ResponseEntity<DogType> getDogType(@PathVariable int id) {
		
		Optional<DogType> dogTypeOptional = dogTypeRepository.findById(id);
		if(dogTypeOptional.isPresent()) {
			return new ResponseEntity<>(dogTypeOptional.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(path="dogs/types")
	public ResponseEntity<DogType> createDogType(@RequestBody NewDogType newDogType) {
		
		DogType persistedDogType = new DogType(newDogType);
		return new ResponseEntity<>(dogTypeRepository.save(persistedDogType), HttpStatus.OK);
	}
	
	@PutMapping("dogs/types/{id}")
	public ResponseEntity<DogType> updateDogType(@RequestBody NewDogType newDogType, @PathVariable int id) {
		DogType persistedDogType;
			
		Optional<DogType> dogTypeOptional = dogTypeRepository.findById(id);
		if(dogTypeOptional.isPresent()) {
			persistedDogType = dogTypeOptional.get();
			persistedDogType.updateDogType(newDogType);
		} else {
			persistedDogType = new DogType(newDogType);
		}
		
		return new ResponseEntity<>(dogTypeRepository.save(persistedDogType), HttpStatus.OK);
	}
	
	@DeleteMapping(path="dogs/types/{id}")
	public ResponseEntity<DogType> deleteDogType(@PathVariable int id) {
		
		Optional<DogType> dogTypeOptional = dogTypeRepository.findById(id);
		if(dogTypeOptional.isPresent()) {
			dogTypeRepository.delete(dogTypeOptional.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
