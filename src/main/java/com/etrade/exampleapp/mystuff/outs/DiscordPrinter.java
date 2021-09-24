package com.etrade.exampleapp.mystuff.outs;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;

public class DiscordPrinter extends OutObject{

    TextChannel channel;

    public DiscordPrinter(String channelId) {
        DiscordApi api = new DiscordApiBuilder().setToken("ODA1MTAzMDk1MDEzMDQ4MzMy.YBWArw.YzdoaKMpBqfQ8zWRquWrlFVnRxQ").login().join();
        channel = api.getTextChannelById(channelId).get();
    }

    @Override
    public void print(String message) {
        channel.sendMessage(message);
    }
}