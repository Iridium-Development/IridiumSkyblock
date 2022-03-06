package com.iridium.iridiumskyblock.configs;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumcore.Background;
import com.iridium.iridiumcore.Item;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnore;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.configs.inventories.*;

import java.util.Arrays;
import java.util.Collections;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Inventories {

    @JsonIgnore
    private final Background background1 = new Background(ImmutableMap.<Integer, Item>builder().build());
    @JsonIgnore
    private final Background background2 = new Background(ImmutableMap.<Integer, Item>builder()
            .put(9, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(10, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(11, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(12, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(13, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(14, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(15, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(16, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .put(17, new Item(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE, 1, " ", Collections.emptyList(), null))
            .build());

    public Item backButton = new Item(XMaterial.NETHER_STAR, -5, 1, "&c&lBack", Collections.emptyList());

    public InventoryConfig islandBorder = new InventoryConfig(27, "&7Island Border", background2, ImmutableMap.<String, Item>builder()
            .put("is border blue", new Item(XMaterial.BLUE_STAINED_GLASS_PANE, 10, 1, "&b&lBlue", Collections.emptyList(), null))
            .put("is border red", new Item(XMaterial.RED_STAINED_GLASS_PANE, 12, 1, "&c&lRed", Collections.emptyList(), null))
            .put("is border green", new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 14, 1, "&a&lGreen", Collections.emptyList(), null))
            .put("is border off", new Item(XMaterial.WHITE_STAINED_GLASS_PANE, 16, 1, "&f&lOff", Collections.emptyList(), null))
            .build()
    );

    public InventoryConfig islandMenu = new InventoryConfig(45, "&7Island Menu", background1, ImmutableMap.<String, Item>builder()
            .put("is regen", new Item(XMaterial.GRASS_BLOCK, 12, 1, "&b&lIsland Regen", Collections.singletonList("&7Regenerate your island"), null))
            .put("is boosters", new Item(XMaterial.EXPERIENCE_BOTTLE, 23, 1, "&b&lIsland Boosters", Collections.singletonList("&7View your island boosters"), null))
            .put("is home", new Item(XMaterial.WHITE_BED, 13, 1, "&b&lIsland Home", Collections.singletonList("&7Teleport to your island home"), null))
            .put("is members", new Item(XMaterial.PLAYER_HEAD, 14, 1, "&b&lIsland Members", "Peaches_MLG", Collections.singletonList("&7View your island members"), null))
            .put("is warps", new Item(XMaterial.END_PORTAL_FRAME, 20, 1, "&b&lIsland Warps", Collections.singletonList("&7View your island warps"), null))
            .put("is upgrade", new Item(XMaterial.DIAMOND, 21, 1, "&b&lIsland Upgrades", Collections.singletonList("&7View your island upgrades"), null))
            .put("is missions", new Item(XMaterial.IRON_SWORD, 22, 1, "&b&lIsland Missions", Collections.singletonList("&7View your island missions"), null))
            .put("is border", new Item(XMaterial.BEACON, 24, 1, "&b&lIsland Border", Collections.singletonList("&7Change your island border"), null))
            .put("is bank", new Item(XMaterial.PLAYER_HEAD, 30, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM4MWM1MjlkNTJlMDNjZDc0YzNiZjM4YmI2YmEzZmRlMTMzN2FlOWJmNTAzMzJmYWE4ODllMGEyOGU4MDgxZiJ9fX0", 1, "&b&lIsland Bank", Collections.singletonList("&7View your island bank"), null))
            .put("is permissions", new Item(XMaterial.WRITABLE_BOOK, 31, 1, "&b&lIsland Permissions", Collections.singletonList("&7View your island permissions"), null))
            .put("is trusted", new Item(XMaterial.NAME_TAG, 32, 1, "&b&lTrusted Members", Collections.singletonList("&7View your island's trusted members"), null))
            .put("is delete", new Item(XMaterial.BARRIER, 44, 1, "&b&lDelete Island", Collections.singletonList("&7Delete your island"), null))
            .build()
    );

    public InventoryConfig missionSelectGUI = new InventoryConfig(27, "&7Island Missions", background2, ImmutableMap.<String, Item>builder()
            .put("is missions once", new Item(XMaterial.WRITABLE_BOOK, 15, 1, "&b&lQuests", Collections.emptyList(), null))
            .put("is missions daily", new Item(XMaterial.DIAMOND_SWORD, 11, 1, "&b&lDaily Missions", Collections.emptyList(), null))
            .build()
    );

    public InventoryConfig blockValueSelectGUI = new InventoryConfig(27, "Block Values", background2, ImmutableMap.<String, Item>builder()
            .put("is blockvalues block", new Item(XMaterial.EMERALD_BLOCK, 11, 1, "&b&lValuable Blocks", Collections.emptyList(), null))
            .put("is blockvalues spawner", new Item(XMaterial.SPAWNER, 15, 1, "&b&lValuable Spawners", Collections.emptyList(), null))
            .build()
    );

    public SingleItemGUI visitGUI = new SingleItemGUI(45, "&7Visit an Island", background1, new Item(XMaterial.PLAYER_HEAD, 1, "&b&l%island_name%", "%island_owner%", Arrays.asList(
            "&7Created: %island_create%",
            "&7Owner: %island_owner%"
    ), null));

    public SingleItemGUI membersGUI = new SingleItemGUI(27, "&7Island Members", background1, new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player_name%", "%player_name%", Arrays.asList(
            "&7Joined: %player_join%",
            "&7Rank: %player_rank%",
            "",
            "&b&l[!] &7Right Click to promote",
            "&b&l[!] &7Left click to demote/kick"
    ), null));
    public SingleItemGUI bansGUI = new SingleItemGUI(27, "&7Island Bans", background1, new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player_name%", "%player_name%", Arrays.asList(
            "&7Banned time: %ban_time%",
            "&7Banned by: %banned_by%",
            "",
            "&b&l[!] &7Left Click to unban"
    ), null));

    public SingleItemGUI trustedGUI = new SingleItemGUI(27, "&7Trusted Members", background1, new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player_name%",
            "%player_name%", Arrays.asList(
            "&7Date: %player_join%",
            "&7Trusted By: %trustee%",
            "",
            "&b&l [!] &7Left click to untrust"
    ), null));

    public SingleItemGUI visitorsGUI = new SingleItemGUI(27, "&7Island Visitors", background1, new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player_name%",
            "%player_name%", Arrays.asList(
            "&7Has Island: %has_island%",
            "",
            "&b&l[!] &7Left Click to expel this visitor from your island",
            "&b&l[!] &7Right Click to ban this visitor from your island"
    ), null));

    public IslandTopInventoryConfig islandTopGUI = new IslandTopInventoryConfig(27, "&7Top Islands", background1, new Item(XMaterial.PLAYER_HEAD, 1, "&b&lIsland Owner: &f%island_owner% &7(#%island_rank%)", "%island_owner%", Arrays.asList(
            "",
            "&b&l * &7Island Name: &b%island_name%",
            "&b&l * &7Island Rank: &b%island_rank%",
            "&b&l * &7Island Value: &b%island_value%",
            "&b&l * &7Netherite Blocks: &b%NETHERITE_BLOCK_AMOUNT%",
            "&b&l * &7Emerald Blocks: &b%EMERALD_BLOCK_AMOUNT%",
            "&b&l * &7Diamond Blocks: &b%DIAMOND_BLOCK_AMOUNT%",
            "&b&l * &7Gold Blocks: &b%GOLD_BLOCK_AMOUNT%",
            "&b&l * &7Iron Blocks: &b%IRON_BLOCK_AMOUNT%",
            "&b&l * &7Hopper Blocks: &b%HOPPER_AMOUNT%",
            "&b&l * &7Beacon Blocks: &b%BEACON_AMOUNT%",
            "",
            "&b&l[!] &bLeft Click to Teleport to this Island."
    ), null), new Item(XMaterial.BARRIER, 1, " ", Collections.emptyList(), null));

    public ConfirmationInventoryConfig confirmationGUI = new ConfirmationInventoryConfig(27, "&7Are you sure?", background2, new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 15, 1, "&a&lYes", Collections.emptyList()), new Item(XMaterial.RED_STAINED_GLASS_PANE, 11, 1, "&c&lNo", Collections.emptyList()));

    public NoItemGUI bankGUI = new NoItemGUI(27, "&7Island Bank", background2);

    public LogInventoryConfig logsGUI = new LogInventoryConfig(27, "&7Island Logs",
            new Item(XMaterial.PLAYER_HEAD, 10, 1, "&b&lIsland Members", "%island_owner%", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.EMERALD, 11, 1, "&b&lIsland Trusts", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.LAPIS_LAZULI, 12, 1, "&b&lIsland Invites", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.GOLD_INGOT, 13, 1, "&b&lIsland Bank", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.EXPERIENCE_BOTTLE, 14, 1, "&b&lIsland Boosters", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.BEACON, 15, 1, "&b&lIsland Upgrades", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            new Item(XMaterial.DIAMOND, 16, 1, "&b&lIsland Rewards", Arrays.asList("", "&7Page %current_page%/%max_page%", "&b&l[!] &bLeft click to view Previous Page", "&b&l[!] &bRight click to view Next Page"), null),
            "&b%user% Joined (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% kicked %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Left (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Invited %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Uninvited %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Promoted %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Demoted %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Trusted %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% UnTrusted %target% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Deposited %amount% %type% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Withdrew %amount% %type% (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Purchased %type% booster (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% Purchased %type% upgrade (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            "&b%user% redeemed %type% reward (%days% days %hours% hours %minutes% minutes %seconds% seconds ago)",
            background2);

    public NoItemGUI upgradesGUI = new NoItemGUI(27, "&7Island Upgrades", background2);

    public NoItemGUI boostersGUI = new NoItemGUI(27, "&7Island Boosters", background2);

    public SingleItemGUI warpsGUI = new SingleItemGUI(27, "&7%island_name%'s Island Warps", background2, new Item(
            XMaterial.GREEN_STAINED_GLASS_PANE, 1, "&b&l%warp_name%",
            Arrays.asList(
                    "&7%description%",
                    "",
                    "&b&l[!] &bLeft Click to Teleport",
                    "&b&l[!] &bRight Click to Delete"
            ), null));

    public BlockValuesInventoryConfig blockValue = new BlockValuesInventoryConfig(27, "&7Block Values", background1, Collections.singletonList("&bValue per block: &7%value%"));

    public SingleItemGUI islandInvitesGUI = new SingleItemGUI(27, "&7Island Invites", background1, new Item(XMaterial.PLAYER_HEAD, 0, 1, "&b&l%player_name%", "%player_name%", Arrays.asList(
            "&7Invited By: %inviter%",
            "&7Time: %time%",
            "",
            "&b&l[!] &7Click to un-invite"
    ), null));

    public NoItemGUI islandSchematicGUI = new NoItemGUI(27, "&7Select a Schematic", background2);

    public NoItemGUI dailyMissionGUI = new NoItemGUI(27, "&7Daily Island Missions", background2);

    public NoItemGUI missionsGUI = new NoItemGUI(45, "&7Island Missions", background1);

    public NoItemGUI islandPermissionsGUI = new NoItemGUI(54, "&7Island Permissions", background1);

    public NoItemGUI islandSettingsGUI = new NoItemGUI(36, "&7Island Settings", background1);

    public IslandRanksInventoryConfig islandRanksGUI = new IslandRanksInventoryConfig(27, "&7Island Permissions", background1,
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 11, 1, "&b&lOwner", Collections.singletonList("&b%members%"), null),
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 12, 1, "&b&lCo-Owner", Collections.singletonList("&b%members%"), null),
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 13, 1, "&b&lModerator", Collections.singletonList("&b%members%"), null),
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 14, 1, "&b&lMember", Collections.singletonList("&b%members%"), null),
            new Item(XMaterial.GREEN_STAINED_GLASS_PANE, 15, 1, "&b&lVisitor", Collections.emptyList(), null));

    public SingleItemGUI biomeGUI = new SingleItemGUI(45, "&7Island Biomes", background1, new Item(XMaterial.GRASS_BLOCK, 1, "&b&l%biome%", Collections.singletonList("&7Click to change your island biome!")));

    public NoItemGUI islandReward = new NoItemGUI(45, "&7Island Rewards", background1);

    public Item nextPage = new Item(XMaterial.LIME_STAINED_GLASS_PANE, 1, "&a&lNext Page", Collections.emptyList(), null);
    public Item previousPage = new Item(XMaterial.RED_STAINED_GLASS_PANE, 1, "&c&lPrevious Page", Collections.emptyList(), null);

}
