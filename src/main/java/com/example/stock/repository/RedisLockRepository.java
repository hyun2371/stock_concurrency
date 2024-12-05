package com.example.stock.repository;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {
	private RedisTemplate<String, String> redisTemplate;

	public Boolean lock(Long key){
		return redisTemplate
			.opsForValue()
			.setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
	}
	//key -> stock id value - "lock"문자열
	// key 점유중 -> false key -> 점유 x -> true

	public Boolean unlock(Long key){
		return redisTemplate.delete(generateKey(key));
	}

	private String generateKey(Long key){
		return key.toString();
	}
}
