package com.taobao.znn.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.Locale;

/**
 * @ClassName SendEmailTask
 * @Author guoxiaoyu
 * @Date 2019/12/1810:09
 **/
public class SendEmailTask implements Runnable {
    private String to;
    private SendEmailUtils.FromVo fromVo;
    private String subject;
    private String htmlText;

    public SendEmailTask(String to, SendEmailUtils.FromVo fromVo, String subject, String htmlText) {
        this.to = to;
        this.fromVo = fromVo;
        this.subject = subject;
        this.htmlText = htmlText;
    }

    @Override
    public void run() {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(SendEmailUtils.getHost(fromVo.from));
            hemail.setSmtpPort(SendEmailUtils.getSmtpPort(fromVo.from));
            hemail.setCharset(SendEmailUtils.charSet);
            hemail.addTo(to);//批量放松
            hemail.setFrom(fromVo.from, SendEmailUtils.fromName);
            hemail.setAuthentication(fromVo.username, fromVo.password);
            hemail.setSubject(subject);
            System.out.println(htmlText);
            hemail.setMsg(htmlText);
            String msgId = hemail.send();
            System.out.println("email send true!msgId="+msgId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("email send error!");
        }
    }
}
