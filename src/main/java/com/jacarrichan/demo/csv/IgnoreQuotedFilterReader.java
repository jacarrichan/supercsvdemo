package com.jacarrichan.demo.csv;

import lombok.extern.slf4j.Slf4j;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Slf4j
public class IgnoreQuotedFilterReader extends FilterReader {
    private List<Character> badChar;

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public IgnoreQuotedFilterReader(Reader in, List<Character> badChar) {
        super(in);
        this.badChar = badChar;
    }

    public int read(char[] buf, int from, int len) throws IOException {
        int count = 0;
        while (count == 0) {
            count = in.read(buf, from, len);
            if (count == -1)
                return -1;

            int last = from;
            for (int i = from; i < from + count; i++) {
                if (!isBadChar(buf[i])) {
                    buf[last++] = buf[i];
                } else {
                    log.debug("bad char:{}", (char) buf[i]);
                }
            }
            count = last - from;
        }
        return count;
    }

    /***
     * 判断是否是需要移除的char
     * @param c
     * @return
     */
    private boolean isBadChar(char c) {
        if ((badChar.contains(c))) {
            return true;
        }
        return false;
    }
}
