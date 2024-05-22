package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GameConfig(
        @JsonProperty("columns")
        Integer columns,
        @JsonProperty("rows")
        Integer rows,
        @JsonProperty("symbols")
        Map<String, Symbol> symbols,
        @JsonProperty("probabilities")
        Probabilities probabilities,
        @JsonProperty("win_combinations")
        Map<String, WinCombination> winCombinations) {
}
