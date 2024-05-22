package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record StandardSymbolsProbability(
        @JsonProperty("column")
        Integer column,
        @JsonProperty("row")
        Integer row,
        @JsonProperty("symbols")
        Map<String, Integer> symbols) {
}
