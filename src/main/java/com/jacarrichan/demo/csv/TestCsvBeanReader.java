package com.jacarrichan.demo.csv;

import com.google.common.collect.Lists;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.FilterReader;


public class TestCsvBeanReader {

    private static CellProcessor[] getProcessors() {

        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");

        final CellProcessor[] processors = new CellProcessor[]{
                new UniqueHashCode(), // customerNo (must be unique)
                new NotNull(), // firstName
                new NotNull(), // lastName
                new ParseDate("dd/MM/yyyy"), // birthDate
                new NotNull(), // mailingAddress
                new Optional(new ParseBool()), // married
                new Optional(new ParseInt()), // numberOfKids
                new NotNull(), // favouriteQuote
                new StrRegEx(emailRegex), // email
                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

        return processors;
    }

    private static void readWithCsvBeanReader() throws Exception {

        String path = String.class.getResource("/readWithCsvBeanReader.csv").getFile();
        try (
                FilterReader is = new IgnoreQuotedFilterReader(new FileReader(path), Lists.newArrayList('"'));
                ICsvBeanReader beanReader = new CsvBeanReader(is, CsvPreference.STANDARD_PREFERENCE);
        ) {

            // the header elements are used to map the values to the bean (names must match)
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            CustomerBean customer;
            while ((customer = beanReader.read(CustomerBean.class, header, processors)) != null) {
                System.out.println(String.format(
                        "lineNo=%s, rowNo=%s, customer=%s",
                        beanReader.getLineNumber(),
                        beanReader.getRowNumber(),
                        customer));
            }
        }
    }

    public static void main(String[] args) throws Exception {

        readWithCsvBeanReader();

    }
}
