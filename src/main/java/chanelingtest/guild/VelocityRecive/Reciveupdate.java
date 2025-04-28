package chanelingtest.guild.VelocityRecive;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Database.XpConnector;
import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;

import java.sql.SQLException;

public class Reciveupdate {
    @Subscribe
    public void onPluginMessagewin(PluginMessageEvent event) throws SQLException, InterruptedException {
        System.out.println("plugin message" + event.getIdentifier().getId());
        if (!event.getIdentifier().getId().equals("xp:update"))
            return;

//        System.out.println("plugin message bent" + event.getIdentifier().getId());

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String message = in.readUTF();

        GDB db = new GDB(Guild.getLogger());

        GuildObject pG = Storage.whatisplayerguild(message);

        XpConnector xp = new XpConnector(Guild.getLogger());
        if(pG == null){
           System.out.println("nincs guildje");
        }else {
            int playxp = xp.allxpbyguild(pG.getName());
            System.out.println("xp s" + playxp + " ---" + pG.getName());
            db.updateGXpsetvalu(pG.getName(), playxp);

        }

    }
}
