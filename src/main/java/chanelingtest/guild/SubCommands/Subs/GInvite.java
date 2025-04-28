package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.CountDown.CD;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GInvite {
    private String[] args;
    private CommandSource source;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ProxyServer server = Guild.getServer();
    public GInvite(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        //rossz parancs
        if (args.length < 2) {
            source.sendMessage(Storage.standerdtext().append(Component.text("Használat: /guild invite <játékos>").color(NamedTextColor.RED)));
            return;
        }

        Player p = (Player) source;
        if(args[1].equalsIgnoreCase(p.getUsername())){
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem hívhatod meg magad!").color(NamedTextColor.RED)));
            return;
        }

        //a játékosnak már van guildje
        if(Storage.isplayeringuild(args[1])){
            source.sendMessage(Storage.standerdtext().append(Component.text("A játékosnak már van guilded!").color(NamedTextColor.RED)));
            return;
        }

        Player intived = server.getPlayer(args[1]).orElse(null);
        if(intived == null){
            source.sendMessage(Component.text("nincs fent a játékost").color(NamedTextColor.RED));
            return;
        }

        //g
        if(Storage.isplayerguildowner(p.getUsername())){
            GuildObject guild = Storage.whatisplayerguild(p.getUsername());
            //max player lekezelés
//            System.out.println("a guild neve: " + guild.getName());
            if(guild.getMembers().size() >= 16){
                source.sendMessage(Storage.standerdtext().append(Component.text("A guildod tele van!").color(NamedTextColor.RED)));
            }else {
                //a játékost éppen hívja egy másik guild is
                if(Storage.istempguildbyname(args[1])) {
//                    System.out.println("a guild már megvolt hívva : " + Storage.tempguilds.get(args[1]));
                    if(Storage.isplayerintemguild(args[1], guild.getName())) {
                        source.sendMessage(Storage.standerdtext().append(Component.text("Ezt a játékost már meghívtad").color(NamedTextColor.RED)));
                    }else {
                        //méég nincs ebbe a guildba meghívva
//                        System.out.println("guild még nincs meghívva" + Storage.tempguilds.get(args[1]));
                        Storage.tempguilds.get(args[1]).add(guild);
                        startCountdown(p, server.getPlayer(args[1]).orElse(null),guild);
                        intived = server.getPlayer(args[1]).orElse(null);//+ " meghívott a " +guild.getName() + "guild-ba"
                        intived.sendMessage(Storage.standerdtext()
                                .append(Component.text(p.getUsername()).color(NamedTextColor.WHITE)
                                        .append(Component.text(" meghívott a ").color(NamedTextColor.YELLOW)
                                                .append(Component.text(guild.getName())).color(NamedTextColor.WHITE)
                                                .append(Component.text(" guild-ba!").color(NamedTextColor.YELLOW)))));
                        source.sendMessage(Component.text("Meghívtad a játékost a guildba!").color(NamedTextColor.YELLOW));
                    }
                }else {
                    //nem hívta meg még senki
//                    System.out.println("nem hívta meg még senki ezért újat hozunk létre");
                    Storage.tempguilds.put(args[1], new ArrayList<>(List.of(guild)));

                    startCountdown(p, intived, guild);
                    assert intived != null;
                    intived.sendMessage(Storage.standerdtext()
                            .append(Component.text(p.getUsername()).color(NamedTextColor.WHITE)
                                    .append(Component.text(" meghívott a ").color(NamedTextColor.YELLOW)
                                            .append(Component.text(guild.getName())).color(NamedTextColor.WHITE)
                                            .append(Component.text(" guild-ba!").color(NamedTextColor.YELLOW)))));
                    source.sendMessage(Storage.standerdtext().append(Component.text("Meghívtad a játékost a guildba!").color(NamedTextColor.YELLOW)));
                }

            }
        }else {
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy guild tulajdonos!").color(NamedTextColor.RED)));
        }
    }

    private void startCountdown(Player p, Player invited, GuildObject guild) {
        if (invited != null) {
            CD countdown = new CD(p, invited, 30,guild);
            countdown.start(scheduler);
        }
    }
}
