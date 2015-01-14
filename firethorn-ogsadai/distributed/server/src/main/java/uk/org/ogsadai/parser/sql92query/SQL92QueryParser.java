package uk.org.ogsadai.parser.sql92query;

// $ANTLR 3.2 Sep 23, 2009 12:02:23 C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g 2010-09-09 10:42:37

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.debug.*;
import java.io.IOException;

import org.antlr.runtime.tree.*;

public class SQL92QueryParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STATEMENT", "QUERY", "SETOP", "ORDER", "SELECT_LIST", "FROM_LIST", "WHERE", "GROUP_BY", "HAVING", "RELATION", "COLUMN", "FUNCTION", "NOT", "SET", "TABLECOLUMN", "RIGHT_OUTER_JOIN", "LEFT_OUTER_JOIN", "FULL_OUTER_JOIN", "JOIN", "IS_NULL", "UNION", "EXCEPT", "UNION_ALL", "EXCEPT_ALL", "INTERSECT", "BOUND", "CAST", "ID", "INT", "FLOAT", "NUMERIC", "STRING", "WS", "';'", "'UNION'", "'ALL'", "'EXCEPT'", "'INTERSECT'", "'SELECT'", "'FROM'", "'WHERE'", "'GROUP BY'", "'HAVING'", "'DISTINCT'", "'('", "')'", "'*'", "','", "'CAST'", "'AS'", "'ORDER'", "'BY'", "'.'", "'DATE'", "'TIMESTAMP'", "'TIME'", "'INTERVAL'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'+'", "'-'", "'/'", "'NULL'", "'TRUE'", "'FALSE'", "'||'", "'RIGHT'", "'OUTER'", "'JOIN'", "'LEFT'", "'FULL'", "'INNER'", "'ON'", "'OR'", "'AND'", "'NOT'", "'IS'", "'IN'", "'BETWEEN'", "'EXISTS'", "'='", "'<>'", "'!='", "'<'", "'>'", "'>='", "'<='", "'SOME'", "'ANY'", "'LIKE'", "'DEFAULT'", "'@'"
    };
    public static final int CAST=30;
    public static final int ASC=30;
    public static final int FUNCTION=15;
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
    public static final int BOUND=29;
    public static final int T__97=97;
    public static final int T__96=96;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int INT=32;
    public static final int NUMERIC=34;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int INTERSECT=28;
    public static final int WS=36;
    public static final int T__71=71;
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
    public static final int LEFT_OUTER_JOIN=20;
    public static final int FLOAT=33;
    public static final int ID=31;
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
    public static final int RIGHT_OUTER_JOIN=19;
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
    public static final int JOIN=22;
    public static final int UNION=24;
    public static final int UNION_ALL=26;
    public static final int FROM_LIST=9;
    public static final int GROUP_BY=11;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int STRING=35;

    // delegates
    // delegators

    public static final String[] ruleNames = new String[] {
        "invalidRule", "synpred130_SQL92Query", "synpred28_SQL92Query", 
        "synpred41_SQL92Query", "table_function", "synpred12_SQL92Query", 
        "synpred37_SQL92Query", "null_predicate", "synpred114_SQL92Query", 
        "synpred7_SQL92Query", "synpred126_SQL92Query", "synpred3_SQL92Query", 
        "synpred74_SQL92Query", "synpred20_SQL92Query", "synpred107_SQL92Query", 
        "synpred101_SQL92Query", "synpred45_SQL92Query", "synpred122_SQL92Query", 
        "synpred27_SQL92Query", "function", "table_name", "join_type", "synpred42_SQL92Query", 
        "synpred23_SQL92Query", "search_condition", "synpred1_SQL92Query", 
        "synpred19_SQL92Query", "synpred88_SQL92Query", "synpred68_SQL92Query", 
        "synpred90_SQL92Query", "synpred62_SQL92Query", "synpred124_SQL92Query", 
        "synpred104_SQL92Query", "synpred63_SQL92Query", "synpred73_SQL92Query", 
        "synpred44_SQL92Query", "statement", "synpred86_SQL92Query", "set_quantifier", 
        "synpred55_SQL92Query", "boolean_term", "synpred79_SQL92Query", 
        "synpred110_SQL92Query", "synpred4_SQL92Query", "relation", "synpred120_SQL92Query", 
        "synpred25_SQL92Query", "synpred100_SQL92Query", "synpred36_SQL92Query", 
        "synpred80_SQL92Query", "synpred132_SQL92Query", "synpred83_SQL92Query", 
        "exists_predicate", "sort_specification", "synpred18_SQL92Query", 
        "synpred116_SQL92Query", "synpred127_SQL92Query", "synpred99_SQL92Query", 
        "numeric_primary", "between_predicate", "synpred50_SQL92Query", 
        "synpred61_SQL92Query", "bind_table", "row_value", "synpred81_SQL92Query", 
        "synpred117_SQL92Query", "sub_query", "synpred30_SQL92Query", "synpred38_SQL92Query", 
        "synpred121_SQL92Query", "table", "synpred54_SQL92Query", "synpred47_SQL92Query", 
        "synpred131_SQL92Query", "synpred6_SQL92Query", "synpred119_SQL92Query", 
        "synpred94_SQL92Query", "synpred118_SQL92Query", "synpred112_SQL92Query", 
        "synpred85_SQL92Query", "synpred115_SQL92Query", "synpred24_SQL92Query", 
        "synpred106_SQL92Query", "synpred84_SQL92Query", "synpred9_SQL92Query", 
        "synpred89_SQL92Query", "synpred78_SQL92Query", "value_expression", 
        "select_list", "synpred108_SQL92Query", "like_predicate", "synpred128_SQL92Query", 
        "synpred113_SQL92Query", "synpred13_SQL92Query", "column_list", 
        "reserved_word_column_name", "string_value_expression", "synpred17_SQL92Query", 
        "synpred72_SQL92Query", "table_function_param", "synpred49_SQL92Query", 
        "synpred26_SQL92Query", "synpred77_SQL92Query", "synpred75_SQL92Query", 
        "in_predicate", "synpred60_SQL92Query", "synpred34_SQL92Query", 
        "synpred109_SQL92Query", "synpred21_SQL92Query", "synpred16_SQL92Query", 
        "synpred71_SQL92Query", "synpred43_SQL92Query", "predicate", "synpred33_SQL92Query", 
        "synpred97_SQL92Query", "synpred15_SQL92Query", "query", "synpred129_SQL92Query", 
        "synpred66_SQL92Query", "comparison_predicate", "synpred5_SQL92Query", 
        "synpred123_SQL92Query", "synpred96_SQL92Query", "synpred40_SQL92Query", 
        "synpred76_SQL92Query", "synpred53_SQL92Query", "synpred92_SQL92Query", 
        "literal", "synpred46_SQL92Query", "synpred103_SQL92Query", "derived_column", 
        "synpred133_SQL92Query", "synpred93_SQL92Query", "synpred125_SQL92Query", 
        "synpred87_SQL92Query", "synpred67_SQL92Query", "correlation_specification", 
        "non_join_table", "synpred102_SQL92Query", "table_expression", "synpred65_SQL92Query", 
        "numeric_value_expression", "synpred48_SQL92Query", "order_by", 
        "interval", "synpred91_SQL92Query", "column_name", "synpred39_SQL92Query", 
        "synpred82_SQL92Query", "in_predicate_tail", "synpred10_SQL92Query", 
        "synpred11_SQL92Query", "synpred95_SQL92Query", "synpred51_SQL92Query", 
        "synpred2_SQL92Query", "value_expression_primary", "synpred59_SQL92Query", 
        "synpred56_SQL92Query", "synpred134_SQL92Query", "query_expression", 
        "synpred105_SQL92Query", "factor", "synpred69_SQL92Query", "synpred64_SQL92Query", 
        "synpred22_SQL92Query", "synpred70_SQL92Query", "boolean_primary", 
        "synpred57_SQL92Query", "synpred111_SQL92Query", "boolean_factor", 
        "datetime", "synpred98_SQL92Query", "synpred14_SQL92Query", "synpred29_SQL92Query", 
        "table_reference", "table_function_subquery", "synpred35_SQL92Query", 
        "synpred58_SQL92Query", "boolean_test", "set_op", "synpred52_SQL92Query", 
        "synpred8_SQL92Query", "synpred31_SQL92Query", "synpred32_SQL92Query"
    };
     
        public int ruleLevel = 0;
        public int getRuleLevel() { return ruleLevel; }
        public void incRuleLevel() { ruleLevel++; }
        public void decRuleLevel() { ruleLevel--; }
        public SQL92QueryParser(TokenStream input) {
            this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
        }
        public SQL92QueryParser(TokenStream input, int port, RecognizerSharedState state) {
            super(input, state);
            DebugEventSocketProxy proxy =
                new DebugEventSocketProxy(this,port,adaptor);
            setDebugListener(proxy);
            setTokenStream(new DebugTokenStream(input,proxy));
            try {
                proxy.handshake();
            }
            catch (IOException ioe) {
                reportError(ioe);
            }
            TreeAdaptor adap = new CommonTreeAdaptor();
            setTreeAdaptor(adap);
            proxy.setTreeAdaptor(adap);
        }
    public SQL92QueryParser(TokenStream input, DebugEventListener dbg) {
        super(input, dbg);

         
        TreeAdaptor adap = new CommonTreeAdaptor();
        setTreeAdaptor(adap);

    }
    protected boolean evalPredicate(boolean result, String predicate) {
        dbg.semanticPredicate(result, predicate);
        return result;
    }

    protected DebugTreeAdaptor adaptor;
    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = new DebugTreeAdaptor(dbg,adaptor);

    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }


    public String[] getTokenNames() { return SQL92QueryParser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g"; }


    protected void mismatch(IntStream input, int ttype, BitSet follow)
        throws RecognitionException
    { 
        throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException re, BitSet follow)
        throws RecognitionException
    {
        throw re;
    }


    public static class statement_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:87:1: statement : query_expression ( order_by )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ) ;
    public final SQL92QueryParser.statement_return statement() throws RecognitionException {
        SQL92QueryParser.statement_return retval = new SQL92QueryParser.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal3=null;
        Token EOF4=null;
        SQL92QueryParser.query_expression_return query_expression1 = null;

        SQL92QueryParser.order_by_return order_by2 = null;


        CommonTree char_literal3_tree=null;
        CommonTree EOF4_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleTokenStream stream_37=new RewriteRuleTokenStream(adaptor,"token 37");
        RewriteRuleSubtreeStream stream_query_expression=new RewriteRuleSubtreeStream(adaptor,"rule query_expression");
        RewriteRuleSubtreeStream stream_order_by=new RewriteRuleSubtreeStream(adaptor,"rule order_by");
        try { dbg.enterRule(getGrammarFileName(), "statement");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(87, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:2: ( query_expression ( order_by )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:4: query_expression ( order_by )? ( ';' )? EOF
            {
            dbg.location(88,4);
            pushFollow(FOLLOW_query_expression_in_statement183);
            query_expression1=query_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_query_expression.add(query_expression1.getTree());
            dbg.location(88,21);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:21: ( order_by )?
            int alt1=2;
            try { dbg.enterSubRule(1);
            try { dbg.enterDecision(1, isCyclicDecision);

            int LA1_0 = input.LA(1);

            if ( (LA1_0==54) ) {
                alt1=1;
            }
            } finally {dbg.exitDecision(1);}

            switch (alt1) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: order_by
                    {
                    dbg.location(88,21);
                    pushFollow(FOLLOW_order_by_in_statement185);
                    order_by2=order_by();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_order_by.add(order_by2.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(1);}

            dbg.location(88,31);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:31: ( ';' )?
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2, isCyclicDecision);

            int LA2_0 = input.LA(1);

            if ( (LA2_0==37) ) {
                alt2=1;
            }
            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: ';'
                    {
                    dbg.location(88,31);
                    char_literal3=(Token)match(input,37,FOLLOW_37_in_statement188); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_37.add(char_literal3);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}

            dbg.location(88,36);
            EOF4=(Token)match(input,EOF,FOLLOW_EOF_in_statement191); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EOF.add(EOF4);



            // AST REWRITE
            // elements: order_by, query_expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 88:40: -> ^( STATEMENT query_expression ( order_by )? )
            {
                dbg.location(88,43);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:43: ^( STATEMENT query_expression ( order_by )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(88,45);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATEMENT, "STATEMENT"), root_1);

                dbg.location(88,55);
                adaptor.addChild(root_1, stream_query_expression.nextTree());
                dbg.location(88,72);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:88:72: ( order_by )?
                if ( stream_order_by.hasNext() ) {
                    dbg.location(88,72);
                    adaptor.addChild(root_1, stream_order_by.nextTree());

                }
                stream_order_by.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(88, 82);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "statement");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "statement"

    public static class query_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query_expression"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:89:1: query_expression : query ( set_op query )* ;
    public final SQL92QueryParser.query_expression_return query_expression() throws RecognitionException {
        SQL92QueryParser.query_expression_return retval = new SQL92QueryParser.query_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.query_return query5 = null;

        SQL92QueryParser.set_op_return set_op6 = null;

        SQL92QueryParser.query_return query7 = null;



        try { dbg.enterRule(getGrammarFileName(), "query_expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(89, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:91:2: ( query ( set_op query )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:91:4: query ( set_op query )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(91,4);
            pushFollow(FOLLOW_query_in_query_expression211);
            query5=query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, query5.getTree());
            dbg.location(91,10);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:91:10: ( set_op query )*
            try { dbg.enterSubRule(3);

            loop3:
            do {
                int alt3=2;
                try { dbg.enterDecision(3, isCyclicDecision);

                int LA3_0 = input.LA(1);

                if ( (LA3_0==38||(LA3_0>=40 && LA3_0<=41)) ) {
                    alt3=1;
                }


                } finally {dbg.exitDecision(3);}

                switch (alt3) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:91:11: set_op query
            	    {
            	    dbg.location(91,17);
            	    pushFollow(FOLLOW_set_op_in_query_expression214);
            	    set_op6=set_op();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(set_op6.getTree(), root_0);
            	    dbg.location(91,19);
            	    pushFollow(FOLLOW_query_in_query_expression217);
            	    query7=query();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, query7.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);
            } finally {dbg.exitSubRule(3);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(92, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "query_expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "query_expression"

    public static class set_op_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_op"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:96:1: set_op : ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) );
    public final SQL92QueryParser.set_op_return set_op() throws RecognitionException {
        SQL92QueryParser.set_op_return retval = new SQL92QueryParser.set_op_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal8=null;
        Token string_literal9=null;
        Token string_literal10=null;
        Token string_literal11=null;
        Token string_literal12=null;
        Token string_literal13=null;
        Token string_literal14=null;

        CommonTree string_literal8_tree=null;
        CommonTree string_literal9_tree=null;
        CommonTree string_literal10_tree=null;
        CommonTree string_literal11_tree=null;
        CommonTree string_literal12_tree=null;
        CommonTree string_literal13_tree=null;
        CommonTree string_literal14_tree=null;
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
        RewriteRuleTokenStream stream_39=new RewriteRuleTokenStream(adaptor,"token 39");
        RewriteRuleTokenStream stream_38=new RewriteRuleTokenStream(adaptor,"token 38");

        try { dbg.enterRule(getGrammarFileName(), "set_op");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(96, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:96:8: ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) )
            int alt4=5;
            try { dbg.enterDecision(4, isCyclicDecision);

            switch ( input.LA(1) ) {
            case 38:
                {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==39) ) {
                    alt4=1;
                }
                else if ( (LA4_1==42||LA4_1==48) ) {
                    alt4=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case 40:
                {
                int LA4_2 = input.LA(2);

                if ( (LA4_2==39) ) {
                    alt4=3;
                }
                else if ( (LA4_2==42||LA4_2==48) ) {
                    alt4=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case 41:
                {
                alt4=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(4);}

            switch (alt4) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:96:10: 'UNION' 'ALL'
                    {
                    dbg.location(96,10);
                    string_literal8=(Token)match(input,38,FOLLOW_38_in_set_op231); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_38.add(string_literal8);

                    dbg.location(96,18);
                    string_literal9=(Token)match(input,39,FOLLOW_39_in_set_op233); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_39.add(string_literal9);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 96:24: -> ^( UNION_ALL )
                    {
                        dbg.location(96,27);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:96:27: ^( UNION_ALL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(96,29);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION_ALL, "UNION_ALL"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:97:4: 'UNION'
                    {
                    dbg.location(97,4);
                    string_literal10=(Token)match(input,38,FOLLOW_38_in_set_op244); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_38.add(string_literal10);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 97:12: -> ^( UNION )
                    {
                        dbg.location(97,15);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:97:15: ^( UNION )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(97,17);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION, "UNION"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:98:4: 'EXCEPT' 'ALL'
                    {
                    dbg.location(98,4);
                    string_literal11=(Token)match(input,40,FOLLOW_40_in_set_op255); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_40.add(string_literal11);

                    dbg.location(98,13);
                    string_literal12=(Token)match(input,39,FOLLOW_39_in_set_op257); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_39.add(string_literal12);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 98:19: -> ^( EXCEPT_ALL )
                    {
                        dbg.location(98,22);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:98:22: ^( EXCEPT_ALL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(98,24);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT_ALL, "EXCEPT_ALL"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:99:4: 'EXCEPT'
                    {
                    dbg.location(99,4);
                    string_literal13=(Token)match(input,40,FOLLOW_40_in_set_op268); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_40.add(string_literal13);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 99:13: -> ^( EXCEPT )
                    {
                        dbg.location(99,16);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:99:16: ^( EXCEPT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(99,18);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT, "EXCEPT"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:100:4: 'INTERSECT'
                    {
                    dbg.location(100,4);
                    string_literal14=(Token)match(input,41,FOLLOW_41_in_set_op279); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_41.add(string_literal14);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 100:16: -> ^( INTERSECT )
                    {
                        dbg.location(100,19);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:100:19: ^( INTERSECT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(100,21);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTERSECT, "INTERSECT"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(101, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "set_op");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "set_op"

    public static class query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:104:1: query : ( sub_query | 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) );
    public final SQL92QueryParser.query_return query() throws RecognitionException {
        SQL92QueryParser.query_return retval = new SQL92QueryParser.query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal16=null;
        Token string_literal19=null;
        Token string_literal21=null;
        Token string_literal22=null;
        Token string_literal24=null;
        SQL92QueryParser.search_condition_return s1 = null;

        SQL92QueryParser.search_condition_return s2 = null;

        SQL92QueryParser.sub_query_return sub_query15 = null;

        SQL92QueryParser.set_quantifier_return set_quantifier17 = null;

        SQL92QueryParser.select_list_return select_list18 = null;

        SQL92QueryParser.table_expression_return table_expression20 = null;

        SQL92QueryParser.column_list_return column_list23 = null;


        CommonTree string_literal16_tree=null;
        CommonTree string_literal19_tree=null;
        CommonTree string_literal21_tree=null;
        CommonTree string_literal22_tree=null;
        CommonTree string_literal24_tree=null;
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
        RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleSubtreeStream stream_select_list=new RewriteRuleSubtreeStream(adaptor,"rule select_list");
        RewriteRuleSubtreeStream stream_set_quantifier=new RewriteRuleSubtreeStream(adaptor,"rule set_quantifier");
        RewriteRuleSubtreeStream stream_column_list=new RewriteRuleSubtreeStream(adaptor,"rule column_list");
        RewriteRuleSubtreeStream stream_search_condition=new RewriteRuleSubtreeStream(adaptor,"rule search_condition");
        RewriteRuleSubtreeStream stream_table_expression=new RewriteRuleSubtreeStream(adaptor,"rule table_expression");
        try { dbg.enterRule(getGrammarFileName(), "query");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(104, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:105:2: ( sub_query | 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) )
            int alt9=2;
            try { dbg.enterDecision(9, isCyclicDecision);

            int LA9_0 = input.LA(1);

            if ( (LA9_0==48) ) {
                alt9=1;
            }
            else if ( (LA9_0==42) ) {
                alt9=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(9);}

            switch (alt9) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:105:4: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(105,4);
                    pushFollow(FOLLOW_sub_query_in_query300);
                    sub_query15=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query15.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:4: 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )?
                    {
                    dbg.location(106,4);
                    string_literal16=(Token)match(input,42,FOLLOW_42_in_query305); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_42.add(string_literal16);

                    dbg.location(106,13);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:13: ( set_quantifier )?
                    int alt5=2;
                    try { dbg.enterSubRule(5);
                    try { dbg.enterDecision(5, isCyclicDecision);

                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==39||LA5_0==47) ) {
                        alt5=1;
                    }
                    } finally {dbg.exitDecision(5);}

                    switch (alt5) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: set_quantifier
                            {
                            dbg.location(106,13);
                            pushFollow(FOLLOW_set_quantifier_in_query307);
                            set_quantifier17=set_quantifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_set_quantifier.add(set_quantifier17.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(5);}

                    dbg.location(106,29);
                    pushFollow(FOLLOW_select_list_in_query310);
                    select_list18=select_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_list.add(select_list18.getTree());
                    dbg.location(106,41);
                    string_literal19=(Token)match(input,43,FOLLOW_43_in_query312); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_43.add(string_literal19);

                    dbg.location(106,48);
                    pushFollow(FOLLOW_table_expression_in_query314);
                    table_expression20=table_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_expression.add(table_expression20.getTree());
                    dbg.location(106,65);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:65: ( 'WHERE' s1= search_condition )?
                    int alt6=2;
                    try { dbg.enterSubRule(6);
                    try { dbg.enterDecision(6, isCyclicDecision);

                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==44) ) {
                        alt6=1;
                    }
                    } finally {dbg.exitDecision(6);}

                    switch (alt6) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:66: 'WHERE' s1= search_condition
                            {
                            dbg.location(106,66);
                            string_literal21=(Token)match(input,44,FOLLOW_44_in_query317); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_44.add(string_literal21);

                            dbg.location(106,76);
                            pushFollow(FOLLOW_search_condition_in_query321);
                            s1=search_condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_search_condition.add(s1.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(6);}

                    dbg.location(106,96);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:96: ( 'GROUP BY' column_list )?
                    int alt7=2;
                    try { dbg.enterSubRule(7);
                    try { dbg.enterDecision(7, isCyclicDecision);

                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==45) ) {
                        alt7=1;
                    }
                    } finally {dbg.exitDecision(7);}

                    switch (alt7) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:97: 'GROUP BY' column_list
                            {
                            dbg.location(106,97);
                            string_literal22=(Token)match(input,45,FOLLOW_45_in_query326); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_45.add(string_literal22);

                            dbg.location(106,108);
                            pushFollow(FOLLOW_column_list_in_query328);
                            column_list23=column_list();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_column_list.add(column_list23.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(7);}

                    dbg.location(106,122);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:122: ( 'HAVING' s2= search_condition )?
                    int alt8=2;
                    try { dbg.enterSubRule(8);
                    try { dbg.enterDecision(8, isCyclicDecision);

                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==46) ) {
                        alt8=1;
                    }
                    } finally {dbg.exitDecision(8);}

                    switch (alt8) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:106:123: 'HAVING' s2= search_condition
                            {
                            dbg.location(106,123);
                            string_literal24=(Token)match(input,46,FOLLOW_46_in_query333); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_46.add(string_literal24);

                            dbg.location(106,134);
                            pushFollow(FOLLOW_search_condition_in_query337);
                            s2=search_condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_search_condition.add(s2.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(8);}



                    // AST REWRITE
                    // elements: s1, select_list, set_quantifier, table_expression, s2, column_list
                    // token labels: 
                    // rule labels: retval, s2, s1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_s2=new RewriteRuleSubtreeStream(adaptor,"rule s2",s2!=null?s2.tree:null);
                    RewriteRuleSubtreeStream stream_s1=new RewriteRuleSubtreeStream(adaptor,"rule s1",s1!=null?s1.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 107:5: -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
                    {
                        dbg.location(107,8);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:8: ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(107,10);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUERY, "QUERY"), root_1);

                        dbg.location(107,16);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:16: ^( SELECT_LIST ( set_quantifier )? select_list )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(107,18);
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELECT_LIST, "SELECT_LIST"), root_2);

                        dbg.location(107,30);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:30: ( set_quantifier )?
                        if ( stream_set_quantifier.hasNext() ) {
                            dbg.location(107,30);
                            adaptor.addChild(root_2, stream_set_quantifier.nextTree());

                        }
                        stream_set_quantifier.reset();
                        dbg.location(107,46);
                        adaptor.addChild(root_2, stream_select_list.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(107,59);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:59: ^( FROM_LIST table_expression )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(107,61);
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FROM_LIST, "FROM_LIST"), root_2);

                        dbg.location(107,71);
                        adaptor.addChild(root_2, stream_table_expression.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(107,89);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:89: ( ^( WHERE $s1) )?
                        if ( stream_s1.hasNext() ) {
                            dbg.location(107,89);
                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:89: ^( WHERE $s1)
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            dbg.location(107,91);
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHERE, "WHERE"), root_2);

                            dbg.location(107,97);
                            adaptor.addChild(root_2, stream_s1.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_s1.reset();
                        dbg.location(107,103);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:103: ( ^( GROUP_BY column_list ) )?
                        if ( stream_column_list.hasNext() ) {
                            dbg.location(107,103);
                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:103: ^( GROUP_BY column_list )
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            dbg.location(107,105);
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(GROUP_BY, "GROUP_BY"), root_2);

                            dbg.location(107,114);
                            adaptor.addChild(root_2, stream_column_list.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_column_list.reset();
                        dbg.location(107,128);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:128: ( ^( HAVING $s2) )?
                        if ( stream_s2.hasNext() ) {
                            dbg.location(107,128);
                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:107:128: ^( HAVING $s2)
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            dbg.location(107,130);
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HAVING, "HAVING"), root_2);

                            dbg.location(107,137);
                            adaptor.addChild(root_2, stream_s2.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_s2.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(108, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "query");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "query"

    public static class set_quantifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_quantifier"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:109:1: set_quantifier : ( 'DISTINCT' | 'ALL' );
    public final SQL92QueryParser.set_quantifier_return set_quantifier() throws RecognitionException {
        SQL92QueryParser.set_quantifier_return retval = new SQL92QueryParser.set_quantifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set25=null;

        CommonTree set25_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "set_quantifier");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(109, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:110:2: ( 'DISTINCT' | 'ALL' )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(110,2);
            set25=(Token)input.LT(1);
            if ( input.LA(1)==39||input.LA(1)==47 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set25));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                dbg.recognitionException(mse);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(110, 22);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "set_quantifier");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "set_quantifier"

    public static class sub_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sub_query"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:111:1: sub_query : '(' query_expression ')' ;
    public final SQL92QueryParser.sub_query_return sub_query() throws RecognitionException {
        SQL92QueryParser.sub_query_return retval = new SQL92QueryParser.sub_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal26=null;
        Token char_literal28=null;
        SQL92QueryParser.query_expression_return query_expression27 = null;


        CommonTree char_literal26_tree=null;
        CommonTree char_literal28_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "sub_query");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(111, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:112:2: ( '(' query_expression ')' )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:112:4: '(' query_expression ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(112,7);
            char_literal26=(Token)match(input,48,FOLLOW_48_in_sub_query409); if (state.failed) return retval;
            dbg.location(112,9);
            pushFollow(FOLLOW_query_expression_in_sub_query412);
            query_expression27=query_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, query_expression27.getTree());
            dbg.location(112,29);
            char_literal28=(Token)match(input,49,FOLLOW_49_in_sub_query414); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(112, 30);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "sub_query");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "sub_query"

    public static class select_list_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "select_list"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:114:1: select_list : ( '*' -> ^( COLUMN '*' ) | derived_column ( ',' derived_column )* );
    public final SQL92QueryParser.select_list_return select_list() throws RecognitionException {
        SQL92QueryParser.select_list_return retval = new SQL92QueryParser.select_list_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal29=null;
        Token char_literal31=null;
        SQL92QueryParser.derived_column_return derived_column30 = null;

        SQL92QueryParser.derived_column_return derived_column32 = null;


        CommonTree char_literal29_tree=null;
        CommonTree char_literal31_tree=null;
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");

        try { dbg.enterRule(getGrammarFileName(), "select_list");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(114, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:115:2: ( '*' -> ^( COLUMN '*' ) | derived_column ( ',' derived_column )* )
            int alt11=2;
            try { dbg.enterDecision(11, isCyclicDecision);

            int LA11_0 = input.LA(1);

            if ( (LA11_0==50) ) {
                alt11=1;
            }
            else if ( ((LA11_0>=ID && LA11_0<=STRING)||LA11_0==48||LA11_0==52||(LA11_0>=57 && LA11_0<=68)||(LA11_0>=70 && LA11_0<=72)) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(11);}

            switch (alt11) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:115:4: '*'
                    {
                    dbg.location(115,4);
                    char_literal29=(Token)match(input,50,FOLLOW_50_in_select_list424); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_50.add(char_literal29);



                    // AST REWRITE
                    // elements: 50
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 115:8: -> ^( COLUMN '*' )
                    {
                        dbg.location(115,11);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:115:11: ^( COLUMN '*' )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(115,13);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        dbg.location(115,20);
                        adaptor.addChild(root_1, stream_50.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:116:5: derived_column ( ',' derived_column )*
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(116,5);
                    pushFollow(FOLLOW_derived_column_in_select_list438);
                    derived_column30=derived_column();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column30.getTree());
                    dbg.location(116,20);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:116:20: ( ',' derived_column )*
                    try { dbg.enterSubRule(10);

                    loop10:
                    do {
                        int alt10=2;
                        try { dbg.enterDecision(10, isCyclicDecision);

                        int LA10_0 = input.LA(1);

                        if ( (LA10_0==51) ) {
                            alt10=1;
                        }


                        } finally {dbg.exitDecision(10);}

                        switch (alt10) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:116:21: ',' derived_column
                    	    {
                    	    dbg.location(116,24);
                    	    char_literal31=(Token)match(input,51,FOLLOW_51_in_select_list441); if (state.failed) return retval;
                    	    dbg.location(116,26);
                    	    pushFollow(FOLLOW_derived_column_in_select_list444);
                    	    derived_column32=derived_column();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column32.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(10);}


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(116, 42);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "select_list");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "select_list"

    public static class derived_column_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "derived_column"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:118:1: derived_column : ( 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) );
    public final SQL92QueryParser.derived_column_return derived_column() throws RecognitionException {
        SQL92QueryParser.derived_column_return retval = new SQL92QueryParser.derived_column_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token id1=null;
        Token id2=null;
        Token string_literal33=null;
        Token string_literal35=null;
        Token string_literal36=null;
        Token string_literal38=null;
        Token ID39=null;
        SQL92QueryParser.value_expression_return value_expression34 = null;

        SQL92QueryParser.value_expression_return value_expression37 = null;


        CommonTree id1_tree=null;
        CommonTree id2_tree=null;
        CommonTree string_literal33_tree=null;
        CommonTree string_literal35_tree=null;
        CommonTree string_literal36_tree=null;
        CommonTree string_literal38_tree=null;
        CommonTree ID39_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try { dbg.enterRule(getGrammarFileName(), "derived_column");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(118, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:2: ( 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) )
            int alt16=2;
            try { dbg.enterDecision(16, isCyclicDecision);

            int LA16_0 = input.LA(1);

            if ( (LA16_0==52) ) {
                alt16=1;
            }
            else if ( ((LA16_0>=ID && LA16_0<=STRING)||LA16_0==48||(LA16_0>=57 && LA16_0<=68)||(LA16_0>=70 && LA16_0<=72)) ) {
                alt16=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(16);}

            switch (alt16) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:4: 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )?
                    {
                    dbg.location(119,4);
                    string_literal33=(Token)match(input,52,FOLLOW_52_in_derived_column456); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_52.add(string_literal33);

                    dbg.location(119,11);
                    pushFollow(FOLLOW_value_expression_in_derived_column458);
                    value_expression34=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression34.getTree());
                    dbg.location(119,28);
                    string_literal35=(Token)match(input,53,FOLLOW_53_in_derived_column460); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(string_literal35);

                    dbg.location(119,36);
                    id1=(Token)match(input,ID,FOLLOW_ID_in_derived_column464); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id1);

                    dbg.location(119,40);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:40: ( ( 'AS' )? id2= ID )?
                    int alt13=2;
                    try { dbg.enterSubRule(13);
                    try { dbg.enterDecision(13, isCyclicDecision);

                    int LA13_0 = input.LA(1);

                    if ( (LA13_0==ID||LA13_0==53) ) {
                        alt13=1;
                    }
                    } finally {dbg.exitDecision(13);}

                    switch (alt13) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:41: ( 'AS' )? id2= ID
                            {
                            dbg.location(119,41);
                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:41: ( 'AS' )?
                            int alt12=2;
                            try { dbg.enterSubRule(12);
                            try { dbg.enterDecision(12, isCyclicDecision);

                            int LA12_0 = input.LA(1);

                            if ( (LA12_0==53) ) {
                                alt12=1;
                            }
                            } finally {dbg.exitDecision(12);}

                            switch (alt12) {
                                case 1 :
                                    dbg.enterAlt(1);

                                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'AS'
                                    {
                                    dbg.location(119,41);
                                    string_literal36=(Token)match(input,53,FOLLOW_53_in_derived_column467); if (state.failed) return retval; 
                                    if ( state.backtracking==0 ) stream_53.add(string_literal36);


                                    }
                                    break;

                            }
                            } finally {dbg.exitSubRule(12);}

                            dbg.location(119,50);
                            id2=(Token)match(input,ID,FOLLOW_ID_in_derived_column472); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(id2);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(13);}



                    // AST REWRITE
                    // elements: id2, id1, value_expression
                    // token labels: id2, id1
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_id2=new RewriteRuleTokenStream(adaptor,"token id2",id2);
                    RewriteRuleTokenStream stream_id1=new RewriteRuleTokenStream(adaptor,"token id1",id1);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 119:56: -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
                    {
                        dbg.location(119,59);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:59: ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(119,61);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        dbg.location(119,68);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:68: ^( CAST value_expression $id1)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(119,70);
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CAST, "CAST"), root_2);

                        dbg.location(119,75);
                        adaptor.addChild(root_2, stream_value_expression.nextTree());
                        dbg.location(119,92);
                        adaptor.addChild(root_2, stream_id1.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        dbg.location(119,98);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:119:98: ( $id2)?
                        if ( stream_id2.hasNext() ) {
                            dbg.location(119,98);
                            adaptor.addChild(root_1, stream_id2.nextNode());

                        }
                        stream_id2.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:4: value_expression ( ( 'AS' )? ID )?
                    {
                    dbg.location(120,4);
                    pushFollow(FOLLOW_value_expression_in_derived_column498);
                    value_expression37=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression37.getTree());
                    dbg.location(120,21);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:21: ( ( 'AS' )? ID )?
                    int alt15=2;
                    try { dbg.enterSubRule(15);
                    try { dbg.enterDecision(15, isCyclicDecision);

                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==ID||LA15_0==53) ) {
                        alt15=1;
                    }
                    } finally {dbg.exitDecision(15);}

                    switch (alt15) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:22: ( 'AS' )? ID
                            {
                            dbg.location(120,22);
                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:22: ( 'AS' )?
                            int alt14=2;
                            try { dbg.enterSubRule(14);
                            try { dbg.enterDecision(14, isCyclicDecision);

                            int LA14_0 = input.LA(1);

                            if ( (LA14_0==53) ) {
                                alt14=1;
                            }
                            } finally {dbg.exitDecision(14);}

                            switch (alt14) {
                                case 1 :
                                    dbg.enterAlt(1);

                                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'AS'
                                    {
                                    dbg.location(120,22);
                                    string_literal38=(Token)match(input,53,FOLLOW_53_in_derived_column501); if (state.failed) return retval; 
                                    if ( state.backtracking==0 ) stream_53.add(string_literal38);


                                    }
                                    break;

                            }
                            } finally {dbg.exitSubRule(14);}

                            dbg.location(120,28);
                            ID39=(Token)match(input,ID,FOLLOW_ID_in_derived_column504); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(ID39);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(15);}



                    // AST REWRITE
                    // elements: value_expression, ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 120:33: -> ^( COLUMN value_expression ( ID )? )
                    {
                        dbg.location(120,36);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:36: ^( COLUMN value_expression ( ID )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(120,38);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        dbg.location(120,45);
                        adaptor.addChild(root_1, stream_value_expression.nextTree());
                        dbg.location(120,62);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:120:62: ( ID )?
                        if ( stream_ID.hasNext() ) {
                            dbg.location(120,62);
                            adaptor.addChild(root_1, stream_ID.nextNode());

                        }
                        stream_ID.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(121, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "derived_column");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "derived_column"

    public static class order_by_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "order_by"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:123:1: order_by : 'ORDER' 'BY' sort_specification ( ',' sort_specification )* -> ^( ORDER ( sort_specification )+ ) ;
    public final SQL92QueryParser.order_by_return order_by() throws RecognitionException {
        SQL92QueryParser.order_by_return retval = new SQL92QueryParser.order_by_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal40=null;
        Token string_literal41=null;
        Token char_literal43=null;
        SQL92QueryParser.sort_specification_return sort_specification42 = null;

        SQL92QueryParser.sort_specification_return sort_specification44 = null;


        CommonTree string_literal40_tree=null;
        CommonTree string_literal41_tree=null;
        CommonTree char_literal43_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_sort_specification=new RewriteRuleSubtreeStream(adaptor,"rule sort_specification");
        try { dbg.enterRule(getGrammarFileName(), "order_by");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(123, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:124:2: ( 'ORDER' 'BY' sort_specification ( ',' sort_specification )* -> ^( ORDER ( sort_specification )+ ) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:124:4: 'ORDER' 'BY' sort_specification ( ',' sort_specification )*
            {
            dbg.location(124,4);
            string_literal40=(Token)match(input,54,FOLLOW_54_in_order_by529); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_54.add(string_literal40);

            dbg.location(124,12);
            string_literal41=(Token)match(input,55,FOLLOW_55_in_order_by531); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_55.add(string_literal41);

            dbg.location(124,17);
            pushFollow(FOLLOW_sort_specification_in_order_by533);
            sort_specification42=sort_specification();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_sort_specification.add(sort_specification42.getTree());
            dbg.location(124,36);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:124:36: ( ',' sort_specification )*
            try { dbg.enterSubRule(17);

            loop17:
            do {
                int alt17=2;
                try { dbg.enterDecision(17, isCyclicDecision);

                int LA17_0 = input.LA(1);

                if ( (LA17_0==51) ) {
                    alt17=1;
                }


                } finally {dbg.exitDecision(17);}

                switch (alt17) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:124:37: ',' sort_specification
            	    {
            	    dbg.location(124,37);
            	    char_literal43=(Token)match(input,51,FOLLOW_51_in_order_by536); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_51.add(char_literal43);

            	    dbg.location(124,41);
            	    pushFollow(FOLLOW_sort_specification_in_order_by538);
            	    sort_specification44=sort_specification();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_sort_specification.add(sort_specification44.getTree());

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);
            } finally {dbg.exitSubRule(17);}



            // AST REWRITE
            // elements: sort_specification
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 124:62: -> ^( ORDER ( sort_specification )+ )
            {
                dbg.location(124,65);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:124:65: ^( ORDER ( sort_specification )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(124,67);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ORDER, "ORDER"), root_1);

                dbg.location(124,73);
                if ( !(stream_sort_specification.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_sort_specification.hasNext() ) {
                    dbg.location(124,73);
                    adaptor.addChild(root_1, stream_sort_specification.nextTree());

                }
                stream_sort_specification.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(124, 93);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "order_by");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "order_by"

    public static class sort_specification_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sort_specification"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:125:1: sort_specification : ( column_name | INT | reserved_word_column_name );
    public final SQL92QueryParser.sort_specification_return sort_specification() throws RecognitionException {
        SQL92QueryParser.sort_specification_return retval = new SQL92QueryParser.sort_specification_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT46=null;
        SQL92QueryParser.column_name_return column_name45 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name47 = null;


        CommonTree INT46_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "sort_specification");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(125, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:126:2: ( column_name | INT | reserved_word_column_name )
            int alt18=3;
            try { dbg.enterDecision(18, isCyclicDecision);

            switch ( input.LA(1) ) {
            case ID:
                {
                int LA18_1 = input.LA(2);

                if ( (LA18_1==56) ) {
                    int LA18_4 = input.LA(3);

                    if ( ((LA18_4>=57 && LA18_4<=66)) ) {
                        alt18=3;
                    }
                    else if ( (LA18_4==ID) ) {
                        alt18=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 4, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else if ( (LA18_1==EOF||LA18_1==37||LA18_1==51) ) {
                    alt18=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case INT:
                {
                alt18=2;
                }
                break;
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
                {
                alt18=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(18);}

            switch (alt18) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:126:4: column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(126,4);
                    pushFollow(FOLLOW_column_name_in_sort_specification557);
                    column_name45=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name45.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:126:18: INT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(126,18);
                    INT46=(Token)match(input,INT,FOLLOW_INT_in_sort_specification561); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT46_tree = (CommonTree)adaptor.create(INT46);
                    adaptor.addChild(root_0, INT46_tree);
                    }

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:126:24: reserved_word_column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(126,24);
                    pushFollow(FOLLOW_reserved_word_column_name_in_sort_specification565);
                    reserved_word_column_name47=reserved_word_column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name47.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(127, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "sort_specification");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "sort_specification"

    public static class reserved_word_column_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "reserved_word_column_name"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:129:1: reserved_word_column_name : (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) ;
    public final SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name() throws RecognitionException {
        SQL92QueryParser.reserved_word_column_name_return retval = new SQL92QueryParser.reserved_word_column_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token char_literal48=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree char_literal48_tree=null;
        RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
        RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");

        try { dbg.enterRule(getGrammarFileName(), "reserved_word_column_name");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(129, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:2: ( (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:4: (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
            {
            dbg.location(130,4);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:4: (tableid= ID '.' )?
            int alt19=2;
            try { dbg.enterSubRule(19);
            try { dbg.enterDecision(19, isCyclicDecision);

            int LA19_0 = input.LA(1);

            if ( (LA19_0==ID) ) {
                alt19=1;
            }
            } finally {dbg.exitDecision(19);}

            switch (alt19) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:5: tableid= ID '.'
                    {
                    dbg.location(130,12);
                    tableid=(Token)match(input,ID,FOLLOW_ID_in_reserved_word_column_name579); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(tableid);

                    dbg.location(130,15);
                    char_literal48=(Token)match(input,56,FOLLOW_56_in_reserved_word_column_name580); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_56.add(char_literal48);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(19);}

            dbg.location(130,20);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:20: (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
            int alt20=10;
            try { dbg.enterSubRule(20);
            try { dbg.enterDecision(20, isCyclicDecision);

            switch ( input.LA(1) ) {
            case 57:
                {
                alt20=1;
                }
                break;
            case 58:
                {
                alt20=2;
                }
                break;
            case 59:
                {
                alt20=3;
                }
                break;
            case 60:
                {
                alt20=4;
                }
                break;
            case 61:
                {
                alt20=5;
                }
                break;
            case 62:
                {
                alt20=6;
                }
                break;
            case 63:
                {
                alt20=7;
                }
                break;
            case 64:
                {
                alt20=8;
                }
                break;
            case 65:
                {
                alt20=9;
                }
                break;
            case 66:
                {
                alt20=10;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(20);}

            switch (alt20) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:21: s= 'DATE'
                    {
                    dbg.location(130,22);
                    s=(Token)match(input,57,FOLLOW_57_in_reserved_word_column_name586); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_57.add(s);


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:32: s= 'TIMESTAMP'
                    {
                    dbg.location(130,33);
                    s=(Token)match(input,58,FOLLOW_58_in_reserved_word_column_name592); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_58.add(s);


                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:48: s= 'TIME'
                    {
                    dbg.location(130,49);
                    s=(Token)match(input,59,FOLLOW_59_in_reserved_word_column_name598); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_59.add(s);


                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:59: s= 'INTERVAL'
                    {
                    dbg.location(130,60);
                    s=(Token)match(input,60,FOLLOW_60_in_reserved_word_column_name604); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_60.add(s);


                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:74: s= 'YEAR'
                    {
                    dbg.location(130,75);
                    s=(Token)match(input,61,FOLLOW_61_in_reserved_word_column_name610); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_61.add(s);


                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:85: s= 'MONTH'
                    {
                    dbg.location(130,86);
                    s=(Token)match(input,62,FOLLOW_62_in_reserved_word_column_name616); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_62.add(s);


                    }
                    break;
                case 7 :
                    dbg.enterAlt(7);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:97: s= 'DAY'
                    {
                    dbg.location(130,98);
                    s=(Token)match(input,63,FOLLOW_63_in_reserved_word_column_name622); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_63.add(s);


                    }
                    break;
                case 8 :
                    dbg.enterAlt(8);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:107: s= 'HOUR'
                    {
                    dbg.location(130,108);
                    s=(Token)match(input,64,FOLLOW_64_in_reserved_word_column_name628); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_64.add(s);


                    }
                    break;
                case 9 :
                    dbg.enterAlt(9);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:118: s= 'MINUTE'
                    {
                    dbg.location(130,119);
                    s=(Token)match(input,65,FOLLOW_65_in_reserved_word_column_name634); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_65.add(s);


                    }
                    break;
                case 10 :
                    dbg.enterAlt(10);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:130:131: s= 'SECOND'
                    {
                    dbg.location(130,132);
                    s=(Token)match(input,66,FOLLOW_66_in_reserved_word_column_name640); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_66.add(s);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(20);}



            // AST REWRITE
            // elements: tableid, s
            // token labels: tableid, s
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 131:4: -> ^( TABLECOLUMN ( $tableid)? $s)
            {
                dbg.location(131,7);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:131:7: ^( TABLECOLUMN ( $tableid)? $s)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(131,9);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                dbg.location(131,21);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:131:21: ( $tableid)?
                if ( stream_tableid.hasNext() ) {
                    dbg.location(131,21);
                    adaptor.addChild(root_1, stream_tableid.nextNode());

                }
                stream_tableid.reset();
                dbg.location(131,31);
                adaptor.addChild(root_1, stream_s.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(132, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "reserved_word_column_name");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "reserved_word_column_name"

    public static class value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value_expression"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:134:1: value_expression : ( string_value_expression | numeric_value_expression );
    public final SQL92QueryParser.value_expression_return value_expression() throws RecognitionException {
        SQL92QueryParser.value_expression_return retval = new SQL92QueryParser.value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.string_value_expression_return string_value_expression49 = null;

        SQL92QueryParser.numeric_value_expression_return numeric_value_expression50 = null;



        try { dbg.enterRule(getGrammarFileName(), "value_expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(134, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:135:2: ( string_value_expression | numeric_value_expression )
            int alt21=2;
            try { dbg.enterDecision(21, isCyclicDecision);

            switch ( input.LA(1) ) {
            case ID:
                {
                switch ( input.LA(2) ) {
                case 56:
                    {
                    int LA21_4 = input.LA(3);

                    if ( ((LA21_4>=57 && LA21_4<=66)) ) {
                        alt21=2;
                    }
                    else if ( (LA21_4==ID) ) {
                        int LA21_6 = input.LA(4);

                        if ( (LA21_6==73) ) {
                            alt21=1;
                        }
                        else if ( (LA21_6==EOF||(LA21_6>=ID && LA21_6<=STRING)||(LA21_6>=37 && LA21_6<=38)||(LA21_6>=40 && LA21_6<=41)||(LA21_6>=43 && LA21_6<=46)||(LA21_6>=48 && LA21_6<=51)||(LA21_6>=53 && LA21_6<=54)||(LA21_6>=57 && LA21_6<=72)||LA21_6==74||(LA21_6>=76 && LA21_6<=79)||(LA21_6>=81 && LA21_6<=94)||(LA21_6>=97 && LA21_6<=99)) ) {
                            alt21=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 6, input);

                            dbg.recognitionException(nvae);
                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 4, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case ID:
                case INT:
                case FLOAT:
                case NUMERIC:
                case STRING:
                case 37:
                case 38:
                case 40:
                case 41:
                case 43:
                case 44:
                case 45:
                case 46:
                case 48:
                case 49:
                case 50:
                case 51:
                case 53:
                case 54:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 74:
                case 76:
                case 77:
                case 78:
                case 79:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 97:
                case 98:
                case 99:
                    {
                    alt21=2;
                    }
                    break;
                case 73:
                    {
                    alt21=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }

                }
                break;
            case STRING:
                {
                int LA21_2 = input.LA(2);

                if ( (LA21_2==73) ) {
                    alt21=1;
                }
                else if ( (LA21_2==EOF||(LA21_2>=ID && LA21_2<=STRING)||(LA21_2>=37 && LA21_2<=38)||(LA21_2>=40 && LA21_2<=41)||(LA21_2>=43 && LA21_2<=46)||(LA21_2>=48 && LA21_2<=51)||(LA21_2>=53 && LA21_2<=54)||(LA21_2>=57 && LA21_2<=72)||LA21_2==74||(LA21_2>=76 && LA21_2<=79)||(LA21_2>=81 && LA21_2<=94)||(LA21_2>=97 && LA21_2<=99)) ) {
                    alt21=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case INT:
            case FLOAT:
            case NUMERIC:
            case 48:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 70:
            case 71:
            case 72:
                {
                alt21=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(21);}

            switch (alt21) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:135:4: string_value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(135,4);
                    pushFollow(FOLLOW_string_value_expression_in_value_expression671);
                    string_value_expression49=string_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_value_expression49.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:136:4: numeric_value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(136,4);
                    pushFollow(FOLLOW_numeric_value_expression_in_value_expression676);
                    numeric_value_expression50=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_value_expression50.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(137, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "value_expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "value_expression"

    public static class numeric_value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_value_expression"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:138:1: numeric_value_expression : factor ( ( '+' | '-' ) factor )* ;
    public final SQL92QueryParser.numeric_value_expression_return numeric_value_expression() throws RecognitionException {
        SQL92QueryParser.numeric_value_expression_return retval = new SQL92QueryParser.numeric_value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set52=null;
        SQL92QueryParser.factor_return factor51 = null;

        SQL92QueryParser.factor_return factor53 = null;


        CommonTree set52_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "numeric_value_expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(138, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:2: ( factor ( ( '+' | '-' ) factor )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:5: factor ( ( '+' | '-' ) factor )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(139,5);
            pushFollow(FOLLOW_factor_in_numeric_value_expression688);
            factor51=factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, factor51.getTree());
            dbg.location(139,12);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:12: ( ( '+' | '-' ) factor )*
            try { dbg.enterSubRule(22);

            loop22:
            do {
                int alt22=2;
                try { dbg.enterDecision(22, isCyclicDecision);

                try {
                    isCyclicDecision = true;
                    alt22 = dfa22.predict(input);
                }
                catch (NoViableAltException nvae) {
                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                } finally {dbg.exitDecision(22);}

                switch (alt22) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:13: ( '+' | '-' ) factor
            	    {
            	    dbg.location(139,13);
            	    set52=(Token)input.LT(1);
            	    set52=(Token)input.LT(1);
            	    if ( (input.LA(1)>=67 && input.LA(1)<=68) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set52), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(139,24);
            	    pushFollow(FOLLOW_factor_in_numeric_value_expression698);
            	    factor53=factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, factor53.getTree());

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);
            } finally {dbg.exitSubRule(22);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(139, 33);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "numeric_value_expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "numeric_value_expression"

    public static class factor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "factor"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:141:1: factor : numeric_primary ( ( '*' | '/' ) numeric_primary )* ;
    public final SQL92QueryParser.factor_return factor() throws RecognitionException {
        SQL92QueryParser.factor_return retval = new SQL92QueryParser.factor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set55=null;
        SQL92QueryParser.numeric_primary_return numeric_primary54 = null;

        SQL92QueryParser.numeric_primary_return numeric_primary56 = null;


        CommonTree set55_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "factor");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(141, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:141:8: ( numeric_primary ( ( '*' | '/' ) numeric_primary )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:141:10: numeric_primary ( ( '*' | '/' ) numeric_primary )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(141,10);
            pushFollow(FOLLOW_numeric_primary_in_factor710);
            numeric_primary54=numeric_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary54.getTree());
            dbg.location(141,26);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:141:26: ( ( '*' | '/' ) numeric_primary )*
            try { dbg.enterSubRule(23);

            loop23:
            do {
                int alt23=2;
                try { dbg.enterDecision(23, isCyclicDecision);

                int LA23_0 = input.LA(1);

                if ( (LA23_0==50||LA23_0==69) ) {
                    alt23=1;
                }


                } finally {dbg.exitDecision(23);}

                switch (alt23) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:141:27: ( '*' | '/' ) numeric_primary
            	    {
            	    dbg.location(141,27);
            	    set55=(Token)input.LT(1);
            	    set55=(Token)input.LT(1);
            	    if ( input.LA(1)==50||input.LA(1)==69 ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set55), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        dbg.recognitionException(mse);
            	        throw mse;
            	    }

            	    dbg.location(141,38);
            	    pushFollow(FOLLOW_numeric_primary_in_factor720);
            	    numeric_primary56=numeric_primary();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary56.getTree());

            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);
            } finally {dbg.exitSubRule(23);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(141, 55);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "factor");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "factor"

    public static class numeric_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_primary"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:143:1: numeric_primary : ( '+' | '-' )? value_expression_primary ;
    public final SQL92QueryParser.numeric_primary_return numeric_primary() throws RecognitionException {
        SQL92QueryParser.numeric_primary_return retval = new SQL92QueryParser.numeric_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal57=null;
        Token char_literal58=null;
        SQL92QueryParser.value_expression_primary_return value_expression_primary59 = null;


        CommonTree char_literal57_tree=null;
        CommonTree char_literal58_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "numeric_primary");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(143, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:144:2: ( ( '+' | '-' )? value_expression_primary )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:144:5: ( '+' | '-' )? value_expression_primary
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(144,5);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:144:5: ( '+' | '-' )?
            int alt24=3;
            try { dbg.enterSubRule(24);
            try { dbg.enterDecision(24, isCyclicDecision);

            int LA24_0 = input.LA(1);

            if ( (LA24_0==67) ) {
                alt24=1;
            }
            else if ( (LA24_0==68) ) {
                alt24=2;
            }
            } finally {dbg.exitDecision(24);}

            switch (alt24) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:144:6: '+'
                    {
                    dbg.location(144,9);
                    char_literal57=(Token)match(input,67,FOLLOW_67_in_numeric_primary733); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal57_tree = (CommonTree)adaptor.create(char_literal57);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal57_tree, root_0);
                    }

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:144:11: '-'
                    {
                    dbg.location(144,14);
                    char_literal58=(Token)match(input,68,FOLLOW_68_in_numeric_primary736); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal58_tree = (CommonTree)adaptor.create(char_literal58);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal58_tree, root_0);
                    }

                    }
                    break;

            }
            } finally {dbg.exitSubRule(24);}

            dbg.location(144,18);
            pushFollow(FOLLOW_value_expression_primary_in_numeric_primary741);
            value_expression_primary59=value_expression_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression_primary59.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(144, 42);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "numeric_primary");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "numeric_primary"

    public static class value_expression_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value_expression_primary"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:145:1: value_expression_primary : ( '(' value_expression ')' | function | column_name | literal | sub_query );
    public final SQL92QueryParser.value_expression_primary_return value_expression_primary() throws RecognitionException {
        SQL92QueryParser.value_expression_primary_return retval = new SQL92QueryParser.value_expression_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal60=null;
        Token char_literal62=null;
        SQL92QueryParser.value_expression_return value_expression61 = null;

        SQL92QueryParser.function_return function63 = null;

        SQL92QueryParser.column_name_return column_name64 = null;

        SQL92QueryParser.literal_return literal65 = null;

        SQL92QueryParser.sub_query_return sub_query66 = null;


        CommonTree char_literal60_tree=null;
        CommonTree char_literal62_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "value_expression_primary");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(145, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:146:2: ( '(' value_expression ')' | function | column_name | literal | sub_query )
            int alt25=5;
            try { dbg.enterDecision(25, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt25 = dfa25.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(25);}

            switch (alt25) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:146:4: '(' value_expression ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(146,7);
                    char_literal60=(Token)match(input,48,FOLLOW_48_in_value_expression_primary751); if (state.failed) return retval;
                    dbg.location(146,9);
                    pushFollow(FOLLOW_value_expression_in_value_expression_primary754);
                    value_expression61=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression61.getTree());
                    dbg.location(146,29);
                    char_literal62=(Token)match(input,49,FOLLOW_49_in_value_expression_primary756); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:147:5: function
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(147,5);
                    pushFollow(FOLLOW_function_in_value_expression_primary763);
                    function63=function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, function63.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:148:5: column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(148,5);
                    pushFollow(FOLLOW_column_name_in_value_expression_primary769);
                    column_name64=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name64.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:149:5: literal
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(149,5);
                    pushFollow(FOLLOW_literal_in_value_expression_primary775);
                    literal65=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal65.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:150:5: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(150,5);
                    pushFollow(FOLLOW_sub_query_in_value_expression_primary781);
                    sub_query66=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query66.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(151, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "value_expression_primary");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "value_expression_primary"

    public static class literal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:1: literal : ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' );
    public final SQL92QueryParser.literal_return literal() throws RecognitionException {
        SQL92QueryParser.literal_return retval = new SQL92QueryParser.literal_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT67=null;
        Token FLOAT68=null;
        Token NUMERIC69=null;
        Token STRING70=null;
        Token string_literal73=null;
        Token string_literal74=null;
        Token string_literal75=null;
        SQL92QueryParser.datetime_return datetime71 = null;

        SQL92QueryParser.interval_return interval72 = null;


        CommonTree INT67_tree=null;
        CommonTree FLOAT68_tree=null;
        CommonTree NUMERIC69_tree=null;
        CommonTree STRING70_tree=null;
        CommonTree string_literal73_tree=null;
        CommonTree string_literal74_tree=null;
        CommonTree string_literal75_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "literal");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(153, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:9: ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' )
            int alt26=9;
            try { dbg.enterDecision(26, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt26 = dfa26.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(26);}

            switch (alt26) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:11: INT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,11);
                    INT67=(Token)match(input,INT,FOLLOW_INT_in_literal791); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT67_tree = (CommonTree)adaptor.create(INT67);
                    adaptor.addChild(root_0, INT67_tree);
                    }

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:17: FLOAT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,17);
                    FLOAT68=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_literal795); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FLOAT68_tree = (CommonTree)adaptor.create(FLOAT68);
                    adaptor.addChild(root_0, FLOAT68_tree);
                    }

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:25: NUMERIC
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,25);
                    NUMERIC69=(Token)match(input,NUMERIC,FOLLOW_NUMERIC_in_literal799); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMERIC69_tree = (CommonTree)adaptor.create(NUMERIC69);
                    adaptor.addChild(root_0, NUMERIC69_tree);
                    }

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:35: STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,35);
                    STRING70=(Token)match(input,STRING,FOLLOW_STRING_in_literal803); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING70_tree = (CommonTree)adaptor.create(STRING70);
                    adaptor.addChild(root_0, STRING70_tree);
                    }

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:44: datetime
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,44);
                    pushFollow(FOLLOW_datetime_in_literal807);
                    datetime71=datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime71.getTree());

                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:55: interval
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,55);
                    pushFollow(FOLLOW_interval_in_literal811);
                    interval72=interval();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interval72.getTree());

                    }
                    break;
                case 7 :
                    dbg.enterAlt(7);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:66: 'NULL'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,66);
                    string_literal73=(Token)match(input,70,FOLLOW_70_in_literal815); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal73_tree = (CommonTree)adaptor.create(string_literal73);
                    adaptor.addChild(root_0, string_literal73_tree);
                    }

                    }
                    break;
                case 8 :
                    dbg.enterAlt(8);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:75: 'TRUE'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,75);
                    string_literal74=(Token)match(input,71,FOLLOW_71_in_literal819); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal74_tree = (CommonTree)adaptor.create(string_literal74);
                    adaptor.addChild(root_0, string_literal74_tree);
                    }

                    }
                    break;
                case 9 :
                    dbg.enterAlt(9);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:153:84: 'FALSE'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(153,84);
                    string_literal75=(Token)match(input,72,FOLLOW_72_in_literal823); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal75_tree = (CommonTree)adaptor.create(string_literal75);
                    adaptor.addChild(root_0, string_literal75_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(153, 91);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "literal");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "literal"

    public static class datetime_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datetime"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:154:1: datetime : ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING | (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
    public final SQL92QueryParser.datetime_return datetime() throws RecognitionException {
        SQL92QueryParser.datetime_return retval = new SQL92QueryParser.datetime_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token set76=null;
        Token STRING77=null;
        Token char_literal78=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree set76_tree=null;
        CommonTree STRING77_tree=null;
        CommonTree char_literal78_tree=null;
        RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try { dbg.enterRule(getGrammarFileName(), "datetime");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(154, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:155:2: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING | (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            int alt29=2;
            try { dbg.enterDecision(29, isCyclicDecision);

            switch ( input.LA(1) ) {
            case 57:
                {
                int LA29_1 = input.LA(2);

                if ( (LA29_1==STRING) ) {
                    int LA29_5 = input.LA(3);

                    if ( (synpred55_SQL92Query()) ) {
                        alt29=1;
                    }
                    else if ( (true) ) {
                        alt29=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 5, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else if ( (LA29_1==EOF||(LA29_1>=ID && LA29_1<=NUMERIC)||(LA29_1>=37 && LA29_1<=38)||(LA29_1>=40 && LA29_1<=41)||(LA29_1>=43 && LA29_1<=46)||(LA29_1>=48 && LA29_1<=51)||(LA29_1>=53 && LA29_1<=54)||(LA29_1>=57 && LA29_1<=72)||LA29_1==74||(LA29_1>=76 && LA29_1<=79)||(LA29_1>=81 && LA29_1<=94)||(LA29_1>=97 && LA29_1<=99)) ) {
                    alt29=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case ID:
                {
                alt29=2;
                }
                break;
            case 58:
                {
                int LA29_3 = input.LA(2);

                if ( (LA29_3==STRING) ) {
                    int LA29_5 = input.LA(3);

                    if ( (synpred55_SQL92Query()) ) {
                        alt29=1;
                    }
                    else if ( (true) ) {
                        alt29=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 5, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else if ( (LA29_3==EOF||(LA29_3>=ID && LA29_3<=NUMERIC)||(LA29_3>=37 && LA29_3<=38)||(LA29_3>=40 && LA29_3<=41)||(LA29_3>=43 && LA29_3<=46)||(LA29_3>=48 && LA29_3<=51)||(LA29_3>=53 && LA29_3<=54)||(LA29_3>=57 && LA29_3<=72)||LA29_3==74||(LA29_3>=76 && LA29_3<=79)||(LA29_3>=81 && LA29_3<=94)||(LA29_3>=97 && LA29_3<=99)) ) {
                    alt29=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 3, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case 59:
                {
                int LA29_4 = input.LA(2);

                if ( (LA29_4==STRING) ) {
                    int LA29_5 = input.LA(3);

                    if ( (synpred55_SQL92Query()) ) {
                        alt29=1;
                    }
                    else if ( (true) ) {
                        alt29=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 29, 5, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else if ( (LA29_4==EOF||(LA29_4>=ID && LA29_4<=NUMERIC)||(LA29_4>=37 && LA29_4<=38)||(LA29_4>=40 && LA29_4<=41)||(LA29_4>=43 && LA29_4<=46)||(LA29_4>=48 && LA29_4<=51)||(LA29_4>=53 && LA29_4<=54)||(LA29_4>=57 && LA29_4<=72)||LA29_4==74||(LA29_4>=76 && LA29_4<=79)||(LA29_4>=81 && LA29_4<=94)||(LA29_4>=97 && LA29_4<=99)) ) {
                    alt29=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 29, 4, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(29);}

            switch (alt29) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:155:4: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(155,4);
                    set76=(Token)input.LT(1);
                    set76=(Token)input.LT(1);
                    if ( (input.LA(1)>=57 && input.LA(1)<=59) ) {
                        input.consume();
                        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set76), root_0);
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        dbg.recognitionException(mse);
                        throw mse;
                    }

                    dbg.location(155,37);
                    STRING77=(Token)match(input,STRING,FOLLOW_STRING_in_datetime844); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING77_tree = (CommonTree)adaptor.create(STRING77);
                    adaptor.addChild(root_0, STRING77_tree);
                    }

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:5: (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' )
                    {
                    dbg.location(156,5);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:5: (tableid= ID '.' )?
                    int alt27=2;
                    try { dbg.enterSubRule(27);
                    try { dbg.enterDecision(27, isCyclicDecision);

                    int LA27_0 = input.LA(1);

                    if ( (LA27_0==ID) ) {
                        alt27=1;
                    }
                    } finally {dbg.exitDecision(27);}

                    switch (alt27) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:6: tableid= ID '.'
                            {
                            dbg.location(156,13);
                            tableid=(Token)match(input,ID,FOLLOW_ID_in_datetime853); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(tableid);

                            dbg.location(156,16);
                            char_literal78=(Token)match(input,56,FOLLOW_56_in_datetime854); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_56.add(char_literal78);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(27);}

                    dbg.location(156,21);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:21: (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' )
                    int alt28=3;
                    try { dbg.enterSubRule(28);
                    try { dbg.enterDecision(28, isCyclicDecision);

                    switch ( input.LA(1) ) {
                    case 57:
                        {
                        alt28=1;
                        }
                        break;
                    case 58:
                        {
                        alt28=2;
                        }
                        break;
                    case 59:
                        {
                        alt28=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 28, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(28);}

                    switch (alt28) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:22: s= 'DATE'
                            {
                            dbg.location(156,23);
                            s=(Token)match(input,57,FOLLOW_57_in_datetime860); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_57.add(s);


                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:33: s= 'TIMESTAMP'
                            {
                            dbg.location(156,34);
                            s=(Token)match(input,58,FOLLOW_58_in_datetime866); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_58.add(s);


                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:49: s= 'TIME'
                            {
                            dbg.location(156,50);
                            s=(Token)match(input,59,FOLLOW_59_in_datetime872); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_59.add(s);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(28);}



                    // AST REWRITE
                    // elements: tableid, s
                    // token labels: tableid, s
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
                    RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 156:59: -> ^( TABLECOLUMN ( $tableid)? $s)
                    {
                        dbg.location(156,62);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:62: ^( TABLECOLUMN ( $tableid)? $s)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(156,64);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                        dbg.location(156,76);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:156:76: ( $tableid)?
                        if ( stream_tableid.hasNext() ) {
                            dbg.location(156,76);
                            adaptor.addChild(root_1, stream_tableid.nextNode());

                        }
                        stream_tableid.reset();
                        dbg.location(156,86);
                        adaptor.addChild(root_1, stream_s.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(157, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "datetime");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "datetime"

    public static class interval_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "interval"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:158:1: interval : ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
    public final SQL92QueryParser.interval_return interval() throws RecognitionException {
        SQL92QueryParser.interval_return retval = new SQL92QueryParser.interval_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token string_literal79=null;
        Token STRING80=null;
        Token set81=null;
        Token char_literal82=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree string_literal79_tree=null;
        CommonTree STRING80_tree=null;
        CommonTree set81_tree=null;
        CommonTree char_literal82_tree=null;
        RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");

        try { dbg.enterRule(getGrammarFileName(), "interval");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(158, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:159:2: ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            int alt32=2;
            try { dbg.enterDecision(32, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt32 = dfa32.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(32);}

            switch (alt32) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:159:4: 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(159,14);
                    string_literal79=(Token)match(input,60,FOLLOW_60_in_interval896); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal79_tree = (CommonTree)adaptor.create(string_literal79);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal79_tree, root_0);
                    }
                    dbg.location(159,16);
                    STRING80=(Token)match(input,STRING,FOLLOW_STRING_in_interval899); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING80_tree = (CommonTree)adaptor.create(STRING80);
                    adaptor.addChild(root_0, STRING80_tree);
                    }
                    dbg.location(159,23);
                    set81=(Token)input.LT(1);
                    if ( (input.LA(1)>=61 && input.LA(1)<=66) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set81));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        dbg.recognitionException(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:4: (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
                    {
                    dbg.location(160,4);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:4: (tableid= ID '.' )?
                    int alt30=2;
                    try { dbg.enterSubRule(30);
                    try { dbg.enterDecision(30, isCyclicDecision);

                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==ID) ) {
                        alt30=1;
                    }
                    } finally {dbg.exitDecision(30);}

                    switch (alt30) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:5: tableid= ID '.'
                            {
                            dbg.location(160,12);
                            tableid=(Token)match(input,ID,FOLLOW_ID_in_interval931); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(tableid);

                            dbg.location(160,15);
                            char_literal82=(Token)match(input,56,FOLLOW_56_in_interval932); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_56.add(char_literal82);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(30);}

                    dbg.location(160,20);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:20: (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
                    int alt31=7;
                    try { dbg.enterSubRule(31);
                    try { dbg.enterDecision(31, isCyclicDecision);

                    switch ( input.LA(1) ) {
                    case 60:
                        {
                        alt31=1;
                        }
                        break;
                    case 61:
                        {
                        alt31=2;
                        }
                        break;
                    case 62:
                        {
                        alt31=3;
                        }
                        break;
                    case 63:
                        {
                        alt31=4;
                        }
                        break;
                    case 64:
                        {
                        alt31=5;
                        }
                        break;
                    case 65:
                        {
                        alt31=6;
                        }
                        break;
                    case 66:
                        {
                        alt31=7;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 31, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(31);}

                    switch (alt31) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:21: s= 'INTERVAL'
                            {
                            dbg.location(160,22);
                            s=(Token)match(input,60,FOLLOW_60_in_interval938); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_60.add(s);


                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:36: s= 'YEAR'
                            {
                            dbg.location(160,37);
                            s=(Token)match(input,61,FOLLOW_61_in_interval944); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_61.add(s);


                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:47: s= 'MONTH'
                            {
                            dbg.location(160,48);
                            s=(Token)match(input,62,FOLLOW_62_in_interval950); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_62.add(s);


                            }
                            break;
                        case 4 :
                            dbg.enterAlt(4);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:59: s= 'DAY'
                            {
                            dbg.location(160,60);
                            s=(Token)match(input,63,FOLLOW_63_in_interval956); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_63.add(s);


                            }
                            break;
                        case 5 :
                            dbg.enterAlt(5);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:69: s= 'HOUR'
                            {
                            dbg.location(160,70);
                            s=(Token)match(input,64,FOLLOW_64_in_interval962); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_64.add(s);


                            }
                            break;
                        case 6 :
                            dbg.enterAlt(6);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:80: s= 'MINUTE'
                            {
                            dbg.location(160,81);
                            s=(Token)match(input,65,FOLLOW_65_in_interval968); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_65.add(s);


                            }
                            break;
                        case 7 :
                            dbg.enterAlt(7);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:93: s= 'SECOND'
                            {
                            dbg.location(160,94);
                            s=(Token)match(input,66,FOLLOW_66_in_interval974); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_66.add(s);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(31);}



                    // AST REWRITE
                    // elements: tableid, s
                    // token labels: tableid, s
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
                    RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 160:105: -> ^( TABLECOLUMN ( $tableid)? $s)
                    {
                        dbg.location(160,108);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:108: ^( TABLECOLUMN ( $tableid)? $s)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(160,110);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                        dbg.location(160,122);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:160:122: ( $tableid)?
                        if ( stream_tableid.hasNext() ) {
                            dbg.location(160,122);
                            adaptor.addChild(root_1, stream_tableid.nextNode());

                        }
                        stream_tableid.reset();
                        dbg.location(160,132);
                        adaptor.addChild(root_1, stream_s.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(161, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "interval");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "interval"

    public static class function_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:1: function : ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) );
    public final SQL92QueryParser.function_return function() throws RecognitionException {
        SQL92QueryParser.function_return retval = new SQL92QueryParser.function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token char_literal83=null;
        Token char_literal85=null;
        Token char_literal87=null;
        Token char_literal88=null;
        Token char_literal89=null;
        Token char_literal90=null;
        SQL92QueryParser.value_expression_return value_expression84 = null;

        SQL92QueryParser.value_expression_return value_expression86 = null;


        CommonTree name_tree=null;
        CommonTree char_literal83_tree=null;
        CommonTree char_literal85_tree=null;
        CommonTree char_literal87_tree=null;
        CommonTree char_literal88_tree=null;
        CommonTree char_literal89_tree=null;
        CommonTree char_literal90_tree=null;
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try { dbg.enterRule(getGrammarFileName(), "function");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(163, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:9: ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) )
            int alt35=2;
            try { dbg.enterDecision(35, isCyclicDecision);

            int LA35_0 = input.LA(1);

            if ( (LA35_0==ID) ) {
                int LA35_1 = input.LA(2);

                if ( (LA35_1==48) ) {
                    int LA35_2 = input.LA(3);

                    if ( (LA35_2==50) ) {
                        alt35=2;
                    }
                    else if ( ((LA35_2>=ID && LA35_2<=STRING)||(LA35_2>=48 && LA35_2<=49)||LA35_2==51||(LA35_2>=57 && LA35_2<=68)||(LA35_2>=70 && LA35_2<=72)) ) {
                        alt35=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 2, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 35, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(35);}

            switch (alt35) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:11: (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')'
                    {
                    dbg.location(163,11);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:11: (name= ID )
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:12: name= ID
                    {
                    dbg.location(163,16);
                    name=(Token)match(input,ID,FOLLOW_ID_in_function1002); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);


                    }

                    dbg.location(163,21);
                    char_literal83=(Token)match(input,48,FOLLOW_48_in_function1005); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_48.add(char_literal83);

                    dbg.location(163,25);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:25: ( value_expression )?
                    int alt33=2;
                    try { dbg.enterSubRule(33);
                    try { dbg.enterDecision(33, isCyclicDecision);

                    int LA33_0 = input.LA(1);

                    if ( ((LA33_0>=ID && LA33_0<=STRING)||LA33_0==48||(LA33_0>=57 && LA33_0<=68)||(LA33_0>=70 && LA33_0<=72)) ) {
                        alt33=1;
                    }
                    } finally {dbg.exitDecision(33);}

                    switch (alt33) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: value_expression
                            {
                            dbg.location(163,25);
                            pushFollow(FOLLOW_value_expression_in_function1007);
                            value_expression84=value_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_value_expression.add(value_expression84.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(33);}

                    dbg.location(163,43);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:43: ( ',' value_expression )*
                    try { dbg.enterSubRule(34);

                    loop34:
                    do {
                        int alt34=2;
                        try { dbg.enterDecision(34, isCyclicDecision);

                        int LA34_0 = input.LA(1);

                        if ( (LA34_0==51) ) {
                            alt34=1;
                        }


                        } finally {dbg.exitDecision(34);}

                        switch (alt34) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:163:44: ',' value_expression
                    	    {
                    	    dbg.location(163,44);
                    	    char_literal85=(Token)match(input,51,FOLLOW_51_in_function1011); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_51.add(char_literal85);

                    	    dbg.location(163,48);
                    	    pushFollow(FOLLOW_value_expression_in_function1013);
                    	    value_expression86=value_expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value_expression.add(value_expression86.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(34);}

                    dbg.location(163,67);
                    char_literal87=(Token)match(input,49,FOLLOW_49_in_function1017); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_49.add(char_literal87);



                    // AST REWRITE
                    // elements: name, value_expression
                    // token labels: name
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 164:4: -> ^( FUNCTION $name ( value_expression )* )
                    {
                        dbg.location(164,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:164:7: ^( FUNCTION $name ( value_expression )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(164,9);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                        dbg.location(164,18);
                        adaptor.addChild(root_1, stream_name.nextNode());
                        dbg.location(164,24);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:164:24: ( value_expression )*
                        while ( stream_value_expression.hasNext() ) {
                            dbg.location(164,24);
                            adaptor.addChild(root_1, stream_value_expression.nextTree());

                        }
                        stream_value_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:165:5: (name= ID ) '(' '*' ')'
                    {
                    dbg.location(165,5);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:165:5: (name= ID )
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:165:6: name= ID
                    {
                    dbg.location(165,10);
                    name=(Token)match(input,ID,FOLLOW_ID_in_function1042); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);


                    }

                    dbg.location(165,15);
                    char_literal88=(Token)match(input,48,FOLLOW_48_in_function1045); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_48.add(char_literal88);

                    dbg.location(165,19);
                    char_literal89=(Token)match(input,50,FOLLOW_50_in_function1047); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_50.add(char_literal89);

                    dbg.location(165,23);
                    char_literal90=(Token)match(input,49,FOLLOW_49_in_function1049); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_49.add(char_literal90);



                    // AST REWRITE
                    // elements: 50, name
                    // token labels: name
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 165:27: -> ^( FUNCTION $name '*' )
                    {
                        dbg.location(165,30);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:165:30: ^( FUNCTION $name '*' )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(165,32);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                        dbg.location(165,41);
                        adaptor.addChild(root_1, stream_name.nextNode());
                        dbg.location(165,47);
                        adaptor.addChild(root_1, stream_50.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(166, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "function");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "function"

    public static class string_value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string_value_expression"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:168:1: string_value_expression : ( column_name | STRING ) ( '||' ( column_name | STRING ) )+ ;
    public final SQL92QueryParser.string_value_expression_return string_value_expression() throws RecognitionException {
        SQL92QueryParser.string_value_expression_return retval = new SQL92QueryParser.string_value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STRING92=null;
        Token string_literal93=null;
        Token STRING95=null;
        SQL92QueryParser.column_name_return column_name91 = null;

        SQL92QueryParser.column_name_return column_name94 = null;


        CommonTree STRING92_tree=null;
        CommonTree string_literal93_tree=null;
        CommonTree STRING95_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "string_value_expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(168, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:2: ( ( column_name | STRING ) ( '||' ( column_name | STRING ) )+ )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:4: ( column_name | STRING ) ( '||' ( column_name | STRING ) )+
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(169,4);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:4: ( column_name | STRING )
            int alt36=2;
            try { dbg.enterSubRule(36);
            try { dbg.enterDecision(36, isCyclicDecision);

            int LA36_0 = input.LA(1);

            if ( (LA36_0==ID) ) {
                alt36=1;
            }
            else if ( (LA36_0==STRING) ) {
                alt36=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(36);}

            switch (alt36) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:5: column_name
                    {
                    dbg.location(169,5);
                    pushFollow(FOLLOW_column_name_in_string_value_expression1072);
                    column_name91=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name91.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:19: STRING
                    {
                    dbg.location(169,19);
                    STRING92=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1076); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING92_tree = (CommonTree)adaptor.create(STRING92);
                    adaptor.addChild(root_0, STRING92_tree);
                    }

                    }
                    break;

            }
            } finally {dbg.exitSubRule(36);}

            dbg.location(169,27);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:27: ( '||' ( column_name | STRING ) )+
            int cnt38=0;
            try { dbg.enterSubRule(38);

            loop38:
            do {
                int alt38=2;
                try { dbg.enterDecision(38, isCyclicDecision);

                int LA38_0 = input.LA(1);

                if ( (LA38_0==73) ) {
                    alt38=1;
                }


                } finally {dbg.exitDecision(38);}

                switch (alt38) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:28: '||' ( column_name | STRING )
            	    {
            	    dbg.location(169,32);
            	    string_literal93=(Token)match(input,73,FOLLOW_73_in_string_value_expression1080); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal93_tree = (CommonTree)adaptor.create(string_literal93);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal93_tree, root_0);
            	    }
            	    dbg.location(169,34);
            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:34: ( column_name | STRING )
            	    int alt37=2;
            	    try { dbg.enterSubRule(37);
            	    try { dbg.enterDecision(37, isCyclicDecision);

            	    int LA37_0 = input.LA(1);

            	    if ( (LA37_0==ID) ) {
            	        alt37=1;
            	    }
            	    else if ( (LA37_0==STRING) ) {
            	        alt37=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 37, 0, input);

            	        dbg.recognitionException(nvae);
            	        throw nvae;
            	    }
            	    } finally {dbg.exitDecision(37);}

            	    switch (alt37) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:35: column_name
            	            {
            	            dbg.location(169,35);
            	            pushFollow(FOLLOW_column_name_in_string_value_expression1084);
            	            column_name94=column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name94.getTree());

            	            }
            	            break;
            	        case 2 :
            	            dbg.enterAlt(2);

            	            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:169:49: STRING
            	            {
            	            dbg.location(169,49);
            	            STRING95=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1088); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            STRING95_tree = (CommonTree)adaptor.create(STRING95);
            	            adaptor.addChild(root_0, STRING95_tree);
            	            }

            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(37);}


            	    }
            	    break;

            	default :
            	    if ( cnt38 >= 1 ) break loop38;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(38, input);
                        dbg.recognitionException(eee);

                        throw eee;
                }
                cnt38++;
            } while (true);
            } finally {dbg.exitSubRule(38);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(170, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "string_value_expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "string_value_expression"

    public static class table_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_expression"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:172:1: table_expression : table_reference ;
    public final SQL92QueryParser.table_expression_return table_expression() throws RecognitionException {
        SQL92QueryParser.table_expression_return retval = new SQL92QueryParser.table_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_reference_return table_reference96 = null;



        try { dbg.enterRule(getGrammarFileName(), "table_expression");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(172, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:173:2: ( table_reference )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:173:4: table_reference
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(173,4);
            pushFollow(FOLLOW_table_reference_in_table_expression1103);
            table_reference96=table_reference();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference96.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(174, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_expression");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_expression"

    public static class table_reference_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_reference"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:175:1: table_reference : table ( ',' table_reference )* ;
    public final SQL92QueryParser.table_reference_return table_reference() throws RecognitionException {
        SQL92QueryParser.table_reference_return retval = new SQL92QueryParser.table_reference_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal98=null;
        SQL92QueryParser.table_return table97 = null;

        SQL92QueryParser.table_reference_return table_reference99 = null;


        CommonTree char_literal98_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "table_reference");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(175, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:2: ( table ( ',' table_reference )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:4: table ( ',' table_reference )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(176,4);
            pushFollow(FOLLOW_table_in_table_reference1113);
            table97=table();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, table97.getTree());
            dbg.location(176,10);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:10: ( ',' table_reference )*
            try { dbg.enterSubRule(39);

            loop39:
            do {
                int alt39=2;
                try { dbg.enterDecision(39, isCyclicDecision);

                int LA39_0 = input.LA(1);

                if ( (LA39_0==51) ) {
                    int LA39_2 = input.LA(2);

                    if ( (synpred78_SQL92Query()) ) {
                        alt39=1;
                    }


                }


                } finally {dbg.exitDecision(39);}

                switch (alt39) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:11: ',' table_reference
            	    {
            	    dbg.location(176,14);
            	    char_literal98=(Token)match(input,51,FOLLOW_51_in_table_reference1116); if (state.failed) return retval;
            	    dbg.location(176,16);
            	    pushFollow(FOLLOW_table_reference_in_table_reference1119);
            	    table_reference99=table_reference();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference99.getTree());

            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);
            } finally {dbg.exitSubRule(39);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(177, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_reference");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_reference"

    public static class join_type_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join_type"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:179:1: join_type : ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN );
    public final SQL92QueryParser.join_type_return join_type() throws RecognitionException {
        SQL92QueryParser.join_type_return retval = new SQL92QueryParser.join_type_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal100=null;
        Token string_literal101=null;
        Token string_literal102=null;
        Token string_literal103=null;
        Token string_literal104=null;
        Token string_literal105=null;
        Token string_literal106=null;
        Token string_literal107=null;
        Token string_literal108=null;
        Token string_literal109=null;
        Token string_literal110=null;

        CommonTree string_literal100_tree=null;
        CommonTree string_literal101_tree=null;
        CommonTree string_literal102_tree=null;
        CommonTree string_literal103_tree=null;
        CommonTree string_literal104_tree=null;
        CommonTree string_literal105_tree=null;
        CommonTree string_literal106_tree=null;
        CommonTree string_literal107_tree=null;
        CommonTree string_literal108_tree=null;
        CommonTree string_literal109_tree=null;
        CommonTree string_literal110_tree=null;
        RewriteRuleTokenStream stream_79=new RewriteRuleTokenStream(adaptor,"token 79");
        RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
        RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
        RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
        RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
        RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");

        try { dbg.enterRule(getGrammarFileName(), "join_type");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(179, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:180:2: ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN )
            int alt44=4;
            try { dbg.enterDecision(44, isCyclicDecision);

            switch ( input.LA(1) ) {
            case 74:
                {
                alt44=1;
                }
                break;
            case 77:
                {
                alt44=2;
                }
                break;
            case 78:
                {
                alt44=3;
                }
                break;
            case 76:
            case 79:
                {
                alt44=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(44);}

            switch (alt44) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:180:4: 'RIGHT' ( 'OUTER' )? 'JOIN'
                    {
                    dbg.location(180,4);
                    string_literal100=(Token)match(input,74,FOLLOW_74_in_join_type1132); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_74.add(string_literal100);

                    dbg.location(180,12);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:180:12: ( 'OUTER' )?
                    int alt40=2;
                    try { dbg.enterSubRule(40);
                    try { dbg.enterDecision(40, isCyclicDecision);

                    int LA40_0 = input.LA(1);

                    if ( (LA40_0==75) ) {
                        alt40=1;
                    }
                    } finally {dbg.exitDecision(40);}

                    switch (alt40) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'OUTER'
                            {
                            dbg.location(180,12);
                            string_literal101=(Token)match(input,75,FOLLOW_75_in_join_type1134); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_75.add(string_literal101);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(40);}

                    dbg.location(180,21);
                    string_literal102=(Token)match(input,76,FOLLOW_76_in_join_type1137); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal102);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 180:28: -> RIGHT_OUTER_JOIN
                    {
                        dbg.location(180,31);
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(RIGHT_OUTER_JOIN, "RIGHT_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:181:4: 'LEFT' ( 'OUTER' )? 'JOIN'
                    {
                    dbg.location(181,4);
                    string_literal103=(Token)match(input,77,FOLLOW_77_in_join_type1147); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_77.add(string_literal103);

                    dbg.location(181,11);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:181:11: ( 'OUTER' )?
                    int alt41=2;
                    try { dbg.enterSubRule(41);
                    try { dbg.enterDecision(41, isCyclicDecision);

                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==75) ) {
                        alt41=1;
                    }
                    } finally {dbg.exitDecision(41);}

                    switch (alt41) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'OUTER'
                            {
                            dbg.location(181,11);
                            string_literal104=(Token)match(input,75,FOLLOW_75_in_join_type1149); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_75.add(string_literal104);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(41);}

                    dbg.location(181,20);
                    string_literal105=(Token)match(input,76,FOLLOW_76_in_join_type1152); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal105);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 181:27: -> LEFT_OUTER_JOIN
                    {
                        dbg.location(181,30);
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(LEFT_OUTER_JOIN, "LEFT_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:182:4: 'FULL' ( 'OUTER' )? 'JOIN'
                    {
                    dbg.location(182,4);
                    string_literal106=(Token)match(input,78,FOLLOW_78_in_join_type1161); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_78.add(string_literal106);

                    dbg.location(182,11);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:182:11: ( 'OUTER' )?
                    int alt42=2;
                    try { dbg.enterSubRule(42);
                    try { dbg.enterDecision(42, isCyclicDecision);

                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==75) ) {
                        alt42=1;
                    }
                    } finally {dbg.exitDecision(42);}

                    switch (alt42) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'OUTER'
                            {
                            dbg.location(182,11);
                            string_literal107=(Token)match(input,75,FOLLOW_75_in_join_type1163); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_75.add(string_literal107);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(42);}

                    dbg.location(182,20);
                    string_literal108=(Token)match(input,76,FOLLOW_76_in_join_type1166); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal108);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 182:27: -> FULL_OUTER_JOIN
                    {
                        dbg.location(182,30);
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(FULL_OUTER_JOIN, "FULL_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:183:5: ( 'INNER' )? 'JOIN'
                    {
                    dbg.location(183,5);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:183:5: ( 'INNER' )?
                    int alt43=2;
                    try { dbg.enterSubRule(43);
                    try { dbg.enterDecision(43, isCyclicDecision);

                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==79) ) {
                        alt43=1;
                    }
                    } finally {dbg.exitDecision(43);}

                    switch (alt43) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: 'INNER'
                            {
                            dbg.location(183,5);
                            string_literal109=(Token)match(input,79,FOLLOW_79_in_join_type1176); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_79.add(string_literal109);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(43);}

                    dbg.location(183,14);
                    string_literal110=(Token)match(input,76,FOLLOW_76_in_join_type1179); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal110);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 183:21: -> JOIN
                    {
                        dbg.location(183,24);
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(JOIN, "JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(184, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "join_type");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "join_type"

    public static class table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:185:1: table : non_join_table ( join_type non_join_table 'ON' search_condition )* ;
    public final SQL92QueryParser.table_return table() throws RecognitionException {
        SQL92QueryParser.table_return retval = new SQL92QueryParser.table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal114=null;
        SQL92QueryParser.non_join_table_return non_join_table111 = null;

        SQL92QueryParser.join_type_return join_type112 = null;

        SQL92QueryParser.non_join_table_return non_join_table113 = null;

        SQL92QueryParser.search_condition_return search_condition115 = null;


        CommonTree string_literal114_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "table");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(185, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:185:8: ( non_join_table ( join_type non_join_table 'ON' search_condition )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:185:10: non_join_table ( join_type non_join_table 'ON' search_condition )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(185,10);
            pushFollow(FOLLOW_non_join_table_in_table1193);
            non_join_table111=non_join_table();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table111.getTree());
            dbg.location(185,25);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:185:25: ( join_type non_join_table 'ON' search_condition )*
            try { dbg.enterSubRule(45);

            loop45:
            do {
                int alt45=2;
                try { dbg.enterDecision(45, isCyclicDecision);

                int LA45_0 = input.LA(1);

                if ( (LA45_0==74||(LA45_0>=76 && LA45_0<=79)) ) {
                    alt45=1;
                }


                } finally {dbg.exitDecision(45);}

                switch (alt45) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:185:26: join_type non_join_table 'ON' search_condition
            	    {
            	    dbg.location(185,35);
            	    pushFollow(FOLLOW_join_type_in_table1196);
            	    join_type112=join_type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(join_type112.getTree(), root_0);
            	    dbg.location(185,37);
            	    pushFollow(FOLLOW_non_join_table_in_table1199);
            	    non_join_table113=non_join_table();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table113.getTree());
            	    dbg.location(185,56);
            	    string_literal114=(Token)match(input,80,FOLLOW_80_in_table1201); if (state.failed) return retval;
            	    dbg.location(185,58);
            	    pushFollow(FOLLOW_search_condition_in_table1204);
            	    search_condition115=search_condition();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition115.getTree());

            	    }
            	    break;

            	default :
            	    break loop45;
                }
            } while (true);
            } finally {dbg.exitSubRule(45);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(186, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table"

    public static class non_join_table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "non_join_table"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:189:1: non_join_table : ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) );
    public final SQL92QueryParser.non_join_table_return non_join_table() throws RecognitionException {
        SQL92QueryParser.non_join_table_return retval = new SQL92QueryParser.non_join_table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_name_return table_name116 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification117 = null;

        SQL92QueryParser.table_function_return table_function118 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification119 = null;

        SQL92QueryParser.sub_query_return sub_query120 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification121 = null;


        RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
        RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");
        RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");
        RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
        try { dbg.enterRule(getGrammarFileName(), "non_join_table");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(189, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:190:2: ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
            int alt47=3;
            try { dbg.enterDecision(47, isCyclicDecision);

            int LA47_0 = input.LA(1);

            if ( (LA47_0==ID) ) {
                int LA47_1 = input.LA(2);

                if ( (LA47_1==48) ) {
                    alt47=2;
                }
                else if ( (LA47_1==EOF||LA47_1==ID||(LA47_1>=37 && LA47_1<=38)||(LA47_1>=40 && LA47_1<=41)||(LA47_1>=44 && LA47_1<=46)||LA47_1==49||LA47_1==51||(LA47_1>=53 && LA47_1<=54)||LA47_1==74||(LA47_1>=76 && LA47_1<=80)) ) {
                    alt47=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 47, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else if ( (LA47_0==48) ) {
                alt47=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(47);}

            switch (alt47) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:190:4: table_name ( correlation_specification )?
                    {
                    dbg.location(190,4);
                    pushFollow(FOLLOW_table_name_in_non_join_table1218);
                    table_name116=table_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_name.add(table_name116.getTree());
                    dbg.location(190,15);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:190:15: ( correlation_specification )?
                    int alt46=2;
                    try { dbg.enterSubRule(46);
                    try { dbg.enterDecision(46, isCyclicDecision);

                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==ID||LA46_0==53) ) {
                        alt46=1;
                    }
                    } finally {dbg.exitDecision(46);}

                    switch (alt46) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: correlation_specification
                            {
                            dbg.location(190,15);
                            pushFollow(FOLLOW_correlation_specification_in_non_join_table1220);
                            correlation_specification117=correlation_specification();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification117.getTree());

                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(46);}



                    // AST REWRITE
                    // elements: correlation_specification, table_name
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 190:42: -> ^( RELATION table_name ( correlation_specification )? )
                    {
                        dbg.location(190,45);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:190:45: ^( RELATION table_name ( correlation_specification )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(190,47);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(190,56);
                        adaptor.addChild(root_1, stream_table_name.nextTree());
                        dbg.location(190,67);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:190:67: ( correlation_specification )?
                        if ( stream_correlation_specification.hasNext() ) {
                            dbg.location(190,67);
                            adaptor.addChild(root_1, stream_correlation_specification.nextTree());

                        }
                        stream_correlation_specification.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:191:5: table_function correlation_specification
                    {
                    dbg.location(191,5);
                    pushFollow(FOLLOW_table_function_in_non_join_table1238);
                    table_function118=table_function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function.add(table_function118.getTree());
                    dbg.location(191,20);
                    pushFollow(FOLLOW_correlation_specification_in_non_join_table1240);
                    correlation_specification119=correlation_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification119.getTree());


                    // AST REWRITE
                    // elements: table_function, correlation_specification
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 191:46: -> ^( RELATION table_function correlation_specification )
                    {
                        dbg.location(191,49);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:191:49: ^( RELATION table_function correlation_specification )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(191,51);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(191,60);
                        adaptor.addChild(root_1, stream_table_function.nextTree());
                        dbg.location(191,75);
                        adaptor.addChild(root_1, stream_correlation_specification.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:192:5: sub_query correlation_specification
                    {
                    dbg.location(192,5);
                    pushFollow(FOLLOW_sub_query_in_non_join_table1256);
                    sub_query120=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sub_query.add(sub_query120.getTree());
                    dbg.location(192,15);
                    pushFollow(FOLLOW_correlation_specification_in_non_join_table1258);
                    correlation_specification121=correlation_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification121.getTree());


                    // AST REWRITE
                    // elements: correlation_specification, sub_query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 192:41: -> ^( RELATION sub_query correlation_specification )
                    {
                        dbg.location(192,44);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:192:44: ^( RELATION sub_query correlation_specification )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(192,46);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(192,55);
                        adaptor.addChild(root_1, stream_sub_query.nextTree());
                        dbg.location(192,65);
                        adaptor.addChild(root_1, stream_correlation_specification.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(193, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "non_join_table");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "non_join_table"

    public static class table_function_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:195:1: table_function : name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) ;
    public final SQL92QueryParser.table_function_return table_function() throws RecognitionException {
        SQL92QueryParser.table_function_return retval = new SQL92QueryParser.table_function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token char_literal122=null;
        Token char_literal124=null;
        Token char_literal126=null;
        Token char_literal128=null;
        SQL92QueryParser.table_function_subquery_return table_function_subquery123 = null;

        SQL92QueryParser.table_function_subquery_return table_function_subquery125 = null;

        SQL92QueryParser.table_function_param_return table_function_param127 = null;


        CommonTree name_tree=null;
        CommonTree char_literal122_tree=null;
        CommonTree char_literal124_tree=null;
        CommonTree char_literal126_tree=null;
        CommonTree char_literal128_tree=null;
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_table_function_subquery=new RewriteRuleSubtreeStream(adaptor,"rule table_function_subquery");
        RewriteRuleSubtreeStream stream_table_function_param=new RewriteRuleSubtreeStream(adaptor,"rule table_function_param");
        try { dbg.enterRule(getGrammarFileName(), "table_function");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(195, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:2: (name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:4: name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')'
            {
            dbg.location(196,8);
            name=(Token)match(input,ID,FOLLOW_ID_in_table_function1282); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            dbg.location(196,12);
            char_literal122=(Token)match(input,48,FOLLOW_48_in_table_function1284); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_48.add(char_literal122);

            dbg.location(196,16);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:16: ( table_function_subquery )?
            int alt48=2;
            try { dbg.enterSubRule(48);
            try { dbg.enterDecision(48, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt48 = dfa48.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(48);}

            switch (alt48) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: table_function_subquery
                    {
                    dbg.location(196,16);
                    pushFollow(FOLLOW_table_function_subquery_in_table_function1286);
                    table_function_subquery123=table_function_subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery123.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(48);}

            dbg.location(196,41);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:41: ( ',' table_function_subquery )*
            try { dbg.enterSubRule(49);

            loop49:
            do {
                int alt49=2;
                try { dbg.enterDecision(49, isCyclicDecision);

                try {
                    isCyclicDecision = true;
                    alt49 = dfa49.predict(input);
                }
                catch (NoViableAltException nvae) {
                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                } finally {dbg.exitDecision(49);}

                switch (alt49) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:42: ',' table_function_subquery
            	    {
            	    dbg.location(196,42);
            	    char_literal124=(Token)match(input,51,FOLLOW_51_in_table_function1290); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_51.add(char_literal124);

            	    dbg.location(196,46);
            	    pushFollow(FOLLOW_table_function_subquery_in_table_function1292);
            	    table_function_subquery125=table_function_subquery();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery125.getTree());

            	    }
            	    break;

            	default :
            	    break loop49;
                }
            } while (true);
            } finally {dbg.exitSubRule(49);}

            dbg.location(196,72);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:72: ( ( ',' )? table_function_param )*
            try { dbg.enterSubRule(51);

            loop51:
            do {
                int alt51=2;
                try { dbg.enterDecision(51, isCyclicDecision);

                int LA51_0 = input.LA(1);

                if ( ((LA51_0>=ID && LA51_0<=STRING)||LA51_0==48||LA51_0==51||(LA51_0>=57 && LA51_0<=68)||(LA51_0>=70 && LA51_0<=72)||LA51_0==83||LA51_0==87||(LA51_0>=98 && LA51_0<=99)) ) {
                    alt51=1;
                }


                } finally {dbg.exitDecision(51);}

                switch (alt51) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:73: ( ',' )? table_function_param
            	    {
            	    dbg.location(196,73);
            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:73: ( ',' )?
            	    int alt50=2;
            	    try { dbg.enterSubRule(50);
            	    try { dbg.enterDecision(50, isCyclicDecision);

            	    int LA50_0 = input.LA(1);

            	    if ( (LA50_0==51) ) {
            	        alt50=1;
            	    }
            	    } finally {dbg.exitDecision(50);}

            	    switch (alt50) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:0:0: ','
            	            {
            	            dbg.location(196,73);
            	            char_literal126=(Token)match(input,51,FOLLOW_51_in_table_function1297); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_51.add(char_literal126);


            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(50);}

            	    dbg.location(196,78);
            	    pushFollow(FOLLOW_table_function_param_in_table_function1300);
            	    table_function_param127=table_function_param();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_table_function_param.add(table_function_param127.getTree());

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);
            } finally {dbg.exitSubRule(51);}

            dbg.location(196,101);
            char_literal128=(Token)match(input,49,FOLLOW_49_in_table_function1304); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_49.add(char_literal128);



            // AST REWRITE
            // elements: name, table_function_param, table_function_subquery
            // token labels: name
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 197:5: -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
            {
                dbg.location(197,8);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:197:8: ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(197,10);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                dbg.location(197,19);
                adaptor.addChild(root_1, stream_name.nextNode());
                dbg.location(197,25);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:197:25: ( table_function_subquery )*
                while ( stream_table_function_subquery.hasNext() ) {
                    dbg.location(197,26);
                    adaptor.addChild(root_1, stream_table_function_subquery.nextTree());

                }
                stream_table_function_subquery.reset();
                dbg.location(197,52);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:197:52: ( table_function_param )*
                while ( stream_table_function_param.hasNext() ) {
                    dbg.location(197,52);
                    adaptor.addChild(root_1, stream_table_function_param.nextTree());

                }
                stream_table_function_param.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(198, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_function");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_function"

    public static class table_function_subquery_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function_subquery"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:200:1: table_function_subquery : sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) ;
    public final SQL92QueryParser.table_function_subquery_return table_function_subquery() throws RecognitionException {
        SQL92QueryParser.table_function_subquery_return retval = new SQL92QueryParser.table_function_subquery_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.sub_query_return sub_query129 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification130 = null;


        RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");
        RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
        try { dbg.enterRule(getGrammarFileName(), "table_function_subquery");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(200, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:201:2: ( sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:201:4: sub_query correlation_specification
            {
            dbg.location(201,4);
            pushFollow(FOLLOW_sub_query_in_table_function_subquery1336);
            sub_query129=sub_query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_sub_query.add(sub_query129.getTree());
            dbg.location(201,14);
            pushFollow(FOLLOW_correlation_specification_in_table_function_subquery1338);
            correlation_specification130=correlation_specification();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification130.getTree());


            // AST REWRITE
            // elements: sub_query, correlation_specification
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 201:40: -> ^( RELATION sub_query correlation_specification )
            {
                dbg.location(201,43);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:201:43: ^( RELATION sub_query correlation_specification )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(201,45);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                dbg.location(201,54);
                adaptor.addChild(root_1, stream_sub_query.nextTree());
                dbg.location(201,64);
                adaptor.addChild(root_1, stream_correlation_specification.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(202, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_function_subquery");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_function_subquery"

    public static class table_function_param_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function_param"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:204:1: table_function_param : ( search_condition | value_expression );
    public final SQL92QueryParser.table_function_param_return table_function_param() throws RecognitionException {
        SQL92QueryParser.table_function_param_return retval = new SQL92QueryParser.table_function_param_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.search_condition_return search_condition131 = null;

        SQL92QueryParser.value_expression_return value_expression132 = null;



        try { dbg.enterRule(getGrammarFileName(), "table_function_param");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(204, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:205:2: ( search_condition | value_expression )
            int alt52=2;
            try { dbg.enterDecision(52, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt52 = dfa52.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(52);}

            switch (alt52) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:205:4: search_condition
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(205,4);
                    pushFollow(FOLLOW_search_condition_in_table_function_param1359);
                    search_condition131=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition131.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:206:4: value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(206,4);
                    pushFollow(FOLLOW_value_expression_in_table_function_param1364);
                    value_expression132=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression132.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(207, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_function_param");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_function_param"

    public static class relation_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relation"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:209:1: relation : ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) );
    public final SQL92QueryParser.relation_return relation() throws RecognitionException {
        SQL92QueryParser.relation_return retval = new SQL92QueryParser.relation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_name_return table_name133 = null;

        SQL92QueryParser.table_function_return table_function134 = null;

        SQL92QueryParser.query_return query135 = null;


        RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
        RewriteRuleSubtreeStream stream_query=new RewriteRuleSubtreeStream(adaptor,"rule query");
        RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");
        try { dbg.enterRule(getGrammarFileName(), "relation");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(209, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:210:3: ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) )
            int alt53=3;
            try { dbg.enterDecision(53, isCyclicDecision);

            int LA53_0 = input.LA(1);

            if ( (LA53_0==ID) ) {
                int LA53_1 = input.LA(2);

                if ( (LA53_1==48) ) {
                    alt53=2;
                }
                else if ( (LA53_1==EOF) ) {
                    alt53=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 53, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else if ( (LA53_0==42||LA53_0==48) ) {
                alt53=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 53, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(53);}

            switch (alt53) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:210:5: table_name
                    {
                    dbg.location(210,5);
                    pushFollow(FOLLOW_table_name_in_relation1378);
                    table_name133=table_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_name.add(table_name133.getTree());


                    // AST REWRITE
                    // elements: table_name
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 210:16: -> ^( RELATION table_name )
                    {
                        dbg.location(210,19);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:210:19: ^( RELATION table_name )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(210,21);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(210,30);
                        adaptor.addChild(root_1, stream_table_name.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:211:5: table_function
                    {
                    dbg.location(211,5);
                    pushFollow(FOLLOW_table_function_in_relation1392);
                    table_function134=table_function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function.add(table_function134.getTree());


                    // AST REWRITE
                    // elements: table_function
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 211:20: -> ^( RELATION table_function )
                    {
                        dbg.location(211,23);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:211:23: ^( RELATION table_function )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(211,25);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(211,34);
                        adaptor.addChild(root_1, stream_table_function.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:212:5: query
                    {
                    dbg.location(212,5);
                    pushFollow(FOLLOW_query_in_relation1406);
                    query135=query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_query.add(query135.getTree());


                    // AST REWRITE
                    // elements: query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 212:11: -> ^( RELATION query )
                    {
                        dbg.location(212,14);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:212:14: ^( RELATION query )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(212,16);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        dbg.location(212,25);
                        adaptor.addChild(root_1, stream_query.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(213, 3);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "relation");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "relation"

    public static class search_condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "search_condition"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:215:1: search_condition : boolean_factor ( 'OR' boolean_factor )* ;
    public final SQL92QueryParser.search_condition_return search_condition() throws RecognitionException {
        SQL92QueryParser.search_condition_return retval = new SQL92QueryParser.search_condition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal137=null;
        SQL92QueryParser.boolean_factor_return boolean_factor136 = null;

        SQL92QueryParser.boolean_factor_return boolean_factor138 = null;


        CommonTree string_literal137_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "search_condition");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(215, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:216:2: ( boolean_factor ( 'OR' boolean_factor )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:216:4: boolean_factor ( 'OR' boolean_factor )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(216,4);
            pushFollow(FOLLOW_boolean_factor_in_search_condition1427);
            boolean_factor136=boolean_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor136.getTree());
            dbg.location(216,19);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:216:19: ( 'OR' boolean_factor )*
            try { dbg.enterSubRule(54);

            loop54:
            do {
                int alt54=2;
                try { dbg.enterDecision(54, isCyclicDecision);

                int LA54_0 = input.LA(1);

                if ( (LA54_0==81) ) {
                    alt54=1;
                }


                } finally {dbg.exitDecision(54);}

                switch (alt54) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:216:20: 'OR' boolean_factor
            	    {
            	    dbg.location(216,24);
            	    string_literal137=(Token)match(input,81,FOLLOW_81_in_search_condition1430); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal137_tree = (CommonTree)adaptor.create(string_literal137);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal137_tree, root_0);
            	    }
            	    dbg.location(216,26);
            	    pushFollow(FOLLOW_boolean_factor_in_search_condition1433);
            	    boolean_factor138=boolean_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor138.getTree());

            	    }
            	    break;

            	default :
            	    break loop54;
                }
            } while (true);
            } finally {dbg.exitSubRule(54);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(216, 42);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "search_condition");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "search_condition"

    public static class boolean_factor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_factor"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:217:1: boolean_factor : boolean_term ( 'AND' boolean_term )* ;
    public final SQL92QueryParser.boolean_factor_return boolean_factor() throws RecognitionException {
        SQL92QueryParser.boolean_factor_return retval = new SQL92QueryParser.boolean_factor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal140=null;
        SQL92QueryParser.boolean_term_return boolean_term139 = null;

        SQL92QueryParser.boolean_term_return boolean_term141 = null;


        CommonTree string_literal140_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "boolean_factor");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(217, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:218:2: ( boolean_term ( 'AND' boolean_term )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:218:4: boolean_term ( 'AND' boolean_term )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(218,4);
            pushFollow(FOLLOW_boolean_term_in_boolean_factor1443);
            boolean_term139=boolean_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term139.getTree());
            dbg.location(218,17);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:218:17: ( 'AND' boolean_term )*
            try { dbg.enterSubRule(55);

            loop55:
            do {
                int alt55=2;
                try { dbg.enterDecision(55, isCyclicDecision);

                int LA55_0 = input.LA(1);

                if ( (LA55_0==82) ) {
                    alt55=1;
                }


                } finally {dbg.exitDecision(55);}

                switch (alt55) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:218:18: 'AND' boolean_term
            	    {
            	    dbg.location(218,23);
            	    string_literal140=(Token)match(input,82,FOLLOW_82_in_boolean_factor1446); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal140_tree = (CommonTree)adaptor.create(string_literal140);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal140_tree, root_0);
            	    }
            	    dbg.location(218,25);
            	    pushFollow(FOLLOW_boolean_term_in_boolean_factor1449);
            	    boolean_term141=boolean_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term141.getTree());

            	    }
            	    break;

            	default :
            	    break loop55;
                }
            } while (true);
            } finally {dbg.exitSubRule(55);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(219, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "boolean_factor");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "boolean_factor"

    public static class boolean_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_term"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:220:1: boolean_term : ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) );
    public final SQL92QueryParser.boolean_term_return boolean_term() throws RecognitionException {
        SQL92QueryParser.boolean_term_return retval = new SQL92QueryParser.boolean_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal143=null;
        SQL92QueryParser.boolean_test_return boolean_test142 = null;

        SQL92QueryParser.boolean_term_return boolean_term144 = null;


        CommonTree string_literal143_tree=null;
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleSubtreeStream stream_boolean_term=new RewriteRuleSubtreeStream(adaptor,"rule boolean_term");
        try { dbg.enterRule(getGrammarFileName(), "boolean_term");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(220, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:221:2: ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) )
            int alt56=2;
            try { dbg.enterDecision(56, isCyclicDecision);

            int LA56_0 = input.LA(1);

            if ( ((LA56_0>=ID && LA56_0<=STRING)||LA56_0==48||(LA56_0>=57 && LA56_0<=68)||(LA56_0>=70 && LA56_0<=72)||LA56_0==87||(LA56_0>=98 && LA56_0<=99)) ) {
                alt56=1;
            }
            else if ( (LA56_0==83) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(56);}

            switch (alt56) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:221:4: boolean_test
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(221,4);
                    pushFollow(FOLLOW_boolean_test_in_boolean_term1461);
                    boolean_test142=boolean_test();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_test142.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:222:4: 'NOT' boolean_term
                    {
                    dbg.location(222,4);
                    string_literal143=(Token)match(input,83,FOLLOW_83_in_boolean_term1466); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal143);

                    dbg.location(222,10);
                    pushFollow(FOLLOW_boolean_term_in_boolean_term1468);
                    boolean_term144=boolean_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_boolean_term.add(boolean_term144.getTree());


                    // AST REWRITE
                    // elements: boolean_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 222:23: -> ^( NOT boolean_term )
                    {
                        dbg.location(222,26);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:222:26: ^( NOT boolean_term )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(222,28);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        dbg.location(222,32);
                        adaptor.addChild(root_1, stream_boolean_term.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(222, 45);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "boolean_term");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "boolean_term"

    public static class boolean_test_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_test"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:223:1: boolean_test : boolean_primary ;
    public final SQL92QueryParser.boolean_test_return boolean_test() throws RecognitionException {
        SQL92QueryParser.boolean_test_return retval = new SQL92QueryParser.boolean_test_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.boolean_primary_return boolean_primary145 = null;



        try { dbg.enterRule(getGrammarFileName(), "boolean_test");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(223, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:224:2: ( boolean_primary )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:224:4: boolean_primary
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(224,4);
            pushFollow(FOLLOW_boolean_primary_in_boolean_test1484);
            boolean_primary145=boolean_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary145.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(224, 19);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "boolean_test");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "boolean_test"

    public static class boolean_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_primary"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:225:1: boolean_primary : ( predicate | '(' search_condition ')' );
    public final SQL92QueryParser.boolean_primary_return boolean_primary() throws RecognitionException {
        SQL92QueryParser.boolean_primary_return retval = new SQL92QueryParser.boolean_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal147=null;
        Token char_literal149=null;
        SQL92QueryParser.predicate_return predicate146 = null;

        SQL92QueryParser.search_condition_return search_condition148 = null;


        CommonTree char_literal147_tree=null;
        CommonTree char_literal149_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "boolean_primary");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(225, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:226:2: ( predicate | '(' search_condition ')' )
            int alt57=2;
            try { dbg.enterDecision(57, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt57 = dfa57.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(57);}

            switch (alt57) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:226:4: predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(226,4);
                    pushFollow(FOLLOW_predicate_in_boolean_primary1492);
                    predicate146=predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, predicate146.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:226:16: '(' search_condition ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(226,19);
                    char_literal147=(Token)match(input,48,FOLLOW_48_in_boolean_primary1496); if (state.failed) return retval;
                    dbg.location(226,21);
                    pushFollow(FOLLOW_search_condition_in_boolean_primary1499);
                    search_condition148=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition148.getTree());
                    dbg.location(226,41);
                    char_literal149=(Token)match(input,49,FOLLOW_49_in_boolean_primary1501); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(226, 42);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "boolean_primary");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "boolean_primary"

    public static class predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:227:1: predicate : ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate );
    public final SQL92QueryParser.predicate_return predicate() throws RecognitionException {
        SQL92QueryParser.predicate_return retval = new SQL92QueryParser.predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.comparison_predicate_return comparison_predicate150 = null;

        SQL92QueryParser.like_predicate_return like_predicate151 = null;

        SQL92QueryParser.in_predicate_return in_predicate152 = null;

        SQL92QueryParser.null_predicate_return null_predicate153 = null;

        SQL92QueryParser.exists_predicate_return exists_predicate154 = null;

        SQL92QueryParser.between_predicate_return between_predicate155 = null;



        try { dbg.enterRule(getGrammarFileName(), "predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(227, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:2: ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate )
            int alt58=6;
            try { dbg.enterDecision(58, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt58 = dfa58.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(58);}

            switch (alt58) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:4: comparison_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,4);
                    pushFollow(FOLLOW_comparison_predicate_in_predicate1511);
                    comparison_predicate150=comparison_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_predicate150.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:27: like_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,27);
                    pushFollow(FOLLOW_like_predicate_in_predicate1515);
                    like_predicate151=like_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_predicate151.getTree());

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:44: in_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,44);
                    pushFollow(FOLLOW_in_predicate_in_predicate1519);
                    in_predicate152=in_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_predicate152.getTree());

                    }
                    break;
                case 4 :
                    dbg.enterAlt(4);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:59: null_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,59);
                    pushFollow(FOLLOW_null_predicate_in_predicate1523);
                    null_predicate153=null_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_predicate153.getTree());

                    }
                    break;
                case 5 :
                    dbg.enterAlt(5);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:76: exists_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,76);
                    pushFollow(FOLLOW_exists_predicate_in_predicate1527);
                    exists_predicate154=exists_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_predicate154.getTree());

                    }
                    break;
                case 6 :
                    dbg.enterAlt(6);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:95: between_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(228,95);
                    pushFollow(FOLLOW_between_predicate_in_predicate1531);
                    between_predicate155=between_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_predicate155.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(228, 112);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "predicate"

    public static class null_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "null_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:229:1: null_predicate : ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) );
    public final SQL92QueryParser.null_predicate_return null_predicate() throws RecognitionException {
        SQL92QueryParser.null_predicate_return retval = new SQL92QueryParser.null_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal157=null;
        Token string_literal158=null;
        Token string_literal160=null;
        Token string_literal161=null;
        Token string_literal162=null;
        SQL92QueryParser.row_value_return row_value156 = null;

        SQL92QueryParser.row_value_return row_value159 = null;


        CommonTree string_literal157_tree=null;
        CommonTree string_literal158_tree=null;
        CommonTree string_literal160_tree=null;
        CommonTree string_literal161_tree=null;
        CommonTree string_literal162_tree=null;
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
        RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try { dbg.enterRule(getGrammarFileName(), "null_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(229, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:230:2: ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) )
            int alt59=2;
            try { dbg.enterDecision(59, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt59 = dfa59.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(59);}

            switch (alt59) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:230:4: row_value 'IS' 'NULL'
                    {
                    dbg.location(230,4);
                    pushFollow(FOLLOW_row_value_in_null_predicate1539);
                    row_value156=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value156.getTree());
                    dbg.location(230,14);
                    string_literal157=(Token)match(input,84,FOLLOW_84_in_null_predicate1541); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(string_literal157);

                    dbg.location(230,19);
                    string_literal158=(Token)match(input,70,FOLLOW_70_in_null_predicate1543); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(string_literal158);



                    // AST REWRITE
                    // elements: row_value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 230:26: -> ^( IS_NULL row_value )
                    {
                        dbg.location(230,29);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:230:29: ^( IS_NULL row_value )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(230,31);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_1);

                        dbg.location(230,39);
                        adaptor.addChild(root_1, stream_row_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:231:4: row_value 'IS' 'NOT' 'NULL'
                    {
                    dbg.location(231,4);
                    pushFollow(FOLLOW_row_value_in_null_predicate1556);
                    row_value159=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value159.getTree());
                    dbg.location(231,14);
                    string_literal160=(Token)match(input,84,FOLLOW_84_in_null_predicate1558); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(string_literal160);

                    dbg.location(231,19);
                    string_literal161=(Token)match(input,83,FOLLOW_83_in_null_predicate1560); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal161);

                    dbg.location(231,25);
                    string_literal162=(Token)match(input,70,FOLLOW_70_in_null_predicate1562); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(string_literal162);



                    // AST REWRITE
                    // elements: row_value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 231:32: -> ^( NOT ^( IS_NULL row_value ) )
                    {
                        dbg.location(231,35);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:231:35: ^( NOT ^( IS_NULL row_value ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(231,37);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        dbg.location(231,41);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:231:41: ^( IS_NULL row_value )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(231,43);
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_2);

                        dbg.location(231,51);
                        adaptor.addChild(root_2, stream_row_value.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(232, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "null_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "null_predicate"

    public static class in_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:233:1: in_predicate : ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) );
    public final SQL92QueryParser.in_predicate_return in_predicate() throws RecognitionException {
        SQL92QueryParser.in_predicate_return retval = new SQL92QueryParser.in_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal164=null;
        Token string_literal165=null;
        Token string_literal168=null;
        SQL92QueryParser.row_value_return row_value163 = null;

        SQL92QueryParser.in_predicate_tail_return in_predicate_tail166 = null;

        SQL92QueryParser.row_value_return row_value167 = null;

        SQL92QueryParser.in_predicate_tail_return in_predicate_tail169 = null;


        CommonTree string_literal164_tree=null;
        CommonTree string_literal165_tree=null;
        CommonTree string_literal168_tree=null;
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_85=new RewriteRuleTokenStream(adaptor,"token 85");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        RewriteRuleSubtreeStream stream_in_predicate_tail=new RewriteRuleSubtreeStream(adaptor,"rule in_predicate_tail");
        try { dbg.enterRule(getGrammarFileName(), "in_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(233, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:234:2: ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) )
            int alt60=2;
            try { dbg.enterDecision(60, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt60 = dfa60.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(60);}

            switch (alt60) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:234:4: row_value 'NOT' 'IN' in_predicate_tail
                    {
                    dbg.location(234,4);
                    pushFollow(FOLLOW_row_value_in_in_predicate1584);
                    row_value163=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value163.getTree());
                    dbg.location(234,14);
                    string_literal164=(Token)match(input,83,FOLLOW_83_in_in_predicate1586); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal164);

                    dbg.location(234,20);
                    string_literal165=(Token)match(input,85,FOLLOW_85_in_in_predicate1588); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_85.add(string_literal165);

                    dbg.location(234,25);
                    pushFollow(FOLLOW_in_predicate_tail_in_in_predicate1590);
                    in_predicate_tail166=in_predicate_tail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail166.getTree());


                    // AST REWRITE
                    // elements: row_value, 85, in_predicate_tail
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 235:4: -> ^( NOT ^( 'IN' row_value in_predicate_tail ) )
                    {
                        dbg.location(235,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:235:7: ^( NOT ^( 'IN' row_value in_predicate_tail ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(235,9);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        dbg.location(235,13);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:235:13: ^( 'IN' row_value in_predicate_tail )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(235,15);
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_85.nextNode(), root_2);

                        dbg.location(235,20);
                        adaptor.addChild(root_2, stream_row_value.nextTree());
                        dbg.location(235,30);
                        adaptor.addChild(root_2, stream_in_predicate_tail.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:236:4: row_value 'IN' in_predicate_tail
                    {
                    dbg.location(236,4);
                    pushFollow(FOLLOW_row_value_in_in_predicate1612);
                    row_value167=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value167.getTree());
                    dbg.location(236,14);
                    string_literal168=(Token)match(input,85,FOLLOW_85_in_in_predicate1614); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_85.add(string_literal168);

                    dbg.location(236,19);
                    pushFollow(FOLLOW_in_predicate_tail_in_in_predicate1616);
                    in_predicate_tail169=in_predicate_tail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail169.getTree());


                    // AST REWRITE
                    // elements: in_predicate_tail, row_value, 85
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 237:4: -> ^( 'IN' row_value in_predicate_tail )
                    {
                        dbg.location(237,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:237:7: ^( 'IN' row_value in_predicate_tail )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(237,9);
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_85.nextNode(), root_1);

                        dbg.location(237,14);
                        adaptor.addChild(root_1, stream_row_value.nextTree());
                        dbg.location(237,24);
                        adaptor.addChild(root_1, stream_in_predicate_tail.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(238, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "in_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "in_predicate"

    public static class in_predicate_tail_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_predicate_tail"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:239:1: in_predicate_tail : ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) );
    public final SQL92QueryParser.in_predicate_tail_return in_predicate_tail() throws RecognitionException {
        SQL92QueryParser.in_predicate_tail_return retval = new SQL92QueryParser.in_predicate_tail_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal171=null;
        Token char_literal173=null;
        Token char_literal175=null;
        SQL92QueryParser.sub_query_return sub_query170 = null;

        SQL92QueryParser.value_expression_return value_expression172 = null;

        SQL92QueryParser.value_expression_return value_expression174 = null;


        CommonTree char_literal171_tree=null;
        CommonTree char_literal173_tree=null;
        CommonTree char_literal175_tree=null;
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try { dbg.enterRule(getGrammarFileName(), "in_predicate_tail");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(239, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:240:2: ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) )
            int alt62=2;
            try { dbg.enterDecision(62, isCyclicDecision);

            int LA62_0 = input.LA(1);

            if ( (LA62_0==48) ) {
                int LA62_1 = input.LA(2);

                if ( (synpred108_SQL92Query()) ) {
                    alt62=1;
                }
                else if ( (true) ) {
                    alt62=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 62, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(62);}

            switch (alt62) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:240:4: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(240,4);
                    pushFollow(FOLLOW_sub_query_in_in_predicate_tail1639);
                    sub_query170=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query170.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:5: '(' ( value_expression ( ',' value_expression )* ) ')'
                    {
                    dbg.location(241,5);
                    char_literal171=(Token)match(input,48,FOLLOW_48_in_in_predicate_tail1646); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_48.add(char_literal171);

                    dbg.location(241,9);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:9: ( value_expression ( ',' value_expression )* )
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:10: value_expression ( ',' value_expression )*
                    {
                    dbg.location(241,10);
                    pushFollow(FOLLOW_value_expression_in_in_predicate_tail1649);
                    value_expression172=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression172.getTree());
                    dbg.location(241,27);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:27: ( ',' value_expression )*
                    try { dbg.enterSubRule(61);

                    loop61:
                    do {
                        int alt61=2;
                        try { dbg.enterDecision(61, isCyclicDecision);

                        int LA61_0 = input.LA(1);

                        if ( (LA61_0==51) ) {
                            alt61=1;
                        }


                        } finally {dbg.exitDecision(61);}

                        switch (alt61) {
                    	case 1 :
                    	    dbg.enterAlt(1);

                    	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:28: ',' value_expression
                    	    {
                    	    dbg.location(241,28);
                    	    char_literal173=(Token)match(input,51,FOLLOW_51_in_in_predicate_tail1652); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_51.add(char_literal173);

                    	    dbg.location(241,32);
                    	    pushFollow(FOLLOW_value_expression_in_in_predicate_tail1654);
                    	    value_expression174=value_expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value_expression.add(value_expression174.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);
                    } finally {dbg.exitSubRule(61);}


                    }

                    dbg.location(241,52);
                    char_literal175=(Token)match(input,49,FOLLOW_49_in_in_predicate_tail1659); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_49.add(char_literal175);



                    // AST REWRITE
                    // elements: value_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 241:56: -> ^( SET ( value_expression )* )
                    {
                        dbg.location(241,59);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:59: ^( SET ( value_expression )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(241,61);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SET, "SET"), root_1);

                        dbg.location(241,65);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:241:65: ( value_expression )*
                        while ( stream_value_expression.hasNext() ) {
                            dbg.location(241,65);
                            adaptor.addChild(root_1, stream_value_expression.nextTree());

                        }
                        stream_value_expression.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(241, 84);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "in_predicate_tail");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "in_predicate_tail"

    public static class between_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "between_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:242:1: between_predicate : (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) );
    public final SQL92QueryParser.between_predicate_return between_predicate() throws RecognitionException {
        SQL92QueryParser.between_predicate_return retval = new SQL92QueryParser.between_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal176=null;
        Token string_literal177=null;
        Token string_literal178=null;
        Token string_literal179=null;
        Token string_literal180=null;
        SQL92QueryParser.row_value_return value = null;

        SQL92QueryParser.row_value_return btw1 = null;

        SQL92QueryParser.row_value_return btw2 = null;


        CommonTree string_literal176_tree=null;
        CommonTree string_literal177_tree=null;
        CommonTree string_literal178_tree=null;
        CommonTree string_literal179_tree=null;
        CommonTree string_literal180_tree=null;
        RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_86=new RewriteRuleTokenStream(adaptor,"token 86");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try { dbg.enterRule(getGrammarFileName(), "between_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(242, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:243:2: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) )
            int alt63=2;
            try { dbg.enterDecision(63, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt63 = dfa63.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(63);}

            switch (alt63) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:243:4: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
                    {
                    dbg.location(243,9);
                    pushFollow(FOLLOW_row_value_in_between_predicate1679);
                    value=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
                    dbg.location(243,20);
                    string_literal176=(Token)match(input,86,FOLLOW_86_in_between_predicate1681); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_86.add(string_literal176);

                    dbg.location(243,34);
                    pushFollow(FOLLOW_row_value_in_between_predicate1685);
                    btw1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
                    dbg.location(243,45);
                    string_literal177=(Token)match(input,82,FOLLOW_82_in_between_predicate1687); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal177);

                    dbg.location(243,55);
                    pushFollow(FOLLOW_row_value_in_between_predicate1691);
                    btw2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());


                    // AST REWRITE
                    // elements: 86, value, btw2, btw1
                    // token labels: 
                    // rule labels: retval, value, btw2, btw1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);
                    RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.tree:null);
                    RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 244:4: -> ^( 'BETWEEN' $value $btw1 $btw2)
                    {
                        dbg.location(244,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:244:7: ^( 'BETWEEN' $value $btw1 $btw2)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(244,9);
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_86.nextNode(), root_1);

                        dbg.location(244,19);
                        adaptor.addChild(root_1, stream_value.nextTree());
                        dbg.location(244,26);
                        adaptor.addChild(root_1, stream_btw1.nextTree());
                        dbg.location(244,32);
                        adaptor.addChild(root_1, stream_btw2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:245:4: value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value
                    {
                    dbg.location(245,9);
                    pushFollow(FOLLOW_row_value_in_between_predicate1717);
                    value=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
                    dbg.location(245,20);
                    string_literal178=(Token)match(input,83,FOLLOW_83_in_between_predicate1719); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal178);

                    dbg.location(245,26);
                    string_literal179=(Token)match(input,86,FOLLOW_86_in_between_predicate1721); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_86.add(string_literal179);

                    dbg.location(245,40);
                    pushFollow(FOLLOW_row_value_in_between_predicate1725);
                    btw1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
                    dbg.location(245,51);
                    string_literal180=(Token)match(input,82,FOLLOW_82_in_between_predicate1727); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal180);

                    dbg.location(245,61);
                    pushFollow(FOLLOW_row_value_in_between_predicate1731);
                    btw2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());


                    // AST REWRITE
                    // elements: 86, btw2, btw1, value
                    // token labels: 
                    // rule labels: retval, value, btw2, btw1
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);
                    RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.tree:null);
                    RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 246:4: -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
                    {
                        dbg.location(246,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:246:7: ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(246,9);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        dbg.location(246,13);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:246:13: ^( 'BETWEEN' $value $btw1 $btw2)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(246,15);
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_86.nextNode(), root_2);

                        dbg.location(246,25);
                        adaptor.addChild(root_2, stream_value.nextTree());
                        dbg.location(246,32);
                        adaptor.addChild(root_2, stream_btw1.nextTree());
                        dbg.location(246,38);
                        adaptor.addChild(root_2, stream_btw2.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(246, 45);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "between_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "between_predicate"

    public static class exists_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exists_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:247:1: exists_predicate : 'EXISTS' sub_query ;
    public final SQL92QueryParser.exists_predicate_return exists_predicate() throws RecognitionException {
        SQL92QueryParser.exists_predicate_return retval = new SQL92QueryParser.exists_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal181=null;
        SQL92QueryParser.sub_query_return sub_query182 = null;


        CommonTree string_literal181_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "exists_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(247, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:248:2: ( 'EXISTS' sub_query )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:248:4: 'EXISTS' sub_query
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(248,12);
            string_literal181=(Token)match(input,87,FOLLOW_87_in_exists_predicate1761); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal181_tree = (CommonTree)adaptor.create(string_literal181);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal181_tree, root_0);
            }
            dbg.location(248,14);
            pushFollow(FOLLOW_sub_query_in_exists_predicate1764);
            sub_query182=sub_query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query182.getTree());

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(248, 23);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "exists_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "exists_predicate"

    public static class comparison_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "comparison_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:250:1: comparison_predicate : ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value );
    public final SQL92QueryParser.comparison_predicate_return comparison_predicate() throws RecognitionException {
        SQL92QueryParser.comparison_predicate_return retval = new SQL92QueryParser.comparison_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token op=null;
        Token ep=null;
        Token char_literal184=null;
        Token set187=null;
        SQL92QueryParser.row_value_return lv = null;

        SQL92QueryParser.row_value_return rv = null;

        SQL92QueryParser.bind_table_return bind_table183 = null;

        SQL92QueryParser.row_value_return row_value185 = null;

        SQL92QueryParser.row_value_return row_value186 = null;

        SQL92QueryParser.row_value_return row_value188 = null;


        CommonTree op_tree=null;
        CommonTree ep_tree=null;
        CommonTree char_literal184_tree=null;
        CommonTree set187_tree=null;
        RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
        RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
        RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
        RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
        RewriteRuleTokenStream stream_39=new RewriteRuleTokenStream(adaptor,"token 39");
        RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try { dbg.enterRule(getGrammarFileName(), "comparison_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(250, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:251:2: ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value )
            int alt66=3;
            try { dbg.enterDecision(66, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt66 = dfa66.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(66);}

            switch (alt66) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:251:4: bind_table '=' row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(251,4);
                    pushFollow(FOLLOW_bind_table_in_comparison_predicate1773);
                    bind_table183=bind_table();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, bind_table183.getTree());
                    dbg.location(251,18);
                    char_literal184=(Token)match(input,88,FOLLOW_88_in_comparison_predicate1775); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal184_tree = (CommonTree)adaptor.create(char_literal184);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal184_tree, root_0);
                    }
                    dbg.location(251,20);
                    pushFollow(FOLLOW_row_value_in_comparison_predicate1778);
                    row_value185=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value185.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:4: lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value
                    {
                    dbg.location(252,6);
                    pushFollow(FOLLOW_row_value_in_comparison_predicate1785);
                    lv=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(lv.getTree());
                    dbg.location(252,17);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:17: (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' )
                    int alt64=7;
                    try { dbg.enterSubRule(64);
                    try { dbg.enterDecision(64, isCyclicDecision);

                    switch ( input.LA(1) ) {
                    case 88:
                        {
                        alt64=1;
                        }
                        break;
                    case 89:
                        {
                        alt64=2;
                        }
                        break;
                    case 90:
                        {
                        alt64=3;
                        }
                        break;
                    case 91:
                        {
                        alt64=4;
                        }
                        break;
                    case 92:
                        {
                        alt64=5;
                        }
                        break;
                    case 93:
                        {
                        alt64=6;
                        }
                        break;
                    case 94:
                        {
                        alt64=7;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 64, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(64);}

                    switch (alt64) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:18: op= '='
                            {
                            dbg.location(252,20);
                            op=(Token)match(input,88,FOLLOW_88_in_comparison_predicate1790); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_88.add(op);


                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:25: op= '<>'
                            {
                            dbg.location(252,27);
                            op=(Token)match(input,89,FOLLOW_89_in_comparison_predicate1794); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_89.add(op);


                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:33: op= '!='
                            {
                            dbg.location(252,35);
                            op=(Token)match(input,90,FOLLOW_90_in_comparison_predicate1798); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_90.add(op);


                            }
                            break;
                        case 4 :
                            dbg.enterAlt(4);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:41: op= '<'
                            {
                            dbg.location(252,43);
                            op=(Token)match(input,91,FOLLOW_91_in_comparison_predicate1802); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_91.add(op);


                            }
                            break;
                        case 5 :
                            dbg.enterAlt(5);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:48: op= '>'
                            {
                            dbg.location(252,50);
                            op=(Token)match(input,92,FOLLOW_92_in_comparison_predicate1806); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_92.add(op);


                            }
                            break;
                        case 6 :
                            dbg.enterAlt(6);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:55: op= '>='
                            {
                            dbg.location(252,57);
                            op=(Token)match(input,93,FOLLOW_93_in_comparison_predicate1810); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_93.add(op);


                            }
                            break;
                        case 7 :
                            dbg.enterAlt(7);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:63: op= '<='
                            {
                            dbg.location(252,65);
                            op=(Token)match(input,94,FOLLOW_94_in_comparison_predicate1814); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_94.add(op);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(64);}

                    dbg.location(252,72);
                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:72: (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' )
                    int alt65=3;
                    try { dbg.enterSubRule(65);
                    try { dbg.enterDecision(65, isCyclicDecision);

                    switch ( input.LA(1) ) {
                    case 39:
                        {
                        alt65=1;
                        }
                        break;
                    case 95:
                        {
                        alt65=2;
                        }
                        break;
                    case 96:
                        {
                        alt65=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 65, 0, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }

                    } finally {dbg.exitDecision(65);}

                    switch (alt65) {
                        case 1 :
                            dbg.enterAlt(1);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:73: ep= 'ALL'
                            {
                            dbg.location(252,75);
                            ep=(Token)match(input,39,FOLLOW_39_in_comparison_predicate1820); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_39.add(ep);


                            }
                            break;
                        case 2 :
                            dbg.enterAlt(2);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:82: ep= 'SOME'
                            {
                            dbg.location(252,84);
                            ep=(Token)match(input,95,FOLLOW_95_in_comparison_predicate1824); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_95.add(ep);


                            }
                            break;
                        case 3 :
                            dbg.enterAlt(3);

                            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:92: ep= 'ANY'
                            {
                            dbg.location(252,94);
                            ep=(Token)match(input,96,FOLLOW_96_in_comparison_predicate1828); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_96.add(ep);


                            }
                            break;

                    }
                    } finally {dbg.exitSubRule(65);}

                    dbg.location(252,104);
                    pushFollow(FOLLOW_row_value_in_comparison_predicate1833);
                    rv=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(rv.getTree());


                    // AST REWRITE
                    // elements: op, rv, ep, lv
                    // token labels: op, ep
                    // rule labels: retval, lv, rv
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
                    RewriteRuleTokenStream stream_ep=new RewriteRuleTokenStream(adaptor,"token ep",ep);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_lv=new RewriteRuleSubtreeStream(adaptor,"rule lv",lv!=null?lv.tree:null);
                    RewriteRuleSubtreeStream stream_rv=new RewriteRuleSubtreeStream(adaptor,"rule rv",rv!=null?rv.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 253:4: -> ^( $ep ^( $op $lv $rv) )
                    {
                        dbg.location(253,7);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:253:7: ^( $ep ^( $op $lv $rv) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(253,9);
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_ep.nextNode(), root_1);

                        dbg.location(253,13);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:253:13: ^( $op $lv $rv)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(253,15);
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_op.nextNode(), root_2);

                        dbg.location(253,19);
                        adaptor.addChild(root_2, stream_lv.nextTree());
                        dbg.location(253,23);
                        adaptor.addChild(root_2, stream_rv.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:254:4: row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(254,4);
                    pushFollow(FOLLOW_row_value_in_comparison_predicate1859);
                    row_value186=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value186.getTree());
                    dbg.location(254,14);
                    set187=(Token)input.LT(1);
                    set187=(Token)input.LT(1);
                    if ( (input.LA(1)>=88 && input.LA(1)<=94) ) {
                        input.consume();
                        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set187), root_0);
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        dbg.recognitionException(mse);
                        throw mse;
                    }

                    dbg.location(254,61);
                    pushFollow(FOLLOW_row_value_in_comparison_predicate1890);
                    row_value188=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value188.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(254, 70);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "comparison_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "comparison_predicate"

    public static class like_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "like_predicate"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:258:1: like_predicate : ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) );
    public final SQL92QueryParser.like_predicate_return like_predicate() throws RecognitionException {
        SQL92QueryParser.like_predicate_return retval = new SQL92QueryParser.like_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal190=null;
        Token string_literal192=null;
        Token string_literal193=null;
        SQL92QueryParser.row_value_return v1 = null;

        SQL92QueryParser.row_value_return v2 = null;

        SQL92QueryParser.row_value_return row_value189 = null;

        SQL92QueryParser.row_value_return row_value191 = null;


        CommonTree string_literal190_tree=null;
        CommonTree string_literal192_tree=null;
        CommonTree string_literal193_tree=null;
        RewriteRuleTokenStream stream_97=new RewriteRuleTokenStream(adaptor,"token 97");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try { dbg.enterRule(getGrammarFileName(), "like_predicate");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(258, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:259:2: ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) )
            int alt67=2;
            try { dbg.enterDecision(67, isCyclicDecision);

            try {
                isCyclicDecision = true;
                alt67 = dfa67.predict(input);
            }
            catch (NoViableAltException nvae) {
                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(67);}

            switch (alt67) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:259:4: row_value 'LIKE' row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(259,4);
                    pushFollow(FOLLOW_row_value_in_like_predicate1901);
                    row_value189=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value189.getTree());
                    dbg.location(259,20);
                    string_literal190=(Token)match(input,97,FOLLOW_97_in_like_predicate1903); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal190_tree = (CommonTree)adaptor.create(string_literal190);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal190_tree, root_0);
                    }
                    dbg.location(259,22);
                    pushFollow(FOLLOW_row_value_in_like_predicate1906);
                    row_value191=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value191.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:260:4: v1= row_value 'NOT' 'LIKE' v2= row_value
                    {
                    dbg.location(260,6);
                    pushFollow(FOLLOW_row_value_in_like_predicate1913);
                    v1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(v1.getTree());
                    dbg.location(260,17);
                    string_literal192=(Token)match(input,83,FOLLOW_83_in_like_predicate1915); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal192);

                    dbg.location(260,23);
                    string_literal193=(Token)match(input,97,FOLLOW_97_in_like_predicate1917); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_97.add(string_literal193);

                    dbg.location(260,32);
                    pushFollow(FOLLOW_row_value_in_like_predicate1921);
                    v2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(v2.getTree());


                    // AST REWRITE
                    // elements: 97, v1, v2
                    // token labels: 
                    // rule labels: v1, retval, v2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_v1=new RewriteRuleSubtreeStream(adaptor,"rule v1",v1!=null?v1.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_v2=new RewriteRuleSubtreeStream(adaptor,"rule v2",v2!=null?v2.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 260:43: -> ^( NOT ^( 'LIKE' $v1 $v2) )
                    {
                        dbg.location(260,46);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:260:46: ^( NOT ^( 'LIKE' $v1 $v2) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        dbg.location(260,48);
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        dbg.location(260,52);
                        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:260:52: ^( 'LIKE' $v1 $v2)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        dbg.location(260,54);
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_97.nextNode(), root_2);

                        dbg.location(260,61);
                        adaptor.addChild(root_2, stream_v1.nextTree());
                        dbg.location(260,65);
                        adaptor.addChild(root_2, stream_v2.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(260, 70);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "like_predicate");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "like_predicate"

    public static class row_value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "row_value"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:266:1: row_value : ( value_expression | 'NULL' | 'DEFAULT' );
    public final SQL92QueryParser.row_value_return row_value() throws RecognitionException {
        SQL92QueryParser.row_value_return retval = new SQL92QueryParser.row_value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal195=null;
        Token string_literal196=null;
        SQL92QueryParser.value_expression_return value_expression194 = null;


        CommonTree string_literal195_tree=null;
        CommonTree string_literal196_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "row_value");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(266, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:2: ( value_expression | 'NULL' | 'DEFAULT' )
            int alt68=3;
            try { dbg.enterDecision(68, isCyclicDecision);

            switch ( input.LA(1) ) {
            case ID:
            case INT:
            case FLOAT:
            case NUMERIC:
            case STRING:
            case 48:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 71:
            case 72:
                {
                alt68=1;
                }
                break;
            case 70:
                {
                int LA68_2 = input.LA(2);

                if ( (synpred128_SQL92Query()) ) {
                    alt68=1;
                }
                else if ( (synpred129_SQL92Query()) ) {
                    alt68=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 68, 2, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
                }
                break;
            case 98:
                {
                alt68=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }

            } finally {dbg.exitDecision(68);}

            switch (alt68) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:4: value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(267,4);
                    pushFollow(FOLLOW_value_expression_in_row_value1950);
                    value_expression194=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression194.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:22: 'NULL'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(267,22);
                    string_literal195=(Token)match(input,70,FOLLOW_70_in_row_value1953); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal195_tree = (CommonTree)adaptor.create(string_literal195);
                    adaptor.addChild(root_0, string_literal195_tree);
                    }

                    }
                    break;
                case 3 :
                    dbg.enterAlt(3);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:31: 'DEFAULT'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    dbg.location(267,31);
                    string_literal196=(Token)match(input,98,FOLLOW_98_in_row_value1957); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal196_tree = (CommonTree)adaptor.create(string_literal196);
                    adaptor.addChild(root_0, string_literal196_tree);
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(267, 41);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "row_value");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "row_value"

    public static class bind_table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bind_table"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:269:1: bind_table : '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) ;
    public final SQL92QueryParser.bind_table_return bind_table() throws RecognitionException {
        SQL92QueryParser.bind_table_return retval = new SQL92QueryParser.bind_table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token columnid=null;
        Token char_literal197=null;
        Token char_literal198=null;

        CommonTree tableid_tree=null;
        CommonTree columnid_tree=null;
        CommonTree char_literal197_tree=null;
        CommonTree char_literal198_tree=null;
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_99=new RewriteRuleTokenStream(adaptor,"token 99");

        try { dbg.enterRule(getGrammarFileName(), "bind_table");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(269, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:270:2: ( '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:270:4: '@' tableid= ID '.' columnid= ID
            {
            dbg.location(270,4);
            char_literal197=(Token)match(input,99,FOLLOW_99_in_bind_table1967); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_99.add(char_literal197);

            dbg.location(270,14);
            tableid=(Token)match(input,ID,FOLLOW_ID_in_bind_table1970); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(tableid);

            dbg.location(270,17);
            char_literal198=(Token)match(input,56,FOLLOW_56_in_bind_table1971); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_56.add(char_literal198);

            dbg.location(270,28);
            columnid=(Token)match(input,ID,FOLLOW_ID_in_bind_table1974); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(columnid);



            // AST REWRITE
            // elements: columnid, tableid
            // token labels: tableid, columnid
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleTokenStream stream_columnid=new RewriteRuleTokenStream(adaptor,"token columnid",columnid);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 270:32: -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
            {
                dbg.location(270,35);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:270:35: ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(270,37);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOUND, "BOUND"), root_1);

                dbg.location(270,43);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:270:43: ^( TABLECOLUMN $tableid $columnid)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                dbg.location(270,45);
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_2);

                dbg.location(270,57);
                adaptor.addChild(root_2, stream_tableid.nextNode());
                dbg.location(270,66);
                adaptor.addChild(root_2, stream_columnid.nextNode());

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(271, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "bind_table");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "bind_table"

    public static class correlation_specification_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "correlation_specification"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:273:1: correlation_specification : ( 'AS' )? ID ;
    public final SQL92QueryParser.correlation_specification_return correlation_specification() throws RecognitionException {
        SQL92QueryParser.correlation_specification_return retval = new SQL92QueryParser.correlation_specification_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal199=null;
        Token ID200=null;

        CommonTree string_literal199_tree=null;
        CommonTree ID200_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "correlation_specification");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(273, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:274:2: ( ( 'AS' )? ID )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:274:4: ( 'AS' )? ID
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(274,4);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:274:4: ( 'AS' )?
            int alt69=2;
            try { dbg.enterSubRule(69);
            try { dbg.enterDecision(69, isCyclicDecision);

            int LA69_0 = input.LA(1);

            if ( (LA69_0==53) ) {
                alt69=1;
            }
            } finally {dbg.exitDecision(69);}

            switch (alt69) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:274:5: 'AS'
                    {
                    dbg.location(274,9);
                    string_literal199=(Token)match(input,53,FOLLOW_53_in_correlation_specification2002); if (state.failed) return retval;

                    }
                    break;

            }
            } finally {dbg.exitSubRule(69);}

            dbg.location(274,13);
            ID200=(Token)match(input,ID,FOLLOW_ID_in_correlation_specification2007); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID200_tree = (CommonTree)adaptor.create(ID200);
            adaptor.addChild(root_0, ID200_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(274, 15);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "correlation_specification");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "correlation_specification"

    public static class table_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_name"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:275:1: table_name : ID ;
    public final SQL92QueryParser.table_name_return table_name() throws RecognitionException {
        SQL92QueryParser.table_name_return retval = new SQL92QueryParser.table_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID201=null;

        CommonTree ID201_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "table_name");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(275, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:276:2: ( ID )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:276:4: ID
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(276,4);
            ID201=(Token)match(input,ID,FOLLOW_ID_in_table_name2016); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID201_tree = (CommonTree)adaptor.create(ID201);
            adaptor.addChild(root_0, ID201_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(276, 6);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "table_name");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "table_name"

    public static class column_list_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "column_list"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:277:1: column_list : ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )* ;
    public final SQL92QueryParser.column_list_return column_list() throws RecognitionException {
        SQL92QueryParser.column_list_return retval = new SQL92QueryParser.column_list_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal204=null;
        SQL92QueryParser.column_name_return column_name202 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name203 = null;

        SQL92QueryParser.column_name_return column_name205 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name206 = null;


        CommonTree char_literal204_tree=null;

        try { dbg.enterRule(getGrammarFileName(), "column_list");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(277, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:2: ( ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )* )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:4: ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )*
            {
            root_0 = (CommonTree)adaptor.nil();

            dbg.location(278,4);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:4: ( column_name | reserved_word_column_name )
            int alt70=2;
            try { dbg.enterSubRule(70);
            try { dbg.enterDecision(70, isCyclicDecision);

            int LA70_0 = input.LA(1);

            if ( (LA70_0==ID) ) {
                int LA70_1 = input.LA(2);

                if ( (LA70_1==56) ) {
                    int LA70_3 = input.LA(3);

                    if ( (LA70_3==ID) ) {
                        alt70=1;
                    }
                    else if ( ((LA70_3>=57 && LA70_3<=66)) ) {
                        alt70=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 70, 3, input);

                        dbg.recognitionException(nvae);
                        throw nvae;
                    }
                }
                else if ( (LA70_1==EOF||(LA70_1>=37 && LA70_1<=38)||(LA70_1>=40 && LA70_1<=41)||LA70_1==46||LA70_1==49||LA70_1==51||LA70_1==54) ) {
                    alt70=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 70, 1, input);

                    dbg.recognitionException(nvae);
                    throw nvae;
                }
            }
            else if ( ((LA70_0>=57 && LA70_0<=66)) ) {
                alt70=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 70, 0, input);

                dbg.recognitionException(nvae);
                throw nvae;
            }
            } finally {dbg.exitDecision(70);}

            switch (alt70) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:5: column_name
                    {
                    dbg.location(278,5);
                    pushFollow(FOLLOW_column_name_in_column_list2025);
                    column_name202=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name202.getTree());

                    }
                    break;
                case 2 :
                    dbg.enterAlt(2);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:19: reserved_word_column_name
                    {
                    dbg.location(278,19);
                    pushFollow(FOLLOW_reserved_word_column_name_in_column_list2029);
                    reserved_word_column_name203=reserved_word_column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name203.getTree());

                    }
                    break;

            }
            } finally {dbg.exitSubRule(70);}

            dbg.location(278,46);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:46: ( ',' ( column_name | reserved_word_column_name ) )*
            try { dbg.enterSubRule(72);

            loop72:
            do {
                int alt72=2;
                try { dbg.enterDecision(72, isCyclicDecision);

                int LA72_0 = input.LA(1);

                if ( (LA72_0==51) ) {
                    alt72=1;
                }


                } finally {dbg.exitDecision(72);}

                switch (alt72) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:47: ',' ( column_name | reserved_word_column_name )
            	    {
            	    dbg.location(278,50);
            	    char_literal204=(Token)match(input,51,FOLLOW_51_in_column_list2033); if (state.failed) return retval;
            	    dbg.location(278,52);
            	    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:52: ( column_name | reserved_word_column_name )
            	    int alt71=2;
            	    try { dbg.enterSubRule(71);
            	    try { dbg.enterDecision(71, isCyclicDecision);

            	    int LA71_0 = input.LA(1);

            	    if ( (LA71_0==ID) ) {
            	        int LA71_1 = input.LA(2);

            	        if ( (LA71_1==56) ) {
            	            int LA71_3 = input.LA(3);

            	            if ( ((LA71_3>=57 && LA71_3<=66)) ) {
            	                alt71=2;
            	            }
            	            else if ( (LA71_3==ID) ) {
            	                alt71=1;
            	            }
            	            else {
            	                if (state.backtracking>0) {state.failed=true; return retval;}
            	                NoViableAltException nvae =
            	                    new NoViableAltException("", 71, 3, input);

            	                dbg.recognitionException(nvae);
            	                throw nvae;
            	            }
            	        }
            	        else if ( (LA71_1==EOF||(LA71_1>=37 && LA71_1<=38)||(LA71_1>=40 && LA71_1<=41)||LA71_1==46||LA71_1==49||LA71_1==51||LA71_1==54) ) {
            	            alt71=1;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return retval;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 71, 1, input);

            	            dbg.recognitionException(nvae);
            	            throw nvae;
            	        }
            	    }
            	    else if ( ((LA71_0>=57 && LA71_0<=66)) ) {
            	        alt71=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 71, 0, input);

            	        dbg.recognitionException(nvae);
            	        throw nvae;
            	    }
            	    } finally {dbg.exitDecision(71);}

            	    switch (alt71) {
            	        case 1 :
            	            dbg.enterAlt(1);

            	            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:53: column_name
            	            {
            	            dbg.location(278,53);
            	            pushFollow(FOLLOW_column_name_in_column_list2037);
            	            column_name205=column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name205.getTree());

            	            }
            	            break;
            	        case 2 :
            	            dbg.enterAlt(2);

            	            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:278:67: reserved_word_column_name
            	            {
            	            dbg.location(278,67);
            	            pushFollow(FOLLOW_reserved_word_column_name_in_column_list2041);
            	            reserved_word_column_name206=reserved_word_column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name206.getTree());

            	            }
            	            break;

            	    }
            	    } finally {dbg.exitSubRule(71);}


            	    }
            	    break;

            	default :
            	    break loop72;
                }
            } while (true);
            } finally {dbg.exitSubRule(72);}


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(278, 95);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "column_list");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "column_list"

    public static class column_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "column_name"
    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:279:1: column_name : (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) ;
    public final SQL92QueryParser.column_name_return column_name() throws RecognitionException {
        SQL92QueryParser.column_name_return retval = new SQL92QueryParser.column_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token columnid=null;
        Token char_literal207=null;

        CommonTree tableid_tree=null;
        CommonTree columnid_tree=null;
        CommonTree char_literal207_tree=null;
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try { dbg.enterRule(getGrammarFileName(), "column_name");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(279, 1);

        try {
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:2: ( (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) )
            dbg.enterAlt(1);

            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:4: (tableid= ID '.' )? columnid= ID
            {
            dbg.location(280,4);
            // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:4: (tableid= ID '.' )?
            int alt73=2;
            try { dbg.enterSubRule(73);
            try { dbg.enterDecision(73, isCyclicDecision);

            int LA73_0 = input.LA(1);

            if ( (LA73_0==ID) ) {
                int LA73_1 = input.LA(2);

                if ( (LA73_1==56) ) {
                    alt73=1;
                }
            }
            } finally {dbg.exitDecision(73);}

            switch (alt73) {
                case 1 :
                    dbg.enterAlt(1);

                    // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:5: tableid= ID '.'
                    {
                    dbg.location(280,12);
                    tableid=(Token)match(input,ID,FOLLOW_ID_in_column_name2055); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(tableid);

                    dbg.location(280,15);
                    char_literal207=(Token)match(input,56,FOLLOW_56_in_column_name2056); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_56.add(char_literal207);


                    }
                    break;

            }
            } finally {dbg.exitSubRule(73);}

            dbg.location(280,28);
            columnid=(Token)match(input,ID,FOLLOW_ID_in_column_name2061); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(columnid);



            // AST REWRITE
            // elements: columnid, tableid
            // token labels: tableid, columnid
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleTokenStream stream_columnid=new RewriteRuleTokenStream(adaptor,"token columnid",columnid);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 280:32: -> ^( TABLECOLUMN ( $tableid)? $columnid)
            {
                dbg.location(280,35);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:35: ^( TABLECOLUMN ( $tableid)? $columnid)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                dbg.location(280,37);
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                dbg.location(280,49);
                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:280:49: ( $tableid)?
                if ( stream_tableid.hasNext() ) {
                    dbg.location(280,49);
                    adaptor.addChild(root_1, stream_tableid.nextNode());

                }
                stream_tableid.reset();
                dbg.location(280,59);
                adaptor.addChild(root_1, stream_columnid.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;}
            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }

        catch (RecognitionException re)
        {
            reportError(re);
            throw re;
        }
        finally {
        }
        dbg.location(281, 2);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "column_name");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return retval;
    }
    // $ANTLR end "column_name"

    // $ANTLR start synpred36_SQL92Query
    public final void synpred36_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:13: ( ( '+' | '-' ) factor )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:139:13: ( '+' | '-' ) factor
        {
        dbg.location(139,13);
        if ( (input.LA(1)>=67 && input.LA(1)<=68) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            dbg.recognitionException(mse);
            throw mse;
        }

        dbg.location(139,24);
        pushFollow(FOLLOW_factor_in_synpred36_SQL92Query698);
        factor();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred36_SQL92Query

    // $ANTLR start synpred41_SQL92Query
    public final void synpred41_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:146:4: ( '(' value_expression ')' )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:146:4: '(' value_expression ')'
        {
        dbg.location(146,4);
        match(input,48,FOLLOW_48_in_synpred41_SQL92Query751); if (state.failed) return ;
        dbg.location(146,9);
        pushFollow(FOLLOW_value_expression_in_synpred41_SQL92Query754);
        value_expression();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(146,26);
        match(input,49,FOLLOW_49_in_synpred41_SQL92Query756); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred41_SQL92Query

    // $ANTLR start synpred42_SQL92Query
    public final void synpred42_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:147:5: ( function )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:147:5: function
        {
        dbg.location(147,5);
        pushFollow(FOLLOW_function_in_synpred42_SQL92Query763);
        function();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred42_SQL92Query

    // $ANTLR start synpred43_SQL92Query
    public final void synpred43_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:148:5: ( column_name )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:148:5: column_name
        {
        dbg.location(148,5);
        pushFollow(FOLLOW_column_name_in_synpred43_SQL92Query769);
        column_name();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred43_SQL92Query

    // $ANTLR start synpred44_SQL92Query
    public final void synpred44_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:149:5: ( literal )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:149:5: literal
        {
        dbg.location(149,5);
        pushFollow(FOLLOW_literal_in_synpred44_SQL92Query775);
        literal();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred44_SQL92Query

    // $ANTLR start synpred55_SQL92Query
    public final void synpred55_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:155:4: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:155:4: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING
        {
        dbg.location(155,4);
        if ( (input.LA(1)>=57 && input.LA(1)<=59) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            dbg.recognitionException(mse);
            throw mse;
        }

        dbg.location(155,37);
        match(input,STRING,FOLLOW_STRING_in_synpred55_SQL92Query844); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred55_SQL92Query

    // $ANTLR start synpred64_SQL92Query
    public final void synpred64_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:159:4: ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:159:4: 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
        {
        dbg.location(159,4);
        match(input,60,FOLLOW_60_in_synpred64_SQL92Query896); if (state.failed) return ;
        dbg.location(159,16);
        match(input,STRING,FOLLOW_STRING_in_synpred64_SQL92Query899); if (state.failed) return ;
        dbg.location(159,23);
        if ( (input.LA(1)>=61 && input.LA(1)<=66) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            dbg.recognitionException(mse);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred64_SQL92Query

    // $ANTLR start synpred78_SQL92Query
    public final void synpred78_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:11: ( ',' table_reference )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:176:11: ',' table_reference
        {
        dbg.location(176,11);
        match(input,51,FOLLOW_51_in_synpred78_SQL92Query1116); if (state.failed) return ;
        dbg.location(176,16);
        pushFollow(FOLLOW_table_reference_in_synpred78_SQL92Query1119);
        table_reference();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred78_SQL92Query

    // $ANTLR start synpred90_SQL92Query
    public final void synpred90_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:16: ( table_function_subquery )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:16: table_function_subquery
        {
        dbg.location(196,16);
        pushFollow(FOLLOW_table_function_subquery_in_synpred90_SQL92Query1286);
        table_function_subquery();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred90_SQL92Query

    // $ANTLR start synpred91_SQL92Query
    public final void synpred91_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:42: ( ',' table_function_subquery )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:196:42: ',' table_function_subquery
        {
        dbg.location(196,42);
        match(input,51,FOLLOW_51_in_synpred91_SQL92Query1290); if (state.failed) return ;
        dbg.location(196,46);
        pushFollow(FOLLOW_table_function_subquery_in_synpred91_SQL92Query1292);
        table_function_subquery();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred91_SQL92Query

    // $ANTLR start synpred94_SQL92Query
    public final void synpred94_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:205:4: ( search_condition )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:205:4: search_condition
        {
        dbg.location(205,4);
        pushFollow(FOLLOW_search_condition_in_synpred94_SQL92Query1359);
        search_condition();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred94_SQL92Query

    // $ANTLR start synpred100_SQL92Query
    public final void synpred100_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:226:4: ( predicate )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:226:4: predicate
        {
        dbg.location(226,4);
        pushFollow(FOLLOW_predicate_in_synpred100_SQL92Query1492);
        predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred100_SQL92Query

    // $ANTLR start synpred101_SQL92Query
    public final void synpred101_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:4: ( comparison_predicate )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:4: comparison_predicate
        {
        dbg.location(228,4);
        pushFollow(FOLLOW_comparison_predicate_in_synpred101_SQL92Query1511);
        comparison_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred101_SQL92Query

    // $ANTLR start synpred102_SQL92Query
    public final void synpred102_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:27: ( like_predicate )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:27: like_predicate
        {
        dbg.location(228,27);
        pushFollow(FOLLOW_like_predicate_in_synpred102_SQL92Query1515);
        like_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred102_SQL92Query

    // $ANTLR start synpred103_SQL92Query
    public final void synpred103_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:44: ( in_predicate )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:44: in_predicate
        {
        dbg.location(228,44);
        pushFollow(FOLLOW_in_predicate_in_synpred103_SQL92Query1519);
        in_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred103_SQL92Query

    // $ANTLR start synpred104_SQL92Query
    public final void synpred104_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:59: ( null_predicate )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:228:59: null_predicate
        {
        dbg.location(228,59);
        pushFollow(FOLLOW_null_predicate_in_synpred104_SQL92Query1523);
        null_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred104_SQL92Query

    // $ANTLR start synpred106_SQL92Query
    public final void synpred106_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:230:4: ( row_value 'IS' 'NULL' )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:230:4: row_value 'IS' 'NULL'
        {
        dbg.location(230,4);
        pushFollow(FOLLOW_row_value_in_synpred106_SQL92Query1539);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(230,14);
        match(input,84,FOLLOW_84_in_synpred106_SQL92Query1541); if (state.failed) return ;
        dbg.location(230,19);
        match(input,70,FOLLOW_70_in_synpred106_SQL92Query1543); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred106_SQL92Query

    // $ANTLR start synpred107_SQL92Query
    public final void synpred107_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:234:4: ( row_value 'NOT' 'IN' in_predicate_tail )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:234:4: row_value 'NOT' 'IN' in_predicate_tail
        {
        dbg.location(234,4);
        pushFollow(FOLLOW_row_value_in_synpred107_SQL92Query1584);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(234,14);
        match(input,83,FOLLOW_83_in_synpred107_SQL92Query1586); if (state.failed) return ;
        dbg.location(234,20);
        match(input,85,FOLLOW_85_in_synpred107_SQL92Query1588); if (state.failed) return ;
        dbg.location(234,25);
        pushFollow(FOLLOW_in_predicate_tail_in_synpred107_SQL92Query1590);
        in_predicate_tail();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred107_SQL92Query

    // $ANTLR start synpred108_SQL92Query
    public final void synpred108_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:240:4: ( sub_query )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:240:4: sub_query
        {
        dbg.location(240,4);
        pushFollow(FOLLOW_sub_query_in_synpred108_SQL92Query1639);
        sub_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred108_SQL92Query

    // $ANTLR start synpred110_SQL92Query
    public final void synpred110_SQL92Query_fragment() throws RecognitionException {   
        SQL92QueryParser.row_value_return value = null;

        SQL92QueryParser.row_value_return btw1 = null;

        SQL92QueryParser.row_value_return btw2 = null;


        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:243:4: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:243:4: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
        {
        dbg.location(243,9);
        pushFollow(FOLLOW_row_value_in_synpred110_SQL92Query1679);
        value=row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(243,20);
        match(input,86,FOLLOW_86_in_synpred110_SQL92Query1681); if (state.failed) return ;
        dbg.location(243,34);
        pushFollow(FOLLOW_row_value_in_synpred110_SQL92Query1685);
        btw1=row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(243,45);
        match(input,82,FOLLOW_82_in_synpred110_SQL92Query1687); if (state.failed) return ;
        dbg.location(243,55);
        pushFollow(FOLLOW_row_value_in_synpred110_SQL92Query1691);
        btw2=row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred110_SQL92Query

    // $ANTLR start synpred120_SQL92Query
    public final void synpred120_SQL92Query_fragment() throws RecognitionException {   
        Token op=null;
        Token ep=null;
        SQL92QueryParser.row_value_return lv = null;

        SQL92QueryParser.row_value_return rv = null;


        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:4: (lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:4: lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value
        {
        dbg.location(252,6);
        pushFollow(FOLLOW_row_value_in_synpred120_SQL92Query1785);
        lv=row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(252,17);
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:17: (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' )
        int alt86=7;
        try { dbg.enterSubRule(86);
        try { dbg.enterDecision(86, isCyclicDecision);

        switch ( input.LA(1) ) {
        case 88:
            {
            alt86=1;
            }
            break;
        case 89:
            {
            alt86=2;
            }
            break;
        case 90:
            {
            alt86=3;
            }
            break;
        case 91:
            {
            alt86=4;
            }
            break;
        case 92:
            {
            alt86=5;
            }
            break;
        case 93:
            {
            alt86=6;
            }
            break;
        case 94:
            {
            alt86=7;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 86, 0, input);

            dbg.recognitionException(nvae);
            throw nvae;
        }

        } finally {dbg.exitDecision(86);}

        switch (alt86) {
            case 1 :
                dbg.enterAlt(1);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:18: op= '='
                {
                dbg.location(252,20);
                op=(Token)match(input,88,FOLLOW_88_in_synpred120_SQL92Query1790); if (state.failed) return ;

                }
                break;
            case 2 :
                dbg.enterAlt(2);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:25: op= '<>'
                {
                dbg.location(252,27);
                op=(Token)match(input,89,FOLLOW_89_in_synpred120_SQL92Query1794); if (state.failed) return ;

                }
                break;
            case 3 :
                dbg.enterAlt(3);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:33: op= '!='
                {
                dbg.location(252,35);
                op=(Token)match(input,90,FOLLOW_90_in_synpred120_SQL92Query1798); if (state.failed) return ;

                }
                break;
            case 4 :
                dbg.enterAlt(4);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:41: op= '<'
                {
                dbg.location(252,43);
                op=(Token)match(input,91,FOLLOW_91_in_synpred120_SQL92Query1802); if (state.failed) return ;

                }
                break;
            case 5 :
                dbg.enterAlt(5);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:48: op= '>'
                {
                dbg.location(252,50);
                op=(Token)match(input,92,FOLLOW_92_in_synpred120_SQL92Query1806); if (state.failed) return ;

                }
                break;
            case 6 :
                dbg.enterAlt(6);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:55: op= '>='
                {
                dbg.location(252,57);
                op=(Token)match(input,93,FOLLOW_93_in_synpred120_SQL92Query1810); if (state.failed) return ;

                }
                break;
            case 7 :
                dbg.enterAlt(7);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:63: op= '<='
                {
                dbg.location(252,65);
                op=(Token)match(input,94,FOLLOW_94_in_synpred120_SQL92Query1814); if (state.failed) return ;

                }
                break;

        }
        } finally {dbg.exitSubRule(86);}

        dbg.location(252,72);
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:72: (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' )
        int alt87=3;
        try { dbg.enterSubRule(87);
        try { dbg.enterDecision(87, isCyclicDecision);

        switch ( input.LA(1) ) {
        case 39:
            {
            alt87=1;
            }
            break;
        case 95:
            {
            alt87=2;
            }
            break;
        case 96:
            {
            alt87=3;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 87, 0, input);

            dbg.recognitionException(nvae);
            throw nvae;
        }

        } finally {dbg.exitDecision(87);}

        switch (alt87) {
            case 1 :
                dbg.enterAlt(1);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:73: ep= 'ALL'
                {
                dbg.location(252,75);
                ep=(Token)match(input,39,FOLLOW_39_in_synpred120_SQL92Query1820); if (state.failed) return ;

                }
                break;
            case 2 :
                dbg.enterAlt(2);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:82: ep= 'SOME'
                {
                dbg.location(252,84);
                ep=(Token)match(input,95,FOLLOW_95_in_synpred120_SQL92Query1824); if (state.failed) return ;

                }
                break;
            case 3 :
                dbg.enterAlt(3);

                // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:252:92: ep= 'ANY'
                {
                dbg.location(252,94);
                ep=(Token)match(input,96,FOLLOW_96_in_synpred120_SQL92Query1828); if (state.failed) return ;

                }
                break;

        }
        } finally {dbg.exitSubRule(87);}

        dbg.location(252,104);
        pushFollow(FOLLOW_row_value_in_synpred120_SQL92Query1833);
        rv=row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred120_SQL92Query

    // $ANTLR start synpred127_SQL92Query
    public final void synpred127_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:259:4: ( row_value 'LIKE' row_value )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:259:4: row_value 'LIKE' row_value
        {
        dbg.location(259,4);
        pushFollow(FOLLOW_row_value_in_synpred127_SQL92Query1901);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        dbg.location(259,14);
        match(input,97,FOLLOW_97_in_synpred127_SQL92Query1903); if (state.failed) return ;
        dbg.location(259,22);
        pushFollow(FOLLOW_row_value_in_synpred127_SQL92Query1906);
        row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred127_SQL92Query

    // $ANTLR start synpred128_SQL92Query
    public final void synpred128_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:4: ( value_expression )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:4: value_expression
        {
        dbg.location(267,4);
        pushFollow(FOLLOW_value_expression_in_synpred128_SQL92Query1950);
        value_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred128_SQL92Query

    // $ANTLR start synpred129_SQL92Query
    public final void synpred129_SQL92Query_fragment() throws RecognitionException {   
        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:22: ( 'NULL' )
        dbg.enterAlt(1);

        // C:\\Documents and Settings\\ahume\\workspace\\ogsadai\\OGSA-DAI DQP\\server\\src\\main\\grammar\\SQL92Query.g:267:22: 'NULL'
        {
        dbg.location(267,22);
        match(input,70,FOLLOW_70_in_synpred129_SQL92Query1953); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred129_SQL92Query

    // Delegated rules

    public final boolean synpred41_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred41_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred55_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred55_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred110_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred110_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred90_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred90_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred43_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred43_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred104_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred104_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred106_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred106_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred101_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred101_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred102_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred102_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred100_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred100_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred91_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred91_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred128_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred128_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred103_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred103_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred78_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred78_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred36_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred36_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred64_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred64_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred127_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred127_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred42_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred42_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred94_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred94_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred120_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred120_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred107_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred107_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred44_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred44_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred129_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred129_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred108_SQL92Query() {
        state.backtracking++;
        dbg.beginBacktrack(state.backtracking);
        int start = input.mark();
        try {
            synpred108_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        dbg.endBacktrack(state.backtracking, success);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA22 dfa22 = new DFA22(this);
    protected DFA25 dfa25 = new DFA25(this);
    protected DFA26 dfa26 = new DFA26(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA48 dfa48 = new DFA48(this);
    protected DFA49 dfa49 = new DFA49(this);
    protected DFA52 dfa52 = new DFA52(this);
    protected DFA57 dfa57 = new DFA57(this);
    protected DFA58 dfa58 = new DFA58(this);
    protected DFA59 dfa59 = new DFA59(this);
    protected DFA60 dfa60 = new DFA60(this);
    protected DFA63 dfa63 = new DFA63(this);
    protected DFA66 dfa66 = new DFA66(this);
    protected DFA67 dfa67 = new DFA67(this);
    static final String DFA22_eotS =
        "\72\uffff";
    static final String DFA22_eofS =
        "\1\1\71\uffff";
    static final String DFA22_minS =
        "\1\37\10\uffff\2\0\57\uffff";
    static final String DFA22_maxS =
        "\1\143\10\uffff\2\0\57\uffff";
    static final String DFA22_acceptS =
        "\1\uffff\1\2\67\uffff\1\1";
    static final String DFA22_specialS =
        "\11\uffff\1\0\1\1\57\uffff}>";
    static final String[] DFA22_transitionS = {
            "\5\1\1\uffff\2\1\1\uffff\2\1\1\uffff\4\1\1\uffff\2\1\1\uffff"+
            "\1\1\1\uffff\2\1\2\uffff\12\1\1\11\1\12\1\uffff\3\1\1\uffff"+
            "\1\1\1\uffff\4\1\1\uffff\16\1\2\uffff\3\1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "()* loopback of 139:12: ( ( '+' | '-' ) factor )*";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_9 = input.LA(1);

                         
                        int index22_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_SQL92Query()) ) {s = 57;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index22_9);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA22_10 = input.LA(1);

                         
                        int index22_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred36_SQL92Query()) ) {s = 57;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index22_10);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA25_eotS =
        "\30\uffff";
    static final String DFA25_eofS =
        "\30\uffff";
    static final String DFA25_minS =
        "\1\37\2\0\25\uffff";
    static final String DFA25_maxS =
        "\1\110\2\0\25\uffff";
    static final String DFA25_acceptS =
        "\3\uffff\1\4\20\uffff\1\1\1\5\1\2\1\3";
    static final String DFA25_specialS =
        "\1\uffff\1\0\1\1\25\uffff}>";
    static final String[] DFA25_transitionS = {
            "\1\2\4\3\14\uffff\1\1\10\uffff\12\3\3\uffff\3\3",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA25_eot = DFA.unpackEncodedString(DFA25_eotS);
    static final short[] DFA25_eof = DFA.unpackEncodedString(DFA25_eofS);
    static final char[] DFA25_min = DFA.unpackEncodedStringToUnsignedChars(DFA25_minS);
    static final char[] DFA25_max = DFA.unpackEncodedStringToUnsignedChars(DFA25_maxS);
    static final short[] DFA25_accept = DFA.unpackEncodedString(DFA25_acceptS);
    static final short[] DFA25_special = DFA.unpackEncodedString(DFA25_specialS);
    static final short[][] DFA25_transition;

    static {
        int numStates = DFA25_transitionS.length;
        DFA25_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA25_transition[i] = DFA.unpackEncodedString(DFA25_transitionS[i]);
        }
    }

    class DFA25 extends DFA {

        public DFA25(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 25;
            this.eot = DFA25_eot;
            this.eof = DFA25_eof;
            this.min = DFA25_min;
            this.max = DFA25_max;
            this.accept = DFA25_accept;
            this.special = DFA25_special;
            this.transition = DFA25_transition;
        }
        public String getDescription() {
            return "145:1: value_expression_primary : ( '(' value_expression ')' | function | column_name | literal | sub_query );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA25_1 = input.LA(1);

                         
                        int index25_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred41_SQL92Query()) ) {s = 20;}

                        else if ( (true) ) {s = 21;}

                         
                        input.seek(index25_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA25_2 = input.LA(1);

                         
                        int index25_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred42_SQL92Query()) ) {s = 22;}

                        else if ( (synpred43_SQL92Query()) ) {s = 23;}

                        else if ( (synpred44_SQL92Query()) ) {s = 3;}

                         
                        input.seek(index25_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 25, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA26_eotS =
        "\14\uffff";
    static final String DFA26_eofS =
        "\14\uffff";
    static final String DFA26_minS =
        "\1\37\5\uffff\1\70\4\uffff\1\71";
    static final String DFA26_maxS =
        "\1\110\5\uffff\1\70\4\uffff\1\102";
    static final String DFA26_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\6\1\7\1\10\1\11\1\uffff";
    static final String DFA26_specialS =
        "\14\uffff}>";
    static final String[] DFA26_transitionS = {
            "\1\6\1\1\1\2\1\3\1\4\25\uffff\3\5\7\7\3\uffff\1\10\1\11\1\12",
            "",
            "",
            "",
            "",
            "",
            "\1\13",
            "",
            "",
            "",
            "",
            "\3\5\7\7"
    };

    static final short[] DFA26_eot = DFA.unpackEncodedString(DFA26_eotS);
    static final short[] DFA26_eof = DFA.unpackEncodedString(DFA26_eofS);
    static final char[] DFA26_min = DFA.unpackEncodedStringToUnsignedChars(DFA26_minS);
    static final char[] DFA26_max = DFA.unpackEncodedStringToUnsignedChars(DFA26_maxS);
    static final short[] DFA26_accept = DFA.unpackEncodedString(DFA26_acceptS);
    static final short[] DFA26_special = DFA.unpackEncodedString(DFA26_specialS);
    static final short[][] DFA26_transition;

    static {
        int numStates = DFA26_transitionS.length;
        DFA26_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA26_transition[i] = DFA.unpackEncodedString(DFA26_transitionS[i]);
        }
    }

    class DFA26 extends DFA {

        public DFA26(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 26;
            this.eot = DFA26_eot;
            this.eof = DFA26_eof;
            this.min = DFA26_min;
            this.max = DFA26_max;
            this.accept = DFA26_accept;
            this.special = DFA26_special;
            this.transition = DFA26_transition;
        }
        public String getDescription() {
            return "153:1: literal : ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
    }
    static final String DFA32_eotS =
        "\13\uffff";
    static final String DFA32_eofS =
        "\1\uffff\1\2\11\uffff";
    static final String DFA32_minS =
        "\2\37\1\uffff\1\37\6\0\1\uffff";
    static final String DFA32_maxS =
        "\1\102\1\143\1\uffff\1\143\6\0\1\uffff";
    static final String DFA32_acceptS =
        "\2\uffff\1\2\7\uffff\1\1";
    static final String DFA32_specialS =
        "\4\uffff\1\0\1\4\1\3\1\1\1\2\1\5\1\uffff}>";
    static final String[] DFA32_transitionS = {
            "\1\2\34\uffff\1\1\6\2",
            "\4\2\1\3\1\uffff\2\2\1\uffff\2\2\1\uffff\4\2\1\uffff\4\2\1"+
            "\uffff\2\2\2\uffff\20\2\1\uffff\1\2\1\uffff\4\2\1\uffff\16\2"+
            "\2\uffff\3\2",
            "",
            "\5\2\14\uffff\4\2\5\uffff\4\2\1\4\1\5\1\6\1\7\1\10\1\11\7"+
            "\2\11\uffff\14\2\2\uffff\3\2",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "158:1: interval : ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_4 = input.LA(1);

                         
                        int index32_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_4);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_7 = input.LA(1);

                         
                        int index32_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_7);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_8 = input.LA(1);

                         
                        int index32_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_8);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_6 = input.LA(1);

                         
                        int index32_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_6);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA32_5 = input.LA(1);

                         
                        int index32_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA32_9 = input.LA(1);

                         
                        int index32_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred64_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index32_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA48_eotS =
        "\35\uffff";
    static final String DFA48_eofS =
        "\35\uffff";
    static final String DFA48_minS =
        "\1\37\1\0\33\uffff";
    static final String DFA48_maxS =
        "\1\143\1\0\33\uffff";
    static final String DFA48_acceptS =
        "\2\uffff\1\2\31\uffff\1\1";
    static final String DFA48_specialS =
        "\1\uffff\1\0\33\uffff}>";
    static final String[] DFA48_transitionS = {
            "\5\2\14\uffff\1\1\1\2\1\uffff\1\2\5\uffff\14\2\1\uffff\3\2"+
            "\12\uffff\1\2\3\uffff\1\2\12\uffff\2\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA48_eot = DFA.unpackEncodedString(DFA48_eotS);
    static final short[] DFA48_eof = DFA.unpackEncodedString(DFA48_eofS);
    static final char[] DFA48_min = DFA.unpackEncodedStringToUnsignedChars(DFA48_minS);
    static final char[] DFA48_max = DFA.unpackEncodedStringToUnsignedChars(DFA48_maxS);
    static final short[] DFA48_accept = DFA.unpackEncodedString(DFA48_acceptS);
    static final short[] DFA48_special = DFA.unpackEncodedString(DFA48_specialS);
    static final short[][] DFA48_transition;

    static {
        int numStates = DFA48_transitionS.length;
        DFA48_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA48_transition[i] = DFA.unpackEncodedString(DFA48_transitionS[i]);
        }
    }

    class DFA48 extends DFA {

        public DFA48(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 48;
            this.eot = DFA48_eot;
            this.eof = DFA48_eof;
            this.min = DFA48_min;
            this.max = DFA48_max;
            this.accept = DFA48_accept;
            this.special = DFA48_special;
            this.transition = DFA48_transition;
        }
        public String getDescription() {
            return "196:16: ( table_function_subquery )?";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA48_1 = input.LA(1);

                         
                        int index48_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred90_SQL92Query()) ) {s = 28;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index48_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 48, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA49_eotS =
        "\35\uffff";
    static final String DFA49_eofS =
        "\35\uffff";
    static final String DFA49_minS =
        "\1\37\1\0\33\uffff";
    static final String DFA49_maxS =
        "\1\143\1\0\33\uffff";
    static final String DFA49_acceptS =
        "\2\uffff\1\2\31\uffff\1\1";
    static final String DFA49_specialS =
        "\1\uffff\1\0\33\uffff}>";
    static final String[] DFA49_transitionS = {
            "\5\2\14\uffff\2\2\1\uffff\1\1\5\uffff\14\2\1\uffff\3\2\12\uffff"+
            "\1\2\3\uffff\1\2\12\uffff\2\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA49_eot = DFA.unpackEncodedString(DFA49_eotS);
    static final short[] DFA49_eof = DFA.unpackEncodedString(DFA49_eofS);
    static final char[] DFA49_min = DFA.unpackEncodedStringToUnsignedChars(DFA49_minS);
    static final char[] DFA49_max = DFA.unpackEncodedStringToUnsignedChars(DFA49_maxS);
    static final short[] DFA49_accept = DFA.unpackEncodedString(DFA49_acceptS);
    static final short[] DFA49_special = DFA.unpackEncodedString(DFA49_specialS);
    static final short[][] DFA49_transition;

    static {
        int numStates = DFA49_transitionS.length;
        DFA49_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA49_transition[i] = DFA.unpackEncodedString(DFA49_transitionS[i]);
        }
    }

    class DFA49 extends DFA {

        public DFA49(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 49;
            this.eot = DFA49_eot;
            this.eof = DFA49_eof;
            this.min = DFA49_min;
            this.max = DFA49_max;
            this.accept = DFA49_accept;
            this.special = DFA49_special;
            this.transition = DFA49_transition;
        }
        public String getDescription() {
            return "()* loopback of 196:41: ( ',' table_function_subquery )*";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA49_1 = input.LA(1);

                         
                        int index49_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred91_SQL92Query()) ) {s = 28;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index49_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 49, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA52_eotS =
        "\33\uffff";
    static final String DFA52_eofS =
        "\33\uffff";
    static final String DFA52_minS =
        "\1\37\1\uffff\25\0\4\uffff";
    static final String DFA52_maxS =
        "\1\143\1\uffff\25\0\4\uffff";
    static final String DFA52_acceptS =
        "\1\uffff\1\1\30\uffff\1\2";
    static final String DFA52_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\4\uffff}>";
    static final String[] DFA52_transitionS = {
            "\1\2\1\7\1\10\1\11\1\3\14\uffff\1\6\10\uffff\1\12\1\13\1\14"+
            "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\4\1\5\1\uffff\1\24\1\25"+
            "\1\26\12\uffff\1\1\3\uffff\1\1\12\uffff\2\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA52_eot = DFA.unpackEncodedString(DFA52_eotS);
    static final short[] DFA52_eof = DFA.unpackEncodedString(DFA52_eofS);
    static final char[] DFA52_min = DFA.unpackEncodedStringToUnsignedChars(DFA52_minS);
    static final char[] DFA52_max = DFA.unpackEncodedStringToUnsignedChars(DFA52_maxS);
    static final short[] DFA52_accept = DFA.unpackEncodedString(DFA52_acceptS);
    static final short[] DFA52_special = DFA.unpackEncodedString(DFA52_specialS);
    static final short[][] DFA52_transition;

    static {
        int numStates = DFA52_transitionS.length;
        DFA52_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA52_transition[i] = DFA.unpackEncodedString(DFA52_transitionS[i]);
        }
    }

    class DFA52 extends DFA {

        public DFA52(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 52;
            this.eot = DFA52_eot;
            this.eof = DFA52_eof;
            this.min = DFA52_min;
            this.max = DFA52_max;
            this.accept = DFA52_accept;
            this.special = DFA52_special;
            this.transition = DFA52_transition;
        }
        public String getDescription() {
            return "204:1: table_function_param : ( search_condition | value_expression );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA52_2 = input.LA(1);

                         
                        int index52_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA52_3 = input.LA(1);

                         
                        int index52_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA52_4 = input.LA(1);

                         
                        int index52_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA52_5 = input.LA(1);

                         
                        int index52_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA52_6 = input.LA(1);

                         
                        int index52_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA52_7 = input.LA(1);

                         
                        int index52_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA52_8 = input.LA(1);

                         
                        int index52_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA52_9 = input.LA(1);

                         
                        int index52_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA52_10 = input.LA(1);

                         
                        int index52_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA52_11 = input.LA(1);

                         
                        int index52_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA52_12 = input.LA(1);

                         
                        int index52_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA52_13 = input.LA(1);

                         
                        int index52_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA52_14 = input.LA(1);

                         
                        int index52_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA52_15 = input.LA(1);

                         
                        int index52_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA52_16 = input.LA(1);

                         
                        int index52_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA52_17 = input.LA(1);

                         
                        int index52_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA52_18 = input.LA(1);

                         
                        int index52_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA52_19 = input.LA(1);

                         
                        int index52_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA52_20 = input.LA(1);

                         
                        int index52_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA52_21 = input.LA(1);

                         
                        int index52_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA52_22 = input.LA(1);

                         
                        int index52_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index52_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 52, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA57_eotS =
        "\32\uffff";
    static final String DFA57_eofS =
        "\32\uffff";
    static final String DFA57_minS =
        "\1\37\5\uffff\1\0\23\uffff";
    static final String DFA57_maxS =
        "\1\143\5\uffff\1\0\23\uffff";
    static final String DFA57_acceptS =
        "\1\uffff\1\1\27\uffff\1\2";
    static final String DFA57_specialS =
        "\6\uffff\1\0\23\uffff}>";
    static final String[] DFA57_transitionS = {
            "\5\1\14\uffff\1\6\10\uffff\14\1\1\uffff\3\1\16\uffff\1\1\12"+
            "\uffff\2\1",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA57_eot = DFA.unpackEncodedString(DFA57_eotS);
    static final short[] DFA57_eof = DFA.unpackEncodedString(DFA57_eofS);
    static final char[] DFA57_min = DFA.unpackEncodedStringToUnsignedChars(DFA57_minS);
    static final char[] DFA57_max = DFA.unpackEncodedStringToUnsignedChars(DFA57_maxS);
    static final short[] DFA57_accept = DFA.unpackEncodedString(DFA57_acceptS);
    static final short[] DFA57_special = DFA.unpackEncodedString(DFA57_specialS);
    static final short[][] DFA57_transition;

    static {
        int numStates = DFA57_transitionS.length;
        DFA57_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA57_transition[i] = DFA.unpackEncodedString(DFA57_transitionS[i]);
        }
    }

    class DFA57 extends DFA {

        public DFA57(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 57;
            this.eot = DFA57_eot;
            this.eof = DFA57_eof;
            this.min = DFA57_min;
            this.max = DFA57_max;
            this.accept = DFA57_accept;
            this.special = DFA57_special;
            this.transition = DFA57_transition;
        }
        public String getDescription() {
            return "225:1: boolean_primary : ( predicate | '(' search_condition ')' );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA57_6 = input.LA(1);

                         
                        int index57_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred100_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index57_6);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 57, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA58_eotS =
        "\35\uffff";
    static final String DFA58_eofS =
        "\35\uffff";
    static final String DFA58_minS =
        "\1\37\1\uffff\26\0\5\uffff";
    static final String DFA58_maxS =
        "\1\143\1\uffff\26\0\5\uffff";
    static final String DFA58_acceptS =
        "\1\uffff\1\1\26\uffff\1\5\1\2\1\3\1\4\1\6";
    static final String DFA58_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\5\uffff}>";
    static final String[] DFA58_transitionS = {
            "\1\2\1\7\1\10\1\11\1\3\14\uffff\1\6\10\uffff\1\12\1\13\1\14"+
            "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\4\1\5\1\uffff\1\24\1\25"+
            "\1\26\16\uffff\1\30\12\uffff\1\27\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA58_eot = DFA.unpackEncodedString(DFA58_eotS);
    static final short[] DFA58_eof = DFA.unpackEncodedString(DFA58_eofS);
    static final char[] DFA58_min = DFA.unpackEncodedStringToUnsignedChars(DFA58_minS);
    static final char[] DFA58_max = DFA.unpackEncodedStringToUnsignedChars(DFA58_maxS);
    static final short[] DFA58_accept = DFA.unpackEncodedString(DFA58_acceptS);
    static final short[] DFA58_special = DFA.unpackEncodedString(DFA58_specialS);
    static final short[][] DFA58_transition;

    static {
        int numStates = DFA58_transitionS.length;
        DFA58_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA58_transition[i] = DFA.unpackEncodedString(DFA58_transitionS[i]);
        }
    }

    class DFA58 extends DFA {

        public DFA58(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 58;
            this.eot = DFA58_eot;
            this.eof = DFA58_eof;
            this.min = DFA58_min;
            this.max = DFA58_max;
            this.accept = DFA58_accept;
            this.special = DFA58_special;
            this.transition = DFA58_transition;
        }
        public String getDescription() {
            return "227:1: predicate : ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA58_2 = input.LA(1);

                         
                        int index58_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA58_3 = input.LA(1);

                         
                        int index58_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA58_4 = input.LA(1);

                         
                        int index58_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA58_5 = input.LA(1);

                         
                        int index58_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA58_6 = input.LA(1);

                         
                        int index58_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA58_7 = input.LA(1);

                         
                        int index58_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA58_8 = input.LA(1);

                         
                        int index58_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA58_9 = input.LA(1);

                         
                        int index58_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA58_10 = input.LA(1);

                         
                        int index58_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA58_11 = input.LA(1);

                         
                        int index58_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA58_12 = input.LA(1);

                         
                        int index58_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA58_13 = input.LA(1);

                         
                        int index58_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA58_14 = input.LA(1);

                         
                        int index58_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA58_15 = input.LA(1);

                         
                        int index58_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA58_16 = input.LA(1);

                         
                        int index58_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA58_17 = input.LA(1);

                         
                        int index58_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA58_18 = input.LA(1);

                         
                        int index58_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA58_19 = input.LA(1);

                         
                        int index58_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA58_20 = input.LA(1);

                         
                        int index58_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA58_21 = input.LA(1);

                         
                        int index58_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA58_22 = input.LA(1);

                         
                        int index58_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA58_23 = input.LA(1);

                         
                        int index58_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred101_SQL92Query()) ) {s = 1;}

                        else if ( (synpred102_SQL92Query()) ) {s = 25;}

                        else if ( (synpred103_SQL92Query()) ) {s = 26;}

                        else if ( (synpred104_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index58_23);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 58, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA59_eotS =
        "\31\uffff";
    static final String DFA59_eofS =
        "\31\uffff";
    static final String DFA59_minS =
        "\1\37\26\0\2\uffff";
    static final String DFA59_maxS =
        "\1\142\26\0\2\uffff";
    static final String DFA59_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA59_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA59_transitionS = {
            "\1\1\1\6\1\7\1\10\1\2\14\uffff\1\5\10\uffff\1\11\1\12\1\13"+
            "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
            "\1\25\31\uffff\1\26",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA59_eot = DFA.unpackEncodedString(DFA59_eotS);
    static final short[] DFA59_eof = DFA.unpackEncodedString(DFA59_eofS);
    static final char[] DFA59_min = DFA.unpackEncodedStringToUnsignedChars(DFA59_minS);
    static final char[] DFA59_max = DFA.unpackEncodedStringToUnsignedChars(DFA59_maxS);
    static final short[] DFA59_accept = DFA.unpackEncodedString(DFA59_acceptS);
    static final short[] DFA59_special = DFA.unpackEncodedString(DFA59_specialS);
    static final short[][] DFA59_transition;

    static {
        int numStates = DFA59_transitionS.length;
        DFA59_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA59_transition[i] = DFA.unpackEncodedString(DFA59_transitionS[i]);
        }
    }

    class DFA59 extends DFA {

        public DFA59(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 59;
            this.eot = DFA59_eot;
            this.eof = DFA59_eof;
            this.min = DFA59_min;
            this.max = DFA59_max;
            this.accept = DFA59_accept;
            this.special = DFA59_special;
            this.transition = DFA59_transition;
        }
        public String getDescription() {
            return "229:1: null_predicate : ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA59_1 = input.LA(1);

                         
                        int index59_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA59_2 = input.LA(1);

                         
                        int index59_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA59_3 = input.LA(1);

                         
                        int index59_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA59_4 = input.LA(1);

                         
                        int index59_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA59_5 = input.LA(1);

                         
                        int index59_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA59_6 = input.LA(1);

                         
                        int index59_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA59_7 = input.LA(1);

                         
                        int index59_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA59_8 = input.LA(1);

                         
                        int index59_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA59_9 = input.LA(1);

                         
                        int index59_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA59_10 = input.LA(1);

                         
                        int index59_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA59_11 = input.LA(1);

                         
                        int index59_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA59_12 = input.LA(1);

                         
                        int index59_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA59_13 = input.LA(1);

                         
                        int index59_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA59_14 = input.LA(1);

                         
                        int index59_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA59_15 = input.LA(1);

                         
                        int index59_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA59_16 = input.LA(1);

                         
                        int index59_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA59_17 = input.LA(1);

                         
                        int index59_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA59_18 = input.LA(1);

                         
                        int index59_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA59_19 = input.LA(1);

                         
                        int index59_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA59_20 = input.LA(1);

                         
                        int index59_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA59_21 = input.LA(1);

                         
                        int index59_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA59_22 = input.LA(1);

                         
                        int index59_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred106_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index59_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 59, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA60_eotS =
        "\31\uffff";
    static final String DFA60_eofS =
        "\31\uffff";
    static final String DFA60_minS =
        "\1\37\26\0\2\uffff";
    static final String DFA60_maxS =
        "\1\142\26\0\2\uffff";
    static final String DFA60_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA60_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA60_transitionS = {
            "\1\1\1\6\1\7\1\10\1\2\14\uffff\1\5\10\uffff\1\11\1\12\1\13"+
            "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
            "\1\25\31\uffff\1\26",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA60_eot = DFA.unpackEncodedString(DFA60_eotS);
    static final short[] DFA60_eof = DFA.unpackEncodedString(DFA60_eofS);
    static final char[] DFA60_min = DFA.unpackEncodedStringToUnsignedChars(DFA60_minS);
    static final char[] DFA60_max = DFA.unpackEncodedStringToUnsignedChars(DFA60_maxS);
    static final short[] DFA60_accept = DFA.unpackEncodedString(DFA60_acceptS);
    static final short[] DFA60_special = DFA.unpackEncodedString(DFA60_specialS);
    static final short[][] DFA60_transition;

    static {
        int numStates = DFA60_transitionS.length;
        DFA60_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA60_transition[i] = DFA.unpackEncodedString(DFA60_transitionS[i]);
        }
    }

    class DFA60 extends DFA {

        public DFA60(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 60;
            this.eot = DFA60_eot;
            this.eof = DFA60_eof;
            this.min = DFA60_min;
            this.max = DFA60_max;
            this.accept = DFA60_accept;
            this.special = DFA60_special;
            this.transition = DFA60_transition;
        }
        public String getDescription() {
            return "233:1: in_predicate : ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA60_1 = input.LA(1);

                         
                        int index60_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA60_2 = input.LA(1);

                         
                        int index60_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA60_3 = input.LA(1);

                         
                        int index60_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA60_4 = input.LA(1);

                         
                        int index60_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA60_5 = input.LA(1);

                         
                        int index60_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA60_6 = input.LA(1);

                         
                        int index60_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA60_7 = input.LA(1);

                         
                        int index60_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA60_8 = input.LA(1);

                         
                        int index60_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA60_9 = input.LA(1);

                         
                        int index60_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA60_10 = input.LA(1);

                         
                        int index60_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA60_11 = input.LA(1);

                         
                        int index60_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA60_12 = input.LA(1);

                         
                        int index60_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA60_13 = input.LA(1);

                         
                        int index60_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA60_14 = input.LA(1);

                         
                        int index60_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA60_15 = input.LA(1);

                         
                        int index60_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA60_16 = input.LA(1);

                         
                        int index60_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA60_17 = input.LA(1);

                         
                        int index60_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA60_18 = input.LA(1);

                         
                        int index60_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA60_19 = input.LA(1);

                         
                        int index60_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA60_20 = input.LA(1);

                         
                        int index60_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA60_21 = input.LA(1);

                         
                        int index60_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA60_22 = input.LA(1);

                         
                        int index60_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred107_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index60_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 60, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA63_eotS =
        "\31\uffff";
    static final String DFA63_eofS =
        "\31\uffff";
    static final String DFA63_minS =
        "\1\37\26\0\2\uffff";
    static final String DFA63_maxS =
        "\1\142\26\0\2\uffff";
    static final String DFA63_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA63_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA63_transitionS = {
            "\1\1\1\6\1\7\1\10\1\2\14\uffff\1\5\10\uffff\1\11\1\12\1\13"+
            "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
            "\1\25\31\uffff\1\26",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA63_eot = DFA.unpackEncodedString(DFA63_eotS);
    static final short[] DFA63_eof = DFA.unpackEncodedString(DFA63_eofS);
    static final char[] DFA63_min = DFA.unpackEncodedStringToUnsignedChars(DFA63_minS);
    static final char[] DFA63_max = DFA.unpackEncodedStringToUnsignedChars(DFA63_maxS);
    static final short[] DFA63_accept = DFA.unpackEncodedString(DFA63_acceptS);
    static final short[] DFA63_special = DFA.unpackEncodedString(DFA63_specialS);
    static final short[][] DFA63_transition;

    static {
        int numStates = DFA63_transitionS.length;
        DFA63_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA63_transition[i] = DFA.unpackEncodedString(DFA63_transitionS[i]);
        }
    }

    class DFA63 extends DFA {

        public DFA63(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 63;
            this.eot = DFA63_eot;
            this.eof = DFA63_eof;
            this.min = DFA63_min;
            this.max = DFA63_max;
            this.accept = DFA63_accept;
            this.special = DFA63_special;
            this.transition = DFA63_transition;
        }
        public String getDescription() {
            return "242:1: between_predicate : (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA63_1 = input.LA(1);

                         
                        int index63_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA63_2 = input.LA(1);

                         
                        int index63_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA63_3 = input.LA(1);

                         
                        int index63_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA63_4 = input.LA(1);

                         
                        int index63_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA63_5 = input.LA(1);

                         
                        int index63_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA63_6 = input.LA(1);

                         
                        int index63_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA63_7 = input.LA(1);

                         
                        int index63_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA63_8 = input.LA(1);

                         
                        int index63_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA63_9 = input.LA(1);

                         
                        int index63_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA63_10 = input.LA(1);

                         
                        int index63_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA63_11 = input.LA(1);

                         
                        int index63_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA63_12 = input.LA(1);

                         
                        int index63_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA63_13 = input.LA(1);

                         
                        int index63_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA63_14 = input.LA(1);

                         
                        int index63_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA63_15 = input.LA(1);

                         
                        int index63_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA63_16 = input.LA(1);

                         
                        int index63_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA63_17 = input.LA(1);

                         
                        int index63_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA63_18 = input.LA(1);

                         
                        int index63_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA63_19 = input.LA(1);

                         
                        int index63_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA63_20 = input.LA(1);

                         
                        int index63_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA63_21 = input.LA(1);

                         
                        int index63_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA63_22 = input.LA(1);

                         
                        int index63_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred110_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index63_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 63, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA66_eotS =
        "\32\uffff";
    static final String DFA66_eofS =
        "\32\uffff";
    static final String DFA66_minS =
        "\1\37\1\uffff\26\0\2\uffff";
    static final String DFA66_maxS =
        "\1\143\1\uffff\26\0\2\uffff";
    static final String DFA66_acceptS =
        "\1\uffff\1\1\26\uffff\1\2\1\3";
    static final String DFA66_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA66_transitionS = {
            "\1\2\1\7\1\10\1\11\1\3\14\uffff\1\6\10\uffff\1\12\1\13\1\14"+
            "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\4\1\5\1\uffff\1\24\1\25"+
            "\1\26\31\uffff\1\27\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA66_eot = DFA.unpackEncodedString(DFA66_eotS);
    static final short[] DFA66_eof = DFA.unpackEncodedString(DFA66_eofS);
    static final char[] DFA66_min = DFA.unpackEncodedStringToUnsignedChars(DFA66_minS);
    static final char[] DFA66_max = DFA.unpackEncodedStringToUnsignedChars(DFA66_maxS);
    static final short[] DFA66_accept = DFA.unpackEncodedString(DFA66_acceptS);
    static final short[] DFA66_special = DFA.unpackEncodedString(DFA66_specialS);
    static final short[][] DFA66_transition;

    static {
        int numStates = DFA66_transitionS.length;
        DFA66_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA66_transition[i] = DFA.unpackEncodedString(DFA66_transitionS[i]);
        }
    }

    class DFA66 extends DFA {

        public DFA66(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 66;
            this.eot = DFA66_eot;
            this.eof = DFA66_eof;
            this.min = DFA66_min;
            this.max = DFA66_max;
            this.accept = DFA66_accept;
            this.special = DFA66_special;
            this.transition = DFA66_transition;
        }
        public String getDescription() {
            return "250:1: comparison_predicate : ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA66_2 = input.LA(1);

                         
                        int index66_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA66_3 = input.LA(1);

                         
                        int index66_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA66_4 = input.LA(1);

                         
                        int index66_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA66_5 = input.LA(1);

                         
                        int index66_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA66_6 = input.LA(1);

                         
                        int index66_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA66_7 = input.LA(1);

                         
                        int index66_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA66_8 = input.LA(1);

                         
                        int index66_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA66_9 = input.LA(1);

                         
                        int index66_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA66_10 = input.LA(1);

                         
                        int index66_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA66_11 = input.LA(1);

                         
                        int index66_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA66_12 = input.LA(1);

                         
                        int index66_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA66_13 = input.LA(1);

                         
                        int index66_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA66_14 = input.LA(1);

                         
                        int index66_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA66_15 = input.LA(1);

                         
                        int index66_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA66_16 = input.LA(1);

                         
                        int index66_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA66_17 = input.LA(1);

                         
                        int index66_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA66_18 = input.LA(1);

                         
                        int index66_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA66_19 = input.LA(1);

                         
                        int index66_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA66_20 = input.LA(1);

                         
                        int index66_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA66_21 = input.LA(1);

                         
                        int index66_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA66_22 = input.LA(1);

                         
                        int index66_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA66_23 = input.LA(1);

                         
                        int index66_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred120_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index66_23);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 66, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA67_eotS =
        "\31\uffff";
    static final String DFA67_eofS =
        "\31\uffff";
    static final String DFA67_minS =
        "\1\37\26\0\2\uffff";
    static final String DFA67_maxS =
        "\1\142\26\0\2\uffff";
    static final String DFA67_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA67_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA67_transitionS = {
            "\1\1\1\6\1\7\1\10\1\2\14\uffff\1\5\10\uffff\1\11\1\12\1\13"+
            "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
            "\1\25\31\uffff\1\26",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA67_eot = DFA.unpackEncodedString(DFA67_eotS);
    static final short[] DFA67_eof = DFA.unpackEncodedString(DFA67_eofS);
    static final char[] DFA67_min = DFA.unpackEncodedStringToUnsignedChars(DFA67_minS);
    static final char[] DFA67_max = DFA.unpackEncodedStringToUnsignedChars(DFA67_maxS);
    static final short[] DFA67_accept = DFA.unpackEncodedString(DFA67_acceptS);
    static final short[] DFA67_special = DFA.unpackEncodedString(DFA67_specialS);
    static final short[][] DFA67_transition;

    static {
        int numStates = DFA67_transitionS.length;
        DFA67_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA67_transition[i] = DFA.unpackEncodedString(DFA67_transitionS[i]);
        }
    }

    class DFA67 extends DFA {

        public DFA67(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 67;
            this.eot = DFA67_eot;
            this.eof = DFA67_eof;
            this.min = DFA67_min;
            this.max = DFA67_max;
            this.accept = DFA67_accept;
            this.special = DFA67_special;
            this.transition = DFA67_transition;
        }
        public String getDescription() {
            return "258:1: like_predicate : ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) );";
        }
        public void error(NoViableAltException nvae) {
            dbg.recognitionException(nvae);
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA67_1 = input.LA(1);

                         
                        int index67_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA67_2 = input.LA(1);

                         
                        int index67_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA67_3 = input.LA(1);

                         
                        int index67_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA67_4 = input.LA(1);

                         
                        int index67_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA67_5 = input.LA(1);

                         
                        int index67_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA67_6 = input.LA(1);

                         
                        int index67_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA67_7 = input.LA(1);

                         
                        int index67_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA67_8 = input.LA(1);

                         
                        int index67_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA67_9 = input.LA(1);

                         
                        int index67_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA67_10 = input.LA(1);

                         
                        int index67_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA67_11 = input.LA(1);

                         
                        int index67_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA67_12 = input.LA(1);

                         
                        int index67_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA67_13 = input.LA(1);

                         
                        int index67_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA67_14 = input.LA(1);

                         
                        int index67_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA67_15 = input.LA(1);

                         
                        int index67_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA67_16 = input.LA(1);

                         
                        int index67_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA67_17 = input.LA(1);

                         
                        int index67_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA67_18 = input.LA(1);

                         
                        int index67_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA67_19 = input.LA(1);

                         
                        int index67_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA67_20 = input.LA(1);

                         
                        int index67_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA67_21 = input.LA(1);

                         
                        int index67_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA67_22 = input.LA(1);

                         
                        int index67_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred127_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index67_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 67, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_query_expression_in_statement183 = new BitSet(new long[]{0x0040002000000000L});
    public static final BitSet FOLLOW_order_by_in_statement185 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_37_in_statement188 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_statement191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_query_expression211 = new BitSet(new long[]{0x0000034000000002L});
    public static final BitSet FOLLOW_set_op_in_query_expression214 = new BitSet(new long[]{0x0001040000000000L});
    public static final BitSet FOLLOW_query_in_query_expression217 = new BitSet(new long[]{0x0000034000000002L});
    public static final BitSet FOLLOW_38_in_set_op231 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_set_op233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_set_op244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_set_op255 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_39_in_set_op257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_set_op268 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_set_op279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_query300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_query305 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_set_quantifier_in_query307 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_select_list_in_query310 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_query312 = new BitSet(new long[]{0x0001000080000000L});
    public static final BitSet FOLLOW_table_expression_in_query314 = new BitSet(new long[]{0x0000700000000002L});
    public static final BitSet FOLLOW_44_in_query317 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_search_condition_in_query321 = new BitSet(new long[]{0x0000600000000002L});
    public static final BitSet FOLLOW_45_in_query326 = new BitSet(new long[]{0xFE00000080000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_column_list_in_query328 = new BitSet(new long[]{0x0000400000000002L});
    public static final BitSet FOLLOW_46_in_query333 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_search_condition_in_query337 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_set_quantifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_sub_query409 = new BitSet(new long[]{0x0001040000000000L});
    public static final BitSet FOLLOW_query_expression_in_sub_query412 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_sub_query414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_select_list424 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_derived_column_in_select_list438 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_select_list441 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_derived_column_in_select_list444 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_52_in_derived_column456 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_derived_column458 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_derived_column460 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_derived_column464 = new BitSet(new long[]{0x0020000080000002L});
    public static final BitSet FOLLOW_53_in_derived_column467 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_derived_column472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_derived_column498 = new BitSet(new long[]{0x0020000080000002L});
    public static final BitSet FOLLOW_53_in_derived_column501 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_derived_column504 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_order_by529 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_55_in_order_by531 = new BitSet(new long[]{0xFE00000180000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_sort_specification_in_order_by533 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_order_by536 = new BitSet(new long[]{0xFE00000180000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_sort_specification_in_order_by538 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_column_name_in_sort_specification557 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_sort_specification561 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_sort_specification565 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_reserved_word_column_name579 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_reserved_word_column_name580 = new BitSet(new long[]{0xFE00000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_57_in_reserved_word_column_name586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_reserved_word_column_name592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_reserved_word_column_name598 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_reserved_word_column_name604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_reserved_word_column_name610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_reserved_word_column_name616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_reserved_word_column_name622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_reserved_word_column_name628 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_reserved_word_column_name634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_reserved_word_column_name640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_value_expression_in_value_expression671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_value_expression_in_value_expression676 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_numeric_value_expression688 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000018L});
    public static final BitSet FOLLOW_set_in_numeric_value_expression691 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_factor_in_numeric_value_expression698 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000018L});
    public static final BitSet FOLLOW_numeric_primary_in_factor710 = new BitSet(new long[]{0x0004000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_set_in_factor713 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_numeric_primary_in_factor720 = new BitSet(new long[]{0x0004000000000002L,0x0000000000000020L});
    public static final BitSet FOLLOW_67_in_numeric_primary733 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_68_in_numeric_primary736 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_primary_in_numeric_primary741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_value_expression_primary751 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_value_expression_primary754 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_value_expression_primary756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_value_expression_primary763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_value_expression_primary769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_value_expression_primary775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_value_expression_primary781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_literal791 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literal795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMERIC_in_literal799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal803 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_in_literal807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interval_in_literal811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_literal815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_literal819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_literal823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_datetime831 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_STRING_in_datetime844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_datetime853 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_datetime854 = new BitSet(new long[]{0x0E00000000000000L});
    public static final BitSet FOLLOW_57_in_datetime860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_datetime866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_datetime872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_interval896 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_STRING_in_interval899 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_set_in_interval901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_interval931 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_interval932 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_60_in_interval938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_interval944 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_interval950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_interval956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_interval962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_interval968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_interval974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function1002 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_function1005 = new BitSet(new long[]{0xFE1F808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_function1007 = new BitSet(new long[]{0x000A000000000000L});
    public static final BitSet FOLLOW_51_in_function1011 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_function1013 = new BitSet(new long[]{0x000A000000000000L});
    public static final BitSet FOLLOW_49_in_function1017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function1042 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_function1045 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_function1047 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_function1049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_string_value_expression1072 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_STRING_in_string_value_expression1076 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_73_in_string_value_expression1080 = new BitSet(new long[]{0x0000000880000000L});
    public static final BitSet FOLLOW_column_name_in_string_value_expression1084 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_STRING_in_string_value_expression1088 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000200L});
    public static final BitSet FOLLOW_table_reference_in_table_expression1103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_in_table_reference1113 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_table_reference1116 = new BitSet(new long[]{0x0001000080000000L});
    public static final BitSet FOLLOW_table_reference_in_table_reference1119 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_74_in_join_type1132 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_75_in_join_type1134 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_join_type1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_join_type1147 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_75_in_join_type1149 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_join_type1152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_join_type1161 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001800L});
    public static final BitSet FOLLOW_75_in_join_type1163 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_join_type1166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_join_type1176 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_join_type1179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_join_table_in_table1193 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F400L});
    public static final BitSet FOLLOW_join_type_in_table1196 = new BitSet(new long[]{0x0001000080000000L});
    public static final BitSet FOLLOW_non_join_table_in_table1199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_80_in_table1201 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_search_condition_in_table1204 = new BitSet(new long[]{0x0000000000000002L,0x000000000000F400L});
    public static final BitSet FOLLOW_table_name_in_non_join_table1218 = new BitSet(new long[]{0x0020000080000002L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_in_non_join_table1238 = new BitSet(new long[]{0x0020000080000000L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_non_join_table1256 = new BitSet(new long[]{0x0020000080000000L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_table_function1282 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_table_function1284 = new BitSet(new long[]{0xFE1F808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_table_function_subquery_in_table_function1286 = new BitSet(new long[]{0xFE1F808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_51_in_table_function1290 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_table_function_subquery_in_table_function1292 = new BitSet(new long[]{0xFE1F808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_51_in_table_function1297 = new BitSet(new long[]{0xFE1D808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_table_function_param_in_table_function1300 = new BitSet(new long[]{0xFE1F808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_49_in_table_function1304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_table_function_subquery1336 = new BitSet(new long[]{0x0020000080000000L});
    public static final BitSet FOLLOW_correlation_specification_in_table_function_subquery1338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_search_condition_in_table_function_param1359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_table_function_param1364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_name_in_relation1378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_in_relation1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_relation1406 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_factor_in_search_condition1427 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_search_condition1430 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_boolean_factor_in_search_condition1433 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_boolean_term_in_boolean_factor1443 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_boolean_factor1446 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_boolean_term_in_boolean_factor1449 = new BitSet(new long[]{0x0000000000000002L,0x0000000000040000L});
    public static final BitSet FOLLOW_boolean_test_in_boolean_term1461 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_boolean_term1466 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_boolean_term_in_boolean_term1468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_test1484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_boolean_primary1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_boolean_primary1496 = new BitSet(new long[]{0xFE15808F80000000L,0x0000000C008801DFL});
    public static final BitSet FOLLOW_search_condition_in_boolean_primary1499 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_boolean_primary1501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_predicate_in_predicate1511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_predicate_in_predicate1515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_predicate_in_predicate1519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_predicate_in_predicate1523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_predicate_in_predicate1527 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_predicate_in_predicate1531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_null_predicate1539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_null_predicate1541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_null_predicate1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_null_predicate1556 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_null_predicate1558 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_null_predicate1560 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_null_predicate1562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_in_predicate1584 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_in_predicate1586 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_85_in_in_predicate1588 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate1590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_in_predicate1612 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_85_in_in_predicate1614 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate1616 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_in_predicate_tail1639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_in_predicate_tail1646 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_in_predicate_tail1649 = new BitSet(new long[]{0x000A000000000000L});
    public static final BitSet FOLLOW_51_in_in_predicate_tail1652 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_in_predicate_tail1654 = new BitSet(new long[]{0x000A000000000000L});
    public static final BitSet FOLLOW_49_in_in_predicate_tail1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_between_predicate1679 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_between_predicate1681 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate1685 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_between_predicate1687 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate1691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_between_predicate1717 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_between_predicate1719 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_between_predicate1721 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate1725 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_between_predicate1727 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate1731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_exists_predicate1761 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_sub_query_in_exists_predicate1764 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bind_table_in_comparison_predicate1773 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_comparison_predicate1775 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate1778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate1785 = new BitSet(new long[]{0x0000000000000000L,0x000000007F000000L});
    public static final BitSet FOLLOW_88_in_comparison_predicate1790 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_89_in_comparison_predicate1794 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_90_in_comparison_predicate1798 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_91_in_comparison_predicate1802 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_92_in_comparison_predicate1806 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_93_in_comparison_predicate1810 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_94_in_comparison_predicate1814 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_39_in_comparison_predicate1820 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_95_in_comparison_predicate1824 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_96_in_comparison_predicate1828 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate1833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate1859 = new BitSet(new long[]{0x0000000000000000L,0x000000007F000000L});
    public static final BitSet FOLLOW_set_in_comparison_predicate1861 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate1890 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_like_predicate1901 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_97_in_like_predicate1903 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_like_predicate1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_like_predicate1913 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_like_predicate1915 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_97_in_like_predicate1917 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_like_predicate1921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_row_value1950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_row_value1953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_row_value1957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_bind_table1967 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_bind_table1970 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_bind_table1971 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_bind_table1974 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_correlation_specification2002 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_correlation_specification2007 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_table_name2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_column_list2025 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2029 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_51_in_column_list2033 = new BitSet(new long[]{0xFE00000080000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_column_name_in_column_list2037 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2041 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_ID_in_column_name2055 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_column_name2056 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_ID_in_column_name2061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred36_SQL92Query691 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_factor_in_synpred36_SQL92Query698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_synpred41_SQL92Query751 = new BitSet(new long[]{0xFE15808F80000000L,0x00000000000001DFL});
    public static final BitSet FOLLOW_value_expression_in_synpred41_SQL92Query754 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred41_SQL92Query756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_synpred42_SQL92Query763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_synpred43_SQL92Query769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_synpred44_SQL92Query775 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred55_SQL92Query831 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_STRING_in_synpred55_SQL92Query844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_synpred64_SQL92Query896 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_STRING_in_synpred64_SQL92Query899 = new BitSet(new long[]{0xE000000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_set_in_synpred64_SQL92Query901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_synpred78_SQL92Query1116 = new BitSet(new long[]{0x0001000080000000L});
    public static final BitSet FOLLOW_table_reference_in_synpred78_SQL92Query1119 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_subquery_in_synpred90_SQL92Query1286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_51_in_synpred91_SQL92Query1290 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_table_function_subquery_in_synpred91_SQL92Query1292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_search_condition_in_synpred94_SQL92Query1359 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_synpred100_SQL92Query1492 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_predicate_in_synpred101_SQL92Query1511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_predicate_in_synpred102_SQL92Query1515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_predicate_in_synpred103_SQL92Query1519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_predicate_in_synpred104_SQL92Query1523 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred106_SQL92Query1539 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_84_in_synpred106_SQL92Query1541 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_70_in_synpred106_SQL92Query1543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred107_SQL92Query1584 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_synpred107_SQL92Query1586 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_85_in_synpred107_SQL92Query1588 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_synpred107_SQL92Query1590 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_synpred108_SQL92Query1639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred110_SQL92Query1679 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_synpred110_SQL92Query1681 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_synpred110_SQL92Query1685 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_synpred110_SQL92Query1687 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_synpred110_SQL92Query1691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred120_SQL92Query1785 = new BitSet(new long[]{0x0000000000000000L,0x000000007F000000L});
    public static final BitSet FOLLOW_88_in_synpred120_SQL92Query1790 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_89_in_synpred120_SQL92Query1794 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_90_in_synpred120_SQL92Query1798 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_91_in_synpred120_SQL92Query1802 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_92_in_synpred120_SQL92Query1806 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_93_in_synpred120_SQL92Query1810 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_94_in_synpred120_SQL92Query1814 = new BitSet(new long[]{0x0000008000000000L,0x0000000180000000L});
    public static final BitSet FOLLOW_39_in_synpred120_SQL92Query1820 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_95_in_synpred120_SQL92Query1824 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_96_in_synpred120_SQL92Query1828 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_synpred120_SQL92Query1833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred127_SQL92Query1901 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
    public static final BitSet FOLLOW_97_in_synpred127_SQL92Query1903 = new BitSet(new long[]{0xFE15808F80000000L,0x00000004000001DFL});
    public static final BitSet FOLLOW_row_value_in_synpred127_SQL92Query1906 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_synpred128_SQL92Query1950 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_synpred129_SQL92Query1953 = new BitSet(new long[]{0x0000000000000002L});

}