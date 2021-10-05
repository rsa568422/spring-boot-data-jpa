package com.bolsadeideas.springboot.app.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/list")
	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Client> clients = this.clientService.findAll(pageRequest);
		
		PageRender<Client> pageRender = PageRender.of("/list", clients);
		
		model.addAttribute("title", "Listado de clientes");
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);

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
	public String edit(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Client client = null;

		if (id != null && !id.equals(0L)) {
			client = this.clientService.findById(id);

			if (client == null) {
				flash.addFlashAttribute("error", "No se encuentra al cliente");
				return "redirect:/list";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente debe ser mayor que cero");
			return "redirect:/list";
		}

		model.addAttribute("title", "Editar cliente");
		model.addAttribute("client", client);

		return "form";
	}

	@PostMapping("/form")
	public String save(@Valid Client client, BindingResult result, Model model,
						RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Formulario de cliente");
			return "form";
		}
		
		String action = client.getId() != null ? "editado" : "creado";

		this.clientService.save(client);
		status.setComplete();

		flash.addFlashAttribute("success", String.format("Cliente %s con éxito", action));
		return "redirect:/list";
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id != null && !id.equals(0L)) {
			this.clientService.delete(id);

			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
		}

		return "redirect:/list";
	}

}
