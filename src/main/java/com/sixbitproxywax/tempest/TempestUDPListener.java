package com.sixbitproxywax.tempest;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class TempestUDPListener {
  private static final int PORT = 50222; // Default Tempest UDP port

  public static void main(String[] args) throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioDatagramChannel.class)
          .handler(new ChannelInitializer<NioDatagramChannel>() {
            @Override
            public void initChannel(NioDatagramChannel ch) {
              ch.pipeline().addLast(new TempestPacketHandler());
            }
          });

      ChannelFuture f = b.bind(PORT).sync();
      System.out.println("Listening for UDP packets on port " + PORT + "...");
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully();
    }
  }
}
