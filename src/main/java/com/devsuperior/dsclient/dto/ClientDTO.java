package com.devsuperior.dsclient.dto;

import java.time.Instant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

import com.devsuperior.dsclient.entities.Client;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {
	
	@JsonIgnoreProperties(allowGetters = true)
	private Long id;
	
	@NotBlank(message = "Must not be blank or null")
	private String name;
	
	@NotBlank(message = "Must not be blank or null")
	private String cpf;
	
	@PositiveOrZero(message = "must be positive or zero")
	private Double income;
	
	private Instant birthDate;
	
	@PositiveOrZero(message = "must be positive or zero")
	private Integer children;
	
	public ClientDTO() {
	}
	
	public ClientDTO(Client entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
		this.income = entity.getIncome();
		this.birthDate = entity.getBirthDate();
		this.children = entity.getChildren();
	}
}
