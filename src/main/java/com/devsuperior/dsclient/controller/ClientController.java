package com.devsuperior.dsclient.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.services.ClientService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {
	
	private ClientService clientService;
	
	@GetMapping
	public ResponseEntity<Page<ClientDTO>> findAll(
			@RequestParam(name = "page",defaultValue = "0") Integer page,
			@RequestParam(name = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(name = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return ResponseEntity.ok().body(clientService.listAllPaged(pageRequest));	
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClientDTO> findById(Long id){
		return ResponseEntity.ok().body(clientService.findById(id));
	}
}
