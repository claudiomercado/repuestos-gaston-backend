package com.repuestosgaston.products.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repuestosgaston.products.controller.dto.ProductRequestDTO;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/product")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(path = "/")
	public ResponseEntity<Page<ProductResponseDTO>> getAllProduct(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(productService.getAllProduct(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("ProductController.getAllProduct - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{productId}")
	public ResponseEntity<ProductResponseDTO> getProductById(Long productId){
		try {
			return ResponseEntity.ok().body(productService.getProductById(productId));
		} catch (Exception e) {
			log.error(String.format("ProductController.getProductById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(path = "/")
	public ResponseEntity<Object> createProduct(@RequestBody ProductRequestDTO productRequestDTO){
		try {
			productService.createProduct(productRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("ProductController.createProduct - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(path = "/chargeImage")
	public ResponseEntity<Object> uploadImagenProduct(@RequestBody ProductRequestDTO productRequestDTO){
		try {
			productService.createProduct(productRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("ProductController.createProduct - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/")
	public ResponseEntity<ProductEntity> updateProduct(@RequestBody ProductEntity product){
		try {
			productService.updateProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("ProductController.updateProduct - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<ProductEntity> deleteProductById(Long id){
		try {
			productService.deleteProductById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error(String.format("ProductController.deleteProductById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
}
