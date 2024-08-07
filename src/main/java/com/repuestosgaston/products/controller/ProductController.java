package com.repuestosgaston.products.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id")Long productId){
		try {
			return ResponseEntity.ok().body(productService.getProductById(productId));
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.getProductById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping(path = "/filter/{name}/name")
	public ResponseEntity<Page<ProductResponseDTO>> getProductByName(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
			@PathVariable("name") String name){
		
		try {
			return ResponseEntity.ok().body(productService.getProductByName(page,
					size,sort,sortDirection,name));
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.getProductByName - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path = "/filter/{barCode}/barCode")
	public ResponseEntity<Object> getProductByBarCode(
			@PathVariable("barCode") Integer barCode){
		try {
			return ResponseEntity.ok().body(productService.getProductByBarcode(barCode));
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.getProductByName - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping(path = "/filter/{category}/category")
	public ResponseEntity<Page<ProductResponseDTO>> getProductByCategory(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
			@PathVariable("category") Long category){
		
		try {
			return ResponseEntity.ok().body(productService.getProductByCategory(page,
					size,sort,sortDirection,category));
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.getProductByCategory - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@GetMapping(path = "/filter/lowStock")
	public ResponseEntity<Page<ProductResponseDTO>> getProductByLowStock(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(productService.getProductByLowStock(page,
					size,sort,sortDirection));
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.getProductByLowStock - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<ProductEntity> updateProductById(@PathVariable("id") Long productId,@RequestBody ProductRequestDTO productRequestDTO){
		try {
			productService.updateProductById(productId,productRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.updateProduct - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<ProductEntity> deleteProductById(@PathVariable("id")Long id){
		try {
			productService.deleteProductById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("ProductController.deleteProductById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
}
