import java.io.*;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;

import uk.org.ogsadai.parser.sql92query.*;


public class __Test__ {

    public static void main(String args[]) throws Exception {
        SQL92QueryLexer lex = new SQL92QueryLexer(new ANTLRFileStream("/home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g", "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        SQL92QueryParser g = new SQL92QueryParser(tokens, 49100, null);
        try {
            g.statement();
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}