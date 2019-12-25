package com.taobao.znn.Utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static com.taobao.znn.Utils.SendEmailMain.*;

/**
 * @ClassName SendEmailTask
 * @Author guoxiaoyu
 * @Date 2019/12/1810:09
 **/
public class SendEmailTask2 implements Runnable {
    private String to;
    private FromVo fromVo;
    private String subject;
    private String htmlText;
    private static Object lock =new Object();
    private int baseGroup;

    public SendEmailTask2(String to, FromVo fromVo, String subject, String htmlText, int group) {
        this.to = to;
        this.fromVo = fromVo;
        this.subject = subject;
        this.htmlText = htmlText;
        this.baseGroup = group;
    }

    @Override
    public  void run() {


        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

            javaMailSender.setUsername(fromVo.username);
            javaMailSender.setPassword(fromVo.password);
            javaMailSender.setHost(SendEmailMain.getHost(fromVo.from));
            javaMailSender.setPort(SendEmailMain.getSmtpPort(fromVo.from));
            javaMailSender.setDefaultEncoding("UTF-8");
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", SendEmailMain.getHost(fromVo.from));
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.connectiontimeout", "20000");
            props.setProperty("mail.smtp.timeout", "20000");
            javaMailSender.setJavaMailProperties(props);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setFrom(fromVo.from);
            helper.setSubject(subject);
            helper.setText(htmlText, true);
            System.out.println(fromVo.getFrom() + "发送中！,to="+to);

            javaMailSender.send(message);

            synchronized (lock) {
                SendEmailMain.success = SendEmailMain.success + 1;
                successEmail.add(fromVo);
            }
            System.out.println(fromVo.getFrom() + "发送成功！to ="+to+",group="+baseGroup);

            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(fromVo.getFrom() + "发送失败！");
            synchronized (lock) {
                failTos.add(to);
                failEmail.add(fromVo.getFrom());
                SendEmailMain.fail = SendEmailMain.fail + 1;
            }

        }

        group=baseGroup;
      /*  success++;
        System.out.println(subject);*/
    }
}
