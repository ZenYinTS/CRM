
import com.crm.domain.Employee;
import com.crm.service.IEmployeeService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestExcel {

    @Test
    public void out() throws Exception {
        //创建出Excel对象
        WritableWorkbook wb = Workbook.createWorkbook(new File("out.xls"));
        //创建工作本sheet1
        WritableSheet sheet = wb.createSheet("我的第一个工作本",0);
        //修改列的宽度
        sheet.setColumnView(0,30);
        //修改行的高度
        sheet.setRowView(0,500);

        //合并单元格（左上角单元格的列、行+右下角单元格的列、行）
        sheet.mergeCells(0,0,4,5);

        //创建单元格Label（列、行、文本内容）
        Label cell = new Label(0,0,"我的字符串文本内容");
        sheet.addCell(cell);

        //日期格式的数据
        DateFormat df = new DateFormat("yyyy-MM-dd HH:mm:ss");
        WritableCellFormat wcf = new WritableCellFormat(df);
        //设置水平居中
        wcf.setAlignment(Alignment.CENTRE);
        //设置垂直居中
        wcf.setVerticalAlignment(VerticalAlignment.CENTRE);

        //合并单元格后，内容由左上角的位置配置，所以列行必须为0,0
        DateTime time = new DateTime(0,0,new Date(),wcf);
        sheet.addCell(time);
        //将写入的内容写到Excel中
        wb.write();
        //关闭资源
        wb.close();
    }

    @Test
    public void read() throws Exception {
        //找到需要读取的文件
        Workbook wb = Workbook.getWorkbook(new File("read.xlsx"));
        //拿到工作本
        Sheet sheet = wb.getSheet(0);
        //获取Excel中有多少行、列
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = sheet.getCell(j,i);
                System.out.print(cell.getContents()+"\t");
            }
            System.out.println();
        }
        //关闭资源
        wb.close();
    }

    //改成使用poi
    @Test
    public void poiOut() throws Exception {
        //创建空的Excel文件
        org.apache.poi.ss.usermodel.Workbook wb = null;
        //创建文件流，判断是xlsx，还是xls
        String filePath = "poi_out.xlsx";
        FileOutputStream fos = new FileOutputStream(filePath);
        if (filePath.endsWith(".xls")){
            wb = new HSSFWorkbook();
        }
        if (filePath.endsWith(".xlsx")){
            wb = new XSSFWorkbook();
        }
        //创建工作表
        org.apache.poi.ss.usermodel.Sheet sheet1 = wb.createSheet("工作表1");

        //创建行数据，0为第一行
        Row row = sheet1.createRow(0);
        //字符串数据
        row.createCell(0).setCellValue("第一列数据");
        //日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        row.createCell(1).setCellValue(sdf.format(new Date()));

        wb.write(fos);
        fos.close();
    }

    @Test
    public void poiRead() throws Exception {
        String filePath = "read.xlsx";
        org.apache.poi.ss.usermodel.Workbook wb = null;
        FileInputStream fis = new FileInputStream(filePath);
        if (filePath.endsWith(".xlsx")){
            wb = new XSSFWorkbook(fis);
        }
        if (filePath.endsWith(".xls")){
            wb = new HSSFWorkbook(fis);
        }
        org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = firstRowNum;i < lastRowNum;i++){
            //获取当前行
            Row row = sheet.getRow(i);
            if (row==null)
                continue;
            //获取当前行的开始列
            int firstColumn = row.getFirstCellNum();
            int lastColumn = row.getLastCellNum();
            for (int j = firstColumn; j < lastColumn; j++) {
                org.apache.poi.ss.usermodel.Cell cell = row.getCell(j);
                System.out.print(cell+"\t");
            }
            System.out.println();
        }
        fis.close();
    }

    //使用poi导出对象
    @Test
    public void poiOutEmps() throws Exception {
        String filePath = "emp_out.xlsx";
        FileOutputStream fos = new FileOutputStream(filePath);
        org.apache.poi.ss.usermodel.Workbook wb = null;
        if (filePath.endsWith(".xlsx")){
            wb = new XSSFWorkbook();
        }
        if (filePath.endsWith(".xls")){
            wb = new HSSFWorkbook();
        }
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("员工列表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("员工账号");
        row.createCell(1).setCellValue("真实姓名");
        row.createCell(2).setCellValue("密码");
        row.createCell(0).setCellValue("员工账号");
        row.createCell(0).setCellValue("员工账号");
        row.createCell(0).setCellValue("员工账号");
        row.createCell(0).setCellValue("员工账号");
    }
}
