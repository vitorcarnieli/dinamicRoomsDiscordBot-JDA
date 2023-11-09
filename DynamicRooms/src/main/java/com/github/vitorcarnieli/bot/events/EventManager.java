package com.github.vitorcarnieli.bot.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class EventManager extends ListenerAdapter {
    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        System.out.println("criou");
    }



}
