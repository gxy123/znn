package com.taobao.znn.Utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
       Workbook workbook = new HSSFWorkbook(fileInputStream);

       Sheet sheet = workbook.getSheetAt(0);
       for (int i = 1; i <= sheet.getLastRowNum(); i++) {
           Row row = sheet.getRow(i);
           String name = row.getCell(0).getStringCellValue();
           String pass = row.getCell(2).getStringCellValue();
            list.add(new SendEmailUtils.FromVo(name,name,pass));
       }
       return  list;
   }
}
