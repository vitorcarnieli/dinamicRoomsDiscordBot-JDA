package com.github.vitorcarnieli.bot.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
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

    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:users")) {
            List<User> users = event.getMentions().getUsers();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("dyrm", "List of commands by the bot"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
