package taladegaCompiler;

import customExceptions.CompilerException;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.SymbolTable;
import semanticAnalyzer.SemanticAnalyzer;
import syntaxAnalyzer.SyntaxAnalyzer;

public class Main {
    public static final int TEST_SELECTOR = 2; // 0 = 'LEXICAL_ANALYZER'; 1 = 'SYNTAX_ANALYZER'; 2 = 'SEMANTIC_ANALYZER'

    public static void main(String[] args) throws Exception {
        var fileName = "input.txt";

        if (args.length > 0)
            fileName = args[0];

        try {
            var symbolTable = new SymbolTable();
            var lexicalAnalyzer = new LexicalAnalyzer(fileName, symbolTable);
            var syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);

            if (TEST_SELECTOR == 0) {
                var token = lexicalAnalyzer.scan();
                while (token != null) {

                    System.out.println(token);

                    token = lexicalAnalyzer.scan();
                }

                symbolTable.print();
            }

            if (TEST_SELECTOR == 1 || TEST_SELECTOR == 2) {
                syntaxAnalyzer.analyze();
            }

            var semanticAnalyzer = new SemanticAnalyzer(syntaxAnalyzer.getSyntaxTree());

            if (TEST_SELECTOR == 2) {
                semanticAnalyzer.analyze();
            }

        } catch (CompilerException e) {
            System.err.println(e.getError());
        }
    }
}

