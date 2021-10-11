package com.bolsadeideas.springboot.app.models.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;
import com.bolsadeideas.springboot.app.models.dao.IProductDAO;
import com.bolsadeideas.springboot.app.models.dao.IReceiptDAO;
import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.entity.Receipt;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Service
public class ClientService implements IClientService {

	@Autowired
	private IClientDAO clientDAO;

	@Autowired
	private IProductDAO productDAO;

	@Autowired
	private IReceiptDAO receiptDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) this.clientDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		return this.clientDAO.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findById(Long id) {
		return this.clientDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Client client) {
		this.clientDAO.save(client);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		this.clientDAO.deleteById(id);
	}

	@Override
	@Transactional
	public List<Product> findByName(String term) {
		return this.productDAO.findByNameLikeIgnoreCase(String.format("%%%s%%", term));
	}

	@Override
	@Transactional
	public void saveReceipt(Receipt receipt) {
		this.receiptDAO.save(receipt);
	}

	@Override
	@Transactional(readOnly = true)
	public Product findProductById(Long id) {
		return this.productDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Receipt findReceiptById(Long id) {
		return this.receiptDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteReceipt(Long id) {
		this.receiptDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Receipt fetchReceiptByIdWithClientWithReceiptLineWithProduct(Long id) {
		return this.receiptDAO.fetchByIdWithClientWithReceiptLineWithProduct(id);
	}

}
