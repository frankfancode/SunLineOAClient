package org.ff.sunlineoaclient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class StreamTool {
    /**
     * 从输入流中获取数据
     *
     * @param inputStream
     * @throws Exception
     */
    public static String readInputStream(InputStream inputStream)
            throws Exception {
        String outString = "";
        // 实例化一个输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 读流的基本知识
        Reader reader = new InputStreamReader(inputStream, "GBK");// GB2312
        int c;
        while ((c = reader.read()) != -1) {
            // System.out.print((char) c);
            outString = outString + (char) c;
        }

        // 用完要关，大家都懂的
        inputStream.close();
        return outString;
    }
}