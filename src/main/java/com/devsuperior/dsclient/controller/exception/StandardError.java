package com.devsuperior.dsclient.controller.exception;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StandardError {
	private Instant timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
}
