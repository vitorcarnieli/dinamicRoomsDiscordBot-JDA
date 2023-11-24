package com.github.vitorcarnieli.bot.events;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EventManager extends ListenerAdapter {


    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        try {

            VoiceChannel channel = Objects.requireNonNull(event.getChannelLeft()).asVoiceChannel();
            String identifier = channel.getName().split("_")[1];
            if (!channel.getName().contains("voice_")) return;
            deleteRoom(channel);


        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        try {
            VoiceChannel channel = event.getChannel().asVoiceChannel();
            deleteRoom(channel);
        } catch (Exception e) {
            return;
        }
    }

    private void deleteRoom(VoiceChannel channel) {
        System.out.println("começou");
        String identifier = channel.getName().split("_")[1];

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        AtomicInteger delay = new AtomicInteger(1);
        int interval = 1;
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!channel.getMembers().isEmpty()) {
                    scheduler.shutdown();
                    return;
                }

                if (delay.get() >= 5) {
                    scheduler.shutdown();

                    Objects.requireNonNull(channel.getParentCategory()).getVoiceChannels().forEach(v -> v.delete().queue());
                    channel.getParentCategory().getTextChannels().forEach(t -> t.delete().queue());
                    channel.getParentCategory().delete().queue();
                    channel.getGuild().getRoles().forEach(r -> {
                        if (r.getName().contains(identifier)) {
                            r.delete().queue();
                            return;
                        }
                    });
                }
                if (Integer.parseInt(delay.toString()) > 1) {
                    channel.getParentCategory().getTextChannels().get(0).sendMessage("deletando sala em " + (6 - Integer.parseInt(delay.toString()))+ " minutos por inatividade!").queue();
                } else {
                    channel.getParentCategory().getTextChannels().get(0).sendMessage("deletando sala em " + (6 - Integer.parseInt(delay.toString()))+ " minuto por inatividade!").queue();
                }

                delay.getAndIncrement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, interval, TimeUnit.MINUTES);
    }

}


