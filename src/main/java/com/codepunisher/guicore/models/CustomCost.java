package com.codepunisher.guicore.models;

import com.mcaim.core.util.Pair;

public final class CustomCost {
    private final double vaultCost;
    private Pair<String, Integer> keyCost;

    public CustomCost(double vaultCost) {
        this.vaultCost = vaultCost;
    }

    public CustomCost(double vaultCost, String key, int amount) {
        this(vaultCost);
        keyCost = new Pair<>(key, amount);
    }

    public double getVaultCost() { return vaultCost; }
    public Pair<String, Integer> getKeyCost() { return keyCost; }
}
