package com.knowheart3.controller;

import com.knowheart3.repository.base.AreaCodeRepository;
import com.qubaopen.survey.entity.base.AreaCode;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mars on 15/4/8.
 */
@RestController
@RequestMapping("document")
public class DocumentTest {

    @Autowired
    private AreaCodeRepository areaCodeRepository;

//    public static void main(String[] args) {
//
//        SAXReader sr = new SAXReader();
//        try {
//            Document doc = sr.read("/Users/mars/Desktop/test1.xml");
//            Element root = doc.getRootElement();
//
////            List list = root.elements();
//
//            List listsf = root.elements("sf");
//
//            List<AreaCode> areaCodes = new ArrayList<>();
//
//            for (Object o : listsf) {
//                Element e = (Element) o;
//                String sfName = e.element("code").getTextTrim();
//                String sfCode = e.element("name").getTextTrim();
//
//                System.out.println("sfName: " + sfName + "  sfCode : " + sfCode);
//
//                List listCity = e.elements("cs");
//
//                for (Object city : listCity) {
//                    Element c = (Element) city;
//                    String cityName = c.element("code").getTextTrim();
//                    String cityCode = c.element("name").getTextTrim();
//                    System.out.println("cityname: " + cityName + "  cityCode : " + cityCode);
//
//                    List areaList = c.elements("dq");
//                    for (Object area : areaList) {
//                        Element a = (Element) area;
//                        String areaName = a.element("code").getTextTrim();
//                        String areaCode = a.element("name").getTextTrim();
//                        System.out.println("areaname: " + areaName + " areaCode : " + areaCode);
//                    }
//
//                }
//
//            }
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

    @RequestMapping(value = "saveCode", method = RequestMethod.GET)
    public String saveCode() {
        SAXReader sr = new SAXReader();
        try {
            Document doc = sr.read("/Users/mars/Desktop/code.xml");
            Element root = doc.getRootElement();

//            List list = root.elements();

            List listsf = root.elements("sf");

            List<AreaCode> areaCodes = new ArrayList<>();

            for (Object o : listsf) {
                Element e = (Element) o;
                String sfName = e.element("code").getTextTrim();
                String sfCode = e.element("name").getTextTrim();

//                System.out.println("sfName: " + sfName + "  sfCode : " + sfCode);
                AreaCode parent = new AreaCode();
                parent.setCode(sfCode);
                parent.setName(sfName);

                areaCodes.add(parent);
                List listCity = e.elements("cs");

                for (Object city : listCity) {
                    Element c = (Element) city;
                    String cityName = c.element("code").getTextTrim();
                    String cityCode = c.element("name").getTextTrim();
//                    System.out.println("cityname: " + cityName + "  cityCode : " + cityCode);

                    AreaCode cityArea = new AreaCode();
                    cityArea.setParent(parent);
                    cityArea.setName(cityName);
                    cityArea.setCode(cityCode);
                    areaCodes.add(cityArea);

                    List areaList = c.elements("dq");
                    for (Object area : areaList) {
                        Element a = (Element) area;
                        String areaName = a.element("code").getTextTrim();
                        String areaCode = a.element("name").getTextTrim();
//                        System.out.println("areaname: " + areaName + " areaCode : " + areaCode);
                        AreaCode areaA = new AreaCode();
                        areaA.setParent(cityArea);
                        areaA.setName(areaName);
                        areaA.setCode(areaCode);
                        areaCodes.add(areaA);
                    }

                }

            }
            areaCodeRepository.save(areaCodes);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "1";
    }

    @RequestMapping(value = "deleteAll", method = RequestMethod.GET)
    public String deleteArea() {

        areaCodeRepository.deleteAll();
        return "1";
    }
}
