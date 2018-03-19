// $ANTLR 3.2 Sep 23, 2009 12:02:23 ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g 2015-01-14 16:16:26

  package uk.org.ogsadai.parser.sql92query; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class SQL92QueryParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STATEMENT", "QUERY", "SETOP", "ORDER", "SELECT_LIST", "FROM_LIST", "WHERE", "GROUP_BY", "HAVING", "RELATION", "COLUMN", "FUNCTION", "NOT", "SET", "TABLECOLUMN", "RIGHT_OUTER_JOIN", "LEFT_OUTER_JOIN", "FULL_OUTER_JOIN", "JOIN", "IS_NULL", "UNION", "EXCEPT", "UNION_ALL", "EXCEPT_ALL", "INTERSECT", "BOUND", "CAST", "ASC", "DESC", "LIMIT", "INT", "ID", "FLOAT", "NUMERIC", "STRING", "WS", "';'", "'LIMIT'", "'UNION'", "'ALL'", "'EXCEPT'", "'INTERSECT'", "'SELECT'", "'FROM'", "'WHERE'", "'GROUP BY'", "'HAVING'", "'DISTINCT'", "'('", "')'", "'*'", "','", "'CAST'", "'AS'", "'ORDER'", "'BY'", "'DESC'", "'ASC'", "'.'", "'DATE'", "'TIMESTAMP'", "'TIME'", "'INTERVAL'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'+'", "'-'", "'/'", "'NULL'", "'TRUE'", "'FALSE'", "'||'", "'RIGHT'", "'OUTER'", "'JOIN'", "'LEFT'", "'FULL'", "'INNER'", "'ON'", "'OR'", "'AND'", "'NOT'", "'IS'", "'IN'", "'BETWEEN'", "'EXISTS'", "'='", "'<>'", "'!='", "'<'", "'>'", "'>='", "'<='", "'SOME'", "'ANY'", "'LIKE'", "'DEFAULT'", "'@'"
    };
    public static final int T__50=50;
    public static final int JOIN=22;
    public static final int SETOP=6;
    public static final int T__59=59;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int ID=35;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int CAST=30;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int WHERE=10;
    public static final int FUNCTION=15;
    public static final int QUERY=5;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int SET=17;
    public static final int LEFT_OUTER_JOIN=20;
    public static final int ORDER=7;
    public static final int ASC=31;
    public static final int NUMERIC=37;
    public static final int RELATION=13;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int STRING=38;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int UNION=24;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__91=91;
    public static final int T__100=100;
    public static final int STATEMENT=4;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__102=102;
    public static final int T__94=94;
    public static final int T__101=101;
    public static final int T__90=90;
    public static final int COLUMN=14;
    public static final int TABLECOLUMN=18;
    public static final int DESC=32;
    public static final int T__99=99;
    public static final int BOUND=29;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int EXCEPT=25;
    public static final int UNION_ALL=26;
    public static final int NOT=16;
    public static final int FROM_LIST=9;
    public static final int HAVING=12;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int FLOAT=36;
    public static final int T__72=72;
    public static final int EXCEPT_ALL=27;
    public static final int LIMIT=33;
    public static final int INT=34;
    public static final int IS_NULL=23;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__73=73;
    public static final int WS=39;
    public static final int EOF=-1;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int RIGHT_OUTER_JOIN=19;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int GROUP_BY=11;
    public static final int SELECT_LIST=8;
    public static final int INTERSECT=28;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__84=84;
    public static final int T__104=104;
    public static final int FULL_OUTER_JOIN=21;
    public static final int T__85=85;
    public static final int T__103=103;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__105=105;

    // delegates
    // delegators


        public SQL92QueryParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public SQL92QueryParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return SQL92QueryParser.tokenNames; }
    public String getGrammarFileName() { return "../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g"; }


    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
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
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:95:1: statement : query_expression ( order_by )? ( limit )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ( limit )? ) ;
    public final SQL92QueryParser.statement_return statement() throws RecognitionException {
        SQL92QueryParser.statement_return retval = new SQL92QueryParser.statement_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal4=null;
        Token EOF5=null;
        SQL92QueryParser.query_expression_return query_expression1 = null;

        SQL92QueryParser.order_by_return order_by2 = null;

        SQL92QueryParser.limit_return limit3 = null;


        CommonTree char_literal4_tree=null;
        CommonTree EOF5_tree=null;
        RewriteRuleTokenStream stream_40=new RewriteRuleTokenStream(adaptor,"token 40");
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_limit=new RewriteRuleSubtreeStream(adaptor,"rule limit");
        RewriteRuleSubtreeStream stream_order_by=new RewriteRuleSubtreeStream(adaptor,"rule order_by");
        RewriteRuleSubtreeStream stream_query_expression=new RewriteRuleSubtreeStream(adaptor,"rule query_expression");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:5: ( query_expression ( order_by )? ( limit )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ( limit )? ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:9: query_expression ( order_by )? ( limit )? ( ';' )? EOF
            {
            pushFollow(FOLLOW_query_expression_in_statement315);
            query_expression1=query_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_query_expression.add(query_expression1.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:26: ( order_by )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==58) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: order_by
                    {
                    pushFollow(FOLLOW_order_by_in_statement317);
                    order_by2=order_by();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_order_by.add(order_by2.getTree());

                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:36: ( limit )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==41) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: limit
                    {
                    pushFollow(FOLLOW_limit_in_statement320);
                    limit3=limit();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_limit.add(limit3.getTree());

                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:43: ( ';' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==40) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: ';'
                    {
                    char_literal4=(Token)match(input,40,FOLLOW_40_in_statement323); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_40.add(char_literal4);


                    }
                    break;

            }

            EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_statement326); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_EOF.add(EOF5);



            // AST REWRITE
            // elements: order_by, limit, query_expression
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 96:52: -> ^( STATEMENT query_expression ( order_by )? ( limit )? )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:55: ^( STATEMENT query_expression ( order_by )? ( limit )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATEMENT, "STATEMENT"), root_1);

                adaptor.addChild(root_1, stream_query_expression.nextTree());
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:84: ( order_by )?
                if ( stream_order_by.hasNext() ) {
                    adaptor.addChild(root_1, stream_order_by.nextTree());

                }
                stream_order_by.reset();
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:94: ( limit )?
                if ( stream_limit.hasNext() ) {
                    adaptor.addChild(root_1, stream_limit.nextTree());

                }
                stream_limit.reset();

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
        return retval;
    }
    // $ANTLR end "statement"

    public static class limit_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "limit"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:99:1: limit : 'LIMIT' INT -> ^( LIMIT INT ) ;
    public final SQL92QueryParser.limit_return limit() throws RecognitionException {
        SQL92QueryParser.limit_return retval = new SQL92QueryParser.limit_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal6=null;
        Token INT7=null;

        CommonTree string_literal6_tree=null;
        CommonTree INT7_tree=null;
        RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
        RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:100:5: ( 'LIMIT' INT -> ^( LIMIT INT ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:100:7: 'LIMIT' INT
            {
            string_literal6=(Token)match(input,41,FOLLOW_41_in_limit363); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_41.add(string_literal6);

            INT7=(Token)match(input,INT,FOLLOW_INT_in_limit365); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_INT.add(INT7);



            // AST REWRITE
            // elements: INT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 100:19: -> ^( LIMIT INT )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:100:22: ^( LIMIT INT )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LIMIT, "LIMIT"), root_1);

                adaptor.addChild(root_1, stream_INT.nextNode());

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
        return retval;
    }
    // $ANTLR end "limit"

    public static class query_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query_expression"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:102:1: query_expression : query ( set_op query )* ;
    public final SQL92QueryParser.query_expression_return query_expression() throws RecognitionException {
        SQL92QueryParser.query_expression_return retval = new SQL92QueryParser.query_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.query_return query8 = null;

        SQL92QueryParser.set_op_return set_op9 = null;

        SQL92QueryParser.query_return query10 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:103:5: ( query ( set_op query )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:103:9: query ( set_op query )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_query_in_query_expression391);
            query8=query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, query8.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:103:15: ( set_op query )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==42||(LA4_0>=44 && LA4_0<=45)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:103:16: set_op query
            	    {
            	    pushFollow(FOLLOW_set_op_in_query_expression394);
            	    set_op9=set_op();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(set_op9.getTree(), root_0);
            	    pushFollow(FOLLOW_query_in_query_expression397);
            	    query10=query();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, query10.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "query_expression"

    public static class set_op_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_op"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:105:1: set_op : ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) );
    public final SQL92QueryParser.set_op_return set_op() throws RecognitionException {
        SQL92QueryParser.set_op_return retval = new SQL92QueryParser.set_op_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal11=null;
        Token string_literal12=null;
        Token string_literal13=null;
        Token string_literal14=null;
        Token string_literal15=null;
        Token string_literal16=null;
        Token string_literal17=null;

        CommonTree string_literal11_tree=null;
        CommonTree string_literal12_tree=null;
        CommonTree string_literal13_tree=null;
        CommonTree string_literal14_tree=null;
        CommonTree string_literal15_tree=null;
        CommonTree string_literal16_tree=null;
        CommonTree string_literal17_tree=null;
        RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
        RewriteRuleTokenStream stream_45=new RewriteRuleTokenStream(adaptor,"token 45");
        RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
        RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:106:5: ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) )
            int alt5=5;
            switch ( input.LA(1) ) {
            case 42:
                {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==43) ) {
                    alt5=1;
                }
                else if ( (LA5_1==46||LA5_1==52) ) {
                    alt5=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
                }
                break;
            case 44:
                {
                int LA5_2 = input.LA(2);

                if ( (LA5_2==43) ) {
                    alt5=3;
                }
                else if ( (LA5_2==46||LA5_2==52) ) {
                    alt5=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 2, input);

                    throw nvae;
                }
                }
                break;
            case 45:
                {
                alt5=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:106:9: 'UNION' 'ALL'
                    {
                    string_literal11=(Token)match(input,42,FOLLOW_42_in_set_op419); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_42.add(string_literal11);

                    string_literal12=(Token)match(input,43,FOLLOW_43_in_set_op421); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_43.add(string_literal12);



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
                    // 106:23: -> ^( UNION_ALL )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:106:26: ^( UNION_ALL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION_ALL, "UNION_ALL"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:9: 'UNION'
                    {
                    string_literal13=(Token)match(input,42,FOLLOW_42_in_set_op437); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_42.add(string_literal13);



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
                    // 107:17: -> ^( UNION )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:20: ^( UNION )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION, "UNION"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:108:9: 'EXCEPT' 'ALL'
                    {
                    string_literal14=(Token)match(input,44,FOLLOW_44_in_set_op453); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_44.add(string_literal14);

                    string_literal15=(Token)match(input,43,FOLLOW_43_in_set_op455); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_43.add(string_literal15);



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
                    // 108:24: -> ^( EXCEPT_ALL )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:108:27: ^( EXCEPT_ALL )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT_ALL, "EXCEPT_ALL"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:109:9: 'EXCEPT'
                    {
                    string_literal16=(Token)match(input,44,FOLLOW_44_in_set_op471); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_44.add(string_literal16);



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
                    // 109:18: -> ^( EXCEPT )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:109:21: ^( EXCEPT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT, "EXCEPT"), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 5 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:110:9: 'INTERSECT'
                    {
                    string_literal17=(Token)match(input,45,FOLLOW_45_in_set_op487); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_45.add(string_literal17);



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
                    // 110:21: -> ^( INTERSECT )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:110:24: ^( INTERSECT )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
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
        return retval;
    }
    // $ANTLR end "set_op"

    public static class query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:112:1: query : ( sub_query | 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) );
    public final SQL92QueryParser.query_return query() throws RecognitionException {
        SQL92QueryParser.query_return retval = new SQL92QueryParser.query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal19=null;
        Token string_literal22=null;
        Token string_literal24=null;
        Token string_literal25=null;
        Token string_literal27=null;
        SQL92QueryParser.search_condition_return s1 = null;

        SQL92QueryParser.search_condition_return s2 = null;

        SQL92QueryParser.sub_query_return sub_query18 = null;

        SQL92QueryParser.set_quantifier_return set_quantifier20 = null;

        SQL92QueryParser.select_list_return select_list21 = null;

        SQL92QueryParser.table_expression_return table_expression23 = null;

        SQL92QueryParser.column_list_return column_list26 = null;


        CommonTree string_literal19_tree=null;
        CommonTree string_literal22_tree=null;
        CommonTree string_literal24_tree=null;
        CommonTree string_literal25_tree=null;
        CommonTree string_literal27_tree=null;
        RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
        RewriteRuleTokenStream stream_47=new RewriteRuleTokenStream(adaptor,"token 47");
        RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
        RewriteRuleTokenStream stream_49=new RewriteRuleTokenStream(adaptor,"token 49");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
        RewriteRuleSubtreeStream stream_table_expression=new RewriteRuleSubtreeStream(adaptor,"rule table_expression");
        RewriteRuleSubtreeStream stream_select_list=new RewriteRuleSubtreeStream(adaptor,"rule select_list");
        RewriteRuleSubtreeStream stream_set_quantifier=new RewriteRuleSubtreeStream(adaptor,"rule set_quantifier");
        RewriteRuleSubtreeStream stream_search_condition=new RewriteRuleSubtreeStream(adaptor,"rule search_condition");
        RewriteRuleSubtreeStream stream_column_list=new RewriteRuleSubtreeStream(adaptor,"rule column_list");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:113:5: ( sub_query | 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==52) ) {
                alt10=1;
            }
            else if ( (LA10_0==46) ) {
                alt10=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }
            switch (alt10) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:113:9: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_sub_query_in_query514);
                    sub_query18=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query18.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:9: 'SELECT' ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )?
                    {
                    string_literal19=(Token)match(input,46,FOLLOW_46_in_query524); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_46.add(string_literal19);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:18: ( set_quantifier )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==43||LA6_0==51) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: set_quantifier
                            {
                            pushFollow(FOLLOW_set_quantifier_in_query526);
                            set_quantifier20=set_quantifier();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_set_quantifier.add(set_quantifier20.getTree());

                            }
                            break;

                    }

                    pushFollow(FOLLOW_select_list_in_query529);
                    select_list21=select_list();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_select_list.add(select_list21.getTree());
                    string_literal22=(Token)match(input,47,FOLLOW_47_in_query531); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_47.add(string_literal22);

                    pushFollow(FOLLOW_table_expression_in_query533);
                    table_expression23=table_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_expression.add(table_expression23.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:70: ( 'WHERE' s1= search_condition )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==48) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:71: 'WHERE' s1= search_condition
                            {
                            string_literal24=(Token)match(input,48,FOLLOW_48_in_query536); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_48.add(string_literal24);

                            pushFollow(FOLLOW_search_condition_in_query540);
                            s1=search_condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_search_condition.add(s1.getTree());

                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:101: ( 'GROUP BY' column_list )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==49) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:102: 'GROUP BY' column_list
                            {
                            string_literal25=(Token)match(input,49,FOLLOW_49_in_query545); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_49.add(string_literal25);

                            pushFollow(FOLLOW_column_list_in_query547);
                            column_list26=column_list();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_column_list.add(column_list26.getTree());

                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:127: ( 'HAVING' s2= search_condition )?
                    int alt9=2;
                    int LA9_0 = input.LA(1);

                    if ( (LA9_0==50) ) {
                        alt9=1;
                    }
                    switch (alt9) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:128: 'HAVING' s2= search_condition
                            {
                            string_literal27=(Token)match(input,50,FOLLOW_50_in_query552); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_50.add(string_literal27);

                            pushFollow(FOLLOW_search_condition_in_query556);
                            s2=search_condition();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_search_condition.add(s2.getTree());

                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: select_list, table_expression, s1, column_list, set_quantifier, s2
                    // token labels: 
                    // rule labels: s1, retval, s2
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_s1=new RewriteRuleSubtreeStream(adaptor,"rule s1",s1!=null?s1.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_s2=new RewriteRuleSubtreeStream(adaptor,"rule s2",s2!=null?s2.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 115:13: -> ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:16: ^( QUERY ^( SELECT_LIST ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUERY, "QUERY"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:24: ^( SELECT_LIST ( set_quantifier )? select_list )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELECT_LIST, "SELECT_LIST"), root_2);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:38: ( set_quantifier )?
                        if ( stream_set_quantifier.hasNext() ) {
                            adaptor.addChild(root_2, stream_set_quantifier.nextTree());

                        }
                        stream_set_quantifier.reset();
                        adaptor.addChild(root_2, stream_select_list.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:67: ^( FROM_LIST table_expression )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FROM_LIST, "FROM_LIST"), root_2);

                        adaptor.addChild(root_2, stream_table_expression.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:97: ( ^( WHERE $s1) )?
                        if ( stream_s1.hasNext() ) {
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:97: ^( WHERE $s1)
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHERE, "WHERE"), root_2);

                            adaptor.addChild(root_2, stream_s1.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_s1.reset();
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:111: ( ^( GROUP_BY column_list ) )?
                        if ( stream_column_list.hasNext() ) {
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:111: ^( GROUP_BY column_list )
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(GROUP_BY, "GROUP_BY"), root_2);

                            adaptor.addChild(root_2, stream_column_list.nextTree());

                            adaptor.addChild(root_1, root_2);
                            }

                        }
                        stream_column_list.reset();
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:136: ( ^( HAVING $s2) )?
                        if ( stream_s2.hasNext() ) {
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:115:136: ^( HAVING $s2)
                            {
                            CommonTree root_2 = (CommonTree)adaptor.nil();
                            root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(HAVING, "HAVING"), root_2);

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
        return retval;
    }
    // $ANTLR end "query"

    public static class set_quantifier_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_quantifier"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:1: set_quantifier : ( 'DISTINCT' | 'ALL' );
    public final SQL92QueryParser.set_quantifier_return set_quantifier() throws RecognitionException {
        SQL92QueryParser.set_quantifier_return retval = new SQL92QueryParser.set_quantifier_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set28=null;

        CommonTree set28_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:5: ( 'DISTINCT' | 'ALL' )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:
            {
            root_0 = (CommonTree)adaptor.nil();

            set28=(Token)input.LT(1);
            if ( input.LA(1)==43||input.LA(1)==51 ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set28));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
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
        return retval;
    }
    // $ANTLR end "set_quantifier"

    public static class sub_query_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sub_query"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:121:1: sub_query : '(' query_expression ')' ;
    public final SQL92QueryParser.sub_query_return sub_query() throws RecognitionException {
        SQL92QueryParser.sub_query_return retval = new SQL92QueryParser.sub_query_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal29=null;
        Token char_literal31=null;
        SQL92QueryParser.query_expression_return query_expression30 = null;


        CommonTree char_literal29_tree=null;
        CommonTree char_literal31_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:122:5: ( '(' query_expression ')' )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:122:9: '(' query_expression ')'
            {
            root_0 = (CommonTree)adaptor.nil();

            char_literal29=(Token)match(input,52,FOLLOW_52_in_sub_query659); if (state.failed) return retval;
            pushFollow(FOLLOW_query_expression_in_sub_query662);
            query_expression30=query_expression();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, query_expression30.getTree());
            char_literal31=(Token)match(input,53,FOLLOW_53_in_sub_query664); if (state.failed) return retval;

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
        return retval;
    }
    // $ANTLR end "sub_query"

    public static class select_list_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "select_list"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:124:1: select_list : ( '*' -> ^( COLUMN '*' ) | derived_column ( ',' derived_column )* );
    public final SQL92QueryParser.select_list_return select_list() throws RecognitionException {
        SQL92QueryParser.select_list_return retval = new SQL92QueryParser.select_list_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal32=null;
        Token char_literal34=null;
        SQL92QueryParser.derived_column_return derived_column33 = null;

        SQL92QueryParser.derived_column_return derived_column35 = null;


        CommonTree char_literal32_tree=null;
        CommonTree char_literal34_tree=null;
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:125:5: ( '*' -> ^( COLUMN '*' ) | derived_column ( ',' derived_column )* )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==54) ) {
                alt12=1;
            }
            else if ( ((LA12_0>=INT && LA12_0<=STRING)||LA12_0==52||LA12_0==56||(LA12_0>=63 && LA12_0<=74)||(LA12_0>=76 && LA12_0<=78)) ) {
                alt12=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:125:9: '*'
                    {
                    char_literal32=(Token)match(input,54,FOLLOW_54_in_select_list683); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_54.add(char_literal32);



                    // AST REWRITE
                    // elements: 54
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 125:13: -> ^( COLUMN '*' )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:125:16: ^( COLUMN '*' )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        adaptor.addChild(root_1, stream_54.nextNode());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:126:9: derived_column ( ',' derived_column )*
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_derived_column_in_select_list701);
                    derived_column33=derived_column();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column33.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:126:24: ( ',' derived_column )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==55) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:126:25: ',' derived_column
                    	    {
                    	    char_literal34=(Token)match(input,55,FOLLOW_55_in_select_list704); if (state.failed) return retval;
                    	    pushFollow(FOLLOW_derived_column_in_select_list707);
                    	    derived_column35=derived_column();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column35.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


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
        return retval;
    }
    // $ANTLR end "select_list"

    public static class derived_column_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "derived_column"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:128:1: derived_column : ( 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) );
    public final SQL92QueryParser.derived_column_return derived_column() throws RecognitionException {
        SQL92QueryParser.derived_column_return retval = new SQL92QueryParser.derived_column_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token id1=null;
        Token id2=null;
        Token string_literal36=null;
        Token string_literal38=null;
        Token string_literal39=null;
        Token string_literal41=null;
        Token ID42=null;
        SQL92QueryParser.value_expression_return value_expression37 = null;

        SQL92QueryParser.value_expression_return value_expression40 = null;


        CommonTree id1_tree=null;
        CommonTree id2_tree=null;
        CommonTree string_literal36_tree=null;
        CommonTree string_literal38_tree=null;
        CommonTree string_literal39_tree=null;
        CommonTree string_literal41_tree=null;
        CommonTree ID42_tree=null;
        RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
        RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:5: ( 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==56) ) {
                alt17=1;
            }
            else if ( ((LA17_0>=INT && LA17_0<=STRING)||LA17_0==52||(LA17_0>=63 && LA17_0<=74)||(LA17_0>=76 && LA17_0<=78)) ) {
                alt17=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:9: 'CAST' value_expression 'AS' id1= ID ( ( 'AS' )? id2= ID )?
                    {
                    string_literal36=(Token)match(input,56,FOLLOW_56_in_derived_column727); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_56.add(string_literal36);

                    pushFollow(FOLLOW_value_expression_in_derived_column729);
                    value_expression37=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression37.getTree());
                    string_literal38=(Token)match(input,57,FOLLOW_57_in_derived_column731); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_57.add(string_literal38);

                    id1=(Token)match(input,ID,FOLLOW_ID_in_derived_column735); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(id1);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:45: ( ( 'AS' )? id2= ID )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==ID||LA14_0==57) ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:46: ( 'AS' )? id2= ID
                            {
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:46: ( 'AS' )?
                            int alt13=2;
                            int LA13_0 = input.LA(1);

                            if ( (LA13_0==57) ) {
                                alt13=1;
                            }
                            switch (alt13) {
                                case 1 :
                                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'AS'
                                    {
                                    string_literal39=(Token)match(input,57,FOLLOW_57_in_derived_column738); if (state.failed) return retval; 
                                    if ( state.backtracking==0 ) stream_57.add(string_literal39);


                                    }
                                    break;

                            }

                            id2=(Token)match(input,ID,FOLLOW_ID_in_derived_column743); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(id2);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: id1, value_expression, id2
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
                    // 129:61: -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:64: ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:73: ^( CAST value_expression $id1)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CAST, "CAST"), root_2);

                        adaptor.addChild(root_2, stream_value_expression.nextTree());
                        adaptor.addChild(root_2, stream_id1.nextNode());

                        adaptor.addChild(root_1, root_2);
                        }
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:103: ( $id2)?
                        if ( stream_id2.hasNext() ) {
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
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:9: value_expression ( ( 'AS' )? ID )?
                    {
                    pushFollow(FOLLOW_value_expression_in_derived_column774);
                    value_expression40=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression40.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:26: ( ( 'AS' )? ID )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0==ID||LA16_0==57) ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:27: ( 'AS' )? ID
                            {
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:27: ( 'AS' )?
                            int alt15=2;
                            int LA15_0 = input.LA(1);

                            if ( (LA15_0==57) ) {
                                alt15=1;
                            }
                            switch (alt15) {
                                case 1 :
                                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'AS'
                                    {
                                    string_literal41=(Token)match(input,57,FOLLOW_57_in_derived_column777); if (state.failed) return retval; 
                                    if ( state.backtracking==0 ) stream_57.add(string_literal41);


                                    }
                                    break;

                            }

                            ID42=(Token)match(input,ID,FOLLOW_ID_in_derived_column780); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(ID42);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: ID, value_expression
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 130:38: -> ^( COLUMN value_expression ( ID )? )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:41: ^( COLUMN value_expression ( ID )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);

                        adaptor.addChild(root_1, stream_value_expression.nextTree());
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:67: ( ID )?
                        if ( stream_ID.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "derived_column"

    public static class order_by_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "order_by"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:132:1: order_by : 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )* -> ^( ORDER ( ordered_sort_spec )+ ) ;
    public final SQL92QueryParser.order_by_return order_by() throws RecognitionException {
        SQL92QueryParser.order_by_return retval = new SQL92QueryParser.order_by_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal43=null;
        Token string_literal44=null;
        Token char_literal46=null;
        SQL92QueryParser.ordered_sort_spec_return ordered_sort_spec45 = null;

        SQL92QueryParser.ordered_sort_spec_return ordered_sort_spec47 = null;


        CommonTree string_literal43_tree=null;
        CommonTree string_literal44_tree=null;
        CommonTree char_literal46_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
        RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
        RewriteRuleSubtreeStream stream_ordered_sort_spec=new RewriteRuleSubtreeStream(adaptor,"rule ordered_sort_spec");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:5: ( 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )* -> ^( ORDER ( ordered_sort_spec )+ ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:9: 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )*
            {
            string_literal43=(Token)match(input,58,FOLLOW_58_in_order_by814); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_58.add(string_literal43);

            string_literal44=(Token)match(input,59,FOLLOW_59_in_order_by816); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_59.add(string_literal44);

            pushFollow(FOLLOW_ordered_sort_spec_in_order_by818);
            ordered_sort_spec45=ordered_sort_spec();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_ordered_sort_spec.add(ordered_sort_spec45.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:40: ( ',' ordered_sort_spec )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==55) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:41: ',' ordered_sort_spec
            	    {
            	    char_literal46=(Token)match(input,55,FOLLOW_55_in_order_by821); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_55.add(char_literal46);

            	    pushFollow(FOLLOW_ordered_sort_spec_in_order_by823);
            	    ordered_sort_spec47=ordered_sort_spec();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_ordered_sort_spec.add(ordered_sort_spec47.getTree());

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);



            // AST REWRITE
            // elements: ordered_sort_spec
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 133:65: -> ^( ORDER ( ordered_sort_spec )+ )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:68: ^( ORDER ( ordered_sort_spec )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ORDER, "ORDER"), root_1);

                if ( !(stream_ordered_sort_spec.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_ordered_sort_spec.hasNext() ) {
                    adaptor.addChild(root_1, stream_ordered_sort_spec.nextTree());

                }
                stream_ordered_sort_spec.reset();

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
        return retval;
    }
    // $ANTLR end "order_by"

    public static class sort_spec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "sort_spec"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:135:1: sort_spec : ( column_name | INT | reserved_word_column_name );
    public final SQL92QueryParser.sort_spec_return sort_spec() throws RecognitionException {
        SQL92QueryParser.sort_spec_return retval = new SQL92QueryParser.sort_spec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT49=null;
        SQL92QueryParser.column_name_return column_name48 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name50 = null;


        CommonTree INT49_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:136:5: ( column_name | INT | reserved_word_column_name )
            int alt19=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                int LA19_1 = input.LA(2);

                if ( (LA19_1==62) ) {
                    int LA19_4 = input.LA(3);

                    if ( ((LA19_4>=63 && LA19_4<=72)) ) {
                        alt19=3;
                    }
                    else if ( (LA19_4==ID) ) {
                        alt19=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 4, input);

                        throw nvae;
                    }
                }
                else if ( (LA19_1==EOF||(LA19_1>=40 && LA19_1<=41)||LA19_1==55||(LA19_1>=60 && LA19_1<=61)) ) {
                    alt19=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 19, 1, input);

                    throw nvae;
                }
                }
                break;
            case INT:
                {
                alt19=2;
                }
                break;
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
                {
                alt19=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:136:9: column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_column_name_in_sort_spec852);
                    column_name48=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name48.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:136:23: INT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    INT49=(Token)match(input,INT,FOLLOW_INT_in_sort_spec856); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT49_tree = (CommonTree)adaptor.create(INT49);
                    adaptor.addChild(root_0, INT49_tree);
                    }

                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:136:29: reserved_word_column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_reserved_word_column_name_in_sort_spec860);
                    reserved_word_column_name50=reserved_word_column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name50.getTree());

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
        return retval;
    }
    // $ANTLR end "sort_spec"

    public static class ordered_sort_spec_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ordered_sort_spec"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:138:1: ordered_sort_spec : ( sort_spec 'DESC' -> ^( DESC sort_spec ) | sort_spec ( 'ASC' )? -> ^( ASC sort_spec ) );
    public final SQL92QueryParser.ordered_sort_spec_return ordered_sort_spec() throws RecognitionException {
        SQL92QueryParser.ordered_sort_spec_return retval = new SQL92QueryParser.ordered_sort_spec_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal52=null;
        Token string_literal54=null;
        SQL92QueryParser.sort_spec_return sort_spec51 = null;

        SQL92QueryParser.sort_spec_return sort_spec53 = null;


        CommonTree string_literal52_tree=null;
        CommonTree string_literal54_tree=null;
        RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
        RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
        RewriteRuleSubtreeStream stream_sort_spec=new RewriteRuleSubtreeStream(adaptor,"rule sort_spec");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:139:5: ( sort_spec 'DESC' -> ^( DESC sort_spec ) | sort_spec ( 'ASC' )? -> ^( ASC sort_spec ) )
            int alt21=2;
            alt21 = dfa21.predict(input);
            switch (alt21) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:139:9: sort_spec 'DESC'
                    {
                    pushFollow(FOLLOW_sort_spec_in_ordered_sort_spec878);
                    sort_spec51=sort_spec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sort_spec.add(sort_spec51.getTree());
                    string_literal52=(Token)match(input,60,FOLLOW_60_in_ordered_sort_spec880); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_60.add(string_literal52);



                    // AST REWRITE
                    // elements: sort_spec
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 139:26: -> ^( DESC sort_spec )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:139:29: ^( DESC sort_spec )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DESC, "DESC"), root_1);

                        adaptor.addChild(root_1, stream_sort_spec.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:9: sort_spec ( 'ASC' )?
                    {
                    pushFollow(FOLLOW_sort_spec_in_ordered_sort_spec898);
                    sort_spec53=sort_spec();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sort_spec.add(sort_spec53.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:19: ( 'ASC' )?
                    int alt20=2;
                    int LA20_0 = input.LA(1);

                    if ( (LA20_0==61) ) {
                        alt20=1;
                    }
                    switch (alt20) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'ASC'
                            {
                            string_literal54=(Token)match(input,61,FOLLOW_61_in_ordered_sort_spec900); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_61.add(string_literal54);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: sort_spec
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 140:26: -> ^( ASC sort_spec )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:29: ^( ASC sort_spec )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ASC, "ASC"), root_1);

                        adaptor.addChild(root_1, stream_sort_spec.nextTree());

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
        return retval;
    }
    // $ANTLR end "ordered_sort_spec"

    public static class reserved_word_column_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "reserved_word_column_name"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:142:1: reserved_word_column_name : (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) ;
    public final SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name() throws RecognitionException {
        SQL92QueryParser.reserved_word_column_name_return retval = new SQL92QueryParser.reserved_word_column_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token char_literal55=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree char_literal55_tree=null;
        RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
        RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
        RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:5: ( (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:9: (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
            {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:9: (tableid= ID '.' )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==ID) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:10: tableid= ID '.'
                    {
                    tableid=(Token)match(input,ID,FOLLOW_ID_in_reserved_word_column_name930); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(tableid);

                    char_literal55=(Token)match(input,62,FOLLOW_62_in_reserved_word_column_name931); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_62.add(char_literal55);


                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:25: (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' | s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
            int alt23=10;
            switch ( input.LA(1) ) {
            case 63:
                {
                alt23=1;
                }
                break;
            case 64:
                {
                alt23=2;
                }
                break;
            case 65:
                {
                alt23=3;
                }
                break;
            case 66:
                {
                alt23=4;
                }
                break;
            case 67:
                {
                alt23=5;
                }
                break;
            case 68:
                {
                alt23=6;
                }
                break;
            case 69:
                {
                alt23=7;
                }
                break;
            case 70:
                {
                alt23=8;
                }
                break;
            case 71:
                {
                alt23=9;
                }
                break;
            case 72:
                {
                alt23=10;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:26: s= 'DATE'
                    {
                    s=(Token)match(input,63,FOLLOW_63_in_reserved_word_column_name937); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_63.add(s);


                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:37: s= 'TIMESTAMP'
                    {
                    s=(Token)match(input,64,FOLLOW_64_in_reserved_word_column_name943); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_64.add(s);


                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:53: s= 'TIME'
                    {
                    s=(Token)match(input,65,FOLLOW_65_in_reserved_word_column_name949); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_65.add(s);


                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:64: s= 'INTERVAL'
                    {
                    s=(Token)match(input,66,FOLLOW_66_in_reserved_word_column_name955); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_66.add(s);


                    }
                    break;
                case 5 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:79: s= 'YEAR'
                    {
                    s=(Token)match(input,67,FOLLOW_67_in_reserved_word_column_name961); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_67.add(s);


                    }
                    break;
                case 6 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:90: s= 'MONTH'
                    {
                    s=(Token)match(input,68,FOLLOW_68_in_reserved_word_column_name967); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_68.add(s);


                    }
                    break;
                case 7 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:102: s= 'DAY'
                    {
                    s=(Token)match(input,69,FOLLOW_69_in_reserved_word_column_name973); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_69.add(s);


                    }
                    break;
                case 8 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:112: s= 'HOUR'
                    {
                    s=(Token)match(input,70,FOLLOW_70_in_reserved_word_column_name979); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_70.add(s);


                    }
                    break;
                case 9 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:123: s= 'MINUTE'
                    {
                    s=(Token)match(input,71,FOLLOW_71_in_reserved_word_column_name985); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_71.add(s);


                    }
                    break;
                case 10 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:136: s= 'SECOND'
                    {
                    s=(Token)match(input,72,FOLLOW_72_in_reserved_word_column_name991); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_72.add(s);


                    }
                    break;

            }



            // AST REWRITE
            // elements: tableid, s
            // token labels: s, tableid
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 144:13: -> ^( TABLECOLUMN ( $tableid)? $s)
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:16: ^( TABLECOLUMN ( $tableid)? $s)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:30: ( $tableid)?
                if ( stream_tableid.hasNext() ) {
                    adaptor.addChild(root_1, stream_tableid.nextNode());

                }
                stream_tableid.reset();
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
        return retval;
    }
    // $ANTLR end "reserved_word_column_name"

    public static class value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value_expression"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:146:1: value_expression : ( string_value_expression | numeric_value_expression );
    public final SQL92QueryParser.value_expression_return value_expression() throws RecognitionException {
        SQL92QueryParser.value_expression_return retval = new SQL92QueryParser.value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.string_value_expression_return string_value_expression56 = null;

        SQL92QueryParser.numeric_value_expression_return numeric_value_expression57 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:5: ( string_value_expression | numeric_value_expression )
            int alt24=2;
            switch ( input.LA(1) ) {
            case ID:
                {
                switch ( input.LA(2) ) {
                case 62:
                    {
                    int LA24_4 = input.LA(3);

                    if ( ((LA24_4>=63 && LA24_4<=72)) ) {
                        alt24=2;
                    }
                    else if ( (LA24_4==ID) ) {
                        int LA24_6 = input.LA(4);

                        if ( (LA24_6==79) ) {
                            alt24=1;
                        }
                        else if ( (LA24_6==EOF||(LA24_6>=INT && LA24_6<=STRING)||(LA24_6>=40 && LA24_6<=42)||(LA24_6>=44 && LA24_6<=45)||(LA24_6>=47 && LA24_6<=50)||(LA24_6>=52 && LA24_6<=55)||(LA24_6>=57 && LA24_6<=58)||(LA24_6>=63 && LA24_6<=78)||LA24_6==80||(LA24_6>=82 && LA24_6<=85)||(LA24_6>=87 && LA24_6<=100)||(LA24_6>=103 && LA24_6<=105)) ) {
                            alt24=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 24, 6, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 24, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case EOF:
                case INT:
                case ID:
                case FLOAT:
                case NUMERIC:
                case STRING:
                case 40:
                case 41:
                case 42:
                case 44:
                case 45:
                case 47:
                case 48:
                case 49:
                case 50:
                case 52:
                case 53:
                case 54:
                case 55:
                case 57:
                case 58:
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
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case 80:
                case 82:
                case 83:
                case 84:
                case 85:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 103:
                case 104:
                case 105:
                    {
                    alt24=2;
                    }
                    break;
                case 79:
                    {
                    alt24=1;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 24, 1, input);

                    throw nvae;
                }

                }
                break;
            case STRING:
                {
                int LA24_2 = input.LA(2);

                if ( (LA24_2==EOF||(LA24_2>=INT && LA24_2<=STRING)||(LA24_2>=40 && LA24_2<=42)||(LA24_2>=44 && LA24_2<=45)||(LA24_2>=47 && LA24_2<=50)||(LA24_2>=52 && LA24_2<=55)||(LA24_2>=57 && LA24_2<=58)||(LA24_2>=63 && LA24_2<=78)||LA24_2==80||(LA24_2>=82 && LA24_2<=85)||(LA24_2>=87 && LA24_2<=100)||(LA24_2>=103 && LA24_2<=105)) ) {
                    alt24=2;
                }
                else if ( (LA24_2==79) ) {
                    alt24=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 24, 2, input);

                    throw nvae;
                }
                }
                break;
            case INT:
            case FLOAT:
            case NUMERIC:
            case 52:
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
            case 73:
            case 74:
            case 76:
            case 77:
            case 78:
                {
                alt24=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:9: string_value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_string_value_expression_in_value_expression1037);
                    string_value_expression56=string_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, string_value_expression56.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:148:9: numeric_value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_numeric_value_expression_in_value_expression1047);
                    numeric_value_expression57=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_value_expression57.getTree());

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
        return retval;
    }
    // $ANTLR end "value_expression"

    public static class numeric_value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_value_expression"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:150:1: numeric_value_expression : factor ( ( '+' | '-' ) factor )* ;
    public final SQL92QueryParser.numeric_value_expression_return numeric_value_expression() throws RecognitionException {
        SQL92QueryParser.numeric_value_expression_return retval = new SQL92QueryParser.numeric_value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set59=null;
        SQL92QueryParser.factor_return factor58 = null;

        SQL92QueryParser.factor_return factor60 = null;


        CommonTree set59_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:5: ( factor ( ( '+' | '-' ) factor )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:9: factor ( ( '+' | '-' ) factor )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_factor_in_numeric_value_expression1066);
            factor58=factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, factor58.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:16: ( ( '+' | '-' ) factor )*
            loop25:
            do {
                int alt25=2;
                alt25 = dfa25.predict(input);
                switch (alt25) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:17: ( '+' | '-' ) factor
            	    {
            	    set59=(Token)input.LT(1);
            	    set59=(Token)input.LT(1);
            	    if ( (input.LA(1)>=73 && input.LA(1)<=74) ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set59), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_factor_in_numeric_value_expression1076);
            	    factor60=factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, factor60.getTree());

            	    }
            	    break;

            	default :
            	    break loop25;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "numeric_value_expression"

    public static class factor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "factor"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:153:1: factor : numeric_primary ( ( '*' | '/' ) numeric_primary )* ;
    public final SQL92QueryParser.factor_return factor() throws RecognitionException {
        SQL92QueryParser.factor_return retval = new SQL92QueryParser.factor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token set62=null;
        SQL92QueryParser.numeric_primary_return numeric_primary61 = null;

        SQL92QueryParser.numeric_primary_return numeric_primary63 = null;


        CommonTree set62_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:154:5: ( numeric_primary ( ( '*' | '/' ) numeric_primary )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:154:9: numeric_primary ( ( '*' | '/' ) numeric_primary )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_numeric_primary_in_factor1098);
            numeric_primary61=numeric_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary61.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:154:25: ( ( '*' | '/' ) numeric_primary )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0==54||LA26_0==75) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:154:26: ( '*' | '/' ) numeric_primary
            	    {
            	    set62=(Token)input.LT(1);
            	    set62=(Token)input.LT(1);
            	    if ( input.LA(1)==54||input.LA(1)==75 ) {
            	        input.consume();
            	        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set62), root_0);
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_numeric_primary_in_factor1108);
            	    numeric_primary63=numeric_primary();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary63.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "factor"

    public static class numeric_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_primary"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:156:1: numeric_primary : ( '+' | '-' )? value_expression_primary ;
    public final SQL92QueryParser.numeric_primary_return numeric_primary() throws RecognitionException {
        SQL92QueryParser.numeric_primary_return retval = new SQL92QueryParser.numeric_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal64=null;
        Token char_literal65=null;
        SQL92QueryParser.value_expression_primary_return value_expression_primary66 = null;


        CommonTree char_literal64_tree=null;
        CommonTree char_literal65_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:5: ( ( '+' | '-' )? value_expression_primary )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:9: ( '+' | '-' )? value_expression_primary
            {
            root_0 = (CommonTree)adaptor.nil();

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:9: ( '+' | '-' )?
            int alt27=3;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==73) ) {
                alt27=1;
            }
            else if ( (LA27_0==74) ) {
                alt27=2;
            }
            switch (alt27) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:10: '+'
                    {
                    char_literal64=(Token)match(input,73,FOLLOW_73_in_numeric_primary1129); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal64_tree = (CommonTree)adaptor.create(char_literal64);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal64_tree, root_0);
                    }

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:15: '-'
                    {
                    char_literal65=(Token)match(input,74,FOLLOW_74_in_numeric_primary1132); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal65_tree = (CommonTree)adaptor.create(char_literal65);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal65_tree, root_0);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_value_expression_primary_in_numeric_primary1137);
            value_expression_primary66=value_expression_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression_primary66.getTree());

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
        return retval;
    }
    // $ANTLR end "numeric_primary"

    public static class value_expression_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value_expression_primary"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:159:1: value_expression_primary : ( '(' value_expression ')' | function | column_name | literal | sub_query );
    public final SQL92QueryParser.value_expression_primary_return value_expression_primary() throws RecognitionException {
        SQL92QueryParser.value_expression_primary_return retval = new SQL92QueryParser.value_expression_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal67=null;
        Token char_literal69=null;
        SQL92QueryParser.value_expression_return value_expression68 = null;

        SQL92QueryParser.function_return function70 = null;

        SQL92QueryParser.column_name_return column_name71 = null;

        SQL92QueryParser.literal_return literal72 = null;

        SQL92QueryParser.sub_query_return sub_query73 = null;


        CommonTree char_literal67_tree=null;
        CommonTree char_literal69_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:160:5: ( '(' value_expression ')' | function | column_name | literal | sub_query )
            int alt28=5;
            alt28 = dfa28.predict(input);
            switch (alt28) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:160:9: '(' value_expression ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal67=(Token)match(input,52,FOLLOW_52_in_value_expression_primary1157); if (state.failed) return retval;
                    pushFollow(FOLLOW_value_expression_in_value_expression_primary1160);
                    value_expression68=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression68.getTree());
                    char_literal69=(Token)match(input,53,FOLLOW_53_in_value_expression_primary1162); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:9: function
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_function_in_value_expression_primary1173);
                    function70=function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, function70.getTree());

                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:162:9: column_name
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_column_name_in_value_expression_primary1183);
                    column_name71=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name71.getTree());

                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:163:9: literal
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_literal_in_value_expression_primary1193);
                    literal72=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, literal72.getTree());

                    }
                    break;
                case 5 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:164:9: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_sub_query_in_value_expression_primary1203);
                    sub_query73=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query73.getTree());

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
        return retval;
    }
    // $ANTLR end "value_expression_primary"

    public static class literal_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "literal"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:166:1: literal : ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' );
    public final SQL92QueryParser.literal_return literal() throws RecognitionException {
        SQL92QueryParser.literal_return retval = new SQL92QueryParser.literal_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token INT74=null;
        Token FLOAT75=null;
        Token NUMERIC76=null;
        Token STRING77=null;
        Token string_literal80=null;
        Token string_literal81=null;
        Token string_literal82=null;
        SQL92QueryParser.datetime_return datetime78 = null;

        SQL92QueryParser.interval_return interval79 = null;


        CommonTree INT74_tree=null;
        CommonTree FLOAT75_tree=null;
        CommonTree NUMERIC76_tree=null;
        CommonTree STRING77_tree=null;
        CommonTree string_literal80_tree=null;
        CommonTree string_literal81_tree=null;
        CommonTree string_literal82_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:5: ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' )
            int alt29=9;
            alt29 = dfa29.predict(input);
            switch (alt29) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:9: INT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    INT74=(Token)match(input,INT,FOLLOW_INT_in_literal1221); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT74_tree = (CommonTree)adaptor.create(INT74);
                    adaptor.addChild(root_0, INT74_tree);
                    }

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:15: FLOAT
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    FLOAT75=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_literal1225); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FLOAT75_tree = (CommonTree)adaptor.create(FLOAT75);
                    adaptor.addChild(root_0, FLOAT75_tree);
                    }

                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:23: NUMERIC
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    NUMERIC76=(Token)match(input,NUMERIC,FOLLOW_NUMERIC_in_literal1229); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMERIC76_tree = (CommonTree)adaptor.create(NUMERIC76);
                    adaptor.addChild(root_0, NUMERIC76_tree);
                    }

                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:33: STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    STRING77=(Token)match(input,STRING,FOLLOW_STRING_in_literal1233); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING77_tree = (CommonTree)adaptor.create(STRING77);
                    adaptor.addChild(root_0, STRING77_tree);
                    }

                    }
                    break;
                case 5 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:42: datetime
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_datetime_in_literal1237);
                    datetime78=datetime();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime78.getTree());

                    }
                    break;
                case 6 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:53: interval
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_interval_in_literal1241);
                    interval79=interval();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, interval79.getTree());

                    }
                    break;
                case 7 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:64: 'NULL'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal80=(Token)match(input,76,FOLLOW_76_in_literal1245); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal80_tree = (CommonTree)adaptor.create(string_literal80);
                    adaptor.addChild(root_0, string_literal80_tree);
                    }

                    }
                    break;
                case 8 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:73: 'TRUE'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal81=(Token)match(input,77,FOLLOW_77_in_literal1249); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal81_tree = (CommonTree)adaptor.create(string_literal81);
                    adaptor.addChild(root_0, string_literal81_tree);
                    }

                    }
                    break;
                case 9 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:82: 'FALSE'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal82=(Token)match(input,78,FOLLOW_78_in_literal1253); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal82_tree = (CommonTree)adaptor.create(string_literal82);
                    adaptor.addChild(root_0, string_literal82_tree);
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
        return retval;
    }
    // $ANTLR end "literal"

    public static class datetime_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datetime"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:169:1: datetime : ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING | (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
    public final SQL92QueryParser.datetime_return datetime() throws RecognitionException {
        SQL92QueryParser.datetime_return retval = new SQL92QueryParser.datetime_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token set83=null;
        Token STRING84=null;
        Token char_literal85=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree set83_tree=null;
        CommonTree STRING84_tree=null;
        CommonTree char_literal85_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
        RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
        RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
        RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:170:5: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING | (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            int alt32=2;
            switch ( input.LA(1) ) {
            case 63:
                {
                int LA32_1 = input.LA(2);

                if ( (LA32_1==STRING) ) {
                    int LA32_5 = input.LA(3);

                    if ( (synpred58_SQL92Query()) ) {
                        alt32=1;
                    }
                    else if ( (true) ) {
                        alt32=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 32, 5, input);

                        throw nvae;
                    }
                }
                else if ( (LA32_1==EOF||(LA32_1>=INT && LA32_1<=NUMERIC)||(LA32_1>=40 && LA32_1<=42)||(LA32_1>=44 && LA32_1<=45)||(LA32_1>=47 && LA32_1<=50)||(LA32_1>=52 && LA32_1<=55)||(LA32_1>=57 && LA32_1<=58)||(LA32_1>=63 && LA32_1<=78)||LA32_1==80||(LA32_1>=82 && LA32_1<=85)||(LA32_1>=87 && LA32_1<=100)||(LA32_1>=103 && LA32_1<=105)) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
                }
                break;
            case ID:
                {
                alt32=2;
                }
                break;
            case 64:
                {
                int LA32_3 = input.LA(2);

                if ( (LA32_3==STRING) ) {
                    int LA32_5 = input.LA(3);

                    if ( (synpred58_SQL92Query()) ) {
                        alt32=1;
                    }
                    else if ( (true) ) {
                        alt32=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 32, 5, input);

                        throw nvae;
                    }
                }
                else if ( (LA32_3==EOF||(LA32_3>=INT && LA32_3<=NUMERIC)||(LA32_3>=40 && LA32_3<=42)||(LA32_3>=44 && LA32_3<=45)||(LA32_3>=47 && LA32_3<=50)||(LA32_3>=52 && LA32_3<=55)||(LA32_3>=57 && LA32_3<=58)||(LA32_3>=63 && LA32_3<=78)||LA32_3==80||(LA32_3>=82 && LA32_3<=85)||(LA32_3>=87 && LA32_3<=100)||(LA32_3>=103 && LA32_3<=105)) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 3, input);

                    throw nvae;
                }
                }
                break;
            case 65:
                {
                int LA32_4 = input.LA(2);

                if ( (LA32_4==STRING) ) {
                    int LA32_5 = input.LA(3);

                    if ( (synpred58_SQL92Query()) ) {
                        alt32=1;
                    }
                    else if ( (true) ) {
                        alt32=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 32, 5, input);

                        throw nvae;
                    }
                }
                else if ( (LA32_4==EOF||(LA32_4>=INT && LA32_4<=NUMERIC)||(LA32_4>=40 && LA32_4<=42)||(LA32_4>=44 && LA32_4<=45)||(LA32_4>=47 && LA32_4<=50)||(LA32_4>=52 && LA32_4<=55)||(LA32_4>=57 && LA32_4<=58)||(LA32_4>=63 && LA32_4<=78)||LA32_4==80||(LA32_4>=82 && LA32_4<=85)||(LA32_4>=87 && LA32_4<=100)||(LA32_4>=103 && LA32_4<=105)) ) {
                    alt32=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:170:9: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    set83=(Token)input.LT(1);
                    set83=(Token)input.LT(1);
                    if ( (input.LA(1)>=63 && input.LA(1)<=65) ) {
                        input.consume();
                        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set83), root_0);
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    STRING84=(Token)match(input,STRING,FOLLOW_STRING_in_datetime1284); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING84_tree = (CommonTree)adaptor.create(STRING84);
                    adaptor.addChild(root_0, STRING84_tree);
                    }

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:9: (tableid= ID '.' )? (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' )
                    {
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:9: (tableid= ID '.' )?
                    int alt30=2;
                    int LA30_0 = input.LA(1);

                    if ( (LA30_0==ID) ) {
                        alt30=1;
                    }
                    switch (alt30) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:10: tableid= ID '.'
                            {
                            tableid=(Token)match(input,ID,FOLLOW_ID_in_datetime1297); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(tableid);

                            char_literal85=(Token)match(input,62,FOLLOW_62_in_datetime1298); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_62.add(char_literal85);


                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:25: (s= 'DATE' | s= 'TIMESTAMP' | s= 'TIME' )
                    int alt31=3;
                    switch ( input.LA(1) ) {
                    case 63:
                        {
                        alt31=1;
                        }
                        break;
                    case 64:
                        {
                        alt31=2;
                        }
                        break;
                    case 65:
                        {
                        alt31=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 31, 0, input);

                        throw nvae;
                    }

                    switch (alt31) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:26: s= 'DATE'
                            {
                            s=(Token)match(input,63,FOLLOW_63_in_datetime1304); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_63.add(s);


                            }
                            break;
                        case 2 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:37: s= 'TIMESTAMP'
                            {
                            s=(Token)match(input,64,FOLLOW_64_in_datetime1310); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_64.add(s);


                            }
                            break;
                        case 3 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:53: s= 'TIME'
                            {
                            s=(Token)match(input,65,FOLLOW_65_in_datetime1316); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_65.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: s, tableid
                    // token labels: s, tableid
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
                    RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 171:63: -> ^( TABLECOLUMN ( $tableid)? $s)
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:66: ^( TABLECOLUMN ( $tableid)? $s)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:80: ( $tableid)?
                        if ( stream_tableid.hasNext() ) {
                            adaptor.addChild(root_1, stream_tableid.nextNode());

                        }
                        stream_tableid.reset();
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
        return retval;
    }
    // $ANTLR end "datetime"

    public static class interval_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "interval"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:173:1: interval : ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
    public final SQL92QueryParser.interval_return interval() throws RecognitionException {
        SQL92QueryParser.interval_return retval = new SQL92QueryParser.interval_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token s=null;
        Token string_literal86=null;
        Token STRING87=null;
        Token set88=null;
        Token char_literal89=null;

        CommonTree tableid_tree=null;
        CommonTree s_tree=null;
        CommonTree string_literal86_tree=null;
        CommonTree STRING87_tree=null;
        CommonTree set88_tree=null;
        CommonTree char_literal89_tree=null;
        RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
        RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
        RewriteRuleTokenStream stream_68=new RewriteRuleTokenStream(adaptor,"token 68");
        RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
        RewriteRuleTokenStream stream_70=new RewriteRuleTokenStream(adaptor,"token 70");
        RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");
        RewriteRuleTokenStream stream_72=new RewriteRuleTokenStream(adaptor,"token 72");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:5: ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
            int alt35=2;
            alt35 = dfa35.predict(input);
            switch (alt35) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal86=(Token)match(input,66,FOLLOW_66_in_interval1348); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal86_tree = (CommonTree)adaptor.create(string_literal86);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal86_tree, root_0);
                    }
                    STRING87=(Token)match(input,STRING,FOLLOW_STRING_in_interval1351); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING87_tree = (CommonTree)adaptor.create(STRING87);
                    adaptor.addChild(root_0, STRING87_tree);
                    }
                    set88=(Token)input.LT(1);
                    if ( (input.LA(1)>=67 && input.LA(1)<=72) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set88));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:9: (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
                    {
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:9: (tableid= ID '.' )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==ID) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:10: tableid= ID '.'
                            {
                            tableid=(Token)match(input,ID,FOLLOW_ID_in_interval1388); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_ID.add(tableid);

                            char_literal89=(Token)match(input,62,FOLLOW_62_in_interval1389); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_62.add(char_literal89);


                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:25: (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' )
                    int alt34=7;
                    switch ( input.LA(1) ) {
                    case 66:
                        {
                        alt34=1;
                        }
                        break;
                    case 67:
                        {
                        alt34=2;
                        }
                        break;
                    case 68:
                        {
                        alt34=3;
                        }
                        break;
                    case 69:
                        {
                        alt34=4;
                        }
                        break;
                    case 70:
                        {
                        alt34=5;
                        }
                        break;
                    case 71:
                        {
                        alt34=6;
                        }
                        break;
                    case 72:
                        {
                        alt34=7;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 34, 0, input);

                        throw nvae;
                    }

                    switch (alt34) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:26: s= 'INTERVAL'
                            {
                            s=(Token)match(input,66,FOLLOW_66_in_interval1395); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_66.add(s);


                            }
                            break;
                        case 2 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:41: s= 'YEAR'
                            {
                            s=(Token)match(input,67,FOLLOW_67_in_interval1401); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_67.add(s);


                            }
                            break;
                        case 3 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:52: s= 'MONTH'
                            {
                            s=(Token)match(input,68,FOLLOW_68_in_interval1407); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_68.add(s);


                            }
                            break;
                        case 4 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:64: s= 'DAY'
                            {
                            s=(Token)match(input,69,FOLLOW_69_in_interval1413); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_69.add(s);


                            }
                            break;
                        case 5 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:74: s= 'HOUR'
                            {
                            s=(Token)match(input,70,FOLLOW_70_in_interval1419); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_70.add(s);


                            }
                            break;
                        case 6 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:85: s= 'MINUTE'
                            {
                            s=(Token)match(input,71,FOLLOW_71_in_interval1425); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_71.add(s);


                            }
                            break;
                        case 7 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:98: s= 'SECOND'
                            {
                            s=(Token)match(input,72,FOLLOW_72_in_interval1431); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_72.add(s);


                            }
                            break;

                    }



                    // AST REWRITE
                    // elements: tableid, s
                    // token labels: s, tableid
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_s=new RewriteRuleTokenStream(adaptor,"token s",s);
                    RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 175:110: -> ^( TABLECOLUMN ( $tableid)? $s)
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:113: ^( TABLECOLUMN ( $tableid)? $s)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:127: ( $tableid)?
                        if ( stream_tableid.hasNext() ) {
                            adaptor.addChild(root_1, stream_tableid.nextNode());

                        }
                        stream_tableid.reset();
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
        return retval;
    }
    // $ANTLR end "interval"

    public static class function_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "function"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:177:1: function : ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) );
    public final SQL92QueryParser.function_return function() throws RecognitionException {
        SQL92QueryParser.function_return retval = new SQL92QueryParser.function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token char_literal90=null;
        Token char_literal92=null;
        Token char_literal94=null;
        Token char_literal95=null;
        Token char_literal96=null;
        Token char_literal97=null;
        SQL92QueryParser.value_expression_return value_expression91 = null;

        SQL92QueryParser.value_expression_return value_expression93 = null;


        CommonTree name_tree=null;
        CommonTree char_literal90_tree=null;
        CommonTree char_literal92_tree=null;
        CommonTree char_literal94_tree=null;
        CommonTree char_literal95_tree=null;
        CommonTree char_literal96_tree=null;
        CommonTree char_literal97_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:5: ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==ID) ) {
                int LA38_1 = input.LA(2);

                if ( (LA38_1==52) ) {
                    int LA38_2 = input.LA(3);

                    if ( (LA38_2==54) ) {
                        alt38=2;
                    }
                    else if ( ((LA38_2>=INT && LA38_2<=STRING)||(LA38_2>=52 && LA38_2<=53)||LA38_2==55||(LA38_2>=63 && LA38_2<=74)||(LA38_2>=76 && LA38_2<=78)) ) {
                        alt38=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 38, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 38, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:9: (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')'
                    {
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:9: (name= ID )
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:10: name= ID
                    {
                    name=(Token)match(input,ID,FOLLOW_ID_in_function1469); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);


                    }

                    char_literal90=(Token)match(input,52,FOLLOW_52_in_function1472); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_52.add(char_literal90);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:23: ( value_expression )?
                    int alt36=2;
                    int LA36_0 = input.LA(1);

                    if ( ((LA36_0>=INT && LA36_0<=STRING)||LA36_0==52||(LA36_0>=63 && LA36_0<=74)||(LA36_0>=76 && LA36_0<=78)) ) {
                        alt36=1;
                    }
                    switch (alt36) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: value_expression
                            {
                            pushFollow(FOLLOW_value_expression_in_function1474);
                            value_expression91=value_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_value_expression.add(value_expression91.getTree());

                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:41: ( ',' value_expression )*
                    loop37:
                    do {
                        int alt37=2;
                        int LA37_0 = input.LA(1);

                        if ( (LA37_0==55) ) {
                            alt37=1;
                        }


                        switch (alt37) {
                    	case 1 :
                    	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:42: ',' value_expression
                    	    {
                    	    char_literal92=(Token)match(input,55,FOLLOW_55_in_function1478); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_55.add(char_literal92);

                    	    pushFollow(FOLLOW_value_expression_in_function1480);
                    	    value_expression93=value_expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value_expression.add(value_expression93.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop37;
                        }
                    } while (true);

                    char_literal94=(Token)match(input,53,FOLLOW_53_in_function1484); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(char_literal94);



                    // AST REWRITE
                    // elements: value_expression, name
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
                    // 179:13: -> ^( FUNCTION $name ( value_expression )* )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:16: ^( FUNCTION $name ( value_expression )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                        adaptor.addChild(root_1, stream_name.nextNode());
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:33: ( value_expression )*
                        while ( stream_value_expression.hasNext() ) {
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
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:180:9: (name= ID ) '(' '*' ')'
                    {
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:180:9: (name= ID )
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:180:10: name= ID
                    {
                    name=(Token)match(input,ID,FOLLOW_ID_in_function1522); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(name);


                    }

                    char_literal95=(Token)match(input,52,FOLLOW_52_in_function1525); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_52.add(char_literal95);

                    char_literal96=(Token)match(input,54,FOLLOW_54_in_function1527); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_54.add(char_literal96);

                    char_literal97=(Token)match(input,53,FOLLOW_53_in_function1529); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(char_literal97);



                    // AST REWRITE
                    // elements: 54, name
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
                    // 180:31: -> ^( FUNCTION $name '*' )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:180:34: ^( FUNCTION $name '*' )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                        adaptor.addChild(root_1, stream_name.nextNode());
                        adaptor.addChild(root_1, stream_54.nextNode());

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
        return retval;
    }
    // $ANTLR end "function"

    public static class string_value_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string_value_expression"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:1: string_value_expression : ( column_name | STRING ) ( '||' ( column_name | STRING ) )+ ;
    public final SQL92QueryParser.string_value_expression_return string_value_expression() throws RecognitionException {
        SQL92QueryParser.string_value_expression_return retval = new SQL92QueryParser.string_value_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token STRING99=null;
        Token string_literal100=null;
        Token STRING102=null;
        SQL92QueryParser.column_name_return column_name98 = null;

        SQL92QueryParser.column_name_return column_name101 = null;


        CommonTree STRING99_tree=null;
        CommonTree string_literal100_tree=null;
        CommonTree STRING102_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:5: ( ( column_name | STRING ) ( '||' ( column_name | STRING ) )+ )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:9: ( column_name | STRING ) ( '||' ( column_name | STRING ) )+
            {
            root_0 = (CommonTree)adaptor.nil();

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:9: ( column_name | STRING )
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==ID) ) {
                alt39=1;
            }
            else if ( (LA39_0==STRING) ) {
                alt39=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }
            switch (alt39) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:10: column_name
                    {
                    pushFollow(FOLLOW_column_name_in_string_value_expression1559);
                    column_name98=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name98.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:24: STRING
                    {
                    STRING99=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1563); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING99_tree = (CommonTree)adaptor.create(STRING99);
                    adaptor.addChild(root_0, STRING99_tree);
                    }

                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:32: ( '||' ( column_name | STRING ) )+
            int cnt41=0;
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==79) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:33: '||' ( column_name | STRING )
            	    {
            	    string_literal100=(Token)match(input,79,FOLLOW_79_in_string_value_expression1567); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal100_tree = (CommonTree)adaptor.create(string_literal100);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal100_tree, root_0);
            	    }
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:39: ( column_name | STRING )
            	    int alt40=2;
            	    int LA40_0 = input.LA(1);

            	    if ( (LA40_0==ID) ) {
            	        alt40=1;
            	    }
            	    else if ( (LA40_0==STRING) ) {
            	        alt40=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 40, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt40) {
            	        case 1 :
            	            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:40: column_name
            	            {
            	            pushFollow(FOLLOW_column_name_in_string_value_expression1571);
            	            column_name101=column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name101.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:54: STRING
            	            {
            	            STRING102=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1575); if (state.failed) return retval;
            	            if ( state.backtracking==0 ) {
            	            STRING102_tree = (CommonTree)adaptor.create(STRING102);
            	            adaptor.addChild(root_0, STRING102_tree);
            	            }

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt41 >= 1 ) break loop41;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(41, input);
                        throw eee;
                }
                cnt41++;
            } while (true);


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
        return retval;
    }
    // $ANTLR end "string_value_expression"

    public static class table_expression_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_expression"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:185:1: table_expression : table_reference ;
    public final SQL92QueryParser.table_expression_return table_expression() throws RecognitionException {
        SQL92QueryParser.table_expression_return retval = new SQL92QueryParser.table_expression_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_reference_return table_reference103 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:186:5: ( table_reference )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:186:9: table_reference
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_table_reference_in_table_expression1596);
            table_reference103=table_reference();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference103.getTree());

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
        return retval;
    }
    // $ANTLR end "table_expression"

    public static class table_reference_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_reference"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:188:1: table_reference : table ( ',' table_reference )* ;
    public final SQL92QueryParser.table_reference_return table_reference() throws RecognitionException {
        SQL92QueryParser.table_reference_return retval = new SQL92QueryParser.table_reference_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal105=null;
        SQL92QueryParser.table_return table104 = null;

        SQL92QueryParser.table_reference_return table_reference106 = null;


        CommonTree char_literal105_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:5: ( table ( ',' table_reference )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:9: table ( ',' table_reference )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_table_in_table_reference1614);
            table104=table();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, table104.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:15: ( ',' table_reference )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==55) ) {
                    int LA42_2 = input.LA(2);

                    if ( (synpred81_SQL92Query()) ) {
                        alt42=1;
                    }


                }


                switch (alt42) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:16: ',' table_reference
            	    {
            	    char_literal105=(Token)match(input,55,FOLLOW_55_in_table_reference1617); if (state.failed) return retval;
            	    pushFollow(FOLLOW_table_reference_in_table_reference1620);
            	    table_reference106=table_reference();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference106.getTree());

            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "table_reference"

    public static class join_type_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "join_type"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:191:1: join_type : ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN );
    public final SQL92QueryParser.join_type_return join_type() throws RecognitionException {
        SQL92QueryParser.join_type_return retval = new SQL92QueryParser.join_type_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal107=null;
        Token string_literal108=null;
        Token string_literal109=null;
        Token string_literal110=null;
        Token string_literal111=null;
        Token string_literal112=null;
        Token string_literal113=null;
        Token string_literal114=null;
        Token string_literal115=null;
        Token string_literal116=null;
        Token string_literal117=null;

        CommonTree string_literal107_tree=null;
        CommonTree string_literal108_tree=null;
        CommonTree string_literal109_tree=null;
        CommonTree string_literal110_tree=null;
        CommonTree string_literal111_tree=null;
        CommonTree string_literal112_tree=null;
        CommonTree string_literal113_tree=null;
        CommonTree string_literal114_tree=null;
        CommonTree string_literal115_tree=null;
        CommonTree string_literal116_tree=null;
        CommonTree string_literal117_tree=null;
        RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
        RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
        RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
        RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
        RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
        RewriteRuleTokenStream stream_85=new RewriteRuleTokenStream(adaptor,"token 85");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:192:5: ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN )
            int alt47=4;
            switch ( input.LA(1) ) {
            case 80:
                {
                alt47=1;
                }
                break;
            case 83:
                {
                alt47=2;
                }
                break;
            case 84:
                {
                alt47=3;
                }
                break;
            case 82:
            case 85:
                {
                alt47=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }

            switch (alt47) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:192:9: 'RIGHT' ( 'OUTER' )? 'JOIN'
                    {
                    string_literal107=(Token)match(input,80,FOLLOW_80_in_join_type1640); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_80.add(string_literal107);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:192:17: ( 'OUTER' )?
                    int alt43=2;
                    int LA43_0 = input.LA(1);

                    if ( (LA43_0==81) ) {
                        alt43=1;
                    }
                    switch (alt43) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'OUTER'
                            {
                            string_literal108=(Token)match(input,81,FOLLOW_81_in_join_type1642); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_81.add(string_literal108);


                            }
                            break;

                    }

                    string_literal109=(Token)match(input,82,FOLLOW_82_in_join_type1645); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal109);



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
                    // 192:33: -> RIGHT_OUTER_JOIN
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(RIGHT_OUTER_JOIN, "RIGHT_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:9: 'LEFT' ( 'OUTER' )? 'JOIN'
                    {
                    string_literal110=(Token)match(input,83,FOLLOW_83_in_join_type1660); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_83.add(string_literal110);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:16: ( 'OUTER' )?
                    int alt44=2;
                    int LA44_0 = input.LA(1);

                    if ( (LA44_0==81) ) {
                        alt44=1;
                    }
                    switch (alt44) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'OUTER'
                            {
                            string_literal111=(Token)match(input,81,FOLLOW_81_in_join_type1662); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_81.add(string_literal111);


                            }
                            break;

                    }

                    string_literal112=(Token)match(input,82,FOLLOW_82_in_join_type1665); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal112);



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
                    // 193:32: -> LEFT_OUTER_JOIN
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(LEFT_OUTER_JOIN, "LEFT_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:194:9: 'FULL' ( 'OUTER' )? 'JOIN'
                    {
                    string_literal113=(Token)match(input,84,FOLLOW_84_in_join_type1679); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_84.add(string_literal113);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:194:16: ( 'OUTER' )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==81) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'OUTER'
                            {
                            string_literal114=(Token)match(input,81,FOLLOW_81_in_join_type1681); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_81.add(string_literal114);


                            }
                            break;

                    }

                    string_literal115=(Token)match(input,82,FOLLOW_82_in_join_type1684); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal115);



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
                    // 194:32: -> FULL_OUTER_JOIN
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(FULL_OUTER_JOIN, "FULL_OUTER_JOIN"));

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:195:9: ( 'INNER' )? 'JOIN'
                    {
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:195:9: ( 'INNER' )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==85) ) {
                        alt46=1;
                    }
                    switch (alt46) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: 'INNER'
                            {
                            string_literal116=(Token)match(input,85,FOLLOW_85_in_join_type1698); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_85.add(string_literal116);


                            }
                            break;

                    }

                    string_literal117=(Token)match(input,82,FOLLOW_82_in_join_type1701); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_82.add(string_literal117);



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
                    // 195:25: -> JOIN
                    {
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
        return retval;
    }
    // $ANTLR end "join_type"

    public static class table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:197:1: table : non_join_table ( join_type non_join_table 'ON' search_condition )* ;
    public final SQL92QueryParser.table_return table() throws RecognitionException {
        SQL92QueryParser.table_return retval = new SQL92QueryParser.table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal121=null;
        SQL92QueryParser.non_join_table_return non_join_table118 = null;

        SQL92QueryParser.join_type_return join_type119 = null;

        SQL92QueryParser.non_join_table_return non_join_table120 = null;

        SQL92QueryParser.search_condition_return search_condition122 = null;


        CommonTree string_literal121_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:5: ( non_join_table ( join_type non_join_table 'ON' search_condition )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:9: non_join_table ( join_type non_join_table 'ON' search_condition )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_non_join_table_in_table1723);
            non_join_table118=non_join_table();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table118.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:24: ( join_type non_join_table 'ON' search_condition )*
            loop48:
            do {
                int alt48=2;
                int LA48_0 = input.LA(1);

                if ( (LA48_0==80||(LA48_0>=82 && LA48_0<=85)) ) {
                    alt48=1;
                }


                switch (alt48) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:25: join_type non_join_table 'ON' search_condition
            	    {
            	    pushFollow(FOLLOW_join_type_in_table1726);
            	    join_type119=join_type();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(join_type119.getTree(), root_0);
            	    pushFollow(FOLLOW_non_join_table_in_table1729);
            	    non_join_table120=non_join_table();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table120.getTree());
            	    string_literal121=(Token)match(input,86,FOLLOW_86_in_table1731); if (state.failed) return retval;
            	    pushFollow(FOLLOW_search_condition_in_table1734);
            	    search_condition122=search_condition();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition122.getTree());

            	    }
            	    break;

            	default :
            	    break loop48;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "table"

    public static class non_join_table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "non_join_table"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:200:1: non_join_table : ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) );
    public final SQL92QueryParser.non_join_table_return non_join_table() throws RecognitionException {
        SQL92QueryParser.non_join_table_return retval = new SQL92QueryParser.non_join_table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_name_return table_name123 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification124 = null;

        SQL92QueryParser.table_function_return table_function125 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification126 = null;

        SQL92QueryParser.sub_query_return sub_query127 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification128 = null;


        RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
        RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
        RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");
        RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:5: ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
            int alt50=3;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==ID) ) {
                int LA50_1 = input.LA(2);

                if ( (LA50_1==52) ) {
                    alt50=2;
                }
                else if ( (LA50_1==EOF||LA50_1==ID||(LA50_1>=40 && LA50_1<=42)||(LA50_1>=44 && LA50_1<=45)||(LA50_1>=48 && LA50_1<=50)||LA50_1==53||LA50_1==55||(LA50_1>=57 && LA50_1<=58)||LA50_1==80||(LA50_1>=82 && LA50_1<=86)) ) {
                    alt50=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 50, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA50_0==52) ) {
                alt50=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:9: table_name ( correlation_specification )?
                    {
                    pushFollow(FOLLOW_table_name_in_non_join_table1754);
                    table_name123=table_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_name.add(table_name123.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:20: ( correlation_specification )?
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==ID||LA49_0==57) ) {
                        alt49=1;
                    }
                    switch (alt49) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: correlation_specification
                            {
                            pushFollow(FOLLOW_correlation_specification_in_non_join_table1756);
                            correlation_specification124=correlation_specification();

                            state._fsp--;
                            if (state.failed) return retval;
                            if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification124.getTree());

                            }
                            break;

                    }



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
                    // 201:47: -> ^( RELATION table_name ( correlation_specification )? )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:50: ^( RELATION table_name ( correlation_specification )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        adaptor.addChild(root_1, stream_table_name.nextTree());
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:72: ( correlation_specification )?
                        if ( stream_correlation_specification.hasNext() ) {
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
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:9: table_function correlation_specification
                    {
                    pushFollow(FOLLOW_table_function_in_non_join_table1778);
                    table_function125=table_function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function.add(table_function125.getTree());
                    pushFollow(FOLLOW_correlation_specification_in_non_join_table1780);
                    correlation_specification126=correlation_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification126.getTree());


                    // AST REWRITE
                    // elements: correlation_specification, table_function
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 202:50: -> ^( RELATION table_function correlation_specification )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:53: ^( RELATION table_function correlation_specification )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        adaptor.addChild(root_1, stream_table_function.nextTree());
                        adaptor.addChild(root_1, stream_correlation_specification.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:203:9: sub_query correlation_specification
                    {
                    pushFollow(FOLLOW_sub_query_in_non_join_table1800);
                    sub_query127=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_sub_query.add(sub_query127.getTree());
                    pushFollow(FOLLOW_correlation_specification_in_non_join_table1802);
                    correlation_specification128=correlation_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification128.getTree());


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
                    // 203:45: -> ^( RELATION sub_query correlation_specification )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:203:48: ^( RELATION sub_query correlation_specification )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        adaptor.addChild(root_1, stream_sub_query.nextTree());
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
        return retval;
    }
    // $ANTLR end "non_join_table"

    public static class table_function_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:1: table_function : name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) ;
    public final SQL92QueryParser.table_function_return table_function() throws RecognitionException {
        SQL92QueryParser.table_function_return retval = new SQL92QueryParser.table_function_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token name=null;
        Token char_literal129=null;
        Token char_literal131=null;
        Token char_literal133=null;
        Token char_literal135=null;
        SQL92QueryParser.table_function_subquery_return table_function_subquery130 = null;

        SQL92QueryParser.table_function_subquery_return table_function_subquery132 = null;

        SQL92QueryParser.table_function_param_return table_function_param134 = null;


        CommonTree name_tree=null;
        CommonTree char_literal129_tree=null;
        CommonTree char_literal131_tree=null;
        CommonTree char_literal133_tree=null;
        CommonTree char_literal135_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleSubtreeStream stream_table_function_subquery=new RewriteRuleSubtreeStream(adaptor,"rule table_function_subquery");
        RewriteRuleSubtreeStream stream_table_function_param=new RewriteRuleSubtreeStream(adaptor,"rule table_function_param");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:5: (name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:9: name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')'
            {
            name=(Token)match(input,ID,FOLLOW_ID_in_table_function1832); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(name);

            char_literal129=(Token)match(input,52,FOLLOW_52_in_table_function1834); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_52.add(char_literal129);

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:21: ( table_function_subquery )?
            int alt51=2;
            alt51 = dfa51.predict(input);
            switch (alt51) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: table_function_subquery
                    {
                    pushFollow(FOLLOW_table_function_subquery_in_table_function1836);
                    table_function_subquery130=table_function_subquery();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery130.getTree());

                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:46: ( ',' table_function_subquery )*
            loop52:
            do {
                int alt52=2;
                alt52 = dfa52.predict(input);
                switch (alt52) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:47: ',' table_function_subquery
            	    {
            	    char_literal131=(Token)match(input,55,FOLLOW_55_in_table_function1840); if (state.failed) return retval; 
            	    if ( state.backtracking==0 ) stream_55.add(char_literal131);

            	    pushFollow(FOLLOW_table_function_subquery_in_table_function1842);
            	    table_function_subquery132=table_function_subquery();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery132.getTree());

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:77: ( ( ',' )? table_function_param )*
            loop54:
            do {
                int alt54=2;
                int LA54_0 = input.LA(1);

                if ( ((LA54_0>=INT && LA54_0<=STRING)||LA54_0==52||LA54_0==55||(LA54_0>=63 && LA54_0<=74)||(LA54_0>=76 && LA54_0<=78)||LA54_0==89||LA54_0==93||(LA54_0>=104 && LA54_0<=105)) ) {
                    alt54=1;
                }


                switch (alt54) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:78: ( ',' )? table_function_param
            	    {
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:78: ( ',' )?
            	    int alt53=2;
            	    int LA53_0 = input.LA(1);

            	    if ( (LA53_0==55) ) {
            	        alt53=1;
            	    }
            	    switch (alt53) {
            	        case 1 :
            	            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:0:0: ','
            	            {
            	            char_literal133=(Token)match(input,55,FOLLOW_55_in_table_function1847); if (state.failed) return retval; 
            	            if ( state.backtracking==0 ) stream_55.add(char_literal133);


            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_table_function_param_in_table_function1850);
            	    table_function_param134=table_function_param();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) stream_table_function_param.add(table_function_param134.getTree());

            	    }
            	    break;

            	default :
            	    break loop54;
                }
            } while (true);

            char_literal135=(Token)match(input,53,FOLLOW_53_in_table_function1854); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_53.add(char_literal135);



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
            // 207:14: -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:207:17: ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);

                adaptor.addChild(root_1, stream_name.nextNode());
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:207:34: ( table_function_subquery )*
                while ( stream_table_function_subquery.hasNext() ) {
                    adaptor.addChild(root_1, stream_table_function_subquery.nextTree());

                }
                stream_table_function_subquery.reset();
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:207:61: ( table_function_param )*
                while ( stream_table_function_param.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "table_function"

    public static class table_function_subquery_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function_subquery"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:209:1: table_function_subquery : sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) ;
    public final SQL92QueryParser.table_function_subquery_return table_function_subquery() throws RecognitionException {
        SQL92QueryParser.table_function_subquery_return retval = new SQL92QueryParser.table_function_subquery_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.sub_query_return sub_query136 = null;

        SQL92QueryParser.correlation_specification_return correlation_specification137 = null;


        RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
        RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:5: ( sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:9: sub_query correlation_specification
            {
            pushFollow(FOLLOW_sub_query_in_table_function_subquery1902);
            sub_query136=sub_query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_sub_query.add(sub_query136.getTree());
            pushFollow(FOLLOW_correlation_specification_in_table_function_subquery1904);
            correlation_specification137=correlation_specification();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification137.getTree());


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
            // 210:45: -> ^( RELATION sub_query correlation_specification )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:48: ^( RELATION sub_query correlation_specification )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                adaptor.addChild(root_1, stream_sub_query.nextTree());
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
        return retval;
    }
    // $ANTLR end "table_function_subquery"

    public static class table_function_param_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_function_param"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:212:1: table_function_param : ( search_condition | value_expression );
    public final SQL92QueryParser.table_function_param_return table_function_param() throws RecognitionException {
        SQL92QueryParser.table_function_param_return retval = new SQL92QueryParser.table_function_param_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.search_condition_return search_condition138 = null;

        SQL92QueryParser.value_expression_return value_expression139 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:213:5: ( search_condition | value_expression )
            int alt55=2;
            alt55 = dfa55.predict(input);
            switch (alt55) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:213:9: search_condition
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_search_condition_in_table_function_param1932);
                    search_condition138=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition138.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:214:9: value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_expression_in_table_function_param1942);
                    value_expression139=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression139.getTree());

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
        return retval;
    }
    // $ANTLR end "table_function_param"

    public static class relation_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "relation"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:216:1: relation : ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) );
    public final SQL92QueryParser.relation_return relation() throws RecognitionException {
        SQL92QueryParser.relation_return retval = new SQL92QueryParser.relation_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.table_name_return table_name140 = null;

        SQL92QueryParser.table_function_return table_function141 = null;

        SQL92QueryParser.query_return query142 = null;


        RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
        RewriteRuleSubtreeStream stream_query=new RewriteRuleSubtreeStream(adaptor,"rule query");
        RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:5: ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) )
            int alt56=3;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==ID) ) {
                int LA56_1 = input.LA(2);

                if ( (LA56_1==52) ) {
                    alt56=2;
                }
                else if ( (LA56_1==EOF) ) {
                    alt56=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 56, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA56_0==46||LA56_0==52) ) {
                alt56=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:9: table_name
                    {
                    pushFollow(FOLLOW_table_name_in_relation1964);
                    table_name140=table_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_name.add(table_name140.getTree());


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
                    // 217:20: -> ^( RELATION table_name )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:23: ^( RELATION table_name )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        adaptor.addChild(root_1, stream_table_name.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:218:9: table_function
                    {
                    pushFollow(FOLLOW_table_function_in_relation1982);
                    table_function141=table_function();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_table_function.add(table_function141.getTree());


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
                    // 218:24: -> ^( RELATION table_function )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:218:27: ^( RELATION table_function )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

                        adaptor.addChild(root_1, stream_table_function.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:219:9: query
                    {
                    pushFollow(FOLLOW_query_in_relation2000);
                    query142=query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_query.add(query142.getTree());


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
                    // 219:15: -> ^( RELATION query )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:219:18: ^( RELATION query )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);

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
        return retval;
    }
    // $ANTLR end "relation"

    public static class search_condition_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "search_condition"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:221:1: search_condition : boolean_factor ( 'OR' boolean_factor )* ;
    public final SQL92QueryParser.search_condition_return search_condition() throws RecognitionException {
        SQL92QueryParser.search_condition_return retval = new SQL92QueryParser.search_condition_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal144=null;
        SQL92QueryParser.boolean_factor_return boolean_factor143 = null;

        SQL92QueryParser.boolean_factor_return boolean_factor145 = null;


        CommonTree string_literal144_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:5: ( boolean_factor ( 'OR' boolean_factor )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:9: boolean_factor ( 'OR' boolean_factor )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_boolean_factor_in_search_condition2026);
            boolean_factor143=boolean_factor();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor143.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:24: ( 'OR' boolean_factor )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==87) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:25: 'OR' boolean_factor
            	    {
            	    string_literal144=(Token)match(input,87,FOLLOW_87_in_search_condition2029); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal144_tree = (CommonTree)adaptor.create(string_literal144);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal144_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_boolean_factor_in_search_condition2032);
            	    boolean_factor145=boolean_factor();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor145.getTree());

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "search_condition"

    public static class boolean_factor_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_factor"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:224:1: boolean_factor : boolean_term ( 'AND' boolean_term )* ;
    public final SQL92QueryParser.boolean_factor_return boolean_factor() throws RecognitionException {
        SQL92QueryParser.boolean_factor_return retval = new SQL92QueryParser.boolean_factor_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal147=null;
        SQL92QueryParser.boolean_term_return boolean_term146 = null;

        SQL92QueryParser.boolean_term_return boolean_term148 = null;


        CommonTree string_literal147_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:225:5: ( boolean_term ( 'AND' boolean_term )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:225:9: boolean_term ( 'AND' boolean_term )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_boolean_term_in_boolean_factor2052);
            boolean_term146=boolean_term();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term146.getTree());
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:225:22: ( 'AND' boolean_term )*
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==88) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:225:23: 'AND' boolean_term
            	    {
            	    string_literal147=(Token)match(input,88,FOLLOW_88_in_boolean_factor2055); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    string_literal147_tree = (CommonTree)adaptor.create(string_literal147);
            	    root_0 = (CommonTree)adaptor.becomeRoot(string_literal147_tree, root_0);
            	    }
            	    pushFollow(FOLLOW_boolean_term_in_boolean_factor2058);
            	    boolean_term148=boolean_term();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term148.getTree());

            	    }
            	    break;

            	default :
            	    break loop58;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "boolean_factor"

    public static class boolean_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_term"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:227:1: boolean_term : ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) );
    public final SQL92QueryParser.boolean_term_return boolean_term() throws RecognitionException {
        SQL92QueryParser.boolean_term_return retval = new SQL92QueryParser.boolean_term_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal150=null;
        SQL92QueryParser.boolean_test_return boolean_test149 = null;

        SQL92QueryParser.boolean_term_return boolean_term151 = null;


        CommonTree string_literal150_tree=null;
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleSubtreeStream stream_boolean_term=new RewriteRuleSubtreeStream(adaptor,"rule boolean_term");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:228:5: ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) )
            int alt59=2;
            int LA59_0 = input.LA(1);

            if ( ((LA59_0>=INT && LA59_0<=STRING)||LA59_0==52||(LA59_0>=63 && LA59_0<=74)||(LA59_0>=76 && LA59_0<=78)||LA59_0==93||(LA59_0>=104 && LA59_0<=105)) ) {
                alt59=1;
            }
            else if ( (LA59_0==89) ) {
                alt59=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 59, 0, input);

                throw nvae;
            }
            switch (alt59) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:228:9: boolean_test
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_boolean_test_in_boolean_term2078);
                    boolean_test149=boolean_test();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_test149.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:9: 'NOT' boolean_term
                    {
                    string_literal150=(Token)match(input,89,FOLLOW_89_in_boolean_term2088); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(string_literal150);

                    pushFollow(FOLLOW_boolean_term_in_boolean_term2090);
                    boolean_term151=boolean_term();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_boolean_term.add(boolean_term151.getTree());


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
                    // 229:28: -> ^( NOT boolean_term )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:31: ^( NOT boolean_term )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

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
        return retval;
    }
    // $ANTLR end "boolean_term"

    public static class boolean_test_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_test"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:231:1: boolean_test : boolean_primary ;
    public final SQL92QueryParser.boolean_test_return boolean_test() throws RecognitionException {
        SQL92QueryParser.boolean_test_return retval = new SQL92QueryParser.boolean_test_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.boolean_primary_return boolean_primary152 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:232:5: ( boolean_primary )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:232:9: boolean_primary
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_boolean_primary_in_boolean_test2116);
            boolean_primary152=boolean_primary();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary152.getTree());

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
        return retval;
    }
    // $ANTLR end "boolean_test"

    public static class boolean_primary_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "boolean_primary"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:234:1: boolean_primary : ( predicate | '(' search_condition ')' );
    public final SQL92QueryParser.boolean_primary_return boolean_primary() throws RecognitionException {
        SQL92QueryParser.boolean_primary_return retval = new SQL92QueryParser.boolean_primary_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal154=null;
        Token char_literal156=null;
        SQL92QueryParser.predicate_return predicate153 = null;

        SQL92QueryParser.search_condition_return search_condition155 = null;


        CommonTree char_literal154_tree=null;
        CommonTree char_literal156_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:5: ( predicate | '(' search_condition ')' )
            int alt60=2;
            alt60 = dfa60.predict(input);
            switch (alt60) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:9: predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_predicate_in_boolean_primary2134);
                    predicate153=predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, predicate153.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:21: '(' search_condition ')'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    char_literal154=(Token)match(input,52,FOLLOW_52_in_boolean_primary2138); if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_boolean_primary2141);
                    search_condition155=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition155.getTree());
                    char_literal156=(Token)match(input,53,FOLLOW_53_in_boolean_primary2143); if (state.failed) return retval;

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
        return retval;
    }
    // $ANTLR end "boolean_primary"

    public static class predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:237:1: predicate : ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate );
    public final SQL92QueryParser.predicate_return predicate() throws RecognitionException {
        SQL92QueryParser.predicate_return retval = new SQL92QueryParser.predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        SQL92QueryParser.comparison_predicate_return comparison_predicate157 = null;

        SQL92QueryParser.like_predicate_return like_predicate158 = null;

        SQL92QueryParser.in_predicate_return in_predicate159 = null;

        SQL92QueryParser.null_predicate_return null_predicate160 = null;

        SQL92QueryParser.exists_predicate_return exists_predicate161 = null;

        SQL92QueryParser.between_predicate_return between_predicate162 = null;



        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:5: ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate )
            int alt61=6;
            alt61 = dfa61.predict(input);
            switch (alt61) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:9: comparison_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_comparison_predicate_in_predicate2165);
                    comparison_predicate157=comparison_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_predicate157.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:32: like_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_like_predicate_in_predicate2169);
                    like_predicate158=like_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, like_predicate158.getTree());

                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:49: in_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_in_predicate_in_predicate2173);
                    in_predicate159=in_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, in_predicate159.getTree());

                    }
                    break;
                case 4 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:64: null_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_null_predicate_in_predicate2177);
                    null_predicate160=null_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, null_predicate160.getTree());

                    }
                    break;
                case 5 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:81: exists_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_exists_predicate_in_predicate2181);
                    exists_predicate161=exists_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_predicate161.getTree());

                    }
                    break;
                case 6 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:100: between_predicate
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_between_predicate_in_predicate2185);
                    between_predicate162=between_predicate();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, between_predicate162.getTree());

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
        return retval;
    }
    // $ANTLR end "predicate"

    public static class null_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "null_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:240:1: null_predicate : ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) );
    public final SQL92QueryParser.null_predicate_return null_predicate() throws RecognitionException {
        SQL92QueryParser.null_predicate_return retval = new SQL92QueryParser.null_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal164=null;
        Token string_literal165=null;
        Token string_literal167=null;
        Token string_literal168=null;
        Token string_literal169=null;
        SQL92QueryParser.row_value_return row_value163 = null;

        SQL92QueryParser.row_value_return row_value166 = null;


        CommonTree string_literal164_tree=null;
        CommonTree string_literal165_tree=null;
        CommonTree string_literal167_tree=null;
        CommonTree string_literal168_tree=null;
        CommonTree string_literal169_tree=null;
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
        RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:5: ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) )
            int alt62=2;
            alt62 = dfa62.predict(input);
            switch (alt62) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:9: row_value 'IS' 'NULL'
                    {
                    pushFollow(FOLLOW_row_value_in_null_predicate2203);
                    row_value163=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value163.getTree());
                    string_literal164=(Token)match(input,90,FOLLOW_90_in_null_predicate2205); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_90.add(string_literal164);

                    string_literal165=(Token)match(input,76,FOLLOW_76_in_null_predicate2207); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal165);



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
                    // 241:31: -> ^( IS_NULL row_value )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:34: ^( IS_NULL row_value )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_1);

                        adaptor.addChild(root_1, stream_row_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:9: row_value 'IS' 'NOT' 'NULL'
                    {
                    pushFollow(FOLLOW_row_value_in_null_predicate2225);
                    row_value166=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value166.getTree());
                    string_literal167=(Token)match(input,90,FOLLOW_90_in_null_predicate2227); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_90.add(string_literal167);

                    string_literal168=(Token)match(input,89,FOLLOW_89_in_null_predicate2229); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(string_literal168);

                    string_literal169=(Token)match(input,76,FOLLOW_76_in_null_predicate2231); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_76.add(string_literal169);



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
                    // 242:37: -> ^( NOT ^( IS_NULL row_value ) )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:40: ^( NOT ^( IS_NULL row_value ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:46: ^( IS_NULL row_value )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_2);

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
        return retval;
    }
    // $ANTLR end "null_predicate"

    public static class in_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:244:1: in_predicate : ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) );
    public final SQL92QueryParser.in_predicate_return in_predicate() throws RecognitionException {
        SQL92QueryParser.in_predicate_return retval = new SQL92QueryParser.in_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal171=null;
        Token string_literal172=null;
        Token string_literal175=null;
        SQL92QueryParser.row_value_return row_value170 = null;

        SQL92QueryParser.in_predicate_tail_return in_predicate_tail173 = null;

        SQL92QueryParser.row_value_return row_value174 = null;

        SQL92QueryParser.in_predicate_tail_return in_predicate_tail176 = null;


        CommonTree string_literal171_tree=null;
        CommonTree string_literal172_tree=null;
        CommonTree string_literal175_tree=null;
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        RewriteRuleSubtreeStream stream_in_predicate_tail=new RewriteRuleSubtreeStream(adaptor,"rule in_predicate_tail");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:5: ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) )
            int alt63=2;
            alt63 = dfa63.predict(input);
            switch (alt63) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: row_value 'NOT' 'IN' in_predicate_tail
                    {
                    pushFollow(FOLLOW_row_value_in_in_predicate2261);
                    row_value170=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value170.getTree());
                    string_literal171=(Token)match(input,89,FOLLOW_89_in_in_predicate2263); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(string_literal171);

                    string_literal172=(Token)match(input,91,FOLLOW_91_in_in_predicate2265); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_91.add(string_literal172);

                    pushFollow(FOLLOW_in_predicate_tail_in_in_predicate2267);
                    in_predicate_tail173=in_predicate_tail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail173.getTree());


                    // AST REWRITE
                    // elements: in_predicate_tail, 91, row_value
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 246:13: -> ^( NOT ^( 'IN' row_value in_predicate_tail ) )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:246:16: ^( NOT ^( 'IN' row_value in_predicate_tail ) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:246:22: ^( 'IN' row_value in_predicate_tail )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_91.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_row_value.nextTree());
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
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:247:9: row_value 'IN' in_predicate_tail
                    {
                    pushFollow(FOLLOW_row_value_in_in_predicate2303);
                    row_value174=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(row_value174.getTree());
                    string_literal175=(Token)match(input,91,FOLLOW_91_in_in_predicate2305); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_91.add(string_literal175);

                    pushFollow(FOLLOW_in_predicate_tail_in_in_predicate2307);
                    in_predicate_tail176=in_predicate_tail();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail176.getTree());


                    // AST REWRITE
                    // elements: row_value, 91, in_predicate_tail
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 248:13: -> ^( 'IN' row_value in_predicate_tail )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:248:16: ^( 'IN' row_value in_predicate_tail )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_91.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_row_value.nextTree());
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
        return retval;
    }
    // $ANTLR end "in_predicate"

    public static class in_predicate_tail_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "in_predicate_tail"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:250:1: in_predicate_tail : ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) );
    public final SQL92QueryParser.in_predicate_tail_return in_predicate_tail() throws RecognitionException {
        SQL92QueryParser.in_predicate_tail_return retval = new SQL92QueryParser.in_predicate_tail_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal178=null;
        Token char_literal180=null;
        Token char_literal182=null;
        SQL92QueryParser.sub_query_return sub_query177 = null;

        SQL92QueryParser.value_expression_return value_expression179 = null;

        SQL92QueryParser.value_expression_return value_expression181 = null;


        CommonTree char_literal178_tree=null;
        CommonTree char_literal180_tree=null;
        CommonTree char_literal182_tree=null;
        RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
        RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
        RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
        RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:251:5: ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) )
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( (LA65_0==52) ) {
                int LA65_1 = input.LA(2);

                if ( (synpred111_SQL92Query()) ) {
                    alt65=1;
                }
                else if ( (true) ) {
                    alt65=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 65, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:251:9: sub_query
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_sub_query_in_in_predicate_tail2347);
                    sub_query177=sub_query();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query177.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:9: '(' ( value_expression ( ',' value_expression )* ) ')'
                    {
                    char_literal178=(Token)match(input,52,FOLLOW_52_in_in_predicate_tail2358); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_52.add(char_literal178);

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:13: ( value_expression ( ',' value_expression )* )
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:14: value_expression ( ',' value_expression )*
                    {
                    pushFollow(FOLLOW_value_expression_in_in_predicate_tail2361);
                    value_expression179=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_value_expression.add(value_expression179.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:31: ( ',' value_expression )*
                    loop64:
                    do {
                        int alt64=2;
                        int LA64_0 = input.LA(1);

                        if ( (LA64_0==55) ) {
                            alt64=1;
                        }


                        switch (alt64) {
                    	case 1 :
                    	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:32: ',' value_expression
                    	    {
                    	    char_literal180=(Token)match(input,55,FOLLOW_55_in_in_predicate_tail2364); if (state.failed) return retval; 
                    	    if ( state.backtracking==0 ) stream_55.add(char_literal180);

                    	    pushFollow(FOLLOW_value_expression_in_in_predicate_tail2366);
                    	    value_expression181=value_expression();

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) stream_value_expression.add(value_expression181.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop64;
                        }
                    } while (true);


                    }

                    char_literal182=(Token)match(input,53,FOLLOW_53_in_in_predicate_tail2371); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_53.add(char_literal182);



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
                    // 252:60: -> ^( SET ( value_expression )* )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:63: ^( SET ( value_expression )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SET, "SET"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:69: ( value_expression )*
                        while ( stream_value_expression.hasNext() ) {
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
        return retval;
    }
    // $ANTLR end "in_predicate_tail"

    public static class between_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "between_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:254:1: between_predicate : (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) );
    public final SQL92QueryParser.between_predicate_return between_predicate() throws RecognitionException {
        SQL92QueryParser.between_predicate_return retval = new SQL92QueryParser.between_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal183=null;
        Token string_literal184=null;
        Token string_literal185=null;
        Token string_literal186=null;
        Token string_literal187=null;
        SQL92QueryParser.row_value_return value = null;

        SQL92QueryParser.row_value_return btw1 = null;

        SQL92QueryParser.row_value_return btw2 = null;


        CommonTree string_literal183_tree=null;
        CommonTree string_literal184_tree=null;
        CommonTree string_literal185_tree=null;
        CommonTree string_literal186_tree=null;
        CommonTree string_literal187_tree=null;
        RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:5: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) )
            int alt66=2;
            alt66 = dfa66.predict(input);
            switch (alt66) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
                    {
                    pushFollow(FOLLOW_row_value_in_between_predicate2400);
                    value=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
                    string_literal183=(Token)match(input,92,FOLLOW_92_in_between_predicate2402); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_92.add(string_literal183);

                    pushFollow(FOLLOW_row_value_in_between_predicate2406);
                    btw1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
                    string_literal184=(Token)match(input,88,FOLLOW_88_in_between_predicate2408); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_88.add(string_literal184);

                    pushFollow(FOLLOW_row_value_in_between_predicate2412);
                    btw2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());


                    // AST REWRITE
                    // elements: btw1, value, 92, btw2
                    // token labels: 
                    // rule labels: btw1, btw2, value, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.tree:null);
                    RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.tree:null);
                    RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 256:13: -> ^( 'BETWEEN' $value $btw1 $btw2)
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:16: ^( 'BETWEEN' $value $btw1 $btw2)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_92.nextNode(), root_1);

                        adaptor.addChild(root_1, stream_value.nextTree());
                        adaptor.addChild(root_1, stream_btw1.nextTree());
                        adaptor.addChild(root_1, stream_btw2.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;}
                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:257:9: value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value
                    {
                    pushFollow(FOLLOW_row_value_in_between_predicate2452);
                    value=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
                    string_literal185=(Token)match(input,89,FOLLOW_89_in_between_predicate2454); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(string_literal185);

                    string_literal186=(Token)match(input,92,FOLLOW_92_in_between_predicate2456); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_92.add(string_literal186);

                    pushFollow(FOLLOW_row_value_in_between_predicate2460);
                    btw1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
                    string_literal187=(Token)match(input,88,FOLLOW_88_in_between_predicate2462); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_88.add(string_literal187);

                    pushFollow(FOLLOW_row_value_in_between_predicate2466);
                    btw2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());


                    // AST REWRITE
                    // elements: value, btw1, btw2, 92
                    // token labels: 
                    // rule labels: btw1, btw2, value, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.tree:null);
                    RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.tree:null);
                    RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 258:13: -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:258:16: ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:258:22: ^( 'BETWEEN' $value $btw1 $btw2)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_92.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_value.nextTree());
                        adaptor.addChild(root_2, stream_btw1.nextTree());
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
        return retval;
    }
    // $ANTLR end "between_predicate"

    public static class exists_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "exists_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:260:1: exists_predicate : 'EXISTS' sub_query ;
    public final SQL92QueryParser.exists_predicate_return exists_predicate() throws RecognitionException {
        SQL92QueryParser.exists_predicate_return retval = new SQL92QueryParser.exists_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal188=null;
        SQL92QueryParser.sub_query_return sub_query189 = null;


        CommonTree string_literal188_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:261:5: ( 'EXISTS' sub_query )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:261:9: 'EXISTS' sub_query
            {
            root_0 = (CommonTree)adaptor.nil();

            string_literal188=(Token)match(input,93,FOLLOW_93_in_exists_predicate2515); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            string_literal188_tree = (CommonTree)adaptor.create(string_literal188);
            root_0 = (CommonTree)adaptor.becomeRoot(string_literal188_tree, root_0);
            }
            pushFollow(FOLLOW_sub_query_in_exists_predicate2518);
            sub_query189=sub_query();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query189.getTree());

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
        return retval;
    }
    // $ANTLR end "exists_predicate"

    public static class comparison_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "comparison_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:263:1: comparison_predicate : ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value );
    public final SQL92QueryParser.comparison_predicate_return comparison_predicate() throws RecognitionException {
        SQL92QueryParser.comparison_predicate_return retval = new SQL92QueryParser.comparison_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token op=null;
        Token ep=null;
        Token char_literal191=null;
        Token set194=null;
        SQL92QueryParser.row_value_return lv = null;

        SQL92QueryParser.row_value_return rv = null;

        SQL92QueryParser.bind_table_return bind_table190 = null;

        SQL92QueryParser.row_value_return row_value192 = null;

        SQL92QueryParser.row_value_return row_value193 = null;

        SQL92QueryParser.row_value_return row_value195 = null;


        CommonTree op_tree=null;
        CommonTree ep_tree=null;
        CommonTree char_literal191_tree=null;
        CommonTree set194_tree=null;
        RewriteRuleTokenStream stream_99=new RewriteRuleTokenStream(adaptor,"token 99");
        RewriteRuleTokenStream stream_100=new RewriteRuleTokenStream(adaptor,"token 100");
        RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
        RewriteRuleTokenStream stream_102=new RewriteRuleTokenStream(adaptor,"token 102");
        RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
        RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
        RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
        RewriteRuleTokenStream stream_97=new RewriteRuleTokenStream(adaptor,"token 97");
        RewriteRuleTokenStream stream_98=new RewriteRuleTokenStream(adaptor,"token 98");
        RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:264:5: ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value )
            int alt69=3;
            alt69 = dfa69.predict(input);
            switch (alt69) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:264:9: bind_table '=' row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_bind_table_in_comparison_predicate2536);
                    bind_table190=bind_table();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, bind_table190.getTree());
                    char_literal191=(Token)match(input,94,FOLLOW_94_in_comparison_predicate2538); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    char_literal191_tree = (CommonTree)adaptor.create(char_literal191);
                    root_0 = (CommonTree)adaptor.becomeRoot(char_literal191_tree, root_0);
                    }
                    pushFollow(FOLLOW_row_value_in_comparison_predicate2541);
                    row_value192=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value192.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:9: lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value
                    {
                    pushFollow(FOLLOW_row_value_in_comparison_predicate2553);
                    lv=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(lv.getTree());
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:22: (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' )
                    int alt67=7;
                    switch ( input.LA(1) ) {
                    case 94:
                        {
                        alt67=1;
                        }
                        break;
                    case 95:
                        {
                        alt67=2;
                        }
                        break;
                    case 96:
                        {
                        alt67=3;
                        }
                        break;
                    case 97:
                        {
                        alt67=4;
                        }
                        break;
                    case 98:
                        {
                        alt67=5;
                        }
                        break;
                    case 99:
                        {
                        alt67=6;
                        }
                        break;
                    case 100:
                        {
                        alt67=7;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 67, 0, input);

                        throw nvae;
                    }

                    switch (alt67) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:23: op= '='
                            {
                            op=(Token)match(input,94,FOLLOW_94_in_comparison_predicate2558); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_94.add(op);


                            }
                            break;
                        case 2 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:30: op= '<>'
                            {
                            op=(Token)match(input,95,FOLLOW_95_in_comparison_predicate2562); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_95.add(op);


                            }
                            break;
                        case 3 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:38: op= '!='
                            {
                            op=(Token)match(input,96,FOLLOW_96_in_comparison_predicate2566); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_96.add(op);


                            }
                            break;
                        case 4 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:46: op= '<'
                            {
                            op=(Token)match(input,97,FOLLOW_97_in_comparison_predicate2570); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_97.add(op);


                            }
                            break;
                        case 5 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:53: op= '>'
                            {
                            op=(Token)match(input,98,FOLLOW_98_in_comparison_predicate2574); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_98.add(op);


                            }
                            break;
                        case 6 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:60: op= '>='
                            {
                            op=(Token)match(input,99,FOLLOW_99_in_comparison_predicate2578); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_99.add(op);


                            }
                            break;
                        case 7 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:68: op= '<='
                            {
                            op=(Token)match(input,100,FOLLOW_100_in_comparison_predicate2582); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_100.add(op);


                            }
                            break;

                    }

                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:77: (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' )
                    int alt68=3;
                    switch ( input.LA(1) ) {
                    case 43:
                        {
                        alt68=1;
                        }
                        break;
                    case 101:
                        {
                        alt68=2;
                        }
                        break;
                    case 102:
                        {
                        alt68=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 68, 0, input);

                        throw nvae;
                    }

                    switch (alt68) {
                        case 1 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:78: ep= 'ALL'
                            {
                            ep=(Token)match(input,43,FOLLOW_43_in_comparison_predicate2588); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_43.add(ep);


                            }
                            break;
                        case 2 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:87: ep= 'SOME'
                            {
                            ep=(Token)match(input,101,FOLLOW_101_in_comparison_predicate2592); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_101.add(ep);


                            }
                            break;
                        case 3 :
                            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:97: ep= 'ANY'
                            {
                            ep=(Token)match(input,102,FOLLOW_102_in_comparison_predicate2596); if (state.failed) return retval; 
                            if ( state.backtracking==0 ) stream_102.add(ep);


                            }
                            break;

                    }

                    pushFollow(FOLLOW_row_value_in_comparison_predicate2601);
                    rv=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(rv.getTree());


                    // AST REWRITE
                    // elements: lv, ep, rv, op
                    // token labels: op, ep
                    // rule labels: rv, lv, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
                    RewriteRuleTokenStream stream_ep=new RewriteRuleTokenStream(adaptor,"token ep",ep);
                    RewriteRuleSubtreeStream stream_rv=new RewriteRuleSubtreeStream(adaptor,"rule rv",rv!=null?rv.tree:null);
                    RewriteRuleSubtreeStream stream_lv=new RewriteRuleSubtreeStream(adaptor,"rule lv",lv!=null?lv.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 266:13: -> ^( $ep ^( $op $lv $rv) )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:266:16: ^( $ep ^( $op $lv $rv) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(stream_ep.nextNode(), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:266:22: ^( $op $lv $rv)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_op.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_lv.nextTree());
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
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:267:9: row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_row_value_in_comparison_predicate2641);
                    row_value193=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value193.getTree());
                    set194=(Token)input.LT(1);
                    set194=(Token)input.LT(1);
                    if ( (input.LA(1)>=94 && input.LA(1)<=100) ) {
                        input.consume();
                        if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set194), root_0);
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_row_value_in_comparison_predicate2672);
                    row_value195=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value195.getTree());

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
        return retval;
    }
    // $ANTLR end "comparison_predicate"

    public static class like_predicate_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "like_predicate"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:1: like_predicate : ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) );
    public final SQL92QueryParser.like_predicate_return like_predicate() throws RecognitionException {
        SQL92QueryParser.like_predicate_return retval = new SQL92QueryParser.like_predicate_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal197=null;
        Token string_literal199=null;
        Token string_literal200=null;
        SQL92QueryParser.row_value_return v1 = null;

        SQL92QueryParser.row_value_return v2 = null;

        SQL92QueryParser.row_value_return row_value196 = null;

        SQL92QueryParser.row_value_return row_value198 = null;


        CommonTree string_literal197_tree=null;
        CommonTree string_literal199_tree=null;
        CommonTree string_literal200_tree=null;
        RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
        RewriteRuleTokenStream stream_103=new RewriteRuleTokenStream(adaptor,"token 103");
        RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:5: ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) )
            int alt70=2;
            alt70 = dfa70.predict(input);
            switch (alt70) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:9: row_value 'LIKE' row_value
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_row_value_in_like_predicate2690);
                    row_value196=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value196.getTree());
                    string_literal197=(Token)match(input,103,FOLLOW_103_in_like_predicate2692); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal197_tree = (CommonTree)adaptor.create(string_literal197);
                    root_0 = (CommonTree)adaptor.becomeRoot(string_literal197_tree, root_0);
                    }
                    pushFollow(FOLLOW_row_value_in_like_predicate2695);
                    row_value198=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value198.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:271:9: v1= row_value 'NOT' 'LIKE' v2= row_value
                    {
                    pushFollow(FOLLOW_row_value_in_like_predicate2707);
                    v1=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(v1.getTree());
                    string_literal199=(Token)match(input,89,FOLLOW_89_in_like_predicate2709); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_89.add(string_literal199);

                    string_literal200=(Token)match(input,103,FOLLOW_103_in_like_predicate2711); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_103.add(string_literal200);

                    pushFollow(FOLLOW_row_value_in_like_predicate2715);
                    v2=row_value();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) stream_row_value.add(v2.getTree());


                    // AST REWRITE
                    // elements: v1, 103, v2
                    // token labels: 
                    // rule labels: v1, v2, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    if ( state.backtracking==0 ) {
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_v1=new RewriteRuleSubtreeStream(adaptor,"rule v1",v1!=null?v1.tree:null);
                    RewriteRuleSubtreeStream stream_v2=new RewriteRuleSubtreeStream(adaptor,"rule v2",v2!=null?v2.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 271:48: -> ^( NOT ^( 'LIKE' $v1 $v2) )
                    {
                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:271:51: ^( NOT ^( 'LIKE' $v1 $v2) )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);

                        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:271:57: ^( 'LIKE' $v1 $v2)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(stream_103.nextNode(), root_2);

                        adaptor.addChild(root_2, stream_v1.nextTree());
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
        return retval;
    }
    // $ANTLR end "like_predicate"

    public static class row_value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "row_value"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:273:1: row_value : ( value_expression | 'NULL' | 'DEFAULT' );
    public final SQL92QueryParser.row_value_return row_value() throws RecognitionException {
        SQL92QueryParser.row_value_return retval = new SQL92QueryParser.row_value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal202=null;
        Token string_literal203=null;
        SQL92QueryParser.value_expression_return value_expression201 = null;


        CommonTree string_literal202_tree=null;
        CommonTree string_literal203_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:5: ( value_expression | 'NULL' | 'DEFAULT' )
            int alt71=3;
            switch ( input.LA(1) ) {
            case INT:
            case ID:
            case FLOAT:
            case NUMERIC:
            case STRING:
            case 52:
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
            case 73:
            case 74:
            case 77:
            case 78:
                {
                alt71=1;
                }
                break;
            case 76:
                {
                int LA71_2 = input.LA(2);

                if ( (synpred131_SQL92Query()) ) {
                    alt71=1;
                }
                else if ( (synpred132_SQL92Query()) ) {
                    alt71=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 71, 2, input);

                    throw nvae;
                }
                }
                break;
            case 104:
                {
                alt71=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 71, 0, input);

                throw nvae;
            }

            switch (alt71) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: value_expression
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_value_expression_in_row_value2749);
                    value_expression201=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression201.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:27: 'NULL'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal202=(Token)match(input,76,FOLLOW_76_in_row_value2752); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal202_tree = (CommonTree)adaptor.create(string_literal202);
                    adaptor.addChild(root_0, string_literal202_tree);
                    }

                    }
                    break;
                case 3 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:36: 'DEFAULT'
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    string_literal203=(Token)match(input,104,FOLLOW_104_in_row_value2756); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    string_literal203_tree = (CommonTree)adaptor.create(string_literal203);
                    adaptor.addChild(root_0, string_literal203_tree);
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
        return retval;
    }
    // $ANTLR end "row_value"

    public static class bind_table_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bind_table"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:276:1: bind_table : '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) ;
    public final SQL92QueryParser.bind_table_return bind_table() throws RecognitionException {
        SQL92QueryParser.bind_table_return retval = new SQL92QueryParser.bind_table_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token columnid=null;
        Token char_literal204=null;
        Token char_literal205=null;

        CommonTree tableid_tree=null;
        CommonTree columnid_tree=null;
        CommonTree char_literal204_tree=null;
        CommonTree char_literal205_tree=null;
        RewriteRuleTokenStream stream_105=new RewriteRuleTokenStream(adaptor,"token 105");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:277:5: ( '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:277:9: '@' tableid= ID '.' columnid= ID
            {
            char_literal204=(Token)match(input,105,FOLLOW_105_in_bind_table2774); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_105.add(char_literal204);

            tableid=(Token)match(input,ID,FOLLOW_ID_in_bind_table2777); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(tableid);

            char_literal205=(Token)match(input,62,FOLLOW_62_in_bind_table2778); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_62.add(char_literal205);

            columnid=(Token)match(input,ID,FOLLOW_ID_in_bind_table2781); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(columnid);



            // AST REWRITE
            // elements: tableid, columnid
            // token labels: columnid, tableid
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_columnid=new RewriteRuleTokenStream(adaptor,"token columnid",columnid);
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 277:37: -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:277:40: ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOUND, "BOUND"), root_1);

                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:277:48: ^( TABLECOLUMN $tableid $columnid)
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_2);

                adaptor.addChild(root_2, stream_tableid.nextNode());
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
        return retval;
    }
    // $ANTLR end "bind_table"

    public static class correlation_specification_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "correlation_specification"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:279:1: correlation_specification : ( 'AS' )? ID ;
    public final SQL92QueryParser.correlation_specification_return correlation_specification() throws RecognitionException {
        SQL92QueryParser.correlation_specification_return retval = new SQL92QueryParser.correlation_specification_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal206=null;
        Token ID207=null;

        CommonTree string_literal206_tree=null;
        CommonTree ID207_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:280:5: ( ( 'AS' )? ID )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:280:9: ( 'AS' )? ID
            {
            root_0 = (CommonTree)adaptor.nil();

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:280:9: ( 'AS' )?
            int alt72=2;
            int LA72_0 = input.LA(1);

            if ( (LA72_0==57) ) {
                alt72=1;
            }
            switch (alt72) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:280:10: 'AS'
                    {
                    string_literal206=(Token)match(input,57,FOLLOW_57_in_correlation_specification2816); if (state.failed) return retval;

                    }
                    break;

            }

            ID207=(Token)match(input,ID,FOLLOW_ID_in_correlation_specification2821); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID207_tree = (CommonTree)adaptor.create(ID207);
            adaptor.addChild(root_0, ID207_tree);
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
        return retval;
    }
    // $ANTLR end "correlation_specification"

    public static class table_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "table_name"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:282:1: table_name : ID ;
    public final SQL92QueryParser.table_name_return table_name() throws RecognitionException {
        SQL92QueryParser.table_name_return retval = new SQL92QueryParser.table_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token ID208=null;

        CommonTree ID208_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:283:5: ( ID )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:283:9: ID
            {
            root_0 = (CommonTree)adaptor.nil();

            ID208=(Token)match(input,ID,FOLLOW_ID_in_table_name2842); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID208_tree = (CommonTree)adaptor.create(ID208);
            adaptor.addChild(root_0, ID208_tree);
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
        return retval;
    }
    // $ANTLR end "table_name"

    public static class column_list_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "column_list"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:285:1: column_list : ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )* ;
    public final SQL92QueryParser.column_list_return column_list() throws RecognitionException {
        SQL92QueryParser.column_list_return retval = new SQL92QueryParser.column_list_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal211=null;
        SQL92QueryParser.column_name_return column_name209 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name210 = null;

        SQL92QueryParser.column_name_return column_name212 = null;

        SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name213 = null;


        CommonTree char_literal211_tree=null;

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:5: ( ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )* )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:9: ( column_name | reserved_word_column_name ) ( ',' ( column_name | reserved_word_column_name ) )*
            {
            root_0 = (CommonTree)adaptor.nil();

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:9: ( column_name | reserved_word_column_name )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==ID) ) {
                int LA73_1 = input.LA(2);

                if ( (LA73_1==62) ) {
                    int LA73_3 = input.LA(3);

                    if ( (LA73_3==ID) ) {
                        alt73=1;
                    }
                    else if ( ((LA73_3>=63 && LA73_3<=72)) ) {
                        alt73=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 73, 3, input);

                        throw nvae;
                    }
                }
                else if ( (LA73_1==EOF||(LA73_1>=40 && LA73_1<=42)||(LA73_1>=44 && LA73_1<=45)||LA73_1==50||LA73_1==53||LA73_1==55||LA73_1==58) ) {
                    alt73=1;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 1, input);

                    throw nvae;
                }
            }
            else if ( ((LA73_0>=63 && LA73_0<=72)) ) {
                alt73=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }
            switch (alt73) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:10: column_name
                    {
                    pushFollow(FOLLOW_column_name_in_column_list2861);
                    column_name209=column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name209.getTree());

                    }
                    break;
                case 2 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:24: reserved_word_column_name
                    {
                    pushFollow(FOLLOW_reserved_word_column_name_in_column_list2865);
                    reserved_word_column_name210=reserved_word_column_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name210.getTree());

                    }
                    break;

            }

            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:51: ( ',' ( column_name | reserved_word_column_name ) )*
            loop75:
            do {
                int alt75=2;
                int LA75_0 = input.LA(1);

                if ( (LA75_0==55) ) {
                    alt75=1;
                }


                switch (alt75) {
            	case 1 :
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:52: ',' ( column_name | reserved_word_column_name )
            	    {
            	    char_literal211=(Token)match(input,55,FOLLOW_55_in_column_list2869); if (state.failed) return retval;
            	    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:57: ( column_name | reserved_word_column_name )
            	    int alt74=2;
            	    int LA74_0 = input.LA(1);

            	    if ( (LA74_0==ID) ) {
            	        int LA74_1 = input.LA(2);

            	        if ( (LA74_1==62) ) {
            	            int LA74_3 = input.LA(3);

            	            if ( (LA74_3==ID) ) {
            	                alt74=1;
            	            }
            	            else if ( ((LA74_3>=63 && LA74_3<=72)) ) {
            	                alt74=2;
            	            }
            	            else {
            	                if (state.backtracking>0) {state.failed=true; return retval;}
            	                NoViableAltException nvae =
            	                    new NoViableAltException("", 74, 3, input);

            	                throw nvae;
            	            }
            	        }
            	        else if ( (LA74_1==EOF||(LA74_1>=40 && LA74_1<=42)||(LA74_1>=44 && LA74_1<=45)||LA74_1==50||LA74_1==53||LA74_1==55||LA74_1==58) ) {
            	            alt74=1;
            	        }
            	        else {
            	            if (state.backtracking>0) {state.failed=true; return retval;}
            	            NoViableAltException nvae =
            	                new NoViableAltException("", 74, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else if ( ((LA74_0>=63 && LA74_0<=72)) ) {
            	        alt74=2;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return retval;}
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 74, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt74) {
            	        case 1 :
            	            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:58: column_name
            	            {
            	            pushFollow(FOLLOW_column_name_in_column_list2873);
            	            column_name212=column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name212.getTree());

            	            }
            	            break;
            	        case 2 :
            	            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:72: reserved_word_column_name
            	            {
            	            pushFollow(FOLLOW_reserved_word_column_name_in_column_list2877);
            	            reserved_word_column_name213=reserved_word_column_name();

            	            state._fsp--;
            	            if (state.failed) return retval;
            	            if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name213.getTree());

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop75;
                }
            } while (true);


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
        return retval;
    }
    // $ANTLR end "column_list"

    public static class column_name_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "column_name"
    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:288:1: column_name : (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) ;
    public final SQL92QueryParser.column_name_return column_name() throws RecognitionException {
        SQL92QueryParser.column_name_return retval = new SQL92QueryParser.column_name_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token tableid=null;
        Token columnid=null;
        Token char_literal214=null;

        CommonTree tableid_tree=null;
        CommonTree columnid_tree=null;
        CommonTree char_literal214_tree=null;
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");

        try {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:5: ( (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) )
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:9: (tableid= ID '.' )? columnid= ID
            {
            // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:9: (tableid= ID '.' )?
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==ID) ) {
                int LA76_1 = input.LA(2);

                if ( (LA76_1==62) ) {
                    alt76=1;
                }
            }
            switch (alt76) {
                case 1 :
                    // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:10: tableid= ID '.'
                    {
                    tableid=(Token)match(input,ID,FOLLOW_ID_in_column_name2901); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_ID.add(tableid);

                    char_literal214=(Token)match(input,62,FOLLOW_62_in_column_name2902); if (state.failed) return retval; 
                    if ( state.backtracking==0 ) stream_62.add(char_literal214);


                    }
                    break;

            }

            columnid=(Token)match(input,ID,FOLLOW_ID_in_column_name2907); if (state.failed) return retval; 
            if ( state.backtracking==0 ) stream_ID.add(columnid);



            // AST REWRITE
            // elements: tableid, columnid
            // token labels: columnid, tableid
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            if ( state.backtracking==0 ) {
            retval.tree = root_0;
            RewriteRuleTokenStream stream_columnid=new RewriteRuleTokenStream(adaptor,"token columnid",columnid);
            RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 289:37: -> ^( TABLECOLUMN ( $tableid)? $columnid)
            {
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:40: ^( TABLECOLUMN ( $tableid)? $columnid)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);

                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:54: ( $tableid)?
                if ( stream_tableid.hasNext() ) {
                    adaptor.addChild(root_1, stream_tableid.nextNode());

                }
                stream_tableid.reset();
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
        return retval;
    }
    // $ANTLR end "column_name"

    // $ANTLR start synpred39_SQL92Query
    public final void synpred39_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:17: ( ( '+' | '-' ) factor )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:17: ( '+' | '-' ) factor
        {
        if ( (input.LA(1)>=73 && input.LA(1)<=74) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        pushFollow(FOLLOW_factor_in_synpred39_SQL92Query1076);
        factor();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred39_SQL92Query

    // $ANTLR start synpred44_SQL92Query
    public final void synpred44_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:160:9: ( '(' value_expression ')' )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:160:9: '(' value_expression ')'
        {
        match(input,52,FOLLOW_52_in_synpred44_SQL92Query1157); if (state.failed) return ;
        pushFollow(FOLLOW_value_expression_in_synpred44_SQL92Query1160);
        value_expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,53,FOLLOW_53_in_synpred44_SQL92Query1162); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred44_SQL92Query

    // $ANTLR start synpred45_SQL92Query
    public final void synpred45_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:9: ( function )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:9: function
        {
        pushFollow(FOLLOW_function_in_synpred45_SQL92Query1173);
        function();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred45_SQL92Query

    // $ANTLR start synpred46_SQL92Query
    public final void synpred46_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:162:9: ( column_name )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:162:9: column_name
        {
        pushFollow(FOLLOW_column_name_in_synpred46_SQL92Query1183);
        column_name();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred46_SQL92Query

    // $ANTLR start synpred47_SQL92Query
    public final void synpred47_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:163:9: ( literal )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:163:9: literal
        {
        pushFollow(FOLLOW_literal_in_synpred47_SQL92Query1193);
        literal();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred47_SQL92Query

    // $ANTLR start synpred58_SQL92Query
    public final void synpred58_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:170:9: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:170:9: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING
        {
        if ( (input.LA(1)>=63 && input.LA(1)<=65) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        match(input,STRING,FOLLOW_STRING_in_synpred58_SQL92Query1284); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred58_SQL92Query

    // $ANTLR start synpred67_SQL92Query
    public final void synpred67_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
        {
        match(input,66,FOLLOW_66_in_synpred67_SQL92Query1348); if (state.failed) return ;
        match(input,STRING,FOLLOW_STRING_in_synpred67_SQL92Query1351); if (state.failed) return ;
        if ( (input.LA(1)>=67 && input.LA(1)<=72) ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        }
    }
    // $ANTLR end synpred67_SQL92Query

    // $ANTLR start synpred81_SQL92Query
    public final void synpred81_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:16: ( ',' table_reference )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:16: ',' table_reference
        {
        match(input,55,FOLLOW_55_in_synpred81_SQL92Query1617); if (state.failed) return ;
        pushFollow(FOLLOW_table_reference_in_synpred81_SQL92Query1620);
        table_reference();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred81_SQL92Query

    // $ANTLR start synpred93_SQL92Query
    public final void synpred93_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:21: ( table_function_subquery )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:21: table_function_subquery
        {
        pushFollow(FOLLOW_table_function_subquery_in_synpred93_SQL92Query1836);
        table_function_subquery();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred93_SQL92Query

    // $ANTLR start synpred94_SQL92Query
    public final void synpred94_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:47: ( ',' table_function_subquery )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:47: ',' table_function_subquery
        {
        match(input,55,FOLLOW_55_in_synpred94_SQL92Query1840); if (state.failed) return ;
        pushFollow(FOLLOW_table_function_subquery_in_synpred94_SQL92Query1842);
        table_function_subquery();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred94_SQL92Query

    // $ANTLR start synpred97_SQL92Query
    public final void synpred97_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:213:9: ( search_condition )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:213:9: search_condition
        {
        pushFollow(FOLLOW_search_condition_in_synpred97_SQL92Query1932);
        search_condition();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred97_SQL92Query

    // $ANTLR start synpred103_SQL92Query
    public final void synpred103_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:9: ( predicate )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:9: predicate
        {
        pushFollow(FOLLOW_predicate_in_synpred103_SQL92Query2134);
        predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred103_SQL92Query

    // $ANTLR start synpred104_SQL92Query
    public final void synpred104_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:9: ( comparison_predicate )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:9: comparison_predicate
        {
        pushFollow(FOLLOW_comparison_predicate_in_synpred104_SQL92Query2165);
        comparison_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred104_SQL92Query

    // $ANTLR start synpred105_SQL92Query
    public final void synpred105_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:32: ( like_predicate )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:32: like_predicate
        {
        pushFollow(FOLLOW_like_predicate_in_synpred105_SQL92Query2169);
        like_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred105_SQL92Query

    // $ANTLR start synpred106_SQL92Query
    public final void synpred106_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:49: ( in_predicate )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:49: in_predicate
        {
        pushFollow(FOLLOW_in_predicate_in_synpred106_SQL92Query2173);
        in_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred106_SQL92Query

    // $ANTLR start synpred107_SQL92Query
    public final void synpred107_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:64: ( null_predicate )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:64: null_predicate
        {
        pushFollow(FOLLOW_null_predicate_in_synpred107_SQL92Query2177);
        null_predicate();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred107_SQL92Query

    // $ANTLR start synpred109_SQL92Query
    public final void synpred109_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:9: ( row_value 'IS' 'NULL' )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:9: row_value 'IS' 'NULL'
        {
        pushFollow(FOLLOW_row_value_in_synpred109_SQL92Query2203);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        match(input,90,FOLLOW_90_in_synpred109_SQL92Query2205); if (state.failed) return ;
        match(input,76,FOLLOW_76_in_synpred109_SQL92Query2207); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred109_SQL92Query

    // $ANTLR start synpred110_SQL92Query
    public final void synpred110_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: ( row_value 'NOT' 'IN' in_predicate_tail )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: row_value 'NOT' 'IN' in_predicate_tail
        {
        pushFollow(FOLLOW_row_value_in_synpred110_SQL92Query2261);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        match(input,89,FOLLOW_89_in_synpred110_SQL92Query2263); if (state.failed) return ;
        match(input,91,FOLLOW_91_in_synpred110_SQL92Query2265); if (state.failed) return ;
        pushFollow(FOLLOW_in_predicate_tail_in_synpred110_SQL92Query2267);
        in_predicate_tail();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred110_SQL92Query

    // $ANTLR start synpred111_SQL92Query
    public final void synpred111_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:251:9: ( sub_query )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:251:9: sub_query
        {
        pushFollow(FOLLOW_sub_query_in_synpred111_SQL92Query2347);
        sub_query();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred111_SQL92Query

    // $ANTLR start synpred113_SQL92Query
    public final void synpred113_SQL92Query_fragment() throws RecognitionException {   
        SQL92QueryParser.row_value_return value = null;

        SQL92QueryParser.row_value_return btw1 = null;

        SQL92QueryParser.row_value_return btw2 = null;


        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
        {
        pushFollow(FOLLOW_row_value_in_synpred113_SQL92Query2400);
        value=row_value();

        state._fsp--;
        if (state.failed) return ;
        match(input,92,FOLLOW_92_in_synpred113_SQL92Query2402); if (state.failed) return ;
        pushFollow(FOLLOW_row_value_in_synpred113_SQL92Query2406);
        btw1=row_value();

        state._fsp--;
        if (state.failed) return ;
        match(input,88,FOLLOW_88_in_synpred113_SQL92Query2408); if (state.failed) return ;
        pushFollow(FOLLOW_row_value_in_synpred113_SQL92Query2412);
        btw2=row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred113_SQL92Query

    // $ANTLR start synpred123_SQL92Query
    public final void synpred123_SQL92Query_fragment() throws RecognitionException {   
        Token op=null;
        Token ep=null;
        SQL92QueryParser.row_value_return lv = null;

        SQL92QueryParser.row_value_return rv = null;


        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:9: (lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:9: lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value
        {
        pushFollow(FOLLOW_row_value_in_synpred123_SQL92Query2553);
        lv=row_value();

        state._fsp--;
        if (state.failed) return ;
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:22: (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' )
        int alt89=7;
        switch ( input.LA(1) ) {
        case 94:
            {
            alt89=1;
            }
            break;
        case 95:
            {
            alt89=2;
            }
            break;
        case 96:
            {
            alt89=3;
            }
            break;
        case 97:
            {
            alt89=4;
            }
            break;
        case 98:
            {
            alt89=5;
            }
            break;
        case 99:
            {
            alt89=6;
            }
            break;
        case 100:
            {
            alt89=7;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 89, 0, input);

            throw nvae;
        }

        switch (alt89) {
            case 1 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:23: op= '='
                {
                op=(Token)match(input,94,FOLLOW_94_in_synpred123_SQL92Query2558); if (state.failed) return ;

                }
                break;
            case 2 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:30: op= '<>'
                {
                op=(Token)match(input,95,FOLLOW_95_in_synpred123_SQL92Query2562); if (state.failed) return ;

                }
                break;
            case 3 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:38: op= '!='
                {
                op=(Token)match(input,96,FOLLOW_96_in_synpred123_SQL92Query2566); if (state.failed) return ;

                }
                break;
            case 4 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:46: op= '<'
                {
                op=(Token)match(input,97,FOLLOW_97_in_synpred123_SQL92Query2570); if (state.failed) return ;

                }
                break;
            case 5 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:53: op= '>'
                {
                op=(Token)match(input,98,FOLLOW_98_in_synpred123_SQL92Query2574); if (state.failed) return ;

                }
                break;
            case 6 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:60: op= '>='
                {
                op=(Token)match(input,99,FOLLOW_99_in_synpred123_SQL92Query2578); if (state.failed) return ;

                }
                break;
            case 7 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:68: op= '<='
                {
                op=(Token)match(input,100,FOLLOW_100_in_synpred123_SQL92Query2582); if (state.failed) return ;

                }
                break;

        }

        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:77: (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' )
        int alt90=3;
        switch ( input.LA(1) ) {
        case 43:
            {
            alt90=1;
            }
            break;
        case 101:
            {
            alt90=2;
            }
            break;
        case 102:
            {
            alt90=3;
            }
            break;
        default:
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 90, 0, input);

            throw nvae;
        }

        switch (alt90) {
            case 1 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:78: ep= 'ALL'
                {
                ep=(Token)match(input,43,FOLLOW_43_in_synpred123_SQL92Query2588); if (state.failed) return ;

                }
                break;
            case 2 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:87: ep= 'SOME'
                {
                ep=(Token)match(input,101,FOLLOW_101_in_synpred123_SQL92Query2592); if (state.failed) return ;

                }
                break;
            case 3 :
                // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:97: ep= 'ANY'
                {
                ep=(Token)match(input,102,FOLLOW_102_in_synpred123_SQL92Query2596); if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_row_value_in_synpred123_SQL92Query2601);
        rv=row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred123_SQL92Query

    // $ANTLR start synpred130_SQL92Query
    public final void synpred130_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:9: ( row_value 'LIKE' row_value )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:9: row_value 'LIKE' row_value
        {
        pushFollow(FOLLOW_row_value_in_synpred130_SQL92Query2690);
        row_value();

        state._fsp--;
        if (state.failed) return ;
        match(input,103,FOLLOW_103_in_synpred130_SQL92Query2692); if (state.failed) return ;
        pushFollow(FOLLOW_row_value_in_synpred130_SQL92Query2695);
        row_value();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred130_SQL92Query

    // $ANTLR start synpred131_SQL92Query
    public final void synpred131_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: ( value_expression )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: value_expression
        {
        pushFollow(FOLLOW_value_expression_in_synpred131_SQL92Query2749);
        value_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred131_SQL92Query

    // $ANTLR start synpred132_SQL92Query
    public final void synpred132_SQL92Query_fragment() throws RecognitionException {   
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:27: ( 'NULL' )
        // ../firethorn/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:27: 'NULL'
        {
        match(input,76,FOLLOW_76_in_synpred132_SQL92Query2752); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred132_SQL92Query

    // Delegated rules

    public final boolean synpred81_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred81_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred106_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred106_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred58_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred58_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred111_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred111_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred47_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred47_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred103_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred103_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred109_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred109_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred94_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred94_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred130_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred130_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred39_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred39_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred132_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred132_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred45_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred45_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred44_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred44_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred97_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred97_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred107_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred107_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred110_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred110_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred105_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred105_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred67_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred67_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred123_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred123_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred113_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred113_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred93_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred93_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred131_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred131_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred104_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred104_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred46_SQL92Query() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred46_SQL92Query_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA21 dfa21 = new DFA21(this);
    protected DFA25 dfa25 = new DFA25(this);
    protected DFA28 dfa28 = new DFA28(this);
    protected DFA29 dfa29 = new DFA29(this);
    protected DFA35 dfa35 = new DFA35(this);
    protected DFA51 dfa51 = new DFA51(this);
    protected DFA52 dfa52 = new DFA52(this);
    protected DFA55 dfa55 = new DFA55(this);
    protected DFA60 dfa60 = new DFA60(this);
    protected DFA61 dfa61 = new DFA61(this);
    protected DFA62 dfa62 = new DFA62(this);
    protected DFA63 dfa63 = new DFA63(this);
    protected DFA66 dfa66 = new DFA66(this);
    protected DFA69 dfa69 = new DFA69(this);
    protected DFA70 dfa70 = new DFA70(this);
    static final String DFA21_eotS =
        "\21\uffff";
    static final String DFA21_eofS =
        "\1\uffff\14\16\3\uffff\1\16";
    static final String DFA21_minS =
        "\1\42\14\50\1\43\2\uffff\1\50";
    static final String DFA21_maxS =
        "\1\110\1\76\13\75\1\110\2\uffff\1\75";
    static final String DFA21_acceptS =
        "\16\uffff\1\2\1\1\1\uffff";
    static final String DFA21_specialS =
        "\21\uffff}>";
    static final String[] DFA21_transitionS = {
            "\1\2\1\1\33\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
            "\14",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16\1\15",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16",
            "\1\20\33\uffff\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14",
            "",
            "",
            "\2\16\15\uffff\1\16\4\uffff\1\17\1\16"
    };

    static final short[] DFA21_eot = DFA.unpackEncodedString(DFA21_eotS);
    static final short[] DFA21_eof = DFA.unpackEncodedString(DFA21_eofS);
    static final char[] DFA21_min = DFA.unpackEncodedStringToUnsignedChars(DFA21_minS);
    static final char[] DFA21_max = DFA.unpackEncodedStringToUnsignedChars(DFA21_maxS);
    static final short[] DFA21_accept = DFA.unpackEncodedString(DFA21_acceptS);
    static final short[] DFA21_special = DFA.unpackEncodedString(DFA21_specialS);
    static final short[][] DFA21_transition;

    static {
        int numStates = DFA21_transitionS.length;
        DFA21_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA21_transition[i] = DFA.unpackEncodedString(DFA21_transitionS[i]);
        }
    }

    class DFA21 extends DFA {

        public DFA21(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 21;
            this.eot = DFA21_eot;
            this.eof = DFA21_eof;
            this.min = DFA21_min;
            this.max = DFA21_max;
            this.accept = DFA21_accept;
            this.special = DFA21_special;
            this.transition = DFA21_transition;
        }
        public String getDescription() {
            return "138:1: ordered_sort_spec : ( sort_spec 'DESC' -> ^( DESC sort_spec ) | sort_spec ( 'ASC' )? -> ^( ASC sort_spec ) );";
        }
    }
    static final String DFA25_eotS =
        "\73\uffff";
    static final String DFA25_eofS =
        "\1\1\72\uffff";
    static final String DFA25_minS =
        "\1\42\10\uffff\2\0\60\uffff";
    static final String DFA25_maxS =
        "\1\151\10\uffff\2\0\60\uffff";
    static final String DFA25_acceptS =
        "\1\uffff\1\2\70\uffff\1\1";
    static final String DFA25_specialS =
        "\11\uffff\1\0\1\1\60\uffff}>";
    static final String[] DFA25_transitionS = {
            "\5\1\1\uffff\3\1\1\uffff\2\1\1\uffff\4\1\1\uffff\2\1\1\uffff"+
            "\1\1\1\uffff\2\1\4\uffff\12\1\1\11\1\12\1\uffff\3\1\1\uffff"+
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
            return "()* loopback of 151:16: ( ( '+' | '-' ) factor )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA25_9 = input.LA(1);

                         
                        int index25_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred39_SQL92Query()) ) {s = 58;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index25_9);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA25_10 = input.LA(1);

                         
                        int index25_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred39_SQL92Query()) ) {s = 58;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index25_10);
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
    static final String DFA28_eotS =
        "\30\uffff";
    static final String DFA28_eofS =
        "\30\uffff";
    static final String DFA28_minS =
        "\1\42\2\0\25\uffff";
    static final String DFA28_maxS =
        "\1\116\2\0\25\uffff";
    static final String DFA28_acceptS =
        "\3\uffff\1\4\20\uffff\1\1\1\5\1\2\1\3";
    static final String DFA28_specialS =
        "\1\uffff\1\0\1\1\25\uffff}>";
    static final String[] DFA28_transitionS = {
            "\1\3\1\2\3\3\15\uffff\1\1\12\uffff\12\3\3\uffff\3\3",
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

    static final short[] DFA28_eot = DFA.unpackEncodedString(DFA28_eotS);
    static final short[] DFA28_eof = DFA.unpackEncodedString(DFA28_eofS);
    static final char[] DFA28_min = DFA.unpackEncodedStringToUnsignedChars(DFA28_minS);
    static final char[] DFA28_max = DFA.unpackEncodedStringToUnsignedChars(DFA28_maxS);
    static final short[] DFA28_accept = DFA.unpackEncodedString(DFA28_acceptS);
    static final short[] DFA28_special = DFA.unpackEncodedString(DFA28_specialS);
    static final short[][] DFA28_transition;

    static {
        int numStates = DFA28_transitionS.length;
        DFA28_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA28_transition[i] = DFA.unpackEncodedString(DFA28_transitionS[i]);
        }
    }

    class DFA28 extends DFA {

        public DFA28(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 28;
            this.eot = DFA28_eot;
            this.eof = DFA28_eof;
            this.min = DFA28_min;
            this.max = DFA28_max;
            this.accept = DFA28_accept;
            this.special = DFA28_special;
            this.transition = DFA28_transition;
        }
        public String getDescription() {
            return "159:1: value_expression_primary : ( '(' value_expression ')' | function | column_name | literal | sub_query );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA28_1 = input.LA(1);

                         
                        int index28_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred44_SQL92Query()) ) {s = 20;}

                        else if ( (true) ) {s = 21;}

                         
                        input.seek(index28_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA28_2 = input.LA(1);

                         
                        int index28_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred45_SQL92Query()) ) {s = 22;}

                        else if ( (synpred46_SQL92Query()) ) {s = 23;}

                        else if ( (synpred47_SQL92Query()) ) {s = 3;}

                         
                        input.seek(index28_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 28, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA29_eotS =
        "\14\uffff";
    static final String DFA29_eofS =
        "\14\uffff";
    static final String DFA29_minS =
        "\1\42\5\uffff\1\76\4\uffff\1\77";
    static final String DFA29_maxS =
        "\1\116\5\uffff\1\76\4\uffff\1\110";
    static final String DFA29_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\uffff\1\6\1\7\1\10\1\11\1\uffff";
    static final String DFA29_specialS =
        "\14\uffff}>";
    static final String[] DFA29_transitionS = {
            "\1\1\1\6\1\2\1\3\1\4\30\uffff\3\5\7\7\3\uffff\1\10\1\11\1\12",
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

    static final short[] DFA29_eot = DFA.unpackEncodedString(DFA29_eotS);
    static final short[] DFA29_eof = DFA.unpackEncodedString(DFA29_eofS);
    static final char[] DFA29_min = DFA.unpackEncodedStringToUnsignedChars(DFA29_minS);
    static final char[] DFA29_max = DFA.unpackEncodedStringToUnsignedChars(DFA29_maxS);
    static final short[] DFA29_accept = DFA.unpackEncodedString(DFA29_acceptS);
    static final short[] DFA29_special = DFA.unpackEncodedString(DFA29_specialS);
    static final short[][] DFA29_transition;

    static {
        int numStates = DFA29_transitionS.length;
        DFA29_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA29_transition[i] = DFA.unpackEncodedString(DFA29_transitionS[i]);
        }
    }

    class DFA29 extends DFA {

        public DFA29(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 29;
            this.eot = DFA29_eot;
            this.eof = DFA29_eof;
            this.min = DFA29_min;
            this.max = DFA29_max;
            this.accept = DFA29_accept;
            this.special = DFA29_special;
            this.transition = DFA29_transition;
        }
        public String getDescription() {
            return "166:1: literal : ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' );";
        }
    }
    static final String DFA35_eotS =
        "\13\uffff";
    static final String DFA35_eofS =
        "\1\uffff\1\2\11\uffff";
    static final String DFA35_minS =
        "\1\43\1\42\1\uffff\1\42\6\0\1\uffff";
    static final String DFA35_maxS =
        "\1\110\1\151\1\uffff\1\151\6\0\1\uffff";
    static final String DFA35_acceptS =
        "\2\uffff\1\2\7\uffff\1\1";
    static final String DFA35_specialS =
        "\4\uffff\1\0\1\5\1\4\1\2\1\1\1\3\1\uffff}>";
    static final String[] DFA35_transitionS = {
            "\1\2\36\uffff\1\1\6\2",
            "\4\2\1\3\1\uffff\3\2\1\uffff\2\2\1\uffff\4\2\1\uffff\4\2\1"+
            "\uffff\2\2\4\uffff\20\2\1\uffff\1\2\1\uffff\4\2\1\uffff\16\2"+
            "\2\uffff\3\2",
            "",
            "\5\2\15\uffff\4\2\7\uffff\4\2\1\4\1\5\1\6\1\7\1\10\1\11\7\2"+
            "\11\uffff\14\2\2\uffff\3\2",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            ""
    };

    static final short[] DFA35_eot = DFA.unpackEncodedString(DFA35_eotS);
    static final short[] DFA35_eof = DFA.unpackEncodedString(DFA35_eofS);
    static final char[] DFA35_min = DFA.unpackEncodedStringToUnsignedChars(DFA35_minS);
    static final char[] DFA35_max = DFA.unpackEncodedStringToUnsignedChars(DFA35_maxS);
    static final short[] DFA35_accept = DFA.unpackEncodedString(DFA35_acceptS);
    static final short[] DFA35_special = DFA.unpackEncodedString(DFA35_specialS);
    static final short[][] DFA35_transition;

    static {
        int numStates = DFA35_transitionS.length;
        DFA35_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA35_transition[i] = DFA.unpackEncodedString(DFA35_transitionS[i]);
        }
    }

    class DFA35 extends DFA {

        public DFA35(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 35;
            this.eot = DFA35_eot;
            this.eof = DFA35_eof;
            this.min = DFA35_min;
            this.max = DFA35_max;
            this.accept = DFA35_accept;
            this.special = DFA35_special;
            this.transition = DFA35_transition;
        }
        public String getDescription() {
            return "173:1: interval : ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' | s= 'YEAR' | s= 'MONTH' | s= 'DAY' | s= 'HOUR' | s= 'MINUTE' | s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA35_4 = input.LA(1);

                         
                        int index35_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_4);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA35_8 = input.LA(1);

                         
                        int index35_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_8);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA35_7 = input.LA(1);

                         
                        int index35_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_7);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA35_9 = input.LA(1);

                         
                        int index35_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_9);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA35_6 = input.LA(1);

                         
                        int index35_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA35_5 = input.LA(1);

                         
                        int index35_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred67_SQL92Query()) ) {s = 10;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index35_5);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 35, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA51_eotS =
        "\35\uffff";
    static final String DFA51_eofS =
        "\35\uffff";
    static final String DFA51_minS =
        "\1\42\1\0\33\uffff";
    static final String DFA51_maxS =
        "\1\151\1\0\33\uffff";
    static final String DFA51_acceptS =
        "\2\uffff\1\2\31\uffff\1\1";
    static final String DFA51_specialS =
        "\1\uffff\1\0\33\uffff}>";
    static final String[] DFA51_transitionS = {
            "\5\2\15\uffff\1\1\1\2\1\uffff\1\2\7\uffff\14\2\1\uffff\3\2\12"+
            "\uffff\1\2\3\uffff\1\2\12\uffff\2\2",
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

    static final short[] DFA51_eot = DFA.unpackEncodedString(DFA51_eotS);
    static final short[] DFA51_eof = DFA.unpackEncodedString(DFA51_eofS);
    static final char[] DFA51_min = DFA.unpackEncodedStringToUnsignedChars(DFA51_minS);
    static final char[] DFA51_max = DFA.unpackEncodedStringToUnsignedChars(DFA51_maxS);
    static final short[] DFA51_accept = DFA.unpackEncodedString(DFA51_acceptS);
    static final short[] DFA51_special = DFA.unpackEncodedString(DFA51_specialS);
    static final short[][] DFA51_transition;

    static {
        int numStates = DFA51_transitionS.length;
        DFA51_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA51_transition[i] = DFA.unpackEncodedString(DFA51_transitionS[i]);
        }
    }

    class DFA51 extends DFA {

        public DFA51(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 51;
            this.eot = DFA51_eot;
            this.eof = DFA51_eof;
            this.min = DFA51_min;
            this.max = DFA51_max;
            this.accept = DFA51_accept;
            this.special = DFA51_special;
            this.transition = DFA51_transition;
        }
        public String getDescription() {
            return "206:21: ( table_function_subquery )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA51_1 = input.LA(1);

                         
                        int index51_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred93_SQL92Query()) ) {s = 28;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index51_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 51, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA52_eotS =
        "\35\uffff";
    static final String DFA52_eofS =
        "\35\uffff";
    static final String DFA52_minS =
        "\1\42\1\0\33\uffff";
    static final String DFA52_maxS =
        "\1\151\1\0\33\uffff";
    static final String DFA52_acceptS =
        "\2\uffff\1\2\31\uffff\1\1";
    static final String DFA52_specialS =
        "\1\uffff\1\0\33\uffff}>";
    static final String[] DFA52_transitionS = {
            "\5\2\15\uffff\2\2\1\uffff\1\1\7\uffff\14\2\1\uffff\3\2\12\uffff"+
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
            return "()* loopback of 206:46: ( ',' table_function_subquery )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA52_1 = input.LA(1);

                         
                        int index52_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred94_SQL92Query()) ) {s = 28;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index52_1);
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
    static final String DFA55_eotS =
        "\33\uffff";
    static final String DFA55_eofS =
        "\33\uffff";
    static final String DFA55_minS =
        "\1\42\1\uffff\25\0\4\uffff";
    static final String DFA55_maxS =
        "\1\151\1\uffff\25\0\4\uffff";
    static final String DFA55_acceptS =
        "\1\uffff\1\1\30\uffff\1\2";
    static final String DFA55_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\4\uffff}>";
    static final String[] DFA55_transitionS = {
            "\1\7\1\2\1\10\1\11\1\3\15\uffff\1\6\12\uffff\1\12\1\13\1\14"+
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

    static final short[] DFA55_eot = DFA.unpackEncodedString(DFA55_eotS);
    static final short[] DFA55_eof = DFA.unpackEncodedString(DFA55_eofS);
    static final char[] DFA55_min = DFA.unpackEncodedStringToUnsignedChars(DFA55_minS);
    static final char[] DFA55_max = DFA.unpackEncodedStringToUnsignedChars(DFA55_maxS);
    static final short[] DFA55_accept = DFA.unpackEncodedString(DFA55_acceptS);
    static final short[] DFA55_special = DFA.unpackEncodedString(DFA55_specialS);
    static final short[][] DFA55_transition;

    static {
        int numStates = DFA55_transitionS.length;
        DFA55_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA55_transition[i] = DFA.unpackEncodedString(DFA55_transitionS[i]);
        }
    }

    class DFA55 extends DFA {

        public DFA55(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 55;
            this.eot = DFA55_eot;
            this.eof = DFA55_eof;
            this.min = DFA55_min;
            this.max = DFA55_max;
            this.accept = DFA55_accept;
            this.special = DFA55_special;
            this.transition = DFA55_transition;
        }
        public String getDescription() {
            return "212:1: table_function_param : ( search_condition | value_expression );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA55_2 = input.LA(1);

                         
                        int index55_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA55_3 = input.LA(1);

                         
                        int index55_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA55_4 = input.LA(1);

                         
                        int index55_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA55_5 = input.LA(1);

                         
                        int index55_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA55_6 = input.LA(1);

                         
                        int index55_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA55_7 = input.LA(1);

                         
                        int index55_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA55_8 = input.LA(1);

                         
                        int index55_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA55_9 = input.LA(1);

                         
                        int index55_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA55_10 = input.LA(1);

                         
                        int index55_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA55_11 = input.LA(1);

                         
                        int index55_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA55_12 = input.LA(1);

                         
                        int index55_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA55_13 = input.LA(1);

                         
                        int index55_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA55_14 = input.LA(1);

                         
                        int index55_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA55_15 = input.LA(1);

                         
                        int index55_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA55_16 = input.LA(1);

                         
                        int index55_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA55_17 = input.LA(1);

                         
                        int index55_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA55_18 = input.LA(1);

                         
                        int index55_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA55_19 = input.LA(1);

                         
                        int index55_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA55_20 = input.LA(1);

                         
                        int index55_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA55_21 = input.LA(1);

                         
                        int index55_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA55_22 = input.LA(1);

                         
                        int index55_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred97_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 26;}

                         
                        input.seek(index55_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 55, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA60_eotS =
        "\32\uffff";
    static final String DFA60_eofS =
        "\32\uffff";
    static final String DFA60_minS =
        "\1\42\5\uffff\1\0\23\uffff";
    static final String DFA60_maxS =
        "\1\151\5\uffff\1\0\23\uffff";
    static final String DFA60_acceptS =
        "\1\uffff\1\1\27\uffff\1\2";
    static final String DFA60_specialS =
        "\6\uffff\1\0\23\uffff}>";
    static final String[] DFA60_transitionS = {
            "\5\1\15\uffff\1\6\12\uffff\14\1\1\uffff\3\1\16\uffff\1\1\12"+
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
            return "234:1: boolean_primary : ( predicate | '(' search_condition ')' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA60_6 = input.LA(1);

                         
                        int index60_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_SQL92Query()) ) {s = 1;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index60_6);
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
    static final String DFA61_eotS =
        "\35\uffff";
    static final String DFA61_eofS =
        "\35\uffff";
    static final String DFA61_minS =
        "\1\42\1\uffff\26\0\5\uffff";
    static final String DFA61_maxS =
        "\1\151\1\uffff\26\0\5\uffff";
    static final String DFA61_acceptS =
        "\1\uffff\1\1\26\uffff\1\5\1\2\1\3\1\4\1\6";
    static final String DFA61_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\5\uffff}>";
    static final String[] DFA61_transitionS = {
            "\1\7\1\2\1\10\1\11\1\3\15\uffff\1\6\12\uffff\1\12\1\13\1\14"+
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

    static final short[] DFA61_eot = DFA.unpackEncodedString(DFA61_eotS);
    static final short[] DFA61_eof = DFA.unpackEncodedString(DFA61_eofS);
    static final char[] DFA61_min = DFA.unpackEncodedStringToUnsignedChars(DFA61_minS);
    static final char[] DFA61_max = DFA.unpackEncodedStringToUnsignedChars(DFA61_maxS);
    static final short[] DFA61_accept = DFA.unpackEncodedString(DFA61_acceptS);
    static final short[] DFA61_special = DFA.unpackEncodedString(DFA61_specialS);
    static final short[][] DFA61_transition;

    static {
        int numStates = DFA61_transitionS.length;
        DFA61_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA61_transition[i] = DFA.unpackEncodedString(DFA61_transitionS[i]);
        }
    }

    class DFA61 extends DFA {

        public DFA61(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 61;
            this.eot = DFA61_eot;
            this.eof = DFA61_eof;
            this.min = DFA61_min;
            this.max = DFA61_max;
            this.accept = DFA61_accept;
            this.special = DFA61_special;
            this.transition = DFA61_transition;
        }
        public String getDescription() {
            return "237:1: predicate : ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA61_2 = input.LA(1);

                         
                        int index61_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA61_3 = input.LA(1);

                         
                        int index61_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA61_4 = input.LA(1);

                         
                        int index61_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA61_5 = input.LA(1);

                         
                        int index61_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA61_6 = input.LA(1);

                         
                        int index61_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA61_7 = input.LA(1);

                         
                        int index61_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA61_8 = input.LA(1);

                         
                        int index61_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA61_9 = input.LA(1);

                         
                        int index61_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA61_10 = input.LA(1);

                         
                        int index61_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA61_11 = input.LA(1);

                         
                        int index61_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA61_12 = input.LA(1);

                         
                        int index61_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA61_13 = input.LA(1);

                         
                        int index61_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA61_14 = input.LA(1);

                         
                        int index61_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA61_15 = input.LA(1);

                         
                        int index61_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA61_16 = input.LA(1);

                         
                        int index61_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA61_17 = input.LA(1);

                         
                        int index61_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA61_18 = input.LA(1);

                         
                        int index61_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA61_19 = input.LA(1);

                         
                        int index61_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA61_20 = input.LA(1);

                         
                        int index61_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA61_21 = input.LA(1);

                         
                        int index61_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA61_22 = input.LA(1);

                         
                        int index61_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA61_23 = input.LA(1);

                         
                        int index61_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred104_SQL92Query()) ) {s = 1;}

                        else if ( (synpred105_SQL92Query()) ) {s = 25;}

                        else if ( (synpred106_SQL92Query()) ) {s = 26;}

                        else if ( (synpred107_SQL92Query()) ) {s = 27;}

                        else if ( (true) ) {s = 28;}

                         
                        input.seek(index61_23);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 61, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA62_eotS =
        "\31\uffff";
    static final String DFA62_eofS =
        "\31\uffff";
    static final String DFA62_minS =
        "\1\42\26\0\2\uffff";
    static final String DFA62_maxS =
        "\1\150\26\0\2\uffff";
    static final String DFA62_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA62_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA62_transitionS = {
            "\1\6\1\1\1\7\1\10\1\2\15\uffff\1\5\12\uffff\1\11\1\12\1\13\1"+
            "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
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

    static final short[] DFA62_eot = DFA.unpackEncodedString(DFA62_eotS);
    static final short[] DFA62_eof = DFA.unpackEncodedString(DFA62_eofS);
    static final char[] DFA62_min = DFA.unpackEncodedStringToUnsignedChars(DFA62_minS);
    static final char[] DFA62_max = DFA.unpackEncodedStringToUnsignedChars(DFA62_maxS);
    static final short[] DFA62_accept = DFA.unpackEncodedString(DFA62_acceptS);
    static final short[] DFA62_special = DFA.unpackEncodedString(DFA62_specialS);
    static final short[][] DFA62_transition;

    static {
        int numStates = DFA62_transitionS.length;
        DFA62_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA62_transition[i] = DFA.unpackEncodedString(DFA62_transitionS[i]);
        }
    }

    class DFA62 extends DFA {

        public DFA62(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 62;
            this.eot = DFA62_eot;
            this.eof = DFA62_eof;
            this.min = DFA62_min;
            this.max = DFA62_max;
            this.accept = DFA62_accept;
            this.special = DFA62_special;
            this.transition = DFA62_transition;
        }
        public String getDescription() {
            return "240:1: null_predicate : ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA62_1 = input.LA(1);

                         
                        int index62_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA62_2 = input.LA(1);

                         
                        int index62_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA62_3 = input.LA(1);

                         
                        int index62_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA62_4 = input.LA(1);

                         
                        int index62_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA62_5 = input.LA(1);

                         
                        int index62_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA62_6 = input.LA(1);

                         
                        int index62_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA62_7 = input.LA(1);

                         
                        int index62_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA62_8 = input.LA(1);

                         
                        int index62_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA62_9 = input.LA(1);

                         
                        int index62_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA62_10 = input.LA(1);

                         
                        int index62_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA62_11 = input.LA(1);

                         
                        int index62_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA62_12 = input.LA(1);

                         
                        int index62_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA62_13 = input.LA(1);

                         
                        int index62_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA62_14 = input.LA(1);

                         
                        int index62_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA62_15 = input.LA(1);

                         
                        int index62_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA62_16 = input.LA(1);

                         
                        int index62_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA62_17 = input.LA(1);

                         
                        int index62_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA62_18 = input.LA(1);

                         
                        int index62_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA62_19 = input.LA(1);

                         
                        int index62_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA62_20 = input.LA(1);

                         
                        int index62_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA62_21 = input.LA(1);

                         
                        int index62_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA62_22 = input.LA(1);

                         
                        int index62_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred109_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index62_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 62, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA63_eotS =
        "\31\uffff";
    static final String DFA63_eofS =
        "\31\uffff";
    static final String DFA63_minS =
        "\1\42\26\0\2\uffff";
    static final String DFA63_maxS =
        "\1\150\26\0\2\uffff";
    static final String DFA63_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA63_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA63_transitionS = {
            "\1\6\1\1\1\7\1\10\1\2\15\uffff\1\5\12\uffff\1\11\1\12\1\13\1"+
            "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
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
            return "244:1: in_predicate : ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) );";
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
        "\31\uffff";
    static final String DFA66_eofS =
        "\31\uffff";
    static final String DFA66_minS =
        "\1\42\26\0\2\uffff";
    static final String DFA66_maxS =
        "\1\150\26\0\2\uffff";
    static final String DFA66_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA66_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA66_transitionS = {
            "\1\6\1\1\1\7\1\10\1\2\15\uffff\1\5\12\uffff\1\11\1\12\1\13\1"+
            "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
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
            return "254:1: between_predicate : (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) | value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA66_1 = input.LA(1);

                         
                        int index66_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA66_2 = input.LA(1);

                         
                        int index66_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA66_3 = input.LA(1);

                         
                        int index66_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA66_4 = input.LA(1);

                         
                        int index66_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA66_5 = input.LA(1);

                         
                        int index66_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA66_6 = input.LA(1);

                         
                        int index66_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA66_7 = input.LA(1);

                         
                        int index66_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA66_8 = input.LA(1);

                         
                        int index66_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA66_9 = input.LA(1);

                         
                        int index66_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA66_10 = input.LA(1);

                         
                        int index66_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA66_11 = input.LA(1);

                         
                        int index66_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA66_12 = input.LA(1);

                         
                        int index66_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA66_13 = input.LA(1);

                         
                        int index66_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA66_14 = input.LA(1);

                         
                        int index66_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA66_15 = input.LA(1);

                         
                        int index66_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA66_16 = input.LA(1);

                         
                        int index66_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA66_17 = input.LA(1);

                         
                        int index66_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA66_18 = input.LA(1);

                         
                        int index66_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA66_19 = input.LA(1);

                         
                        int index66_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA66_20 = input.LA(1);

                         
                        int index66_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA66_21 = input.LA(1);

                         
                        int index66_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA66_22 = input.LA(1);

                         
                        int index66_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred113_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index66_22);
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
    static final String DFA69_eotS =
        "\32\uffff";
    static final String DFA69_eofS =
        "\32\uffff";
    static final String DFA69_minS =
        "\1\42\1\uffff\26\0\2\uffff";
    static final String DFA69_maxS =
        "\1\151\1\uffff\26\0\2\uffff";
    static final String DFA69_acceptS =
        "\1\uffff\1\1\26\uffff\1\2\1\3";
    static final String DFA69_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA69_transitionS = {
            "\1\7\1\2\1\10\1\11\1\3\15\uffff\1\6\12\uffff\1\12\1\13\1\14"+
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

    static final short[] DFA69_eot = DFA.unpackEncodedString(DFA69_eotS);
    static final short[] DFA69_eof = DFA.unpackEncodedString(DFA69_eofS);
    static final char[] DFA69_min = DFA.unpackEncodedStringToUnsignedChars(DFA69_minS);
    static final char[] DFA69_max = DFA.unpackEncodedStringToUnsignedChars(DFA69_maxS);
    static final short[] DFA69_accept = DFA.unpackEncodedString(DFA69_acceptS);
    static final short[] DFA69_special = DFA.unpackEncodedString(DFA69_specialS);
    static final short[][] DFA69_transition;

    static {
        int numStates = DFA69_transitionS.length;
        DFA69_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA69_transition[i] = DFA.unpackEncodedString(DFA69_transitionS[i]);
        }
    }

    class DFA69 extends DFA {

        public DFA69(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 69;
            this.eot = DFA69_eot;
            this.eof = DFA69_eof;
            this.min = DFA69_min;
            this.max = DFA69_max;
            this.accept = DFA69_accept;
            this.special = DFA69_special;
            this.transition = DFA69_transition;
        }
        public String getDescription() {
            return "263:1: comparison_predicate : ( bind_table '=' row_value | lv= row_value (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) (ep= 'ALL' | ep= 'SOME' | ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) row_value );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA69_2 = input.LA(1);

                         
                        int index69_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA69_3 = input.LA(1);

                         
                        int index69_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA69_4 = input.LA(1);

                         
                        int index69_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA69_5 = input.LA(1);

                         
                        int index69_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA69_6 = input.LA(1);

                         
                        int index69_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA69_7 = input.LA(1);

                         
                        int index69_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA69_8 = input.LA(1);

                         
                        int index69_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA69_9 = input.LA(1);

                         
                        int index69_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_9);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA69_10 = input.LA(1);

                         
                        int index69_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_10);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA69_11 = input.LA(1);

                         
                        int index69_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_11);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA69_12 = input.LA(1);

                         
                        int index69_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_12);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA69_13 = input.LA(1);

                         
                        int index69_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_13);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA69_14 = input.LA(1);

                         
                        int index69_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_14);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA69_15 = input.LA(1);

                         
                        int index69_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_15);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA69_16 = input.LA(1);

                         
                        int index69_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_16);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA69_17 = input.LA(1);

                         
                        int index69_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_17);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA69_18 = input.LA(1);

                         
                        int index69_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_18);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA69_19 = input.LA(1);

                         
                        int index69_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_19);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA69_20 = input.LA(1);

                         
                        int index69_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_20);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA69_21 = input.LA(1);

                         
                        int index69_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_21);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA69_22 = input.LA(1);

                         
                        int index69_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_22);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA69_23 = input.LA(1);

                         
                        int index69_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred123_SQL92Query()) ) {s = 24;}

                        else if ( (true) ) {s = 25;}

                         
                        input.seek(index69_23);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 69, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA70_eotS =
        "\31\uffff";
    static final String DFA70_eofS =
        "\31\uffff";
    static final String DFA70_minS =
        "\1\42\26\0\2\uffff";
    static final String DFA70_maxS =
        "\1\150\26\0\2\uffff";
    static final String DFA70_acceptS =
        "\27\uffff\1\1\1\2";
    static final String DFA70_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14"+
        "\1\15\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\2\uffff}>";
    static final String[] DFA70_transitionS = {
            "\1\6\1\1\1\7\1\10\1\2\15\uffff\1\5\12\uffff\1\11\1\12\1\13\1"+
            "\14\1\15\1\16\1\17\1\20\1\21\1\22\1\3\1\4\1\uffff\1\23\1\24"+
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

    static final short[] DFA70_eot = DFA.unpackEncodedString(DFA70_eotS);
    static final short[] DFA70_eof = DFA.unpackEncodedString(DFA70_eofS);
    static final char[] DFA70_min = DFA.unpackEncodedStringToUnsignedChars(DFA70_minS);
    static final char[] DFA70_max = DFA.unpackEncodedStringToUnsignedChars(DFA70_maxS);
    static final short[] DFA70_accept = DFA.unpackEncodedString(DFA70_acceptS);
    static final short[] DFA70_special = DFA.unpackEncodedString(DFA70_specialS);
    static final short[][] DFA70_transition;

    static {
        int numStates = DFA70_transitionS.length;
        DFA70_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA70_transition[i] = DFA.unpackEncodedString(DFA70_transitionS[i]);
        }
    }

    class DFA70 extends DFA {

        public DFA70(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 70;
            this.eot = DFA70_eot;
            this.eof = DFA70_eof;
            this.min = DFA70_min;
            this.max = DFA70_max;
            this.accept = DFA70_accept;
            this.special = DFA70_special;
            this.transition = DFA70_transition;
        }
        public String getDescription() {
            return "269:1: like_predicate : ( row_value 'LIKE' row_value | v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA70_1 = input.LA(1);

                         
                        int index70_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA70_2 = input.LA(1);

                         
                        int index70_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA70_3 = input.LA(1);

                         
                        int index70_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA70_4 = input.LA(1);

                         
                        int index70_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA70_5 = input.LA(1);

                         
                        int index70_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA70_6 = input.LA(1);

                         
                        int index70_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA70_7 = input.LA(1);

                         
                        int index70_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA70_8 = input.LA(1);

                         
                        int index70_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA70_9 = input.LA(1);

                         
                        int index70_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA70_10 = input.LA(1);

                         
                        int index70_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA70_11 = input.LA(1);

                         
                        int index70_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA70_12 = input.LA(1);

                         
                        int index70_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA70_13 = input.LA(1);

                         
                        int index70_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA70_14 = input.LA(1);

                         
                        int index70_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_14);
                        if ( s>=0 ) return s;
                        break;
                    case 14 : 
                        int LA70_15 = input.LA(1);

                         
                        int index70_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_15);
                        if ( s>=0 ) return s;
                        break;
                    case 15 : 
                        int LA70_16 = input.LA(1);

                         
                        int index70_16 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_16);
                        if ( s>=0 ) return s;
                        break;
                    case 16 : 
                        int LA70_17 = input.LA(1);

                         
                        int index70_17 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_17);
                        if ( s>=0 ) return s;
                        break;
                    case 17 : 
                        int LA70_18 = input.LA(1);

                         
                        int index70_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_18);
                        if ( s>=0 ) return s;
                        break;
                    case 18 : 
                        int LA70_19 = input.LA(1);

                         
                        int index70_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_19);
                        if ( s>=0 ) return s;
                        break;
                    case 19 : 
                        int LA70_20 = input.LA(1);

                         
                        int index70_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_20);
                        if ( s>=0 ) return s;
                        break;
                    case 20 : 
                        int LA70_21 = input.LA(1);

                         
                        int index70_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_21);
                        if ( s>=0 ) return s;
                        break;
                    case 21 : 
                        int LA70_22 = input.LA(1);

                         
                        int index70_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_SQL92Query()) ) {s = 23;}

                        else if ( (true) ) {s = 24;}

                         
                        input.seek(index70_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 70, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_query_expression_in_statement315 = new BitSet(new long[]{0x0400030000000000L});
    public static final BitSet FOLLOW_order_by_in_statement317 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_limit_in_statement320 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_statement323 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_statement326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_limit363 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_INT_in_limit365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_query_expression391 = new BitSet(new long[]{0x0000340000000002L});
    public static final BitSet FOLLOW_set_op_in_query_expression394 = new BitSet(new long[]{0x0010400000000000L});
    public static final BitSet FOLLOW_query_in_query_expression397 = new BitSet(new long[]{0x0000340000000002L});
    public static final BitSet FOLLOW_42_in_set_op419 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_set_op421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_set_op437 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_set_op453 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_set_op455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_set_op471 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_set_op487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_query514 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_query524 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_set_quantifier_in_query526 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_select_list_in_query529 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_query531 = new BitSet(new long[]{0x0010000800000000L});
    public static final BitSet FOLLOW_table_expression_in_query533 = new BitSet(new long[]{0x0007000000000002L});
    public static final BitSet FOLLOW_48_in_query536 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_search_condition_in_query540 = new BitSet(new long[]{0x0006000000000002L});
    public static final BitSet FOLLOW_49_in_query545 = new BitSet(new long[]{0x8000000800000000L,0x00000000000001FFL});
    public static final BitSet FOLLOW_column_list_in_query547 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_query552 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_search_condition_in_query556 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_set_quantifier0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_sub_query659 = new BitSet(new long[]{0x0010400000000000L});
    public static final BitSet FOLLOW_query_expression_in_sub_query662 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_sub_query664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_select_list683 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_derived_column_in_select_list701 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_select_list704 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_derived_column_in_select_list707 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_56_in_derived_column727 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_derived_column729 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_57_in_derived_column731 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_derived_column735 = new BitSet(new long[]{0x0200000800000002L});
    public static final BitSet FOLLOW_57_in_derived_column738 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_derived_column743 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_derived_column774 = new BitSet(new long[]{0x0200000800000002L});
    public static final BitSet FOLLOW_57_in_derived_column777 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_derived_column780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_order_by814 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_59_in_order_by816 = new BitSet(new long[]{0x8000000C00000000L,0x00000000000001FFL});
    public static final BitSet FOLLOW_ordered_sort_spec_in_order_by818 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_order_by821 = new BitSet(new long[]{0x8000000C00000000L,0x00000000000001FFL});
    public static final BitSet FOLLOW_ordered_sort_spec_in_order_by823 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_column_name_in_sort_spec852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_sort_spec856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_sort_spec860 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sort_spec_in_ordered_sort_spec878 = new BitSet(new long[]{0x1000000000000000L});
    public static final BitSet FOLLOW_60_in_ordered_sort_spec880 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sort_spec_in_ordered_sort_spec898 = new BitSet(new long[]{0x2000000000000002L});
    public static final BitSet FOLLOW_61_in_ordered_sort_spec900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_reserved_word_column_name930 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_62_in_reserved_word_column_name931 = new BitSet(new long[]{0x8000000000000000L,0x00000000000001FFL});
    public static final BitSet FOLLOW_63_in_reserved_word_column_name937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_reserved_word_column_name943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_reserved_word_column_name949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_reserved_word_column_name955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_reserved_word_column_name961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_reserved_word_column_name967 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_reserved_word_column_name973 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_reserved_word_column_name979 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_reserved_word_column_name985 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_reserved_word_column_name991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_value_expression_in_value_expression1037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_value_expression_in_value_expression1047 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_factor_in_numeric_value_expression1066 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000600L});
    public static final BitSet FOLLOW_set_in_numeric_value_expression1069 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_factor_in_numeric_value_expression1076 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000600L});
    public static final BitSet FOLLOW_numeric_primary_in_factor1098 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_set_in_factor1101 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_numeric_primary_in_factor1108 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000800L});
    public static final BitSet FOLLOW_73_in_numeric_primary1129 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_74_in_numeric_primary1132 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_primary_in_numeric_primary1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_value_expression_primary1157 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_value_expression_primary1160 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_value_expression_primary1162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_value_expression_primary1173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_value_expression_primary1183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_value_expression_primary1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_value_expression_primary1203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_literal1221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literal1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMERIC_in_literal1229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal1233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_in_literal1237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interval_in_literal1241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_literal1245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_literal1249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_literal1253 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_datetime1271 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_STRING_in_datetime1284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_datetime1297 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_62_in_datetime1298 = new BitSet(new long[]{0x8000000000000000L,0x0000000000000003L});
    public static final BitSet FOLLOW_63_in_datetime1304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_datetime1310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_datetime1316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_interval1348 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_STRING_in_interval1351 = new BitSet(new long[]{0x0000000000000000L,0x00000000000001F8L});
    public static final BitSet FOLLOW_set_in_interval1353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_interval1388 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_62_in_interval1389 = new BitSet(new long[]{0x0000000000000000L,0x00000000000001FCL});
    public static final BitSet FOLLOW_66_in_interval1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_interval1401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_interval1407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_interval1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_70_in_interval1419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_interval1425 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_interval1431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function1469 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_function1472 = new BitSet(new long[]{0x81F8087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_function1474 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_55_in_function1478 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_function1480 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_53_in_function1484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_function1522 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_function1525 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_function1527 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_function1529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_string_value_expression1559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_STRING_in_string_value_expression1563 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_79_in_string_value_expression1567 = new BitSet(new long[]{0x0000004800000000L});
    public static final BitSet FOLLOW_column_name_in_string_value_expression1571 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_STRING_in_string_value_expression1575 = new BitSet(new long[]{0x0000000000000002L,0x0000000000008000L});
    public static final BitSet FOLLOW_table_reference_in_table_expression1596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_in_table_reference1614 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_table_reference1617 = new BitSet(new long[]{0x0010000800000000L});
    public static final BitSet FOLLOW_table_reference_in_table_reference1620 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_80_in_join_type1640 = new BitSet(new long[]{0x0000000000000000L,0x0000000000060000L});
    public static final BitSet FOLLOW_81_in_join_type1642 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_join_type1645 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_join_type1660 = new BitSet(new long[]{0x0000000000000000L,0x0000000000060000L});
    public static final BitSet FOLLOW_81_in_join_type1662 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_join_type1665 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_84_in_join_type1679 = new BitSet(new long[]{0x0000000000000000L,0x0000000000060000L});
    public static final BitSet FOLLOW_81_in_join_type1681 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_join_type1684 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_join_type1698 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_82_in_join_type1701 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_non_join_table_in_table1723 = new BitSet(new long[]{0x0000000000000002L,0x00000000003D0000L});
    public static final BitSet FOLLOW_join_type_in_table1726 = new BitSet(new long[]{0x0010000800000000L});
    public static final BitSet FOLLOW_non_join_table_in_table1729 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_table1731 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_search_condition_in_table1734 = new BitSet(new long[]{0x0000000000000002L,0x00000000003D0000L});
    public static final BitSet FOLLOW_table_name_in_non_join_table1754 = new BitSet(new long[]{0x0200000800000002L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_in_non_join_table1778 = new BitSet(new long[]{0x0200000800000000L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_non_join_table1800 = new BitSet(new long[]{0x0200000800000000L});
    public static final BitSet FOLLOW_correlation_specification_in_non_join_table1802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_table_function1832 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_table_function1834 = new BitSet(new long[]{0x81F8087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_table_function_subquery_in_table_function1836 = new BitSet(new long[]{0x81F8087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_55_in_table_function1840 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_table_function_subquery_in_table_function1842 = new BitSet(new long[]{0x81F8087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_55_in_table_function1847 = new BitSet(new long[]{0x81D8087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_table_function_param_in_table_function1850 = new BitSet(new long[]{0x81F8087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_53_in_table_function1854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_table_function_subquery1902 = new BitSet(new long[]{0x0200000800000000L});
    public static final BitSet FOLLOW_correlation_specification_in_table_function_subquery1904 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_search_condition_in_table_function_param1932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_table_function_param1942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_name_in_relation1964 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_in_relation1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_relation2000 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_factor_in_search_condition2026 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_87_in_search_condition2029 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_boolean_factor_in_search_condition2032 = new BitSet(new long[]{0x0000000000000002L,0x0000000000800000L});
    public static final BitSet FOLLOW_boolean_term_in_boolean_factor2052 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_boolean_factor2055 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_boolean_term_in_boolean_factor2058 = new BitSet(new long[]{0x0000000000000002L,0x0000000001000000L});
    public static final BitSet FOLLOW_boolean_test_in_boolean_term2078 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_boolean_term2088 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_boolean_term_in_boolean_term2090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_boolean_primary_in_boolean_test2116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_boolean_primary2134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_boolean_primary2138 = new BitSet(new long[]{0x8158087C00000000L,0x00000300220077FFL});
    public static final BitSet FOLLOW_search_condition_in_boolean_primary2141 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_boolean_primary2143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_predicate_in_predicate2165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_predicate_in_predicate2169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_predicate_in_predicate2173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_predicate_in_predicate2177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_predicate_in_predicate2181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_predicate_in_predicate2185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_null_predicate2203 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_90_in_null_predicate2205 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_null_predicate2207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_null_predicate2225 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_90_in_null_predicate2227 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_89_in_null_predicate2229 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_null_predicate2231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_in_predicate2261 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_89_in_in_predicate2263 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_91_in_in_predicate2265 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate2267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_in_predicate2303 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_91_in_in_predicate2305 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate2307 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_in_predicate_tail2347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_in_predicate_tail2358 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_in_predicate_tail2361 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_55_in_in_predicate_tail2364 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_in_predicate_tail2366 = new BitSet(new long[]{0x00A0000000000000L});
    public static final BitSet FOLLOW_53_in_in_predicate_tail2371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_between_predicate2400 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_between_predicate2402 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate2406 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_between_predicate2408 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_between_predicate2452 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_89_in_between_predicate2454 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_between_predicate2456 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate2460 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_between_predicate2462 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_between_predicate2466 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_93_in_exists_predicate2515 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_sub_query_in_exists_predicate2518 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bind_table_in_comparison_predicate2536 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_94_in_comparison_predicate2538 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2553 = new BitSet(new long[]{0x0000000000000000L,0x0000001FC0000000L});
    public static final BitSet FOLLOW_94_in_comparison_predicate2558 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_95_in_comparison_predicate2562 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_96_in_comparison_predicate2566 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_97_in_comparison_predicate2570 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_98_in_comparison_predicate2574 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_99_in_comparison_predicate2578 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_100_in_comparison_predicate2582 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_43_in_comparison_predicate2588 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_101_in_comparison_predicate2592 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_102_in_comparison_predicate2596 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2641 = new BitSet(new long[]{0x0000000000000000L,0x0000001FC0000000L});
    public static final BitSet FOLLOW_set_in_comparison_predicate2643 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2672 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_like_predicate2690 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_like_predicate2692 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_like_predicate2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_like_predicate2707 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_89_in_like_predicate2709 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_like_predicate2711 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_like_predicate2715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_row_value2749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_row_value2752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_row_value2756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_bind_table2774 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_bind_table2777 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_62_in_bind_table2778 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_bind_table2781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_correlation_specification2816 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_correlation_specification2821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_table_name2842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_column_list2861 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2865 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_column_list2869 = new BitSet(new long[]{0x8000000800000000L,0x00000000000001FFL});
    public static final BitSet FOLLOW_column_name_in_column_list2873 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2877 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_ID_in_column_name2901 = new BitSet(new long[]{0x4000000000000000L});
    public static final BitSet FOLLOW_62_in_column_name2902 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_column_name2907 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred39_SQL92Query1069 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_factor_in_synpred39_SQL92Query1076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred44_SQL92Query1157 = new BitSet(new long[]{0x8158087C00000000L,0x00000000000077FFL});
    public static final BitSet FOLLOW_value_expression_in_synpred44_SQL92Query1160 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_synpred44_SQL92Query1162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_synpred45_SQL92Query1173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_synpred46_SQL92Query1183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_synpred47_SQL92Query1193 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_synpred58_SQL92Query1271 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_STRING_in_synpred58_SQL92Query1284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_synpred67_SQL92Query1348 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_STRING_in_synpred67_SQL92Query1351 = new BitSet(new long[]{0x0000000000000000L,0x00000000000001F8L});
    public static final BitSet FOLLOW_set_in_synpred67_SQL92Query1353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred81_SQL92Query1617 = new BitSet(new long[]{0x0010000800000000L});
    public static final BitSet FOLLOW_table_reference_in_synpred81_SQL92Query1620 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_table_function_subquery_in_synpred93_SQL92Query1836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred94_SQL92Query1840 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_table_function_subquery_in_synpred94_SQL92Query1842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_search_condition_in_synpred97_SQL92Query1932 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_synpred103_SQL92Query2134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_comparison_predicate_in_synpred104_SQL92Query2165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_predicate_in_synpred105_SQL92Query2169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_predicate_in_synpred106_SQL92Query2173 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_predicate_in_synpred107_SQL92Query2177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred109_SQL92Query2203 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_90_in_synpred109_SQL92Query2205 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_76_in_synpred109_SQL92Query2207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred110_SQL92Query2261 = new BitSet(new long[]{0x0000000000000000L,0x0000000002000000L});
    public static final BitSet FOLLOW_89_in_synpred110_SQL92Query2263 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
    public static final BitSet FOLLOW_91_in_synpred110_SQL92Query2265 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_in_predicate_tail_in_synpred110_SQL92Query2267 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sub_query_in_synpred111_SQL92Query2347 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred113_SQL92Query2400 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
    public static final BitSet FOLLOW_92_in_synpred113_SQL92Query2402 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_synpred113_SQL92Query2406 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_88_in_synpred113_SQL92Query2408 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_synpred113_SQL92Query2412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred123_SQL92Query2553 = new BitSet(new long[]{0x0000000000000000L,0x0000001FC0000000L});
    public static final BitSet FOLLOW_94_in_synpred123_SQL92Query2558 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_95_in_synpred123_SQL92Query2562 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_96_in_synpred123_SQL92Query2566 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_97_in_synpred123_SQL92Query2570 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_98_in_synpred123_SQL92Query2574 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_99_in_synpred123_SQL92Query2578 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_100_in_synpred123_SQL92Query2582 = new BitSet(new long[]{0x0000080000000000L,0x0000006000000000L});
    public static final BitSet FOLLOW_43_in_synpred123_SQL92Query2588 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_101_in_synpred123_SQL92Query2592 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_102_in_synpred123_SQL92Query2596 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_synpred123_SQL92Query2601 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_row_value_in_synpred130_SQL92Query2690 = new BitSet(new long[]{0x0000000000000000L,0x0000008000000000L});
    public static final BitSet FOLLOW_103_in_synpred130_SQL92Query2692 = new BitSet(new long[]{0x8158087C00000000L,0x00000100000077FFL});
    public static final BitSet FOLLOW_row_value_in_synpred130_SQL92Query2695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_expression_in_synpred131_SQL92Query2749 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_synpred132_SQL92Query2752 = new BitSet(new long[]{0x0000000000000002L});

}