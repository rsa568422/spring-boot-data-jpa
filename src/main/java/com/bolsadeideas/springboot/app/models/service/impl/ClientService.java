package com.bolsadeideas.springboot.app.models.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Service
public class ClientService implements IClientService {

	@Autowired
	private IClientDAO clientDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return this.clientDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return this.clientDAO.findById(id);
	}

	@Override
	@Transactional
	public void save(Client client) {
		this.clientDAO.save(client);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clientDAO.delete(id);
	}

}
