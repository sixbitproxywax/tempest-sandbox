package com.sixbitproxywax.tempest;

import com.sixbitproxywax.tempest.model.parsing.TempestPacketParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.nio.charset.StandardCharsets;

public class TempestPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
        String jsonString = packet.content().toString(StandardCharsets.UTF_8);
        try {
            final var parsedPacket = TempestPacketParser.parse(jsonString);
            System.out.println("Parsed Packet: " + parsedPacket);
        } catch (Exception e) {
            System.err.println("Error parsing packet: " + e.getMessage());
            System.err.println("Raw data: " + jsonString);
        }
    }
}
