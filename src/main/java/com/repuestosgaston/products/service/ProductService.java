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
import com.repuestosgaston.products.converter.ProductEntityToProductResponse;
import com.repuestosgaston.products.converter.ProductRequestToProductEntity;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.CategoryRepository;
import com.repuestosgaston.products.repository.ProductRepository;

@Service 
public class ProductService {
	
	private final ModelMapper modelMapper;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductEntityToProductResponse productEntityToProductResponse;
	private final ProductRequestToProductEntity productRequestToProductEntity;
	
	public ProductService(ModelMapper modelMapper, ProductRepository productRepository, CategoryRepository categoryRepository,ProductEntityToProductResponse productEntityToProductResponse,ProductRequestToProductEntity productRequestToProductEntity) {
		this.modelMapper = modelMapper;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.productEntityToProductResponse = productEntityToProductResponse;
		this.productRequestToProductEntity = productRequestToProductEntity;
	}

	public Page<ProductResponseDTO> getAllProduct(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
 
        return productRepository.findAll(pageable)
				.map(productEntityToProductResponse::convert);
	}
	
	public ProductResponseDTO getProductById(Long productId) {
		return productRepository.findById(productId).map(productEntityToProductResponse::convert)
	            .orElseThrow(() -> new IllegalArgumentException(
	                    String.format("Product [%s] not found", productId)));
	}
	
	public Page<ProductResponseDTO> getProductByName(int page,int size,String sort,String sortDirection,String name) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
        
		return productRepository.filterToName(pageable,name).map(productEntityToProductResponse::convert);
	}
	
	public Page<ProductResponseDTO> getProductByCategory(int page,int size,String sort,String sortDirection,Long category) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
        
		return productRepository.filterToCategory(pageable,category).map(productEntityToProductResponse::convert);
	}

	public void createProduct(ProductRequestDTO productRequestDTO) {
		productRepository.save(productRequestToProductEntity.convert(productRequestDTO));
	}

	public void updateProductById(Long productId,ProductRequestDTO productRequestDTO) {
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Product [%s] not found", productId));
		}

		productRepository.save(updateFields(productEntity.get(),productRequestDTO));
	}
	
	public void deleteProductById(Long id) {
		Optional<ProductEntity> productEntity = productRepository.findById(id);
		if (productEntity.isEmpty()) {
			throw new IllegalArgumentException(
					String.format("Product [%s] not found", id)); 
		}
		productRepository.deleteById(productEntity.get().getId());
	}
	
	private ProductEntity updateFields(ProductEntity productEntity, ProductRequestDTO productRequestDTO) {
		if(productRequestDTO.getIdCategory() != null) {
			var category = categoryRepository.findById(productRequestDTO.getIdCategory());
			if (category.isEmpty()) {
				throw new IllegalArgumentException(
						String.format("Category [%s] not found", productRequestDTO.getIdCategory()));
			}
			productEntity.setCategory(category.get());
		}
		
		if (productRequestDTO.getBarCode() != null) {
			Integer barCode = productRequestDTO.getBarCode();
			if(productRepository.findByBarCode(barCode).isPresent()) {
				throw new IllegalArgumentException(
					String.format("Bar code existente"));
				}
			productEntity.setBarCode(barCode);
			}
		
		if (productRequestDTO.getName() != null) {
			productEntity.setName(productRequestDTO.getName());
	    }
	    if (productRequestDTO.getDescription() != null) {
	        productEntity.setDescription(productRequestDTO.getDescription());
	    }
	    if (productRequestDTO.getPrice() != null) {
	        productEntity.setPrice(productRequestDTO.getPrice());
	    }
	    if (productRequestDTO.getStock() != null) {
	        productEntity.setStock(productRequestDTO.getStock());
	    }
	    if(productRequestDTO.getImage() != null) {
	    	productEntity.setImage(productRequestDTO.getImage());
	    }
	    
	    productEntity.setAmount(null);
		productEntity.setSub_total_price(null);
		
	    return productEntity;
	}
//	
//	public ProductEntity convertDTOtoEntity(ProductRequestDTO productRequestDTO) {
//		var category = categoryRepository.findById(productRequestDTO.getIdCategory());
//		
//		if (category.isEmpty()) {
//			throw new IllegalArgumentException(
//					String.format("Category [%s] not found", productRequestDTO.getIdCategory()));
//		}
//		ProductEntity productEntity = new ProductEntity();
//		productEntity.setName(productRequestDTO.getName());
//		productEntity.setDescription(productRequestDTO.getDescription());
//		productEntity.setPrice(productRequestDTO.getPrice());
//		productEntity.setStock(productRequestDTO.getStock());
//		productEntity.setBarCode(productRequestDTO.getBarCode());
//		productEntity.setAmount(null);
//		productEntity.setSub_total_price(null);
//		productEntity.setCategory(category.get());
//		
//		return productEntity;
//	}
}
