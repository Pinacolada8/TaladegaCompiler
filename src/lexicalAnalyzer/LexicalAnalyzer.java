package lexicalAnalyzer;

import taladegaCompiler.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {

    private String fileName;
    private FileReader fileReader;
    private int currentLine = -1;
    private int currentColumn = -1; // TODO: Check if it will be implemented

    private int currentReading = 0;
    private boolean readNextChar = false;

    public LexicalAnalyzer(String fileName) throws IOException {
        this.fileName = fileName;
        var file = new File(this.fileName);
        this.fileReader = new FileReader(file);

        currentLine = 0;
        currentColumn = 0;
    }

    public Token scan() throws Exception {
        var token = new Token();
        var wordBuffer = new StringBuilder();
        int state = 0;

        var tokenLineStart = currentLine;
        var tokenColumnStart = currentColumn;

        while (currentReading != -1) {
            if (readNextChar) {
                currentReading = this.fileReader.read();
                currentColumn++;
            } else {
                readNextChar = true;
            }

            var ch = (char) currentReading;

            if (ch == '\n') {
                currentLine++;
                currentColumn = 0;
            }

            // Initial state
            if (state == 0) {
                if (Character.isWhitespace(ch))
                    continue;

                tokenLineStart = currentLine;
                tokenColumnStart = currentColumn;

                wordBuffer.append(ch);

                switch (ch) {
                    case '{': // literal
                        state = 1;
                        continue;
                    case '\'': // char_const
                        state = 2;
                        continue;
                    case '*': // mult_operator
                        token.TokenType = TokenType.OPERATOR_MUL;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '/': // div_operator
                        token.TokenType = TokenType.OPERATOR_DIV;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '+': //
                        token.TokenType = TokenType.OPERATOR_PLUS;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '-':
                        token.TokenType = TokenType.OPERATOR_MINUS;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '&': // &&
                        state = 3;
                        continue;
                    case '|': // ||
                        state = 4;
                        continue;
                    case '=': //  Assignment Or "=="
                        state = 5;
                        continue;
                    case '>':
                        state = 6;
                        continue;
                    case '<':
                        state = 7;
                        continue;
                    case '!':
                        state = 8;
                        continue;
                    case '(':
                        token.TokenType = TokenType.PUNCT_PARENTHESIS_OPEN;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ')':
                        token.TokenType = TokenType.PUNCT_PARENTHESIS_CLOSE;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ';':
                        token.TokenType = TokenType.PUNCT_SEMICOLON;
                        token.Value = wordBuffer.toString();
                        return token;
                    case ',':
                        token.TokenType = TokenType.PUNCT_COLON;
                        token.Value = wordBuffer.toString();
                        return token;
                    case '.':
                        token.TokenType = TokenType.PUNCT_DOT;
                        token.Value = wordBuffer.toString();
                        return token;
                    default:
                        if (Character.isDigit(ch)) {
                            state = 9;
                            continue;
                        }
                        if (Character.isLetter(ch)) {
                            state = 11;
                            continue;
                        }
                        // TODO: Ajustar excessao
                        throw new Exception("Invalid character on symbol(POS: " + 0 + " )");
                }
            }


            switch (state) {

                case 1: // Literal reading state
                    wordBuffer.append(ch);

                    if (ch == '}') {
                        token.TokenType = TokenType.LITERAL;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    if (tokenLineStart != currentLine)
                        // TODO: Ajustar excessao
                        throw new Exception("Invalid character on symbol(POS: " + 0 + " )");

                    continue;
                    // --------------------------------------------
                case 2: // Character Constant reading state
                    wordBuffer.append(ch);
                    if (ch == '\'') {
                        token.TokenType = TokenType.CHAR_CONST;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    continue;
                    // --------------------------------------------
                case 3: // && Operator reading state
                    wordBuffer.append(ch);
                    if (ch == '&') {
                        token.TokenType = TokenType.OPERATOR_AND;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    // TODO: Ajustar excessao
                    throw new Exception("Invalid character on symbol(POS: " + 0 + " )");

                    // --------------------------------------------
                case 4: // || Operator reading state
                    wordBuffer.append(ch);
                    if (ch == '|') {
                        token.TokenType = TokenType.OPERATOR_OR;
                        token.Value = wordBuffer.toString();
                        return token;
                    }
                    // TODO: Ajustar excessao
                    throw new Exception("Invalid character on symbol(POS: " + 0 + " )");

                    // --------------------------------------------
                case 5: // ASSIGNMENT or EQUAL state
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_EQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.ASSIGN;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 6: // GREATER THAN or GREATER OR EQUAL THAN
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_GREQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_GR;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 7: // LESS THAN or LESS OR EQUAL THAN
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_LSEQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_LS;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 8: // NOT or NOT EQUAL
                    if (ch == '=') {
                        wordBuffer.append(ch);
                        token.TokenType = TokenType.OPERATOR_NEQ;
                        token.Value = wordBuffer.toString();
                        return token;
                    }

                    readNextChar = false;
                    token.TokenType = TokenType.OPERATOR_EXCLAMATION;
                    token.Value = wordBuffer.toString();
                    return token;

                // --------------------------------------------
                case 9: //
                    wordBuffer.append(ch);

                    if (Character.isDigit(ch))
                        continue;

                    if (ch == '.') {
                        state = 10;
                        continue;
                    }

                    if (Character.isWhitespace(ch)) {
                        token.TokenType = TokenType.INTEGER_CONST;
                        token.Value = wordBuffer.toString().stripTrailing();
                        return token;
                    }

                    // TODO: Ajustar excessao
                    throw new Exception("Invalid character on symbol(POS: " + 0 + " )");

                    // --------------------------------------------
                case 10: //
                    wordBuffer.append(ch);

                    if (Character.isDigit(ch))
                        continue;

                    if (Character.isWhitespace(ch)) {
                        token.TokenType = TokenType.FLOAT_CONST;
                        token.Value = wordBuffer.toString().stripTrailing();
                        return token;
                    }

                    // TODO: Ajustar excessao
                    throw new Exception("Invalid character on symbol(POS: " + 0 + " )");

                    // --------------------------------------------
                case 11: //
                    wordBuffer.append(ch);

                    if (Character.isLetterOrDigit(ch) || ch == '_')
                        continue;

                    token.Value = wordBuffer.toString().stripTrailing();

                    switch (token.Value) {
                        case "program":
                            token.TokenType = TokenType.KEYWORD_PROGRAM;
                            break;
                        case "begin":
                            token.TokenType = TokenType.KEYWORD_BEGIN;
                            break;
                        case "end":
                            token.TokenType = TokenType.KEYWORD_END;
                            break;
                        case "is":
                            token.TokenType = TokenType.KEYWORD_IS;
                            break;
                        case "int":
                            token.TokenType = TokenType.TYPE_INT;
                            break;
                        case "float":
                            token.TokenType = TokenType.TYPE_FLOAT;
                            break;
                        case "char":
                            token.TokenType = TokenType.TYPE_CHAR;
                            break;
                        case "if":
                            token.TokenType = TokenType.KEYWORD_IF;
                            break;
                        case "then":
                            token.TokenType = TokenType.KEYWORD_THEN;
                            break;
                        case "else":
                            token.TokenType = TokenType.KEYWORD_ELSE;
                            break;
                        case "repeat":
                            token.TokenType = TokenType.KEYWORD_REPEAT;
                            break;
                        case "until":
                            token.TokenType = TokenType.KEYWORD_UNTIL;
                            break;
                        case "while":
                            token.TokenType = TokenType.KEYWORD_WHILE;
                            break;
                        case "do":
                            token.TokenType = TokenType.KEYWORD_DO;
                            break;
                        case "read":
                            token.TokenType = TokenType.KEYWORD_READ;
                            break;
                        case "write":
                            token.TokenType = TokenType.KEYWORD_WRITE;
                            break;
                        default:
                            token.TokenType = TokenType.IDENTIFIER;
                    }

                    readNextChar = false;
                    return token;

                // --------------------------------------------
                default: // Invalid state
                    // TODO: Ajustar excessao
                    throw new Exception("Invalid character on symbol(POS: " + 0 + " )");
            }
        }

        // TODO: Ajustar excessao
        throw new Exception("Invalid character on symbol(POS: " + 0 + " )");
    }
}
