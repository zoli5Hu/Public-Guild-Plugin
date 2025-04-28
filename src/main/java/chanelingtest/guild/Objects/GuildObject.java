package chanelingtest.guild.Objects;

import chanelingtest.guild.Database.GDB;
import chanelingtest.guild.Guild;
import com.velocitypowered.api.proxy.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GuildObject {
    private String name;
    private String owner;
    private List<String> co_owners;
    private List<String> members;
    private int xp;
    private String guild_description;


    public GuildObject(String name, String owner, int xp) {
        this.name = name;
        this.owner = owner;
        this.xp = xp;
        this.members = new ArrayList<>();
        members.add(owner);
        this.co_owners = new ArrayList<>();

        GDB paddDB = new GDB(Guild.getLogger());
        paddDB.addplayerguild(owner,name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getCo_owners() {
        return co_owners;
    }

    public List<String> getMembers() {
        return members;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getGuild_description() {
        return guild_description;
    }

    public void setGuild_description(String guild_description) {
        this.guild_description = guild_description;
    }

    public void setCo_owners(List<String> co_owners) {
        this.co_owners = co_owners;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
