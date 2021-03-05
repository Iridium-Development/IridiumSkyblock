package com.iridium.iridiumskyblock.database;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "stacked_blocks")
public final class StackedBlock {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private int id;

    @DatabaseField(columnName = "island_id")
    private int island;

    @DatabaseField(columnName = "material", canBeNull = false)
    private String material;

    @DatabaseField(columnName = "location", canBeNull = false)
    private String location;

    @DatabaseField(columnName = "amount", canBeNull = false)
    @Setter
    private int amountStacked;


    public @NotNull Optional<Island> getIsland() {
        return IridiumSkyblock.getInstance().getIslandManager().getIslandById(island);
    }

    public StackedBlock(@NotNull Island island, @NotNull String material, int amount, Location loc) {
        this.island = island.getId();
        this.material = material;
        this.amountStacked = amount;
        this.location = loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getPitch() + "," + loc.getYaw();
    }

    public @NotNull Location getLocation() {
        String[] params = location.split(",");
        World world = IridiumSkyblockAPI.getInstance().getWorld();
        return new Location(world, Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]), Float.parseFloat(params[3]), Float.parseFloat(params[4]));
    }

}
