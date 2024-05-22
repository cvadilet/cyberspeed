package com.cyberspeed.scratchgame.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SymbolImpact {
    @JsonProperty("multiply_reward")
    MULTIPLY_REWARD,
    @JsonProperty("extra_bonus")
    EXTRA_BONUS,
    @JsonProperty("miss")
    MISS
}
