package com.iridium.iridiumskyblock.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandBuilder;
import com.iridium.iridiumskyblock.TestingHelper;
import com.iridium.iridiumskyblock.UserBuilder;
import com.iridium.iridiumskyblock.gui.IslandCreateGUI;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateCommandTest {

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
    public void executeNoArgs() {
        PlayerMock playerMock = new UserBuilder(serverMock).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create");
        assertTrue(playerMock.getOpenInventory().getTopInventory().getHolder() instanceof IslandCreateGUI);
        IslandCreateGUI islandCreateGUI = (IslandCreateGUI) playerMock.getOpenInventory().getTopInventory().getHolder();
        assertNull(islandCreateGUI.getIslandName());
    }

    @Test
    public void executeWithName() {
        PlayerMock playerMock = new UserBuilder(serverMock).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create IslandName");
        assertTrue(playerMock.getOpenInventory().getTopInventory().getHolder() instanceof IslandCreateGUI);
        IslandCreateGUI islandCreateGUI = (IslandCreateGUI) playerMock.getOpenInventory().getTopInventory().getHolder();
        assertEquals("IslandName", islandCreateGUI.getIslandName());
    }

    @Test
    public void executeWithSchematic() {
        PlayerMock playerMock = new UserBuilder(serverMock).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create IslandName " + TestingHelper.getSchematicKey());
        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().creatingIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @Test
    public void executeWithInvalidSchematic() {
        PlayerMock playerMock = new UserBuilder(serverMock).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create IslandName invalidSchematic");
        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandSchematicNotFound.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @Test
    public void executeWithExistingIsland() {
        PlayerMock playerMock = new UserBuilder(serverMock).withIsland(new IslandBuilder().build()).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create IslandName " + TestingHelper.getSchematicKey());
        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().alreadyHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @Test
    public void executeWithExistingIslandName() {
        new IslandBuilder("IslandName").build();
        PlayerMock playerMock = new UserBuilder(serverMock).buildPlayer();
        serverMock.dispatchCommand(playerMock, "is create IslandName " + TestingHelper.getSchematicKey());
        playerMock.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandWithNameAlreadyExists.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

}