package com.eum.example.dto;

public record ExampleDto(
		String data1,
		String data2
) {
	public static ExampleDto from(
			String data1,
			String data2
	) {
		return new ExampleDto(data1, data2);
	}
}
