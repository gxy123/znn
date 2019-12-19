package com.taobao.znn.Utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.util.DateUtils;

import java.io.*;
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
    public static final String fromName = "hhda";
    public static Integer success = 0;
    public static Integer fail = 0;
    public static List<String> failEmail = new LinkedList<>();//发送邮箱异常
    public static Set<FromVo> successEmail = new HashSet<>();//发送邮箱正常的邮箱
    public static List<String> failTos = new LinkedList<>();//接收失败的邮箱


    private static Map<String, String> hostMap = new HashMap<String, String>();


    static {

        // 126
        hostMap.put("smtp.126", "smtp.126.com");
        hostMap.put("smtp.163", "smtp.163.com");
        hostMap.put("smtp.qq", "smtp.qq.com");
        hostMap.put("smtp.sina", "smtp.sina.com");
        hostMap.put("smtp.tom", "smtp.tom.com");


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
            //System.out.println(htmlText);
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
        //new SendEmailTask("1006351406@qq.com", new FromVo("xiaoweilvzheng8@126.com", "xiaoweilvzheng8@126.com", "xiaoweilvzheng1", 21), "天天好看", "sdfdsf").run();

        duTask();


    }

    public static void duTask() throws IOException, InterruptedException {
        StringBuffer log = new StringBuffer();
        Date start = new Date();
        String htmlText;
        htmlText = getHtml("C:\\Users\\guoxiaoyu\\Desktop", "add.html", null);
        FromListUtils fromListUtils = new FromListUtils();
        FileInputStream fileInputStream0 = new FileInputStream(new File("C:\\Users\\guoxiaoyu\\Desktop\\froms.xlsx"));
        List<FromVo> fromList = fromListUtils.getList(fileInputStream0);
        FileInputStream fileInputStream1 = new FileInputStream(new File("C:\\Users\\guoxiaoyu\\Desktop\\tos.xlsx"));
        List<String> toList = fromListUtils.getToList(fileInputStream1);
        Map<Integer, Integer> maxMap = new HashMap<>();
        int size = fromList.size();
        int fromIndex = size;
        ExecutorService executorService = Executors.newSingleThreadExecutor();//创建同发件箱数量同等数量任务
        for (int i = 0; i < toList.size(); i++) {
            if (i != 0 && i % size == 0 || fromIndex == 0) {
                fromIndex = size;
                System.out.println("每个邮箱已经发了一遍...等待1分钟后......");
                Thread.sleep(60000);
            }
            String s = toList.get(i);
            System.out.println(fromIndex);
            FromVo fromVo;
            while (true) {
                fromVo = fromList.get(fromIndex - 1);
                if (maxMap.get(fromIndex) != null) {
                    Integer count = maxMap.get(fromIndex);
                    if (fromVo.getMaxCount() <= count) {
                        fromIndex--;

                    } else {
                        maxMap.put(fromIndex, count + 1);
                        fromIndex--;
                        break;
                    }
                } else {
                    maxMap.put(fromIndex, 1);
                    fromIndex--;
                    break;
                }
            }

            executorService.execute(new SendEmailTask(s, fromVo, "天天好看" + i, htmlText));
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println("线程执行全部执行完毕!");
                break;
            }
            Thread.sleep(2000);

        }
        String textName = System.currentTimeMillis() + "";
        File file = new File("C:\\Users\\guoxiaoyu\\Desktop\\" + textName + ".txt");
        PrintStream ps = new PrintStream(new FileOutputStream(file));
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        Date end = new Date();
        log.append("开始时间：" + DateUtils.format(start, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH));
        log.append("\n");
        log.append("结束时间：" + DateUtils.format(end, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH));
        log.append("\n");
        log.append("成功条数：" + success);
        log.append("\n");
        log.append("失败条数：" + fail);
        log.append("\n");
        log.append("发送总量：" + toList.size());
        log.append("\n");
        log.append("不能用的邮箱：" + failEmail);
        log.append("\n");
        log.append("能用的邮箱：" + successEmail);
        log.append("\n");
        log.append("接收失败的客户邮箱：" + failTos);
        fromListUtils.outFileLog(DateUtils.format(start, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH), DateUtils.format(end, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH), toList.size(), success, failEmail, successEmail, failTos);

    }


}
