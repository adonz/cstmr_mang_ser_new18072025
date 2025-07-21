package com.incede.nbfc.customer_management.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.incede.nbfc.customer_management.Models.CustomerFreezeActions;

public interface CustomerFreezeActionsRepository extends JpaRepository<CustomerFreezeActions, Integer>{

}
