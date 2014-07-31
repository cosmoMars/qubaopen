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

	@Query("from UserReceiveAddress ura where ura.user = :user and ura.defaultAddress = :defaultAddress")
	UserReceiveAddress findByUserAndTrueAddress(@Param("user")User user, @Param("defaultAddress") boolean defaultAddress);

	List<UserReceiveAddress> findByUserAndDefaultAddress(User user, boolean defaultAddress);

	@Query("from UserReceiveAddress ura where ura.user.id = :userId and ura.defaultAddress = true")
	UserReceiveAddress findDefaultAddressByUserId(@Param("userId") long userId);

}
