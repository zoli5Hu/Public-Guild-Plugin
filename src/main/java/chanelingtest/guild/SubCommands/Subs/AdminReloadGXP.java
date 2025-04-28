package chanelingtest.guild.SubCommands.Subs;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Database.XpConnector;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

public class AdminReloadGXP {
    String[] args;
    CommandSource source;

    public AdminReloadGXP(String[] args, CommandSource source) {
        this.args = args;
        this.source = source;
        GDB db = new GDB(Guild.getLogger());
        Player p = (Player) source;
        GuildObject pG = Storage.whatisplayerguild(p.getUsername());

        XpConnector xp = new XpConnector(Guild.getLogger());
        int playxp = xp.allxpbyguild(pG.getName());
//        System.out.println("xp s" + playxp + " ---" + pG.getName());
        db.updateGXpsetvalu(pG.getName(), playxp);
    }
}
