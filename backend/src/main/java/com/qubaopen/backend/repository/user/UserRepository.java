package com.qubaopen.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.backend.repository.user.custom.UserReportRepositoryCustom;
import com.qubaopen.survey.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long>, UserReportRepositoryCustom {

}
