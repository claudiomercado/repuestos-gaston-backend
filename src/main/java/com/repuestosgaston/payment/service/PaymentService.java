package com.repuestosgaston.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Product;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

@Service
public class PaymentService {
	
	@Autowired
	public PaymentService(@Value("${stripe.key.secret}") String secretKey) {
		Stripe.apiKey = secretKey;
	}

    public Product createProduct(String name, String description) throws Exception {
        ProductCreateParams params = ProductCreateParams.builder()
            .setName(name)
            .setDescription(description)
            .build();

        return Product.create(params);
    }

    public Price createPrice(String productId, long unitAmount) throws Exception {
        PriceCreateParams params = PriceCreateParams.builder()
            .setUnitAmount(unitAmount)
            .setCurrency("ars")
            .setProduct(productId)
            .build();

        return Price.create(params);
    }
}
