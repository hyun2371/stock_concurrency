package com.example.stock.transaction;

import lombok.RequiredArgsConstructor;

// spring transactional annotation 동작 방식
// @Transactional 사용시 위와 같이 stockSyncService의 래핑 클래스가 생김
@RequiredArgsConstructor
public class TransactionStockService {

    private final StockSyncService stockService;
    public void decrease(Long id, Long quantity){
        startTransaction();
        stockService.decrease(id, quantity);
        endTransaction();
        /*
        트랜잭션 종료 시점에 DB에 데이터 업데이트
        decrease() 완료 후 DB 업데이트되기 전에 다른 스레드가 decrease()를 호출할 수 있음
        -> 다른 스레드는 갱신되기 이전 값을 가져가서 race condition 발생
         */
    }

    private void startTransaction() {
        System.out.println("Transaction Start");
    }

    private void endTransaction() {
        System.out.println("commit");
    }
}
