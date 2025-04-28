package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Gchat {
    private String[] args;
    private CommandSource source;

    public Gchat(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        args[0] = "";

        Player p = (Player) source;
        if (Storage.isplayeringuild(p.getUsername())) {
//            System.out.println("a player guildban van");
            GuildObject guild = Storage.whatisplayerguild(p.getUsername());
            guild.getMembers().forEach(k -> {
                Guild.getServer().getPlayer(k).ifPresent(player -> {
                    player.sendMessage(Component.text("[").color(NamedTextColor.YELLOW)
                            .append(Component.text("Guild")).color(NamedTextColor.WHITE)
                            .append(Component.text("] ").color(NamedTextColor.YELLOW))
                            .append(Component.text(p.getUsername()).color(NamedTextColor.YELLOW))
                            .append(Component.text(" » ").color(NamedTextColor.GRAY))
                            .append(Component.text(String.join(" ", args)).color(NamedTextColor.WHITE)));
                });
            });
        }else {
            p.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
        }
    }
}
