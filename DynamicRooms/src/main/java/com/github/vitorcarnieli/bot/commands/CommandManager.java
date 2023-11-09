package com.github.vitorcarnieli.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CommandManager extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("dyrm")) {
            EntitySelectMenu menu = EntitySelectMenu.create("menu:users", EntitySelectMenu.SelectTarget.USER)
                    .setRequiredRange(2, 25)
                    .build();


        }
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:users")) {

            String roleName = event.getUser().getName() + "-ROOM-" + new Date().getTime();

            Role role = Objects.requireNonNull(event.getGuild()).createRole().setName(roleName).complete();

            event.getGuild().addRoleToMember(event.getUser(), role).queue();

            event.getMentions().getUsers().forEach(user -> {
                event.getGuild().addRoleToMember(user, role).queue();
            });
            Category category = event.getGuild().createCategory(roleName).complete();
            category.upsertPermissionOverride(role).setAllowed(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
            category.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
            category.createTextChannel("txt").queue();
            category.createVoiceChannel("voice").queue();
            event.reply("ok").queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> comandData = new ArrayList<>();
        comandData.forEach(i -> {
            if (i.equals(Commands.slash("dyrm", "create room"))) return;
        });
        comandData.add((Commands.slash("dyrm", "create room")));
        event.getGuild().updateCommands().addCommands(comandData).queue();

    }

    //TODO: delete channels when category is destroyed
    //TODO: implements delete empty category after 5 minutes
    //TODO: bug, create 2 channels
}
