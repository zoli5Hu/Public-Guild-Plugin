package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Database.XpConnector;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GLeave {
    private String[] args;
    private CommandSource source;

    public GLeave(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        //leave guild leave
        Player p = (Player) source;
        if (args.length != 1) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Használd: /guild leave").color(NamedTextColor.RED)));
            return;
        }

        if(!Storage.isplayeringuild(p.getUsername())){
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
            return;
        }

        if (Storage.isplayerguildowner(p.getUsername())) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem hagyhatod el a guild-ed, mert te vagy a tulajdonosa!").color(NamedTextColor.RED)));
            return;
        }

        for(GuildObject guild : Storage.guilds){


            if(guild.getMembers().contains(p.getUsername())){
                StringBuilder sb = new StringBuilder();
                sb.append(p.getUsername());
                sb.append(";");
                sb.append(guild.getName());
                sb.append(";");
                sb.append("remove");
//                System.out.println("message előtt " + sb.toString());
                Storage.sendMessageToServer("lobby",sb.toString());
//                System.out.println("message után " + sb.toString());
                source.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen kiléptél a guildedből!").color(NamedTextColor.GREEN)));
                if(Storage.isplayercoowner(p.getUsername())){
                    Storage.removeplayerfromguildcoowner(p.getUsername());
                    Storage.removeplayerformhuildmember(p.getUsername());

                } else if (Storage.isplayeringuild(p.getUsername())) {
                    Storage.removeplayerformhuildmember(p.getUsername());
                }

                GDB db = new GDB(Guild.getLogger());
                db.removeplayerguild(p.getUsername());
                Storage.sendMessageToUpdateBoard(p.getUsername());
                XpConnector xp = new XpConnector(Guild.getLogger());
                int playxp = xp.getxpbyname(p.getUsername());
                db.updateGxpandGmemberp(guild.getName(), playxp, false);

                guild.getMembers().forEach(k -> {
                    Guild.getServer().getPlayer(k).ifPresent(player -> {
                        player.sendMessage(Storage.standerdtext().append(Component.text(p.getUsername() + " kilépett a guildedből!").color(NamedTextColor.RED)));
                    });
                });
            }
        }
    }
}
