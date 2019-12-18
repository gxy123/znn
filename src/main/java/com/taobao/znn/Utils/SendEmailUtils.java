package com.taobao.znn.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName SendEmailUtils
 * @Author guoxiaoyu
 * @Date 2019/12/1610:08
 **/
@Component
@Data
public class SendEmailUtils {

    public static final String charSet = "utf-8";
    public static final String fromName = "测试公司";


    private static Map<String, String> hostMap = new HashMap<String, String>();
    private static List<FromVo> formVoList = new ArrayList<>();


    static {

        //初始化发送邮箱
        formVoList.add(new FromVo("xiaoweilvzheng1@163.com", "xiaoweilvzheng1@163.com", "xiaoweilvzheng1", 30));
        formVoList.add(new FromVo("1536734676@QQ.COM", "1536734676@QQ.COM", "gblvkgaaouoahhbe", 30));
        formVoList.add(new FromVo("xiaoweilvzheng6@126.com", "xiaoweilvzheng6@126.com", "xiaoweilvzheng1", 30));
        formVoList.add(new FromVo("xiaoweilvzheng1@sina.com", "xiaoweilvzheng1@sina.com", "c4ff5d65e138903e", 30));
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
     * @param path
     * @param name
     * @param map
     * @return
     */
    public static String getHtml(String path, String name, Map<String, Object> map) {
        Template template;
        Configuration freeMarkerConfig;

        try {

            freeMarkerConfig = new Configuration();
            freeMarkerConfig.setDirectoryForTemplateLoading(new File(path));
            // 获取模板
            template = freeMarkerConfig.getTemplate(name, new Locale("Zh_cn"));
            // 模板内容转换为string
            String htmlText = FreeMarkerTemplateUtils
                    .processTemplateIntoString(template, map);
            System.out.println(htmlText);
            return htmlText;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("email send error!");
        }
        return "";
    }

    @Data
    static
    class FromVo {
        public FromVo(String from, String username, String password, Integer maxCount) {
            this.from = from;
            this.username = username;
            this.password = password;
            this.maxCount = maxCount;
        }

        String from;
        String username;//邮箱账号
        String password;//授权码
        Integer maxCount;//每天最大发送条数
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String htmlText;
        htmlText = getHtml("", "", null);
        FromListUtils fromListUtils = new FromListUtils();
        FileInputStream fileInputStream0 = new FileInputStream(new File(""));
        List<FromVo> fromList = fromListUtils.getList(fileInputStream0);
        FileInputStream fileInputStream1 = new FileInputStream(new File(""));
        List<String> toList = fromListUtils.getToList(fileInputStream1);
        int size = fromList.size();
        int fromIndex = size;
        ExecutorService executorService = Executors.newFixedThreadPool(size);//创建同发件箱数量同等数量任务
        for (int i = 0; i < toList.size(); i++) {
            if (i / size == 0) {
                fromIndex = size;
                System.out.println("等待一会再发...");
                Thread.sleep(60000);
            }
            String s = toList.get(i);
            fromIndex--;
            FromVo fromVo = fromList.get(fromIndex);

            executorService.execute(new SendEmailTask(s, fromVo, "主题", htmlText));
        }
        SendEmailUtils s = new SendEmailUtils();

    }
}
