package com.sixbitproxywax.tempest;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sixbitproxywax.tempest.model.RapidWindPacket;
import com.sixbitproxywax.tempest.model.TempestPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class TempestPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {
  private static final Gson gson = new Gson();

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
    String jsonString = packet.content().toString(StandardCharsets.UTF_8);
    try {
      final var jsonObject = gson.fromJson(jsonString, JsonObject.class);
      final var type = TempestPacket.Type.parse(jsonObject.get("type").getAsString());

      final var parsedPacket = switch (type) {
        case TempestPacket.Type.RAPID_WIND -> parseRapidWind(jsonObject);
        default -> new RuntimeException("Unsupported Type: " + type);
      };

      System.out.println("Parsed Packet: " + parsedPacket);

    } catch (Exception e) {
      System.err.println("Error parsing packet: " + e.getMessage());
      System.err.println("Raw data: " + jsonString);
    }
  }

  private static TempestPacket parseRapidWind(final JsonObject json) {
    final var serialNumber = json.get("serial_number").getAsString();
    final var hubSerialNumber = json.get("hub_sn").getAsString();
    final var observation = json.getAsJsonArray("ob");
    final var timeEpoch = Instant.ofEpochSecond(observation.get(0).getAsLong());
    final var windSpeed = observation.get(1).getAsDouble();
    final var windDirection = observation.get(2).getAsInt();
    return new RapidWindPacket(serialNumber, hubSerialNumber, timeEpoch, windSpeed, windDirection);
  }
}
