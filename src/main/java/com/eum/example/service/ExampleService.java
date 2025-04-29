package com.eum.example.service;

import org.springframework.stereotype.Service;

import com.eum.example.dto.ExampleDto;
import com.eum.example.dto.request.ExampleReqeust;
import com.eum.example.repository.ExampleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExampleService {

	private final ExampleRepository exampleRepository;

	public ExampleDto postExample(ExampleReqeust reqeust) {
		return ExampleDto.from("data1", "data2");
	}
}
