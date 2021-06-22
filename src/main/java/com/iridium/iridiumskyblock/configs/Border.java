package com.iridium.iridiumskyblock.configs;

import com.iridium.iridiumcore.Color;
import com.iridium.iridiumcore.dependencies.fasterxml.annotation.JsonIgnoreProperties;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Border {

    public Color defaultColor = Color.BLUE;
    public HashMap<Color, Boolean> enabled = new HashMap<>();

}
