package chanelingtest.guild.CountDown;

import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CD {
    private final Player p;
    private final Player invited;
    private final int countdownSeconds;
    private ScheduledFuture<?> countdownTask;
    private int remainingSeconds;
    private boolean eventTriggered;
    private GuildObject guild;

    public CD(Player p, Player invited, int countdownSeconds, GuildObject guild) {
        this.p = p;
        this.invited = invited;
        this.countdownSeconds = countdownSeconds;
        this.remainingSeconds = countdownSeconds;
        this.eventTriggered = false;
        this.guild = guild;
    }

    public void start(ScheduledExecutorService scheduler) {
        countdownTask = scheduler.scheduleAtFixedRate(() -> {
            if (eventTriggered) {
                System.out.println("Event triggered, countdown stopped.");
                return;
            }
            for (Map.Entry<String, List<GuildObject>> gt : Storage.tempguilds.entrySet()) {
//                System.out.println("key: " + gt.getKey());
                for (GuildObject g : gt.getValue()) {
//                    System.out.println("guild name: " + g.getName());
                }
            }
            if (remainingSeconds > 0) {
                System.out.println("Remaining seconds: " + remainingSeconds);
                //System.out.println(Storage.isplayerintemguild(p.getUsername(), guild.getName()));

                if (!isintempguild(invited.getUsername())) {
                    // p.sendMessage(Component.text("A meghívott játékos csatlakozott!"));
                    System.out.println("Invited player joined the party.");
                    eventTriggered = true;
                    stop(); // Stop the countdown if the invited player joins the party3
                    return;
                }
                remainingSeconds--;
            } else {
                System.out.println("Countdown finished.");
               // removeInvitation();
                System.out.println("guild " + guild.getName());
                Storage.removetempguildbyname(invited.getUsername(), guild.getName());
                System.out.println("Invited player did not join the party.");
                invited.sendMessage(Component.text(guild.getName() + " lejárt az idő").color(NamedTextColor.RED));
                p.sendMessage(Component.text(guild.getName() + " lejárt az idő").color(NamedTextColor.RED));

                eventTriggered = true;
                stop();
            }
        }, 0, 1, TimeUnit.SECONDS);

        scheduler.schedule(this::triggerEvent, countdownSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        if (countdownTask != null && !countdownTask.isCancelled()) {
            countdownTask.cancel(false);
        }
    }

    private void triggerEvent() {
        stop();
        removeInvitation();
        if (Storage.istempguildbyname(p.getUsername())) {
            p.sendMessage(Component.text("A meghívás ideje lejárt."));
        }

    }

    private void removeInvitation() {
        Storage.removetempguildbyname(p.getUsername(), guild.getName());
    }

    private boolean isintempguild(String name) {
        return Storage.tempguilds.containsKey(name);
    }
}
