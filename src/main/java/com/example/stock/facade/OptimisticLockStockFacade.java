package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private final OptimisticLockStockService optimisticLockStockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true){
            try{
                optimisticLockStockService.decrease(id, quantity);
                break; //[2] 정상적으로 업데이트 시 while문 종료
            } catch (Exception e){
                Thread.sleep(50); //[1] 수량 감소 실패 시 50ms 후 재시도

            }
        }
    }
}
