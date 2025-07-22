package com.incede.nbfc.customer_management.FeignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.incede.nbfc.customer_management.FeignClientsModels.UserInfoDTO;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;



@FeignClient(name = "UserManagementService", url = "${user.service.url}")
public interface UserClient {

		@GetMapping("/v1/usermanagement/user-roles/{id}")
		ResponseWrapper<Boolean> getRoleById(@PathVariable Integer id);
		
		@GetMapping("/v1/usermanagement/users/validate/{userId}")
	    ResponseWrapper<Boolean> isValidUser(@PathVariable("userId") Integer userId);
	
	    @GetMapping("/v1/usermanagement/users/{userId}")
	    ResponseWrapper<UserInfoDTO> getUserDetails(@PathVariable("userId") Integer userId);
}
