package chanelingtest.guild.Lib;

import chanelingtest.guild.Guild;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Storage {

    public  static  List<GuildObject> guilds = new ArrayList<>();
    public  static HashMap<String, List<GuildObject>> tempguilds = new HashMap<>();

    public static void guildinfo(Player p) {
        if(!isplayeringuild(p.getUsername())){
            p.sendMessage(Component.text("Nem vagy egy guild tagja!"));
            return;
        }
        guilds.forEach(k -> {
            if(k.getMembers().stream().anyMatch(j -> j.equals(p.getUsername()))){
                p.sendMessage(Component.text("Guild name: " + k.getName()));
                p.sendMessage(Component.text("Guild owner: " + k.getOwner()));
                p.sendMessage(Component.text("Guild description: " + k.getGuild_description()));
                k.getMembers().forEach(j -> {
                    if(Storage.isplayercoowner(j)){
                        p.sendMessage(Component.text("Guild co-owner: " + j));

                    } else if (Storage.isplayerguildowner(j)) {

                    }else {
                        p.sendMessage(Component.text("Guild member: " + j));

                    }
                });
            }
        });
    }

    public static boolean istempguildbyname(String name) {
        for(var tmp : tempguilds.entrySet()){
            if(tmp.getKey().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public static GuildObject whatisplayerguild(String name) {
        for(GuildObject guild : guilds){
            if(guild.getMembers().stream().anyMatch(k -> k.equals(name))){
                return guild;
            }
        }
        return null;
    }

    public static GuildObject whatisplayerguildcoowner(String name) {
        for(GuildObject guild : guilds){
            if(guild.getCo_owners().stream().anyMatch(k -> k.equals(name))){
                return guild;
            }
        }
        return null;
    }

    public static GuildObject whatisplayerguildowner(String name) {
        for(GuildObject guild : guilds){
            if(guild.getOwner().equals(name)){
                return guild;
            }
        }
        return null;
    }

    public static void removeguild(String guildname) {
        Iterator<GuildObject> iterator = guilds.iterator();
        while (iterator.hasNext()) {
            GuildObject k = iterator.next();
            if (k.getName().equals(guildname)) {
                iterator.remove();  // Itt használjuk az iterátor remove metódusát
            }
        }
    }

    public static void removefullguildbyguild(GuildObject guild) {
        Iterator<GuildObject> iterator = guilds.iterator();
        while (iterator.hasNext()) {
            GuildObject k = iterator.next();
            if (k.getName().equals(guild.getName())) {
                iterator.remove();  // Itt használjuk az iterátor remove metódusát
            }
        }
    }

    public static void removetempguildbyname(String name, String guildname) {
        String true_name = "";
        for(var I:tempguilds.entrySet()){
            if(name.equalsIgnoreCase(I.getKey())){
                true_name = I.getKey();
            }
        }
        Iterator<GuildObject> iterator = tempguilds.get(true_name).iterator();
        int torlo = 0;
        while (iterator.hasNext()) {
            GuildObject k = iterator.next();  // Csak egyszer hívjuk meg a next() metódust
            System.out.println("remov try " + k.getName());
            System.out.println("torlo " + torlo);
            if (k.getName().equalsIgnoreCase(guildname)) {
                System.out.println("remove temp ********");
                iterator.remove();
            } else {
                torlo++;
            }
        }
        if(torlo == 0){
            System.out.println("remove temp ********");
            tempguilds.remove(name);
        }
    }

    public static boolean isplayerintemguild(String name, String guildname) {
        return tempguilds.get(name).stream().anyMatch(k -> k.getName().equals(guildname));
    }

    public static void removeplayerformhuildmember(String name) {
        for (GuildObject guild : guilds) {
            Iterator<String> iterator = guild.getMembers().iterator();
            while (iterator.hasNext()) {
                String k = iterator.next();
                if (k.equals(name)) {
                    iterator.remove();
                }
            }
        }
    }

    public static void removeplayerfromguildcoowner(String name) {
        for (GuildObject guild : guilds) {
            Iterator<String> iterator = guild.getCo_owners().iterator();
            while (iterator.hasNext()) {
                String k = iterator.next();
                if (k.equals(name)) {
                    iterator.remove();
                }
            }
        }
    }

    public static boolean isplayercoowner(String name) {
        for (GuildObject guild : guilds) {
            if (guild.getCo_owners().stream().anyMatch(k -> k.equals(name))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isguildtemvalid(String name, String guildname) {
        for (Map.Entry<String, List<GuildObject>> entry : tempguilds.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                for (GuildObject guild : entry.getValue()) {
                    if (guild.getName().equalsIgnoreCase(guildname)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isplayeringuild(String name) {
        return guilds.stream().anyMatch(k -> k.getMembers().stream().anyMatch(p -> p.equals(name)));
    }

    public static boolean isplayerguildowner(String name) {
        return guilds.stream().anyMatch(k -> k.getOwner().equalsIgnoreCase(name));
    }

    public static boolean iscoowner(String name) {
        for (GuildObject guild : guilds) {

                for(String p : guild.getCo_owners()){
                    if(p.equalsIgnoreCase(name)){
                        return true;
                    }
                }

        }
        return false;
    }


    public static void sendMessageToServer(String serverName, String message) {
        Optional<RegisteredServer> serverOptional = Guild.getServer().getServer(serverName);
        if (serverOptional.isPresent()) {
            RegisteredServer registeredServer = serverOptional.get();
            registeredServer.sendPluginMessage(
                    MinecraftChannelIdentifier.from("guildpvp:operation"),
                    message.getBytes(StandardCharsets.UTF_8)
            );
            System.out.println("Message sent to server: " + serverName);
        } else {
            System.out.println("Server not found: " + serverName);
        }
    }
    //update:sb
    public static void sendMessageToUpdateBoard(String message){
        System.out.println("message updateboard: ******************" + message);
        //    System.out.println("bent sb: " + serverName);
        Optional<Player> playerOptional = Guild.getServer().getPlayer(message);  // Játékos lekérése

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();  // Játékos objektum
            Optional<RegisteredServer> serverOptional = player.getCurrentServer().map(serverConnection -> serverConnection.getServer());

            if (serverOptional.isPresent()) {
                RegisteredServer server = serverOptional.get();  // A szerver, amin a játékos van
                // Itt dolgozhatsz tovább a server objektummal
//                System.out.println("Player is on server: " + server.getServerInfo().getName());
                server.sendPluginMessage(
                        MinecraftChannelIdentifier.from("update:sb"),
                        message.getBytes(StandardCharsets.UTF_8)
                );
            } else {
                System.out.println("Player is not connected to any server.");
            }
        } else {
            System.out.println("Player not found.");
        }

        //   System.out.println("Server name: " + serverName);

    }

    public static Component standerdtext(){
        return Component.text("[").color(NamedTextColor.DARK_GRAY)
                .append(Component.text("Charity").color(NamedTextColor.WHITE)
                .append(Component.text("Craft").color(NamedTextColor.YELLOW)
                .append(Component.text("] ").color(NamedTextColor.DARK_GRAY)
                .append(Component.text("» ").color(NamedTextColor.GRAY)))));
    }


}