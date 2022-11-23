package com.sb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.exception.ResourceNotFoundException;
import com.sb.model.Item;
import com.sb.repository.ItemRepository;
import com.sb.service.ItemService;

@RestController
public class ItemController {

	@Autowired
	ItemService is;
	@Autowired
	ItemRepository ir;

	@PostMapping("/insert")
	private ResponseEntity<Item> createItem(@RequestBody Item i1) {
		try {
			Item i = is.saveOrUpdate(i1);
			return new ResponseEntity<>(i, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/item/{ino}")
	public ResponseEntity<Item> getItemById(@PathVariable(value = "id") long Itemid)
			throws ResourceNotFoundException {
		Item it = ir.findById(Itemid)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Your Entered Product Number is not available in Database,Could you please try with other Prod Number :: "
								+ Itemid));
		return ResponseEntity.ok().body(it);
	}

	@GetMapping("/readAll")
	public ResponseEntity<List<Item>> getAllItems(@RequestParam(required = false) String name) {
		try {
			List<Item> items = new ArrayList<Item>();

			if (name == null)
				ir.findAll().forEach(items::add);

			else
				ir.findByNameContaining(name).forEach(items::add);

			if (items.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(items, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	private void deleteItem(@PathVariable("Ino") Long Ino) {
		is.delete(Ino);
	}

	@GetMapping("/get/{id}")

	private Item getItem(@PathVariable("Ino") Long Ino) {
		return is.getProductById(Ino);
	}

	@PutMapping("/update/{id}")
	private Item update(@RequestBody Item item) {
		is.saveOrUpdate(item);
		return item;
	}
	
}
