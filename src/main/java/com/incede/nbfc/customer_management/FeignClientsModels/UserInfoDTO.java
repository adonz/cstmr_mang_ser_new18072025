package com.incede.nbfc.customer_management.FeignClientsModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {

	private Integer userId;
	private String userName;
	private String fullName;
}
