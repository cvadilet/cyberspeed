package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Input(
        @JsonProperty("bet_amount")
        BigDecimal betAmount) {
}
