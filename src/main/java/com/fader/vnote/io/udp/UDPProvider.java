package com.fader.vnote.io.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author FaderW
 * 2019/8/2
 */

public class UDPProvider {

    public static void main(String[] args) throws IOException {
        DatagramSocket provider = new DatagramSocket(20000);
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        provider.receive(packet);
        byte[] recvData = packet.getData();
        int dataLen = packet.getLength();

        String address = packet.getAddress().getHostAddress();
        int port = packet.getPort();
        System.out.println("receive search form " + address + "p:" + port);
        System.out.println("msg " + new String(recvData, 0, dataLen));


        byte[] sendData = ("receive search data len " + dataLen).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData,
                sendData.length,
                packet.getAddress(),
                port);

        provider.send(sendPacket);
        System.out.println("UDP Provider close");
        provider.close();
    }
}
