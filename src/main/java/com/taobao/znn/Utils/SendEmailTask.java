package com.taobao.znn.Utils;

import org.apache.commons.mail.HtmlEmail;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import static com.taobao.znn.Utils.SendEmailUtils.failEmail;
import static com.taobao.znn.Utils.SendEmailUtils.failTos;
import static com.taobao.znn.Utils.SendEmailUtils.successEmail;

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
    public synchronized void run() {
        HtmlEmail hemail = new HtmlEmail();

        try {
            hemail.setHostName(SendEmailUtils.getHost(fromVo.from));
            hemail.setSmtpPort(SendEmailUtils.getSmtpPort(fromVo.from));
            hemail.setCharset(SendEmailUtils.charSet);
            hemail.addTo(to);//批量放松
            hemail.setFrom(fromVo.from, SendEmailUtils.fromName);
            hemail.setAuthentication(fromVo.username, fromVo.password);
            hemail.setSubject(subject);

            hemail.setMsg(htmlText);
            System.out.println(fromVo.getFrom() + "发送中！");
            hemail.send();

            SendEmailUtils.success = SendEmailUtils.success + 1;
            successEmail.add(fromVo);

            System.out.println(fromVo.getFrom() + "发送成功！");
        } catch (Exception e) {
            e.printStackTrace();


            System.out.println(fromVo.getFrom() + "发送失败！");
            failTos.add(to);
            failEmail.add(fromVo.getFrom());
            SendEmailUtils.fail = SendEmailUtils.fail + 1;

        }

    }
}
