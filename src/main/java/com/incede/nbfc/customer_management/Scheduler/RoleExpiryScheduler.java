package com.incede.nbfc.customer_management.Scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.incede.nbfc.customer_management.Services.CustomerRoleAssignmentService;


@Component
public class RoleExpiryScheduler {

    @Autowired
    private CustomerRoleAssignmentService assignmentService;

    @Scheduled(cron = "0 0 1 * * ?") // Every day at 01:00 AM
    public void processExpiredRoles() {
        assignmentService.expireOutdatedRoles();
    }
}
