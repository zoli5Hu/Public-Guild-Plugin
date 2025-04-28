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
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Gaccept {
    private String[] args;
    private CommandSource source;

    public Gaccept(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        if (args.length < 2) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Helytelen használat! /guild accept <guildnév>").color(NamedTextColor.RED)));
            return;
        }

        Player p = (Player) source;
        String inviterName = args[1];

        if(Storage.isplayeringuild(p.getUsername())){
            p.sendMessage(Storage.standerdtext().append(Component.text("már guildban vagy").color(NamedTextColor.RED)));
            return;
        }

        //test
//        p.sendMessage(Component.text("-------------------------- " ));
//        for(Map.Entry<String, List<GuildObject>> gt : Storage.tempguilds.entrySet()){
//            p.sendMessage(Component.text("key: " + gt.getKey()));
//            for(GuildObject g : gt.getValue()){
//                p.sendMessage(Component.text("guild name: " + g.getName()));
//            }
//        }
//        p.sendMessage(Component.text("-------------------------- " ));


        if (Storage.istempguildbyname(p.getUsername())) {
            if(Storage.isguildtemvalid(p.getUsername(), args[1])){
                Storage.removetempguildbyname(p.getUsername(),args[1]);
                for (GuildObject guild : Storage.guilds) {
                    if (guild.getName().equalsIgnoreCase(args[1])) {
                        guild.getMembers().add(p.getUsername());
                        //playg
                        GDB db = new GDB(Guild.getLogger());
                        db.addplayerguild(p.getUsername(), guild.getName());
                        //xp guild stb
                        XpConnector xp = new XpConnector(Guild.getLogger());
                        int xpofplayer = xp.getxpbyname(p.getUsername());
                        db.updateGxpandGmemberp(guild.getName(), xpofplayer,true);


                        //send message pvp
                        StringBuilder sb = new StringBuilder();
                        sb.append(p.getUsername());
                        sb.append(";");
                        sb.append(guild.getName());
                        sb.append(";");
                        sb.append("add");

                        Storage.sendMessageToServer("lobby",sb.toString());
                        p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen csatlakoztál a guild-hoz!").color(NamedTextColor.GREEN)));
                        Storage.sendMessageToUpdateBoard(p.getUsername());
                        guild.getMembers().forEach(k -> {
                            if(!k.equalsIgnoreCase(p.getUsername())){

                                Guild.getServer().getPlayer(k).ifPresent(presplayer -> {
                                    presplayer.sendMessage(Storage.standerdtext().append(Component.text(p.getUsername() + " Csatlakozott a guild-hez!").color(NamedTextColor.YELLOW)));

                                });

                            }
                        });
                        return;
                    }
                }
                p.sendMessage(Storage.standerdtext().append(Component.text("Nem található ilyen meghívás!1").color(NamedTextColor.RED)));
            }else {
                p.sendMessage(Storage.standerdtext().append(Component.text("Nem található ilyen meghívás!2").color(NamedTextColor.RED)));
            }
        }else {
            p.sendMessage(Storage.standerdtext().append(Component.text("Nem található ilyen meghívás!3").color(NamedTextColor.RED)));
        }

    }
}
