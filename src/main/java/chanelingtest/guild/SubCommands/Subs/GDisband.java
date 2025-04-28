package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Objects;

public class GDisband {
    private String[] args;
    private CommandSource source;

    public GDisband(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;

        if(args.length != 1){
            source.sendMessage(Storage.standerdtext().append(Component.text("Helytelen használat! /guild disband").color(NamedTextColor.RED)));
            return;
        }
        Player p = (Player) source;
        GuildObject guild = Storage.whatisplayerguild(p.getUsername());
        if (guild == null) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy guildba").color(NamedTextColor.RED)));

        }

        if (Storage.isplayeringuild(p.getUsername())) {
            if (Storage.isplayerguildowner(p.getUsername())) {
                for (GuildObject guildit : Storage.guilds) {
                    if (Objects.equals(guildit.getName(), guild.getName())){
//                        System.out.println("guildit.getMembers() " + guildit.getMembers());
                        for (String member : guildit.getMembers()) {

                            StringBuilder sb = new StringBuilder();
                            sb.append(member);
                            sb.append(";");
                            sb.append(guildit.getName());
                            sb.append(";");
                            sb.append("remove");
//                            System.out.println("message előtt " + sb.toString());
                            Storage.sendMessageToServer("lobby", sb.toString());
//                            System.out.println("message után " + sb.toString());
                        }

                    }
                    Storage.sendMessageToUpdateBoard(p.getUsername());
                    if(Objects.equals(guildit.getName(), guild.getName())){
                        guildit.getMembers().forEach(k -> {
                            Guild.getServer().getPlayer(k).ifPresent(player -> {
                                System.out.println("player: megkapta" + player);
                                if(player!= p){
                                    player.sendMessage(Storage.standerdtext().append(Component.text("A guilded feloszlott!").color(NamedTextColor.YELLOW)));
                                    Storage.sendMessageToUpdateBoard(player.getUsername());

                                }
                            });

                        });

                    }
                }
                Storage.removefullguildbyguild(guild);
                p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen feloszlattad a guilded!").color(NamedTextColor.GREEN)));
                GDB db = new GDB(Guild.getLogger());
                db.removelallguildlabel(guild.getName());
                db.removeguild(guild.getName());
//                System.out.println("guild.getMembers()  for elott" + guild.getMembers());
//                System.out.println("guilds size)  for elott" + Storage.guilds.size());
            } else {
                source.sendMessage(Storage.standerdtext().append(Component.text("Ezt a parancsot csak a guild tulajdonosa használhatja!").color(NamedTextColor.RED)));
            }
        } else {
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
        }
    }
}
