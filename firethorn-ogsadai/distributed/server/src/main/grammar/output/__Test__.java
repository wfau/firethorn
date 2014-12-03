import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;


public class __Test__ {

    public static void main(String args[]) throws Exception {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRFileStream("C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\output\\__Test___input.txt", "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens, 49100, null);
        try {
            g.statement();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}