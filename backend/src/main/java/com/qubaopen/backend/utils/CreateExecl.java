package com.qubaopen.backend.utils;

import jxl.Workbook;
import jxl.format.*;
import jxl.write.*;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.io.File;

/**
 * Created by mars on 15/4/13.
 */
public class CreateExecl {

    public static void main(String args[]) {
        try {
            // 打开文件
            WritableWorkbook book = Workbook.createWorkbook(new File("test.xls"));
            // 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("任务", 0);

//            sheet.setColumnView(0, 30); // 设置列的宽度
//            sheet.setColumnView(1, 30); // 设置列的宽度
//            sheet.setColumnView(2, 30); // 设置列的宽度
//            sheet.setRowView(6, 1000); // 设置行的高度
//            sheet.setRowView(4, 1000); // 设置行的高度
//            sheet.setRowView(5, 1000); // 设置行的高度

            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            // 以及单元格内容为test

            WritableFont wf = new WritableFont(WritableFont.ARIAL, 15,
                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    jxl.format.Colour.CORAL); // 定义格式 字体 下划线 斜体 粗体 颜色
            WritableCellFormat wcf = new WritableCellFormat(wf);
            wcf.setBackground(jxl.format.Colour.WHITE); // 设置单元格的背景颜色
            wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
            Label label00 = new Label(0, 0, "订单id", wcf);
            Label label10 = new Label(1, 0, "订单号", wcf);

            // 将定义好的单元格添加到工作表中
            sheet.addCell(label00);
            sheet.addCell(label10);
            sheet.setRowView(0,400);

            Label label01 = new Label(0, 1, "ddddd", wcf);
            Label label02 = new Label(0, 2, "123123", wcf);
            Label label11 = new Label(1, 1, "ddddd", wcf);
            Label label12 = new Label(1, 2, "123123", wcf);

            sheet.addCell(label01);
            sheet.addCell(label02);

            sheet.addCell(label11);
            sheet.addCell(label12);

            /*
             * 生成一个保存数字的单元格 必须使用Number的完整包路径，否则有语法歧义 单元格位置是第二列，第一行，值为789.123
             */
//            jxl.write.Number number = new jxl.write.Number(0, 1, 555.12541);
//            sheet.addCell(number);

            // 写入数据并关闭文件
            book.write();
            book.close();
//            deleteFile("/Users/mars/test.xls");

//            sendTextMail("sdfdfsdf ", "349280576@qq.com", "343434343");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    public static String sendTextMail(String content, String email, String url) {

        MultiPartEmail sEmail = new MultiPartEmail ();
        //smtp host
        sEmail.setHostName("smtp.mxhichina.com");
        //登陆邮件服务器的用户名和密码
        sEmail.setAuthentication("postmaster@qudiaoyan.cn", "Qubaopen2013");
        //接收人
        try {
            EmailAttachment attachment = new EmailAttachment();
            //绝对路径
            attachment.setPath("/Users/mars/test.xls");
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("execl");
            attachment.setName("test.xls");

            sEmail.addTo(email);
            //发送人
            sEmail.setFrom("postmaster@qudiaoyan.cn", "知心团队", "UTF-8");
            //标题
            StringBuffer buffer = new StringBuffer();
            buffer.append("问题来了");
            buffer.append("<br>");
            buffer.append("<div><font size =\"3\" face=\"arial\" >" + content + "</font></div>");
            buffer.append("<div><font size =\"3\" face=\"arial\" >" + url + "</font></div>");
//            buffer.append("<a href='" + url + "'>");
            buffer.append("<br>");
            sEmail.setSubject("待解决问题 订单");
            //邮件内容
            sEmail.setMsg(buffer.toString());
            sEmail.attach(attachment);

            sEmail.setCharset("UTF-8");

            //发送
            sEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
        return "1";
    }

}
