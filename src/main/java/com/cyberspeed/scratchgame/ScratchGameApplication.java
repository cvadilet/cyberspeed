package com.cyberspeed.scratchgame;

import com.cyberspeed.scratchgame.config.ParamsConfig;
import com.cyberspeed.scratchgame.config.ParsingUtils;
import com.cyberspeed.scratchgame.logic.GameBoard;
import com.cyberspeed.scratchgame.model.Output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ScratchGameApplication {

    public static void main(String[] args) throws IOException {
        Map<String, String> argsMap = handleCLI(args);
        ParamsConfig paramsConfig = new ParamsConfig(argsMap);
        GameBoard gameBoard = new GameBoard(paramsConfig.getConfig());
        Output result = gameBoard.play(paramsConfig.getBettingAmount());
        ParsingUtils.writeResult(result);

        System.out.println(ParsingUtils.resultAsString(result));
    }

    private static Map<String, String> handleCLI(String[] args) {
        Map<String, String> params = new HashMap<>();
        String paramKey = null;
        String regex = "^--\\w+";
        Pattern pattern = Pattern.compile(regex);

        for (String arg: args) {
            if (pattern.matcher(arg).find()) {
                paramKey = arg;
                params.put(paramKey, null);
            } else {
                if (paramKey != null) {
                    params.putIfAbsent(paramKey, arg);
                }
            }
        }

        return params;
    }
}
