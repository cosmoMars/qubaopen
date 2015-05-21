package com.qubaopen.backend.controller.help;

import com.qubaopen.backend.repository.help.HelpCommentRepository;
import com.qubaopen.backend.repository.help.HelpRepository;
import com.qubaopen.backend.repository.hospital.HospitalRepository;
import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.comment.HelpComment;
import com.qubaopen.survey.entity.hospital.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mars on 15/5/19.
 */
@RestController
@RequestMapping("helpComment")
public class HelpCommentController extends AbstractBaseController<HelpComment, Long> {

    @Autowired
    private HelpCommentRepository helpCommentRepository;

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    protected MyRepository<HelpComment, Long> getRepository() {
        return helpCommentRepository;
    }

    @RequestMapping(value = "addCommentHelp")
    public
    @ResponseBody
    Map<String,Object> addCommentHelp(@RequestParam long helpId, @RequestParam String content) {


        Map<String, Object> result = new HashMap<>();
        if (content == null || "".equals(content.trim())){
            result.put("success", "0");
            result.put("message","content没有内容");
            return result;
        }
        Help help = helpRepository.findOne(helpId);

        Hospital hospital = hospitalRepository.findOne(1l);

        HelpComment helpComment = new HelpComment();
        helpComment.setHelp(help);
        helpComment.setHospital(hospital);
        helpComment.setTime(new Date());
        helpComment.setContent(content);

        helpComment = helpCommentRepository.save(helpComment);

        result.put("commentId",helpComment.getId());
        result.put("doctorId", helpComment.getHospital().getId());
        result.put("doctorName", helpComment.getHospital().getHospitalInfo().getName());
        result.put("doctorContent", helpComment.getContent());
        result.put("doctorAvatar", helpComment.getHospital().getHospitalInfo().getHospitalAvatar());
        result.put("hospitalId", helpComment.getHospital().getId());
        result.put("hospitalName", helpComment.getHospital().getHospitalInfo().getName());
        result.put("hospitalAvatar", helpComment.getContent());
        result.put("doctorTime", helpComment.getTime());
        result.put("goods", 0);
        result.put("isGood", false);
        result.put("type", 1);
        result.put("success", "1");

        return result;
    }
}
