package com.murilo.market_place;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MarketPlaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceApplication.class, args);
	}

}
