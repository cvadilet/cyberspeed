package com.cyberspeed.scratchgame.model;

import com.cyberspeed.scratchgame.enums.SymbolImpact;
import com.cyberspeed.scratchgame.enums.SymbolType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Symbol(
        @JsonProperty("reward_multiplier")
        BigDecimal rewardMultiplier,
        @JsonProperty("extra")
        BigDecimal extra,
        @JsonProperty("type")
        SymbolType type,
        @JsonProperty("impact")
        SymbolImpact impact) {
}
