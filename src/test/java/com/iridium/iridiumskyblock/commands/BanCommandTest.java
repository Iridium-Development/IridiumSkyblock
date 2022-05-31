package com.iridium.iridiumskyblock.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandBuilder;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.UserBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.gui.IslandBansGUI;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BanCommandTest {

    private ServerMock serverMock;

    @BeforeEach
    public void setup() {
        this.serverMock = MockBukkit.mock();
        MockBukkit.load(IridiumSkyblock.class);
    }

    @AfterEach
    public void tearDown() {
        Bukkit.getScheduler().cancelTasks(IridiumSkyblock.getInstance());
        MockBukkit.unmock();
    }

    @Test
    public void executeBanCommandNoIsland() {
        PlayerMock playerMock = new UserBuilder(serverMock).build();

        serverMock.dispatchCommand(playerMock, "is ban");

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandOpensGUI() {
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).build();

        serverMock.dispatchCommand(playerMock, "is ban");

        assertTrue(playerMock.getOpenInventory().getTopInventory().getHolder() instanceof IslandBansGUI);
    }

    @Test
    public void executeBanCommandNoPermission() {
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).build();

        serverMock.dispatchCommand(playerMock, "is ban Player");

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noPermission.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandNotAPlayer() {
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).withIslandRank(IslandRank.OWNER).build();

        serverMock.dispatchCommand(playerMock, "is ban InvalidPlayer");

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().notAPlayer.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandPlayerInYourIsland() {
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).withIslandRank(IslandRank.OWNER).build();

        serverMock.dispatchCommand(playerMock, "is ban " + playerMock.getName());

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().inYourTeam
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", playerMock.getName())
        ));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandCannotBanAdmin() {
        PlayerMock target = new UserBuilder(serverMock).setBypassing().build();
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).withIslandRank(IslandRank.OWNER).build();

        serverMock.dispatchCommand(playerMock, "is ban " + target.getName());

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotBanned
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
        ));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandAlreadyBanned() {
        PlayerMock target = new UserBuilder(serverMock).build();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(target);
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).withIslandRank(IslandRank.OWNER).build();
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().addEntry(new IslandBan(island, targetUser, targetUser));

        serverMock.dispatchCommand(playerMock, "is ban " + target.getName());

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyBanned
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
        ));
        playerMock.assertNoMoreSaid();
    }

    @Test
    public void executeBanCommandSuccessfully() {
        PlayerMock target = new UserBuilder(serverMock).build();
        User targetUser = IridiumSkyblock.getInstance().getUserManager().getUser(target);
        Island island = new IslandBuilder().build();
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(island).withIslandRank(IslandRank.OWNER).build();

        serverMock.dispatchCommand(playerMock, "is ban " + target.getName());

        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().playerBanned
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%player%", target.getName())
        ));
        playerMock.assertNoMoreSaid();
        target.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
        ));
        assertTrue(IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island, targetUser));
    }
}