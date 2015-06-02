package com.qubaopen.survey.controller.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/5/4.
 */
@RestController
@RequestMapping("system")
public class SystemController {

    @Value("${isNew}")
    boolean isNew;

    @Value("${nextNew}")
    boolean nextNew;

    @RequestMapping(value = "retrieveIsNew")
    Map<String, Object> retrieveIsNew() {

        Map<String, Object> map = new HashMap<>();
        map.put("isNew", isNew);
        return map;
    }


    @RequestMapping(value = "retrieveNextNew")
    Map<String, Object> retrieveNextNew() {

        Map<String, Object> map = new HashMap<>();
        map.put("success", "1");
        map.put("nextNew", nextNew);
        return map;
    }

}
