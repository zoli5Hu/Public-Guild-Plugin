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

import java.util.Optional;

public class GKick {
    private String[] args;
    public CommandSource source;

    public GKick(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        Player  p = (Player) source;
        if(args.length < 2){
            source.sendMessage(Storage.standerdtext().append(Component.text("Használat: /guild kick <játékos>").color(NamedTextColor.YELLOW)));
            return;
        }

        if(Storage.iscoowner(args[1]) || Storage.isplayerguildowner(args[1])){
            source.sendMessage(Storage.standerdtext().append(Component.text("őt nem rúghatod ki a guildból").color(NamedTextColor.RED)));

            return;
        }

        if(Storage.isplayeringuild(p.getUsername())){
            if(Storage.isplayerguildowner(p.getUsername()) || Storage.iscoowner(p.getUsername() )){
                GuildObject guild = Storage.whatisplayerguild(p.getUsername());
                if(Storage.isplayeringuild(args[1])){
                    if(Storage.isplayercoowner(args[1])){
                        if(Storage.isplayerguildowner(p.getUsername())){
                            Storage.removeplayerfromguildcoowner(args[1]);
//                            System.out.println("a guild neve: " + guild.getName());
                            GDB db = new GDB(Guild.getLogger());
                            db.removeplayerguild(args[1]);

                            XpConnector xp = new XpConnector(Guild.getLogger());
                            int playxp = xp.getxpbyname(args[1]);
                            db.updateGxpandGmemberp(guild.getName(), playxp, false);


//                            System.out.println("a játékost kikkeltéék " + args[1]);
                            Optional<Player> player = Guild.getServer().getPlayer(args[1]);
                            if(player.isPresent()){
                                player.get().sendMessage(Storage.standerdtext().append(Component.text("Kickeltek a guildből!").color(NamedTextColor.RED)));
                            }
                            p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen kickelted a játékost!").color(NamedTextColor.GREEN)));
                            //message
                            StringBuilder sb = new StringBuilder();
                            sb.append(args[1]);
                            sb.append(";");
                            sb.append(guild.getName());
                            sb.append(";");
                            sb.append("remove");

                            Storage.sendMessageToServer("lobby",sb.toString());
                        }else {
                            source.sendMessage(Storage.standerdtext().append(Component.text("Ezt a játékost csak a guild tulajdonosa tudja kickelni!").color(NamedTextColor.RED)));
                        }

                    }else {
                        Storage.removeplayerformhuildmember(args[1]);
//                        System.out.println("a guild neve: " + guild.getName());
//                        System.out.println("a játékost kikkeltéék " + args[1]);
                        GDB db = new GDB(Guild.getLogger());
                        db.removeplayerguild(args[1]);
                        //xp gumember stv
                        XpConnector xp = new XpConnector(Guild.getLogger());
                        int playxp = xp.getxpbyname(args[1]);
                        db.updateGxpandGmemberp(guild.getName(), playxp, false);

                        Optional<Player> player = Guild.getServer().getPlayer(args[1]);
                        if(player.isPresent()){
                            player.get().sendMessage(Storage.standerdtext().append(Component.text("Kickeltek a guildből!").color(NamedTextColor.RED)));
                        }
                        p.sendMessage(Storage.standerdtext().append(Component.text("Sikeresen kikickelted a játékost!").color(NamedTextColor.GREEN)));

                        StringBuilder sb = new StringBuilder();
                        sb.append(args[1]);
                        sb.append(";");
                        sb.append(guild.getName());
                        sb.append(";");
                        sb.append("remove");

                        Storage.sendMessageToServer("lobby",sb.toString());

                    }
                }else {
                    source.sendMessage(Storage.standerdtext().append(Component.text("Ez a játékos nem a guild tagja!").color(NamedTextColor.RED)));
                }
            }else {
                source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy a guild tulajdonosa! vagy co-owner!").color(NamedTextColor.RED)));
            }

        }else {
            source.sendMessage(Storage.standerdtext().append(Component.text("Nem vagy egy guild tagja!").color(NamedTextColor.RED)));
        }
    }
}
