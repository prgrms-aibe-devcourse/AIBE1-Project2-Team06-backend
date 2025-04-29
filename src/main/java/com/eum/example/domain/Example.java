package com.eum.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "example")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Example {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public static Example of(
			String name
	) {
		Example example = new Example();
		example.name = name;
		return example;
	}
}
