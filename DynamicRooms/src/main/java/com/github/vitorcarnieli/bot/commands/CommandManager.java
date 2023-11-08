package com.github.vitorcarnieli.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import org.jetbrains.annotations.*;

import java.util.Date;

public class CommandManager extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("dyrm")) {
            EntitySelectMenu menu = EntitySelectMenu.create("menu:users", EntitySelectMenu.SelectTarget.USER)
                    .setRequiredRange(2, 25)
                    .build();

            event.reply("Selecione quem você deseja que acesse sua sala: ")
                    .setEphemeral(true)
                    .addActionRow(menu)
                    .queue();
        }
    }

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:users")) {

            String roleName = event.getUser().getName() + "-ROOM-" + new Date().getTime();

            Role role = event.getGuild().createRole().setName(roleName).complete();

            event.getGuild().addRoleToMember(event.getUser(), role).queue();

            event.getMentions().getUsers().forEach(user -> {
                event.getGuild().addRoleToMember(user, role).queue();
            });
            Category category = event.getGuild().createCategory("Nome da Categoria").complete();
            category.upsertPermissionOverride(role).setAllowed(Permission.VIEW_CHANNEL, Permission.VOICE_CONNECT).queue();
            category.upsertPermissionOverride(event.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
            category.createTextChannel("txt").queue();
            category.createVoiceChannel("voice").queue();
            event.deferReply().queue();
        }
    }

    //TODO: delete channels when category is destroyed
    //TODO: implements delete empty category after 5 minutes
}
