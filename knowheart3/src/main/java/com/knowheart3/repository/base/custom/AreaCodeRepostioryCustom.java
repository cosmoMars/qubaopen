package com.knowheart3.repository.base.custom;

import com.qubaopen.survey.entity.base.AreaCode;

import java.util.List;

/**
 * Created by mars on 15/4/8.
 */
public interface AreaCodeRepostioryCustom {

    List<AreaCode> findExistDoctorAreaCode();
}
