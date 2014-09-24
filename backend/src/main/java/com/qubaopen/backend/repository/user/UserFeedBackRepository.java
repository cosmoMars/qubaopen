package com.qubaopen.backend.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.user.UserFeedBack;

public interface UserFeedBackRepository extends JpaRepository<UserFeedBack, Long> {

}
