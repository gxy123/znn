package com.taobao.znn.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SendEmailUtils
 * @Author guoxiaoyu
 * @Date 2019/12/1610:08
 **/
@Component
public class SendEmailUtils {

    private static final String charSet = "utf-8";
    private static final String fromName = "测试公司";


    private static Map<String, String> hostMap = new HashMap<String, String>();
    private static List<FromVo> formVoList =new ArrayList<>();


    static {

        //初始化发送邮箱
        formVoList.add(new FromVo("xiaoweilvzheng1@163.com","xiaoweilvzheng1@163.com","xiaoweilvzheng1"));
        formVoList.add(new FromVo("1536734676@QQ.COM","1536734676@QQ.COM","gblvkgaaouoahhbe"));
        formVoList.add(new FromVo("xiaoweilvzheng6@126.com","xiaoweilvzheng6@126.com","xiaoweilvzheng1"));
        formVoList.add(new FromVo("xiaoweilvzheng1@sina.com","xiaoweilvzheng1@sina.com","c4ff5d65e138903e"));
        // 126
        hostMap.put("smtp.126", "smtp.126.com");
        hostMap.put("smtp.163", "smtp.163.com");
        hostMap.put("smtp.qq", "smtp.qq.com");


    }

    public static String getHost(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return hostMap.get(key);
        } else {
            throw new Exception("unSupportEmail");
        }
    }

    public static int getSmtpPort(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
        Matcher matcher = pattern.matcher(email);
        String key = "unSupportEmail";
        if (matcher.find()) {
            key = "smtp.port." + matcher.group(1);
        }
        if (hostMap.containsKey(key)) {
            return Integer.parseInt(hostMap.get(key));
        } else {
            return 25;
        }
    }


    /**
     * @param tos   接收方
     * @param subject      邮件主题
     * @param map          替换动态名称
     */
    public static void sendFtlMail(FromVo vo, String tos, String subject, Map<String, Object> map) {
        Template template;
        Configuration freeMarkerConfig;
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(getHost(vo.from));
            hemail.setSmtpPort(getSmtpPort(vo.from));
            hemail.setCharset(charSet);
            hemail.addTo(tos);//批量放松
            hemail.setFrom(vo.from, fromName);
            hemail.setAuthentication(vo.username, vo.password);
            hemail.setSubject(subject);
            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File("C:\\Users\\guoxiaoyu\\Desktop"));
            // 获取模板
            template = freeMarkerConfig.getTemplate("index.html", new Locale("Zh_cn"));
            // 模板内容转换为string
            String htmlText = FreeMarkerTemplateUtils
                    .processTemplateIntoString(template, map);
            System.out.println(htmlText);
            hemail.setMsg(htmlText);
            String msgId = hemail.send();
            System.out.println("email send true!msgId="+msgId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("email send error!");
        }
    }
    @Data
    static
    class FromVo{
        public FromVo(String from, String username, String password) {
            this.from = from;
            this.username = username;
            this.password = password;
        }

        String from;
          String username;//邮箱账号
          String password;//授权码
    }

    public static void main(String[] args) {
        SendEmailUtils s = new SendEmailUtils();
        s.sendFtlMail(new FromVo("xiaoweilvzheng1@163.com","xiaoweilvzheng1@163.com","xiaoweilvzheng1"),"xiaoweilvzheng1@sohu.com", "主题", null);
    }
}
