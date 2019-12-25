package com.taobao.znn.Utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Session;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import static com.taobao.znn.Utils.SendEmailUtils.*;

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
    private static Object lock =new Object();

    public SendEmailTask(String to, SendEmailUtils.FromVo fromVo, String subject, String htmlText) {
        this.to = to;
        this.fromVo = fromVo;
        this.subject = subject;
        this.htmlText = htmlText;
    }

    @Override
    public  void run() {


        try {
            HtmlEmail hemail = new HtmlEmail();
            hemail.setHostName(SendEmailUtils.getHost(fromVo.from));
            hemail.setSmtpPort(SendEmailUtils.getSmtpPort(fromVo.from));
            hemail.setAuthenticator(new DefaultAuthenticator(fromVo.username, fromVo.password));
            hemail.setCharset(SendEmailUtils.charSet);
            hemail.addTo(to);//批量放松
            hemail.setFrom(fromVo.from, SendEmailUtils.fromName);
            hemail.setSubject(subject);

            hemail.setMsg(htmlText);
            System.out.println(fromVo.getFrom() + "发送中！");
            hemail.send();
            synchronized (lock) {
                SendEmailUtils.success = SendEmailUtils.success + 1;
                successEmail.add(fromVo);
            }
            System.out.println(fromVo.getFrom() + "发送成功！");
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(fromVo.getFrom() + "发送失败！");
            synchronized (lock) {
                failTos.add(to);
                failEmail.add(fromVo.getFrom());
                SendEmailUtils.fail = SendEmailUtils.fail + 1;
            }

        }


      /*  success++;
        System.out.println(subject);*/
    }
}
