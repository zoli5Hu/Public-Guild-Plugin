package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Lib.Storage;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

public class AdminInfo {
    String[] args;
    CommandSource source;

    public AdminInfo(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        Player p = (Player) source;
        Storage.guilds.forEach(k -> {
            p.sendMessage(Component.text("Guild name: " + k.getName()));
            p.sendMessage(Component.text("Guild owner: " + k.getOwner()));
            p.sendMessage(Component.text("Guild description: " + k.getGuild_description()));
            k.getMembers().forEach(j -> {
                p.sendMessage(Component.text("Guild member: " + j));
            });

            k.getCo_owners().forEach(j -> {
                p.sendMessage(Component.text("Guild co-owner: " + j));
            });
        });

        p.sendMessage(Component.text("-------------------- " ));
        Storage.tempguilds.forEach((k, v) -> {
            p.sendMessage(Component.text("Guild name: " + k));
            v.forEach(j -> {
                p.sendMessage(Component.text("Guild member: " + j.getName()));
            });
        });
    }
}
