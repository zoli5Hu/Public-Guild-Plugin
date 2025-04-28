package chanelingtest.guild.SubCommands;

import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.SubCommands.Subs.*;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

public class GuildCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (args.length == 0) {
            source.sendMessage(Component.text("                                                      ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH));
            source.sendMessage(Component.text("Guild help").color(NamedTextColor.GOLD));
            source.sendMessage(Component.text(""));
            source.sendMessage(helptext("/guild create <név>", "Guild létrehozása"));
            source.sendMessage(helptext("/guild accept <játékos>", "Guild meghívás elfogadása"));
            source.sendMessage(helptext("/guild leave", "Kilépés a guild-ből"));
            source.sendMessage(helptext("/guild chat <üzenet>", "Guild chat"));
            source.sendMessage(helptext("/guild info", "Guild információ"));
            source.sendMessage(Component.text(""));
            source.sendMessage(Component.text("Coleader és leader parancsok:").color(NamedTextColor.GOLD));
            source.sendMessage(Component.text(""));
            source.sendMessage(helptext("/guild invite <játékos>", "Játékos meghívása a guild-be"));
            source.sendMessage(helptext("/guild description <leírás>", "Guild leírás módosítása"));
            source.sendMessage(helptext("/guild kick <játékos>", "Játékos kirúgása a guild-ből"));
            source.sendMessage(helptext("/guild disband", "Guild feloszlatása"));
            source.sendMessage(helptext("/guild demote <játékos>", "Játékos lefokozása"));
            source.sendMessage(helptext("/guild promote <játékos>", "Játékos előléptetése"));
            source.sendMessage(helptext("/guild admininfo", "Admin információ"));
            source.sendMessage(helptext("/guild adminreload", "Admin újratöltése"));
            source.sendMessage(Component.text("                                                      ").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.STRIKETHROUGH));

            return;
        }

        String subcommand = args[0].toLowerCase();
        Player p = (Player) source;
        switch (subcommand) {
            case "create":
                new Gcreate(args, source);
                break;
            case "accept":
                new Gaccept(args, source);
                break;
            case "info":
                Storage.guildinfo(p);
                break;
            case "invite":
                new GInvite(args, source);
                break;
            case "description":
                new GDescription(args, source);
                break;
            case "admininfo":
                new AdminInfo(args, source);
                break;
            case "chat":
                new Gchat(args, source);
                break;
            case "leave":
                new GLeave(args, source);
                break;
            case "kick":
                new GKick(args, source);
                break;
            case "disband":
                new GDisband(args, source);
                break;
            case "demote":
                new GDemote(args, source);
                break;
            case "promote":
                new GPromote(args, source);
                break;
            case "adminreload":
                new AdminReloadGXP(args, source);
                break;
        }


    }

    @Override
    public boolean hasPermission(SimpleCommand.Invocation invocation) {
        // Itt ellenőrizheted a parancs használatának jogosultságát
        return true;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        // Itt kezelheted a tab-kompletálást
        return List.of("create", "accept", "info",
                "invite", "description", "admininfo", "chat", "leave",
                "kick", "disband", "demote", "promote", "adminreload");
    }

    public Component helptext(String parancs, String leiras) {
        Component text = Component.text(parancs).color(NamedTextColor.WHITE)
                        .append(Component.text(" » ").color(NamedTextColor.GRAY))
                        .append(Component.text(leiras).color(NamedTextColor.YELLOW));
        return text;
    }
}
