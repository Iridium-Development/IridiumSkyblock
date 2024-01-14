package com.iridium.iridiumskyblock;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumteams.utils.PlayerUtils;

public class UserBuilder {
    private final ServerMock serverMock;
    private final PlayerMock playerMock;
    private final User user;

    public UserBuilder(ServerMock serverMock) {
        this.serverMock = serverMock;
        this.playerMock = serverMock.addPlayer();
        this.user = IridiumSkyblock.getInstance().getUserManager().getUser(playerMock);
    }

    public UserBuilder(ServerMock serverMock, String playerName) {
        this.serverMock = serverMock;
        this.playerMock = serverMock.addPlayer(playerName);
        this.user = IridiumSkyblock.getInstance().getUserManager().getUser(playerMock);
    }

    public UserBuilder withExperience(int experience) {
        PlayerUtils.setTotalExperience(playerMock, experience);
        return this;
    }

    public UserBuilder withTeamInvite(Island island) {
        IridiumSkyblock.getInstance().getTeamManager().createTeamInvite(island, user, user);
        return this;
    }

    public UserBuilder withChatType(String chatType) {
        user.setChatType(chatType);
        return this;
    }

    public UserBuilder withTrust(Island island) {
        IridiumSkyblock.getInstance().getTeamManager().createTeamTrust(island, user, user);
        return this;
    }

    public UserBuilder withTeam(Island island) {
        user.setTeam(island);
        return this;
    }

    public UserBuilder withRank(int rank) {
        user.setUserRank(rank);
        return this;
    }

    public UserBuilder withPermission(String... permissions) {
        for (String permission : permissions) {
            playerMock.addAttachment(IridiumSkyblock.getInstance(), permission, true);
        }
        return this;
    }

    public UserBuilder setBypassing() {
        user.setBypassing(true);
        return this;
    }

    public UserBuilder setOp() {
        playerMock.setOp(true);
        return this;
    }

    public PlayerMock build() {
        return playerMock;
    }

}
