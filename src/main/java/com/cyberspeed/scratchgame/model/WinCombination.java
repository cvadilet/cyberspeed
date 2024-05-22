package com.cyberspeed.scratchgame.model;

import com.cyberspeed.scratchgame.enums.WinCombinationGroup;
import com.cyberspeed.scratchgame.enums.WinCombinationType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record WinCombination(
        @JsonProperty("reward_multiplier")
        BigDecimal rewardMultiplier,
        @JsonProperty("when")
        WinCombinationType when,
        @JsonProperty("count")
        Integer count,
        @JsonProperty("group")
        WinCombinationGroup group,
        @JsonProperty("covered_areas")
        List<List<String>> coveredAreas) {
}
