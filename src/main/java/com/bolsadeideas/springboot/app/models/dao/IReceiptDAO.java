package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Receipt;

public interface IReceiptDAO extends CrudRepository<Receipt, Long> {

	@Query("select r from Receipt r join fetch r.client c join fetch r.lines l join fetch l.product where r.id = ?1")
	public Receipt fetchByIdWithClientWithReceiptLineWithProduct(Long id);

}
