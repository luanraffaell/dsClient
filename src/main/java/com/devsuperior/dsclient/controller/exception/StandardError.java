package com.devsuperior.dsclient.controller.exception;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class StandardError {
	@Builder.Default
	private Instant timestamp = Instant.now();
	private Integer status;
	private String error;
	private String message;
	private String path;
	private List<Field> fields;
	
	@Builder
	@Getter
	public static class Field{
		private String name;
		private String userMessage;
	}
}
