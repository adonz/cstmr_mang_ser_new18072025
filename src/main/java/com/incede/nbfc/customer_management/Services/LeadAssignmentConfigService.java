package com.incede.nbfc.customer_management.Services;

import org.springframework.stereotype.Service;

@Service
public class LeadAssignmentConfigService {
    public boolean isMultiAssigneeAllowed() {
        // Read from DB, YAML, env variable, or tenant metadata
        return false; // default behavior
    }
}

