package com.cyberspeed.scratchgame.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WinCombinationType {
    @JsonProperty("same_symbols")
    SAME_SYMBOLS,
    @JsonProperty("linear_symbols")
    LINEAR_SYMBOLS
}
