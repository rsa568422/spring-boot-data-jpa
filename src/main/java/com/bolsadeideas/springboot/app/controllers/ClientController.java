package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;

@Controller
public class ClientController {

	@Autowired
	private IClientDAO clientDAO;

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("title", "Listado de clientes");
		model.addAttribute("clients", this.clientDAO.findAll());

		return "list";
	}

}
