package com.bolsadeideas.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Receipt;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@RequestMapping("/receipt")
@SessionAttributes("receipt")
public class ReceiptController {

	@Autowired
	private IClientService clientService;

	@GetMapping("/form/{clientId}")
	public String create(@PathVariable(value = "clientId") Long clientId, Model model, RedirectAttributes flash) {
		Client client = this.clientService.findById(clientId);

		if (client == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/list";
		}

		Receipt receipt = new Receipt();
		receipt.setClient(client);

		model.addAttribute("receipt", receipt);
		model.addAttribute("title", "Crear Factura");

		return "receipt/form";
	}

}
