package com.campusretail.productcatalogservice.controller;

import com.campusretail.productcatalogservice.dto.WriteProductDto;
import com.campusretail.productcatalogservice.entity.Product;
import com.campusretail.productcatalogservice.exception.CustomizedResponseEntityException;
import com.campusretail.productcatalogservice.exception.ProductNotFoundException;
import com.campusretail.productcatalogservice.exception.RequestEmptyException;
import com.campusretail.productcatalogservice.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controller to manage all the advanced
 * operations for the product entity
 * by the admin
 */
@RestController
@RequestMapping("/admin")
public class AdminProductController {

	private final ProductService service;
	private final ModelMapper mapper;

	@Autowired
	public AdminProductController(ProductService service, ModelMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	/**
	 * Endpoint which adds to the database a new
	 * product using a DTO to write it
	 *
	 * @param writeProductDto the product template to
	 *                        add into the database
	 * @return an HTTP response according to the obtained scenario
	 */
	@PostMapping("/products")
	private ResponseEntity<Product> addProduct(@RequestBody WriteProductDto writeProductDto, @RequestHeader("X-auth-role") String role) throws ExecutionException, InterruptedException {
		if (!role.equals("Admin"))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		if (writeProductDto.isValid()) {
			Product product = service.saveProduct(this.mapper.map(writeProductDto, Product.class)).get();
			if (product != null)
				return new ResponseEntity<>(product, HttpStatus.CREATED);

			throw new RuntimeException("Unable to add product");
		}
		throw new RequestEmptyException("The request should not be empty");
	}


	/**
	 * Endpoint which delete a specific
	 * product from the database
	 *
	 * @param id is the id to the wanted product to delete
	 *           from the database
	 * @return an HTTP response according to the obtained scenario
	 * @throws Exception in case of bod working of asynchronous methods
	 */
	@DeleteMapping("/products/{id}")
	private ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader("X-auth-role") String role) throws Exception {
		if (!role.equals("Admin"))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
		Product product = service.deleteProduct(id).get();
		if (product != null)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		throw new ProductNotFoundException("The product with id " + id + " was not found");
	}

	/**
	 * Endpoint which update a specific product
	 * in the database searched by id
	 *
	 * @param id the id of the wanted product to update
	 * @return an HTTP response according to the obtained scenario
	 * @throws Exception in case of bod working of asynchronous methods
	 */
	@PutMapping("/products/{id}")
	private ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestHeader("X-auth-role") String role) throws Exception {
		if (!role.equals("Admin"))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		Product product = service.getProductById(id).get();
		if (product != null)
			try {
				service.saveProduct(product).get();
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		throw new ProductNotFoundException("The product with id " + id + " was not found");
	}
}
