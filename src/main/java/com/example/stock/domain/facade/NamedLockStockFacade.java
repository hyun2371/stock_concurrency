package com.example.stock.domain.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NamedLockStockFacade {

	private final LockRepository lockRepository;
	private final StockService stockService;

	@Transactional
	public void decrease(Long id, Long quantity){
		try {
			lockRepository.getLock(id.toString());
			stockService.decrease(id, quantity);
		} finally {
			lockRepository.releaseLock(id.toString());
		}
	}
}
