package com.fader.vnote.io;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author FaderW
 * @Date 2021/1/14 19:38
 */
public class ZeroCopySimple {

//    public static void ioTest() throws FileNotFoundException {
//        new FileInputStream(new File("")).getChannel().transferTo(0, 0)
//    }


    public static void zeroCopyReadTest() throws IOException {
        /**
         * java nio提供的FileChannel提供了map()方法，该方法可以在一个打开的文件和MappedByteBuffer之间建立一个虚拟内存映射，
         * MappedByteBuffer继承于ByteBuffer，类似于一个基于内存的缓冲区，只不过该对象的数据元素存储在磁盘的一个文件中；
         * 调用get()方法会从磁盘中获取数据，此数据反映该文件当前的内容，调用put()方法会更新磁盘上的文件，并且对文件做的修改对其他阅读者也是可见的
         *
         * mmap + read
         */
        File file = new File("D:\\资金\\资金管理产品规划介绍_20201104.pptx");
        long len = file.length();
        byte[] bytes = new byte[(int) len];
        MappedByteBuffer mappedByteBuffer = new FileInputStream(file).getChannel().map(FileChannel.MapMode.READ_ONLY, 0, len);

        for (int offset = 0; offset < len; offset++) {
            byte b = mappedByteBuffer.get();
            bytes[offset] = b;
        }
        Scanner scan = new Scanner(new ByteArrayInputStream(bytes)).useDelimiter(" ");
        while (scan.hasNext()) {
            System.out.print(scan.next() + " ");
        }
    }

    public static void commonRead() throws IOException {
        File file = new File("D:\\资金\\资金管理产品规划介绍_20201104.pptx");
        long len = file.length();
        byte[] bytes = new byte[(int) len];
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        int offset = 0;
        int b = 0;
        while ((b = bufferedInputStream.read()) != -1) {
            bytes[offset++] = (byte) b;
        }
        Scanner scan = new Scanner(new ByteArrayInputStream(bytes)).useDelimiter(" ");
        while (scan.hasNext()) {
            System.out.print(scan.next() + " ");
        }

    }

    public static void main(String[] args) throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        zeroCopyReadTest();
//        commonRead();
        stopWatch.stop();
        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));


    }
}
