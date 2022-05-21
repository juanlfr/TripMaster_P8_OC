package com.tripmaster.microservice.tourguide.proxies;

import com.tripmaster.microservice.tourguide.beans.AttractionBean;
import com.tripmaster.microservice.tourguide.beans.VisitedLocationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "microservice-gps", url = "localhost:9001")
public interface MicroserviceGpsProxy {
    @GetMapping("/getLocation")
    VisitedLocationBean getUserLocation(@RequestParam("userId") UUID userId);
    @GetMapping("/getAttractions")
    List<AttractionBean> getAttractions();
}
