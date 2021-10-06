package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Client;
import com.bolsadeideas.springboot.app.models.service.IClientService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	@Autowired
	private IClientService clientService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final static String UPLOADS_FOLDER = "uploads";

	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
		Path photo = Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();

		this.log.info("photo: ".concat(photo.toString()));

		Resource resource = null;

		try {
			resource = new UrlResource(photo.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("Error: no se puede cargar la imagen ".concat(photo.toString()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@GetMapping("/see/{id}")
	public String see(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Client client = this.clientService.findById(id);

		if (client == null) {
			flash.addAttribute("error", "Cliente no encontrado");
			return "redirect:/list";
		}

		model.addAttribute("title", "Detalles cliente: ".concat(client.getName()));
		model.addAttribute("client", client);

		return "see";
	}

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
			@RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Formulario de cliente");
			return "form";
		}

		if (!photo.isEmpty()) {
			if (client.getId() != null && client.getId() > 0 && client.getPhoto() != null
					&& client.getPhoto().length() > 0) {
				this.removePhoto(client.getId(), flash);
			}

			String fileName = String.format("%s_%s", UUID.randomUUID().toString().toUpperCase(),
					photo.getOriginalFilename().toLowerCase().trim().replace(" ", "_"));

			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();

			this.log.info("rootPath: ".concat(rootPath.toString()));

			try {
				Files.copy(photo.getInputStream(), rootPath);

				client.setPhoto(fileName);

				flash.addFlashAttribute("info", String.format("Imagen subida correctamente: '%s'", fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			this.removePhoto(id, flash);

			this.clientService.delete(id);

			flash.addFlashAttribute("success", "Cliente eliminado con éxito");
		}

		return "redirect:/list";
	}

	private void removePhoto(Long id, RedirectAttributes flash) {
		Client client = this.clientService.findById(id);

		if (client != null && client.getPhoto() != null && client.getPhoto().length() > 0) {
			String filename = client.getPhoto();
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(client.getPhoto()).toAbsolutePath();
			File photo = rootPath.toFile();

			if (photo.exists() && photo.canRead() && photo.delete()) {
				flash.addFlashAttribute("info", String.format("Foto '%s' eliminada con éxito", filename));
			}
		}
	}

}
