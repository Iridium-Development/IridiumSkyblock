package com.iridium.iridiumskyblock.multiversion;

import com.iridium.iridiumskyblock.nms.NMS;
import com.iridium.iridiumskyblock.nms.NMS_V1_8_R2;
import com.iridium.iridiumskyblock.nms.NMS_V1_8_R3;
import com.iridium.iridiumskyblock.nms.NMS_V1_9_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_9_R2;
import com.iridium.iridiumskyblock.nms.NMS_V1_10_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_11_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_12_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_13_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_13_R2;
import com.iridium.iridiumskyblock.nms.NMS_V1_14_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_15_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_16_R1;
import com.iridium.iridiumskyblock.nms.NMS_V1_16_R2;
import com.iridium.iridiumskyblock.nms.NMS_V1_16_R3;
import java.util.function.Supplier;

public enum MinecraftVersion {

    V1_8_R2(() -> new NMS_V1_8_R2(), MultiVersion_V1_8_R2::new),
    V1_8_R3(() -> new NMS_V1_8_R3(), MultiVersion_V1_8_R3::new),
    V1_9_R1(() -> new NMS_V1_9_R1(), MultiVersion_V1_9_R1::new),
    V1_9_R2(() -> new NMS_V1_9_R2(), MultiVersion_V1_9_R2::new),
    V1_10_R1(() -> new NMS_V1_10_R1(), MultiVersion_V1_10_R1::new),
    V1_11_R1(() -> new NMS_V1_11_R1(), MultiVersion_V1_11_R1::new),
    V1_12_R1(() -> new NMS_V1_12_R1(), MultiVersion_V1_12_R1::new),
    V1_13_R1(() -> new NMS_V1_13_R1(), MultiVersion_V1_13_R1::new),
    V1_13_R2(() -> new NMS_V1_13_R2(), MultiVersion_V1_13_R2::new),
    V1_14_R1(() -> new NMS_V1_14_R1(), MultiVersion_V1_14_R1::new),
    V1_15_R1(() -> new NMS_V1_15_R1(), MultiVersion_V1_15_R1::new),
    V1_16_R1(() -> new NMS_V1_16_R1(), MultiVersion_V1_16_R1::new),
    V1_16_R2(() -> new NMS_V1_16_R2(), MultiVersion_V1_16_R2::new),
    V1_16_R3(() -> new NMS_V1_16_R3(), MultiVersion_V1_16_R3::new);

    private final Supplier<NMS> nmsSupplier;
    private final Supplier<MultiVersion> multiVersionSupplier;

    MinecraftVersion(Supplier<NMS> nmsSupplier, Supplier<MultiVersion> multiVersionSupplier) {
        this.nmsSupplier = nmsSupplier;
        this.multiVersionSupplier = multiVersionSupplier;
    }

    public NMS getNms() {
        return nmsSupplier.get();
    }

    public MultiVersion getMultiVersion() {
        return multiVersionSupplier.get();
    }

    public static MinecraftVersion byName(String version) {
        for (MinecraftVersion minecraftVersion : values()) {
            if (minecraftVersion.name().equalsIgnoreCase(version)) {
                return minecraftVersion;
            }
        }

        return null;
    }

}
