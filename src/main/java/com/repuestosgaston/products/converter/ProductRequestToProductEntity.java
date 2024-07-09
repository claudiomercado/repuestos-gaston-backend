package com.repuestosgaston.products.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.repuestosgaston.payment.service.PaymentService;
import com.repuestosgaston.products.controller.dto.ProductRequestDTO;
import com.repuestosgaston.products.model.ProductEntity;
import com.repuestosgaston.products.repository.CategoryRepository;

@Component
public class ProductRequestToProductEntity implements Converter<ProductRequestDTO, ProductEntity>{

	@Autowired
	private CategoryRepository categoryRepository;
	
    @Autowired
    private PaymentService paymentService;
	
	@Override
	public ProductEntity convert(ProductRequestDTO productRequestDTO) {
		ProductEntity productEntity = new ProductEntity();
		
		if(productRequestDTO.getIdCategory() != null) {
			var category = categoryRepository.findById(productRequestDTO.getIdCategory());
			if (category.isEmpty()) {
				throw new IllegalArgumentException(
						String.format("Category [%s] not found", productRequestDTO.getIdCategory()));
			}
			productEntity.setCategory(category.get());
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
	    if (productRequestDTO.getBarCode() != null) {
	        productEntity.setBarCode(productRequestDTO.getBarCode());
	    }
	    if (productRequestDTO.getImage() != null) {
	        productEntity.setImage(productRequestDTO.getImage());   
	    }
	    if(productRequestDTO.getAmount() == null) {
	    	productEntity.setAmount(0);
	    }
	    if(productRequestDTO.getSubTotalPrice() == null) {
	    	productEntity.setSub_total_price(0.0);
	    }
	    
//		productEntity.setSub_total_price(null);
		
		 try {
	            var product = paymentService.createProduct(productRequestDTO.getName(), productRequestDTO.getDescription());
	            var price = paymentService.createPrice(product.getId(), Math.round(productRequestDTO.getPrice() * 100)); // en centavos

	            // Guardar los IDs de Stripe en la entidad del producto
	            productEntity.setProductIdStripe(product.getId());
	            productEntity.setPriceIdStripe(price.getId());
	        } catch (Exception e) {
	            throw new RuntimeException(e.getMessage());
	        }
		return productEntity;
//		productEntity.setName(productRequestDTO.getName());
//		productEntity.setDescription(productRequestDTO.getDescription());
//		productEntity.setPrice(productRequestDTO.getPrice());
//		productEntity.setStock(productRequestDTO.getStock());
//		productEntity.setBarCode(productRequestDTO.getBarCode());
//		productEntity.setAmount(null);
//		productEntity.setSub_total_price(null);	
	}

}
