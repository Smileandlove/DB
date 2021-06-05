package BNFCommands;

import DBException.QueryErrorException;
import Engine.Engine;
import Parser.Parser;

public interface Commands {
    void interpret(Parser parser, Engine engine) throws QueryErrorException;
}
