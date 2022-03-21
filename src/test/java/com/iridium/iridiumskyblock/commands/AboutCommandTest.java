package com.iridium.iridiumskyblock.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.UserBuilder;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AboutCommandTest {

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
    public void executeAboutCommand() {
        PlayerMock playerMock = new UserBuilder(serverMock).build();
        serverMock.dispatchCommand(playerMock, "is about");

        playerMock.assertSaid(StringUtils.color("&7Plugin Name: &bIridiumSkyblock"));
        playerMock.assertSaid(StringUtils.color("&7Plugin Version: &b" + IridiumSkyblock.getInstance().getDescription().getVersion()));
        playerMock.assertSaid(StringUtils.color("&7Plugin Author: &bPeaches_MLG"));
        playerMock.assertSaid(StringUtils.color("&7Plugin Contributors: &bdas_, SlashRemix, DoctaEnkoda"));
        playerMock.assertSaid(StringUtils.color("&7Plugin Donations: &bwww.patreon.com/Peaches_MLG"));
        playerMock.assertNoMoreSaid();
    }

}