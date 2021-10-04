package com.bolsadeideas.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/list")
	public String list(Model model) {

		model.addAttribute("title", "Listado de clientes");
		model.addAttribute("clients", this.clientService.findAll());

		return "list";
	}

	@GetMapping("/form")
	public String create(Model model) {

		Client client = new Client();

		model.addAttribute("client", client);
		model.addAttribute("title", "Formulario de cliente");

		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, Model model) {

		Client client = null;

		if (id != null && !id.equals(0L)) {
			client = this.clientService.findById(id);
		} else {
			return "redirect:/list";
		}

		model.addAttribute("title", "Editar cliente");
		model.addAttribute("client", client);

		return "form";
	}

	@PostMapping("/form")
	public String save(@Valid Client client, BindingResult result, Model model, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Formulario de cliente");

			return "form";
		}

		this.clientService.save(client);
		status.setComplete();

		return "redirect:/list";
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id) {

		if (id != null && !id.equals(0L)) {
			this.clientService.delete(id);
		}

		return "redirect:/list";
	}

}
