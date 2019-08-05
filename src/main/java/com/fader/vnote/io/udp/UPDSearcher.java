package com.fader.vnote.io.udp;

import java.io.IOException;
import java.net.*;

/**
 * @author FaderW
 * 2019/8/2
 */

public class UPDSearcher {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        byte[] sendData = "HelloWorld".getBytes();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length);
        packet.setAddress(InetAddress.getLocalHost());
        packet.setPort(20000);

        socket.send(packet);

        //接收数据
        byte[] buf = new byte[512];
        DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
        socket.receive(recvPacket);

        String address = recvPacket.getAddress().getHostAddress();
        int port = recvPacket.getPort();
        int dataLength = recvPacket.getLength();

        byte[] recvData = recvPacket.getData();
        System.out.println("provider address :" + address + "p:" + port);
        System.out.println("provider receive :" + new String(recvData, 0, dataLength));

    }
}
