package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Database.XpConnector;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Gcreate {
    private String[] args;
    private CommandSource source;


    public Gcreate(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        Player owner = (Player) source;

        if(args.length != 2){
            owner.sendMessage(Storage.standerdtext().append(Component.text("Helytelen használat! /guild accept <név>").color(NamedTextColor.RED)));
            return;
        }

        if(Storage.isplayeringuild(owner.getUsername())){
            owner.sendMessage(Storage.standerdtext().append(Component.text("Már tagja vagy egy guildnek!").color(NamedTextColor.RED)));
            return;
        }
        if(!Storage.guilds.contains(args[1])){
            GuildObject guild = new GuildObject(args[1], owner.getUsername(), 0);
            Storage.guilds.add(guild);
            owner.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen létrehoztad a guildet!").color(NamedTextColor.GREEN)));
            XpConnector xp = new XpConnector(Guild.getLogger());
            GDB db = new GDB(Guild.getLogger());
            db.createTableIfNotExist();
            db.addGuild(guild.getName(), guild.getOwner(), xp.getxpbyname(guild.getOwner()), guild.getMembers().size());


            //send message
            StringBuilder sb = new StringBuilder();
            sb.append(owner.getUsername());
            sb.append(";");
            sb.append(guild.getName());
            sb.append(";");
            sb.append("add");

            Storage.sendMessageToServer("lobby",sb.toString());
            Storage.sendMessageToUpdateBoard(owner.getUsername());
        }else {
            owner.sendMessage(Storage.standerdtext().append(Component.text("Ez a név már foglalt!").color(NamedTextColor.RED)));

        }
    }
}
