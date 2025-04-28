package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class GPromote {
    private String[] args;
    private CommandSource source;

    public GPromote(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        if (args.length < 2) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Használat: /guild promote <játékos>").color(NamedTextColor.RED)));
            return;
        }
        Player p = (Player) source;
        if (Storage.isplayeringuild(p.getUsername())) {
            if (Storage.isplayerguildowner(p.getUsername())) {
                GuildObject guild = Storage.whatisplayerguild(p.getUsername());
                if (Storage.isplayeringuild(args[1])) {
                    if (Storage.isplayercoowner(args[1])) {
                        source.sendMessage(Component.text("Ez a játékos már coowner!"));
                        return;
                    }
//                    System.out.println("a guild neve: " + guild.getName());
//                    System.out.println("a játékost promózták " + args[1]);
                    Optional<Player> player = Guild.getServer().getPlayer(args[1]);
                    if (player.isPresent()) {
                        player.get().sendMessage(Storage.standerdtext().append(Component.text("Promóztak coownernek!").color(NamedTextColor.RED)));
                        p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen promóztad a játékost!").color(NamedTextColor.GREEN)));
                        Storage.guilds.forEach(k -> {
                            if (k.getMembers().contains(player.get().getUsername())) {
                                k.getCo_owners().add(player.get().getUsername());
                                //                            System.out.println("a guild neve: " + k.getName());
                                //                            System.out.println("a játékost promózták " + args[1]);
                            }


                        });

                    }else {
                        p.sendMessage(Storage.standerdtext().append(Component.text("A játékos jelenleg nem elérhető").color(NamedTextColor.GREEN)));

                    }
                } else {
                    source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy a guild tulajdonosa!").color(NamedTextColor.RED)));
                }

            } else {
                source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
            }
        }
    }
}
