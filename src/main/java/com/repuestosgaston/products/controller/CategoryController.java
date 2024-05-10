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

import com.repuestosgaston.products.controller.dto.CategoryRequestDTO;
import com.repuestosgaston.products.controller.dto.CategoryReponseDTO;
import com.repuestosgaston.products.model.CategoryEntity;
import com.repuestosgaston.products.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/category")
public class CategoryController {

	private final CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping(path = "/")
	public ResponseEntity<Page<CategoryReponseDTO>> getAllCategory(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(categoryService.getAllCategory(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("CategoryController.getAllCategory - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<CategoryReponseDTO> getCategoryById(@PathVariable("id") Long categoryId){
		try {
			return ResponseEntity.ok().body(categoryService.getCategoryById(categoryId));
		} catch (IllegalArgumentException e) {
			log.error(String.format("CategoryController.getCategoryById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@PostMapping(path = "/")
	public ResponseEntity<Object> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO){
		try {
			categoryService.createCategory(categoryRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("CategoryController.createCategory - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<CategoryEntity> updateCategoryById(@PathVariable("id") Long categoryId,@RequestBody CategoryRequestDTO categoryRequestDTO){
		try {
			categoryService.updateCategoryById(categoryId,categoryRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("CategoryController.updateCategory - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<CategoryEntity> deleteCategoryById(@PathVariable("id") Long categoryId){
		try {
			categoryService.deleteCategoryById(categoryId);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			log.error(String.format("CategoryController.deleteCategoryById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}		
	}
}
