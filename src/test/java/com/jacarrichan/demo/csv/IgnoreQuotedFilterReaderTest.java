package com.jacarrichan.demo.csv;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;


public class IgnoreQuotedFilterReaderTest {
    @Test
    public void test()  {
        String testString = "This is a test string.\"This is a test string.\"This is a test string.\'";
        try (
                StringReader srcReader = new StringReader(testString);
                IgnoreQuotedFilterReader filterReader = new IgnoreQuotedFilterReader(srcReader, Lists.newArrayList('"', '\''));
                BufferedReader rd = new BufferedReader(filterReader);
        ) {
            final StringBuffer buffer = new StringBuffer(2048);
            String line;
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer);
            Assert.assertEquals(testString.length() - 3, buffer.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}