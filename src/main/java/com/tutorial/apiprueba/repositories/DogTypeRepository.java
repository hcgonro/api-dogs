package com.tutorial.apiprueba.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tutorial.apiprueba.models.DogType;

public interface DogTypeRepository extends JpaRepository<DogType, Integer>{

}
