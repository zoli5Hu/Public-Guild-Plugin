package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GDescription {
    private String[] args;
    private CommandSource source;

    public GDescription(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        if(args.length <= 2){
            source.sendMessage(Storage.standerdtext().append(Component.text("Helytelen használat! /guild description <leírás>").color(NamedTextColor.RED)));
            return;
        }
        Player p = (Player) source;
        if (Storage.isplayeringuild(p.getUsername())) {
//            System.out.println("a player guildban van");
            if (Storage.isplayerguildowner(p.getUsername())) {
//                System.out.println("a player guild owner is");
                GuildObject guild = Storage.whatisplayerguild(p.getUsername());
                Storage.guilds.forEach(k -> {
                    if(k.getName().equals(guild.getName())){
                        StringBuilder dp = new StringBuilder();
                        for(int i = 1;i< args.length;i++){

                            dp.append(args[i]).append(" ");
                        }
                       k.setGuild_description(dp.toString());
                       p.sendMessage(Component.text("sikeresen beállítottad a descriptiont"));
                    }
                });
            }else {
                p.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy a guild tulajdonosa!").color(NamedTextColor.RED)));
            }
        }else {
            p.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!")));
        }
    }
}
