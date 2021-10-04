package com.bolsadeideas.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.bolsadeideas.springboot.app.models.dao.IClientDAO;
import com.bolsadeideas.springboot.app.models.entity.Client;

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

	@GetMapping("/form")
	public String create(Model model) {

		Client client = new Client();

		model.addAttribute("client", client);
		model.addAttribute("title", "Formulario de cliente");

		return "form";
	}

	@PostMapping("/form")
	public String save(@Valid Client client, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Formulario de cliente");
			
			return "form";
		}
		
		this.clientDAO.save(client);

		return "redirect:list";
	}

}
