package com.cyberspeed.scratchgame.logic;

import com.cyberspeed.scratchgame.enums.WinCombinationType;
import com.cyberspeed.scratchgame.model.GameConfig;
import com.cyberspeed.scratchgame.model.Output;
import com.cyberspeed.scratchgame.model.WinCombination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard {

    private final GameConfig config;
    private final String[][] matrix;
    private Map<String, Integer> symbolCountMap = new HashMap<>();
    private String bonusSymbol;
    private BigDecimal totalReward = BigDecimal.ZERO;

    public GameBoard(GameConfig config) {
        this.config = config;
        matrix = new String[config.rows()][config.columns()];
        Random rgen = new Random();
        int totalBonusProb = config.probabilities().bonusSymbols().symbols().values().stream().mapToInt(Integer::intValue).sum();

        config.probabilities().standardSymbols().forEach(x -> {
            int totalStandardProb = x.symbols().values().stream().mapToInt(Integer::intValue).sum();
            int totalSumProb = totalBonusProb + totalStandardProb;

            String[] symbolSequence = new String[totalSumProb];
            int index = 0;
            for (Map.Entry<String, Integer> standardEntry : x.symbols().entrySet()) {
                for (int i = 0; i < standardEntry.getValue(); i++) {
                    symbolSequence[index++] = standardEntry.getKey();
                }
            }

            if (bonusSymbol == null) {
                for (Map.Entry<String, Integer> bonusEntry : config.probabilities().bonusSymbols().symbols().entrySet()) {
                    for (int i = 0; i < bonusEntry.getValue(); i++) {
                        symbolSequence[index++] = bonusEntry.getKey();
                    }
                }
                index = rgen.nextInt(totalSumProb);
            } else {
                index = rgen.nextInt(totalStandardProb);
            }

            matrix[x.row()][x.column()] = symbolSequence[index];
            if (index >= totalStandardProb) {
                bonusSymbol = symbolSequence[index];
            } else {
                symbolCountMap.put(symbolSequence[index], symbolCountMap.getOrDefault(symbolSequence[index], 0) + 1);
            }
        });
    }

    public GameBoard(GameConfig config, String[][] matrix, Map<String, Integer> symbolCountMap, String bonusSymbol) {
        this.config = config;
        this.matrix = matrix;
        this.symbolCountMap = symbolCountMap;
        this.bonusSymbol = bonusSymbol;
    }

    public Output play(BigDecimal bettingAmount) {
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();

        for (Map.Entry<String, Integer> symbolEntry : symbolCountMap.entrySet()) {
            BigDecimal reward = BigDecimal.ZERO;

            Optional<Map.Entry<String, WinCombination>> winCombination =
                    config.winCombinations().entrySet().stream()
                            .filter(wc -> wc.getValue().when().equals(WinCombinationType.SAME_SYMBOLS))
                            .filter(wc -> wc.getValue().count().equals(symbolEntry.getValue()))
                            .findFirst();
            if (winCombination.isPresent()) {
                List<String> combinations = new ArrayList<>();
                combinations.add(winCombination.get().getKey());
                appliedWinningCombinations.put(symbolEntry.getKey(), combinations);

                reward = reward.add(bettingAmount
                        .multiply(config.symbols().get(symbolEntry.getKey()).rewardMultiplier())
                        .multiply(winCombination.get().getValue().rewardMultiplier()));
            }

            for (Map.Entry<String, WinCombination> wcEntry :
                    config.winCombinations().entrySet().stream()
                            .filter(wc -> wc.getValue().when().equals(WinCombinationType.LINEAR_SYMBOLS))
                            .collect(Collectors.toSet())) {

                boolean wcMatch;
                for (List<String> coveredArea : wcEntry.getValue().coveredAreas()) {
                    wcMatch = coveredArea.stream().allMatch(cell -> {
                        String[] rc = cell.split(":");
                        return symbolEntry.getKey().equals(matrix[Integer.parseInt(rc[0])][Integer.parseInt(rc[1])]);
                    });
                    if (wcMatch) {
                        appliedWinningCombinations.get(symbolEntry.getKey()).add(wcEntry.getKey());

                        reward = reward.multiply(wcEntry.getValue().rewardMultiplier());
                        break;
                    }
                }
            }
            totalReward = totalReward.add(reward);
        }

        if (bonusSymbol != null) {
            switch (config.symbols().get(bonusSymbol).impact()) {
                case MULTIPLY_REWARD -> totalReward = totalReward.multiply(config.symbols().get(bonusSymbol).rewardMultiplier());
                case EXTRA_BONUS -> totalReward = totalReward.add(config.symbols().get(bonusSymbol).extra());
                case MISS -> {}
            }
        }

        return new Output(matrix, totalReward, appliedWinningCombinations, bonusSymbol);
    }

    public String[][] getMatrix() {
        return matrix;
    }
}
