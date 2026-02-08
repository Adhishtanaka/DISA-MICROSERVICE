package com.example.personnel_service.repository;

import com.example.personnel_service.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
