package com.cyberspeed.scratchgame;

import com.cyberspeed.scratchgame.config.ParsingUtils;
import com.cyberspeed.scratchgame.enums.SymbolImpact;
import com.cyberspeed.scratchgame.enums.SymbolType;
import com.cyberspeed.scratchgame.enums.WinCombinationGroup;
import com.cyberspeed.scratchgame.enums.WinCombinationType;
import com.cyberspeed.scratchgame.logic.GameBoard;
import com.cyberspeed.scratchgame.model.BonusSymbolsProbability;
import com.cyberspeed.scratchgame.model.GameConfig;
import com.cyberspeed.scratchgame.model.Input;
import com.cyberspeed.scratchgame.model.Output;
import com.cyberspeed.scratchgame.model.Probabilities;
import com.cyberspeed.scratchgame.model.StandardSymbolsProbability;
import com.cyberspeed.scratchgame.model.Symbol;
import com.cyberspeed.scratchgame.model.WinCombination;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScratchGameApplicationTests {

    @Test
    public void testParseConfigurationValidFile() throws IOException {
        GameConfig config = ParsingUtils.parseConfig("src/test/resources/test_config.json");
        assertNotNull(config);
        assertEquals(3, config.columns());
        assertEquals(3, config.rows());
        assertEquals(11, config.symbols().size());
        assertEquals(9, config.probabilities().standardSymbols().size());
        assertNotNull(config.probabilities().bonusSymbols());
        assertEquals(11, config.winCombinations().size());
    }

    @Test
    public void testParseConfigurationInvalidFile() {
        assertThrows(IOException.class, ()->{
            GameConfig config = ParsingUtils.parseConfig("src/test/resources/non_existent_config.json");
        });
    }

    @Test
    public void testGeneratedMatrix() {
        Map<String, Integer> stdSymbols = new HashMap<>();
        stdSymbols.put("A", 4);
        stdSymbols.put("B", 3);
        stdSymbols.put("C", 2);
        stdSymbols.put("D", 1);

        List<StandardSymbolsProbability> standardSymbolsProb = new ArrayList<>();
        standardSymbolsProb.add(new StandardSymbolsProbability(0, 0, stdSymbols));
        standardSymbolsProb.add(new StandardSymbolsProbability(0, 1, stdSymbols));
        standardSymbolsProb.add(new StandardSymbolsProbability(1, 0, stdSymbols));
        standardSymbolsProb.add(new StandardSymbolsProbability(1, 1, stdSymbols));

        Map<String, Integer> bSymbols = new HashMap<>();
        bSymbols.put("X", 10);
        bSymbols.put("Y", 5);

        BonusSymbolsProbability bonusSymbolsProb = new BonusSymbolsProbability(bSymbols);
        Probabilities probabilities = new Probabilities(standardSymbolsProb, bonusSymbolsProb);

        GameConfig config = new GameConfig(2, 2, Mockito.mock(HashMap.class), probabilities, Mockito.mock(HashMap.class));
        GameBoard gameBoard = new GameBoard(config);
        assertNotNull(gameBoard.getMatrix());
        assertEquals(2, gameBoard.getMatrix().length);
        assertEquals(2, gameBoard.getMatrix()[0].length);
        assertEquals(2, gameBoard.getMatrix()[1].length);
    }

    @Test
    public void testCalculateRewards() {
        Symbol d = new Symbol(BigDecimal.valueOf(1), null, SymbolType.STANDARD, null);
        Symbol c = new Symbol(BigDecimal.valueOf(2), null, SymbolType.STANDARD, null);
        Symbol b = new Symbol(BigDecimal.valueOf(3), null, SymbolType.STANDARD, null);
        Symbol a = new Symbol(BigDecimal.valueOf(4), null, SymbolType.STANDARD, null);
        Symbol x = new Symbol(BigDecimal.valueOf(10), null, SymbolType.BONUS, SymbolImpact.MULTIPLY_REWARD);
        Symbol y = new Symbol(BigDecimal.valueOf(5), null, SymbolType.BONUS, SymbolImpact.MULTIPLY_REWARD);

        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", a);
        symbols.put("B", b);
        symbols.put("C", c);
        symbols.put("D", d);
        symbols.put("X", x);
        symbols.put("Y", y);

        List<List<String>> hAreas = new ArrayList<>();
        List<String> firstList = new ArrayList<>();
        firstList.add("0:0");
        firstList.add("0:1");
        List<String> secondList = new ArrayList<>();
        secondList.add("1:0");
        secondList.add("1:1");
        hAreas.add(firstList);
        hAreas.add(secondList);

        List<List<String>> vAreas = new ArrayList<>();
        List<String> thirdList = new ArrayList<>();
        thirdList.add("0:0");
        thirdList.add("1:0");
        List<String> forthList = new ArrayList<>();
        forthList.add("0:1");
        forthList.add("1:1");
        vAreas.add(thirdList);
        vAreas.add(forthList);

        WinCombination wc = new WinCombination(BigDecimal.valueOf(1), WinCombinationType.SAME_SYMBOLS, 2, WinCombinationGroup.SAME_SYMBOLS, null);
        WinCombination wch = new WinCombination(BigDecimal.valueOf(2), WinCombinationType.LINEAR_SYMBOLS, null, WinCombinationGroup.HORIZONTALLY_LINEAR_SYMBOLS, hAreas);
        WinCombination wcv = new WinCombination(BigDecimal.valueOf(2), WinCombinationType.LINEAR_SYMBOLS, null, WinCombinationGroup.VERTICALLY_LINEAR_SYMBOLS, vAreas);

        Map<String, WinCombination> winCombinations = new HashMap<>();
        winCombinations.put("same_symbol_2_times", wc);
        winCombinations.put("same_symbols_horizontally", wch);
        winCombinations.put("same_symbols_vertically", wcv);

        GameConfig config = new GameConfig(2, 2, symbols, Mockito.mock(Probabilities.class), winCombinations);

        String[][] matrix = new String[2][2];
        matrix[0][0] = "A";
        matrix[0][1] = "X";
        matrix[1][0] = "A";
        matrix[1][1] = "D";

        Map<String, Integer> symbolCountMap = new HashMap<>();
        symbolCountMap.put("A", 2);
        symbolCountMap.put("X", 1);
        symbolCountMap.put("D", 1);

        GameBoard gameBoard = new GameBoard(config, matrix, symbolCountMap, "X");
        Output out = gameBoard.play(BigDecimal.TEN);
        //10*4*1*2*10 = 800

        assertEquals(BigDecimal.valueOf(800), out.reward());
    }

    @Test
    public void testParseOutputFile() throws IOException {
        GameConfig config = ParsingUtils.parseConfig("src/test/resources/test_config.json");
        Input input = ParsingUtils.parseInput("src/test/resources/test_bet_amount.json");
        GameBoard gameBoard = new GameBoard(config);
        Output result = gameBoard.play(input.betAmount());
        ParsingUtils.writeResult(result);

        assertNotNull(input);
        assertEquals(BigDecimal.valueOf(100), input.betAmount());
        assertNotNull(result);

        Output output = ParsingUtils.parseOutput("output.json");

        assertNotNull(output);
        assertEquals(result.reward(), output.reward());
    }
}
