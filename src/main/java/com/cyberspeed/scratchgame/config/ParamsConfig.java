package com.cyberspeed.scratchgame.config;

import com.cyberspeed.scratchgame.model.GameConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class ParamsConfig {
    private static final String CONFIG_PATH_KEY = "--config";
    private static final String BETTING_AMOUNT_KEY = "--betting-amount";
    private final GameConfig config;
    private final BigDecimal bettingAmount;

    public ParamsConfig(Map<String, String> params) throws IOException {
        if (!params.containsKey(CONFIG_PATH_KEY) || !params.containsKey(BETTING_AMOUNT_KEY)) {
            throw new IllegalArgumentException("Please use the following arguments: --config and --betting-amount ");
        }

        this.config = ParsingUtils.parseConfig(params.get(CONFIG_PATH_KEY));
        this.bettingAmount = new BigDecimal(params.get(BETTING_AMOUNT_KEY));
    }

    public GameConfig getConfig() {
        return config;
    }

    public BigDecimal getBettingAmount() {
        return bettingAmount;
    }
}
