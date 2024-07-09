package com.repuestosgaston.shopping_cart.controller;

import static com.repuestosgaston.utils.constants.UserPermissions.VIEW_ADMIN;
import static com.repuestosgaston.utils.constants.UserPermissions.VIEW_USER;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderStatusRequestDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.shopping_cart.service.SaleOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/v1/orders")
public class SaleOrderController {
	private final SaleOrderService saleOrderService;


	public SaleOrderController(SaleOrderService saleOrderService) {
		this.saleOrderService = saleOrderService;
	}

	@PreAuthorize("hasAuthority('"+VIEW_ADMIN+"')")
	@GetMapping(path = "/")
	public ResponseEntity<Page<SaleOrderResponseDTO>> getAllOrders(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(saleOrderService.getAllOrders(page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.getAllOrders - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PreAuthorize("hasAuthority('"+VIEW_ADMIN+"')")
	@GetMapping(path = "/{orderId}")
	public ResponseEntity<SaleOrderResponseDTO> getOrderById(@PathVariable(name = "orderId")Long orderId){
		try {
			return ResponseEntity.ok().body(saleOrderService.getOrderById(orderId));
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.getSaleOrderById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PreAuthorize("hasAuthority('"+VIEW_ADMIN+"')")
	@GetMapping(path = "/numberSale/{numberSale}")
	public ResponseEntity<Page<SaleOrderResponseDTO>> getOrderByNumberSale(
			@PathVariable(name = "numberSale") Integer numberSale,
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection){
		try {
			return ResponseEntity.ok().body(saleOrderService.getOrderByNumberSale(numberSale,page,
					size,sort,sortDirection));
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.getSaleOrderById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	//@PreAuthorize("hasAuthority('"+VIEW_USER+"')")
	@PostMapping(path = "/")
	public ResponseEntity<SaleOrderResponseDTO> createSaleOrder(){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			SaleOrderResponseDTO saleOrderResponseDTO = saleOrderService.createSaleOrder(username);
			return ResponseEntity.status(HttpStatus.CREATED).body(saleOrderResponseDTO);
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.createSaleOrder - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PutMapping(path = "/{orderId}")
	public ResponseEntity<SaleOrderEntity> updateOrder(@PathVariable Long orderId,@RequestBody SaleOrderRequestDTO saleOrder){
		try {
			saleOrderService.updateSaleOrder(orderId,saleOrder);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.updateSaleOrder - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@DeleteMapping(path = "/")
	public ResponseEntity<SaleOrderEntity> deleteOrderById(Long orderId){
		try {
			saleOrderService.deleteSaleOrderById(orderId);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.deleteSaleOrderById - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@PatchMapping(path = "/updateStatus/{orderId}")
	public ResponseEntity<SaleOrderEntity> updateOrderStatus(@PathVariable(name = "orderId") Long orderId,@RequestBody SaleOrderStatusRequestDTO status){
		try {
			SaleOrderEntity saleOrderEntity = saleOrderService.updateSaleOrderStatus(orderId, status);
			return ResponseEntity.status(HttpStatus.OK).body(saleOrderEntity);
		} catch (Exception e) {
			log.error(String.format("SaleOrderController.updateSaleStatus - Failed with message [%s]", e.getMessage()));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
