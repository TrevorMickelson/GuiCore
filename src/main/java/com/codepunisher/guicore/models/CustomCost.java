package com.codepunisher.guicore.models;

import com.codepunisher.guicore.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class CustomCost {
    private final double vaultCost;
    private List<Pair<String, Integer>> keyCost = new ArrayList<>();

    public CustomCost(double vaultCost) {
        this.vaultCost = vaultCost;
    }

    public CustomCost(double vaultCost, List<Pair<String, Integer>> keyCost) {
        this(vaultCost);
        this.keyCost = keyCost;
    }
}
