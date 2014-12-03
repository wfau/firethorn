// $ANTLR 3.2 Sep 23, 2009 12:02:23 /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g 2011-11-10 14:34:00

  package uk.org.ogsadai.parser.sql92query; 


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.antlr.stringtemplate.*;
import org.antlr.stringtemplate.language.*;
import java.util.HashMap;
public class SQL92QueryWalker extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STATEMENT", "QUERY", "SETOP", "ORDER", "SELECT_LIST", "FROM_LIST", "WHERE", "GROUP_BY", "HAVING", "RELATION", "COLUMN", "FUNCTION", "NOT", "SET", "TABLECOLUMN", "RIGHT_OUTER_JOIN", "LEFT_OUTER_JOIN", "FULL_OUTER_JOIN", "JOIN", "IS_NULL", "UNION", "EXCEPT", "UNION_ALL", "EXCEPT_ALL", "INTERSECT", "BOUND", "CAST", "ASC", "DESC", "LIMIT", "INT", "ID", "FLOAT", "NUMERIC", "STRING", "WS", "';'", "'LIMIT'", "'UNION'", "'ALL'", "'EXCEPT'", "'INTERSECT'", "'SELECT'", "'FROM'", "'WHERE'", "'GROUP BY'", "'HAVING'", "'DISTINCT'", "'('", "')'", "'*'", "','", "'CAST'", "'AS'", "'ORDER'", "'BY'", "'DESC'", "'ASC'", "'.'", "'DATE'", "'TIMESTAMP'", "'TIME'", "'INTERVAL'", "'YEAR'", "'MONTH'", "'DAY'", "'HOUR'", "'MINUTE'", "'SECOND'", "'+'", "'-'", "'/'", "'NULL'", "'TRUE'", "'FALSE'", "'||'", "'RIGHT'", "'OUTER'", "'JOIN'", "'LEFT'", "'FULL'", "'INNER'", "'ON'", "'OR'", "'AND'", "'NOT'", "'IS'", "'IN'", "'BETWEEN'", "'EXISTS'", "'='", "'<>'", "'!='", "'<'", "'>'", "'>='", "'<='", "'SOME'", "'ANY'", "'LIKE'", "'DEFAULT'", "'@'"
    };
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

    // delegates
    // delegators


        public SQL92QueryWalker(TreeNodeStream input) {
            this(input, new RecognizerSharedState());
        }
        public SQL92QueryWalker(TreeNodeStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected StringTemplateGroup templateLib =
      new StringTemplateGroup("SQL92QueryWalkerTemplates", AngleBracketTemplateLexer.class);

    public void setTemplateLib(StringTemplateGroup templateLib) {
      this.templateLib = templateLib;
    }
    public StringTemplateGroup getTemplateLib() {
      return templateLib;
    }
    /** allows convenient multi-value initialization:
     *  "new STAttrMap().put(...).put(...)"
     */
    public static class STAttrMap extends HashMap {
      public STAttrMap put(String attrName, Object value) {
        super.put(attrName, value);
        return this;
      }
      public STAttrMap put(String attrName, int value) {
        super.put(attrName, new Integer(value));
        return this;
      }
    }

    public String[] getTokenNames() { return SQL92QueryWalker.tokenNames; }
    public String getGrammarFileName() { return "/Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g"; }


    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
    }

    public Object recoverFromMismatchedSet(IntStream input, RecognitionException re, BitSet follow)
        throws RecognitionException
    {
        throw re;
    }


    public static class statement_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "statement"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:36:1: statement : ^( STATEMENT query_expression (o+= order_by )? ) -> statement(query=$query_expression.storder_by=$o);
    public final SQL92QueryWalker.statement_return statement() throws RecognitionException {
        SQL92QueryWalker.statement_return retval = new SQL92QueryWalker.statement_return();
        retval.start = input.LT(1);

        List list_o=null;
        SQL92QueryWalker.query_expression_return query_expression1 = null;

        RuleReturnScope o = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:37:5: ( ^( STATEMENT query_expression (o+= order_by )? ) -> statement(query=$query_expression.storder_by=$o))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:37:9: ^( STATEMENT query_expression (o+= order_by )? )
            {
            match(input,STATEMENT,FOLLOW_STATEMENT_in_statement87); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_query_expression_in_statement89);
            query_expression1=query_expression();

            state._fsp--;
            if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:37:39: (o+= order_by )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==ORDER) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:0:0: o+= order_by
                    {
                    pushFollow(FOLLOW_order_by_in_statement93);
                    o=order_by();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_o==null) list_o=new ArrayList();
                    list_o.add(o.getTemplate());


                    }
                    break;

            }


            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 37:52: -> statement(query=$query_expression.storder_by=$o)
              {
                  retval.st = templateLib.getInstanceOf("statement",
                new STAttrMap().put("query", (query_expression1!=null?query_expression1.st:null)).put("order_by", list_o));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class order_by_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "order_by"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:39:1: order_by : ^( ORDER (s+= sort_specification )+ ) -> order_by(columns=$s);
    public final SQL92QueryWalker.order_by_return order_by() throws RecognitionException {
        SQL92QueryWalker.order_by_return retval = new SQL92QueryWalker.order_by_return();
        retval.start = input.LT(1);

        List list_s=null;
        RuleReturnScope s = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:39:9: ( ^( ORDER (s+= sort_specification )+ ) -> order_by(columns=$s))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:39:13: ^( ORDER (s+= sort_specification )+ )
            {
            match(input,ORDER,FOLLOW_ORDER_in_order_by119); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:39:21: (s+= sort_specification )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=ASC && LA2_0<=DESC)) ) {
                    alt2=1;
                }


                switch (alt2) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:39:22: s+= sort_specification
                    {
                    pushFollow(FOLLOW_sort_specification_in_order_by124);
                    s=sort_specification();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_s==null) list_s=new ArrayList();
                    list_s.add(s.getTemplate());


                    }
                    break;

                default :
                    if ( cnt2 >= 1 ) break loop2;
                    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 39:47: -> order_by(columns=$s)
              {
                  retval.st = templateLib.getInstanceOf("order_by",
                new STAttrMap().put("columns", list_s));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "order_by"

    public static class query_expression_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "query_expression"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:41:1: query_expression : ( ^(op= UNION_ALL v1= query_expression v2= query_expression ) -> set_op_union_all(value1=$v1.stvalue2=$v2.st) | ^(op= EXCEPT_ALL v1= query_expression v2= query_expression ) -> set_op_except_all(value1=$v1.stvalue2=$v2.st) | ^( (op= UNION | op= EXCEPT | op= INTERSECT ) v1= query_expression v2= query_expression ) -> set_op(op=$opvalue1=$v1.stvalue2=$v2.st) | query -> {$query.st});
    public final SQL92QueryWalker.query_expression_return query_expression() throws RecognitionException {
        SQL92QueryWalker.query_expression_return retval = new SQL92QueryWalker.query_expression_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.query_expression_return v1 = null;

        SQL92QueryWalker.query_expression_return v2 = null;

        SQL92QueryWalker.query_return query2 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:42:5: ( ^(op= UNION_ALL v1= query_expression v2= query_expression ) -> set_op_union_all(value1=$v1.stvalue2=$v2.st) | ^(op= EXCEPT_ALL v1= query_expression v2= query_expression ) -> set_op_except_all(value1=$v1.stvalue2=$v2.st) | ^( (op= UNION | op= EXCEPT | op= INTERSECT ) v1= query_expression v2= query_expression ) -> set_op(op=$opvalue1=$v1.stvalue2=$v2.st) | query -> {$query.st})
            int alt4=4;
            switch ( input.LA(1) ) {
            case UNION_ALL:
                {
                alt4=1;
                }
                break;
            case EXCEPT_ALL:
                {
                alt4=2;
                }
                break;
            case UNION:
            case EXCEPT:
            case INTERSECT:
                {
                alt4=3;
                }
                break;
            case QUERY:
                {
                alt4=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:42:9: ^(op= UNION_ALL v1= query_expression v2= query_expression )
                    {
                    op=(CommonTree)match(input,UNION_ALL,FOLLOW_UNION_ALL_in_query_expression153); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression157);
                    v1=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression161);
                    v2=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 43:13: -> set_op_union_all(value1=$v1.stvalue2=$v2.st)
                      {
                          retval.st = templateLib.getInstanceOf("set_op_union_all",
                        new STAttrMap().put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:44:9: ^(op= EXCEPT_ALL v1= query_expression v2= query_expression )
                    {
                    op=(CommonTree)match(input,EXCEPT_ALL,FOLLOW_EXCEPT_ALL_in_query_expression201); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression205);
                    v1=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression209);
                    v2=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 45:13: -> set_op_except_all(value1=$v1.stvalue2=$v2.st)
                      {
                          retval.st = templateLib.getInstanceOf("set_op_except_all",
                        new STAttrMap().put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:46:9: ^( (op= UNION | op= EXCEPT | op= INTERSECT ) v1= query_expression v2= query_expression )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:46:11: (op= UNION | op= EXCEPT | op= INTERSECT )
                    int alt3=3;
                    switch ( input.LA(1) ) {
                    case UNION:
                        {
                        alt3=1;
                        }
                        break;
                    case EXCEPT:
                        {
                        alt3=2;
                        }
                        break;
                    case INTERSECT:
                        {
                        alt3=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                        throw nvae;
                    }

                    switch (alt3) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:46:12: op= UNION
                            {
                            op=(CommonTree)match(input,UNION,FOLLOW_UNION_in_query_expression250); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:46:23: op= EXCEPT
                            {
                            op=(CommonTree)match(input,EXCEPT,FOLLOW_EXCEPT_in_query_expression256); if (state.failed) return retval;

                            }
                            break;
                        case 3 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:46:35: op= INTERSECT
                            {
                            op=(CommonTree)match(input,INTERSECT,FOLLOW_INTERSECT_in_query_expression262); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression267);
                    v1=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_query_expression271);
                    v2=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 47:13: -> set_op(op=$opvalue1=$v1.stvalue2=$v2.st)
                      {
                          retval.st = templateLib.getInstanceOf("set_op",
                        new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:48:9: query
                    {
                    pushFollow(FOLLOW_query_in_query_expression314);
                    query2=query();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 48:15: -> {$query.st}
                      {
                          retval.st = (query2!=null?query2.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "query_expression"

    public static class query_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "query"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:51:1: query : ^( QUERY ^( SELECT_LIST (sq= 'DISTINCT' | sq= 'ALL' )? select_list ) ^( FROM_LIST (t+= table )+ ) ( ^( WHERE wh+= search_condition ) )? ( ^( GROUP_BY (c+= column_name )+ ) )? ( ^( HAVING hav+= search_condition ) )? ) -> query(select_list=$select_list.stfrom_list=$twhere=$whgroup_by=$chaving=$havset_quantifier=$sq);
    public final SQL92QueryWalker.query_return query() throws RecognitionException {
        SQL92QueryWalker.query_return retval = new SQL92QueryWalker.query_return();
        retval.start = input.LT(1);

        CommonTree sq=null;
        List list_t=null;
        List list_wh=null;
        List list_c=null;
        List list_hav=null;
        SQL92QueryWalker.select_list_return select_list3 = null;

        RuleReturnScope t = null;
        RuleReturnScope wh = null;
        RuleReturnScope c = null;
        RuleReturnScope hav = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:51:9: ( ^( QUERY ^( SELECT_LIST (sq= 'DISTINCT' | sq= 'ALL' )? select_list ) ^( FROM_LIST (t+= table )+ ) ( ^( WHERE wh+= search_condition ) )? ( ^( GROUP_BY (c+= column_name )+ ) )? ( ^( HAVING hav+= search_condition ) )? ) -> query(select_list=$select_list.stfrom_list=$twhere=$whgroup_by=$chaving=$havset_quantifier=$sq))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:51:13: ^( QUERY ^( SELECT_LIST (sq= 'DISTINCT' | sq= 'ALL' )? select_list ) ^( FROM_LIST (t+= table )+ ) ( ^( WHERE wh+= search_condition ) )? ( ^( GROUP_BY (c+= column_name )+ ) )? ( ^( HAVING hav+= search_condition ) )? )
            {
            match(input,QUERY,FOLLOW_QUERY_in_query340); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            match(input,SELECT_LIST,FOLLOW_SELECT_LIST_in_query356); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:52:27: (sq= 'DISTINCT' | sq= 'ALL' )?
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==51) ) {
                alt5=1;
            }
            else if ( (LA5_0==43) ) {
                alt5=2;
            }
            switch (alt5) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:52:28: sq= 'DISTINCT'
                    {
                    sq=(CommonTree)match(input,51,FOLLOW_51_in_query361); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:52:44: sq= 'ALL'
                    {
                    sq=(CommonTree)match(input,43,FOLLOW_43_in_query367); if (state.failed) return retval;

                    }
                    break;

            }

            pushFollow(FOLLOW_select_list_in_query371);
            select_list3=select_list();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;
            match(input,FROM_LIST,FOLLOW_FROM_LIST_in_query388); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:53:25: (t+= table )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==RELATION||(LA6_0>=RIGHT_OUTER_JOIN && LA6_0<=JOIN)) ) {
                    alt6=1;
                }


                switch (alt6) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:53:26: t+= table
                    {
                    pushFollow(FOLLOW_table_in_query393);
                    t=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_t==null) list_t=new ArrayList();
                    list_t.add(t.getTemplate());


                    }
                    break;

                default :
                    if ( cnt6 >= 1 ) break loop6;
                    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:54:13: ( ^( WHERE wh+= search_condition ) )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==WHERE) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:54:14: ^( WHERE wh+= search_condition )
                    {
                    match(input,WHERE,FOLLOW_WHERE_in_query413); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_query417);
                    wh=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_wh==null) list_wh=new ArrayList();
                    list_wh.add(wh.getTemplate());


                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }

            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:55:13: ( ^( GROUP_BY (c+= column_name )+ ) )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==GROUP_BY) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:55:14: ^( GROUP_BY (c+= column_name )+ )
                    {
                    match(input,GROUP_BY,FOLLOW_GROUP_BY_in_query437); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:55:26: (c+= column_name )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        int LA8_0 = input.LA(1);

                        if ( (LA8_0==TABLECOLUMN) ) {
                            alt8=1;
                        }


                        switch (alt8) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:0:0: c+= column_name
                            {
                            pushFollow(FOLLOW_column_name_in_query441);
                            c=column_name();

                            state._fsp--;
                            if (state.failed) return retval;
                            if (list_c==null) list_c=new ArrayList();
                            list_c.add(c.getTemplate());


                            }
                            break;

                        default :
                            if ( cnt8 >= 1 ) break loop8;
                            if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(8, input);
                                throw eee;
                        }
                        cnt8++;
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }

            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:56:13: ( ^( HAVING hav+= search_condition ) )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==HAVING) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:56:14: ^( HAVING hav+= search_condition )
                    {
                    match(input,HAVING,FOLLOW_HAVING_in_query462); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_query466);
                    hav=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_hav==null) list_hav=new ArrayList();
                    list_hav.add(hav.getTemplate());


                    match(input, Token.UP, null); if (state.failed) return retval;

                    }
                    break;

            }


            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 58:13: -> query(select_list=$select_list.stfrom_list=$twhere=$whgroup_by=$chaving=$havset_quantifier=$sq)
              {
                  retval.st = templateLib.getInstanceOf("query",
                new STAttrMap().put("select_list", (select_list3!=null?select_list3.st:null)).put("from_list", list_t).put("where", list_wh).put("group_by", list_c).put("having", list_hav).put("set_quantifier", sq));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "query"

    public static class select_list_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "select_list"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:62:1: select_list : ( ^( COLUMN s= '*' ) -> emitstr(str=$s) | (c+= column_def )+ -> column_list(columns=$c));
    public final SQL92QueryWalker.select_list_return select_list() throws RecognitionException {
        SQL92QueryWalker.select_list_return retval = new SQL92QueryWalker.select_list_return();
        retval.start = input.LT(1);

        CommonTree s=null;
        List list_c=null;
        RuleReturnScope c = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:63:5: ( ^( COLUMN s= '*' ) -> emitstr(str=$s) | (c+= column_def )+ -> column_list(columns=$c))
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==COLUMN) ) {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==DOWN) ) {
                    int LA12_2 = input.LA(3);

                    if ( (LA12_2==54) ) {
                        int LA12_3 = input.LA(4);

                        if ( (LA12_3==UP) ) {
                            alt12=1;
                        }
                        else if ( (LA12_3==DOWN) ) {
                            alt12=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 12, 3, input);

                            throw nvae;
                        }
                    }
                    else if ( (LA12_2==QUERY||LA12_2==FUNCTION||LA12_2==TABLECOLUMN||(LA12_2>=UNION && LA12_2<=INTERSECT)||LA12_2==INT||(LA12_2>=FLOAT && LA12_2<=STRING)||(LA12_2>=63 && LA12_2<=66)||(LA12_2>=73 && LA12_2<=79)) ) {
                        alt12=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 12, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:63:9: ^( COLUMN s= '*' )
                    {
                    match(input,COLUMN,FOLLOW_COLUMN_in_select_list558); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    s=(CommonTree)match(input,54,FOLLOW_54_in_select_list562); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 63:28: -> emitstr(str=$s)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", s));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:64:9: (c+= column_def )+
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:64:9: (c+= column_def )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COLUMN) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:64:10: c+= column_def
                            {
                            pushFollow(FOLLOW_column_def_in_select_list588);
                            c=column_def();

                            state._fsp--;
                            if (state.failed) return retval;
                            if (list_c==null) list_c=new ArrayList();
                            list_c.add(c.getTemplate());


                            }
                            break;

                        default :
                            if ( cnt11 >= 1 ) break loop11;
                            if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);



                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 64:28: -> column_list(columns=$c)
                      {
                          retval.st = templateLib.getInstanceOf("column_list",
                        new STAttrMap().put("columns", list_c));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "select_list"

    public static class column_def_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "column_def"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:66:1: column_def : ( ^( COLUMN value_expression ) -> {$value_expression.st} | ^( COLUMN value_expression ID ) -> expr_as(expression=$value_expression.stalias=$ID));
    public final SQL92QueryWalker.column_def_return column_def() throws RecognitionException {
        SQL92QueryWalker.column_def_return retval = new SQL92QueryWalker.column_def_return();
        retval.start = input.LT(1);

        CommonTree ID6=null;
        SQL92QueryWalker.value_expression_return value_expression4 = null;

        SQL92QueryWalker.value_expression_return value_expression5 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:67:5: ( ^( COLUMN value_expression ) -> {$value_expression.st} | ^( COLUMN value_expression ID ) -> expr_as(expression=$value_expression.stalias=$ID))
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==COLUMN) ) {
                int LA13_1 = input.LA(2);

                if ( (synpred17_SQL92QueryWalker()) ) {
                    alt13=1;
                }
                else if ( (true) ) {
                    alt13=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 13, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:67:9: ^( COLUMN value_expression )
                    {
                    match(input,COLUMN,FOLLOW_COLUMN_in_column_def620); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_value_expression_in_column_def622);
                    value_expression4=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 67:39: -> {$value_expression.st}
                      {
                          retval.st = (value_expression4!=null?value_expression4.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:68:9: ^( COLUMN value_expression ID )
                    {
                    match(input,COLUMN,FOLLOW_COLUMN_in_column_def641); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_value_expression_in_column_def643);
                    value_expression5=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    ID6=(CommonTree)match(input,ID,FOLLOW_ID_in_column_def645); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 68:39: -> expr_as(expression=$value_expression.stalias=$ID)
                      {
                          retval.st = templateLib.getInstanceOf("expr_as",
                        new STAttrMap().put("expression", (value_expression5!=null?value_expression5.st:null)).put("alias", ID6));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "column_def"

    public static class sort_specification_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "sort_specification"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:70:1: sort_specification : ( ^( (spec= ASC | spec= DESC ) column_name ) -> emitstr2(str2=$specstr1=$column_name.st) | ^( (spec= ASC | spec= DESC ) INT ) -> emitstr2(str2=$specstr1=$INT));
    public final SQL92QueryWalker.sort_specification_return sort_specification() throws RecognitionException {
        SQL92QueryWalker.sort_specification_return retval = new SQL92QueryWalker.sort_specification_return();
        retval.start = input.LT(1);

        CommonTree spec=null;
        CommonTree INT8=null;
        SQL92QueryWalker.column_name_return column_name7 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:71:5: ( ^( (spec= ASC | spec= DESC ) column_name ) -> emitstr2(str2=$specstr1=$column_name.st) | ^( (spec= ASC | spec= DESC ) INT ) -> emitstr2(str2=$specstr1=$INT))
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==ASC) ) {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==DOWN) ) {
                    int LA16_3 = input.LA(3);

                    if ( (LA16_3==INT) ) {
                        alt16=2;
                    }
                    else if ( (LA16_3==TABLECOLUMN) ) {
                        alt16=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA16_0==DESC) ) {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==DOWN) ) {
                    int LA16_3 = input.LA(3);

                    if ( (LA16_3==INT) ) {
                        alt16=2;
                    }
                    else if ( (LA16_3==TABLECOLUMN) ) {
                        alt16=1;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:71:9: ^( (spec= ASC | spec= DESC ) column_name )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:71:11: (spec= ASC | spec= DESC )
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0==ASC) ) {
                        alt14=1;
                    }
                    else if ( (LA14_0==DESC) ) {
                        alt14=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }
                    switch (alt14) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:71:12: spec= ASC
                            {
                            spec=(CommonTree)match(input,ASC,FOLLOW_ASC_in_sort_specification682); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:71:23: spec= DESC
                            {
                            spec=(CommonTree)match(input,DESC,FOLLOW_DESC_in_sort_specification688); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_column_name_in_sort_specification691);
                    column_name7=column_name();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 71:48: -> emitstr2(str2=$specstr1=$column_name.st)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr2",
                        new STAttrMap().put("str2", spec).put("str1", (column_name7!=null?column_name7.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:72:9: ^( (spec= ASC | spec= DESC ) INT )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:72:11: (spec= ASC | spec= DESC )
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==ASC) ) {
                        alt15=1;
                    }
                    else if ( (LA15_0==DESC) ) {
                        alt15=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 15, 0, input);

                        throw nvae;
                    }
                    switch (alt15) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:72:12: spec= ASC
                            {
                            spec=(CommonTree)match(input,ASC,FOLLOW_ASC_in_sort_specification721); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:72:23: spec= DESC
                            {
                            spec=(CommonTree)match(input,DESC,FOLLOW_DESC_in_sort_specification727); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    INT8=(CommonTree)match(input,INT,FOLLOW_INT_in_sort_specification730); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 72:48: -> emitstr2(str2=$specstr1=$INT)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr2",
                        new STAttrMap().put("str2", spec).put("str1", INT8));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "sort_specification"

    public static class value_expression_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "value_expression"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:74:1: value_expression : ( string_value_expression -> {$string_value_expression.st} | numeric_value_expression -> {$numeric_value_expression.st});
    public final SQL92QueryWalker.value_expression_return value_expression() throws RecognitionException {
        SQL92QueryWalker.value_expression_return retval = new SQL92QueryWalker.value_expression_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.string_value_expression_return string_value_expression9 = null;

        SQL92QueryWalker.numeric_value_expression_return numeric_value_expression10 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:75:5: ( string_value_expression -> {$string_value_expression.st} | numeric_value_expression -> {$numeric_value_expression.st})
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==79) ) {
                alt17=1;
            }
            else if ( (LA17_0==QUERY||LA17_0==FUNCTION||LA17_0==TABLECOLUMN||(LA17_0>=UNION && LA17_0<=INTERSECT)||LA17_0==INT||(LA17_0>=FLOAT && LA17_0<=STRING)||LA17_0==54||(LA17_0>=63 && LA17_0<=66)||(LA17_0>=73 && LA17_0<=78)) ) {
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
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:75:9: string_value_expression
                    {
                    pushFollow(FOLLOW_string_value_expression_in_value_expression772);
                    string_value_expression9=string_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 75:34: -> {$string_value_expression.st}
                      {
                          retval.st = (string_value_expression9!=null?string_value_expression9.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:76:9: numeric_value_expression
                    {
                    pushFollow(FOLLOW_numeric_value_expression_in_value_expression787);
                    numeric_value_expression10=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 76:34: -> {$numeric_value_expression.st}
                      {
                          retval.st = (numeric_value_expression10!=null?numeric_value_expression10.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value_expression"

    public static class numeric_value_expression_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "numeric_value_expression"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:78:1: numeric_value_expression : ( ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr_paren(op=$opvalue1=$v1.stvalue2=$v2.st) | ^( (op= '/' | op= '*' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st) | numeric_primary -> {$numeric_primary.st});
    public final SQL92QueryWalker.numeric_value_expression_return numeric_value_expression() throws RecognitionException {
        SQL92QueryWalker.numeric_value_expression_return retval = new SQL92QueryWalker.numeric_value_expression_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.numeric_value_expression_return v1 = null;

        SQL92QueryWalker.numeric_value_expression_return v2 = null;

        SQL92QueryWalker.numeric_primary_return numeric_primary11 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:5: ( ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr_paren(op=$opvalue1=$v1.stvalue2=$v2.st) | ^( (op= '/' | op= '*' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st) | numeric_primary -> {$numeric_primary.st})
            int alt20=3;
            alt20 = dfa20.predict(input);
            switch (alt20) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:9: ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:11: (op= '+' | op= '-' )
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==73) ) {
                        alt18=1;
                    }
                    else if ( (LA18_0==74) ) {
                        alt18=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 18, 0, input);

                        throw nvae;
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:12: op= '+'
                            {
                            op=(CommonTree)match(input,73,FOLLOW_73_in_numeric_value_expression813); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:21: op= '-'
                            {
                            op=(CommonTree)match(input,74,FOLLOW_74_in_numeric_value_expression819); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_numeric_value_expression_in_numeric_value_expression825);
                    v1=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_numeric_value_expression_in_numeric_value_expression829);
                    v2=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 80:13: -> bin_expr_paren(op=$opvalue1=$v1.stvalue2=$v2.st)
                      {
                          retval.st = templateLib.getInstanceOf("bin_expr_paren",
                        new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:81:9: ^( (op= '/' | op= '*' ) v1= numeric_value_expression v2= numeric_value_expression )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:81:11: (op= '/' | op= '*' )
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==75) ) {
                        alt19=1;
                    }
                    else if ( (LA19_0==54) ) {
                        alt19=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                        throw nvae;
                    }
                    switch (alt19) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:81:12: op= '/'
                            {
                            op=(CommonTree)match(input,75,FOLLOW_75_in_numeric_value_expression876); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:81:21: op= '*'
                            {
                            op=(CommonTree)match(input,54,FOLLOW_54_in_numeric_value_expression882); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_numeric_value_expression_in_numeric_value_expression887);
                    v1=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_numeric_value_expression_in_numeric_value_expression891);
                    v2=numeric_value_expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 82:13: -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st)
                      {
                          retval.st = templateLib.getInstanceOf("bin_expr",
                        new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:83:9: numeric_primary
                    {
                    pushFollow(FOLLOW_numeric_primary_in_numeric_value_expression934);
                    numeric_primary11=numeric_primary();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 83:25: -> {$numeric_primary.st}
                      {
                          retval.st = (numeric_primary11!=null?numeric_primary11.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "numeric_value_expression"

    public static class numeric_primary_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "numeric_primary"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:86:1: numeric_primary : ( ^(op= ( '+' | '-' ) value_expression_primary ) -> unary_expr(op=$opvalue=$value_expression_primary.st) | value_expression_primary -> {$value_expression_primary.st});
    public final SQL92QueryWalker.numeric_primary_return numeric_primary() throws RecognitionException {
        SQL92QueryWalker.numeric_primary_return retval = new SQL92QueryWalker.numeric_primary_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.value_expression_primary_return value_expression_primary12 = null;

        SQL92QueryWalker.value_expression_primary_return value_expression_primary13 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:87:5: ( ^(op= ( '+' | '-' ) value_expression_primary ) -> unary_expr(op=$opvalue=$value_expression_primary.st) | value_expression_primary -> {$value_expression_primary.st})
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( ((LA22_0>=73 && LA22_0<=74)) ) {
                alt22=1;
            }
            else if ( (LA22_0==QUERY||LA22_0==FUNCTION||LA22_0==TABLECOLUMN||(LA22_0>=UNION && LA22_0<=INTERSECT)||LA22_0==INT||(LA22_0>=FLOAT && LA22_0<=STRING)||(LA22_0>=63 && LA22_0<=66)||(LA22_0>=76 && LA22_0<=78)) ) {
                alt22=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:87:9: ^(op= ( '+' | '-' ) value_expression_primary )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:87:14: ( '+' | '-' )
                    int alt21=2;
                    int LA21_0 = input.LA(1);

                    if ( (LA21_0==73) ) {
                        alt21=1;
                    }
                    else if ( (LA21_0==74) ) {
                        alt21=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 0, input);

                        throw nvae;
                    }
                    switch (alt21) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:87:15: '+'
                            {
                            match(input,73,FOLLOW_73_in_numeric_primary965); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:87:21: '-'
                            {
                            match(input,74,FOLLOW_74_in_numeric_primary969); if (state.failed) return retval;

                            }
                            break;

                    }


                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_value_expression_primary_in_numeric_primary972);
                    value_expression_primary12=value_expression_primary();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 87:52: -> unary_expr(op=$opvalue=$value_expression_primary.st)
                      {
                          retval.st = templateLib.getInstanceOf("unary_expr",
                        new STAttrMap().put("op", op).put("value", (value_expression_primary12!=null?value_expression_primary12.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:88:9: value_expression_primary
                    {
                    pushFollow(FOLLOW_value_expression_primary_in_numeric_primary997);
                    value_expression_primary13=value_expression_primary();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 88:34: -> {$value_expression_primary.st}
                      {
                          retval.st = (value_expression_primary13!=null?value_expression_primary13.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "numeric_primary"

    public static class value_expression_primary_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "value_expression_primary"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:90:1: value_expression_primary : ( function -> {$function.st} | column_name -> {$column_name.st} | literal -> {$literal.st} | query_expression -> paren(str=$query_expression.st));
    public final SQL92QueryWalker.value_expression_primary_return value_expression_primary() throws RecognitionException {
        SQL92QueryWalker.value_expression_primary_return retval = new SQL92QueryWalker.value_expression_primary_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.function_return function14 = null;

        SQL92QueryWalker.column_name_return column_name15 = null;

        SQL92QueryWalker.literal_return literal16 = null;

        SQL92QueryWalker.query_expression_return query_expression17 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:91:5: ( function -> {$function.st} | column_name -> {$column_name.st} | literal -> {$literal.st} | query_expression -> paren(str=$query_expression.st))
            int alt23=4;
            switch ( input.LA(1) ) {
            case FUNCTION:
                {
                alt23=1;
                }
                break;
            case TABLECOLUMN:
                {
                alt23=2;
                }
                break;
            case INT:
            case FLOAT:
            case NUMERIC:
            case STRING:
            case 63:
            case 64:
            case 65:
            case 66:
            case 76:
            case 77:
            case 78:
                {
                alt23=3;
                }
                break;
            case QUERY:
            case UNION:
            case EXCEPT:
            case UNION_ALL:
            case EXCEPT_ALL:
            case INTERSECT:
                {
                alt23=4;
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
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:91:9: function
                    {
                    pushFollow(FOLLOW_function_in_value_expression_primary1017);
                    function14=function();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 91:18: -> {$function.st}
                      {
                          retval.st = (function14!=null?function14.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:92:9: column_name
                    {
                    pushFollow(FOLLOW_column_name_in_value_expression_primary1031);
                    column_name15=column_name();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 92:21: -> {$column_name.st}
                      {
                          retval.st = (column_name15!=null?column_name15.st:null);
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:93:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_value_expression_primary1045);
                    literal16=literal();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 93:17: -> {$literal.st}
                      {
                          retval.st = (literal16!=null?literal16.st:null);
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:94:9: query_expression
                    {
                    pushFollow(FOLLOW_query_expression_in_value_expression_primary1059);
                    query_expression17=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 94:26: -> paren(str=$query_expression.st)
                      {
                          retval.st = templateLib.getInstanceOf("paren",
                        new STAttrMap().put("str", (query_expression17!=null?query_expression17.st:null)));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value_expression_primary"

    public static class function_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "function"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:97:1: function : ( ^( FUNCTION ID (param+= value_expression )+ ) -> function(name=$IDparam=$param) | ^( FUNCTION ID p= '*' ) -> function(name=$IDparam=$p));
    public final SQL92QueryWalker.function_return function() throws RecognitionException {
        SQL92QueryWalker.function_return retval = new SQL92QueryWalker.function_return();
        retval.start = input.LT(1);

        CommonTree p=null;
        CommonTree ID18=null;
        CommonTree ID19=null;
        List list_param=null;
        RuleReturnScope param = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:98:5: ( ^( FUNCTION ID (param+= value_expression )+ ) -> function(name=$IDparam=$param) | ^( FUNCTION ID p= '*' ) -> function(name=$IDparam=$p))
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==FUNCTION) ) {
                int LA25_1 = input.LA(2);

                if ( (LA25_1==DOWN) ) {
                    int LA25_2 = input.LA(3);

                    if ( (LA25_2==ID) ) {
                        int LA25_3 = input.LA(4);

                        if ( (LA25_3==54) ) {
                            int LA25_4 = input.LA(5);

                            if ( (LA25_4==UP) ) {
                                alt25=2;
                            }
                            else if ( (LA25_4==DOWN) ) {
                                alt25=1;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return retval;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 25, 4, input);

                                throw nvae;
                            }
                        }
                        else if ( (LA25_3==QUERY||LA25_3==FUNCTION||LA25_3==TABLECOLUMN||(LA25_3>=UNION && LA25_3<=INTERSECT)||LA25_3==INT||(LA25_3>=FLOAT && LA25_3<=STRING)||(LA25_3>=63 && LA25_3<=66)||(LA25_3>=73 && LA25_3<=79)) ) {
                            alt25=1;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 25, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 25, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:98:9: ^( FUNCTION ID (param+= value_expression )+ )
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_function1092); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    ID18=(CommonTree)match(input,ID,FOLLOW_ID_in_function1094); if (state.failed) return retval;
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:98:23: (param+= value_expression )+
                    int cnt24=0;
                    loop24:
                    do {
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( (LA24_0==QUERY||LA24_0==FUNCTION||LA24_0==TABLECOLUMN||(LA24_0>=UNION && LA24_0<=INTERSECT)||LA24_0==INT||(LA24_0>=FLOAT && LA24_0<=STRING)||LA24_0==54||(LA24_0>=63 && LA24_0<=66)||(LA24_0>=73 && LA24_0<=79)) ) {
                            alt24=1;
                        }


                        switch (alt24) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:98:24: param+= value_expression
                            {
                            pushFollow(FOLLOW_value_expression_in_function1099);
                            param=value_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if (list_param==null) list_param=new ArrayList();
                            list_param.add(param.getTemplate());


                            }
                            break;

                        default :
                            if ( cnt24 >= 1 ) break loop24;
                            if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(24, input);
                                throw eee;
                        }
                        cnt24++;
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 98:51: -> function(name=$IDparam=$param)
                      {
                          retval.st = templateLib.getInstanceOf("function",
                        new STAttrMap().put("name", ID18).put("param", list_param));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:99:9: ^( FUNCTION ID p= '*' )
                    {
                    match(input,FUNCTION,FOLLOW_FUNCTION_in_function1127); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    ID19=(CommonTree)match(input,ID,FOLLOW_ID_in_function1129); if (state.failed) return retval;
                    p=(CommonTree)match(input,54,FOLLOW_54_in_function1133); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 99:30: -> function(name=$IDparam=$p)
                      {
                          retval.st = templateLib.getInstanceOf("function",
                        new STAttrMap().put("name", ID19).put("param", p));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "function"

    public static class literal_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "literal"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:102:1: literal : ( INT -> emitstr(str=$INT.text) | FLOAT -> emitstr(str=$FLOAT.text) | NUMERIC -> emitstr(str=$NUMERIC.text) | STRING -> emitstr(str=$STRING.text) | datetime -> {$datetime.st} | interval -> {$interval.st} | (str= 'TRUE' | str= 'FALSE' | str= 'NULL' ) -> emitstr(str=$str));
    public final SQL92QueryWalker.literal_return literal() throws RecognitionException {
        SQL92QueryWalker.literal_return retval = new SQL92QueryWalker.literal_return();
        retval.start = input.LT(1);

        CommonTree str=null;
        CommonTree INT20=null;
        CommonTree FLOAT21=null;
        CommonTree NUMERIC22=null;
        CommonTree STRING23=null;
        SQL92QueryWalker.datetime_return datetime24 = null;

        SQL92QueryWalker.interval_return interval25 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:102:9: ( INT -> emitstr(str=$INT.text) | FLOAT -> emitstr(str=$FLOAT.text) | NUMERIC -> emitstr(str=$NUMERIC.text) | STRING -> emitstr(str=$STRING.text) | datetime -> {$datetime.st} | interval -> {$interval.st} | (str= 'TRUE' | str= 'FALSE' | str= 'NULL' ) -> emitstr(str=$str))
            int alt27=7;
            switch ( input.LA(1) ) {
            case INT:
                {
                alt27=1;
                }
                break;
            case FLOAT:
                {
                alt27=2;
                }
                break;
            case NUMERIC:
                {
                alt27=3;
                }
                break;
            case STRING:
                {
                alt27=4;
                }
                break;
            case 63:
            case 64:
            case 65:
                {
                alt27=5;
                }
                break;
            case 66:
                {
                alt27=6;
                }
                break;
            case 76:
            case 77:
            case 78:
                {
                alt27=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:102:13: INT
                    {
                    INT20=(CommonTree)match(input,INT,FOLLOW_INT_in_literal1167); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 102:17: -> emitstr(str=$INT.text)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", (INT20!=null?INT20.getText():null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:103:9: FLOAT
                    {
                    FLOAT21=(CommonTree)match(input,FLOAT,FOLLOW_FLOAT_in_literal1186); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 103:16: -> emitstr(str=$FLOAT.text)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", (FLOAT21!=null?FLOAT21.getText():null)));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:104:9: NUMERIC
                    {
                    NUMERIC22=(CommonTree)match(input,NUMERIC,FOLLOW_NUMERIC_in_literal1206); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 104:17: -> emitstr(str=$NUMERIC.text)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", (NUMERIC22!=null?NUMERIC22.getText():null)));
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:105:9: STRING
                    {
                    STRING23=(CommonTree)match(input,STRING,FOLLOW_STRING_in_literal1225); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 105:16: -> emitstr(str=$STRING.text)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", (STRING23!=null?STRING23.getText():null)));
                      }

                    }
                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:106:9: datetime
                    {
                    pushFollow(FOLLOW_datetime_in_literal1244);
                    datetime24=datetime();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 106:18: -> {$datetime.st}
                      {
                          retval.st = (datetime24!=null?datetime24.st:null);
                      }

                    }
                    }
                    break;
                case 6 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:107:9: interval
                    {
                    pushFollow(FOLLOW_interval_in_literal1258);
                    interval25=interval();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 107:18: -> {$interval.st}
                      {
                          retval.st = (interval25!=null?interval25.st:null);
                      }

                    }
                    }
                    break;
                case 7 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:108:9: (str= 'TRUE' | str= 'FALSE' | str= 'NULL' )
                    {
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:108:9: (str= 'TRUE' | str= 'FALSE' | str= 'NULL' )
                    int alt26=3;
                    switch ( input.LA(1) ) {
                    case 77:
                        {
                        alt26=1;
                        }
                        break;
                    case 78:
                        {
                        alt26=2;
                        }
                        break;
                    case 76:
                        {
                        alt26=3;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 26, 0, input);

                        throw nvae;
                    }

                    switch (alt26) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:108:11: str= 'TRUE'
                            {
                            str=(CommonTree)match(input,77,FOLLOW_77_in_literal1276); if (state.failed) return retval;

                            }
                            break;
                        case 2 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:108:24: str= 'FALSE'
                            {
                            str=(CommonTree)match(input,78,FOLLOW_78_in_literal1282); if (state.failed) return retval;

                            }
                            break;
                        case 3 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:108:38: str= 'NULL'
                            {
                            str=(CommonTree)match(input,76,FOLLOW_76_in_literal1288); if (state.failed) return retval;

                            }
                            break;

                    }



                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 108:51: -> emitstr(str=$str)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", str));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "literal"

    public static class string_value_expression_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "string_value_expression"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:111:1: string_value_expression : ^(op= '||' v1= string_primary v2= string_primary ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st);
    public final SQL92QueryWalker.string_value_expression_return string_value_expression() throws RecognitionException {
        SQL92QueryWalker.string_value_expression_return retval = new SQL92QueryWalker.string_value_expression_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.string_primary_return v1 = null;

        SQL92QueryWalker.string_primary_return v2 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:112:5: ( ^(op= '||' v1= string_primary v2= string_primary ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:112:9: ^(op= '||' v1= string_primary v2= string_primary )
            {
            op=(CommonTree)match(input,79,FOLLOW_79_in_string_value_expression1324); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_string_primary_in_string_value_expression1328);
            v1=string_primary();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_string_primary_in_string_value_expression1332);
            v2=string_primary();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 112:56: -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st)
              {
                  retval.st = templateLib.getInstanceOf("bin_expr",
                new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string_value_expression"

    public static class string_primary_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "string_primary"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:113:1: string_primary : ( column_name -> {$column_name.st} | STRING -> emitstr(str=$STRING.text));
    public final SQL92QueryWalker.string_primary_return string_primary() throws RecognitionException {
        SQL92QueryWalker.string_primary_return retval = new SQL92QueryWalker.string_primary_return();
        retval.start = input.LT(1);

        CommonTree STRING27=null;
        SQL92QueryWalker.column_name_return column_name26 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:114:5: ( column_name -> {$column_name.st} | STRING -> emitstr(str=$STRING.text))
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==TABLECOLUMN) ) {
                alt28=1;
            }
            else if ( (LA28_0==STRING) ) {
                alt28=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:114:9: column_name
                    {
                    pushFollow(FOLLOW_column_name_in_string_primary1365);
                    column_name26=column_name();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 114:21: -> {$column_name.st}
                      {
                          retval.st = (column_name26!=null?column_name26.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:115:9: STRING
                    {
                    STRING27=(CommonTree)match(input,STRING,FOLLOW_STRING_in_string_primary1379); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 115:16: -> emitstr(str=$STRING.text)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", (STRING27!=null?STRING27.getText():null)));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string_primary"

    public static class datetime_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "datetime"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:117:1: datetime : ^( (op= 'DATE' | op= 'TIME' | op= 'TIMESTAMP' ) STRING ) -> unary_expr(op=$opvalue=$STRING);
    public final SQL92QueryWalker.datetime_return datetime() throws RecognitionException {
        SQL92QueryWalker.datetime_return retval = new SQL92QueryWalker.datetime_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        CommonTree STRING28=null;

        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:5: ( ^( (op= 'DATE' | op= 'TIME' | op= 'TIMESTAMP' ) STRING ) -> unary_expr(op=$opvalue=$STRING))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:9: ^( (op= 'DATE' | op= 'TIME' | op= 'TIMESTAMP' ) STRING )
            {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:11: (op= 'DATE' | op= 'TIME' | op= 'TIMESTAMP' )
            int alt29=3;
            switch ( input.LA(1) ) {
            case 63:
                {
                alt29=1;
                }
                break;
            case 65:
                {
                alt29=2;
                }
                break;
            case 64:
                {
                alt29=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:12: op= 'DATE'
                    {
                    op=(CommonTree)match(input,63,FOLLOW_63_in_datetime1410); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:24: op= 'TIME'
                    {
                    op=(CommonTree)match(input,65,FOLLOW_65_in_datetime1416); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:118:36: op= 'TIMESTAMP'
                    {
                    op=(CommonTree)match(input,64,FOLLOW_64_in_datetime1422); if (state.failed) return retval;

                    }
                    break;

            }


            match(input, Token.DOWN, null); if (state.failed) return retval;
            STRING28=(CommonTree)match(input,STRING,FOLLOW_STRING_in_datetime1425); if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 118:60: -> unary_expr(op=$opvalue=$STRING)
              {
                  retval.st = templateLib.getInstanceOf("unary_expr",
                new STAttrMap().put("op", op).put("value", STRING28));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "datetime"

    public static class interval_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "interval"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:1: interval : ^( 'INTERVAL' STRING (f= 'YEAR' | f= 'MONTH' | f= 'DAY' | f= 'HOUR' | f= 'MINUTE' | f= 'SECOND' ) ) -> interval(value=$STRINGfield=$f);
    public final SQL92QueryWalker.interval_return interval() throws RecognitionException {
        SQL92QueryWalker.interval_return retval = new SQL92QueryWalker.interval_return();
        retval.start = input.LT(1);

        CommonTree f=null;
        CommonTree STRING29=null;

        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:9: ( ^( 'INTERVAL' STRING (f= 'YEAR' | f= 'MONTH' | f= 'DAY' | f= 'HOUR' | f= 'MINUTE' | f= 'SECOND' ) ) -> interval(value=$STRINGfield=$f))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:13: ^( 'INTERVAL' STRING (f= 'YEAR' | f= 'MONTH' | f= 'DAY' | f= 'HOUR' | f= 'MINUTE' | f= 'SECOND' ) )
            {
            match(input,66,FOLLOW_66_in_interval1454); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            STRING29=(CommonTree)match(input,STRING,FOLLOW_STRING_in_interval1456); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:33: (f= 'YEAR' | f= 'MONTH' | f= 'DAY' | f= 'HOUR' | f= 'MINUTE' | f= 'SECOND' )
            int alt30=6;
            switch ( input.LA(1) ) {
            case 67:
                {
                alt30=1;
                }
                break;
            case 68:
                {
                alt30=2;
                }
                break;
            case 69:
                {
                alt30=3;
                }
                break;
            case 70:
                {
                alt30=4;
                }
                break;
            case 71:
                {
                alt30=5;
                }
                break;
            case 72:
                {
                alt30=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }

            switch (alt30) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:34: f= 'YEAR'
                    {
                    f=(CommonTree)match(input,67,FOLLOW_67_in_interval1461); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:45: f= 'MONTH'
                    {
                    f=(CommonTree)match(input,68,FOLLOW_68_in_interval1467); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:57: f= 'DAY'
                    {
                    f=(CommonTree)match(input,69,FOLLOW_69_in_interval1473); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:67: f= 'HOUR'
                    {
                    f=(CommonTree)match(input,70,FOLLOW_70_in_interval1479); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:78: f= 'MINUTE'
                    {
                    f=(CommonTree)match(input,71,FOLLOW_71_in_interval1485); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:120:91: f= 'SECOND'
                    {
                    f=(CommonTree)match(input,72,FOLLOW_72_in_interval1491); if (state.failed) return retval;

                    }
                    break;

            }


            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 121:13: -> interval(value=$STRINGfield=$f)
              {
                  retval.st = templateLib.getInstanceOf("interval",
                new STAttrMap().put("value", STRING29).put("field", f));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "interval"

    public static class table_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "table"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:123:1: table : ( ^( RIGHT_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"RIGHT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( LEFT_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"LEFT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( FULL_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"FULL\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( JOIN t1= table t2= table search_condition ) -> join(type=\"\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | non_join_table -> {$non_join_table.st});
    public final SQL92QueryWalker.table_return table() throws RecognitionException {
        SQL92QueryWalker.table_return retval = new SQL92QueryWalker.table_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.table_return t1 = null;

        SQL92QueryWalker.table_return t2 = null;

        SQL92QueryWalker.search_condition_return search_condition30 = null;

        SQL92QueryWalker.search_condition_return search_condition31 = null;

        SQL92QueryWalker.search_condition_return search_condition32 = null;

        SQL92QueryWalker.search_condition_return search_condition33 = null;

        SQL92QueryWalker.non_join_table_return non_join_table34 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:123:9: ( ^( RIGHT_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"RIGHT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( LEFT_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"LEFT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( FULL_OUTER_JOIN t1= table t2= table search_condition ) -> join(type=\"FULL\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | ^( JOIN t1= table t2= table search_condition ) -> join(type=\"\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st) | non_join_table -> {$non_join_table.st})
            int alt31=5;
            switch ( input.LA(1) ) {
            case RIGHT_OUTER_JOIN:
                {
                alt31=1;
                }
                break;
            case LEFT_OUTER_JOIN:
                {
                alt31=2;
                }
                break;
            case FULL_OUTER_JOIN:
                {
                alt31=3;
                }
                break;
            case JOIN:
                {
                alt31=4;
                }
                break;
            case RELATION:
                {
                alt31=5;
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
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:123:13: ^( RIGHT_OUTER_JOIN t1= table t2= table search_condition )
                    {
                    match(input,RIGHT_OUTER_JOIN,FOLLOW_RIGHT_OUTER_JOIN_in_table1536); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1540);
                    t1=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1544);
                    t2=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_table1546);
                    search_condition30=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 123:68: -> join(type=\"RIGHT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st)
                      {
                          retval.st = templateLib.getInstanceOf("join",
                        new STAttrMap().put("type", "RIGHT").put("table1", (t1!=null?t1.st:null)).put("table2", (t2!=null?t2.st:null)).put("condition", (search_condition30!=null?search_condition30.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:124:9: ^( LEFT_OUTER_JOIN t1= table t2= table search_condition )
                    {
                    match(input,LEFT_OUTER_JOIN,FOLLOW_LEFT_OUTER_JOIN_in_table1582); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1587);
                    t1=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1591);
                    t2=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_table1593);
                    search_condition31=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 124:64: -> join(type=\"LEFT\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st)
                      {
                          retval.st = templateLib.getInstanceOf("join",
                        new STAttrMap().put("type", "LEFT").put("table1", (t1!=null?t1.st:null)).put("table2", (t2!=null?t2.st:null)).put("condition", (search_condition31!=null?search_condition31.st:null)));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:125:9: ^( FULL_OUTER_JOIN t1= table t2= table search_condition )
                    {
                    match(input,FULL_OUTER_JOIN,FOLLOW_FULL_OUTER_JOIN_in_table1629); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1634);
                    t1=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1638);
                    t2=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_table1640);
                    search_condition32=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 125:64: -> join(type=\"FULL\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st)
                      {
                          retval.st = templateLib.getInstanceOf("join",
                        new STAttrMap().put("type", "FULL").put("table1", (t1!=null?t1.st:null)).put("table2", (t2!=null?t2.st:null)).put("condition", (search_condition32!=null?search_condition32.st:null)));
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:126:9: ^( JOIN t1= table t2= table search_condition )
                    {
                    match(input,JOIN,FOLLOW_JOIN_in_table1676); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1692);
                    t1=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_table_in_table1696);
                    t2=table();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_table1698);
                    search_condition33=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 126:64: -> join(type=\"\"table1=$t1.sttable2=$t2.stcondition=$search_condition.st)
                      {
                          retval.st = templateLib.getInstanceOf("join",
                        new STAttrMap().put("type", "").put("table1", (t1!=null?t1.st:null)).put("table2", (t2!=null?t2.st:null)).put("condition", (search_condition33!=null?search_condition33.st:null)));
                      }

                    }
                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:127:9: non_join_table
                    {
                    pushFollow(FOLLOW_non_join_table_in_table1733);
                    non_join_table34=non_join_table();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 127:24: -> {$non_join_table.st}
                      {
                          retval.st = (non_join_table34!=null?non_join_table34.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "table"

    public static class non_join_table_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "non_join_table"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:130:1: non_join_table : ( ^( RELATION table_name ) -> {$table_name.st} | ^( RELATION table_name ID ) -> expr_as(expression=$table_name.stalias=$ID) | ^( RELATION table_function ) -> {$table_function.st} | ^( RELATION table_function ID ) -> expr_as(expression=$table_function.stalias=$ID) | ^( RELATION query_expression ID ) -> subquery(query=$query_expression.stalias=$ID));
    public final SQL92QueryWalker.non_join_table_return non_join_table() throws RecognitionException {
        SQL92QueryWalker.non_join_table_return retval = new SQL92QueryWalker.non_join_table_return();
        retval.start = input.LT(1);

        CommonTree ID37=null;
        CommonTree ID40=null;
        CommonTree ID42=null;
        SQL92QueryWalker.table_name_return table_name35 = null;

        SQL92QueryWalker.table_name_return table_name36 = null;

        SQL92QueryWalker.table_function_return table_function38 = null;

        SQL92QueryWalker.table_function_return table_function39 = null;

        SQL92QueryWalker.query_expression_return query_expression41 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:131:5: ( ^( RELATION table_name ) -> {$table_name.st} | ^( RELATION table_name ID ) -> expr_as(expression=$table_name.stalias=$ID) | ^( RELATION table_function ) -> {$table_function.st} | ^( RELATION table_function ID ) -> expr_as(expression=$table_function.stalias=$ID) | ^( RELATION query_expression ID ) -> subquery(query=$query_expression.stalias=$ID))
            int alt32=5;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==RELATION) ) {
                int LA32_1 = input.LA(2);

                if ( (synpred53_SQL92QueryWalker()) ) {
                    alt32=1;
                }
                else if ( (synpred54_SQL92QueryWalker()) ) {
                    alt32=2;
                }
                else if ( (synpred55_SQL92QueryWalker()) ) {
                    alt32=3;
                }
                else if ( (synpred56_SQL92QueryWalker()) ) {
                    alt32=4;
                }
                else if ( (true) ) {
                    alt32=5;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 32, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;
            }
            switch (alt32) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:131:9: ^( RELATION table_name )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_non_join_table1757); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_name_in_non_join_table1759);
                    table_name35=table_name();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 131:32: -> {$table_name.st}
                      {
                          retval.st = (table_name35!=null?table_name35.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:132:9: ^( RELATION table_name ID )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_non_join_table1776); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_name_in_non_join_table1778);
                    table_name36=table_name();

                    state._fsp--;
                    if (state.failed) return retval;
                    ID37=(CommonTree)match(input,ID,FOLLOW_ID_in_non_join_table1780); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 132:35: -> expr_as(expression=$table_name.stalias=$ID)
                      {
                          retval.st = templateLib.getInstanceOf("expr_as",
                        new STAttrMap().put("expression", (table_name36!=null?table_name36.st:null)).put("alias", ID37));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:133:9: ^( RELATION table_function )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_non_join_table1806); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_function_in_non_join_table1808);
                    table_function38=table_function();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 133:36: -> {$table_function.st}
                      {
                          retval.st = (table_function38!=null?table_function38.st:null);
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:134:9: ^( RELATION table_function ID )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_non_join_table1824); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_function_in_non_join_table1826);
                    table_function39=table_function();

                    state._fsp--;
                    if (state.failed) return retval;
                    ID40=(CommonTree)match(input,ID,FOLLOW_ID_in_non_join_table1828); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 134:39: -> expr_as(expression=$table_function.stalias=$ID)
                      {
                          retval.st = templateLib.getInstanceOf("expr_as",
                        new STAttrMap().put("expression", (table_function39!=null?table_function39.st:null)).put("alias", ID40));
                      }

                    }
                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:135:9: ^( RELATION query_expression ID )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_non_join_table1854); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_query_expression_in_non_join_table1856);
                    query_expression41=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;
                    ID42=(CommonTree)match(input,ID,FOLLOW_ID_in_non_join_table1858); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 135:41: -> subquery(query=$query_expression.stalias=$ID)
                      {
                          retval.st = templateLib.getInstanceOf("subquery",
                        new STAttrMap().put("query", (query_expression41!=null?query_expression41.st:null)).put("alias", ID42));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "non_join_table"

    public static class table_function_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "table_function"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:138:1: table_function : ^( FUNCTION ID (param+= relation )+ (param+= literal )* ) -> function(name=$IDparam=$param);
    public final SQL92QueryWalker.table_function_return table_function() throws RecognitionException {
        SQL92QueryWalker.table_function_return retval = new SQL92QueryWalker.table_function_return();
        retval.start = input.LT(1);

        CommonTree ID43=null;
        List list_param=null;
        RuleReturnScope param = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:5: ( ^( FUNCTION ID (param+= relation )+ (param+= literal )* ) -> function(name=$IDparam=$param))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:9: ^( FUNCTION ID (param+= relation )+ (param+= literal )* )
            {
            match(input,FUNCTION,FOLLOW_FUNCTION_in_table_function1897); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            ID43=(CommonTree)match(input,ID,FOLLOW_ID_in_table_function1899); if (state.failed) return retval;
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:23: (param+= relation )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                int LA33_0 = input.LA(1);

                if ( (LA33_0==RELATION) ) {
                    alt33=1;
                }


                switch (alt33) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:24: param+= relation
                    {
                    pushFollow(FOLLOW_relation_in_table_function1904);
                    param=relation();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_param==null) list_param=new ArrayList();
                    list_param.add(param.getTemplate());


                    }
                    break;

                default :
                    if ( cnt33 >= 1 ) break loop33;
                    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
            } while (true);

            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:42: (param+= literal )*
            loop34:
            do {
                int alt34=2;
                int LA34_0 = input.LA(1);

                if ( (LA34_0==INT||(LA34_0>=FLOAT && LA34_0<=STRING)||(LA34_0>=63 && LA34_0<=66)||(LA34_0>=76 && LA34_0<=78)) ) {
                    alt34=1;
                }


                switch (alt34) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:139:43: param+= literal
                    {
                    pushFollow(FOLLOW_literal_in_table_function1911);
                    param=literal();

                    state._fsp--;
                    if (state.failed) return retval;
                    if (list_param==null) list_param=new ArrayList();
                    list_param.add(param.getTemplate());


                    }
                    break;

                default :
                    break loop34;
                }
            } while (true);


            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 139:61: -> function(name=$IDparam=$param)
              {
                  retval.st = templateLib.getInstanceOf("function",
                new STAttrMap().put("name", ID43).put("param", list_param));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "table_function"

    public static class relation_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "relation"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:142:1: relation : ( ^( RELATION table_name ) -> {$table_name.st} | ^( RELATION table_function ) -> {$table_function.st} | ^( RELATION query ) -> {$query.st});
    public final SQL92QueryWalker.relation_return relation() throws RecognitionException {
        SQL92QueryWalker.relation_return retval = new SQL92QueryWalker.relation_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.table_name_return table_name44 = null;

        SQL92QueryWalker.table_function_return table_function45 = null;

        SQL92QueryWalker.query_return query46 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:142:9: ( ^( RELATION table_name ) -> {$table_name.st} | ^( RELATION table_function ) -> {$table_function.st} | ^( RELATION query ) -> {$query.st})
            int alt35=3;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==RELATION) ) {
                int LA35_1 = input.LA(2);

                if ( (LA35_1==DOWN) ) {
                    switch ( input.LA(3) ) {
                    case FUNCTION:
                        {
                        alt35=2;
                        }
                        break;
                    case QUERY:
                        {
                        alt35=3;
                        }
                        break;
                    case ID:
                        {
                        alt35=1;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 2, input);

                        throw nvae;
                    }

                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 35, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 35, 0, input);

                throw nvae;
            }
            switch (alt35) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:142:13: ^( RELATION table_name )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_relation1947); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_name_in_relation1949);
                    table_name44=table_name();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 142:36: -> {$table_name.st}
                      {
                          retval.st = (table_name44!=null?table_name44.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:143:9: ^( RELATION table_function )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_relation1965); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_table_function_in_relation1967);
                    table_function45=table_function();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 143:36: -> {$table_function.st}
                      {
                          retval.st = (table_function45!=null?table_function45.st:null);
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:144:9: ^( RELATION query )
                    {
                    match(input,RELATION,FOLLOW_RELATION_in_relation1983); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_query_in_relation1985);
                    query46=query();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 144:27: -> {$query.st}
                      {
                          retval.st = (query46!=null?query46.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "relation"

    public static class search_condition_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "search_condition"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:147:1: search_condition : ( ^(op= 'OR' s1= search_condition s2= search_condition ) -> bin_expr_paren(op=$opvalue1=$s1.stvalue2=$s2.st) | ^(op= 'AND' s1= search_condition s2= search_condition ) -> bin_expr(op=$opvalue1=$s1.stvalue2=$s2.st) | boolean_term -> {$boolean_term.st});
    public final SQL92QueryWalker.search_condition_return search_condition() throws RecognitionException {
        SQL92QueryWalker.search_condition_return retval = new SQL92QueryWalker.search_condition_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.search_condition_return s1 = null;

        SQL92QueryWalker.search_condition_return s2 = null;

        SQL92QueryWalker.boolean_term_return boolean_term47 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:148:5: ( ^(op= 'OR' s1= search_condition s2= search_condition ) -> bin_expr_paren(op=$opvalue1=$s1.stvalue2=$s2.st) | ^(op= 'AND' s1= search_condition s2= search_condition ) -> bin_expr(op=$opvalue1=$s1.stvalue2=$s2.st) | boolean_term -> {$boolean_term.st})
            int alt36=3;
            switch ( input.LA(1) ) {
            case 87:
                {
                alt36=1;
                }
                break;
            case 88:
                {
                alt36=2;
                }
                break;
            case NOT:
            case IS_NULL:
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
                {
                alt36=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }

            switch (alt36) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:148:9: ^(op= 'OR' s1= search_condition s2= search_condition )
                    {
                    op=(CommonTree)match(input,87,FOLLOW_87_in_search_condition2016); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_search_condition2020);
                    s1=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_search_condition2024);
                    s2=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 148:60: -> bin_expr_paren(op=$opvalue1=$s1.stvalue2=$s2.st)
                      {
                          retval.st = templateLib.getInstanceOf("bin_expr_paren",
                        new STAttrMap().put("op", op).put("value1", (s1!=null?s1.st:null)).put("value2", (s2!=null?s2.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:149:9: ^(op= 'AND' s1= search_condition s2= search_condition )
                    {
                    op=(CommonTree)match(input,88,FOLLOW_88_in_search_condition2057); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_search_condition2061);
                    s1=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;
                    pushFollow(FOLLOW_search_condition_in_search_condition2065);
                    s2=search_condition();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 149:61: -> bin_expr(op=$opvalue1=$s1.stvalue2=$s2.st)
                      {
                          retval.st = templateLib.getInstanceOf("bin_expr",
                        new STAttrMap().put("op", op).put("value1", (s1!=null?s1.st:null)).put("value2", (s2!=null?s2.st:null)));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:150:9: boolean_term
                    {
                    pushFollow(FOLLOW_boolean_term_in_search_condition2095);
                    boolean_term47=boolean_term();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 150:22: -> {$boolean_term.st}
                      {
                          retval.st = (boolean_term47!=null?boolean_term47.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "search_condition"

    public static class boolean_term_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "boolean_term"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:152:1: boolean_term : ( predicate -> {$predicate.st} | ^(op= NOT predicate ) -> unary_expr(op=$op.textvalue=$predicate.st));
    public final SQL92QueryWalker.boolean_term_return boolean_term() throws RecognitionException {
        SQL92QueryWalker.boolean_term_return retval = new SQL92QueryWalker.boolean_term_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.predicate_return predicate48 = null;

        SQL92QueryWalker.predicate_return predicate49 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:153:5: ( predicate -> {$predicate.st} | ^(op= NOT predicate ) -> unary_expr(op=$op.textvalue=$predicate.st))
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==IS_NULL||(LA37_0>=91 && LA37_0<=100)||LA37_0==103) ) {
                alt37=1;
            }
            else if ( (LA37_0==NOT) ) {
                alt37=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }
            switch (alt37) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:153:9: predicate
                    {
                    pushFollow(FOLLOW_predicate_in_boolean_term2117);
                    predicate48=predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 153:19: -> {$predicate.st}
                      {
                          retval.st = (predicate48!=null?predicate48.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:154:9: ^(op= NOT predicate )
                    {
                    op=(CommonTree)match(input,NOT,FOLLOW_NOT_in_boolean_term2134); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    pushFollow(FOLLOW_predicate_in_boolean_term2136);
                    predicate49=predicate();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 154:30: -> unary_expr(op=$op.textvalue=$predicate.st)
                      {
                          retval.st = templateLib.getInstanceOf("unary_expr",
                        new STAttrMap().put("op", (op!=null?op.getText():null)).put("value", (predicate49!=null?predicate49.st:null)));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "boolean_term"

    public static class predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:156:1: predicate : ( comparison_predicate -> {$comparison_predicate.st} | like_predicate -> {$like_predicate.st} | in_predicate -> {$in_predicate.st} | null_predicate -> {$null_predicate.st} | exists_predicate -> {$exists_predicate.st} | between_predicate -> {$between_predicate.st});
    public final SQL92QueryWalker.predicate_return predicate() throws RecognitionException {
        SQL92QueryWalker.predicate_return retval = new SQL92QueryWalker.predicate_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.comparison_predicate_return comparison_predicate50 = null;

        SQL92QueryWalker.like_predicate_return like_predicate51 = null;

        SQL92QueryWalker.in_predicate_return in_predicate52 = null;

        SQL92QueryWalker.null_predicate_return null_predicate53 = null;

        SQL92QueryWalker.exists_predicate_return exists_predicate54 = null;

        SQL92QueryWalker.between_predicate_return between_predicate55 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:157:5: ( comparison_predicate -> {$comparison_predicate.st} | like_predicate -> {$like_predicate.st} | in_predicate -> {$in_predicate.st} | null_predicate -> {$null_predicate.st} | exists_predicate -> {$exists_predicate.st} | between_predicate -> {$between_predicate.st})
            int alt38=6;
            switch ( input.LA(1) ) {
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
                {
                alt38=1;
                }
                break;
            case 103:
                {
                alt38=2;
                }
                break;
            case 91:
                {
                alt38=3;
                }
                break;
            case IS_NULL:
                {
                alt38=4;
                }
                break;
            case 93:
                {
                alt38=5;
                }
                break;
            case 92:
                {
                alt38=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }

            switch (alt38) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:157:9: comparison_predicate
                    {
                    pushFollow(FOLLOW_comparison_predicate_in_predicate2170);
                    comparison_predicate50=comparison_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 157:30: -> {$comparison_predicate.st}
                      {
                          retval.st = (comparison_predicate50!=null?comparison_predicate50.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:158:9: like_predicate
                    {
                    pushFollow(FOLLOW_like_predicate_in_predicate2184);
                    like_predicate51=like_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 158:30: -> {$like_predicate.st}
                      {
                          retval.st = (like_predicate51!=null?like_predicate51.st:null);
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:159:9: in_predicate
                    {
                    pushFollow(FOLLOW_in_predicate_in_predicate2204);
                    in_predicate52=in_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 159:30: -> {$in_predicate.st}
                      {
                          retval.st = (in_predicate52!=null?in_predicate52.st:null);
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:160:9: null_predicate
                    {
                    pushFollow(FOLLOW_null_predicate_in_predicate2226);
                    null_predicate53=null_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 160:30: -> {$null_predicate.st}
                      {
                          retval.st = (null_predicate53!=null?null_predicate53.st:null);
                      }

                    }
                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:161:9: exists_predicate
                    {
                    pushFollow(FOLLOW_exists_predicate_in_predicate2246);
                    exists_predicate54=exists_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 161:30: -> {$exists_predicate.st}
                      {
                          retval.st = (exists_predicate54!=null?exists_predicate54.st:null);
                      }

                    }
                    }
                    break;
                case 6 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:162:9: between_predicate
                    {
                    pushFollow(FOLLOW_between_predicate_in_predicate2264);
                    between_predicate55=between_predicate();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 162:30: -> {$between_predicate.st}
                      {
                          retval.st = (between_predicate55!=null?between_predicate55.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "predicate"

    public static class null_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "null_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:165:1: null_predicate : ^( IS_NULL row_value ) -> unary_expr(op=$row_value.stvalue=\"IS NULL\");
    public final SQL92QueryWalker.null_predicate_return null_predicate() throws RecognitionException {
        SQL92QueryWalker.null_predicate_return retval = new SQL92QueryWalker.null_predicate_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.row_value_return row_value56 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:166:5: ( ^( IS_NULL row_value ) -> unary_expr(op=$row_value.stvalue=\"IS NULL\"))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:166:9: ^( IS_NULL row_value )
            {
            match(input,IS_NULL,FOLLOW_IS_NULL_in_null_predicate2292); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_null_predicate2294);
            row_value56=row_value();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 166:30: -> unary_expr(op=$row_value.stvalue=\"IS NULL\")
              {
                  retval.st = templateLib.getInstanceOf("unary_expr",
                new STAttrMap().put("op", (row_value56!=null?row_value56.st:null)).put("value", "IS NULL"));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "null_predicate"

    public static class in_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "in_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:167:1: in_predicate : ^(op= 'IN' row_value in_predicate_tail ) -> bin_expr(op=$opvalue1=$row_value.stvalue2=$in_predicate_tail.st);
    public final SQL92QueryWalker.in_predicate_return in_predicate() throws RecognitionException {
        SQL92QueryWalker.in_predicate_return retval = new SQL92QueryWalker.in_predicate_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.row_value_return row_value57 = null;

        SQL92QueryWalker.in_predicate_tail_return in_predicate_tail58 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:168:5: ( ^(op= 'IN' row_value in_predicate_tail ) -> bin_expr(op=$opvalue1=$row_value.stvalue2=$in_predicate_tail.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:168:9: ^(op= 'IN' row_value in_predicate_tail )
            {
            op=(CommonTree)match(input,91,FOLLOW_91_in_in_predicate2325); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_in_predicate2327);
            row_value57=row_value();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_in_predicate_tail_in_in_predicate2329);
            in_predicate_tail58=in_predicate_tail();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 168:48: -> bin_expr(op=$opvalue1=$row_value.stvalue2=$in_predicate_tail.st)
              {
                  retval.st = templateLib.getInstanceOf("bin_expr",
                new STAttrMap().put("op", op).put("value1", (row_value57!=null?row_value57.st:null)).put("value2", (in_predicate_tail58!=null?in_predicate_tail58.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "in_predicate"

    public static class in_predicate_tail_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "in_predicate_tail"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:169:1: in_predicate_tail : ( query_expression -> paren(str=$query_expression.st) | ^( SET (e+= value_expression )+ ) -> set(expr=$e));
    public final SQL92QueryWalker.in_predicate_tail_return in_predicate_tail() throws RecognitionException {
        SQL92QueryWalker.in_predicate_tail_return retval = new SQL92QueryWalker.in_predicate_tail_return();
        retval.start = input.LT(1);

        List list_e=null;
        SQL92QueryWalker.query_expression_return query_expression59 = null;

        RuleReturnScope e = null;
        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:170:5: ( query_expression -> paren(str=$query_expression.st) | ^( SET (e+= value_expression )+ ) -> set(expr=$e))
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==QUERY||(LA40_0>=UNION && LA40_0<=INTERSECT)) ) {
                alt40=1;
            }
            else if ( (LA40_0==SET) ) {
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
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:170:9: query_expression
                    {
                    pushFollow(FOLLOW_query_expression_in_in_predicate_tail2363);
                    query_expression59=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 170:26: -> paren(str=$query_expression.st)
                      {
                          retval.st = templateLib.getInstanceOf("paren",
                        new STAttrMap().put("str", (query_expression59!=null?query_expression59.st:null)));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:171:9: ^( SET (e+= value_expression )+ )
                    {
                    match(input,SET,FOLLOW_SET_in_in_predicate_tail2383); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:171:15: (e+= value_expression )+
                    int cnt39=0;
                    loop39:
                    do {
                        int alt39=2;
                        int LA39_0 = input.LA(1);

                        if ( (LA39_0==QUERY||LA39_0==FUNCTION||LA39_0==TABLECOLUMN||(LA39_0>=UNION && LA39_0<=INTERSECT)||LA39_0==INT||(LA39_0>=FLOAT && LA39_0<=STRING)||LA39_0==54||(LA39_0>=63 && LA39_0<=66)||(LA39_0>=73 && LA39_0<=79)) ) {
                            alt39=1;
                        }


                        switch (alt39) {
                        case 1 :
                            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:171:16: e+= value_expression
                            {
                            pushFollow(FOLLOW_value_expression_in_in_predicate_tail2388);
                            e=value_expression();

                            state._fsp--;
                            if (state.failed) return retval;
                            if (list_e==null) list_e=new ArrayList();
                            list_e.add(e.getTemplate());


                            }
                            break;

                        default :
                            if ( cnt39 >= 1 ) break loop39;
                            if (state.backtracking>0) {state.failed=true; return retval;}
                                EarlyExitException eee =
                                    new EarlyExitException(39, input);
                                throw eee;
                        }
                        cnt39++;
                    } while (true);


                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 171:39: -> set(expr=$e)
                      {
                          retval.st = templateLib.getInstanceOf("set",
                        new STAttrMap().put("expr", list_e));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "in_predicate_tail"

    public static class between_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "between_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:172:1: between_predicate : ^( 'BETWEEN' val= row_value gt= row_value lt= row_value ) -> between(value=$val.stgreater_than=$gt.stless_than=$lt.st);
    public final SQL92QueryWalker.between_predicate_return between_predicate() throws RecognitionException {
        SQL92QueryWalker.between_predicate_return retval = new SQL92QueryWalker.between_predicate_return();
        retval.start = input.LT(1);

        SQL92QueryWalker.row_value_return val = null;

        SQL92QueryWalker.row_value_return gt = null;

        SQL92QueryWalker.row_value_return lt = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:173:5: ( ^( 'BETWEEN' val= row_value gt= row_value lt= row_value ) -> between(value=$val.stgreater_than=$gt.stless_than=$lt.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:173:9: ^( 'BETWEEN' val= row_value gt= row_value lt= row_value )
            {
            match(input,92,FOLLOW_92_in_between_predicate2414); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_between_predicate2418);
            val=row_value();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_between_predicate2422);
            gt=row_value();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_between_predicate2426);
            lt=row_value();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 173:62: -> between(value=$val.stgreater_than=$gt.stless_than=$lt.st)
              {
                  retval.st = templateLib.getInstanceOf("between",
                new STAttrMap().put("value", (val!=null?val.st:null)).put("greater_than", (gt!=null?gt.st:null)).put("less_than", (lt!=null?lt.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "between_predicate"

    public static class exists_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "exists_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:174:1: exists_predicate : ^(name= 'EXISTS' query_expression ) -> function(name=$nameparam=$query_expression.st);
    public final SQL92QueryWalker.exists_predicate_return exists_predicate() throws RecognitionException {
        SQL92QueryWalker.exists_predicate_return retval = new SQL92QueryWalker.exists_predicate_return();
        retval.start = input.LT(1);

        CommonTree name=null;
        SQL92QueryWalker.query_expression_return query_expression60 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:175:5: ( ^(name= 'EXISTS' query_expression ) -> function(name=$nameparam=$query_expression.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:175:9: ^(name= 'EXISTS' query_expression )
            {
            name=(CommonTree)match(input,93,FOLLOW_93_in_exists_predicate2462); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_query_expression_in_exists_predicate2464);
            query_expression60=query_expression();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 175:43: -> function(name=$nameparam=$query_expression.st)
              {
                  retval.st = templateLib.getInstanceOf("function",
                new STAttrMap().put("name", name).put("param", (query_expression60!=null?query_expression60.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "exists_predicate"

    public static class comparison_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "comparison_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:176:1: comparison_predicate : ^( (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) v1= row_value v2= row_value ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st);
    public final SQL92QueryWalker.comparison_predicate_return comparison_predicate() throws RecognitionException {
        SQL92QueryWalker.comparison_predicate_return retval = new SQL92QueryWalker.comparison_predicate_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.row_value_return v1 = null;

        SQL92QueryWalker.row_value_return v2 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:5: ( ^( (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) v1= row_value v2= row_value ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:9: ^( (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' ) v1= row_value v2= row_value )
            {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:11: (op= '=' | op= '<>' | op= '!=' | op= '<' | op= '>' | op= '>=' | op= '<=' )
            int alt41=7;
            switch ( input.LA(1) ) {
            case 94:
                {
                alt41=1;
                }
                break;
            case 95:
                {
                alt41=2;
                }
                break;
            case 96:
                {
                alt41=3;
                }
                break;
            case 97:
                {
                alt41=4;
                }
                break;
            case 98:
                {
                alt41=5;
                }
                break;
            case 99:
                {
                alt41=6;
                }
                break;
            case 100:
                {
                alt41=7;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:12: op= '='
                    {
                    op=(CommonTree)match(input,94,FOLLOW_94_in_comparison_predicate2496); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:21: op= '<>'
                    {
                    op=(CommonTree)match(input,95,FOLLOW_95_in_comparison_predicate2502); if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:31: op= '!='
                    {
                    op=(CommonTree)match(input,96,FOLLOW_96_in_comparison_predicate2508); if (state.failed) return retval;

                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:41: op= '<'
                    {
                    op=(CommonTree)match(input,97,FOLLOW_97_in_comparison_predicate2514); if (state.failed) return retval;

                    }
                    break;
                case 5 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:50: op= '>'
                    {
                    op=(CommonTree)match(input,98,FOLLOW_98_in_comparison_predicate2520); if (state.failed) return retval;

                    }
                    break;
                case 6 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:59: op= '>='
                    {
                    op=(CommonTree)match(input,99,FOLLOW_99_in_comparison_predicate2526); if (state.failed) return retval;

                    }
                    break;
                case 7 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:177:69: op= '<='
                    {
                    op=(CommonTree)match(input,100,FOLLOW_100_in_comparison_predicate2532); if (state.failed) return retval;

                    }
                    break;

            }


            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_comparison_predicate2537);
            v1=row_value();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_comparison_predicate2541);
            v2=row_value();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 178:13: -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st)
              {
                  retval.st = templateLib.getInstanceOf("bin_expr",
                new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "comparison_predicate"

    public static class like_predicate_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "like_predicate"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:179:1: like_predicate : ^(op= 'LIKE' v1= row_value v2= row_value ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st);
    public final SQL92QueryWalker.like_predicate_return like_predicate() throws RecognitionException {
        SQL92QueryWalker.like_predicate_return retval = new SQL92QueryWalker.like_predicate_return();
        retval.start = input.LT(1);

        CommonTree op=null;
        SQL92QueryWalker.row_value_return v1 = null;

        SQL92QueryWalker.row_value_return v2 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:180:5: ( ^(op= 'LIKE' v1= row_value v2= row_value ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:180:9: ^(op= 'LIKE' v1= row_value v2= row_value )
            {
            op=(CommonTree)match(input,103,FOLLOW_103_in_like_predicate2590); if (state.failed) return retval;

            match(input, Token.DOWN, null); if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_like_predicate2594);
            v1=row_value();

            state._fsp--;
            if (state.failed) return retval;
            pushFollow(FOLLOW_row_value_in_like_predicate2598);
            v2=row_value();

            state._fsp--;
            if (state.failed) return retval;

            match(input, Token.UP, null); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 180:48: -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st)
              {
                  retval.st = templateLib.getInstanceOf("bin_expr",
                new STAttrMap().put("op", op).put("value1", (v1!=null?v1.st:null)).put("value2", (v2!=null?v2.st:null)));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "like_predicate"

    public static class row_value_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "row_value"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:185:1: row_value : ( value_expression -> {$value_expression.st} | str= 'NULL' -> emitstr(str=$str) | str= 'DEFAULT' -> emitstr(str=$str) | query_expression -> {$query_expression.st});
    public final SQL92QueryWalker.row_value_return row_value() throws RecognitionException {
        SQL92QueryWalker.row_value_return retval = new SQL92QueryWalker.row_value_return();
        retval.start = input.LT(1);

        CommonTree str=null;
        SQL92QueryWalker.value_expression_return value_expression61 = null;

        SQL92QueryWalker.query_expression_return query_expression62 = null;


        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:186:5: ( value_expression -> {$value_expression.st} | str= 'NULL' -> emitstr(str=$str) | str= 'DEFAULT' -> emitstr(str=$str) | query_expression -> {$query_expression.st})
            int alt42=4;
            alt42 = dfa42.predict(input);
            switch (alt42) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:186:9: value_expression
                    {
                    pushFollow(FOLLOW_value_expression_in_row_value2663);
                    value_expression61=value_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 186:27: -> {$value_expression.st}
                      {
                          retval.st = (value_expression61!=null?value_expression61.st:null);
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:187:9: str= 'NULL'
                    {
                    str=(CommonTree)match(input,76,FOLLOW_76_in_row_value2680); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 187:27: -> emitstr(str=$str)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", str));
                      }

                    }
                    }
                    break;
                case 3 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:188:9: str= 'DEFAULT'
                    {
                    str=(CommonTree)match(input,104,FOLLOW_104_in_row_value2708); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 188:27: -> emitstr(str=$str)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", str));
                      }

                    }
                    }
                    break;
                case 4 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:189:9: query_expression
                    {
                    pushFollow(FOLLOW_query_expression_in_row_value2731);
                    query_expression62=query_expression();

                    state._fsp--;
                    if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 189:26: -> {$query_expression.st}
                      {
                          retval.st = (query_expression62!=null?query_expression62.st:null);
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "row_value"

    public static class table_name_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "table_name"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:197:1: table_name : ID -> emitstr(str=$ID);
    public final SQL92QueryWalker.table_name_return table_name() throws RecognitionException {
        SQL92QueryWalker.table_name_return retval = new SQL92QueryWalker.table_name_return();
        retval.start = input.LT(1);

        CommonTree ID63=null;

        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:198:5: ( ID -> emitstr(str=$ID))
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:198:9: ID
            {
            ID63=(CommonTree)match(input,ID,FOLLOW_ID_in_table_name2759); if (state.failed) return retval;


            // TEMPLATE REWRITE
            if ( state.backtracking==0 ) {
              // 198:12: -> emitstr(str=$ID)
              {
                  retval.st = templateLib.getInstanceOf("emitstr",
                new STAttrMap().put("str", ID63));
              }

            }
            }

        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "table_name"

    public static class column_name_return extends TreeRuleReturnScope {
        public StringTemplate st;
        public Object getTemplate() { return st; }
        public String toString() { return st==null?null:st.toString(); }
    };

    // $ANTLR start "column_name"
    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:199:1: column_name : ( ^( TABLECOLUMN ID ) -> emitstr(str=$ID) | ^( TABLECOLUMN t= ID c= ID ) -> column_name(table=$tname=$c));
    public final SQL92QueryWalker.column_name_return column_name() throws RecognitionException {
        SQL92QueryWalker.column_name_return retval = new SQL92QueryWalker.column_name_return();
        retval.start = input.LT(1);

        CommonTree t=null;
        CommonTree c=null;
        CommonTree ID64=null;

        try {
            // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:200:5: ( ^( TABLECOLUMN ID ) -> emitstr(str=$ID) | ^( TABLECOLUMN t= ID c= ID ) -> column_name(table=$tname=$c))
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==TABLECOLUMN) ) {
                int LA43_1 = input.LA(2);

                if ( (LA43_1==DOWN) ) {
                    int LA43_2 = input.LA(3);

                    if ( (LA43_2==ID) ) {
                        int LA43_3 = input.LA(4);

                        if ( (LA43_3==UP) ) {
                            alt43=1;
                        }
                        else if ( (LA43_3==ID) ) {
                            alt43=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return retval;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 43, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 43, 2, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 43, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 43, 0, input);

                throw nvae;
            }
            switch (alt43) {
                case 1 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:200:9: ^( TABLECOLUMN ID )
                    {
                    match(input,TABLECOLUMN,FOLLOW_TABLECOLUMN_in_column_name2782); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    ID64=(CommonTree)match(input,ID,FOLLOW_ID_in_column_name2784); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 200:34: -> emitstr(str=$ID)
                      {
                          retval.st = templateLib.getInstanceOf("emitstr",
                        new STAttrMap().put("str", ID64));
                      }

                    }
                    }
                    break;
                case 2 :
                    // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:201:9: ^( TABLECOLUMN t= ID c= ID )
                    {
                    match(input,TABLECOLUMN,FOLLOW_TABLECOLUMN_in_column_name2812); if (state.failed) return retval;

                    match(input, Token.DOWN, null); if (state.failed) return retval;
                    t=(CommonTree)match(input,ID,FOLLOW_ID_in_column_name2816); if (state.failed) return retval;
                    c=(CommonTree)match(input,ID,FOLLOW_ID_in_column_name2820); if (state.failed) return retval;

                    match(input, Token.UP, null); if (state.failed) return retval;


                    // TEMPLATE REWRITE
                    if ( state.backtracking==0 ) {
                      // 201:34: -> column_name(table=$tname=$c)
                      {
                          retval.st = templateLib.getInstanceOf("column_name",
                        new STAttrMap().put("table", t).put("name", c));
                      }

                    }
                    }
                    break;

            }
        }

        catch (RecognitionException re)
        {
            re.printStackTrace();
            reportError(re);
            throw re;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "column_name"

    // $ANTLR start synpred17_SQL92QueryWalker
    public final void synpred17_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:67:9: ( ^( COLUMN value_expression ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:67:9: ^( COLUMN value_expression )
        {
        match(input,COLUMN,FOLLOW_COLUMN_in_synpred17_SQL92QueryWalker620); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_value_expression_in_synpred17_SQL92QueryWalker622);
        value_expression();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred17_SQL92QueryWalker

    // $ANTLR start synpred23_SQL92QueryWalker
    public final void synpred23_SQL92QueryWalker_fragment() throws RecognitionException {   
        CommonTree op=null;
        SQL92QueryWalker.numeric_value_expression_return v1 = null;

        SQL92QueryWalker.numeric_value_expression_return v2 = null;


        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:9: ( ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:9: ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression )
        {
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:11: (op= '+' | op= '-' )
        int alt47=2;
        int LA47_0 = input.LA(1);

        if ( (LA47_0==73) ) {
            alt47=1;
        }
        else if ( (LA47_0==74) ) {
            alt47=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 47, 0, input);

            throw nvae;
        }
        switch (alt47) {
            case 1 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:12: op= '+'
                {
                op=(CommonTree)match(input,73,FOLLOW_73_in_synpred23_SQL92QueryWalker813); if (state.failed) return ;

                }
                break;
            case 2 :
                // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:79:21: op= '-'
                {
                op=(CommonTree)match(input,74,FOLLOW_74_in_synpred23_SQL92QueryWalker819); if (state.failed) return ;

                }
                break;

        }


        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_numeric_value_expression_in_synpred23_SQL92QueryWalker825);
        v1=numeric_value_expression();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_numeric_value_expression_in_synpred23_SQL92QueryWalker829);
        v2=numeric_value_expression();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred23_SQL92QueryWalker

    // $ANTLR start synpred53_SQL92QueryWalker
    public final void synpred53_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:131:9: ( ^( RELATION table_name ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:131:9: ^( RELATION table_name )
        {
        match(input,RELATION,FOLLOW_RELATION_in_synpred53_SQL92QueryWalker1757); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_table_name_in_synpred53_SQL92QueryWalker1759);
        table_name();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred53_SQL92QueryWalker

    // $ANTLR start synpred54_SQL92QueryWalker
    public final void synpred54_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:132:9: ( ^( RELATION table_name ID ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:132:9: ^( RELATION table_name ID )
        {
        match(input,RELATION,FOLLOW_RELATION_in_synpred54_SQL92QueryWalker1776); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_table_name_in_synpred54_SQL92QueryWalker1778);
        table_name();

        state._fsp--;
        if (state.failed) return ;
        match(input,ID,FOLLOW_ID_in_synpred54_SQL92QueryWalker1780); if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred54_SQL92QueryWalker

    // $ANTLR start synpred55_SQL92QueryWalker
    public final void synpred55_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:133:9: ( ^( RELATION table_function ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:133:9: ^( RELATION table_function )
        {
        match(input,RELATION,FOLLOW_RELATION_in_synpred55_SQL92QueryWalker1806); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_table_function_in_synpred55_SQL92QueryWalker1808);
        table_function();

        state._fsp--;
        if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred55_SQL92QueryWalker

    // $ANTLR start synpred56_SQL92QueryWalker
    public final void synpred56_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:134:9: ( ^( RELATION table_function ID ) )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:134:9: ^( RELATION table_function ID )
        {
        match(input,RELATION,FOLLOW_RELATION_in_synpred56_SQL92QueryWalker1824); if (state.failed) return ;

        match(input, Token.DOWN, null); if (state.failed) return ;
        pushFollow(FOLLOW_table_function_in_synpred56_SQL92QueryWalker1826);
        table_function();

        state._fsp--;
        if (state.failed) return ;
        match(input,ID,FOLLOW_ID_in_synpred56_SQL92QueryWalker1828); if (state.failed) return ;

        match(input, Token.UP, null); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred56_SQL92QueryWalker

    // $ANTLR start synpred77_SQL92QueryWalker
    public final void synpred77_SQL92QueryWalker_fragment() throws RecognitionException {   
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:186:9: ( value_expression )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:186:9: value_expression
        {
        pushFollow(FOLLOW_value_expression_in_synpred77_SQL92QueryWalker2663);
        value_expression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred77_SQL92QueryWalker

    // $ANTLR start synpred78_SQL92QueryWalker
    public final void synpred78_SQL92QueryWalker_fragment() throws RecognitionException {   
        CommonTree str=null;

        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:187:9: (str= 'NULL' )
        // /Users/akrause/Documents/eclipse/workspace/ogsa-dai/ogsa-dai/trunk/extensions/dqp/server/src/main/grammar/SQL92QueryWalker.g:187:9: str= 'NULL'
        {
        str=(CommonTree)match(input,76,FOLLOW_76_in_synpred78_SQL92QueryWalker2680); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred78_SQL92QueryWalker

    // Delegated rules

    public final boolean synpred54_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred54_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred56_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred56_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred17_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred17_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred77_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred77_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred55_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred55_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred78_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred78_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred23_SQL92QueryWalker() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred23_SQL92QueryWalker_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA20 dfa20 = new DFA20(this);
    protected DFA42 dfa42 = new DFA42(this);
    static final String DFA20_eotS =
        "\31\uffff";
    static final String DFA20_eofS =
        "\31\uffff";
    static final String DFA20_minS =
        "\1\5\2\0\26\uffff";
    static final String DFA20_maxS =
        "\1\116\2\0\26\uffff";
    static final String DFA20_acceptS =
        "\3\uffff\1\2\1\uffff\1\3\22\uffff\1\1";
    static final String DFA20_specialS =
        "\1\uffff\1\0\1\1\26\uffff}>";
    static final String[] DFA20_transitionS = {
            "\1\5\11\uffff\1\5\2\uffff\1\5\5\uffff\5\5\5\uffff\1\5\1\uffff"+
            "\3\5\17\uffff\1\3\10\uffff\4\5\6\uffff\1\1\1\2\1\3\3\5",
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
            ""
    };

    static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
    static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
    static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
    static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
    static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
    static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
    static final short[][] DFA20_transition;

    static {
        int numStates = DFA20_transitionS.length;
        DFA20_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
        }
    }

    class DFA20 extends DFA {

        public DFA20(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 20;
            this.eot = DFA20_eot;
            this.eof = DFA20_eof;
            this.min = DFA20_min;
            this.max = DFA20_max;
            this.accept = DFA20_accept;
            this.special = DFA20_special;
            this.transition = DFA20_transition;
        }
        public String getDescription() {
            return "78:1: numeric_value_expression : ( ^( (op= '+' | op= '-' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr_paren(op=$opvalue1=$v1.stvalue2=$v2.st) | ^( (op= '/' | op= '*' ) v1= numeric_value_expression v2= numeric_value_expression ) -> bin_expr(op=$opvalue1=$v1.stvalue2=$v2.st) | numeric_primary -> {$numeric_primary.st});";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TreeNodeStream input = (TreeNodeStream)_input;
            int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA20_1 = input.LA(1);

                         
                        int index20_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_SQL92QueryWalker()) ) {s = 24;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index20_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA20_2 = input.LA(1);

                         
                        int index20_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred23_SQL92QueryWalker()) ) {s = 24;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index20_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 20, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA42_eotS =
        "\34\uffff";
    static final String DFA42_eofS =
        "\34\uffff";
    static final String DFA42_minS =
        "\1\5\21\uffff\7\0\3\uffff";
    static final String DFA42_maxS =
        "\1\150\21\uffff\7\0\3\uffff";
    static final String DFA42_acceptS =
        "\1\uffff\1\1\27\uffff\1\3\1\2\1\4";
    static final String DFA42_specialS =
        "\22\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\3\uffff}>";
    static final String[] DFA42_transitionS = {
            "\1\30\11\uffff\1\1\2\uffff\1\1\5\uffff\1\25\1\26\1\23\1\24\1"+
            "\27\5\uffff\1\1\1\uffff\3\1\17\uffff\1\1\10\uffff\4\1\6\uffff"+
            "\3\1\1\22\3\1\30\uffff\1\31",
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
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA42_eot = DFA.unpackEncodedString(DFA42_eotS);
    static final short[] DFA42_eof = DFA.unpackEncodedString(DFA42_eofS);
    static final char[] DFA42_min = DFA.unpackEncodedStringToUnsignedChars(DFA42_minS);
    static final char[] DFA42_max = DFA.unpackEncodedStringToUnsignedChars(DFA42_maxS);
    static final short[] DFA42_accept = DFA.unpackEncodedString(DFA42_acceptS);
    static final short[] DFA42_special = DFA.unpackEncodedString(DFA42_specialS);
    static final short[][] DFA42_transition;

    static {
        int numStates = DFA42_transitionS.length;
        DFA42_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA42_transition[i] = DFA.unpackEncodedString(DFA42_transitionS[i]);
        }
    }

    class DFA42 extends DFA {

        public DFA42(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 42;
            this.eot = DFA42_eot;
            this.eof = DFA42_eof;
            this.min = DFA42_min;
            this.max = DFA42_max;
            this.accept = DFA42_accept;
            this.special = DFA42_special;
            this.transition = DFA42_transition;
        }
        public String getDescription() {
            return "185:1: row_value : ( value_expression -> {$value_expression.st} | str= 'NULL' -> emitstr(str=$str) | str= 'DEFAULT' -> emitstr(str=$str) | query_expression -> {$query_expression.st});";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TreeNodeStream input = (TreeNodeStream)_input;
            int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA42_18 = input.LA(1);

                         
                        int index42_18 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (synpred78_SQL92QueryWalker()) ) {s = 26;}

                         
                        input.seek(index42_18);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA42_19 = input.LA(1);

                         
                        int index42_19 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_19);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA42_20 = input.LA(1);

                         
                        int index42_20 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_20);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA42_21 = input.LA(1);

                         
                        int index42_21 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_21);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA42_22 = input.LA(1);

                         
                        int index42_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_22);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA42_23 = input.LA(1);

                         
                        int index42_23 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_23);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA42_24 = input.LA(1);

                         
                        int index42_24 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred77_SQL92QueryWalker()) ) {s = 1;}

                        else if ( (true) ) {s = 27;}

                         
                        input.seek(index42_24);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 42, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_STATEMENT_in_statement87 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_statement89 = new BitSet(new long[]{0x0000000000000088L});
    public static final BitSet FOLLOW_order_by_in_statement93 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ORDER_in_order_by119 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_sort_specification_in_order_by124 = new BitSet(new long[]{0x0000000180000008L});
    public static final BitSet FOLLOW_UNION_ALL_in_query_expression153 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_query_expression157 = new BitSet(new long[]{0x000000001F000020L});
    public static final BitSet FOLLOW_query_expression_in_query_expression161 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXCEPT_ALL_in_query_expression201 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_query_expression205 = new BitSet(new long[]{0x000000001F000020L});
    public static final BitSet FOLLOW_query_expression_in_query_expression209 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UNION_in_query_expression250 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_EXCEPT_in_query_expression256 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INTERSECT_in_query_expression262 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_query_expression267 = new BitSet(new long[]{0x000000001F000020L});
    public static final BitSet FOLLOW_query_expression_in_query_expression271 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_in_query_expression314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUERY_in_query340 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_SELECT_LIST_in_query356 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_51_in_query361 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_43_in_query367 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_select_list_in_query371 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FROM_LIST_in_query388 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_in_query393 = new BitSet(new long[]{0x0000000000782008L});
    public static final BitSet FOLLOW_WHERE_in_query413 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_search_condition_in_query417 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GROUP_BY_in_query437 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_column_name_in_query441 = new BitSet(new long[]{0x0000000000040008L});
    public static final BitSet FOLLOW_HAVING_in_query462 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_search_condition_in_query466 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COLUMN_in_select_list558 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_54_in_select_list562 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_column_def_in_select_list588 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_COLUMN_in_column_def620 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_value_expression_in_column_def622 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COLUMN_in_column_def641 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_value_expression_in_column_def643 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_column_def645 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASC_in_sort_specification682 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DESC_in_sort_specification688 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_column_name_in_sort_specification691 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASC_in_sort_specification721 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_DESC_in_sort_specification727 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_sort_specification730 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_string_value_expression_in_value_expression772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_numeric_value_expression_in_value_expression787 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_numeric_value_expression813 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_74_in_numeric_value_expression819 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_numeric_value_expression_in_numeric_value_expression825 = new BitSet(new long[]{0x804000741F048028L,0x0000000000007E07L});
    public static final BitSet FOLLOW_numeric_value_expression_in_numeric_value_expression829 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_75_in_numeric_value_expression876 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_54_in_numeric_value_expression882 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_numeric_value_expression_in_numeric_value_expression887 = new BitSet(new long[]{0x804000741F048028L,0x0000000000007E07L});
    public static final BitSet FOLLOW_numeric_value_expression_in_numeric_value_expression891 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_numeric_primary_in_numeric_value_expression934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_numeric_primary965 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_74_in_numeric_primary969 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_value_expression_primary_in_numeric_primary972 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_value_expression_primary_in_numeric_primary997 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_value_expression_primary1017 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_column_name_in_value_expression_primary1031 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_value_expression_primary1045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_expression_in_value_expression_primary1059 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FUNCTION_in_function1092 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_function1094 = new BitSet(new long[]{0x804000741F048028L,0x000000000000FE07L});
    public static final BitSet FOLLOW_value_expression_in_function1099 = new BitSet(new long[]{0x804000741F048028L,0x000000000000FE07L});
    public static final BitSet FOLLOW_FUNCTION_in_function1127 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_function1129 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_54_in_function1133 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INT_in_literal1167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literal1186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMERIC_in_literal1206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetime_in_literal1244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interval_in_literal1258 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_literal1276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_literal1282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_literal1288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_79_in_string_value_expression1324 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_string_primary_in_string_value_expression1328 = new BitSet(new long[]{0x0000004000040008L});
    public static final BitSet FOLLOW_string_primary_in_string_value_expression1332 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_column_name_in_string_primary1365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_string_primary1379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_datetime1410 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_65_in_datetime1416 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_64_in_datetime1422 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_datetime1425 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_66_in_interval1454 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_interval1456 = new BitSet(new long[]{0x0000000000000000L,0x00000000000001F8L});
    public static final BitSet FOLLOW_67_in_interval1461 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_68_in_interval1467 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_69_in_interval1473 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_70_in_interval1479 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_71_in_interval1485 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_72_in_interval1491 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RIGHT_OUTER_JOIN_in_table1536 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_in_table1540 = new BitSet(new long[]{0x0000000000782008L});
    public static final BitSet FOLLOW_table_in_table1544 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_table1546 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LEFT_OUTER_JOIN_in_table1582 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_in_table1587 = new BitSet(new long[]{0x0000000000782008L});
    public static final BitSet FOLLOW_table_in_table1591 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_table1593 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FULL_OUTER_JOIN_in_table1629 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_in_table1634 = new BitSet(new long[]{0x0000000000782008L});
    public static final BitSet FOLLOW_table_in_table1638 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_table1640 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_JOIN_in_table1676 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_in_table1692 = new BitSet(new long[]{0x0000000000782008L});
    public static final BitSet FOLLOW_table_in_table1696 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_table1698 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_non_join_table_in_table1733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RELATION_in_non_join_table1757 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_name_in_non_join_table1759 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_non_join_table1776 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_name_in_non_join_table1778 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_non_join_table1780 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_non_join_table1806 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_function_in_non_join_table1808 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_non_join_table1824 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_function_in_non_join_table1826 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_non_join_table1828 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_non_join_table1854 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_non_join_table1856 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_non_join_table1858 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_in_table_function1897 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_table_function1899 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_relation_in_table_function1904 = new BitSet(new long[]{0x8000007400002008L,0x0000000000007007L});
    public static final BitSet FOLLOW_literal_in_table_function1911 = new BitSet(new long[]{0x8000007400000008L,0x0000000000007007L});
    public static final BitSet FOLLOW_RELATION_in_relation1947 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_name_in_relation1949 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_relation1965 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_function_in_relation1967 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_relation1983 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_in_relation1985 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_87_in_search_condition2016 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_search_condition_in_search_condition2020 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_search_condition2024 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_88_in_search_condition2057 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_search_condition_in_search_condition2061 = new BitSet(new long[]{0x0000000000810000L,0x0000009FF9800000L});
    public static final BitSet FOLLOW_search_condition_in_search_condition2065 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_boolean_term_in_search_condition2095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_predicate_in_boolean_term2117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NOT_in_boolean_term2134 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_predicate_in_boolean_term2136 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_comparison_predicate_in_predicate2170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_like_predicate_in_predicate2184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_in_predicate_in_predicate2204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_null_predicate_in_predicate2226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_exists_predicate_in_predicate2246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_between_predicate_in_predicate2264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IS_NULL_in_null_predicate2292 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_row_value_in_null_predicate2294 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_91_in_in_predicate2325 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_row_value_in_in_predicate2327 = new BitSet(new long[]{0x000000001F020020L});
    public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate2329 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_query_expression_in_in_predicate_tail2363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SET_in_in_predicate_tail2383 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_value_expression_in_in_predicate_tail2388 = new BitSet(new long[]{0x804000741F048028L,0x000000000000FE07L});
    public static final BitSet FOLLOW_92_in_between_predicate2414 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_row_value_in_between_predicate2418 = new BitSet(new long[]{0x804000741F048028L,0x000001000000FE07L});
    public static final BitSet FOLLOW_row_value_in_between_predicate2422 = new BitSet(new long[]{0x804000741F048028L,0x000001000000FE07L});
    public static final BitSet FOLLOW_row_value_in_between_predicate2426 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_93_in_exists_predicate2462 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_query_expression_in_exists_predicate2464 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_94_in_comparison_predicate2496 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_95_in_comparison_predicate2502 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_96_in_comparison_predicate2508 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_97_in_comparison_predicate2514 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_98_in_comparison_predicate2520 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_99_in_comparison_predicate2526 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_100_in_comparison_predicate2532 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2537 = new BitSet(new long[]{0x804000741F048028L,0x000001000000FE07L});
    public static final BitSet FOLLOW_row_value_in_comparison_predicate2541 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_103_in_like_predicate2590 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_row_value_in_like_predicate2594 = new BitSet(new long[]{0x804000741F048028L,0x000001000000FE07L});
    public static final BitSet FOLLOW_row_value_in_like_predicate2598 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_value_expression_in_row_value2663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_row_value2680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_row_value2708 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_expression_in_row_value2731 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_table_name2759 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TABLECOLUMN_in_column_name2782 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_column_name2784 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TABLECOLUMN_in_column_name2812 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_column_name2816 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_column_name2820 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COLUMN_in_synpred17_SQL92QueryWalker620 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_value_expression_in_synpred17_SQL92QueryWalker622 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_73_in_synpred23_SQL92QueryWalker813 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_74_in_synpred23_SQL92QueryWalker819 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_numeric_value_expression_in_synpred23_SQL92QueryWalker825 = new BitSet(new long[]{0x804000741F048028L,0x0000000000007E07L});
    public static final BitSet FOLLOW_numeric_value_expression_in_synpred23_SQL92QueryWalker829 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_synpred53_SQL92QueryWalker1757 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_name_in_synpred53_SQL92QueryWalker1759 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_synpred54_SQL92QueryWalker1776 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_name_in_synpred54_SQL92QueryWalker1778 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_synpred54_SQL92QueryWalker1780 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_synpred55_SQL92QueryWalker1806 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_function_in_synpred55_SQL92QueryWalker1808 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RELATION_in_synpred56_SQL92QueryWalker1824 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_table_function_in_synpred56_SQL92QueryWalker1826 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_ID_in_synpred56_SQL92QueryWalker1828 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_value_expression_in_synpred77_SQL92QueryWalker2663 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_synpred78_SQL92QueryWalker2680 = new BitSet(new long[]{0x0000000000000002L});

}