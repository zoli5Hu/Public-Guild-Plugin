package chanelingtest.guild;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Database.XpConnector;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import chanelingtest.guild.SubCommands.GuildCommand;
import chanelingtest.guild.VelocityRecive.Reciveupdate;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "guild",
        name = "Guild",
        version = "1.0-SNAPSHOT"
)
public class Guild {

    private static ProxyServer server;
    private static Logger logger;

    @Inject
    public Guild(ProxyServer server, Logger logger) {
        Guild.server = server;
        Guild.logger = logger;
        GDB g = new GDB(logger);
        g.initDatabaseConnection();
        XpConnector xp = new XpConnector(logger);
        xp.initDatabaseConnection();
        // Register the main command
        server.getCommandManager().register(server.getCommandManager().metaBuilder("guild").build(), new GuildCommand());
        // Assuming ExampleCommand implements the required interface for commands
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        GDB gdb = new GDB(logger);
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("guildpvp:operation"));
        server.getChannelRegistrar().register(MinecraftChannelIdentifier.from("xp:update"));
        server.getEventManager().register(this, new Reciveupdate());
        gdb.createTableIfNotExist();
        logger.info("Guild plugin has been initialized!");
//        System.out.println("***********************");

        //data loading
        //guilds
        gdb.loaddata();

    }

    public static Logger getLogger() {
        return logger;
    }

    public static ProxyServer getServer() {
        return server;
    }
}
