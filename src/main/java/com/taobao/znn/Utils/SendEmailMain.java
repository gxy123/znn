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
public class SendEmailMain {

    public static final String charSet = "utf-8";
    public static final String fromName = "hhda";
    public static Integer success = 0;
    public static Integer fail = 0;
    public static List<String> failEmail = new LinkedList<>();//发送邮箱异常
    public static Set<FromVo> successEmail = new HashSet<>();//发送邮箱正常的邮箱
    public static List<String> failTos = new LinkedList<>();//接收失败的邮箱
    public static int group = 0;
    static List<FromVo> fromList0 = new ArrayList<>();//sina
    static List<FromVo> fromList1 = new ArrayList<>();//163
    static List<FromVo> fromList2 = new ArrayList<>();//126
    static List<FromVo> fromList3 = new ArrayList<>();//qq
    static List<FromVo> fromList4 = new ArrayList<>();//tom
    static int size0 = 0;
    static int size1 = 0;
    static int size2 = 0;
    static int size3 = 0;
    static int size4 = 0;
    public static int size = 0;


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
        Map m = new HashMap();
       m.put("name", "sdddd");
        m.put("rtn", "sdf");
        String htmlText = getHtml("C:\\Users\\guoxiaoyu\\Desktop", "add.html", m);
        //new SendEmailTask2("17663492290@163.com", new FromVo("2581977373@qq.com", "2581977373@qq.com", "pzdpcihgslgbdjgi", 21), "恭喜秦雨谈恋爱了！！！", htmlText,1).run();
        // new SendEmailTask2("anne_xiaoxia@163.com", new FromVo("2581977373@qq.com", "2581977373@qq.com", "pzdpcihgslgbdjgi", 21), "电商人，需要注意啦！", htmlText).run();
        new SendEmailTask2("ggg_xiaoyu@163.com", new FromVo("xiaoweilvzheng1@tom.com", "xiaoweilvzheng1@tom.com", "xiaowei2019", 21), "电商人，需要注意啦！新电商法来啦！", htmlText).run();


         //duTask();


    }

    public static void duTask() throws IOException, InterruptedException {
        Date start = new Date();
        String htmlText;
        Map m = new HashMap();
        m.put("name", "sdddd");
        m.put("rtn", "sdf");
        htmlText = getHtml("C:\\Users\\guoxiaoyu\\Desktop", "add.html", m);
        FromListUtils fromListUtils = new FromListUtils();
        FileInputStream fileInputStream0 = new FileInputStream(new File("C:\\Users\\guoxiaoyu\\Desktop\\froms.xlsx"));
        List<FromVo> fromList = fromListUtils.getList(fileInputStream0);
        setFrom(fromList);
        FileInputStream fileInputStream1 = new FileInputStream(new File("C:\\Users\\guoxiaoyu\\Desktop\\tos.xlsx"));
        List<String> toList = fromListUtils.getToList(fileInputStream1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);//创建同发件箱数量同等数量任务
        for (int i = 0; i < toList.size(); i++) {
            String s = toList.get(i);
            FromVo fromVo = getPolling();
          /* while (true) {
                fromVo = fromList.get(fromIndex);
                if (maxMap.get(fromIndex) != null) {
                    Integer count = maxMap.get(fromIndex);
                    if (fromVo.getMaxCount() <= count) {
                        fromIndex++;

                    } else {
                        maxMap.put(fromIndex, count + 1);
                        fromIndex++;
                        break;
                    }
                } else {
                    maxMap.put(fromIndex, 1);
                    fromIndex++;
                    break;
                }
            }*/
            executorService.execute(new SendEmailTask2(s, fromVo, "电商人，需要注意啦！新电商法来啦！", htmlText));//
            if (size == 5) {
                System.out.println("每类邮箱已经发了一遍...等待执行完毕再发起新的任务......");
                while (true) {
                    Thread.sleep(2000);
                    System.out.println("检测前置任务是否完成...ing,group=" + group);
                    if (group == 5) {
                        group = 0;
                        System.out.println("前置任务执行完毕！！！！！！！！！！！！");
                        break;
                    }

                }
                Thread.sleep(10000);
            }
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println("线程执行全部执行完毕!");
                break;
            }
            Thread.sleep(2000);

        }
        Date end = new Date();

        fromListUtils.outFileLog(DateUtils.format(start, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH), DateUtils.format(end, "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒", Locale.ENGLISH), toList.size(), success, failEmail, successEmail, failTos);
        System.out.println(success);
    }

    public static void setFrom(List<FromVo> fromList) {

        fromList.forEach(FromVo -> {
            if (FromVo.from.contains("@sina")) {
                fromList0.add(FromVo);
            } else if (FromVo.from.contains("@163")) {
                fromList1.add(FromVo);
            } else if (FromVo.from.contains("@126")) {
                fromList2.add(FromVo);
            } else if (FromVo.from.contains("@qq")) {
                fromList3.add(FromVo);
            } else if (FromVo.from.contains("@tom")) {
                fromList4.add(FromVo);
            }
        });


    }

    public static FromVo getPolling() {

        FromVo fromVo = null;
        while (fromVo == null) {
            if (size > 4) {
                size = 0;
            }
            switch (size) {
                case 0:
                    if (fromList0.size() == 0) {
                        size++;
                        return null;
                    }
                    if (size0 == fromList0.size()) {
                        size0 = 0;
                    }
                    fromVo = fromList0.get(size0);
                    size0++;
                    break;
                case 1:
                    if (fromList1.size() == 0) {
                        size++;
                        return null;
                    }
                    if (size1 == fromList1.size()) {
                        size1 = 0;
                    }
                    fromVo = fromList1.get(size1);
                    size1 ++;
                    break;
                case 2:
                    if (fromList1.size() == 0) {
                        size++;
                        return null;
                    }
                    if (size1 == fromList1.size()) {
                        size1 = 0;
                    }
                    fromVo = fromList1.get(size1);
                    size1 ++;
                    break;
                case 3:
                    if (fromList3.size() == 0) {
                        size++;
                        return null;
                    }
                    if (size3 == fromList3.size()) {
                        size3 = 0;
                    }
                    fromVo = fromList3.get(size3);
                    size3 ++;
                    break;
                case 4:
                    if (fromList4.size() == 0) {
                        size++;
                        return null;
                    }
                    if (size4 == fromList4.size()) {
                        size4 = 0;
                    }
                    fromVo = fromList4.get(size4);
                    size4++;
                    break;
                default:
                    break;
            }
            size++;
        }
        return fromVo;

    }


}
