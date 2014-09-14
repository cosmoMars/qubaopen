package com.qubaopen.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qubaopen.survey.entity.interest.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {

}
