package com.github.vitorcarnieli;

import com.github.vitorcarnieli.bot.BotMain;

import javax.security.auth.login.LoginException;

/*
* JDA-5.0.0-beta.17 DynamicRooms Discord Bot
*
* @author Vítor Carnieli Belisário <a href="https://github.com/vitorcarnieli/dynamicRoomsDiscordBot-JDA">...</a>
*
*/

public class Launcher {

    public static void main(String[] args) {
        System.out.println("|===========================================|");
        System.out.println("|============== DYNAMIC ROOMS ==============|");
        System.out.println("|===========================================|");

        try {
            BotMain bot = new BotMain();

        } catch (LoginException e) {
            System.out.println("exception thrown during bot initialization: " + e.getMessage());
        }
    }

}
