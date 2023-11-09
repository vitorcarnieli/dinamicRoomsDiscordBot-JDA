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
            event.reply("## Criando sala...\n*selecione os usuários que poderam visualizar e entrar em sua sala:*").addActionRow(EntitySelectMenu.create("menu:users", EntitySelectMenu.SelectTarget.USER).setRequiredRange(2, 25).build()).queue();
        }
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {

        if (event.getComponentId().equals("menu:users")) {
            String time = new Date().getTime() + "";
            String roleName = event.getUser().getName() + "-ROOM_" + time;

            Role role = Objects.requireNonNull(event.getGuild()).createRole().setName(roleName).complete();

            event.getGuild().addRoleToMember(event.getUser(), role).queue();

            event.getMentions().getUsers().forEach(user -> {
                event.getGuild().addRoleToMember(user, role).queue();
            });
            Category category = event.getGuild().createCategory(roleName).complete();
            category.upsertPermissionOverride(role).setAllowed(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
            category.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
            category.createTextChannel("txt_" + time).queue();
            category.createVoiceChannel("voice_" + time).queue();
            event.reply("ok").queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> comandData = new ArrayList<>();
        comandData.add((Commands.slash("dyrm", "create room")));

        event.getGuild().updateCommands().addCommands(comandData).queue();
    }

}
