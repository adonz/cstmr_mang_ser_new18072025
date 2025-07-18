package com.incede.nbfc.customer_management.FeignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.incede.nbfc.customer_management.Response.ResponseWrapper;



@FeignClient(name = "UserManagementService", url = "${user.service.url}")
public interface UserClient {

		@GetMapping("/v1/user-roles/{id}")
		ResponseWrapper<Boolean> getRoleById(@PathVariable Integer id);
	
}
