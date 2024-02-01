package com.repuestosgaston.products.service;

import java.util.Optional;

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
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty()) {
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

	public void updateProduct(Long product_id,ProductRequestDTO productDTO) {
		Optional<ProductEntity> productEntity = productRepository.findById(product_id);
		if (productEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Product [%s] not found", product_id));
		}
		
		productEntity.get().setName(productDTO.getName() != null ? productDTO.getName() : productEntity.get().getName());
		productEntity.get().setDescription(productDTO.getDescription() != null ? productDTO.getDescription() : productEntity.get().getDescription());
		productEntity.get().setPrice(productDTO.getPrice() != null ? productDTO.getPrice() : productEntity.get().getPrice());
		productEntity.get().setStock(productDTO.getStock() != null ? productDTO.getStock() : productEntity.get().getStock());
		productEntity.get().setBarCode(productDTO.getBarCode() != null ? productDTO.getBarCode() : productEntity.get().getBarCode());
//		productEntity.get().setImage(productDTO.getImage() != null ? productDTO.getImage() : productEntity.get().getImage());		
//		productEntity.get().setCategory(productDTO.getCategory() != null ? productDTO.getCategory() : productEntity.get().getCategory());
//		modelMapper.map(productDTO, ProductEntity.class);
		
		productRepository.save(productEntity.get());
	}
	
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);
	}
}
