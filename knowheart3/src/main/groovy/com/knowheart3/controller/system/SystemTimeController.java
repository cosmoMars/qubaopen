package com.knowheart3.controller.system;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by mars on 15/3/20.
 */
@RestController
@RequestMapping("system")
public class SystemTimeController {

    @RequestMapping(value = "retrieveSystemDate", method = RequestMethod.GET)
    public Object retrieveSystemDate() {
        return "{\"systemDate\" : \"" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\"}";
    }
}
