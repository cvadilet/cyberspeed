package com.cyberspeed.scratchgame.config;

import com.cyberspeed.scratchgame.model.GameConfig;
import com.cyberspeed.scratchgame.model.Input;
import com.cyberspeed.scratchgame.model.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ParsingUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static GameConfig parseConfig(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), GameConfig.class);
    }

    public static Input parseInput(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), Input.class);
    }

    public static Output parseOutput(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), Output.class);
    }

    public static void writeResult(Output result) throws IOException {
        mapper.writeValue(new File("output.json"), result);
    }

    public static String resultAsString(Output result) throws JsonProcessingException {
        return mapper.writeValueAsString(result);
    }
}
