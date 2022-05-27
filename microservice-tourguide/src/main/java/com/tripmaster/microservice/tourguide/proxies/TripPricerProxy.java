package com.tripmaster.microservice.tourguide.proxies;

import com.tripmaster.microservice.tourguide.beans.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-pricer", url = "localhost:9003")
public interface TripPricerProxy {
    @GetMapping("/getPrice")
    List<Provider> getPrice(@RequestParam("apiKey")String apiKey, @RequestParam("attractionId") UUID attractionId, @RequestParam int adults, @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints );

}
