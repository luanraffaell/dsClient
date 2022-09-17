package com.devsuperior.dsclient.dto;

import java.time.Instant;

import com.devsuperior.dsclient.entities.Client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClientDTO {	
	private Long id;
	private String name;
	private String cpf;
	private Double income;
	private Instant birthDate;
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
