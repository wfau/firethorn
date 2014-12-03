// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g 2011-11-03 11:20:26

package uk.org.ogsadai.parser.sql92query;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class SQL92QueryLexer extends Lexer {
    public static final int FUNCTION=15;
    public static final int CAST=30;
    public static final int EXCEPT_ALL=27;
    public static final int FULL_OUTER_JOIN=21;
    public static final int NOT=16;
    public static final int EXCEPT=25;
    public static final int EOF=-1;
    public static final int STATEMENT=4;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int IS_NULL=23;
    public static final int TABLECOLUMN=18;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int BOUND=29;
    public static final int T__96=96;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int ASC=31;
    public static final int INT=34;
    public static final int NUMERIC=37;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int INTERSECT=28;
    public static final int T__71=71;
    public static final int WS=39;
    public static final int T__72=72;
    public static final int T__70=70;
    public static final int SELECT_LIST=8;
    public static final int QUERY=5;
    public static final int SETOP=6;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int T__74=74;
    public static final int T__73=73;
    public static final int T__79=79;
    public static final int T__78=78;
    public static final int T__77=77;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int WHERE=10;
    public static final int T__67=67;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int ORDER=7;
    public static final int LIMIT=33;
    public static final int LEFT_OUTER_JOIN=20;
    public static final int FLOAT=36;
    public static final int ID=35;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__59=59;
    public static final int T__103=103;
    public static final int RIGHT_OUTER_JOIN=19;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int COLUMN=14;
    public static final int T__50=50;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int RELATION=13;
    public static final int SET=17;
    public static final int HAVING=12;
    public static final int T__102=102;
    public static final int T__101=101;
    public static final int T__100=100;
    public static final int JOIN=22;
    public static final int UNION=24;
    public static final int UNION_ALL=26;
    public static final int GROUP_BY=11;
    public static final int FROM_LIST=9;
    public static final int DESC=32;
    public static final int STRING=38;

    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException re, BitSet follow)
        throws RecognitionException
    {
        throw re;
    }


    // delegates
    // delegators

    public SQL92QueryLexer() {;} 
    public SQL92QueryLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SQL92QueryLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g"; }

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:25:7: ( ';' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:25:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:26:7: ( 'LIMIT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:26:9: 'LIMIT'
            {
            match("LIMIT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:27:7: ( 'UNION' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:27:9: 'UNION'
            {
            match("UNION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:28:7: ( 'ALL' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:28:9: 'ALL'
            {
            match("ALL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:29:7: ( 'EXCEPT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:29:9: 'EXCEPT'
            {
            match("EXCEPT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:30:7: ( 'INTERSECT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:30:9: 'INTERSECT'
            {
            match("INTERSECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:31:7: ( 'SELECT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:31:9: 'SELECT'
            {
            match("SELECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:32:7: ( 'FROM' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:32:9: 'FROM'
            {
            match("FROM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:33:7: ( 'WHERE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:33:9: 'WHERE'
            {
            match("WHERE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:34:7: ( 'GROUP BY' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:34:9: 'GROUP BY'
            {
            match("GROUP BY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:35:7: ( 'HAVING' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:35:9: 'HAVING'
            {
            match("HAVING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:36:7: ( 'DISTINCT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:36:9: 'DISTINCT'
            {
            match("DISTINCT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:37:7: ( '(' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:37:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:38:7: ( ')' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:38:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:39:7: ( '*' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:39:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:40:7: ( ',' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:40:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:41:7: ( 'CAST' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:41:9: 'CAST'
            {
            match("CAST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:42:7: ( 'AS' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:42:9: 'AS'
            {
            match("AS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:43:7: ( 'ORDER' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:43:9: 'ORDER'
            {
            match("ORDER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:44:7: ( 'BY' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:44:9: 'BY'
            {
            match("BY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:45:7: ( 'DESC' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:45:9: 'DESC'
            {
            match("DESC"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:46:7: ( 'ASC' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:46:9: 'ASC'
            {
            match("ASC"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:47:7: ( '.' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:47:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:48:7: ( 'DATE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:48:9: 'DATE'
            {
            match("DATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:49:7: ( 'TIMESTAMP' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:49:9: 'TIMESTAMP'
            {
            match("TIMESTAMP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:50:7: ( 'TIME' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:50:9: 'TIME'
            {
            match("TIME"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:51:7: ( 'INTERVAL' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:51:9: 'INTERVAL'
            {
            match("INTERVAL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:52:7: ( 'YEAR' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:52:9: 'YEAR'
            {
            match("YEAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:53:7: ( 'MONTH' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:53:9: 'MONTH'
            {
            match("MONTH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:54:7: ( 'DAY' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:54:9: 'DAY'
            {
            match("DAY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:55:7: ( 'HOUR' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:55:9: 'HOUR'
            {
            match("HOUR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:56:7: ( 'MINUTE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:56:9: 'MINUTE'
            {
            match("MINUTE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:57:7: ( 'SECOND' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:57:9: 'SECOND'
            {
            match("SECOND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:58:7: ( '+' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:58:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:59:7: ( '-' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:59:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:60:7: ( '/' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:60:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:61:7: ( 'NULL' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:61:9: 'NULL'
            {
            match("NULL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:62:7: ( 'TRUE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:62:9: 'TRUE'
            {
            match("TRUE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:63:7: ( 'FALSE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:63:9: 'FALSE'
            {
            match("FALSE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:64:7: ( '||' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:64:9: '||'
            {
            match("||"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:65:7: ( 'RIGHT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:65:9: 'RIGHT'
            {
            match("RIGHT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:66:7: ( 'OUTER' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:66:9: 'OUTER'
            {
            match("OUTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:67:7: ( 'JOIN' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:67:9: 'JOIN'
            {
            match("JOIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:68:7: ( 'LEFT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:68:9: 'LEFT'
            {
            match("LEFT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:69:7: ( 'FULL' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:69:9: 'FULL'
            {
            match("FULL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:70:7: ( 'INNER' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:70:9: 'INNER'
            {
            match("INNER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:71:7: ( 'ON' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:71:9: 'ON'
            {
            match("ON"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:72:7: ( 'OR' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:72:9: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:73:7: ( 'AND' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:73:9: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:74:7: ( 'NOT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:74:9: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:75:7: ( 'IS' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:75:9: 'IS'
            {
            match("IS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:76:7: ( 'IN' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:76:9: 'IN'
            {
            match("IN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:77:7: ( 'BETWEEN' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:77:9: 'BETWEEN'
            {
            match("BETWEEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:78:7: ( 'EXISTS' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:78:9: 'EXISTS'
            {
            match("EXISTS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:79:7: ( '=' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:79:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:80:7: ( '<>' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:80:9: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "T__96"
    public final void mT__96() throws RecognitionException {
        try {
            int _type = T__96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:81:7: ( '!=' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:81:9: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__96"

    // $ANTLR start "T__97"
    public final void mT__97() throws RecognitionException {
        try {
            int _type = T__97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:82:7: ( '<' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:82:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__97"

    // $ANTLR start "T__98"
    public final void mT__98() throws RecognitionException {
        try {
            int _type = T__98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:83:7: ( '>' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:83:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__98"

    // $ANTLR start "T__99"
    public final void mT__99() throws RecognitionException {
        try {
            int _type = T__99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:84:7: ( '>=' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:84:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__99"

    // $ANTLR start "T__100"
    public final void mT__100() throws RecognitionException {
        try {
            int _type = T__100;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:85:8: ( '<=' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:85:10: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__100"

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:86:8: ( 'SOME' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:86:10: 'SOME'
            {
            match("SOME"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:87:8: ( 'ANY' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:87:10: 'ANY'
            {
            match("ANY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:88:8: ( 'LIKE' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:88:10: 'LIKE'
            {
            match("LIKE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:89:8: ( 'DEFAULT' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:89:10: 'DEFAULT'
            {
            match("DEFAULT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:90:8: ( '@' )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:90:10: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:5: ( ( 'a' .. 'z' | 'A' .. 'Z' ) ( ( 'a' .. 'z' | 'A' .. 'Z' ) | ( '0' .. '9' ) | '_' )* | '`' (~ ( '\\'' | '\\n' | '\\r' | '`' ) ) ( (~ ( '\\'' | '\\n' | '\\r' | '`' ) ) )* '`' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( ((LA3_0>='A' && LA3_0<='Z')||(LA3_0>='a' && LA3_0<='z')) ) {
                alt3=1;
            }
            else if ( (LA3_0=='`') ) {
                alt3=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:9: ( 'a' .. 'z' | 'A' .. 'Z' ) ( ( 'a' .. 'z' | 'A' .. 'Z' ) | ( '0' .. '9' ) | '_' )*
                    {
                    if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:31: ( ( 'a' .. 'z' | 'A' .. 'Z' ) | ( '0' .. '9' ) | '_' )*
                    loop1:
                    do {
                        int alt1=4;
                        switch ( input.LA(1) ) {
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'I':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'S':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'k':
                        case 'l':
                        case 'm':
                        case 'n':
                        case 'o':
                        case 'p':
                        case 'q':
                        case 'r':
                        case 's':
                        case 't':
                        case 'u':
                        case 'v':
                        case 'w':
                        case 'x':
                        case 'y':
                        case 'z':
                            {
                            alt1=1;
                            }
                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            {
                            alt1=2;
                            }
                            break;
                        case '_':
                            {
                            alt1=3;
                            }
                            break;

                        }

                        switch (alt1) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:33: ( 'a' .. 'z' | 'A' .. 'Z' )
                            {
                            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:57: ( '0' .. '9' )
                            {
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:57: ( '0' .. '9' )
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:58: '0' .. '9'
                            {
                            matchRange('0','9'); 

                            }


                            }
                            break;
                        case 3 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:292:70: '_'
                            {
                            match('_'); 

                            }
                            break;

                        default :
                            break loop1;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:9: '`' (~ ( '\\'' | '\\n' | '\\r' | '`' ) ) ( (~ ( '\\'' | '\\n' | '\\r' | '`' ) ) )* '`'
                    {
                    match('`'); 
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:13: (~ ( '\\'' | '\\n' | '\\r' | '`' ) )
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:14: ~ ( '\\'' | '\\n' | '\\r' | '`' )
                    {
                    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='_')||(input.LA(1)>='a' && input.LA(1)<='\uFFFF') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }

                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:37: ( (~ ( '\\'' | '\\n' | '\\r' | '`' ) ) )*
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( ((LA2_0>='\u0000' && LA2_0<='\t')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='&')||(LA2_0>='(' && LA2_0<='_')||(LA2_0>='a' && LA2_0<='\uFFFF')) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:39: (~ ( '\\'' | '\\n' | '\\r' | '`' ) )
                            {
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:39: (~ ( '\\'' | '\\n' | '\\r' | '`' ) )
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:293:40: ~ ( '\\'' | '\\n' | '\\r' | '`' )
                            {
                            if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='_')||(input.LA(1)>='a' && input.LA(1)<='\uFFFF') ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }


                            }
                            break;

                        default :
                            break loop2;
                        }
                    } while (true);

                    match('`'); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )+ )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )+
            {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:9: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')) ) {
                    alt4=1;
                }


                switch (alt4) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:10: '0' .. '9'
                    {
                    matchRange('0','9'); 

                    }
                    break;

                default :
                    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            match('.'); 
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:25: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:296:26: '0' .. '9'
                    {
                    matchRange('0','9'); 

                    }
                    break;

                default :
                    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:299:5: ( ( '0' .. '9' )+ )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:299:9: ( '0' .. '9' )+
            {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:299:9: ( '0' .. '9' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                    alt6=1;
                }


                switch (alt6) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:299:10: '0' .. '9'
                    {
                    matchRange('0','9'); 

                    }
                    break;

                default :
                    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "NUMERIC"
    public final void mNUMERIC() throws RecognitionException {
        try {
            int _type = NUMERIC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:5: ( ( INT | FLOAT ) 'E' ( '+' | '-' )? INT )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:9: ( INT | FLOAT ) 'E' ( '+' | '-' )? INT
            {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:9: ( INT | FLOAT )
            int alt7=2;
            alt7 = dfa7.predict(input);
            switch (alt7) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:10: INT
                    {
                    mINT(); 

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:16: FLOAT
                    {
                    mFLOAT(); 

                    }
                    break;

            }

            match('E'); 
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:302:27: ( '+' | '-' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='+'||LA8_0=='-') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            mINT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMERIC"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:305:5: ( '\"' (~ ( '\"' | '\\n' | '\\r' ) )* '\"' | '\\'' (~ ( '\\'' | '\\n' | '\\r' ) )* '\\'' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0=='\"') ) {
                alt11=1;
            }
            else if ( (LA11_0=='\'') ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:305:9: '\"' (~ ( '\"' | '\\n' | '\\r' ) )* '\"'
                    {
                    match('\"'); 
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:305:13: (~ ( '\"' | '\\n' | '\\r' ) )*
                    loop9:
                    do {
                        int alt9=2;
                        int LA9_0 = input.LA(1);

                        if ( ((LA9_0>='\u0000' && LA9_0<='\t')||(LA9_0>='\u000B' && LA9_0<='\f')||(LA9_0>='\u000E' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='\uFFFF')) ) {
                            alt9=1;
                        }


                        switch (alt9) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:305:14: ~ ( '\"' | '\\n' | '\\r' )
                            {
                            if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                        default :
                            break loop9;
                        }
                    } while (true);

                    match('\"'); 

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:306:9: '\\'' (~ ( '\\'' | '\\n' | '\\r' ) )* '\\''
                    {
                    match('\''); 
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:306:14: (~ ( '\\'' | '\\n' | '\\r' ) )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);

                        if ( ((LA10_0>='\u0000' && LA10_0<='\t')||(LA10_0>='\u000B' && LA10_0<='\f')||(LA10_0>='\u000E' && LA10_0<='&')||(LA10_0>='(' && LA10_0<='\uFFFF')) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:306:15: ~ ( '\\'' | '\\n' | '\\r' )
                            {
                            if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
                                input.consume();

                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                recover(mse);
                                throw mse;}


                            }
                            break;

                        default :
                            break loop10;
                        }
                    } while (true);

                    match('\''); 

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:309:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:309:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:8: ( T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | ID | FLOAT | INT | NUMERIC | STRING | WS )
        int alt12=72;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:10: T__40
                {
                mT__40(); 

                }
                break;
            case 2 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:16: T__41
                {
                mT__41(); 

                }
                break;
            case 3 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:22: T__42
                {
                mT__42(); 

                }
                break;
            case 4 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:28: T__43
                {
                mT__43(); 

                }
                break;
            case 5 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:34: T__44
                {
                mT__44(); 

                }
                break;
            case 6 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:40: T__45
                {
                mT__45(); 

                }
                break;
            case 7 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:46: T__46
                {
                mT__46(); 

                }
                break;
            case 8 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:52: T__47
                {
                mT__47(); 

                }
                break;
            case 9 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:58: T__48
                {
                mT__48(); 

                }
                break;
            case 10 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:64: T__49
                {
                mT__49(); 

                }
                break;
            case 11 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:70: T__50
                {
                mT__50(); 

                }
                break;
            case 12 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:76: T__51
                {
                mT__51(); 

                }
                break;
            case 13 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:82: T__52
                {
                mT__52(); 

                }
                break;
            case 14 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:88: T__53
                {
                mT__53(); 

                }
                break;
            case 15 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:94: T__54
                {
                mT__54(); 

                }
                break;
            case 16 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:100: T__55
                {
                mT__55(); 

                }
                break;
            case 17 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:106: T__56
                {
                mT__56(); 

                }
                break;
            case 18 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:112: T__57
                {
                mT__57(); 

                }
                break;
            case 19 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:118: T__58
                {
                mT__58(); 

                }
                break;
            case 20 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:124: T__59
                {
                mT__59(); 

                }
                break;
            case 21 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:130: T__60
                {
                mT__60(); 

                }
                break;
            case 22 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:136: T__61
                {
                mT__61(); 

                }
                break;
            case 23 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:142: T__62
                {
                mT__62(); 

                }
                break;
            case 24 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:148: T__63
                {
                mT__63(); 

                }
                break;
            case 25 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:154: T__64
                {
                mT__64(); 

                }
                break;
            case 26 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:160: T__65
                {
                mT__65(); 

                }
                break;
            case 27 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:166: T__66
                {
                mT__66(); 

                }
                break;
            case 28 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:172: T__67
                {
                mT__67(); 

                }
                break;
            case 29 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:178: T__68
                {
                mT__68(); 

                }
                break;
            case 30 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:184: T__69
                {
                mT__69(); 

                }
                break;
            case 31 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:190: T__70
                {
                mT__70(); 

                }
                break;
            case 32 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:196: T__71
                {
                mT__71(); 

                }
                break;
            case 33 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:202: T__72
                {
                mT__72(); 

                }
                break;
            case 34 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:208: T__73
                {
                mT__73(); 

                }
                break;
            case 35 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:214: T__74
                {
                mT__74(); 

                }
                break;
            case 36 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:220: T__75
                {
                mT__75(); 

                }
                break;
            case 37 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:226: T__76
                {
                mT__76(); 

                }
                break;
            case 38 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:232: T__77
                {
                mT__77(); 

                }
                break;
            case 39 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:238: T__78
                {
                mT__78(); 

                }
                break;
            case 40 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:244: T__79
                {
                mT__79(); 

                }
                break;
            case 41 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:250: T__80
                {
                mT__80(); 

                }
                break;
            case 42 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:256: T__81
                {
                mT__81(); 

                }
                break;
            case 43 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:262: T__82
                {
                mT__82(); 

                }
                break;
            case 44 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:268: T__83
                {
                mT__83(); 

                }
                break;
            case 45 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:274: T__84
                {
                mT__84(); 

                }
                break;
            case 46 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:280: T__85
                {
                mT__85(); 

                }
                break;
            case 47 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:286: T__86
                {
                mT__86(); 

                }
                break;
            case 48 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:292: T__87
                {
                mT__87(); 

                }
                break;
            case 49 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:298: T__88
                {
                mT__88(); 

                }
                break;
            case 50 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:304: T__89
                {
                mT__89(); 

                }
                break;
            case 51 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:310: T__90
                {
                mT__90(); 

                }
                break;
            case 52 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:316: T__91
                {
                mT__91(); 

                }
                break;
            case 53 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:322: T__92
                {
                mT__92(); 

                }
                break;
            case 54 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:328: T__93
                {
                mT__93(); 

                }
                break;
            case 55 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:334: T__94
                {
                mT__94(); 

                }
                break;
            case 56 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:340: T__95
                {
                mT__95(); 

                }
                break;
            case 57 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:346: T__96
                {
                mT__96(); 

                }
                break;
            case 58 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:352: T__97
                {
                mT__97(); 

                }
                break;
            case 59 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:358: T__98
                {
                mT__98(); 

                }
                break;
            case 60 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:364: T__99
                {
                mT__99(); 

                }
                break;
            case 61 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:370: T__100
                {
                mT__100(); 

                }
                break;
            case 62 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:377: T__101
                {
                mT__101(); 

                }
                break;
            case 63 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:384: T__102
                {
                mT__102(); 

                }
                break;
            case 64 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:391: T__103
                {
                mT__103(); 

                }
                break;
            case 65 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:398: T__104
                {
                mT__104(); 

                }
                break;
            case 66 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:405: T__105
                {
                mT__105(); 

                }
                break;
            case 67 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:412: ID
                {
                mID(); 

                }
                break;
            case 68 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:415: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 69 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:421: INT
                {
                mINT(); 

                }
                break;
            case 70 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:425: NUMERIC
                {
                mNUMERIC(); 

                }
                break;
            case 71 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:433: STRING
                {
                mSTRING(); 

                }
                break;
            case 72 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92Query.g:1:440: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA7 dfa7 = new DFA7(this);
    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA7_eotS =
        "\4\uffff";
    static final String DFA7_eofS =
        "\4\uffff";
    static final String DFA7_minS =
        "\1\60\1\56\2\uffff";
    static final String DFA7_maxS =
        "\1\71\1\105\2\uffff";
    static final String DFA7_acceptS =
        "\2\uffff\1\2\1\1";
    static final String DFA7_specialS =
        "\4\uffff}>";
    static final String[] DFA7_transitionS = {
            "\12\1",
            "\1\2\1\uffff\12\1\13\uffff\1\3",
            "",
            ""
    };

    static final short[] DFA7_eot = DFA.unpackEncodedString(DFA7_eotS);
    static final short[] DFA7_eof = DFA.unpackEncodedString(DFA7_eofS);
    static final char[] DFA7_min = DFA.unpackEncodedStringToUnsignedChars(DFA7_minS);
    static final char[] DFA7_max = DFA.unpackEncodedStringToUnsignedChars(DFA7_maxS);
    static final short[] DFA7_accept = DFA.unpackEncodedString(DFA7_acceptS);
    static final short[] DFA7_special = DFA.unpackEncodedString(DFA7_specialS);
    static final short[][] DFA7_transition;

    static {
        int numStates = DFA7_transitionS.length;
        DFA7_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA7_transition[i] = DFA.unpackEncodedString(DFA7_transitionS[i]);
        }
    }

    class DFA7 extends DFA {

        public DFA7(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 7;
            this.eot = DFA7_eot;
            this.eof = DFA7_eof;
            this.min = DFA7_min;
            this.max = DFA7_max;
            this.accept = DFA7_accept;
            this.special = DFA7_special;
            this.transition = DFA7_transition;
        }
        public String getDescription() {
            return "302:9: ( INT | FLOAT )";
        }
    }
    static final String DFA12_eotS =
        "\2\uffff\13\44\4\uffff\3\44\1\uffff\3\44\3\uffff\1\44\1\uffff\2"+
        "\44\1\uffff\1\116\1\uffff\1\120\2\uffff\1\121\2\uffff\4\44\1\132"+
        "\2\44\1\141\1\142\15\44\1\164\1\44\1\166\1\167\12\44\10\uffff\4"+
        "\44\1\u0087\1\u0088\1\uffff\1\u0089\1\u008a\4\44\2\uffff\16\44\1"+
        "\u009d\2\44\1\uffff\1\44\2\uffff\7\44\1\u00a8\2\44\1\u00ab\1\44"+
        "\1\u00ad\1\u00ae\1\44\4\uffff\6\44\1\u00b6\1\u00b7\1\44\1\u00b9"+
        "\3\44\1\u00bd\1\44\1\u00bf\1\44\1\u00c1\1\uffff\1\u00c2\3\44\1\u00c7"+
        "\1\u00c8\1\u00c9\2\44\1\u00cc\1\uffff\1\44\1\u00ce\1\uffff\1\u00cf"+
        "\2\uffff\1\u00d0\3\44\1\u00d5\2\44\2\uffff\1\u00d8\1\uffff\1\u00d9"+
        "\2\44\1\uffff\1\44\1\uffff\1\44\2\uffff\1\u00de\1\u00df\2\44\3\uffff"+
        "\1\u00e2\1\44\1\uffff\1\u00e4\3\uffff\1\u00e5\1\u00e6\2\44\1\uffff"+
        "\1\u00e9\1\u00ea\3\uffff\1\u00eb\2\44\2\uffff\2\44\1\uffff\1\u00f0"+
        "\3\uffff\2\44\3\uffff\1\44\1\u00f4\1\u00f5\1\44\1\uffff\1\44\1\u00f8"+
        "\1\u00f9\2\uffff\1\44\1\u00fb\2\uffff\1\u00fc\2\uffff";
    static final String DFA12_eofS =
        "\u00fd\uffff";
    static final String DFA12_minS =
        "\1\11\1\uffff\1\105\1\116\1\114\1\130\1\116\1\105\1\101\1\110\1"+
        "\122\2\101\4\uffff\1\101\1\116\1\105\1\uffff\1\111\1\105\1\111\3"+
        "\uffff\1\117\1\uffff\1\111\1\117\1\uffff\1\75\1\uffff\1\75\2\uffff"+
        "\1\56\2\uffff\1\113\1\106\1\111\1\114\1\60\1\104\1\103\2\60\1\103"+
        "\1\115\1\117\2\114\1\105\1\117\1\126\1\125\1\123\1\106\1\124\1\123"+
        "\1\60\1\124\2\60\1\124\1\115\1\125\1\101\2\116\1\114\1\124\1\107"+
        "\1\111\6\uffff\1\60\1\uffff\1\111\1\105\1\124\1\117\2\60\1\uffff"+
        "\2\60\1\105\1\123\2\105\2\uffff\1\105\1\117\1\105\1\115\1\123\1"+
        "\114\1\122\1\125\1\111\1\122\1\124\1\103\1\101\1\105\1\60\1\124"+
        "\1\105\1\uffff\1\105\2\uffff\1\127\2\105\1\122\1\124\1\125\1\114"+
        "\1\60\1\110\1\116\1\60\1\124\2\60\1\116\4\uffff\1\120\1\124\2\122"+
        "\1\103\1\116\2\60\1\105\1\60\1\105\1\120\1\116\1\60\1\111\1\60\1"+
        "\125\1\60\1\uffff\1\60\2\122\1\105\3\60\1\110\1\124\1\60\1\uffff"+
        "\1\124\1\60\1\uffff\1\60\2\uffff\1\60\1\124\2\123\1\60\1\124\1\104"+
        "\2\uffff\1\60\1\uffff\1\60\1\40\1\107\1\uffff\1\116\1\uffff\1\114"+
        "\2\uffff\2\60\1\105\1\124\3\uffff\1\60\1\105\1\uffff\1\60\3\uffff"+
        "\2\60\1\105\1\101\1\uffff\2\60\3\uffff\1\60\1\103\1\124\2\uffff"+
        "\1\116\1\101\1\uffff\1\60\3\uffff\1\103\1\114\3\uffff\1\124\2\60"+
        "\1\115\1\uffff\1\124\2\60\2\uffff\1\120\1\60\2\uffff\1\60\2\uffff";
    static final String DFA12_maxS =
        "\1\174\1\uffff\1\111\1\116\1\123\1\130\1\123\1\117\1\125\1\110\1"+
        "\122\1\117\1\111\4\uffff\1\101\1\125\1\131\1\uffff\1\122\1\105\1"+
        "\117\3\uffff\1\125\1\uffff\1\111\1\117\1\uffff\1\76\1\uffff\1\75"+
        "\2\uffff\1\105\2\uffff\1\115\1\106\1\111\1\114\1\172\1\131\1\111"+
        "\2\172\1\114\1\115\1\117\2\114\1\105\1\117\1\126\1\125\2\123\1\131"+
        "\1\123\1\172\1\124\2\172\1\124\1\115\1\125\1\101\2\116\1\114\1\124"+
        "\1\107\1\111\6\uffff\1\71\1\uffff\1\111\1\105\1\124\1\117\2\172"+
        "\1\uffff\2\172\1\105\1\123\2\105\2\uffff\1\105\1\117\1\105\1\115"+
        "\1\123\1\114\1\122\1\125\1\111\1\122\1\124\1\103\1\101\1\105\1\172"+
        "\1\124\1\105\1\uffff\1\105\2\uffff\1\127\2\105\1\122\1\124\1\125"+
        "\1\114\1\172\1\110\1\116\1\105\1\124\2\172\1\116\4\uffff\1\120\1"+
        "\124\2\122\1\103\1\116\2\172\1\105\1\172\1\105\1\120\1\116\1\172"+
        "\1\111\1\172\1\125\1\172\1\uffff\1\172\2\122\1\105\3\172\1\110\1"+
        "\124\1\172\1\uffff\1\124\1\172\1\uffff\1\172\2\uffff\1\172\1\124"+
        "\1\123\1\126\1\172\1\124\1\104\2\uffff\1\172\1\uffff\1\172\1\40"+
        "\1\107\1\uffff\1\116\1\uffff\1\114\2\uffff\2\172\1\105\1\124\3\uffff"+
        "\1\172\1\105\1\uffff\1\172\3\uffff\2\172\1\105\1\101\1\uffff\2\172"+
        "\3\uffff\1\172\1\103\1\124\2\uffff\1\116\1\101\1\uffff\1\172\3\uffff"+
        "\1\103\1\114\3\uffff\1\124\2\172\1\115\1\uffff\1\124\2\172\2\uffff"+
        "\1\120\1\172\2\uffff\1\172\2\uffff";
    static final String DFA12_acceptS =
        "\1\uffff\1\1\13\uffff\1\15\1\16\1\17\1\20\3\uffff\1\27\3\uffff\1"+
        "\42\1\43\1\44\1\uffff\1\50\2\uffff\1\67\1\uffff\1\71\1\uffff\1\102"+
        "\1\103\1\uffff\1\107\1\110\44\uffff\1\70\1\75\1\72\1\74\1\73\1\105"+
        "\1\uffff\1\106\6\uffff\1\22\6\uffff\1\64\1\63\21\uffff\1\60\1\uffff"+
        "\1\57\1\24\17\uffff\1\4\1\26\1\61\1\77\22\uffff\1\36\12\uffff\1"+
        "\62\2\uffff\1\104\1\uffff\1\100\1\54\7\uffff\1\76\1\10\1\uffff\1"+
        "\55\3\uffff\1\37\1\uffff\1\25\1\uffff\1\30\1\21\4\uffff\1\32\1\46"+
        "\1\34\2\uffff\1\45\1\uffff\1\53\1\2\1\3\4\uffff\1\56\2\uffff\1\47"+
        "\1\11\1\12\3\uffff\1\23\1\52\2\uffff\1\35\1\uffff\1\51\1\5\1\66"+
        "\2\uffff\1\7\1\41\1\13\4\uffff\1\40\3\uffff\1\101\1\65\2\uffff\1"+
        "\33\1\14\1\uffff\1\6\1\31";
    static final String DFA12_specialS =
        "\u00fd\uffff}>";
    static final String[] DFA12_transitionS = {
            "\2\47\2\uffff\1\47\22\uffff\1\47\1\41\1\46\4\uffff\1\46\1\15"+
            "\1\16\1\17\1\30\1\20\1\31\1\24\1\32\12\45\1\uffff\1\1\1\40\1"+
            "\37\1\42\1\uffff\1\43\1\4\1\23\1\21\1\14\1\5\1\10\1\12\1\13"+
            "\1\6\1\36\1\44\1\2\1\27\1\33\1\22\2\44\1\35\1\7\1\25\1\3\1\44"+
            "\1\11\1\44\1\26\1\44\5\uffff\33\44\1\uffff\1\34",
            "",
            "\1\51\3\uffff\1\50",
            "\1\52",
            "\1\53\1\uffff\1\55\4\uffff\1\54",
            "\1\56",
            "\1\57\4\uffff\1\60",
            "\1\61\11\uffff\1\62",
            "\1\64\20\uffff\1\63\2\uffff\1\65",
            "\1\66",
            "\1\67",
            "\1\70\15\uffff\1\71",
            "\1\74\3\uffff\1\73\3\uffff\1\72",
            "",
            "",
            "",
            "",
            "\1\75",
            "\1\100\3\uffff\1\76\2\uffff\1\77",
            "\1\102\23\uffff\1\101",
            "",
            "\1\103\10\uffff\1\104",
            "\1\105",
            "\1\107\5\uffff\1\106",
            "",
            "",
            "",
            "\1\111\5\uffff\1\110",
            "",
            "\1\112",
            "\1\113",
            "",
            "\1\115\1\114",
            "",
            "\1\117",
            "",
            "",
            "\1\122\1\uffff\12\45\13\uffff\1\123",
            "",
            "",
            "\1\125\1\uffff\1\124",
            "\1\126",
            "\1\127",
            "\1\130",
            "\12\44\7\uffff\2\44\1\131\27\44\4\uffff\1\44\1\uffff\32\44",
            "\1\133\24\uffff\1\134",
            "\1\135\5\uffff\1\136",
            "\12\44\7\uffff\15\44\1\140\5\44\1\137\6\44\4\uffff\1\44\1\uffff"+
            "\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\144\10\uffff\1\143",
            "\1\145",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\157\14\uffff\1\156",
            "\1\160\4\uffff\1\161",
            "\1\162",
            "\12\44\7\uffff\3\44\1\163\26\44\4\uffff\1\44\1\uffff\32\44",
            "\1\165",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\1\176",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\u0082",
            "",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "",
            "",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\u009c",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u009e",
            "\1\u009f",
            "",
            "\1\u00a0",
            "",
            "",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00a9",
            "\1\u00aa",
            "\12\u0082\13\uffff\1\123",
            "\1\u00ac",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00af",
            "",
            "",
            "",
            "",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "\1\u00b4",
            "\1\u00b5",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00b8",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00be",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00c0",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\12\44\7\uffff\22\44\1\u00c6\7\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00ca",
            "\1\u00cb",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\1\u00cd",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3\2\uffff\1\u00d4",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00d6",
            "\1\u00d7",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00da",
            "\1\u00db",
            "",
            "\1\u00dc",
            "",
            "\1\u00dd",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00e0",
            "\1\u00e1",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00e3",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00e7",
            "\1\u00e8",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00ec",
            "\1\u00ed",
            "",
            "",
            "\1\u00ee",
            "\1\u00ef",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "",
            "\1\u00f1",
            "\1\u00f2",
            "",
            "",
            "",
            "\1\u00f3",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\1\u00f6",
            "",
            "\1\u00f7",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\1\u00fa",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            "",
            "\12\44\7\uffff\32\44\4\uffff\1\44\1\uffff\32\44",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | ID | FLOAT | INT | NUMERIC | STRING | WS );";
        }
    }
 

}