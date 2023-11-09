package com.github.vitorcarnieli.bot.events;

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EventManager extends ListenerAdapter {


    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {

        try {

            VoiceChannel channel = Objects.requireNonNull(event.getChannelLeft()).asVoiceChannel();
            String identifier = channel.getName().split("_")[1];
            System.out.println(identifier);
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

    private void deleteRoom(VoiceChannel channel) throws InterruptedException {
        String identifier = channel.getName().split("_")[1];
        int cont = 0;
        boolean isEmpty = true;

        do {
            Thread.sleep(60000);
            if (channel.getMembers().isEmpty()) {
                System.out.println("canal is empty");
            } else {
                isEmpty = false;
                break;
            }
            cont++;
        } while (cont < 5);

        if (isEmpty) {
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
    }


}


