package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record BonusSymbolsProbability(
        @JsonProperty("symbols")
        Map<String, Integer> symbols) {
}
