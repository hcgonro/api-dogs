package com.tutorial.apiprueba.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tutorial.apiprueba.models.Dog;

public interface DogRepository extends JpaRepository<Dog, Integer>{
	
}
