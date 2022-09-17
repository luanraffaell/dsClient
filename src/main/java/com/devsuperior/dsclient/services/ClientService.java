package com.devsuperior.dsclient.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.devsuperior.dsclient.core.ModelMapperConfig;
import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entities.Client;
import com.devsuperior.dsclient.repositories.ClientRepository;
import com.devsuperior.dsclient.services.exception.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClientService {
	private ClientRepository clientRepository;
	private ModelMapperConfig modelMapper;
	
	public Page<ClientDTO> listAllPaged(PageRequest pageRequest){
		Page<Client> findAll = clientRepository.findAll(pageRequest);	
		return findAll.map(x -> new ClientDTO(x));
	}

	public ClientDTO findById(Long id) {
		Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
		ClientDTO clientDTO = new ClientDTO();
		modelMapper.modelMapper().map(client, clientDTO);
		return clientDTO;
	}
}
