package com.github.vitorcarnieli.bot;

import com.github.vitorcarnieli.bot.commands.CommandManager;
import com.github.vitorcarnieli.bot.events.EventManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
/*
*
* JDA
*
* */

public class BotMain {
    private final ShardManager SHARD_MANAGER;
    private final Dotenv CONFIG;

    public BotMain() throws LoginException {
        CONFIG = Dotenv.configure().ignoreIfMissing().load();
        String token = CONFIG.get("TOKEN");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.of(Activity.ActivityType.CUSTOM_STATUS, "Gerenciando"));

        SHARD_MANAGER = builder.build();
        SHARD_MANAGER.addEventListener(new CommandManager(), new EventManager());
    }

    public Dotenv getCONFIG() {
        return CONFIG;
    }

    public ShardManager getSHARD_MANAGER() {
        return  SHARD_MANAGER;
    }
}
