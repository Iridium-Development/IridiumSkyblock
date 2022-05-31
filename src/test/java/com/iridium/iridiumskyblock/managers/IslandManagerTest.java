package com.iridium.iridiumskyblock.managers;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandBuilder;
import com.iridium.iridiumskyblock.UserBuilder;
import com.iridium.iridiumskyblock.database.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandManagerTest {

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
    public void getIslandInvite() {
        Island island = new IslandBuilder().build();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(new UserBuilder(serverMock).withIsland(island).build());

        assertFalse(IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island, user).isPresent());
        IslandInvite islandInvite = new IslandInvite(island, user, user);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandInviteTableManager().addEntry(islandInvite);
        assertEquals(islandInvite, IridiumSkyblock.getInstance().getIslandManager().getIslandInvite(island, user).orElse(null));
    }

    @Test
    public void teleportYourIslandHome() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).withIsland(island).build();
        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHome.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @Test
    public void teleportOtherIslandHomePublic() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).build();
        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHomeOther.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", island.getOwner().getName())));
    }

    @Test
    public void teleportOtherIslandHomeTrusted() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).build();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        island.setVisitable(false);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandTrustedTableManager().addEntry(new IslandTrusted(island, user, user));

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHomeOther.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", island.getOwner().getName())));
    }

    @Test
    public void teleportOtherIslandHomeBypassing() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).build();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);

        island.setVisitable(false);
        user.setBypassing(true);

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingHomeOther.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix).replace("%owner%", island.getOwner().getName())));
    }

    @Test
    public void teleportIslandHomePrivate() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).build();

        island.setVisitable(false);

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertNotEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
    }

    @Test
    public void teleportIslandHomeBanned() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).build();
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        IridiumSkyblock.getInstance().getDatabaseManager().getIslandBanTableManager().addEntry(new IslandBan(island, user, user));

        IridiumSkyblock.getInstance().getIslandManager().teleportHome(player, island, 0);
        assertNotEquals(island.getHome(), player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                .replace("%owner%", island.getOwner().getName())
                .replace("%name%", island.getName())
        ));
    }

    @Test
    public void teleportIslandWarp() {
        Island island = new IslandBuilder().build();
        PlayerMock player = new UserBuilder(serverMock).withIsland(island).build();
        Location location = new Location(player.getWorld(), 100, 100, 100);
        IslandWarp islandWarp = new IslandWarp(island, location, "test");

        IridiumSkyblock.getInstance().getIslandManager().teleportWarp(player, islandWarp, 0);

        assertEquals(location, player.getLocation());
        player.assertSaid(StringUtils.color(IridiumSkyblock.getInstance().getMessages().teleportingWarp
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix))
                .replace("%name%", islandWarp.getName())
        );
    }

    @Test
    public void getIslandById() {
        assertEquals(new IslandBuilder(1).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(1).orElse(null));
        assertEquals(new IslandBuilder(2).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(2).orElse(null));
        assertEquals(new IslandBuilder(3).build(), IridiumSkyblock.getInstance().getIslandManager().getIslandById(3).orElse(null));
    }

    @Test
    public void getIslandByName() {
        Island island = new IslandBuilder("Island Name").build();
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("Island Name").orElse(null));
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("ISLAND NAME").orElse(null));
        assertEquals(island, IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island name").orElse(null));

        assertNull(IridiumSkyblock.getInstance().getIslandManager().getIslandByName("fake_island").orElse(null));
        assertNull(IridiumSkyblock.getInstance().getIslandManager().getIslandByName("island1").orElse(null));
    }

    @Test
    public void getIslandMembers() {
        Island island = new IslandBuilder().build();

        User user1 = IridiumSkyblock.getInstance().getUserManager().getUser(new UserBuilder(serverMock).withIsland(island).build());
        User user2 = IridiumSkyblock.getInstance().getUserManager().getUser(new UserBuilder(serverMock).withIsland(island).build());
        User user3 = IridiumSkyblock.getInstance().getUserManager().getUser(new UserBuilder(serverMock).withIsland(island).build());

        List<User> users = IridiumSkyblock.getInstance().getIslandManager().getIslandMembers(island)
                .stream()
                .sorted(Comparator.comparing(User::getName))
                .toList();

        assertEquals(List.of(user1, user2, user3), users);
    }
}