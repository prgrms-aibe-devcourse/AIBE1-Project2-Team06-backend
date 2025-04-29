package com.eum.example.dto.response;

import com.eum.example.dto.ExampleDto;

public record ExampleResponse(
		String data1,
		String data2
) {
	public static ExampleResponse from(ExampleDto dto) {
		return new ExampleResponse(dto.data1(), dto.data2());
	}
}
