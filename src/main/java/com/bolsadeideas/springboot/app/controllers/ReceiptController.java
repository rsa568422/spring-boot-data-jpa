package com.bolsadeideas.springboot.app.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.entity.Product;
import com.bolsadeideas.springboot.app.models.entity.Receipt;
import com.bolsadeideas.springboot.app.models.entity.ReceiptLine;
import com.bolsadeideas.springboot.app.models.service.IClientService;

@Controller
@RequestMapping("/receipt")
@SessionAttributes("receipt")
public class ReceiptController {

	@Autowired
	private IClientService clientService;

	private final Logger log = LoggerFactory.getLogger(ReceiptController.class);

	@GetMapping("/see/{id}")
	public String see(@PathVariable Long id, Model model, RedirectAttributes flash) {
		Receipt receipt = this.clientService.findReceiptById(id);

		if (receipt == null) {
			flash.addFlashAttribute("error", "La factura no existe en al base de datos");
			return "redirect:/list";
		}

		model.addAttribute("title", "Factura: ".concat(receipt.getDescription()));
		model.addAttribute("receipt", receipt);

		return "receipt/see";
	}

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

	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) {
		return this.clientService.findByName(term);
	}

	@PostMapping("/form")
	public String save(@Valid Receipt receipt, BindingResult result, Model model,
			@RequestParam(name = "line_id[]", required = false) Long[] lineId,
			@RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
			SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("title", "Crear Factura");

			return "receipt/form";
		}

		if (lineId == null || lineId.length == 0) {
			model.addAttribute("title", "Crear Factura");
			model.addAttribute("error", "Error: la factura debe contener alguna línea");

			return "receipt/form";
		}

		for (int i = 0; i < lineId.length; i++) {
			ReceiptLine line = new ReceiptLine();
			line.setProduct(this.clientService.findProductById(lineId[i]));
			line.setQuantity(quantity[i]);

			this.log.info(String.format("ID: %s, cantidad: %s", lineId[i], quantity[i]));

			receipt.addLine(line);
		}

		this.clientService.saveReceipt(receipt);

		status.setComplete();
		flash.addFlashAttribute("success", "Factura creada con éxito");

		return "redirect:/see/".concat(receipt.getClient().getId().toString());
	}

}
