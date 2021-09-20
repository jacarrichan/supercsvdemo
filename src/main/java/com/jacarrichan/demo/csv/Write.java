package com.jacarrichan.demo.csv;

import org.supercsv.cellprocessor.FmtBool;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Write {
    //第一步 定义单元格约束 若出错，会生成一张只有表头而没有内容的csv文件
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // customerNo (must be unique) 唯一值
                new NotNull(), // firstName //不允许为空null 但可以是 ""
                new NotNull(), // lastName
                new FmtDate("dd/MM/yyyy"), // birthDate 日期的格式
                new NotNull(), // mailingAddress
                new Optional(new FmtBool("Y", "N")), //married 可为空 true->Y false->N
                new Optional(), // numberOfKids
                new NotNull(), // favouriteQuote
                new NotNull(), // email
                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

        return processors;
    }

    //第二步 写入内容到csv文件中  特殊字符使用\转义  \n换行
    private static void writeWithCsvBeanWriter() throws Exception {

        // 创建自定义的实体类
        final CustomerBean john = new CustomerBean(
                "1", "John", "Dunbar",
                new GregorianCalendar(1945, Calendar.JUNE, 13).getTime(),
                "1600 Amphitheatre Parkway\nMountain View, CA 94043\nUnited States",
                null, null,
                "\"May the Force be with you.\" - Star Wars",
                "jdunbar@gmail.com", 0L);

        final CustomerBean bob = new CustomerBean(
                "2", "Bob", "Down",
                new GregorianCalendar(1919, Calendar.FEBRUARY, 25).getTime(),
                "1601 Willow Rd.\nMenlo Park, CA 94025\nUnited States",
                true, 0,
                "\"Frankly, my dear, I don't give a damn.\" - Gone With The Wind",
                "bobdown@hotmail.com", 123456L);

        final List<CustomerBean> customers = Arrays.asList(john, bob);


        String path = String.class.getResource("/writeWithCsvBeanWriter.csv").getFile();
        try (
                ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(path), CsvPreference.STANDARD_PREFERENCE);
        ) {
            // 定义表头元素的值
            final String[] header = new String[]{"customerNo", "firstName", "lastName", "birthDate",
                    "mailingAddress", "married", "numberOfKids", "favouriteQuote", "email", "loyaltyPoints"};
            final CellProcessor[] processors = getProcessors();
            //写入表头
            beanWriter.writeHeader(header);
            // 写入数据
            for (final CustomerBean customer : customers) {
                beanWriter.write(customer, header, processors);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        writeWithCsvBeanWriter();
    }

}