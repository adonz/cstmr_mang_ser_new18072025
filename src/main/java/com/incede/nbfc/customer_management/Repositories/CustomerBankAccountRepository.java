package com.incede.nbfc.customer_management.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incede.nbfc.customer_management.Models.CustomerBankAccountModel;



@Repository
public interface CustomerBankAccountRepository extends JpaRepository<CustomerBankAccountModel, Integer> {

	CustomerBankAccountModel findByBankAccountId(Integer bankAccountId);

	CustomerBankAccountModel findByAccountNumber(String accountNumber);

	CustomerBankAccountModel findByUpiId(String upiId);

	CustomerBankAccountModel findByBankAccountIdAndIsDeleteFalse(Integer bankAccountId);

	Page<CustomerBankAccountModel> findByIsDeleteFalse(Pageable pageble);

	Optional<CustomerBankAccountModel> findByCustomerIdAndIsPrimaryTrue(Integer customerId);

	CustomerBankAccountModel findByBankAccountIdAndIsActiveTrue(Integer bankAccountId);

	List<CustomerBankAccountModel> findByCustomerId(Integer customerAccounts);

 
}
