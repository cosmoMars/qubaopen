package com.qubaopen.survey.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.user.User;
import com.qubaopen.survey.entity.user.UserReceiveAddress;

public interface UserReceiveAddressRepository extends MyRepository<UserReceiveAddress, Long> {

	long countByUser(User user);

	List<UserReceiveAddress> findByUser(User user);

	@Query("from UserReceiveAddress ura where ura.user = :user and ura.isDefaultAddress = :defaultAddress")
	UserReceiveAddress findByUserAndDefaultAddress(@Param("user")User user, @Param("defaultAddress") boolean defaultAddress);

	List<UserReceiveAddress> findByUserAndIsDefaultAddress(User user, boolean defaultAddress);

	@Query("from UserReceiveAddress ura where ura.user.id = :userId and ura.isDefaultAddress = true")
	UserReceiveAddress findDefaultAddressByUserId(@Param("userId") long userId);

}
