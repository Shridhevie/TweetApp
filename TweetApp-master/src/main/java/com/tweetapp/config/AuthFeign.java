package com.tweetapp.config;

import com.tweetapp.model.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${authservice.client.name}",url = "${authservice.client.url}")
public interface AuthFeign {



    @GetMapping(value = "/apps/v1.0/tweets/validate")
    public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization") final String token);
}
