package com.repuestosgaston.products.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.products.controller.dto.ProductRequestDTO;
import com.repuestosgaston.products.controller.dto.ProductResponseDTO;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.ProductRepository;

@Service 
public class ProductService {
	
	private final ModelMapper modelMapper;
	private final ProductRepository productRepository;
	
	public ProductService(ModelMapper modelMapper, ProductRepository productRepository) {
		this.modelMapper = modelMapper;
		this.productRepository = productRepository;
	}

	public Page<ProductResponseDTO> getAllProduct(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return productRepository.findAll(pageable)
				.map(product -> modelMapper.map(product, ProductResponseDTO.class));
	}
	
	public ProductResponseDTO getProductById(Long productId) {
		var productEntity = productRepository.findById(productId);
		if (!productEntity.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Product [%s] not found", productId));
		}
		return modelMapper.map(productEntity.get(), ProductResponseDTO.class);
	}

	//Recibe un ProductRequestDTO
	public void createProduct(ProductRequestDTO productRequestDTO) {
		ProductEntity productEntity = modelMapper.map(productRequestDTO, ProductEntity.class);
		productRepository.save(productEntity);
	}

	public void updateProduct(ProductEntity product) {
		//Logica para modificar el producto en base al producto que recibe
		productRepository.save(product);
	}
	
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);
	}
}
