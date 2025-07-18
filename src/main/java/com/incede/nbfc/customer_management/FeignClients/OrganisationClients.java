package com.incede.nbfc.customer_management.FeignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.incede.nbfc.customer_management.FeignClientsModels.StaffDto;
import com.incede.nbfc.customer_management.Response.ResponseWrapper;




@FeignClient(
    name = "tenant-onboarding-service",
    url = "${organisation.service.url}"
)
public interface OrganisationClients {

    @GetMapping("/v1/tenantorganisation/staffassignment/")
    ResponseWrapper<List<StaffDto>> getAllStaffByTenant();
}
