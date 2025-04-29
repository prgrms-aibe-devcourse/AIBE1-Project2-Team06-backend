package com.eum.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eum.example.domain.Example;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Integer> {
}
