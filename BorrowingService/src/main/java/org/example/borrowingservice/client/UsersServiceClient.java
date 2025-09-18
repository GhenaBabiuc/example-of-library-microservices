package org.example.borrowingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UsersServiceClient {

    @GetMapping("/users/exist/{id}")
    Boolean existsById(@PathVariable Long id);
}
