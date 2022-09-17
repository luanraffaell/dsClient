package com.devsuperior.dsclient.services;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
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
	@Transactional
	public ClientDTO save(ClientDTO clientDTO) {
		Client client = new Client();
		modelMapper.modelMapper().map(clientDTO, client);
		client = clientRepository.save(client);
		return new ClientDTO(client);
	}
	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDTO) {
		Client client = clientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Client not found"));
		clientDTO.setId(client.getId());
		modelMapper.modelMapper().map(clientDTO, client);
		client = clientRepository.save(client);
		return new ClientDTO(client);
	}
	
	@Transactional
	public void remove(Long id) {
		try {
			clientRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new EntityNotFoundException("Entity not found");
		}
		
	}
}
