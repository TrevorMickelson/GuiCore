package com.codepunisher.guicore.config;

import com.codepunisher.guicore.GuiCorePlugin;

public class GuiConfig extends ConfigFile {
    protected static final String SECTION = "Items";

    protected GuiConfig(String name) {
        super(GuiCorePlugin.getInstance(), name);
    }

    protected String getClickActionPathAtTarget(String target) {
        return getPathAtTarget(target) + "ClickActions.";
    }

    protected String getItemStackPathAtTarget(String target) {
        return getPathAtTarget(target) + "ItemStack.";
    }

    protected String getPathAtTarget(String target) {
        return SECTION + "." + target + ".";
    }
}
