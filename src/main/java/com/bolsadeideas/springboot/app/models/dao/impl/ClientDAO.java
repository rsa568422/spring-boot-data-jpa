package com.bolsadeideas.springboot.app.models.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;
import com.bolsadeideas.springboot.app.models.entity.Client;

@Repository
public class ClientDAO implements IClientDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return this.em.createQuery("from Client").getResultList();
	}

	@Override
	public Client findById(Long id) {
		return this.em.find(Client.class, id);
	}

	@Override
	@Transactional
	public void save(Client client) {

		if (client.getId() != null && client.getId() > 0L) {
			this.em.merge(client); // merge -> actualiza
		} else {
			this.em.persist(client); // persist -> crea
		}
	}

}
