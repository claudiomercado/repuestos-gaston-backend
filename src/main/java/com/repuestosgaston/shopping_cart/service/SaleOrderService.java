package com.repuestosgaston.shopping_cart.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderRequestDTO;
import com.repuestosgaston.shopping_cart.controller.dto.SaleOrderResponseDTO;
import com.repuestosgaston.shopping_cart.model.SaleOrderEntity;
import com.repuestosgaston.shopping_cart.repository.SaleOrderRepository;

@Service
public class SaleOrderService {

	private final SaleOrderRepository saleOrderRepository;
	
	private final ModelMapper modelMapper;

	public SaleOrderService(SaleOrderRepository saleOrderRepository, ModelMapper modelMapper) {
		this.saleOrderRepository = saleOrderRepository;
		this.modelMapper = modelMapper;
	}

	public Page<SaleOrderResponseDTO> getAllSaleOrder(int page,int size,String sort,String sortDirection) {
		Sort sorter = Sort
                .by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sorter);
		
        return saleOrderRepository.findAll(pageable)
				.map(saleOrder -> modelMapper.map(saleOrder, SaleOrderResponseDTO.class));
	}
	
	public SaleOrderResponseDTO getSaleOrderById(Long saleOrderId) {
		var saleOrderEntity = saleOrderRepository.findById(saleOrderId);
		if (!saleOrderEntity.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Sale Order [%s] not found", saleOrderId));
		}
		return modelMapper.map(saleOrderEntity.get(), SaleOrderResponseDTO.class);
	}

	//Recibe un SaleOrderRequestDTO
	public void createSaleOrder(SaleOrderRequestDTO saleOrderRequestDTO) {
		SaleOrderEntity saleOrderEntity = modelMapper.map(saleOrderRequestDTO, SaleOrderEntity.class);
		saleOrderRepository.save(saleOrderEntity);
	}

	public void updateSaleOrder(Long saleOrder_id,SaleOrderRequestDTO saleOrder) {
		SaleOrderEntity saleOrderEntity = saleOrderRepository.findById(saleOrder_id).get();
		if (saleOrderEntity==null) {
			throw new IllegalArgumentException(
					String.format("Sale Order [%s] not found", saleOrder_id));
		}

		modelMapper.map(saleOrder, SaleOrderEntity.class);
		//Logica para modificar el saleOrder en base al saleOrder que recibe
		saleOrderRepository.save(saleOrderEntity);
	}
	
	public void deleteSaleOrderById(Long id) {
		saleOrderRepository.deleteById(id);
	}
	
}
