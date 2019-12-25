package com.taobao.znn.Utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import static com.taobao.znn.Utils.SendEmailMain.*;

/**
 * @ClassName SendEmailTask
 * @Author guoxiaoyu
 * @Date 2019/12/1810:09
 **/
public class SendEmailTask implements Runnable {
    private String to;
    private SendEmailMain.FromVo fromVo;
    private String subject;
    private String htmlText;
    private static Object lock =new Object();
    private int baseGroup;

    public SendEmailTask(String to, SendEmailMain.FromVo fromVo, String subject, String htmlText, int group) {
        this.to = to;
        this.fromVo = fromVo;
        this.subject = subject;
        this.htmlText = htmlText;
        this.baseGroup = group;
    }

    @Override
    public  void run() {


        try {
            HtmlEmail hemail = new HtmlEmail();
            hemail.setHostName(SendEmailMain.getHost(fromVo.from));
            hemail.setSmtpPort(SendEmailMain.getSmtpPort(fromVo.from));
            hemail.setAuthenticator(new DefaultAuthenticator(fromVo.username, fromVo.password));
            hemail.setCharset(SendEmailMain.charSet);
            hemail.addTo(to);//批量放松
            hemail.setFrom(fromVo.from, SendEmailMain.fromName);
            hemail.setSubject(subject);

            hemail.setMsg(htmlText);
            System.out.println(fromVo.getFrom() + "发送中！,to="+to);
            hemail.send();
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
