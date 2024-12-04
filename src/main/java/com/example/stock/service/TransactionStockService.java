package com.example.stock.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionStockService {
	private StockService stockService;

	public void decrease(Long id, Long quantity){
		startTransaction();
		stockService.decrease(id, quantity);
		//다른 스레드가 decrease 메서드를 호출할 수 있음
		endTransaction();
	}

	private void startTransaction(){
		System.out.println("Transaction Start");
	}

	private void endTransaction(){
		System.out.println("Commit");
	}

}
