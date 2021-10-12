package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.app.models.entity.Client;

public interface IClientDAO extends PagingAndSortingRepository<Client, Long> {

	@Query("select c from Client c left join fetch c.receipts where c.id = ?1")
	public Client fetchByIdWithReceipts(Long id);

}
