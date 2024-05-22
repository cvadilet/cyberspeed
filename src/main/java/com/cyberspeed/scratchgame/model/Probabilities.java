package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Probabilities(
        @JsonProperty("standard_symbols")
        List<StandardSymbolsProbability> standardSymbols,
        @JsonProperty("bonus_symbols")
        BonusSymbolsProbability bonusSymbols) {
}
