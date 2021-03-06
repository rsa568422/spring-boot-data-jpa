package com.bolsadeideas.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.entity.Receipt;

public interface IClientService {

	public List<Client> findAll();

	public Page<Client> findAll(Pageable pageable);

	public Client findById(Long id);

	public Client fetchByIdWithReceipt(Long id);

	public void save(Client client);

	public void delete(Long id);

	public List<Product> findByName(String term);

	public void saveReceipt(Receipt receipt);

	public Product findProductById(Long id);

	public Receipt findReceiptById(Long id);

	public Receipt fetchReceiptByIdWithClientWithReceiptLineWithProduct(Long id);

	public void deleteReceipt(Long id);

}
