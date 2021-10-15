package com.iridium.iridiumskyblock.configs.inventories;

import com.iridium.iridiumcore.BackButton;
import com.iridium.iridiumcore.Background;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class NoItemGUI {

    /**
     * The size of the GUI.
     */
    public int size;

    /**
     * The title of the GUI.
     */
    public String title;

    /**
     * The background of the GUI.
     */
    public Background background;

    /**
     * The Back button of the GUI
     */
    public BackButton backButton;


}
