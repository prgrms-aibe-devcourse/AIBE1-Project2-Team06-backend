package com.eum.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eum.example.model.dto.request.ExampleReqeust;
import com.eum.example.model.dto.response.ExampleResponse;
import com.eum.example.service.ExampleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/examples")
public class ExampleController {

	private final ExampleService exampleService;

	@PostMapping
	public ResponseEntity<ExampleResponse> createExample(
			@RequestBody ExampleReqeust reqeust
	) {

		return ResponseEntity.ok(
				ExampleResponse.from(
						exampleService.addExample(reqeust)
				)
		);
	}
}
