package com.eum.example.model.dto.response;

import com.eum.example.model.dto.ExampleDto;

public record ExampleResponse(
		String data1
) {
	public static ExampleResponse from(ExampleDto dto) {
		return new ExampleResponse(dto.data1());
	}
}
