package com.repuestosgaston.shopping_cart.controller;

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

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.shopping_cart.service.SaleOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/sale_order")
public class SaleOrderController {
	private final SaleOrderService saleOrderService;

	public SaleOrderController(SaleOrderService saleOrderService) {
		this.saleOrderService = saleOrderService;
	}

	@GetMapping(path = "/")
	public ResponseEntity<Page<SaleOrderResponseDTO>> getAllSaleOrder(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(saleOrderService.getAllSaleOrder(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.getAllSaleOrder - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@GetMapping(path = "/{saleOrderId}")
	public ResponseEntity<SaleOrderResponseDTO> getSaleOrderById(Long saleOrderId){
		try {
			return ResponseEntity.ok().body(saleOrderService.getSaleOrderById(saleOrderId));
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.getSaleOrderById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PostMapping(path = "/")
	public ResponseEntity<Object> createSaleOrder(@RequestBody SaleOrderRequestDTO saleOrderRequestDTO){
		try {
			saleOrderService.createSaleOrder(saleOrderRequestDTO);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.createSaleOrder - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	
	@PutMapping(path = "/{saleOrder_id}")
	public ResponseEntity<SaleOrderEntity> updateSaleOrder(@PathVariable Long saleOrder_id,@RequestBody SaleOrderRequestDTO saleOrder){
		try {
			saleOrderService.updateSaleOrder(saleOrder_id,saleOrder);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.updateSaleOrder - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<SaleOrderEntity> deleteSaleOrderById(Long id){
		try {
			saleOrderService.deleteSaleOrderById(id);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.deleteSaleOrderById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
}
