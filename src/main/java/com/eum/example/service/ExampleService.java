package com.eum.example.service;

import org.springframework.stereotype.Service;

import com.eum.example.model.dto.ExampleDto;
import com.eum.example.model.dto.request.ExampleReqeust;
import com.eum.example.model.repository.ExampleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExampleService {

	private final ExampleRepository exampleRepository;

	public ExampleDto addExample(ExampleReqeust reqeust) {
		return ExampleDto.from("data1", "data2");
	}
}
