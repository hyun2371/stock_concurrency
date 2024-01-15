package com.example.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import com.example.stock.transaction.StockSyncService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockSyncService stockService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before(){
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    void 재고감소(){
        stockService.decrease(1L, 1L);

        //100-1 = 99
        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(99, stock.getQuantity());
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        /*
        동시에 여러개 요청 -> 멀티 스레드 사용해야 함
        멀티 스레드 위해 ExecutorService(비동기 작업을 단순화하여 사용 가능한 API) 사용

         */
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 thread에서 수행 중인 작업이 완료될 때까지 대기할 수 있도록 돕는 클래스

        for (int i = 0; i< threadCount; i++){
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown(); //
                }
            });
        }

        latch.await(); //다른 스레드에서 수행중인 작업이 완료될 때까지 대기

        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}