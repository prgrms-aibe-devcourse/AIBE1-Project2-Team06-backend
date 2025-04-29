package com.eum.example.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eum.example.model.entity.Example;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Integer> {
}
