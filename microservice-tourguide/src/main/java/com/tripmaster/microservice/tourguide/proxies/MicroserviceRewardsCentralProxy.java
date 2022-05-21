package com.tripmaster.microservice.tourguide.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "microservice-rewards", url = "localhost:9002")
public interface MicroserviceRewardsCentralProxy {
    @GetMapping("/getAttractionRewardPoints")
    int getAttractionRewardPoints(@RequestParam("attractionId") UUID attractionId,@RequestParam("userId") UUID userId);
}
