package com.sixbitproxywax.tempest;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sixbitproxywax.tempest.model.RapidWindPacket;
import com.sixbitproxywax.tempest.model.TempestPacket;

import com.sixbitproxywax.tempest.model.parsing.TempestPacketParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class TempestPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {
  private static final Gson gson = new Gson();

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
