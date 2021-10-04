package com.bolsadeideas.springboot.app.models.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;
import com.bolsadeideas.springboot.app.models.entity.Client;

@Repository
public class ClientDAO implements IClientDAO {

	@PersistenceContext
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<Client> findAll() {
		return this.em.createQuery("from Client").getResultList();
	}

	@Override
	public Client findById(Long id) {
		return this.em.find(Client.class, id);
	}

	@Override
	public void save(Client client) {

		if (client.getId() != null && client.getId() > 0L) {
			this.em.merge(client); // merge -> actualiza
		} else {
			this.em.persist(client); // persist -> crea
		}
	}

	@Override
	public void delete(Long id) {
		this.em.remove(this.findById(id));
	}

}
