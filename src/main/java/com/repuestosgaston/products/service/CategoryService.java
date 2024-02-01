package com.repuestosgaston.products.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.products.controller.dto.CategoryReponseDTO;
import com.repuestosgaston.products.controller.dto.CategoryRequestDTO;
import com.repuestosgaston.products.model.CategoryEntity;
import com.repuestosgaston.products.repository.CategoryRepository;

@Service 
public class CategoryService {
	
	private final ModelMapper modelMapper;
	private final CategoryRepository categoryRepository;

	public CategoryService(ModelMapper modelMapper, CategoryRepository categoryRepository) {
		this.modelMapper = modelMapper;
		this.categoryRepository = categoryRepository;
	}

	public Page<CategoryReponseDTO> getAllCategory(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return categoryRepository.findAll(pageable)
				.map(category -> modelMapper.map(category, CategoryReponseDTO.class));
	}
	
	public CategoryReponseDTO getCategoryById(Long categoryId) {
		var categoryEntity = categoryRepository.findById(categoryId);
		if (!categoryEntity.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Category [%s] not found", categoryId));
		}
		return modelMapper.map(categoryEntity.get(), CategoryReponseDTO.class);
	}

	//Recibe un CategoryRequestDTO
	public void createCategory(CategoryRequestDTO categoryRequestDTO) {
		CategoryEntity categoryEntity = modelMapper.map(categoryRequestDTO, CategoryEntity.class);
		categoryRepository.save(categoryEntity);
	}

	public void updateCategory(CategoryEntity categoryEntity) {
		//Logica para modificar la categoria en base a la categoria que recibe
		categoryRepository.save(categoryEntity);
	}
	
	public void deleteCategoryById(Long categoryId) {
		categoryRepository.deleteById(categoryId);
	}
	
}
