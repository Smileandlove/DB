import BNFCommands.*;
import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Interpreter {
    private final Parser parser;
    private final Engine engine;
    private final Map<String, Commands> commandsMap;
    private String input;
    private String[] tokens;
    private ArrayList<String> queryTokens;

    public Interpreter() {
        parser = new Parser();
        engine = new Engine();
        commandsMap = Map.ofEntries(Map.entry("USE",new UseCommand()),
                Map.entry("CREATE",new CreateCommand()),
                Map.entry("DROP", new DropCommand()),
                Map.entry("ALTER",new AlterCommand()),
                Map.entry("JOIN",new JoinCommand()),
                Map.entry("INSERT",new InsertCommand()),
                Map.entry("SELECT",new SelectCommand()),
                Map.entry("UPDATE",new UpdateCommand()),
                Map.entry("DELETE",new DeleteCommand()));
    }

    public String preformQuery(String input) throws QueryErrorException {
        this.input=input;
        queryTokens = new ArrayList<>();
        tokenize();
        parser.setTokens(queryTokens);
        executeQuery();
        return output(engine.getOutput());
    }

    private void tokenize(){
        // tokens split by spaces, quotes, semi-colon, and commas
        tokens = input.split("(?=[ ,;()'])|(?<=[ ,;()'])");
        trimSpaces();
        queryTokens.removeAll(Collections.singleton(""));
    }

    private void trimSpaces(){
        for(String token : tokens) {
            String newToken = token.trim();
            queryTokens.add(newToken);
        }
    }

    private void executeQuery() throws QueryErrorException {
        String stringCommand = queryTokens.get(0);
        Commands command = commandsMap.get(stringCommand);
        if(command == null) {
            throw new QueryErrorException("Invalid query");
        }
        command.interpret(parser,engine);
    }

    public String output(ArrayList<String> listOfResults) {
        StringBuilder out = new StringBuilder();
        for (String str: listOfResults) {
            out.append(str).append("\n");
        }
        return out.toString();
    }
}
