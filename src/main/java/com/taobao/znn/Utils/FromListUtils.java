package com.taobao.znn.Utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName FromListUtils
 * @Author guoxiaoyu
 * @Date 2019/12/1617:17
 **/
public class FromListUtils {
    /**
     * 获取所有的发邮件箱
     * @param fileInputStream
     * @return
     * @throws IOException
     */
   public  List<SendEmailUtils.FromVo> getList( FileInputStream fileInputStream) throws IOException {
       List<SendEmailUtils.FromVo> list =new ArrayList<>();
       Workbook workbook = new XSSFWorkbook(fileInputStream);

       Sheet sheet = workbook.getSheetAt(0);
       for (int i = 1; i <= sheet.getLastRowNum(); i++) {
           Row row = sheet.getRow(i);
           String name = row.getCell(0).getStringCellValue();
           String pass = row.getCell(2).getStringCellValue();
           Cell cell = row.getCell(3);
           cell.setCellType(CellType.STRING);
           Integer count = Integer.valueOf(row.getCell(3).getStringCellValue());
            list.add(new SendEmailUtils.FromVo(name,name,pass,count));
       }
       return  list;
   }

    /**
     * 获取接收邮件的地址
     * @param fileInputStream
     * @return
     * @throws IOException
     */
    public  List<String> getToList( FileInputStream fileInputStream) throws IOException {
        List<String> list =new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String to = row.getCell(1).getStringCellValue();
            list.add(to);
        }
        return  list;
    }

    public  List<String> outFileLog(String start, String end, Integer countAll, Integer success, List<String> failEmail, Set< SendEmailUtils.FromVo> successEmail, List<String> failTos) throws IOException {
        List<String> list =new ArrayList<>();
        FileOutputStream out =new FileOutputStream(new File("C:\\Users\\guoxiaoyu\\Desktop\\log\\new_log_"+System.currentTimeMillis()+".xlsx"));
        Workbook workbook =new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("总结");

        Row title = sheet.createRow(0);
        Cell cell3 = title.createCell(0);
        cell3.setCellValue("开始时间");
        Cell cell5 = title.createCell(1);
        cell5.setCellValue("结束时间");
        Cell cell7 = title.createCell(2);
        cell7.setCellValue("发送总量");
        Cell cell9 = title.createCell(3);
        cell9.setCellValue("成功条数");
        Row data = sheet.createRow(1);
        Cell cell4 = data.createCell(0);
        cell4.setCellValue(start);
        Cell cell6 = data.createCell(1);
        cell6.setCellValue(end);
        Cell cell8 = data.createCell(2);
        cell8.setCellValue(countAll);
        Cell cell10 = data.createCell(3);
        cell10.setCellValue(success);

        Sheet sheet1 = workbook.createSheet("接收失败的客户邮箱");
        Row row1 = sheet1.createRow(0);
        Cell cell0 = row1.createCell(0);
        cell0.setCellValue("接收失败的客户邮箱");
        for (int i = 0; i < failTos.size(); i++) {
            String s =  failTos.get(i);
            Row row = sheet1.createRow(i+1);
            Cell cell = row.createCell(0);
            cell.setCellValue(s);
        }

        Sheet sheet2 = workbook.createSheet("可以使用的邮箱");
        Row row2 = sheet2.createRow(0);
        Cell cell1 = row2.createCell(0);
        cell1.setCellValue("可以使用的邮箱");
        Cell cell = row2.createCell(1);
        cell.setCellValue("邮箱");
        Cell cell11 = row2.createCell(2);
        cell11.setCellValue("授权码");

        Iterator<SendEmailUtils.FromVo> iterator = successEmail.iterator();
        int s =1;
        while (iterator.hasNext()){
            SendEmailUtils.FromVo next = iterator.next();
            Row row = sheet2.createRow(s);
            Cell cell2 = row.createCell(0);
            cell2.setCellValue(next.getFrom());
            Cell cell12 = row.createCell(1);
            cell12.setCellValue(next.getFrom());
            Cell cell13 = row.createCell(2);
            cell13.setCellValue(next.getPassword());
            s++;

        }

        Sheet sheet3 = workbook.createSheet("异常的邮箱");
        Row row3 = sheet3.createRow(0);
        Cell cell2 = row3.createCell(0);
        cell2.setCellValue("异常的邮箱");
        for (int i = 0; i < failEmail.size(); i++) {
            Row row = sheet3.createRow(1+i);
            Cell cell12 = row.createCell(0);
            cell12.setCellValue(failEmail.get(i));
        }
        workbook.write(out);
        out.flush();
        out.close();
        return  list;
    }
}
