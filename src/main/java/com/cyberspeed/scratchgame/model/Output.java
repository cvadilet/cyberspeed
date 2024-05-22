package com.cyberspeed.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record Output(
        @JsonProperty("matrix")
        String[][] matrix,
        @JsonProperty("reward")
        BigDecimal reward,
        @JsonProperty("applied_winning_combinations")
        Map<String, List<String>> appliedWinningCombinations,
        @JsonProperty("applied_bonus_symbol")
        String appliedBonusSymbol) {
}
