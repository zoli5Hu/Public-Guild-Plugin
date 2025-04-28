package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.units.qual.N;

import java.util.Objects;
import java.util.Optional;

public class GDemote {
    private String[] args;
    private CommandSource source;

    public GDemote(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;

        if (args.length > 2) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Helytelen használat! /guild demote <játékos>").color(NamedTextColor.RED)));
            return;
        }
        Player p = (Player) source;
        if (Storage.isplayeringuild(p.getUsername())) {
            if (Storage.isplayerguildowner(p.getUsername())) {
                Optional<Player> target = Guild.getServer().getPlayer(args[1]);
                if (target.isPresent()) {
                    if (Objects.requireNonNull(Storage.whatisplayerguild(p.getUsername())).getMembers().contains(target.get().getUsername())) {
                        GuildObject guild = Storage.whatisplayerguild(p.getUsername());
                        if (guild.getCo_owners().contains(target.get().getUsername())) {
                            guild.getCo_owners().remove(target.get().getUsername());
                            p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen lefokoztad a játékost!").color(NamedTextColor.GREEN)));
                            target.get().sendMessage(Storage.standerdtext().append(Component.text("Lefokoztak a guildben!").color(NamedTextColor.YELLOW)));
                        } else {
                            p.sendMessage(Storage.standerdtext().append(Component.text("Ez a játékos nem co-owner!").color(NamedTextColor.RED)));
                        }
                    }

                } else {
                    GuildObject guild = Storage.whatisplayerguild(p.getUsername());
                    for(var i : guild.getCo_owners()){
                        if(i.equalsIgnoreCase(args[1])){
                            guild.getCo_owners().remove(i);
                            p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen lefokoztad a játékost! de nem online").color(NamedTextColor.GREEN)));
                            return;
                        }
                    }
                    p.sendMessage(Storage.standerdtext().append(Component.text("Ez a játékos nem co-owner!").color(NamedTextColor.RED)));


                }
            } else {
                source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
            }
        }
    }
}
