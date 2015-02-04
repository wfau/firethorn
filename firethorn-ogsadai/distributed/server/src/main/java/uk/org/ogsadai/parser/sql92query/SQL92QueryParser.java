// $ANTLR 3.5.1 /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g 2015-02-04 18:49:30

  package uk.org.ogsadai.parser.sql92query; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class SQL92QueryParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASC", "BOUND", "CAST", "COLUMN", 
		"DESC", "EXCEPT", "EXCEPT_ALL", "FLOAT", "FROM_LIST", "FULL_OUTER_JOIN", 
		"FUNCTION", "GROUP_BY", "HAVING", "ID", "INT", "INTERSECT", "IS_NULL", 
		"JOIN", "LEFT_OUTER_JOIN", "LIMIT", "NOT", "NUMERIC", "ORDER", "QUERY", 
		"RELATION", "RIGHT_OUTER_JOIN", "SELECT_LIST", "SET", "SETOP", "STATEMENT", 
		"STRING", "TABLECOLUMN", "TOP", "UNION", "UNION_ALL", "WHERE", "WS", "'!='", 
		"'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "';'", "'<'", 
		"'<='", "'<>'", "'='", "'>'", "'>='", "'@'", "'ALL'", "'AND'", "'ANY'", 
		"'AS'", "'ASC'", "'BETWEEN'", "'BY'", "'CAST'", "'DATE'", "'DAY'", "'DEFAULT'", 
		"'DESC'", "'DISTINCT'", "'EXCEPT'", "'EXISTS'", "'FALSE'", "'FROM'", "'FULL'", 
		"'GROUP BY'", "'HAVING'", "'HOUR'", "'IN'", "'INNER'", "'INTERSECT'", 
		"'INTERVAL'", "'IS'", "'JOIN'", "'LEFT'", "'LIKE'", "'LIMIT'", "'MINUTE'", 
		"'MONTH'", "'NOT'", "'NULL'", "'ON'", "'OR'", "'ORDER'", "'OUTER'", "'RIGHT'", 
		"'SECOND'", "'SELECT'", "'SOME'", "'TIME'", "'TIMESTAMP'", "'TOP'", "'TRUE'", 
		"'UNION'", "'WHERE'", "'YEAR'", "'||'"
	};
	public static final int EOF=-1;
	public static final int T__41=41;
	public static final int T__42=42;
	public static final int T__43=43;
	public static final int T__44=44;
	public static final int T__45=45;
	public static final int T__46=46;
	public static final int T__47=47;
	public static final int T__48=48;
	public static final int T__49=49;
	public static final int T__50=50;
	public static final int T__51=51;
	public static final int T__52=52;
	public static final int T__53=53;
	public static final int T__54=54;
	public static final int T__55=55;
	public static final int T__56=56;
	public static final int T__57=57;
	public static final int T__58=58;
	public static final int T__59=59;
	public static final int T__60=60;
	public static final int T__61=61;
	public static final int T__62=62;
	public static final int T__63=63;
	public static final int T__64=64;
	public static final int T__65=65;
	public static final int T__66=66;
	public static final int T__67=67;
	public static final int T__68=68;
	public static final int T__69=69;
	public static final int T__70=70;
	public static final int T__71=71;
	public static final int T__72=72;
	public static final int T__73=73;
	public static final int T__74=74;
	public static final int T__75=75;
	public static final int T__76=76;
	public static final int T__77=77;
	public static final int T__78=78;
	public static final int T__79=79;
	public static final int T__80=80;
	public static final int T__81=81;
	public static final int T__82=82;
	public static final int T__83=83;
	public static final int T__84=84;
	public static final int T__85=85;
	public static final int T__86=86;
	public static final int T__87=87;
	public static final int T__88=88;
	public static final int T__89=89;
	public static final int T__90=90;
	public static final int T__91=91;
	public static final int T__92=92;
	public static final int T__93=93;
	public static final int T__94=94;
	public static final int T__95=95;
	public static final int T__96=96;
	public static final int T__97=97;
	public static final int T__98=98;
	public static final int T__99=99;
	public static final int T__100=100;
	public static final int T__101=101;
	public static final int T__102=102;
	public static final int T__103=103;
	public static final int T__104=104;
	public static final int T__105=105;
	public static final int T__106=106;
	public static final int T__107=107;
	public static final int ASC=4;
	public static final int BOUND=5;
	public static final int CAST=6;
	public static final int COLUMN=7;
	public static final int DESC=8;
	public static final int EXCEPT=9;
	public static final int EXCEPT_ALL=10;
	public static final int FLOAT=11;
	public static final int FROM_LIST=12;
	public static final int FULL_OUTER_JOIN=13;
	public static final int FUNCTION=14;
	public static final int GROUP_BY=15;
	public static final int HAVING=16;
	public static final int ID=17;
	public static final int INT=18;
	public static final int INTERSECT=19;
	public static final int IS_NULL=20;
	public static final int JOIN=21;
	public static final int LEFT_OUTER_JOIN=22;
	public static final int LIMIT=23;
	public static final int NOT=24;
	public static final int NUMERIC=25;
	public static final int ORDER=26;
	public static final int QUERY=27;
	public static final int RELATION=28;
	public static final int RIGHT_OUTER_JOIN=29;
	public static final int SELECT_LIST=30;
	public static final int SET=31;
	public static final int SETOP=32;
	public static final int STATEMENT=33;
	public static final int STRING=34;
	public static final int TABLECOLUMN=35;
	public static final int TOP=36;
	public static final int UNION=37;
	public static final int UNION_ALL=38;
	public static final int WHERE=39;
	public static final int WS=40;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

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
	@Override public String[] getTokenNames() { return SQL92QueryParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g"; }


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
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "statement"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:96:1: statement : query_expression ( order_by )? ( limit )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ( limit )? ) ;
	public final SQL92QueryParser.statement_return statement() throws RecognitionException {
		SQL92QueryParser.statement_return retval = new SQL92QueryParser.statement_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal4=null;
		Token EOF5=null;
		ParserRuleReturnScope query_expression1 =null;
		ParserRuleReturnScope order_by2 =null;
		ParserRuleReturnScope limit3 =null;

		CommonTree char_literal4_tree=null;
		CommonTree EOF5_tree=null;
		RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_limit=new RewriteRuleSubtreeStream(adaptor,"rule limit");
		RewriteRuleSubtreeStream stream_order_by=new RewriteRuleSubtreeStream(adaptor,"rule order_by");
		RewriteRuleSubtreeStream stream_query_expression=new RewriteRuleSubtreeStream(adaptor,"rule query_expression");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:5: ( query_expression ( order_by )? ( limit )? ( ';' )? EOF -> ^( STATEMENT query_expression ( order_by )? ( limit )? ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:9: query_expression ( order_by )? ( limit )? ( ';' )? EOF
			{
			pushFollow(FOLLOW_query_expression_in_statement322);
			query_expression1=query_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_query_expression.add(query_expression1.getTree());
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:26: ( order_by )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==94) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:26: order_by
					{
					pushFollow(FOLLOW_order_by_in_statement324);
					order_by2=order_by();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_order_by.add(order_by2.getTree());
					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:36: ( limit )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==87) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:36: limit
					{
					pushFollow(FOLLOW_limit_in_statement327);
					limit3=limit();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_limit.add(limit3.getTree());
					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:43: ( ';' )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==50) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:43: ';'
					{
					char_literal4=(Token)match(input,50,FOLLOW_50_in_statement330); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_50.add(char_literal4);

					}
					break;

			}

			EOF5=(Token)match(input,EOF,FOLLOW_EOF_in_statement333); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_EOF.add(EOF5);

			// AST REWRITE
			// elements: limit, query_expression, order_by
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 97:52: -> ^( STATEMENT query_expression ( order_by )? ( limit )? )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:55: ^( STATEMENT query_expression ( order_by )? ( limit )? )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STATEMENT, "STATEMENT"), root_1);
				adaptor.addChild(root_1, stream_query_expression.nextTree());
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:84: ( order_by )?
				if ( stream_order_by.hasNext() ) {
					adaptor.addChild(root_1, stream_order_by.nextTree());
				}
				stream_order_by.reset();

				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:97:94: ( limit )?
				if ( stream_limit.hasNext() ) {
					adaptor.addChild(root_1, stream_limit.nextTree());
				}
				stream_limit.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "statement"


	public static class limit_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "limit"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:100:1: limit : 'LIMIT' INT -> ^( LIMIT INT ) ;
	public final SQL92QueryParser.limit_return limit() throws RecognitionException {
		SQL92QueryParser.limit_return retval = new SQL92QueryParser.limit_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal6=null;
		Token INT7=null;

		CommonTree string_literal6_tree=null;
		CommonTree INT7_tree=null;
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");
		RewriteRuleTokenStream stream_87=new RewriteRuleTokenStream(adaptor,"token 87");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:101:5: ( 'LIMIT' INT -> ^( LIMIT INT ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:101:7: 'LIMIT' INT
			{
			string_literal6=(Token)match(input,87,FOLLOW_87_in_limit370); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_87.add(string_literal6);

			INT7=(Token)match(input,INT,FOLLOW_INT_in_limit372); if (state.failed) return retval; 
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 101:19: -> ^( LIMIT INT )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:101:22: ^( LIMIT INT )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(LIMIT, "LIMIT"), root_1);
				adaptor.addChild(root_1, stream_INT.nextNode());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "limit"


	public static class top_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "top"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:103:1: top : 'TOP' INT -> ^( TOP INT ) ;
	public final SQL92QueryParser.top_return top() throws RecognitionException {
		SQL92QueryParser.top_return retval = new SQL92QueryParser.top_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal8=null;
		Token INT9=null;

		CommonTree string_literal8_tree=null;
		CommonTree INT9_tree=null;
		RewriteRuleTokenStream stream_102=new RewriteRuleTokenStream(adaptor,"token 102");
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:104:5: ( 'TOP' INT -> ^( TOP INT ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:104:7: 'TOP' INT
			{
			string_literal8=(Token)match(input,102,FOLLOW_102_in_top398); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_102.add(string_literal8);

			INT9=(Token)match(input,INT,FOLLOW_INT_in_top400); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_INT.add(INT9);

			// AST REWRITE
			// elements: INT
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 104:17: -> ^( TOP INT )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:104:20: ^( TOP INT )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TOP, "TOP"), root_1);
				adaptor.addChild(root_1, stream_INT.nextNode());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "top"


	public static class query_expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "query_expression"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:106:1: query_expression : query ( set_op ^ query )* ;
	public final SQL92QueryParser.query_expression_return query_expression() throws RecognitionException {
		SQL92QueryParser.query_expression_return retval = new SQL92QueryParser.query_expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope query10 =null;
		ParserRuleReturnScope set_op11 =null;
		ParserRuleReturnScope query12 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:5: ( query ( set_op ^ query )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:9: query ( set_op ^ query )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_query_in_query_expression429);
			query10=query();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, query10.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:15: ( set_op ^ query )*
			loop4:
			while (true) {
				int alt4=2;
				int LA4_0 = input.LA(1);
				if ( (LA4_0==71||LA4_0==81||LA4_0==104) ) {
					alt4=1;
				}

				switch (alt4) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:107:16: set_op ^ query
					{
					pushFollow(FOLLOW_set_op_in_query_expression432);
					set_op11=set_op();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(set_op11.getTree(), root_0);
					pushFollow(FOLLOW_query_in_query_expression435);
					query12=query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, query12.getTree());

					}
					break;

				default :
					break loop4;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "query_expression"


	public static class set_op_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "set_op"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:109:1: set_op : ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) );
	public final SQL92QueryParser.set_op_return set_op() throws RecognitionException {
		SQL92QueryParser.set_op_return retval = new SQL92QueryParser.set_op_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal13=null;
		Token string_literal14=null;
		Token string_literal15=null;
		Token string_literal16=null;
		Token string_literal17=null;
		Token string_literal18=null;
		Token string_literal19=null;

		CommonTree string_literal13_tree=null;
		CommonTree string_literal14_tree=null;
		CommonTree string_literal15_tree=null;
		CommonTree string_literal16_tree=null;
		CommonTree string_literal17_tree=null;
		CommonTree string_literal18_tree=null;
		CommonTree string_literal19_tree=null;
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_104=new RewriteRuleTokenStream(adaptor,"token 104");
		RewriteRuleTokenStream stream_81=new RewriteRuleTokenStream(adaptor,"token 81");
		RewriteRuleTokenStream stream_71=new RewriteRuleTokenStream(adaptor,"token 71");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:110:5: ( 'UNION' 'ALL' -> ^( UNION_ALL ) | 'UNION' -> ^( UNION ) | 'EXCEPT' 'ALL' -> ^( EXCEPT_ALL ) | 'EXCEPT' -> ^( EXCEPT ) | 'INTERSECT' -> ^( INTERSECT ) )
			int alt5=5;
			switch ( input.LA(1) ) {
			case 104:
				{
				int LA5_1 = input.LA(2);
				if ( (LA5_1==58) ) {
					alt5=1;
				}
				else if ( (LA5_1==42||LA5_1==98) ) {
					alt5=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 5, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 71:
				{
				int LA5_2 = input.LA(2);
				if ( (LA5_2==58) ) {
					alt5=3;
				}
				else if ( (LA5_2==42||LA5_2==98) ) {
					alt5=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 5, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 81:
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:110:9: 'UNION' 'ALL'
					{
					string_literal13=(Token)match(input,104,FOLLOW_104_in_set_op457); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_104.add(string_literal13);

					string_literal14=(Token)match(input,58,FOLLOW_58_in_set_op459); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(string_literal14);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 110:23: -> ^( UNION_ALL )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:110:26: ^( UNION_ALL )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION_ALL, "UNION_ALL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:111:9: 'UNION'
					{
					string_literal15=(Token)match(input,104,FOLLOW_104_in_set_op475); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_104.add(string_literal15);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 111:17: -> ^( UNION )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:111:20: ^( UNION )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(UNION, "UNION"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:112:9: 'EXCEPT' 'ALL'
					{
					string_literal16=(Token)match(input,71,FOLLOW_71_in_set_op491); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_71.add(string_literal16);

					string_literal17=(Token)match(input,58,FOLLOW_58_in_set_op493); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_58.add(string_literal17);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 112:24: -> ^( EXCEPT_ALL )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:112:27: ^( EXCEPT_ALL )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT_ALL, "EXCEPT_ALL"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:113:9: 'EXCEPT'
					{
					string_literal18=(Token)match(input,71,FOLLOW_71_in_set_op509); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_71.add(string_literal18);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 113:18: -> ^( EXCEPT )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:113:21: ^( EXCEPT )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(EXCEPT, "EXCEPT"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:9: 'INTERSECT'
					{
					string_literal19=(Token)match(input,81,FOLLOW_81_in_set_op525); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_81.add(string_literal19);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 114:21: -> ^( INTERSECT )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:114:24: ^( INTERSECT )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(INTERSECT, "INTERSECT"), root_1);
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "set_op"


	public static class query_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "query"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:116:1: query : ( sub_query | 'SELECT' ( top )? ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( top )? ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) );
	public final SQL92QueryParser.query_return query() throws RecognitionException {
		SQL92QueryParser.query_return retval = new SQL92QueryParser.query_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal21=null;
		Token string_literal25=null;
		Token string_literal27=null;
		Token string_literal28=null;
		Token string_literal30=null;
		ParserRuleReturnScope s1 =null;
		ParserRuleReturnScope s2 =null;
		ParserRuleReturnScope sub_query20 =null;
		ParserRuleReturnScope top22 =null;
		ParserRuleReturnScope set_quantifier23 =null;
		ParserRuleReturnScope select_list24 =null;
		ParserRuleReturnScope table_expression26 =null;
		ParserRuleReturnScope column_list29 =null;

		CommonTree string_literal21_tree=null;
		CommonTree string_literal25_tree=null;
		CommonTree string_literal27_tree=null;
		CommonTree string_literal28_tree=null;
		CommonTree string_literal30_tree=null;
		RewriteRuleTokenStream stream_77=new RewriteRuleTokenStream(adaptor,"token 77");
		RewriteRuleTokenStream stream_105=new RewriteRuleTokenStream(adaptor,"token 105");
		RewriteRuleTokenStream stream_74=new RewriteRuleTokenStream(adaptor,"token 74");
		RewriteRuleTokenStream stream_76=new RewriteRuleTokenStream(adaptor,"token 76");
		RewriteRuleTokenStream stream_98=new RewriteRuleTokenStream(adaptor,"token 98");
		RewriteRuleSubtreeStream stream_table_expression=new RewriteRuleSubtreeStream(adaptor,"rule table_expression");
		RewriteRuleSubtreeStream stream_select_list=new RewriteRuleSubtreeStream(adaptor,"rule select_list");
		RewriteRuleSubtreeStream stream_top=new RewriteRuleSubtreeStream(adaptor,"rule top");
		RewriteRuleSubtreeStream stream_set_quantifier=new RewriteRuleSubtreeStream(adaptor,"rule set_quantifier");
		RewriteRuleSubtreeStream stream_search_condition=new RewriteRuleSubtreeStream(adaptor,"rule search_condition");
		RewriteRuleSubtreeStream stream_column_list=new RewriteRuleSubtreeStream(adaptor,"rule column_list");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:117:5: ( sub_query | 'SELECT' ( top )? ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )? -> ^( QUERY ^( SELECT_LIST ( top )? ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? ) )
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==42) ) {
				alt11=1;
			}
			else if ( (LA11_0==98) ) {
				alt11=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 11, 0, input);
				throw nvae;
			}

			switch (alt11) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:117:9: sub_query
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_sub_query_in_query552);
					sub_query20=sub_query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query20.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:9: 'SELECT' ( top )? ( set_quantifier )? select_list 'FROM' table_expression ( 'WHERE' s1= search_condition )? ( 'GROUP BY' column_list )? ( 'HAVING' s2= search_condition )?
					{
					string_literal21=(Token)match(input,98,FOLLOW_98_in_query562); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_98.add(string_literal21);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:18: ( top )?
					int alt6=2;
					int LA6_0 = input.LA(1);
					if ( (LA6_0==102) ) {
						alt6=1;
					}
					switch (alt6) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:18: top
							{
							pushFollow(FOLLOW_top_in_query564);
							top22=top();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_top.add(top22.getTree());
							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:23: ( set_quantifier )?
					int alt7=2;
					int LA7_0 = input.LA(1);
					if ( (LA7_0==58||LA7_0==70) ) {
						alt7=1;
					}
					switch (alt7) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:23: set_quantifier
							{
							pushFollow(FOLLOW_set_quantifier_in_query567);
							set_quantifier23=set_quantifier();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_set_quantifier.add(set_quantifier23.getTree());
							}
							break;

					}

					pushFollow(FOLLOW_select_list_in_query570);
					select_list24=select_list();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_list.add(select_list24.getTree());
					string_literal25=(Token)match(input,74,FOLLOW_74_in_query572); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_74.add(string_literal25);

					pushFollow(FOLLOW_table_expression_in_query574);
					table_expression26=table_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_expression.add(table_expression26.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:75: ( 'WHERE' s1= search_condition )?
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==105) ) {
						alt8=1;
					}
					switch (alt8) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:76: 'WHERE' s1= search_condition
							{
							string_literal27=(Token)match(input,105,FOLLOW_105_in_query577); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_105.add(string_literal27);

							pushFollow(FOLLOW_search_condition_in_query581);
							s1=search_condition();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_search_condition.add(s1.getTree());
							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:106: ( 'GROUP BY' column_list )?
					int alt9=2;
					int LA9_0 = input.LA(1);
					if ( (LA9_0==76) ) {
						alt9=1;
					}
					switch (alt9) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:107: 'GROUP BY' column_list
							{
							string_literal28=(Token)match(input,76,FOLLOW_76_in_query586); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_76.add(string_literal28);

							pushFollow(FOLLOW_column_list_in_query588);
							column_list29=column_list();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_column_list.add(column_list29.getTree());
							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:132: ( 'HAVING' s2= search_condition )?
					int alt10=2;
					int LA10_0 = input.LA(1);
					if ( (LA10_0==77) ) {
						alt10=1;
					}
					switch (alt10) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:118:133: 'HAVING' s2= search_condition
							{
							string_literal30=(Token)match(input,77,FOLLOW_77_in_query593); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_77.add(string_literal30);

							pushFollow(FOLLOW_search_condition_in_query597);
							s2=search_condition();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_search_condition.add(s2.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: s2, set_quantifier, table_expression, s1, column_list, top, select_list
					// token labels: 
					// rule labels: s1, retval, s2
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_s1=new RewriteRuleSubtreeStream(adaptor,"rule s1",s1!=null?s1.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
					RewriteRuleSubtreeStream stream_s2=new RewriteRuleSubtreeStream(adaptor,"rule s2",s2!=null?s2.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 119:13: -> ^( QUERY ^( SELECT_LIST ( top )? ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:16: ^( QUERY ^( SELECT_LIST ( top )? ( set_quantifier )? select_list ) ^( FROM_LIST table_expression ) ( ^( WHERE $s1) )? ( ^( GROUP_BY column_list ) )? ( ^( HAVING $s2) )? )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(QUERY, "QUERY"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:24: ^( SELECT_LIST ( top )? ( set_quantifier )? select_list )
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SELECT_LIST, "SELECT_LIST"), root_2);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:38: ( top )?
						if ( stream_top.hasNext() ) {
							adaptor.addChild(root_2, stream_top.nextTree());
						}
						stream_top.reset();

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:43: ( set_quantifier )?
						if ( stream_set_quantifier.hasNext() ) {
							adaptor.addChild(root_2, stream_set_quantifier.nextTree());
						}
						stream_set_quantifier.reset();

						adaptor.addChild(root_2, stream_select_list.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:72: ^( FROM_LIST table_expression )
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FROM_LIST, "FROM_LIST"), root_2);
						adaptor.addChild(root_2, stream_table_expression.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:102: ( ^( WHERE $s1) )?
						if ( stream_s1.hasNext() ) {
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:102: ^( WHERE $s1)
							{
							CommonTree root_2 = (CommonTree)adaptor.nil();
							root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(WHERE, "WHERE"), root_2);
							adaptor.addChild(root_2, stream_s1.nextTree());
							adaptor.addChild(root_1, root_2);
							}

						}
						stream_s1.reset();

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:116: ( ^( GROUP_BY column_list ) )?
						if ( stream_column_list.hasNext() ) {
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:116: ^( GROUP_BY column_list )
							{
							CommonTree root_2 = (CommonTree)adaptor.nil();
							root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(GROUP_BY, "GROUP_BY"), root_2);
							adaptor.addChild(root_2, stream_column_list.nextTree());
							adaptor.addChild(root_1, root_2);
							}

						}
						stream_column_list.reset();

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:141: ( ^( HAVING $s2) )?
						if ( stream_s2.hasNext() ) {
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:119:141: ^( HAVING $s2)
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


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "query"


	public static class set_quantifier_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "set_quantifier"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:122:1: set_quantifier : ( 'DISTINCT' | 'ALL' );
	public final SQL92QueryParser.set_quantifier_return set_quantifier() throws RecognitionException {
		SQL92QueryParser.set_quantifier_return retval = new SQL92QueryParser.set_quantifier_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token set31=null;

		CommonTree set31_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:123:5: ( 'DISTINCT' | 'ALL' )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:
			{
			root_0 = (CommonTree)adaptor.nil();


			set31=input.LT(1);
			if ( input.LA(1)==58||input.LA(1)==70 ) {
				input.consume();
				if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set31));
				state.errorRecovery=false;
				state.failed=false;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "set_quantifier"


	public static class sub_query_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "sub_query"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:125:1: sub_query : '(' ! query_expression ')' !;
	public final SQL92QueryParser.sub_query_return sub_query() throws RecognitionException {
		SQL92QueryParser.sub_query_return retval = new SQL92QueryParser.sub_query_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal32=null;
		Token char_literal34=null;
		ParserRuleReturnScope query_expression33 =null;

		CommonTree char_literal32_tree=null;
		CommonTree char_literal34_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:126:5: ( '(' ! query_expression ')' !)
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:126:9: '(' ! query_expression ')' !
			{
			root_0 = (CommonTree)adaptor.nil();


			char_literal32=(Token)match(input,42,FOLLOW_42_in_sub_query703); if (state.failed) return retval;
			pushFollow(FOLLOW_query_expression_in_sub_query706);
			query_expression33=query_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, query_expression33.getTree());

			char_literal34=(Token)match(input,43,FOLLOW_43_in_sub_query708); if (state.failed) return retval;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sub_query"


	public static class select_list_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "select_list"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:128:1: select_list : ( derived_column ( ',' ! derived_column )* | '*' -> ^( COLUMN '*' ) );
	public final SQL92QueryParser.select_list_return select_list() throws RecognitionException {
		SQL92QueryParser.select_list_return retval = new SQL92QueryParser.select_list_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal36=null;
		Token char_literal38=null;
		ParserRuleReturnScope derived_column35 =null;
		ParserRuleReturnScope derived_column37 =null;

		CommonTree char_literal36_tree=null;
		CommonTree char_literal38_tree=null;
		RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:5: ( derived_column ( ',' ! derived_column )* | '*' -> ^( COLUMN '*' ) )
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==FLOAT||(LA13_0 >= ID && LA13_0 <= INT)||LA13_0==NUMERIC||LA13_0==STRING||LA13_0==42||LA13_0==45||LA13_0==47||(LA13_0 >= 65 && LA13_0 <= 67)||LA13_0==73||LA13_0==78||LA13_0==82||(LA13_0 >= 88 && LA13_0 <= 89)||LA13_0==91||LA13_0==97||(LA13_0 >= 100 && LA13_0 <= 101)||LA13_0==103||LA13_0==106) ) {
				alt13=1;
			}
			else if ( (LA13_0==44) ) {
				alt13=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 13, 0, input);
				throw nvae;
			}

			switch (alt13) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:9: derived_column ( ',' ! derived_column )*
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_derived_column_in_select_list727);
					derived_column35=derived_column();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column35.getTree());

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:24: ( ',' ! derived_column )*
					loop12:
					while (true) {
						int alt12=2;
						int LA12_0 = input.LA(1);
						if ( (LA12_0==46) ) {
							alt12=1;
						}

						switch (alt12) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:129:25: ',' ! derived_column
							{
							char_literal36=(Token)match(input,46,FOLLOW_46_in_select_list730); if (state.failed) return retval;
							pushFollow(FOLLOW_derived_column_in_select_list733);
							derived_column37=derived_column();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_column37.getTree());

							}
							break;

						default :
							break loop12;
						}
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:9: '*'
					{
					char_literal38=(Token)match(input,44,FOLLOW_44_in_select_list745); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_44.add(char_literal38);

					// AST REWRITE
					// elements: 44
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 130:13: -> ^( COLUMN '*' )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:130:16: ^( COLUMN '*' )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);
						adaptor.addChild(root_1, stream_44.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select_list"


	public static class derived_column_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "derived_column"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:132:1: derived_column : ( 'CAST' '(' value_expression 'AS' id1= ID ')' ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) );
	public final SQL92QueryParser.derived_column_return derived_column() throws RecognitionException {
		SQL92QueryParser.derived_column_return retval = new SQL92QueryParser.derived_column_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token id1=null;
		Token id2=null;
		Token string_literal39=null;
		Token char_literal40=null;
		Token string_literal42=null;
		Token char_literal43=null;
		Token string_literal44=null;
		Token string_literal46=null;
		Token ID47=null;
		ParserRuleReturnScope value_expression41 =null;
		ParserRuleReturnScope value_expression45 =null;

		CommonTree id1_tree=null;
		CommonTree id2_tree=null;
		CommonTree string_literal39_tree=null;
		CommonTree char_literal40_tree=null;
		CommonTree string_literal42_tree=null;
		CommonTree char_literal43_tree=null;
		CommonTree string_literal44_tree=null;
		CommonTree string_literal46_tree=null;
		CommonTree ID47_tree=null;
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_61=new RewriteRuleTokenStream(adaptor,"token 61");
		RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleTokenStream stream_65=new RewriteRuleTokenStream(adaptor,"token 65");
		RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:5: ( 'CAST' '(' value_expression 'AS' id1= ID ')' ( ( 'AS' )? id2= ID )? -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? ) | value_expression ( ( 'AS' )? ID )? -> ^( COLUMN value_expression ( ID )? ) )
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0==65) ) {
				alt18=1;
			}
			else if ( (LA18_0==FLOAT||(LA18_0 >= ID && LA18_0 <= INT)||LA18_0==NUMERIC||LA18_0==STRING||LA18_0==42||LA18_0==45||LA18_0==47||(LA18_0 >= 66 && LA18_0 <= 67)||LA18_0==73||LA18_0==78||LA18_0==82||(LA18_0 >= 88 && LA18_0 <= 89)||LA18_0==91||LA18_0==97||(LA18_0 >= 100 && LA18_0 <= 101)||LA18_0==103||LA18_0==106) ) {
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:7: 'CAST' '(' value_expression 'AS' id1= ID ')' ( ( 'AS' )? id2= ID )?
					{
					string_literal39=(Token)match(input,65,FOLLOW_65_in_derived_column769); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_65.add(string_literal39);

					char_literal40=(Token)match(input,42,FOLLOW_42_in_derived_column772); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_42.add(char_literal40);

					pushFollow(FOLLOW_value_expression_in_derived_column774);
					value_expression41=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_value_expression.add(value_expression41.getTree());
					string_literal42=(Token)match(input,61,FOLLOW_61_in_derived_column776); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_61.add(string_literal42);

					id1=(Token)match(input,ID,FOLLOW_ID_in_derived_column780); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(id1);

					char_literal43=(Token)match(input,43,FOLLOW_43_in_derived_column782); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_43.add(char_literal43);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:52: ( ( 'AS' )? id2= ID )?
					int alt15=2;
					int LA15_0 = input.LA(1);
					if ( (LA15_0==ID||LA15_0==61) ) {
						alt15=1;
					}
					switch (alt15) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:53: ( 'AS' )? id2= ID
							{
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:53: ( 'AS' )?
							int alt14=2;
							int LA14_0 = input.LA(1);
							if ( (LA14_0==61) ) {
								alt14=1;
							}
							switch (alt14) {
								case 1 :
									// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:53: 'AS'
									{
									string_literal44=(Token)match(input,61,FOLLOW_61_in_derived_column785); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_61.add(string_literal44);

									}
									break;

							}

							id2=(Token)match(input,ID,FOLLOW_ID_in_derived_column790); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(id2);

							}
							break;

					}

					// AST REWRITE
					// elements: id2, value_expression, id1
					// token labels: id2, id1
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleTokenStream stream_id2=new RewriteRuleTokenStream(adaptor,"token id2",id2);
					RewriteRuleTokenStream stream_id1=new RewriteRuleTokenStream(adaptor,"token id1",id1);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 133:68: -> ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:71: ^( COLUMN ^( CAST value_expression $id1) ( $id2)? )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:80: ^( CAST value_expression $id1)
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(CAST, "CAST"), root_2);
						adaptor.addChild(root_2, stream_value_expression.nextTree());
						adaptor.addChild(root_2, stream_id1.nextNode());
						adaptor.addChild(root_1, root_2);
						}

						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:133:111: ( $id2)?
						if ( stream_id2.hasNext() ) {
							adaptor.addChild(root_1, stream_id2.nextNode());
						}
						stream_id2.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:9: value_expression ( ( 'AS' )? ID )?
					{
					pushFollow(FOLLOW_value_expression_in_derived_column821);
					value_expression45=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_value_expression.add(value_expression45.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:26: ( ( 'AS' )? ID )?
					int alt17=2;
					int LA17_0 = input.LA(1);
					if ( (LA17_0==ID||LA17_0==61) ) {
						alt17=1;
					}
					switch (alt17) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:27: ( 'AS' )? ID
							{
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:27: ( 'AS' )?
							int alt16=2;
							int LA16_0 = input.LA(1);
							if ( (LA16_0==61) ) {
								alt16=1;
							}
							switch (alt16) {
								case 1 :
									// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:27: 'AS'
									{
									string_literal46=(Token)match(input,61,FOLLOW_61_in_derived_column824); if (state.failed) return retval; 
									if ( state.backtracking==0 ) stream_61.add(string_literal46);

									}
									break;

							}

							ID47=(Token)match(input,ID,FOLLOW_ID_in_derived_column827); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(ID47);

							}
							break;

					}

					// AST REWRITE
					// elements: value_expression, ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 134:38: -> ^( COLUMN value_expression ( ID )? )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:41: ^( COLUMN value_expression ( ID )? )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(COLUMN, "COLUMN"), root_1);
						adaptor.addChild(root_1, stream_value_expression.nextTree());
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:134:67: ( ID )?
						if ( stream_ID.hasNext() ) {
							adaptor.addChild(root_1, stream_ID.nextNode());
						}
						stream_ID.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "derived_column"


	public static class order_by_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "order_by"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:136:1: order_by : 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )* -> ^( ORDER ( ordered_sort_spec )+ ) ;
	public final SQL92QueryParser.order_by_return order_by() throws RecognitionException {
		SQL92QueryParser.order_by_return retval = new SQL92QueryParser.order_by_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal48=null;
		Token string_literal49=null;
		Token char_literal51=null;
		ParserRuleReturnScope ordered_sort_spec50 =null;
		ParserRuleReturnScope ordered_sort_spec52 =null;

		CommonTree string_literal48_tree=null;
		CommonTree string_literal49_tree=null;
		CommonTree char_literal51_tree=null;
		RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
		RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
		RewriteRuleTokenStream stream_64=new RewriteRuleTokenStream(adaptor,"token 64");
		RewriteRuleSubtreeStream stream_ordered_sort_spec=new RewriteRuleSubtreeStream(adaptor,"rule ordered_sort_spec");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:137:5: ( 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )* -> ^( ORDER ( ordered_sort_spec )+ ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:137:9: 'ORDER' 'BY' ordered_sort_spec ( ',' ordered_sort_spec )*
			{
			string_literal48=(Token)match(input,94,FOLLOW_94_in_order_by861); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_94.add(string_literal48);

			string_literal49=(Token)match(input,64,FOLLOW_64_in_order_by863); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_64.add(string_literal49);

			pushFollow(FOLLOW_ordered_sort_spec_in_order_by865);
			ordered_sort_spec50=ordered_sort_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_ordered_sort_spec.add(ordered_sort_spec50.getTree());
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:137:40: ( ',' ordered_sort_spec )*
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( (LA19_0==46) ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:137:41: ',' ordered_sort_spec
					{
					char_literal51=(Token)match(input,46,FOLLOW_46_in_order_by868); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_46.add(char_literal51);

					pushFollow(FOLLOW_ordered_sort_spec_in_order_by870);
					ordered_sort_spec52=ordered_sort_spec();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_ordered_sort_spec.add(ordered_sort_spec52.getTree());
					}
					break;

				default :
					break loop19;
				}
			}

			// AST REWRITE
			// elements: ordered_sort_spec
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 137:65: -> ^( ORDER ( ordered_sort_spec )+ )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:137:68: ^( ORDER ( ordered_sort_spec )+ )
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


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "order_by"


	public static class sort_spec_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "sort_spec"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:139:1: sort_spec : ( column_name | INT | reserved_word_column_name );
	public final SQL92QueryParser.sort_spec_return sort_spec() throws RecognitionException {
		SQL92QueryParser.sort_spec_return retval = new SQL92QueryParser.sort_spec_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token INT54=null;
		ParserRuleReturnScope column_name53 =null;
		ParserRuleReturnScope reserved_word_column_name55 =null;

		CommonTree INT54_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:5: ( column_name | INT | reserved_word_column_name )
			int alt20=3;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA20_1 = input.LA(2);
				if ( (LA20_1==48) ) {
					int LA20_4 = input.LA(3);
					if ( (LA20_4==ID) ) {
						alt20=1;
					}
					else if ( ((LA20_4 >= 66 && LA20_4 <= 67)||LA20_4==78||LA20_4==82||(LA20_4 >= 88 && LA20_4 <= 89)||LA20_4==97||(LA20_4 >= 100 && LA20_4 <= 101)||LA20_4==106) ) {
						alt20=3;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 20, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA20_1==EOF||LA20_1==46||LA20_1==50||LA20_1==62||LA20_1==69||LA20_1==87) ) {
					alt20=1;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 20, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case INT:
				{
				alt20=2;
				}
				break;
			case 66:
			case 67:
			case 78:
			case 82:
			case 88:
			case 89:
			case 97:
			case 100:
			case 101:
			case 106:
				{
				alt20=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 20, 0, input);
				throw nvae;
			}
			switch (alt20) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:9: column_name
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_column_name_in_sort_spec899);
					column_name53=column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name53.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:23: INT
					{
					root_0 = (CommonTree)adaptor.nil();


					INT54=(Token)match(input,INT,FOLLOW_INT_in_sort_spec903); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INT54_tree = (CommonTree)adaptor.create(INT54);
					adaptor.addChild(root_0, INT54_tree);
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:140:29: reserved_word_column_name
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_reserved_word_column_name_in_sort_spec907);
					reserved_word_column_name55=reserved_word_column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name55.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sort_spec"


	public static class ordered_sort_spec_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "ordered_sort_spec"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:142:1: ordered_sort_spec : ( sort_spec 'DESC' -> ^( DESC sort_spec ) | sort_spec ( 'ASC' )? -> ^( ASC sort_spec ) );
	public final SQL92QueryParser.ordered_sort_spec_return ordered_sort_spec() throws RecognitionException {
		SQL92QueryParser.ordered_sort_spec_return retval = new SQL92QueryParser.ordered_sort_spec_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal57=null;
		Token string_literal59=null;
		ParserRuleReturnScope sort_spec56 =null;
		ParserRuleReturnScope sort_spec58 =null;

		CommonTree string_literal57_tree=null;
		CommonTree string_literal59_tree=null;
		RewriteRuleTokenStream stream_69=new RewriteRuleTokenStream(adaptor,"token 69");
		RewriteRuleTokenStream stream_62=new RewriteRuleTokenStream(adaptor,"token 62");
		RewriteRuleSubtreeStream stream_sort_spec=new RewriteRuleSubtreeStream(adaptor,"rule sort_spec");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:5: ( sort_spec 'DESC' -> ^( DESC sort_spec ) | sort_spec ( 'ASC' )? -> ^( ASC sort_spec ) )
			int alt22=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				switch ( input.LA(2) ) {
				case 48:
					{
					switch ( input.LA(3) ) {
					case ID:
						{
						int LA22_16 = input.LA(4);
						if ( (LA22_16==69) ) {
							alt22=1;
						}
						else if ( (LA22_16==EOF||LA22_16==46||LA22_16==50||LA22_16==62||LA22_16==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 16, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 66:
						{
						int LA22_3 = input.LA(4);
						if ( (LA22_3==69) ) {
							alt22=1;
						}
						else if ( (LA22_3==EOF||LA22_3==46||LA22_3==50||LA22_3==62||LA22_3==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 101:
						{
						int LA22_4 = input.LA(4);
						if ( (LA22_4==69) ) {
							alt22=1;
						}
						else if ( (LA22_4==EOF||LA22_4==46||LA22_4==50||LA22_4==62||LA22_4==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 4, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 100:
						{
						int LA22_5 = input.LA(4);
						if ( (LA22_5==69) ) {
							alt22=1;
						}
						else if ( (LA22_5==EOF||LA22_5==46||LA22_5==50||LA22_5==62||LA22_5==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 5, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 82:
						{
						int LA22_6 = input.LA(4);
						if ( (LA22_6==69) ) {
							alt22=1;
						}
						else if ( (LA22_6==EOF||LA22_6==46||LA22_6==50||LA22_6==62||LA22_6==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 6, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 106:
						{
						int LA22_7 = input.LA(4);
						if ( (LA22_7==69) ) {
							alt22=1;
						}
						else if ( (LA22_7==EOF||LA22_7==46||LA22_7==50||LA22_7==62||LA22_7==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 7, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 89:
						{
						int LA22_8 = input.LA(4);
						if ( (LA22_8==69) ) {
							alt22=1;
						}
						else if ( (LA22_8==EOF||LA22_8==46||LA22_8==50||LA22_8==62||LA22_8==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 8, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 67:
						{
						int LA22_9 = input.LA(4);
						if ( (LA22_9==69) ) {
							alt22=1;
						}
						else if ( (LA22_9==EOF||LA22_9==46||LA22_9==50||LA22_9==62||LA22_9==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 9, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 78:
						{
						int LA22_10 = input.LA(4);
						if ( (LA22_10==69) ) {
							alt22=1;
						}
						else if ( (LA22_10==EOF||LA22_10==46||LA22_10==50||LA22_10==62||LA22_10==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 10, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 88:
						{
						int LA22_11 = input.LA(4);
						if ( (LA22_11==69) ) {
							alt22=1;
						}
						else if ( (LA22_11==EOF||LA22_11==46||LA22_11==50||LA22_11==62||LA22_11==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 11, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					case 97:
						{
						int LA22_12 = input.LA(4);
						if ( (LA22_12==69) ) {
							alt22=1;
						}
						else if ( (LA22_12==EOF||LA22_12==46||LA22_12==50||LA22_12==62||LA22_12==87) ) {
							alt22=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 22, 12, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 22, 13, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
					}
					break;
				case 69:
					{
					alt22=1;
					}
					break;
				case EOF:
				case 46:
				case 50:
				case 62:
				case 87:
					{
					alt22=2;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case INT:
				{
				int LA22_2 = input.LA(2);
				if ( (LA22_2==69) ) {
					alt22=1;
				}
				else if ( (LA22_2==EOF||LA22_2==46||LA22_2==50||LA22_2==62||LA22_2==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 66:
				{
				int LA22_3 = input.LA(2);
				if ( (LA22_3==69) ) {
					alt22=1;
				}
				else if ( (LA22_3==EOF||LA22_3==46||LA22_3==50||LA22_3==62||LA22_3==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 101:
				{
				int LA22_4 = input.LA(2);
				if ( (LA22_4==69) ) {
					alt22=1;
				}
				else if ( (LA22_4==EOF||LA22_4==46||LA22_4==50||LA22_4==62||LA22_4==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 100:
				{
				int LA22_5 = input.LA(2);
				if ( (LA22_5==69) ) {
					alt22=1;
				}
				else if ( (LA22_5==EOF||LA22_5==46||LA22_5==50||LA22_5==62||LA22_5==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 5, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 82:
				{
				int LA22_6 = input.LA(2);
				if ( (LA22_6==69) ) {
					alt22=1;
				}
				else if ( (LA22_6==EOF||LA22_6==46||LA22_6==50||LA22_6==62||LA22_6==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 106:
				{
				int LA22_7 = input.LA(2);
				if ( (LA22_7==69) ) {
					alt22=1;
				}
				else if ( (LA22_7==EOF||LA22_7==46||LA22_7==50||LA22_7==62||LA22_7==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 7, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 89:
				{
				int LA22_8 = input.LA(2);
				if ( (LA22_8==69) ) {
					alt22=1;
				}
				else if ( (LA22_8==EOF||LA22_8==46||LA22_8==50||LA22_8==62||LA22_8==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 67:
				{
				int LA22_9 = input.LA(2);
				if ( (LA22_9==69) ) {
					alt22=1;
				}
				else if ( (LA22_9==EOF||LA22_9==46||LA22_9==50||LA22_9==62||LA22_9==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 9, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 78:
				{
				int LA22_10 = input.LA(2);
				if ( (LA22_10==69) ) {
					alt22=1;
				}
				else if ( (LA22_10==EOF||LA22_10==46||LA22_10==50||LA22_10==62||LA22_10==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 10, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 88:
				{
				int LA22_11 = input.LA(2);
				if ( (LA22_11==69) ) {
					alt22=1;
				}
				else if ( (LA22_11==EOF||LA22_11==46||LA22_11==50||LA22_11==62||LA22_11==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 11, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 97:
				{
				int LA22_12 = input.LA(2);
				if ( (LA22_12==69) ) {
					alt22=1;
				}
				else if ( (LA22_12==EOF||LA22_12==46||LA22_12==50||LA22_12==62||LA22_12==87) ) {
					alt22=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 22, 12, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 22, 0, input);
				throw nvae;
			}
			switch (alt22) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:9: sort_spec 'DESC'
					{
					pushFollow(FOLLOW_sort_spec_in_ordered_sort_spec925);
					sort_spec56=sort_spec();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort_spec.add(sort_spec56.getTree());
					string_literal57=(Token)match(input,69,FOLLOW_69_in_ordered_sort_spec927); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_69.add(string_literal57);

					// AST REWRITE
					// elements: sort_spec
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 143:26: -> ^( DESC sort_spec )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:143:29: ^( DESC sort_spec )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(DESC, "DESC"), root_1);
						adaptor.addChild(root_1, stream_sort_spec.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:9: sort_spec ( 'ASC' )?
					{
					pushFollow(FOLLOW_sort_spec_in_ordered_sort_spec945);
					sort_spec58=sort_spec();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sort_spec.add(sort_spec58.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:19: ( 'ASC' )?
					int alt21=2;
					int LA21_0 = input.LA(1);
					if ( (LA21_0==62) ) {
						alt21=1;
					}
					switch (alt21) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:19: 'ASC'
							{
							string_literal59=(Token)match(input,62,FOLLOW_62_in_ordered_sort_spec947); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_62.add(string_literal59);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 144:26: -> ^( ASC sort_spec )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:144:29: ^( ASC sort_spec )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ASC, "ASC"), root_1);
						adaptor.addChild(root_1, stream_sort_spec.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "ordered_sort_spec"


	public static class reserved_word_column_name_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "reserved_word_column_name"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:146:1: reserved_word_column_name : (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' |s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) ;
	public final SQL92QueryParser.reserved_word_column_name_return reserved_word_column_name() throws RecognitionException {
		SQL92QueryParser.reserved_word_column_name_return retval = new SQL92QueryParser.reserved_word_column_name_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token tableid=null;
		Token s=null;
		Token char_literal60=null;

		CommonTree tableid_tree=null;
		CommonTree s_tree=null;
		CommonTree char_literal60_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
		RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
		RewriteRuleTokenStream stream_100=new RewriteRuleTokenStream(adaptor,"token 100");
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_106=new RewriteRuleTokenStream(adaptor,"token 106");
		RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_97=new RewriteRuleTokenStream(adaptor,"token 97");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:5: ( (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' |s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:9: (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' |s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' )
			{
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:9: (tableid= ID '.' )?
			int alt23=2;
			int LA23_0 = input.LA(1);
			if ( (LA23_0==ID) ) {
				alt23=1;
			}
			switch (alt23) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:10: tableid= ID '.'
					{
					tableid=(Token)match(input,ID,FOLLOW_ID_in_reserved_word_column_name977); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(tableid);

					char_literal60=(Token)match(input,48,FOLLOW_48_in_reserved_word_column_name978); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_48.add(char_literal60);

					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:25: (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' |s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' )
			int alt24=10;
			switch ( input.LA(1) ) {
			case 66:
				{
				alt24=1;
				}
				break;
			case 101:
				{
				alt24=2;
				}
				break;
			case 100:
				{
				alt24=3;
				}
				break;
			case 82:
				{
				alt24=4;
				}
				break;
			case 106:
				{
				alt24=5;
				}
				break;
			case 89:
				{
				alt24=6;
				}
				break;
			case 67:
				{
				alt24=7;
				}
				break;
			case 78:
				{
				alt24=8;
				}
				break;
			case 88:
				{
				alt24=9;
				}
				break;
			case 97:
				{
				alt24=10;
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:26: s= 'DATE'
					{
					s=(Token)match(input,66,FOLLOW_66_in_reserved_word_column_name984); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(s);

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:37: s= 'TIMESTAMP'
					{
					s=(Token)match(input,101,FOLLOW_101_in_reserved_word_column_name990); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_101.add(s);

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:53: s= 'TIME'
					{
					s=(Token)match(input,100,FOLLOW_100_in_reserved_word_column_name996); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_100.add(s);

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:64: s= 'INTERVAL'
					{
					s=(Token)match(input,82,FOLLOW_82_in_reserved_word_column_name1002); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_82.add(s);

					}
					break;
				case 5 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:79: s= 'YEAR'
					{
					s=(Token)match(input,106,FOLLOW_106_in_reserved_word_column_name1008); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_106.add(s);

					}
					break;
				case 6 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:90: s= 'MONTH'
					{
					s=(Token)match(input,89,FOLLOW_89_in_reserved_word_column_name1014); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_89.add(s);

					}
					break;
				case 7 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:102: s= 'DAY'
					{
					s=(Token)match(input,67,FOLLOW_67_in_reserved_word_column_name1020); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_67.add(s);

					}
					break;
				case 8 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:112: s= 'HOUR'
					{
					s=(Token)match(input,78,FOLLOW_78_in_reserved_word_column_name1026); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_78.add(s);

					}
					break;
				case 9 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:123: s= 'MINUTE'
					{
					s=(Token)match(input,88,FOLLOW_88_in_reserved_word_column_name1032); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_88.add(s);

					}
					break;
				case 10 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:147:136: s= 'SECOND'
					{
					s=(Token)match(input,97,FOLLOW_97_in_reserved_word_column_name1038); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_97.add(s);

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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 148:13: -> ^( TABLECOLUMN ( $tableid)? $s)
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:148:16: ^( TABLECOLUMN ( $tableid)? $s)
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:148:31: ( $tableid)?
				if ( stream_tableid.hasNext() ) {
					adaptor.addChild(root_1, stream_tableid.nextNode());
				}
				stream_tableid.reset();

				adaptor.addChild(root_1, stream_s.nextNode());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "reserved_word_column_name"


	public static class value_expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "value_expression"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:150:1: value_expression : ( string_value_expression | numeric_value_expression );
	public final SQL92QueryParser.value_expression_return value_expression() throws RecognitionException {
		SQL92QueryParser.value_expression_return retval = new SQL92QueryParser.value_expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope string_value_expression61 =null;
		ParserRuleReturnScope numeric_value_expression62 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:5: ( string_value_expression | numeric_value_expression )
			int alt25=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				switch ( input.LA(2) ) {
				case 48:
					{
					int LA25_4 = input.LA(3);
					if ( (LA25_4==ID) ) {
						int LA25_6 = input.LA(4);
						if ( (LA25_6==107) ) {
							alt25=1;
						}
						else if ( (LA25_6==EOF||LA25_6==FLOAT||(LA25_6 >= ID && LA25_6 <= INT)||LA25_6==NUMERIC||LA25_6==STRING||(LA25_6 >= 41 && LA25_6 <= 47)||(LA25_6 >= 49 && LA25_6 <= 57)||LA25_6==59||LA25_6==61||LA25_6==63||(LA25_6 >= 66 && LA25_6 <= 68)||(LA25_6 >= 71 && LA25_6 <= 91)||(LA25_6 >= 93 && LA25_6 <= 94)||(LA25_6 >= 96 && LA25_6 <= 97)||(LA25_6 >= 100 && LA25_6 <= 101)||(LA25_6 >= 103 && LA25_6 <= 106)) ) {
							alt25=2;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 25, 6, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( ((LA25_4 >= 66 && LA25_4 <= 67)||LA25_4==78||LA25_4==82||(LA25_4 >= 88 && LA25_4 <= 89)||LA25_4==97||(LA25_4 >= 100 && LA25_4 <= 101)||LA25_4==106) ) {
						alt25=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 25, 4, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

					}
					break;
				case EOF:
				case FLOAT:
				case ID:
				case INT:
				case NUMERIC:
				case STRING:
				case 41:
				case 42:
				case 43:
				case 44:
				case 45:
				case 46:
				case 47:
				case 49:
				case 50:
				case 51:
				case 52:
				case 53:
				case 54:
				case 55:
				case 56:
				case 57:
				case 59:
				case 61:
				case 63:
				case 66:
				case 67:
				case 68:
				case 71:
				case 72:
				case 73:
				case 74:
				case 75:
				case 76:
				case 77:
				case 78:
				case 79:
				case 80:
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
				case 93:
				case 94:
				case 96:
				case 97:
				case 100:
				case 101:
				case 103:
				case 104:
				case 105:
				case 106:
					{
					alt25=2;
					}
					break;
				case 107:
					{
					alt25=1;
					}
					break;
				default:
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 25, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}
				}
				break;
			case STRING:
				{
				int LA25_2 = input.LA(2);
				if ( (LA25_2==107) ) {
					alt25=1;
				}
				else if ( (LA25_2==EOF||LA25_2==FLOAT||(LA25_2 >= ID && LA25_2 <= INT)||LA25_2==NUMERIC||LA25_2==STRING||(LA25_2 >= 41 && LA25_2 <= 47)||(LA25_2 >= 49 && LA25_2 <= 57)||LA25_2==59||LA25_2==61||LA25_2==63||(LA25_2 >= 66 && LA25_2 <= 68)||(LA25_2 >= 71 && LA25_2 <= 91)||(LA25_2 >= 93 && LA25_2 <= 94)||(LA25_2 >= 96 && LA25_2 <= 97)||(LA25_2 >= 100 && LA25_2 <= 101)||(LA25_2 >= 103 && LA25_2 <= 106)) ) {
					alt25=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 25, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case FLOAT:
			case INT:
			case NUMERIC:
			case 42:
			case 45:
			case 47:
			case 66:
			case 67:
			case 73:
			case 78:
			case 82:
			case 88:
			case 89:
			case 91:
			case 97:
			case 100:
			case 101:
			case 103:
			case 106:
				{
				alt25=2;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 25, 0, input);
				throw nvae;
			}
			switch (alt25) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:151:9: string_value_expression
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_string_value_expression_in_value_expression1084);
					string_value_expression61=string_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_value_expression61.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:152:9: numeric_value_expression
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_numeric_value_expression_in_value_expression1094);
					numeric_value_expression62=numeric_value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_value_expression62.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "value_expression"


	public static class numeric_value_expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "numeric_value_expression"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:154:1: numeric_value_expression : factor ( ( '+' | '-' ) ^ factor )* ;
	public final SQL92QueryParser.numeric_value_expression_return numeric_value_expression() throws RecognitionException {
		SQL92QueryParser.numeric_value_expression_return retval = new SQL92QueryParser.numeric_value_expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token set64=null;
		ParserRuleReturnScope factor63 =null;
		ParserRuleReturnScope factor65 =null;

		CommonTree set64_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:5: ( factor ( ( '+' | '-' ) ^ factor )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:9: factor ( ( '+' | '-' ) ^ factor )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_factor_in_numeric_value_expression1113);
			factor63=factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, factor63.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:16: ( ( '+' | '-' ) ^ factor )*
			loop26:
			while (true) {
				int alt26=2;
				int LA26_0 = input.LA(1);
				if ( (LA26_0==45) ) {
					int LA26_9 = input.LA(2);
					if ( (synpred40_SQL92Query()) ) {
						alt26=1;
					}

				}
				else if ( (LA26_0==47) ) {
					int LA26_10 = input.LA(2);
					if ( (synpred40_SQL92Query()) ) {
						alt26=1;
					}

				}

				switch (alt26) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:17: ( '+' | '-' ) ^ factor
					{
					set64=input.LT(1);
					set64=input.LT(1);
					if ( input.LA(1)==45||input.LA(1)==47 ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set64), root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_factor_in_numeric_value_expression1123);
					factor65=factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, factor65.getTree());

					}
					break;

				default :
					break loop26;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "numeric_value_expression"


	public static class factor_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "factor"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:157:1: factor : numeric_primary ( ( '*' | '/' ) ^ numeric_primary )* ;
	public final SQL92QueryParser.factor_return factor() throws RecognitionException {
		SQL92QueryParser.factor_return retval = new SQL92QueryParser.factor_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token set67=null;
		ParserRuleReturnScope numeric_primary66 =null;
		ParserRuleReturnScope numeric_primary68 =null;

		CommonTree set67_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:158:5: ( numeric_primary ( ( '*' | '/' ) ^ numeric_primary )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:158:9: numeric_primary ( ( '*' | '/' ) ^ numeric_primary )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_numeric_primary_in_factor1145);
			numeric_primary66=numeric_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary66.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:158:25: ( ( '*' | '/' ) ^ numeric_primary )*
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==44||LA27_0==49) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:158:26: ( '*' | '/' ) ^ numeric_primary
					{
					set67=input.LT(1);
					set67=input.LT(1);
					if ( input.LA(1)==44||input.LA(1)==49 ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set67), root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_numeric_primary_in_factor1155);
					numeric_primary68=numeric_primary();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_primary68.getTree());

					}
					break;

				default :
					break loop27;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "factor"


	public static class numeric_primary_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "numeric_primary"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:160:1: numeric_primary : ( '+' ^| '-' ^)? value_expression_primary ;
	public final SQL92QueryParser.numeric_primary_return numeric_primary() throws RecognitionException {
		SQL92QueryParser.numeric_primary_return retval = new SQL92QueryParser.numeric_primary_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal69=null;
		Token char_literal70=null;
		ParserRuleReturnScope value_expression_primary71 =null;

		CommonTree char_literal69_tree=null;
		CommonTree char_literal70_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:5: ( ( '+' ^| '-' ^)? value_expression_primary )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:9: ( '+' ^| '-' ^)? value_expression_primary
			{
			root_0 = (CommonTree)adaptor.nil();


			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:9: ( '+' ^| '-' ^)?
			int alt28=3;
			int LA28_0 = input.LA(1);
			if ( (LA28_0==45) ) {
				alt28=1;
			}
			else if ( (LA28_0==47) ) {
				alt28=2;
			}
			switch (alt28) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:10: '+' ^
					{
					char_literal69=(Token)match(input,45,FOLLOW_45_in_numeric_primary1176); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal69_tree = (CommonTree)adaptor.create(char_literal69);
					root_0 = (CommonTree)adaptor.becomeRoot(char_literal69_tree, root_0);
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:161:15: '-' ^
					{
					char_literal70=(Token)match(input,47,FOLLOW_47_in_numeric_primary1179); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal70_tree = (CommonTree)adaptor.create(char_literal70);
					root_0 = (CommonTree)adaptor.becomeRoot(char_literal70_tree, root_0);
					}

					}
					break;

			}

			pushFollow(FOLLOW_value_expression_primary_in_numeric_primary1184);
			value_expression_primary71=value_expression_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression_primary71.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "numeric_primary"


	public static class value_expression_primary_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "value_expression_primary"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:163:1: value_expression_primary : ( '(' ! value_expression ')' !| function | column_name | literal | sub_query );
	public final SQL92QueryParser.value_expression_primary_return value_expression_primary() throws RecognitionException {
		SQL92QueryParser.value_expression_primary_return retval = new SQL92QueryParser.value_expression_primary_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal72=null;
		Token char_literal74=null;
		ParserRuleReturnScope value_expression73 =null;
		ParserRuleReturnScope function75 =null;
		ParserRuleReturnScope column_name76 =null;
		ParserRuleReturnScope literal77 =null;
		ParserRuleReturnScope sub_query78 =null;

		CommonTree char_literal72_tree=null;
		CommonTree char_literal74_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:164:5: ( '(' ! value_expression ')' !| function | column_name | literal | sub_query )
			int alt29=5;
			switch ( input.LA(1) ) {
			case 42:
				{
				int LA29_1 = input.LA(2);
				if ( (synpred45_SQL92Query()) ) {
					alt29=1;
				}
				else if ( (true) ) {
					alt29=5;
				}

				}
				break;
			case ID:
				{
				int LA29_2 = input.LA(2);
				if ( (synpred46_SQL92Query()) ) {
					alt29=2;
				}
				else if ( (synpred47_SQL92Query()) ) {
					alt29=3;
				}
				else if ( (synpred48_SQL92Query()) ) {
					alt29=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 29, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case FLOAT:
			case INT:
			case NUMERIC:
			case STRING:
			case 66:
			case 67:
			case 73:
			case 78:
			case 82:
			case 88:
			case 89:
			case 91:
			case 97:
			case 100:
			case 101:
			case 103:
			case 106:
				{
				alt29=4;
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:164:9: '(' ! value_expression ')' !
					{
					root_0 = (CommonTree)adaptor.nil();


					char_literal72=(Token)match(input,42,FOLLOW_42_in_value_expression_primary1204); if (state.failed) return retval;
					pushFollow(FOLLOW_value_expression_in_value_expression_primary1207);
					value_expression73=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression73.getTree());

					char_literal74=(Token)match(input,43,FOLLOW_43_in_value_expression_primary1209); if (state.failed) return retval;
					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:165:9: function
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_function_in_value_expression_primary1220);
					function75=function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function75.getTree());

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:166:9: column_name
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_column_name_in_value_expression_primary1230);
					column_name76=column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name76.getTree());

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:9: literal
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_literal_in_value_expression_primary1240);
					literal77=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal77.getTree());

					}
					break;
				case 5 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:168:9: sub_query
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_sub_query_in_value_expression_primary1250);
					sub_query78=sub_query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query78.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "value_expression_primary"


	public static class literal_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "literal"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:170:1: literal : ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' );
	public final SQL92QueryParser.literal_return literal() throws RecognitionException {
		SQL92QueryParser.literal_return retval = new SQL92QueryParser.literal_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token INT79=null;
		Token FLOAT80=null;
		Token NUMERIC81=null;
		Token STRING82=null;
		Token string_literal85=null;
		Token string_literal86=null;
		Token string_literal87=null;
		ParserRuleReturnScope datetime83 =null;
		ParserRuleReturnScope interval84 =null;

		CommonTree INT79_tree=null;
		CommonTree FLOAT80_tree=null;
		CommonTree NUMERIC81_tree=null;
		CommonTree STRING82_tree=null;
		CommonTree string_literal85_tree=null;
		CommonTree string_literal86_tree=null;
		CommonTree string_literal87_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:5: ( INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE' )
			int alt30=9;
			switch ( input.LA(1) ) {
			case INT:
				{
				alt30=1;
				}
				break;
			case FLOAT:
				{
				alt30=2;
				}
				break;
			case NUMERIC:
				{
				alt30=3;
				}
				break;
			case STRING:
				{
				alt30=4;
				}
				break;
			case 66:
			case 100:
			case 101:
				{
				alt30=5;
				}
				break;
			case ID:
				{
				int LA30_6 = input.LA(2);
				if ( (LA30_6==48) ) {
					int LA30_11 = input.LA(3);
					if ( (LA30_11==66||(LA30_11 >= 100 && LA30_11 <= 101)) ) {
						alt30=5;
					}
					else if ( (LA30_11==67||LA30_11==78||LA30_11==82||(LA30_11 >= 88 && LA30_11 <= 89)||LA30_11==97||LA30_11==106) ) {
						alt30=6;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 30, 11, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 30, 6, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 67:
			case 78:
			case 82:
			case 88:
			case 89:
			case 97:
			case 106:
				{
				alt30=6;
				}
				break;
			case 91:
				{
				alt30=7;
				}
				break;
			case 103:
				{
				alt30=8;
				}
				break;
			case 73:
				{
				alt30=9;
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:9: INT
					{
					root_0 = (CommonTree)adaptor.nil();


					INT79=(Token)match(input,INT,FOLLOW_INT_in_literal1268); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					INT79_tree = (CommonTree)adaptor.create(INT79);
					adaptor.addChild(root_0, INT79_tree);
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:15: FLOAT
					{
					root_0 = (CommonTree)adaptor.nil();


					FLOAT80=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_literal1272); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					FLOAT80_tree = (CommonTree)adaptor.create(FLOAT80);
					adaptor.addChild(root_0, FLOAT80_tree);
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:23: NUMERIC
					{
					root_0 = (CommonTree)adaptor.nil();


					NUMERIC81=(Token)match(input,NUMERIC,FOLLOW_NUMERIC_in_literal1276); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					NUMERIC81_tree = (CommonTree)adaptor.create(NUMERIC81);
					adaptor.addChild(root_0, NUMERIC81_tree);
					}

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:33: STRING
					{
					root_0 = (CommonTree)adaptor.nil();


					STRING82=(Token)match(input,STRING,FOLLOW_STRING_in_literal1280); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STRING82_tree = (CommonTree)adaptor.create(STRING82);
					adaptor.addChild(root_0, STRING82_tree);
					}

					}
					break;
				case 5 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:42: datetime
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_datetime_in_literal1284);
					datetime83=datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime83.getTree());

					}
					break;
				case 6 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:53: interval
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_interval_in_literal1288);
					interval84=interval();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, interval84.getTree());

					}
					break;
				case 7 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:64: 'NULL'
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal85=(Token)match(input,91,FOLLOW_91_in_literal1292); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal85_tree = (CommonTree)adaptor.create(string_literal85);
					adaptor.addChild(root_0, string_literal85_tree);
					}

					}
					break;
				case 8 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:73: 'TRUE'
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal86=(Token)match(input,103,FOLLOW_103_in_literal1296); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal86_tree = (CommonTree)adaptor.create(string_literal86);
					adaptor.addChild(root_0, string_literal86_tree);
					}

					}
					break;
				case 9 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:171:82: 'FALSE'
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal87=(Token)match(input,73,FOLLOW_73_in_literal1300); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal87_tree = (CommonTree)adaptor.create(string_literal87);
					adaptor.addChild(root_0, string_literal87_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "literal"


	public static class datetime_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "datetime"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:173:1: datetime : ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) ^ STRING | (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
	public final SQL92QueryParser.datetime_return datetime() throws RecognitionException {
		SQL92QueryParser.datetime_return retval = new SQL92QueryParser.datetime_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token tableid=null;
		Token s=null;
		Token set88=null;
		Token STRING89=null;
		Token char_literal90=null;

		CommonTree tableid_tree=null;
		CommonTree s_tree=null;
		CommonTree set88_tree=null;
		CommonTree STRING89_tree=null;
		CommonTree char_literal90_tree=null;
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_100=new RewriteRuleTokenStream(adaptor,"token 100");
		RewriteRuleTokenStream stream_101=new RewriteRuleTokenStream(adaptor,"token 101");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:5: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) ^ STRING | (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
			int alt33=2;
			switch ( input.LA(1) ) {
			case 66:
				{
				int LA33_1 = input.LA(2);
				if ( (LA33_1==STRING) ) {
					int LA33_5 = input.LA(3);
					if ( (synpred59_SQL92Query()) ) {
						alt33=1;
					}
					else if ( (true) ) {
						alt33=2;
					}

				}
				else if ( (LA33_1==EOF||LA33_1==FLOAT||(LA33_1 >= ID && LA33_1 <= INT)||LA33_1==NUMERIC||(LA33_1 >= 41 && LA33_1 <= 47)||(LA33_1 >= 49 && LA33_1 <= 57)||LA33_1==59||LA33_1==61||LA33_1==63||(LA33_1 >= 66 && LA33_1 <= 68)||(LA33_1 >= 71 && LA33_1 <= 91)||(LA33_1 >= 93 && LA33_1 <= 94)||(LA33_1 >= 96 && LA33_1 <= 97)||(LA33_1 >= 100 && LA33_1 <= 101)||(LA33_1 >= 103 && LA33_1 <= 106)) ) {
					alt33=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 33, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case ID:
				{
				alt33=2;
				}
				break;
			case 101:
				{
				int LA33_3 = input.LA(2);
				if ( (LA33_3==STRING) ) {
					int LA33_6 = input.LA(3);
					if ( (synpred59_SQL92Query()) ) {
						alt33=1;
					}
					else if ( (true) ) {
						alt33=2;
					}

				}
				else if ( (LA33_3==EOF||LA33_3==FLOAT||(LA33_3 >= ID && LA33_3 <= INT)||LA33_3==NUMERIC||(LA33_3 >= 41 && LA33_3 <= 47)||(LA33_3 >= 49 && LA33_3 <= 57)||LA33_3==59||LA33_3==61||LA33_3==63||(LA33_3 >= 66 && LA33_3 <= 68)||(LA33_3 >= 71 && LA33_3 <= 91)||(LA33_3 >= 93 && LA33_3 <= 94)||(LA33_3 >= 96 && LA33_3 <= 97)||(LA33_3 >= 100 && LA33_3 <= 101)||(LA33_3 >= 103 && LA33_3 <= 106)) ) {
					alt33=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 33, 3, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 100:
				{
				int LA33_4 = input.LA(2);
				if ( (LA33_4==STRING) ) {
					int LA33_7 = input.LA(3);
					if ( (synpred59_SQL92Query()) ) {
						alt33=1;
					}
					else if ( (true) ) {
						alt33=2;
					}

				}
				else if ( (LA33_4==EOF||LA33_4==FLOAT||(LA33_4 >= ID && LA33_4 <= INT)||LA33_4==NUMERIC||(LA33_4 >= 41 && LA33_4 <= 47)||(LA33_4 >= 49 && LA33_4 <= 57)||LA33_4==59||LA33_4==61||LA33_4==63||(LA33_4 >= 66 && LA33_4 <= 68)||(LA33_4 >= 71 && LA33_4 <= 91)||(LA33_4 >= 93 && LA33_4 <= 94)||(LA33_4 >= 96 && LA33_4 <= 97)||(LA33_4 >= 100 && LA33_4 <= 101)||(LA33_4 >= 103 && LA33_4 <= 106)) ) {
					alt33=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 33, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 33, 0, input);
				throw nvae;
			}
			switch (alt33) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) ^ STRING
					{
					root_0 = (CommonTree)adaptor.nil();


					set88=input.LT(1);
					set88=input.LT(1);
					if ( input.LA(1)==66||(input.LA(1) >= 100 && input.LA(1) <= 101) ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set88), root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					STRING89=(Token)match(input,STRING,FOLLOW_STRING_in_datetime1331); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STRING89_tree = (CommonTree)adaptor.create(STRING89);
					adaptor.addChild(root_0, STRING89_tree);
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:9: (tableid= ID '.' )? (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' )
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:9: (tableid= ID '.' )?
					int alt31=2;
					int LA31_0 = input.LA(1);
					if ( (LA31_0==ID) ) {
						alt31=1;
					}
					switch (alt31) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:10: tableid= ID '.'
							{
							tableid=(Token)match(input,ID,FOLLOW_ID_in_datetime1344); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(tableid);

							char_literal90=(Token)match(input,48,FOLLOW_48_in_datetime1345); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_48.add(char_literal90);

							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:25: (s= 'DATE' |s= 'TIMESTAMP' |s= 'TIME' )
					int alt32=3;
					switch ( input.LA(1) ) {
					case 66:
						{
						alt32=1;
						}
						break;
					case 101:
						{
						alt32=2;
						}
						break;
					case 100:
						{
						alt32=3;
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
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:26: s= 'DATE'
							{
							s=(Token)match(input,66,FOLLOW_66_in_datetime1351); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_66.add(s);

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:37: s= 'TIMESTAMP'
							{
							s=(Token)match(input,101,FOLLOW_101_in_datetime1357); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_101.add(s);

							}
							break;
						case 3 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:53: s= 'TIME'
							{
							s=(Token)match(input,100,FOLLOW_100_in_datetime1363); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_100.add(s);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 175:63: -> ^( TABLECOLUMN ( $tableid)? $s)
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:66: ^( TABLECOLUMN ( $tableid)? $s)
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:175:81: ( $tableid)?
						if ( stream_tableid.hasNext() ) {
							adaptor.addChild(root_1, stream_tableid.nextNode());
						}
						stream_tableid.reset();

						adaptor.addChild(root_1, stream_s.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "datetime"


	public static class interval_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "interval"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:177:1: interval : ( 'INTERVAL' ^ STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) );
	public final SQL92QueryParser.interval_return interval() throws RecognitionException {
		SQL92QueryParser.interval_return retval = new SQL92QueryParser.interval_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token tableid=null;
		Token s=null;
		Token string_literal91=null;
		Token STRING92=null;
		Token set93=null;
		Token char_literal94=null;

		CommonTree tableid_tree=null;
		CommonTree s_tree=null;
		CommonTree string_literal91_tree=null;
		CommonTree STRING92_tree=null;
		CommonTree set93_tree=null;
		CommonTree char_literal94_tree=null;
		RewriteRuleTokenStream stream_88=new RewriteRuleTokenStream(adaptor,"token 88");
		RewriteRuleTokenStream stream_78=new RewriteRuleTokenStream(adaptor,"token 78");
		RewriteRuleTokenStream stream_67=new RewriteRuleTokenStream(adaptor,"token 67");
		RewriteRuleTokenStream stream_89=new RewriteRuleTokenStream(adaptor,"token 89");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_106=new RewriteRuleTokenStream(adaptor,"token 106");
		RewriteRuleTokenStream stream_82=new RewriteRuleTokenStream(adaptor,"token 82");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_97=new RewriteRuleTokenStream(adaptor,"token 97");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:5: ( 'INTERVAL' ^ STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) | (tableid= ID '.' )? (s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' ) -> ^( TABLECOLUMN ( $tableid)? $s) )
			int alt36=2;
			int LA36_0 = input.LA(1);
			if ( (LA36_0==82) ) {
				int LA36_1 = input.LA(2);
				if ( (LA36_1==STRING) ) {
					switch ( input.LA(3) ) {
					case 106:
						{
						int LA36_4 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					case FLOAT:
					case ID:
					case INT:
					case NUMERIC:
					case STRING:
					case 41:
					case 42:
					case 43:
					case 44:
					case 45:
					case 46:
					case 47:
					case 49:
					case 51:
					case 52:
					case 53:
					case 54:
					case 55:
					case 56:
					case 57:
					case 63:
					case 66:
					case 68:
					case 72:
					case 73:
					case 79:
					case 82:
					case 83:
					case 86:
					case 90:
					case 91:
					case 100:
					case 101:
					case 103:
					case 107:
						{
						alt36=2;
						}
						break;
					case 89:
						{
						int LA36_5 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					case 67:
						{
						int LA36_6 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					case 78:
						{
						int LA36_7 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					case 88:
						{
						int LA36_8 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					case 97:
						{
						int LA36_9 = input.LA(4);
						if ( (synpred68_SQL92Query()) ) {
							alt36=1;
						}
						else if ( (true) ) {
							alt36=2;
						}

						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 36, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}
				}
				else if ( (LA36_1==EOF||LA36_1==FLOAT||(LA36_1 >= ID && LA36_1 <= INT)||LA36_1==NUMERIC||(LA36_1 >= 41 && LA36_1 <= 47)||(LA36_1 >= 49 && LA36_1 <= 57)||LA36_1==59||LA36_1==61||LA36_1==63||(LA36_1 >= 66 && LA36_1 <= 68)||(LA36_1 >= 71 && LA36_1 <= 91)||(LA36_1 >= 93 && LA36_1 <= 94)||(LA36_1 >= 96 && LA36_1 <= 97)||(LA36_1 >= 100 && LA36_1 <= 101)||(LA36_1 >= 103 && LA36_1 <= 106)) ) {
					alt36=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 36, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA36_0==ID||LA36_0==67||LA36_0==78||(LA36_0 >= 88 && LA36_0 <= 89)||LA36_0==97||LA36_0==106) ) {
				alt36=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 36, 0, input);
				throw nvae;
			}

			switch (alt36) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:9: 'INTERVAL' ^ STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal91=(Token)match(input,82,FOLLOW_82_in_interval1395); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal91_tree = (CommonTree)adaptor.create(string_literal91);
					root_0 = (CommonTree)adaptor.becomeRoot(string_literal91_tree, root_0);
					}

					STRING92=(Token)match(input,STRING,FOLLOW_STRING_in_interval1398); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STRING92_tree = (CommonTree)adaptor.create(STRING92);
					adaptor.addChild(root_0, STRING92_tree);
					}

					set93=input.LT(1);
					if ( input.LA(1)==67||input.LA(1)==78||(input.LA(1) >= 88 && input.LA(1) <= 89)||input.LA(1)==97||input.LA(1)==106 ) {
						input.consume();
						if ( state.backtracking==0 ) adaptor.addChild(root_0, (CommonTree)adaptor.create(set93));
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:9: (tableid= ID '.' )? (s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' )
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:9: (tableid= ID '.' )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==ID) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:10: tableid= ID '.'
							{
							tableid=(Token)match(input,ID,FOLLOW_ID_in_interval1435); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ID.add(tableid);

							char_literal94=(Token)match(input,48,FOLLOW_48_in_interval1436); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_48.add(char_literal94);

							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:25: (s= 'INTERVAL' |s= 'YEAR' |s= 'MONTH' |s= 'DAY' |s= 'HOUR' |s= 'MINUTE' |s= 'SECOND' )
					int alt35=7;
					switch ( input.LA(1) ) {
					case 82:
						{
						alt35=1;
						}
						break;
					case 106:
						{
						alt35=2;
						}
						break;
					case 89:
						{
						alt35=3;
						}
						break;
					case 67:
						{
						alt35=4;
						}
						break;
					case 78:
						{
						alt35=5;
						}
						break;
					case 88:
						{
						alt35=6;
						}
						break;
					case 97:
						{
						alt35=7;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 35, 0, input);
						throw nvae;
					}
					switch (alt35) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:26: s= 'INTERVAL'
							{
							s=(Token)match(input,82,FOLLOW_82_in_interval1442); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_82.add(s);

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:41: s= 'YEAR'
							{
							s=(Token)match(input,106,FOLLOW_106_in_interval1448); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_106.add(s);

							}
							break;
						case 3 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:52: s= 'MONTH'
							{
							s=(Token)match(input,89,FOLLOW_89_in_interval1454); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_89.add(s);

							}
							break;
						case 4 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:64: s= 'DAY'
							{
							s=(Token)match(input,67,FOLLOW_67_in_interval1460); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_67.add(s);

							}
							break;
						case 5 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:74: s= 'HOUR'
							{
							s=(Token)match(input,78,FOLLOW_78_in_interval1466); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_78.add(s);

							}
							break;
						case 6 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:85: s= 'MINUTE'
							{
							s=(Token)match(input,88,FOLLOW_88_in_interval1472); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_88.add(s);

							}
							break;
						case 7 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:98: s= 'SECOND'
							{
							s=(Token)match(input,97,FOLLOW_97_in_interval1478); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_97.add(s);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 179:110: -> ^( TABLECOLUMN ( $tableid)? $s)
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:113: ^( TABLECOLUMN ( $tableid)? $s)
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:179:128: ( $tableid)?
						if ( stream_tableid.hasNext() ) {
							adaptor.addChild(root_1, stream_tableid.nextNode());
						}
						stream_tableid.reset();

						adaptor.addChild(root_1, stream_s.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "interval"


	public static class function_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "function"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:181:1: function : ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) );
	public final SQL92QueryParser.function_return function() throws RecognitionException {
		SQL92QueryParser.function_return retval = new SQL92QueryParser.function_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token name=null;
		Token char_literal95=null;
		Token char_literal97=null;
		Token char_literal99=null;
		Token char_literal100=null;
		Token char_literal101=null;
		Token char_literal102=null;
		ParserRuleReturnScope value_expression96 =null;
		ParserRuleReturnScope value_expression98 =null;

		CommonTree name_tree=null;
		CommonTree char_literal95_tree=null;
		CommonTree char_literal97_tree=null;
		CommonTree char_literal99_tree=null;
		CommonTree char_literal100_tree=null;
		CommonTree char_literal101_tree=null;
		CommonTree char_literal102_tree=null;
		RewriteRuleTokenStream stream_44=new RewriteRuleTokenStream(adaptor,"token 44");
		RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:5: ( (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')' -> ^( FUNCTION $name ( value_expression )* ) | (name= ID ) '(' '*' ')' -> ^( FUNCTION $name '*' ) )
			int alt39=2;
			int LA39_0 = input.LA(1);
			if ( (LA39_0==ID) ) {
				int LA39_1 = input.LA(2);
				if ( (LA39_1==42) ) {
					int LA39_2 = input.LA(3);
					if ( (LA39_2==44) ) {
						alt39=2;
					}
					else if ( (LA39_2==FLOAT||(LA39_2 >= ID && LA39_2 <= INT)||LA39_2==NUMERIC||LA39_2==STRING||(LA39_2 >= 42 && LA39_2 <= 43)||(LA39_2 >= 45 && LA39_2 <= 47)||(LA39_2 >= 66 && LA39_2 <= 67)||LA39_2==73||LA39_2==78||LA39_2==82||(LA39_2 >= 88 && LA39_2 <= 89)||LA39_2==91||LA39_2==97||(LA39_2 >= 100 && LA39_2 <= 101)||LA39_2==103||LA39_2==106) ) {
						alt39=1;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 39, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 39, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 39, 0, input);
				throw nvae;
			}

			switch (alt39) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:9: (name= ID ) '(' ( value_expression )? ( ',' value_expression )* ')'
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:9: (name= ID )
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:10: name= ID
					{
					name=(Token)match(input,ID,FOLLOW_ID_in_function1516); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(name);

					}

					char_literal95=(Token)match(input,42,FOLLOW_42_in_function1519); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_42.add(char_literal95);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:23: ( value_expression )?
					int alt37=2;
					int LA37_0 = input.LA(1);
					if ( (LA37_0==FLOAT||(LA37_0 >= ID && LA37_0 <= INT)||LA37_0==NUMERIC||LA37_0==STRING||LA37_0==42||LA37_0==45||LA37_0==47||(LA37_0 >= 66 && LA37_0 <= 67)||LA37_0==73||LA37_0==78||LA37_0==82||(LA37_0 >= 88 && LA37_0 <= 89)||LA37_0==91||LA37_0==97||(LA37_0 >= 100 && LA37_0 <= 101)||LA37_0==103||LA37_0==106) ) {
						alt37=1;
					}
					switch (alt37) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:23: value_expression
							{
							pushFollow(FOLLOW_value_expression_in_function1521);
							value_expression96=value_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_value_expression.add(value_expression96.getTree());
							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:41: ( ',' value_expression )*
					loop38:
					while (true) {
						int alt38=2;
						int LA38_0 = input.LA(1);
						if ( (LA38_0==46) ) {
							alt38=1;
						}

						switch (alt38) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:182:42: ',' value_expression
							{
							char_literal97=(Token)match(input,46,FOLLOW_46_in_function1525); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_46.add(char_literal97);

							pushFollow(FOLLOW_value_expression_in_function1527);
							value_expression98=value_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_value_expression.add(value_expression98.getTree());
							}
							break;

						default :
							break loop38;
						}
					}

					char_literal99=(Token)match(input,43,FOLLOW_43_in_function1531); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_43.add(char_literal99);

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
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 183:13: -> ^( FUNCTION $name ( value_expression )* )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:16: ^( FUNCTION $name ( value_expression )* )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);
						adaptor.addChild(root_1, stream_name.nextNode());
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:183:33: ( value_expression )*
						while ( stream_value_expression.hasNext() ) {
							adaptor.addChild(root_1, stream_value_expression.nextTree());
						}
						stream_value_expression.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:184:9: (name= ID ) '(' '*' ')'
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:184:9: (name= ID )
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:184:10: name= ID
					{
					name=(Token)match(input,ID,FOLLOW_ID_in_function1569); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(name);

					}

					char_literal100=(Token)match(input,42,FOLLOW_42_in_function1572); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_42.add(char_literal100);

					char_literal101=(Token)match(input,44,FOLLOW_44_in_function1574); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_44.add(char_literal101);

					char_literal102=(Token)match(input,43,FOLLOW_43_in_function1576); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_43.add(char_literal102);

					// AST REWRITE
					// elements: name, 44
					// token labels: name
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleTokenStream stream_name=new RewriteRuleTokenStream(adaptor,"token name",name);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 184:31: -> ^( FUNCTION $name '*' )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:184:34: ^( FUNCTION $name '*' )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);
						adaptor.addChild(root_1, stream_name.nextNode());
						adaptor.addChild(root_1, stream_44.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "function"


	public static class string_value_expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "string_value_expression"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:186:1: string_value_expression : ( column_name | STRING ) ( '||' ^ ( column_name | STRING ) )+ ;
	public final SQL92QueryParser.string_value_expression_return string_value_expression() throws RecognitionException {
		SQL92QueryParser.string_value_expression_return retval = new SQL92QueryParser.string_value_expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token STRING104=null;
		Token string_literal105=null;
		Token STRING107=null;
		ParserRuleReturnScope column_name103 =null;
		ParserRuleReturnScope column_name106 =null;

		CommonTree STRING104_tree=null;
		CommonTree string_literal105_tree=null;
		CommonTree STRING107_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:5: ( ( column_name | STRING ) ( '||' ^ ( column_name | STRING ) )+ )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:9: ( column_name | STRING ) ( '||' ^ ( column_name | STRING ) )+
			{
			root_0 = (CommonTree)adaptor.nil();


			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:9: ( column_name | STRING )
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:10: column_name
					{
					pushFollow(FOLLOW_column_name_in_string_value_expression1606);
					column_name103=column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name103.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:24: STRING
					{
					STRING104=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1610); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					STRING104_tree = (CommonTree)adaptor.create(STRING104);
					adaptor.addChild(root_0, STRING104_tree);
					}

					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:32: ( '||' ^ ( column_name | STRING ) )+
			int cnt42=0;
			loop42:
			while (true) {
				int alt42=2;
				int LA42_0 = input.LA(1);
				if ( (LA42_0==107) ) {
					alt42=1;
				}

				switch (alt42) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:33: '||' ^ ( column_name | STRING )
					{
					string_literal105=(Token)match(input,107,FOLLOW_107_in_string_value_expression1614); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal105_tree = (CommonTree)adaptor.create(string_literal105);
					root_0 = (CommonTree)adaptor.becomeRoot(string_literal105_tree, root_0);
					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:39: ( column_name | STRING )
					int alt41=2;
					int LA41_0 = input.LA(1);
					if ( (LA41_0==ID) ) {
						alt41=1;
					}
					else if ( (LA41_0==STRING) ) {
						alt41=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 41, 0, input);
						throw nvae;
					}

					switch (alt41) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:40: column_name
							{
							pushFollow(FOLLOW_column_name_in_string_value_expression1618);
							column_name106=column_name();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name106.getTree());

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:187:54: STRING
							{
							STRING107=(Token)match(input,STRING,FOLLOW_STRING_in_string_value_expression1622); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							STRING107_tree = (CommonTree)adaptor.create(STRING107);
							adaptor.addChild(root_0, STRING107_tree);
							}

							}
							break;

					}

					}
					break;

				default :
					if ( cnt42 >= 1 ) break loop42;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(42, input);
					throw eee;
				}
				cnt42++;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "string_value_expression"


	public static class table_expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_expression"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:189:1: table_expression : table_reference ;
	public final SQL92QueryParser.table_expression_return table_expression() throws RecognitionException {
		SQL92QueryParser.table_expression_return retval = new SQL92QueryParser.table_expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope table_reference108 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:190:5: ( table_reference )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:190:9: table_reference
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_table_reference_in_table_expression1643);
			table_reference108=table_reference();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference108.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_expression"


	public static class table_reference_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_reference"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:192:1: table_reference : table ( ',' ! table_reference )* ;
	public final SQL92QueryParser.table_reference_return table_reference() throws RecognitionException {
		SQL92QueryParser.table_reference_return retval = new SQL92QueryParser.table_reference_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal110=null;
		ParserRuleReturnScope table109 =null;
		ParserRuleReturnScope table_reference111 =null;

		CommonTree char_literal110_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:5: ( table ( ',' ! table_reference )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:9: table ( ',' ! table_reference )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_table_in_table_reference1661);
			table109=table();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, table109.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:15: ( ',' ! table_reference )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==46) ) {
					int LA43_2 = input.LA(2);
					if ( (synpred82_SQL92Query()) ) {
						alt43=1;
					}

				}

				switch (alt43) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:16: ',' ! table_reference
					{
					char_literal110=(Token)match(input,46,FOLLOW_46_in_table_reference1664); if (state.failed) return retval;
					pushFollow(FOLLOW_table_reference_in_table_reference1667);
					table_reference111=table_reference();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, table_reference111.getTree());

					}
					break;

				default :
					break loop43;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_reference"


	public static class join_type_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "join_type"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:195:1: join_type : ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN );
	public final SQL92QueryParser.join_type_return join_type() throws RecognitionException {
		SQL92QueryParser.join_type_return retval = new SQL92QueryParser.join_type_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal112=null;
		Token string_literal113=null;
		Token string_literal114=null;
		Token string_literal115=null;
		Token string_literal116=null;
		Token string_literal117=null;
		Token string_literal118=null;
		Token string_literal119=null;
		Token string_literal120=null;
		Token string_literal121=null;
		Token string_literal122=null;

		CommonTree string_literal112_tree=null;
		CommonTree string_literal113_tree=null;
		CommonTree string_literal114_tree=null;
		CommonTree string_literal115_tree=null;
		CommonTree string_literal116_tree=null;
		CommonTree string_literal117_tree=null;
		CommonTree string_literal118_tree=null;
		CommonTree string_literal119_tree=null;
		CommonTree string_literal120_tree=null;
		CommonTree string_literal121_tree=null;
		CommonTree string_literal122_tree=null;
		RewriteRuleTokenStream stream_80=new RewriteRuleTokenStream(adaptor,"token 80");
		RewriteRuleTokenStream stream_84=new RewriteRuleTokenStream(adaptor,"token 84");
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleTokenStream stream_96=new RewriteRuleTokenStream(adaptor,"token 96");
		RewriteRuleTokenStream stream_85=new RewriteRuleTokenStream(adaptor,"token 85");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:196:5: ( 'RIGHT' ( 'OUTER' )? 'JOIN' -> RIGHT_OUTER_JOIN | 'LEFT' ( 'OUTER' )? 'JOIN' -> LEFT_OUTER_JOIN | 'FULL' ( 'OUTER' )? 'JOIN' -> FULL_OUTER_JOIN | ( 'INNER' )? 'JOIN' -> JOIN )
			int alt48=4;
			switch ( input.LA(1) ) {
			case 96:
				{
				alt48=1;
				}
				break;
			case 85:
				{
				alt48=2;
				}
				break;
			case 75:
				{
				alt48=3;
				}
				break;
			case 80:
			case 84:
				{
				alt48=4;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 48, 0, input);
				throw nvae;
			}
			switch (alt48) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:196:9: 'RIGHT' ( 'OUTER' )? 'JOIN'
					{
					string_literal112=(Token)match(input,96,FOLLOW_96_in_join_type1687); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_96.add(string_literal112);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:196:17: ( 'OUTER' )?
					int alt44=2;
					int LA44_0 = input.LA(1);
					if ( (LA44_0==95) ) {
						alt44=1;
					}
					switch (alt44) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:196:17: 'OUTER'
							{
							string_literal113=(Token)match(input,95,FOLLOW_95_in_join_type1689); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_95.add(string_literal113);

							}
							break;

					}

					string_literal114=(Token)match(input,84,FOLLOW_84_in_join_type1692); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_84.add(string_literal114);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 196:33: -> RIGHT_OUTER_JOIN
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(RIGHT_OUTER_JOIN, "RIGHT_OUTER_JOIN"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:197:9: 'LEFT' ( 'OUTER' )? 'JOIN'
					{
					string_literal115=(Token)match(input,85,FOLLOW_85_in_join_type1707); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_85.add(string_literal115);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:197:16: ( 'OUTER' )?
					int alt45=2;
					int LA45_0 = input.LA(1);
					if ( (LA45_0==95) ) {
						alt45=1;
					}
					switch (alt45) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:197:16: 'OUTER'
							{
							string_literal116=(Token)match(input,95,FOLLOW_95_in_join_type1709); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_95.add(string_literal116);

							}
							break;

					}

					string_literal117=(Token)match(input,84,FOLLOW_84_in_join_type1712); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_84.add(string_literal117);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 197:32: -> LEFT_OUTER_JOIN
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(LEFT_OUTER_JOIN, "LEFT_OUTER_JOIN"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:9: 'FULL' ( 'OUTER' )? 'JOIN'
					{
					string_literal118=(Token)match(input,75,FOLLOW_75_in_join_type1726); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal118);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:16: ( 'OUTER' )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==95) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:198:16: 'OUTER'
							{
							string_literal119=(Token)match(input,95,FOLLOW_95_in_join_type1728); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_95.add(string_literal119);

							}
							break;

					}

					string_literal120=(Token)match(input,84,FOLLOW_84_in_join_type1731); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_84.add(string_literal120);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 198:32: -> FULL_OUTER_JOIN
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(FULL_OUTER_JOIN, "FULL_OUTER_JOIN"));
					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:199:9: ( 'INNER' )? 'JOIN'
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:199:9: ( 'INNER' )?
					int alt47=2;
					int LA47_0 = input.LA(1);
					if ( (LA47_0==80) ) {
						alt47=1;
					}
					switch (alt47) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:199:9: 'INNER'
							{
							string_literal121=(Token)match(input,80,FOLLOW_80_in_join_type1745); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_80.add(string_literal121);

							}
							break;

					}

					string_literal122=(Token)match(input,84,FOLLOW_84_in_join_type1748); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_84.add(string_literal122);

					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 199:25: -> JOIN
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(JOIN, "JOIN"));
					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join_type"


	public static class table_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:201:1: table : non_join_table ( join_type ^ non_join_table 'ON' ! search_condition )* ;
	public final SQL92QueryParser.table_return table() throws RecognitionException {
		SQL92QueryParser.table_return retval = new SQL92QueryParser.table_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal126=null;
		ParserRuleReturnScope non_join_table123 =null;
		ParserRuleReturnScope join_type124 =null;
		ParserRuleReturnScope non_join_table125 =null;
		ParserRuleReturnScope search_condition127 =null;

		CommonTree string_literal126_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:5: ( non_join_table ( join_type ^ non_join_table 'ON' ! search_condition )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:9: non_join_table ( join_type ^ non_join_table 'ON' ! search_condition )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_non_join_table_in_table1770);
			non_join_table123=non_join_table();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table123.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:24: ( join_type ^ non_join_table 'ON' ! search_condition )*
			loop49:
			while (true) {
				int alt49=2;
				int LA49_0 = input.LA(1);
				if ( (LA49_0==75||LA49_0==80||(LA49_0 >= 84 && LA49_0 <= 85)||LA49_0==96) ) {
					alt49=1;
				}

				switch (alt49) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:202:25: join_type ^ non_join_table 'ON' ! search_condition
					{
					pushFollow(FOLLOW_join_type_in_table1773);
					join_type124=join_type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot(join_type124.getTree(), root_0);
					pushFollow(FOLLOW_non_join_table_in_table1776);
					non_join_table125=non_join_table();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, non_join_table125.getTree());

					string_literal126=(Token)match(input,92,FOLLOW_92_in_table1778); if (state.failed) return retval;
					pushFollow(FOLLOW_search_condition_in_table1781);
					search_condition127=search_condition();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition127.getTree());

					}
					break;

				default :
					break loop49;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table"


	public static class non_join_table_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "non_join_table"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:204:1: non_join_table : ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) );
	public final SQL92QueryParser.non_join_table_return non_join_table() throws RecognitionException {
		SQL92QueryParser.non_join_table_return retval = new SQL92QueryParser.non_join_table_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope table_name128 =null;
		ParserRuleReturnScope correlation_specification129 =null;
		ParserRuleReturnScope table_function130 =null;
		ParserRuleReturnScope correlation_specification131 =null;
		ParserRuleReturnScope sub_query132 =null;
		ParserRuleReturnScope correlation_specification133 =null;

		RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
		RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
		RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");
		RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:5: ( table_name ( correlation_specification )? -> ^( RELATION table_name ( correlation_specification )? ) | table_function correlation_specification -> ^( RELATION table_function correlation_specification ) | sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
			int alt51=3;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==ID) ) {
				int LA51_1 = input.LA(2);
				if ( (LA51_1==42) ) {
					alt51=2;
				}
				else if ( (LA51_1==EOF||LA51_1==ID||LA51_1==43||LA51_1==46||LA51_1==50||LA51_1==61||LA51_1==71||(LA51_1 >= 75 && LA51_1 <= 77)||(LA51_1 >= 80 && LA51_1 <= 81)||(LA51_1 >= 84 && LA51_1 <= 85)||LA51_1==87||LA51_1==92||LA51_1==94||LA51_1==96||(LA51_1 >= 104 && LA51_1 <= 105)) ) {
					alt51=1;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 51, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA51_0==42) ) {
				alt51=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 51, 0, input);
				throw nvae;
			}

			switch (alt51) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:9: table_name ( correlation_specification )?
					{
					pushFollow(FOLLOW_table_name_in_non_join_table1801);
					table_name128=table_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_name.add(table_name128.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:20: ( correlation_specification )?
					int alt50=2;
					int LA50_0 = input.LA(1);
					if ( (LA50_0==ID||LA50_0==61) ) {
						alt50=1;
					}
					switch (alt50) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:20: correlation_specification
							{
							pushFollow(FOLLOW_correlation_specification_in_non_join_table1803);
							correlation_specification129=correlation_specification();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification129.getTree());
							}
							break;

					}

					// AST REWRITE
					// elements: table_name, correlation_specification
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 205:47: -> ^( RELATION table_name ( correlation_specification )? )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:50: ^( RELATION table_name ( correlation_specification )? )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_table_name.nextTree());
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:205:72: ( correlation_specification )?
						if ( stream_correlation_specification.hasNext() ) {
							adaptor.addChild(root_1, stream_correlation_specification.nextTree());
						}
						stream_correlation_specification.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:9: table_function correlation_specification
					{
					pushFollow(FOLLOW_table_function_in_non_join_table1825);
					table_function130=table_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_function.add(table_function130.getTree());
					pushFollow(FOLLOW_correlation_specification_in_non_join_table1827);
					correlation_specification131=correlation_specification();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification131.getTree());
					// AST REWRITE
					// elements: table_function, correlation_specification
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 206:50: -> ^( RELATION table_function correlation_specification )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:206:53: ^( RELATION table_function correlation_specification )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_table_function.nextTree());
						adaptor.addChild(root_1, stream_correlation_specification.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:207:9: sub_query correlation_specification
					{
					pushFollow(FOLLOW_sub_query_in_non_join_table1847);
					sub_query132=sub_query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_sub_query.add(sub_query132.getTree());
					pushFollow(FOLLOW_correlation_specification_in_non_join_table1849);
					correlation_specification133=correlation_specification();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification133.getTree());
					// AST REWRITE
					// elements: correlation_specification, sub_query
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 207:45: -> ^( RELATION sub_query correlation_specification )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:207:48: ^( RELATION sub_query correlation_specification )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_sub_query.nextTree());
						adaptor.addChild(root_1, stream_correlation_specification.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "non_join_table"


	public static class table_function_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_function"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:209:1: table_function : name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) ;
	public final SQL92QueryParser.table_function_return table_function() throws RecognitionException {
		SQL92QueryParser.table_function_return retval = new SQL92QueryParser.table_function_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token name=null;
		Token char_literal134=null;
		Token char_literal136=null;
		Token char_literal138=null;
		Token char_literal140=null;
		ParserRuleReturnScope table_function_subquery135 =null;
		ParserRuleReturnScope table_function_subquery137 =null;
		ParserRuleReturnScope table_function_param139 =null;

		CommonTree name_tree=null;
		CommonTree char_literal134_tree=null;
		CommonTree char_literal136_tree=null;
		CommonTree char_literal138_tree=null;
		CommonTree char_literal140_tree=null;
		RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleSubtreeStream stream_table_function_subquery=new RewriteRuleSubtreeStream(adaptor,"rule table_function_subquery");
		RewriteRuleSubtreeStream stream_table_function_param=new RewriteRuleSubtreeStream(adaptor,"rule table_function_param");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:5: (name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')' -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:9: name= ID '(' ( table_function_subquery )? ( ',' table_function_subquery )* ( ( ',' )? table_function_param )* ')'
			{
			name=(Token)match(input,ID,FOLLOW_ID_in_table_function1879); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(name);

			char_literal134=(Token)match(input,42,FOLLOW_42_in_table_function1881); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_42.add(char_literal134);

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:21: ( table_function_subquery )?
			int alt52=2;
			int LA52_0 = input.LA(1);
			if ( (LA52_0==42) ) {
				int LA52_1 = input.LA(2);
				if ( (synpred94_SQL92Query()) ) {
					alt52=1;
				}
			}
			switch (alt52) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:21: table_function_subquery
					{
					pushFollow(FOLLOW_table_function_subquery_in_table_function1883);
					table_function_subquery135=table_function_subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery135.getTree());
					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:46: ( ',' table_function_subquery )*
			loop53:
			while (true) {
				int alt53=2;
				int LA53_0 = input.LA(1);
				if ( (LA53_0==46) ) {
					int LA53_1 = input.LA(2);
					if ( (synpred95_SQL92Query()) ) {
						alt53=1;
					}

				}

				switch (alt53) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:47: ',' table_function_subquery
					{
					char_literal136=(Token)match(input,46,FOLLOW_46_in_table_function1887); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_46.add(char_literal136);

					pushFollow(FOLLOW_table_function_subquery_in_table_function1889);
					table_function_subquery137=table_function_subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_function_subquery.add(table_function_subquery137.getTree());
					}
					break;

				default :
					break loop53;
				}
			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:77: ( ( ',' )? table_function_param )*
			loop55:
			while (true) {
				int alt55=2;
				int LA55_0 = input.LA(1);
				if ( (LA55_0==FLOAT||(LA55_0 >= ID && LA55_0 <= INT)||LA55_0==NUMERIC||LA55_0==STRING||LA55_0==42||(LA55_0 >= 45 && LA55_0 <= 47)||LA55_0==57||(LA55_0 >= 66 && LA55_0 <= 68)||(LA55_0 >= 72 && LA55_0 <= 73)||LA55_0==78||LA55_0==82||(LA55_0 >= 88 && LA55_0 <= 91)||LA55_0==97||(LA55_0 >= 100 && LA55_0 <= 101)||LA55_0==103||LA55_0==106) ) {
					alt55=1;
				}

				switch (alt55) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:78: ( ',' )? table_function_param
					{
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:78: ( ',' )?
					int alt54=2;
					int LA54_0 = input.LA(1);
					if ( (LA54_0==46) ) {
						alt54=1;
					}
					switch (alt54) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:78: ','
							{
							char_literal138=(Token)match(input,46,FOLLOW_46_in_table_function1894); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_46.add(char_literal138);

							}
							break;

					}

					pushFollow(FOLLOW_table_function_param_in_table_function1897);
					table_function_param139=table_function_param();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_function_param.add(table_function_param139.getTree());
					}
					break;

				default :
					break loop55;
				}
			}

			char_literal140=(Token)match(input,43,FOLLOW_43_in_table_function1901); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_43.add(char_literal140);

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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 211:14: -> ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:211:17: ^( FUNCTION $name ( table_function_subquery )* ( table_function_param )* )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCTION, "FUNCTION"), root_1);
				adaptor.addChild(root_1, stream_name.nextNode());
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:211:34: ( table_function_subquery )*
				while ( stream_table_function_subquery.hasNext() ) {
					adaptor.addChild(root_1, stream_table_function_subquery.nextTree());
				}
				stream_table_function_subquery.reset();

				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:211:61: ( table_function_param )*
				while ( stream_table_function_param.hasNext() ) {
					adaptor.addChild(root_1, stream_table_function_param.nextTree());
				}
				stream_table_function_param.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_function"


	public static class table_function_subquery_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_function_subquery"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:213:1: table_function_subquery : sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) ;
	public final SQL92QueryParser.table_function_subquery_return table_function_subquery() throws RecognitionException {
		SQL92QueryParser.table_function_subquery_return retval = new SQL92QueryParser.table_function_subquery_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope sub_query141 =null;
		ParserRuleReturnScope correlation_specification142 =null;

		RewriteRuleSubtreeStream stream_correlation_specification=new RewriteRuleSubtreeStream(adaptor,"rule correlation_specification");
		RewriteRuleSubtreeStream stream_sub_query=new RewriteRuleSubtreeStream(adaptor,"rule sub_query");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:214:5: ( sub_query correlation_specification -> ^( RELATION sub_query correlation_specification ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:214:9: sub_query correlation_specification
			{
			pushFollow(FOLLOW_sub_query_in_table_function_subquery1949);
			sub_query141=sub_query();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_sub_query.add(sub_query141.getTree());
			pushFollow(FOLLOW_correlation_specification_in_table_function_subquery1951);
			correlation_specification142=correlation_specification();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_correlation_specification.add(correlation_specification142.getTree());
			// AST REWRITE
			// elements: correlation_specification, sub_query
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 214:45: -> ^( RELATION sub_query correlation_specification )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:214:48: ^( RELATION sub_query correlation_specification )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
				adaptor.addChild(root_1, stream_sub_query.nextTree());
				adaptor.addChild(root_1, stream_correlation_specification.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_function_subquery"


	public static class table_function_param_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_function_param"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:216:1: table_function_param : ( search_condition | value_expression );
	public final SQL92QueryParser.table_function_param_return table_function_param() throws RecognitionException {
		SQL92QueryParser.table_function_param_return retval = new SQL92QueryParser.table_function_param_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope search_condition143 =null;
		ParserRuleReturnScope value_expression144 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:5: ( search_condition | value_expression )
			int alt56=2;
			switch ( input.LA(1) ) {
			case 57:
			case 68:
			case 72:
			case 90:
				{
				alt56=1;
				}
				break;
			case ID:
				{
				int LA56_2 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case STRING:
				{
				int LA56_3 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 45:
				{
				int LA56_4 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 47:
				{
				int LA56_5 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 42:
				{
				int LA56_6 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case INT:
				{
				int LA56_7 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case FLOAT:
				{
				int LA56_8 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case NUMERIC:
				{
				int LA56_9 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 66:
				{
				int LA56_10 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 101:
				{
				int LA56_11 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 100:
				{
				int LA56_12 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 82:
				{
				int LA56_13 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 106:
				{
				int LA56_14 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 89:
				{
				int LA56_15 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 67:
				{
				int LA56_16 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 78:
				{
				int LA56_17 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 88:
				{
				int LA56_18 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 97:
				{
				int LA56_19 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 91:
				{
				int LA56_20 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 103:
				{
				int LA56_21 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			case 73:
				{
				int LA56_22 = input.LA(2);
				if ( (synpred98_SQL92Query()) ) {
					alt56=1;
				}
				else if ( (true) ) {
					alt56=2;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 56, 0, input);
				throw nvae;
			}
			switch (alt56) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:9: search_condition
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_search_condition_in_table_function_param1979);
					search_condition143=search_condition();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition143.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:218:9: value_expression
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_value_expression_in_table_function_param1989);
					value_expression144=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression144.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_function_param"


	public static class relation_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "relation"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:220:1: relation : ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) );
	public final SQL92QueryParser.relation_return relation() throws RecognitionException {
		SQL92QueryParser.relation_return retval = new SQL92QueryParser.relation_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope table_name145 =null;
		ParserRuleReturnScope table_function146 =null;
		ParserRuleReturnScope query147 =null;

		RewriteRuleSubtreeStream stream_table_function=new RewriteRuleSubtreeStream(adaptor,"rule table_function");
		RewriteRuleSubtreeStream stream_query=new RewriteRuleSubtreeStream(adaptor,"rule query");
		RewriteRuleSubtreeStream stream_table_name=new RewriteRuleSubtreeStream(adaptor,"rule table_name");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:221:5: ( table_name -> ^( RELATION table_name ) | table_function -> ^( RELATION table_function ) | query -> ^( RELATION query ) )
			int alt57=3;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==ID) ) {
				int LA57_1 = input.LA(2);
				if ( (LA57_1==42) ) {
					alt57=2;
				}
				else if ( (LA57_1==EOF) ) {
					alt57=1;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 57, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( (LA57_0==42||LA57_0==98) ) {
				alt57=3;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 57, 0, input);
				throw nvae;
			}

			switch (alt57) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:221:9: table_name
					{
					pushFollow(FOLLOW_table_name_in_relation2011);
					table_name145=table_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_name.add(table_name145.getTree());
					// AST REWRITE
					// elements: table_name
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 221:20: -> ^( RELATION table_name )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:221:23: ^( RELATION table_name )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_table_name.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:9: table_function
					{
					pushFollow(FOLLOW_table_function_in_relation2029);
					table_function146=table_function();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_table_function.add(table_function146.getTree());
					// AST REWRITE
					// elements: table_function
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 222:24: -> ^( RELATION table_function )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:222:27: ^( RELATION table_function )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_table_function.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:223:9: query
					{
					pushFollow(FOLLOW_query_in_relation2047);
					query147=query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_query.add(query147.getTree());
					// AST REWRITE
					// elements: query
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 223:15: -> ^( RELATION query )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:223:18: ^( RELATION query )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(RELATION, "RELATION"), root_1);
						adaptor.addChild(root_1, stream_query.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "relation"


	public static class search_condition_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "search_condition"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:225:1: search_condition : boolean_factor ( 'OR' ^ boolean_factor )* ;
	public final SQL92QueryParser.search_condition_return search_condition() throws RecognitionException {
		SQL92QueryParser.search_condition_return retval = new SQL92QueryParser.search_condition_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal149=null;
		ParserRuleReturnScope boolean_factor148 =null;
		ParserRuleReturnScope boolean_factor150 =null;

		CommonTree string_literal149_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:226:5: ( boolean_factor ( 'OR' ^ boolean_factor )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:226:9: boolean_factor ( 'OR' ^ boolean_factor )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_boolean_factor_in_search_condition2073);
			boolean_factor148=boolean_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor148.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:226:24: ( 'OR' ^ boolean_factor )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==93) ) {
					alt58=1;
				}

				switch (alt58) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:226:25: 'OR' ^ boolean_factor
					{
					string_literal149=(Token)match(input,93,FOLLOW_93_in_search_condition2076); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal149_tree = (CommonTree)adaptor.create(string_literal149);
					root_0 = (CommonTree)adaptor.becomeRoot(string_literal149_tree, root_0);
					}

					pushFollow(FOLLOW_boolean_factor_in_search_condition2079);
					boolean_factor150=boolean_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_factor150.getTree());

					}
					break;

				default :
					break loop58;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "search_condition"


	public static class boolean_factor_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "boolean_factor"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:228:1: boolean_factor : boolean_term ( 'AND' ^ boolean_term )* ;
	public final SQL92QueryParser.boolean_factor_return boolean_factor() throws RecognitionException {
		SQL92QueryParser.boolean_factor_return retval = new SQL92QueryParser.boolean_factor_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal152=null;
		ParserRuleReturnScope boolean_term151 =null;
		ParserRuleReturnScope boolean_term153 =null;

		CommonTree string_literal152_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:5: ( boolean_term ( 'AND' ^ boolean_term )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:9: boolean_term ( 'AND' ^ boolean_term )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_boolean_term_in_boolean_factor2099);
			boolean_term151=boolean_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term151.getTree());

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:22: ( 'AND' ^ boolean_term )*
			loop59:
			while (true) {
				int alt59=2;
				int LA59_0 = input.LA(1);
				if ( (LA59_0==59) ) {
					alt59=1;
				}

				switch (alt59) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:229:23: 'AND' ^ boolean_term
					{
					string_literal152=(Token)match(input,59,FOLLOW_59_in_boolean_factor2102); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal152_tree = (CommonTree)adaptor.create(string_literal152);
					root_0 = (CommonTree)adaptor.becomeRoot(string_literal152_tree, root_0);
					}

					pushFollow(FOLLOW_boolean_term_in_boolean_factor2105);
					boolean_term153=boolean_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_term153.getTree());

					}
					break;

				default :
					break loop59;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_factor"


	public static class boolean_term_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "boolean_term"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:231:1: boolean_term : ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) );
	public final SQL92QueryParser.boolean_term_return boolean_term() throws RecognitionException {
		SQL92QueryParser.boolean_term_return retval = new SQL92QueryParser.boolean_term_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal155=null;
		ParserRuleReturnScope boolean_test154 =null;
		ParserRuleReturnScope boolean_term156 =null;

		CommonTree string_literal155_tree=null;
		RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
		RewriteRuleSubtreeStream stream_boolean_term=new RewriteRuleSubtreeStream(adaptor,"rule boolean_term");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:232:5: ( boolean_test | 'NOT' boolean_term -> ^( NOT boolean_term ) )
			int alt60=2;
			int LA60_0 = input.LA(1);
			if ( (LA60_0==FLOAT||(LA60_0 >= ID && LA60_0 <= INT)||LA60_0==NUMERIC||LA60_0==STRING||LA60_0==42||LA60_0==45||LA60_0==47||LA60_0==57||(LA60_0 >= 66 && LA60_0 <= 68)||(LA60_0 >= 72 && LA60_0 <= 73)||LA60_0==78||LA60_0==82||(LA60_0 >= 88 && LA60_0 <= 89)||LA60_0==91||LA60_0==97||(LA60_0 >= 100 && LA60_0 <= 101)||LA60_0==103||LA60_0==106) ) {
				alt60=1;
			}
			else if ( (LA60_0==90) ) {
				alt60=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 60, 0, input);
				throw nvae;
			}

			switch (alt60) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:232:9: boolean_test
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_boolean_test_in_boolean_term2125);
					boolean_test154=boolean_test();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_test154.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:233:9: 'NOT' boolean_term
					{
					string_literal155=(Token)match(input,90,FOLLOW_90_in_boolean_term2135); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_90.add(string_literal155);

					pushFollow(FOLLOW_boolean_term_in_boolean_term2137);
					boolean_term156=boolean_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_boolean_term.add(boolean_term156.getTree());
					// AST REWRITE
					// elements: boolean_term
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 233:28: -> ^( NOT boolean_term )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:233:31: ^( NOT boolean_term )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);
						adaptor.addChild(root_1, stream_boolean_term.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_term"


	public static class boolean_test_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "boolean_test"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:235:1: boolean_test : boolean_primary ;
	public final SQL92QueryParser.boolean_test_return boolean_test() throws RecognitionException {
		SQL92QueryParser.boolean_test_return retval = new SQL92QueryParser.boolean_test_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope boolean_primary157 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:236:5: ( boolean_primary )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:236:9: boolean_primary
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_boolean_primary_in_boolean_test2163);
			boolean_primary157=boolean_primary();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_primary157.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_test"


	public static class boolean_primary_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "boolean_primary"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:238:1: boolean_primary : ( predicate | '(' ! search_condition ')' !);
	public final SQL92QueryParser.boolean_primary_return boolean_primary() throws RecognitionException {
		SQL92QueryParser.boolean_primary_return retval = new SQL92QueryParser.boolean_primary_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal159=null;
		Token char_literal161=null;
		ParserRuleReturnScope predicate158 =null;
		ParserRuleReturnScope search_condition160 =null;

		CommonTree char_literal159_tree=null;
		CommonTree char_literal161_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:239:5: ( predicate | '(' ! search_condition ')' !)
			int alt61=2;
			int LA61_0 = input.LA(1);
			if ( (LA61_0==FLOAT||(LA61_0 >= ID && LA61_0 <= INT)||LA61_0==NUMERIC||LA61_0==STRING||LA61_0==45||LA61_0==47||LA61_0==57||(LA61_0 >= 66 && LA61_0 <= 68)||(LA61_0 >= 72 && LA61_0 <= 73)||LA61_0==78||LA61_0==82||(LA61_0 >= 88 && LA61_0 <= 89)||LA61_0==91||LA61_0==97||(LA61_0 >= 100 && LA61_0 <= 101)||LA61_0==103||LA61_0==106) ) {
				alt61=1;
			}
			else if ( (LA61_0==42) ) {
				int LA61_6 = input.LA(2);
				if ( (synpred104_SQL92Query()) ) {
					alt61=1;
				}
				else if ( (true) ) {
					alt61=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 61, 0, input);
				throw nvae;
			}

			switch (alt61) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:239:9: predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_predicate_in_boolean_primary2181);
					predicate158=predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, predicate158.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:239:21: '(' ! search_condition ')' !
					{
					root_0 = (CommonTree)adaptor.nil();


					char_literal159=(Token)match(input,42,FOLLOW_42_in_boolean_primary2185); if (state.failed) return retval;
					pushFollow(FOLLOW_search_condition_in_boolean_primary2188);
					search_condition160=search_condition();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, search_condition160.getTree());

					char_literal161=(Token)match(input,43,FOLLOW_43_in_boolean_primary2190); if (state.failed) return retval;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "boolean_primary"


	public static class predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:241:1: predicate : ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate );
	public final SQL92QueryParser.predicate_return predicate() throws RecognitionException {
		SQL92QueryParser.predicate_return retval = new SQL92QueryParser.predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope comparison_predicate162 =null;
		ParserRuleReturnScope like_predicate163 =null;
		ParserRuleReturnScope in_predicate164 =null;
		ParserRuleReturnScope null_predicate165 =null;
		ParserRuleReturnScope exists_predicate166 =null;
		ParserRuleReturnScope between_predicate167 =null;


		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:5: ( comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate )
			int alt62=6;
			switch ( input.LA(1) ) {
			case 57:
				{
				alt62=1;
				}
				break;
			case ID:
				{
				int LA62_2 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case STRING:
				{
				int LA62_3 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 45:
				{
				int LA62_4 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 47:
				{
				int LA62_5 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 42:
				{
				int LA62_6 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case INT:
				{
				int LA62_7 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case FLOAT:
				{
				int LA62_8 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case NUMERIC:
				{
				int LA62_9 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 66:
				{
				int LA62_10 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 101:
				{
				int LA62_11 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 100:
				{
				int LA62_12 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 82:
				{
				int LA62_13 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 106:
				{
				int LA62_14 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 89:
				{
				int LA62_15 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 67:
				{
				int LA62_16 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 78:
				{
				int LA62_17 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 88:
				{
				int LA62_18 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 97:
				{
				int LA62_19 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 91:
				{
				int LA62_20 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 103:
				{
				int LA62_21 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 73:
				{
				int LA62_22 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 68:
				{
				int LA62_23 = input.LA(2);
				if ( (synpred105_SQL92Query()) ) {
					alt62=1;
				}
				else if ( (synpred106_SQL92Query()) ) {
					alt62=2;
				}
				else if ( (synpred107_SQL92Query()) ) {
					alt62=3;
				}
				else if ( (synpred108_SQL92Query()) ) {
					alt62=4;
				}
				else if ( (true) ) {
					alt62=6;
				}

				}
				break;
			case 72:
				{
				alt62=5;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 62, 0, input);
				throw nvae;
			}
			switch (alt62) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:9: comparison_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_comparison_predicate_in_predicate2212);
					comparison_predicate162=comparison_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_predicate162.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:32: like_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_like_predicate_in_predicate2216);
					like_predicate163=like_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_predicate163.getTree());

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:49: in_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_in_predicate_in_predicate2220);
					in_predicate164=in_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_predicate164.getTree());

					}
					break;
				case 4 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:64: null_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_null_predicate_in_predicate2224);
					null_predicate165=null_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_predicate165.getTree());

					}
					break;
				case 5 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:81: exists_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_exists_predicate_in_predicate2228);
					exists_predicate166=exists_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_predicate166.getTree());

					}
					break;
				case 6 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:100: between_predicate
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_between_predicate_in_predicate2232);
					between_predicate167=between_predicate();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_predicate167.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "predicate"


	public static class null_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "null_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:244:1: null_predicate : ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) );
	public final SQL92QueryParser.null_predicate_return null_predicate() throws RecognitionException {
		SQL92QueryParser.null_predicate_return retval = new SQL92QueryParser.null_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal169=null;
		Token string_literal170=null;
		Token string_literal172=null;
		Token string_literal173=null;
		Token string_literal174=null;
		ParserRuleReturnScope row_value168 =null;
		ParserRuleReturnScope row_value171 =null;

		CommonTree string_literal169_tree=null;
		CommonTree string_literal170_tree=null;
		CommonTree string_literal172_tree=null;
		CommonTree string_literal173_tree=null;
		CommonTree string_literal174_tree=null;
		RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
		RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
		RewriteRuleTokenStream stream_83=new RewriteRuleTokenStream(adaptor,"token 83");
		RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:5: ( row_value 'IS' 'NULL' -> ^( IS_NULL row_value ) | row_value 'IS' 'NOT' 'NULL' -> ^( NOT ^( IS_NULL row_value ) ) )
			int alt63=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA63_1 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case STRING:
				{
				int LA63_2 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 45:
				{
				int LA63_3 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 47:
				{
				int LA63_4 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 42:
				{
				int LA63_5 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case INT:
				{
				int LA63_6 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case FLOAT:
				{
				int LA63_7 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case NUMERIC:
				{
				int LA63_8 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 66:
				{
				int LA63_9 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 101:
				{
				int LA63_10 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 100:
				{
				int LA63_11 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 82:
				{
				int LA63_12 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 106:
				{
				int LA63_13 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 89:
				{
				int LA63_14 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 67:
				{
				int LA63_15 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 78:
				{
				int LA63_16 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 88:
				{
				int LA63_17 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 97:
				{
				int LA63_18 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 91:
				{
				int LA63_19 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 103:
				{
				int LA63_20 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 73:
				{
				int LA63_21 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			case 68:
				{
				int LA63_22 = input.LA(2);
				if ( (synpred110_SQL92Query()) ) {
					alt63=1;
				}
				else if ( (true) ) {
					alt63=2;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 63, 0, input);
				throw nvae;
			}
			switch (alt63) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: row_value 'IS' 'NULL'
					{
					pushFollow(FOLLOW_row_value_in_null_predicate2250);
					row_value168=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(row_value168.getTree());
					string_literal169=(Token)match(input,83,FOLLOW_83_in_null_predicate2252); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_83.add(string_literal169);

					string_literal170=(Token)match(input,91,FOLLOW_91_in_null_predicate2254); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_91.add(string_literal170);

					// AST REWRITE
					// elements: row_value
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 245:31: -> ^( IS_NULL row_value )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:34: ^( IS_NULL row_value )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_1);
						adaptor.addChild(root_1, stream_row_value.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:246:9: row_value 'IS' 'NOT' 'NULL'
					{
					pushFollow(FOLLOW_row_value_in_null_predicate2272);
					row_value171=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(row_value171.getTree());
					string_literal172=(Token)match(input,83,FOLLOW_83_in_null_predicate2274); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_83.add(string_literal172);

					string_literal173=(Token)match(input,90,FOLLOW_90_in_null_predicate2276); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_90.add(string_literal173);

					string_literal174=(Token)match(input,91,FOLLOW_91_in_null_predicate2278); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_91.add(string_literal174);

					// AST REWRITE
					// elements: row_value
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 246:37: -> ^( NOT ^( IS_NULL row_value ) )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:246:40: ^( NOT ^( IS_NULL row_value ) )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:246:46: ^( IS_NULL row_value )
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IS_NULL, "IS_NULL"), root_2);
						adaptor.addChild(root_2, stream_row_value.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "null_predicate"


	public static class in_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "in_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:248:1: in_predicate : ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) );
	public final SQL92QueryParser.in_predicate_return in_predicate() throws RecognitionException {
		SQL92QueryParser.in_predicate_return retval = new SQL92QueryParser.in_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal176=null;
		Token string_literal177=null;
		Token string_literal180=null;
		ParserRuleReturnScope row_value175 =null;
		ParserRuleReturnScope in_predicate_tail178 =null;
		ParserRuleReturnScope row_value179 =null;
		ParserRuleReturnScope in_predicate_tail181 =null;

		CommonTree string_literal176_tree=null;
		CommonTree string_literal177_tree=null;
		CommonTree string_literal180_tree=null;
		RewriteRuleTokenStream stream_79=new RewriteRuleTokenStream(adaptor,"token 79");
		RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
		RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");
		RewriteRuleSubtreeStream stream_in_predicate_tail=new RewriteRuleSubtreeStream(adaptor,"rule in_predicate_tail");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:249:5: ( row_value 'NOT' 'IN' in_predicate_tail -> ^( NOT ^( 'IN' row_value in_predicate_tail ) ) | row_value 'IN' in_predicate_tail -> ^( 'IN' row_value in_predicate_tail ) )
			int alt64=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA64_1 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case STRING:
				{
				int LA64_2 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 45:
				{
				int LA64_3 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 47:
				{
				int LA64_4 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 42:
				{
				int LA64_5 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case INT:
				{
				int LA64_6 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case FLOAT:
				{
				int LA64_7 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case NUMERIC:
				{
				int LA64_8 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 66:
				{
				int LA64_9 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 101:
				{
				int LA64_10 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 100:
				{
				int LA64_11 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 82:
				{
				int LA64_12 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 106:
				{
				int LA64_13 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 89:
				{
				int LA64_14 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 67:
				{
				int LA64_15 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 78:
				{
				int LA64_16 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 88:
				{
				int LA64_17 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 97:
				{
				int LA64_18 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 91:
				{
				int LA64_19 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 103:
				{
				int LA64_20 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 73:
				{
				int LA64_21 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			case 68:
				{
				int LA64_22 = input.LA(2);
				if ( (synpred111_SQL92Query()) ) {
					alt64=1;
				}
				else if ( (true) ) {
					alt64=2;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 64, 0, input);
				throw nvae;
			}
			switch (alt64) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:249:9: row_value 'NOT' 'IN' in_predicate_tail
					{
					pushFollow(FOLLOW_row_value_in_in_predicate2308);
					row_value175=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(row_value175.getTree());
					string_literal176=(Token)match(input,90,FOLLOW_90_in_in_predicate2310); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_90.add(string_literal176);

					string_literal177=(Token)match(input,79,FOLLOW_79_in_in_predicate2312); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_79.add(string_literal177);

					pushFollow(FOLLOW_in_predicate_tail_in_in_predicate2314);
					in_predicate_tail178=in_predicate_tail();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail178.getTree());
					// AST REWRITE
					// elements: row_value, 79, in_predicate_tail
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 250:13: -> ^( NOT ^( 'IN' row_value in_predicate_tail ) )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:250:16: ^( NOT ^( 'IN' row_value in_predicate_tail ) )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:250:22: ^( 'IN' row_value in_predicate_tail )
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot(stream_79.nextNode(), root_2);
						adaptor.addChild(root_2, stream_row_value.nextTree());
						adaptor.addChild(root_2, stream_in_predicate_tail.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:251:9: row_value 'IN' in_predicate_tail
					{
					pushFollow(FOLLOW_row_value_in_in_predicate2350);
					row_value179=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(row_value179.getTree());
					string_literal180=(Token)match(input,79,FOLLOW_79_in_in_predicate2352); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_79.add(string_literal180);

					pushFollow(FOLLOW_in_predicate_tail_in_in_predicate2354);
					in_predicate_tail181=in_predicate_tail();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_in_predicate_tail.add(in_predicate_tail181.getTree());
					// AST REWRITE
					// elements: 79, in_predicate_tail, row_value
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 252:13: -> ^( 'IN' row_value in_predicate_tail )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:252:16: ^( 'IN' row_value in_predicate_tail )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_79.nextNode(), root_1);
						adaptor.addChild(root_1, stream_row_value.nextTree());
						adaptor.addChild(root_1, stream_in_predicate_tail.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "in_predicate"


	public static class in_predicate_tail_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "in_predicate_tail"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:254:1: in_predicate_tail : ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) );
	public final SQL92QueryParser.in_predicate_tail_return in_predicate_tail() throws RecognitionException {
		SQL92QueryParser.in_predicate_tail_return retval = new SQL92QueryParser.in_predicate_tail_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal183=null;
		Token char_literal185=null;
		Token char_literal187=null;
		ParserRuleReturnScope sub_query182 =null;
		ParserRuleReturnScope value_expression184 =null;
		ParserRuleReturnScope value_expression186 =null;

		CommonTree char_literal183_tree=null;
		CommonTree char_literal185_tree=null;
		CommonTree char_literal187_tree=null;
		RewriteRuleTokenStream stream_46=new RewriteRuleTokenStream(adaptor,"token 46");
		RewriteRuleTokenStream stream_42=new RewriteRuleTokenStream(adaptor,"token 42");
		RewriteRuleTokenStream stream_43=new RewriteRuleTokenStream(adaptor,"token 43");
		RewriteRuleSubtreeStream stream_value_expression=new RewriteRuleSubtreeStream(adaptor,"rule value_expression");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:5: ( sub_query | '(' ( value_expression ( ',' value_expression )* ) ')' -> ^( SET ( value_expression )* ) )
			int alt66=2;
			int LA66_0 = input.LA(1);
			if ( (LA66_0==42) ) {
				int LA66_1 = input.LA(2);
				if ( (synpred112_SQL92Query()) ) {
					alt66=1;
				}
				else if ( (true) ) {
					alt66=2;
				}

			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 66, 0, input);
				throw nvae;
			}

			switch (alt66) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: sub_query
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_sub_query_in_in_predicate_tail2394);
					sub_query182=sub_query();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query182.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:9: '(' ( value_expression ( ',' value_expression )* ) ')'
					{
					char_literal183=(Token)match(input,42,FOLLOW_42_in_in_predicate_tail2405); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_42.add(char_literal183);

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:13: ( value_expression ( ',' value_expression )* )
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:14: value_expression ( ',' value_expression )*
					{
					pushFollow(FOLLOW_value_expression_in_in_predicate_tail2408);
					value_expression184=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_value_expression.add(value_expression184.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:31: ( ',' value_expression )*
					loop65:
					while (true) {
						int alt65=2;
						int LA65_0 = input.LA(1);
						if ( (LA65_0==46) ) {
							alt65=1;
						}

						switch (alt65) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:32: ',' value_expression
							{
							char_literal185=(Token)match(input,46,FOLLOW_46_in_in_predicate_tail2411); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_46.add(char_literal185);

							pushFollow(FOLLOW_value_expression_in_in_predicate_tail2413);
							value_expression186=value_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_value_expression.add(value_expression186.getTree());
							}
							break;

						default :
							break loop65;
						}
					}

					}

					char_literal187=(Token)match(input,43,FOLLOW_43_in_in_predicate_tail2418); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_43.add(char_literal187);

					// AST REWRITE
					// elements: value_expression
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 256:60: -> ^( SET ( value_expression )* )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:63: ^( SET ( value_expression )* )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SET, "SET"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:256:69: ( value_expression )*
						while ( stream_value_expression.hasNext() ) {
							adaptor.addChild(root_1, stream_value_expression.nextTree());
						}
						stream_value_expression.reset();

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "in_predicate_tail"


	public static class between_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "between_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:258:1: between_predicate : (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) |value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) );
	public final SQL92QueryParser.between_predicate_return between_predicate() throws RecognitionException {
		SQL92QueryParser.between_predicate_return retval = new SQL92QueryParser.between_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal188=null;
		Token string_literal189=null;
		Token string_literal190=null;
		Token string_literal191=null;
		Token string_literal192=null;
		ParserRuleReturnScope value =null;
		ParserRuleReturnScope btw1 =null;
		ParserRuleReturnScope btw2 =null;

		CommonTree string_literal188_tree=null;
		CommonTree string_literal189_tree=null;
		CommonTree string_literal190_tree=null;
		CommonTree string_literal191_tree=null;
		CommonTree string_literal192_tree=null;
		RewriteRuleTokenStream stream_59=new RewriteRuleTokenStream(adaptor,"token 59");
		RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
		RewriteRuleTokenStream stream_63=new RewriteRuleTokenStream(adaptor,"token 63");
		RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:259:5: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( 'BETWEEN' $value $btw1 $btw2) |value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) ) )
			int alt67=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA67_1 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case STRING:
				{
				int LA67_2 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 45:
				{
				int LA67_3 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 47:
				{
				int LA67_4 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 42:
				{
				int LA67_5 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case INT:
				{
				int LA67_6 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case FLOAT:
				{
				int LA67_7 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case NUMERIC:
				{
				int LA67_8 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 66:
				{
				int LA67_9 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 101:
				{
				int LA67_10 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 100:
				{
				int LA67_11 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 82:
				{
				int LA67_12 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 106:
				{
				int LA67_13 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 89:
				{
				int LA67_14 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 67:
				{
				int LA67_15 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 78:
				{
				int LA67_16 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 88:
				{
				int LA67_17 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 97:
				{
				int LA67_18 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 91:
				{
				int LA67_19 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 103:
				{
				int LA67_20 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 73:
				{
				int LA67_21 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

				}
				break;
			case 68:
				{
				int LA67_22 = input.LA(2);
				if ( (synpred114_SQL92Query()) ) {
					alt67=1;
				}
				else if ( (true) ) {
					alt67=2;
				}

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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:259:9: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
					{
					pushFollow(FOLLOW_row_value_in_between_predicate2447);
					value=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
					string_literal188=(Token)match(input,63,FOLLOW_63_in_between_predicate2449); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(string_literal188);

					pushFollow(FOLLOW_row_value_in_between_predicate2453);
					btw1=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
					string_literal189=(Token)match(input,59,FOLLOW_59_in_between_predicate2455); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_59.add(string_literal189);

					pushFollow(FOLLOW_row_value_in_between_predicate2459);
					btw2=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());
					// AST REWRITE
					// elements: btw2, btw1, value, 63
					// token labels: 
					// rule labels: btw1, btw2, value, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.getTree():null);
					RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.getTree():null);
					RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 260:13: -> ^( 'BETWEEN' $value $btw1 $btw2)
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:260:16: ^( 'BETWEEN' $value $btw1 $btw2)
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_63.nextNode(), root_1);
						adaptor.addChild(root_1, stream_value.nextTree());
						adaptor.addChild(root_1, stream_btw1.nextTree());
						adaptor.addChild(root_1, stream_btw2.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:261:9: value= row_value 'NOT' 'BETWEEN' btw1= row_value 'AND' btw2= row_value
					{
					pushFollow(FOLLOW_row_value_in_between_predicate2499);
					value=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(value.getTree());
					string_literal190=(Token)match(input,90,FOLLOW_90_in_between_predicate2501); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_90.add(string_literal190);

					string_literal191=(Token)match(input,63,FOLLOW_63_in_between_predicate2503); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_63.add(string_literal191);

					pushFollow(FOLLOW_row_value_in_between_predicate2507);
					btw1=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(btw1.getTree());
					string_literal192=(Token)match(input,59,FOLLOW_59_in_between_predicate2509); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_59.add(string_literal192);

					pushFollow(FOLLOW_row_value_in_between_predicate2513);
					btw2=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(btw2.getTree());
					// AST REWRITE
					// elements: btw1, btw2, value, 63
					// token labels: 
					// rule labels: btw1, btw2, value, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_btw1=new RewriteRuleSubtreeStream(adaptor,"rule btw1",btw1!=null?btw1.getTree():null);
					RewriteRuleSubtreeStream stream_btw2=new RewriteRuleSubtreeStream(adaptor,"rule btw2",btw2!=null?btw2.getTree():null);
					RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 262:13: -> ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:262:16: ^( NOT ^( 'BETWEEN' $value $btw1 $btw2) )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:262:22: ^( 'BETWEEN' $value $btw1 $btw2)
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot(stream_63.nextNode(), root_2);
						adaptor.addChild(root_2, stream_value.nextTree());
						adaptor.addChild(root_2, stream_btw1.nextTree());
						adaptor.addChild(root_2, stream_btw2.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "between_predicate"


	public static class exists_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "exists_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:264:1: exists_predicate : 'EXISTS' ^ sub_query ;
	public final SQL92QueryParser.exists_predicate_return exists_predicate() throws RecognitionException {
		SQL92QueryParser.exists_predicate_return retval = new SQL92QueryParser.exists_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal193=null;
		ParserRuleReturnScope sub_query194 =null;

		CommonTree string_literal193_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:5: ( 'EXISTS' ^ sub_query )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:265:9: 'EXISTS' ^ sub_query
			{
			root_0 = (CommonTree)adaptor.nil();


			string_literal193=(Token)match(input,72,FOLLOW_72_in_exists_predicate2562); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal193_tree = (CommonTree)adaptor.create(string_literal193);
			root_0 = (CommonTree)adaptor.becomeRoot(string_literal193_tree, root_0);
			}

			pushFollow(FOLLOW_sub_query_in_exists_predicate2565);
			sub_query194=sub_query();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, sub_query194.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "exists_predicate"


	public static class comparison_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "comparison_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:267:1: comparison_predicate : ( bind_table '=' ^ row_value |lv= row_value (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' ) (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) ^ row_value );
	public final SQL92QueryParser.comparison_predicate_return comparison_predicate() throws RecognitionException {
		SQL92QueryParser.comparison_predicate_return retval = new SQL92QueryParser.comparison_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token op=null;
		Token ep=null;
		Token char_literal196=null;
		Token set199=null;
		ParserRuleReturnScope lv =null;
		ParserRuleReturnScope rv =null;
		ParserRuleReturnScope bind_table195 =null;
		ParserRuleReturnScope row_value197 =null;
		ParserRuleReturnScope row_value198 =null;
		ParserRuleReturnScope row_value200 =null;

		CommonTree op_tree=null;
		CommonTree ep_tree=null;
		CommonTree char_literal196_tree=null;
		CommonTree set199_tree=null;
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_99=new RewriteRuleTokenStream(adaptor,"token 99");
		RewriteRuleTokenStream stream_56=new RewriteRuleTokenStream(adaptor,"token 56");
		RewriteRuleTokenStream stream_58=new RewriteRuleTokenStream(adaptor,"token 58");
		RewriteRuleTokenStream stream_60=new RewriteRuleTokenStream(adaptor,"token 60");
		RewriteRuleTokenStream stream_51=new RewriteRuleTokenStream(adaptor,"token 51");
		RewriteRuleTokenStream stream_41=new RewriteRuleTokenStream(adaptor,"token 41");
		RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
		RewriteRuleTokenStream stream_53=new RewriteRuleTokenStream(adaptor,"token 53");
		RewriteRuleTokenStream stream_54=new RewriteRuleTokenStream(adaptor,"token 54");
		RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:268:5: ( bind_table '=' ^ row_value |lv= row_value (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' ) (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' ) rv= row_value -> ^( $ep ^( $op $lv $rv) ) | row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) ^ row_value )
			int alt70=3;
			switch ( input.LA(1) ) {
			case 57:
				{
				alt70=1;
				}
				break;
			case ID:
				{
				int LA70_2 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case STRING:
				{
				int LA70_3 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 45:
				{
				int LA70_4 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 47:
				{
				int LA70_5 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 42:
				{
				int LA70_6 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case INT:
				{
				int LA70_7 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case FLOAT:
				{
				int LA70_8 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case NUMERIC:
				{
				int LA70_9 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 66:
				{
				int LA70_10 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 101:
				{
				int LA70_11 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 100:
				{
				int LA70_12 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 82:
				{
				int LA70_13 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 106:
				{
				int LA70_14 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 89:
				{
				int LA70_15 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 67:
				{
				int LA70_16 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 78:
				{
				int LA70_17 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 88:
				{
				int LA70_18 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 97:
				{
				int LA70_19 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 91:
				{
				int LA70_20 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 103:
				{
				int LA70_21 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 73:
				{
				int LA70_22 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			case 68:
				{
				int LA70_23 = input.LA(2);
				if ( (synpred124_SQL92Query()) ) {
					alt70=2;
				}
				else if ( (true) ) {
					alt70=3;
				}

				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 70, 0, input);
				throw nvae;
			}
			switch (alt70) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:268:9: bind_table '=' ^ row_value
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_bind_table_in_comparison_predicate2583);
					bind_table195=bind_table();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, bind_table195.getTree());

					char_literal196=(Token)match(input,54,FOLLOW_54_in_comparison_predicate2585); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal196_tree = (CommonTree)adaptor.create(char_literal196);
					root_0 = (CommonTree)adaptor.becomeRoot(char_literal196_tree, root_0);
					}

					pushFollow(FOLLOW_row_value_in_comparison_predicate2588);
					row_value197=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value197.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:9: lv= row_value (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' ) (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' ) rv= row_value
					{
					pushFollow(FOLLOW_row_value_in_comparison_predicate2600);
					lv=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(lv.getTree());
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:22: (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' )
					int alt68=7;
					switch ( input.LA(1) ) {
					case 54:
						{
						alt68=1;
						}
						break;
					case 53:
						{
						alt68=2;
						}
						break;
					case 41:
						{
						alt68=3;
						}
						break;
					case 51:
						{
						alt68=4;
						}
						break;
					case 55:
						{
						alt68=5;
						}
						break;
					case 56:
						{
						alt68=6;
						}
						break;
					case 52:
						{
						alt68=7;
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
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:23: op= '='
							{
							op=(Token)match(input,54,FOLLOW_54_in_comparison_predicate2605); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_54.add(op);

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:30: op= '<>'
							{
							op=(Token)match(input,53,FOLLOW_53_in_comparison_predicate2609); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_53.add(op);

							}
							break;
						case 3 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:38: op= '!='
							{
							op=(Token)match(input,41,FOLLOW_41_in_comparison_predicate2613); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_41.add(op);

							}
							break;
						case 4 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:46: op= '<'
							{
							op=(Token)match(input,51,FOLLOW_51_in_comparison_predicate2617); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_51.add(op);

							}
							break;
						case 5 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:53: op= '>'
							{
							op=(Token)match(input,55,FOLLOW_55_in_comparison_predicate2621); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_55.add(op);

							}
							break;
						case 6 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:60: op= '>='
							{
							op=(Token)match(input,56,FOLLOW_56_in_comparison_predicate2625); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_56.add(op);

							}
							break;
						case 7 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:68: op= '<='
							{
							op=(Token)match(input,52,FOLLOW_52_in_comparison_predicate2629); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_52.add(op);

							}
							break;

					}

					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:77: (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' )
					int alt69=3;
					switch ( input.LA(1) ) {
					case 58:
						{
						alt69=1;
						}
						break;
					case 99:
						{
						alt69=2;
						}
						break;
					case 60:
						{
						alt69=3;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 69, 0, input);
						throw nvae;
					}
					switch (alt69) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:78: ep= 'ALL'
							{
							ep=(Token)match(input,58,FOLLOW_58_in_comparison_predicate2635); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_58.add(ep);

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:87: ep= 'SOME'
							{
							ep=(Token)match(input,99,FOLLOW_99_in_comparison_predicate2639); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_99.add(ep);

							}
							break;
						case 3 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:97: ep= 'ANY'
							{
							ep=(Token)match(input,60,FOLLOW_60_in_comparison_predicate2643); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_60.add(ep);

							}
							break;

					}

					pushFollow(FOLLOW_row_value_in_comparison_predicate2648);
					rv=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(rv.getTree());
					// AST REWRITE
					// elements: op, rv, lv, ep
					// token labels: op, ep
					// rule labels: rv, lv, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleTokenStream stream_op=new RewriteRuleTokenStream(adaptor,"token op",op);
					RewriteRuleTokenStream stream_ep=new RewriteRuleTokenStream(adaptor,"token ep",ep);
					RewriteRuleSubtreeStream stream_rv=new RewriteRuleSubtreeStream(adaptor,"rule rv",rv!=null?rv.getTree():null);
					RewriteRuleSubtreeStream stream_lv=new RewriteRuleSubtreeStream(adaptor,"rule lv",lv!=null?lv.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 270:13: -> ^( $ep ^( $op $lv $rv) )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:16: ^( $ep ^( $op $lv $rv) )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_ep.nextNode(), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:270:22: ^( $op $lv $rv)
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


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:271:9: row_value ( '=' | '<>' | '!=' | '<' | '>' | '>=' | '<=' ) ^ row_value
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_row_value_in_comparison_predicate2688);
					row_value198=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value198.getTree());

					set199=input.LT(1);
					set199=input.LT(1);
					if ( input.LA(1)==41||(input.LA(1) >= 51 && input.LA(1) <= 56) ) {
						input.consume();
						if ( state.backtracking==0 ) root_0 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(set199), root_0);
						state.errorRecovery=false;
						state.failed=false;
					}
					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					pushFollow(FOLLOW_row_value_in_comparison_predicate2719);
					row_value200=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value200.getTree());

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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "comparison_predicate"


	public static class like_predicate_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "like_predicate"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:273:1: like_predicate : ( row_value 'LIKE' ^ row_value |v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) );
	public final SQL92QueryParser.like_predicate_return like_predicate() throws RecognitionException {
		SQL92QueryParser.like_predicate_return retval = new SQL92QueryParser.like_predicate_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal202=null;
		Token string_literal204=null;
		Token string_literal205=null;
		ParserRuleReturnScope v1 =null;
		ParserRuleReturnScope v2 =null;
		ParserRuleReturnScope row_value201 =null;
		ParserRuleReturnScope row_value203 =null;

		CommonTree string_literal202_tree=null;
		CommonTree string_literal204_tree=null;
		CommonTree string_literal205_tree=null;
		RewriteRuleTokenStream stream_90=new RewriteRuleTokenStream(adaptor,"token 90");
		RewriteRuleTokenStream stream_86=new RewriteRuleTokenStream(adaptor,"token 86");
		RewriteRuleSubtreeStream stream_row_value=new RewriteRuleSubtreeStream(adaptor,"rule row_value");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:5: ( row_value 'LIKE' ^ row_value |v1= row_value 'NOT' 'LIKE' v2= row_value -> ^( NOT ^( 'LIKE' $v1 $v2) ) )
			int alt71=2;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA71_1 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case STRING:
				{
				int LA71_2 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 45:
				{
				int LA71_3 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 47:
				{
				int LA71_4 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 42:
				{
				int LA71_5 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case INT:
				{
				int LA71_6 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case FLOAT:
				{
				int LA71_7 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case NUMERIC:
				{
				int LA71_8 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 66:
				{
				int LA71_9 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 101:
				{
				int LA71_10 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 100:
				{
				int LA71_11 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 82:
				{
				int LA71_12 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 106:
				{
				int LA71_13 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 89:
				{
				int LA71_14 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 67:
				{
				int LA71_15 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 78:
				{
				int LA71_16 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 88:
				{
				int LA71_17 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 97:
				{
				int LA71_18 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 91:
				{
				int LA71_19 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 103:
				{
				int LA71_20 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 73:
				{
				int LA71_21 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

				}
				break;
			case 68:
				{
				int LA71_22 = input.LA(2);
				if ( (synpred131_SQL92Query()) ) {
					alt71=1;
				}
				else if ( (true) ) {
					alt71=2;
				}

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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: row_value 'LIKE' ^ row_value
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_row_value_in_like_predicate2737);
					row_value201=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value201.getTree());

					string_literal202=(Token)match(input,86,FOLLOW_86_in_like_predicate2739); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal202_tree = (CommonTree)adaptor.create(string_literal202);
					root_0 = (CommonTree)adaptor.becomeRoot(string_literal202_tree, root_0);
					}

					pushFollow(FOLLOW_row_value_in_like_predicate2742);
					row_value203=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, row_value203.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:275:9: v1= row_value 'NOT' 'LIKE' v2= row_value
					{
					pushFollow(FOLLOW_row_value_in_like_predicate2754);
					v1=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(v1.getTree());
					string_literal204=(Token)match(input,90,FOLLOW_90_in_like_predicate2756); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_90.add(string_literal204);

					string_literal205=(Token)match(input,86,FOLLOW_86_in_like_predicate2758); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_86.add(string_literal205);

					pushFollow(FOLLOW_row_value_in_like_predicate2762);
					v2=row_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_row_value.add(v2.getTree());
					// AST REWRITE
					// elements: v2, 86, v1
					// token labels: 
					// rule labels: v1, v2, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_v1=new RewriteRuleSubtreeStream(adaptor,"rule v1",v1!=null?v1.getTree():null);
					RewriteRuleSubtreeStream stream_v2=new RewriteRuleSubtreeStream(adaptor,"rule v2",v2!=null?v2.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 275:48: -> ^( NOT ^( 'LIKE' $v1 $v2) )
					{
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:275:51: ^( NOT ^( 'LIKE' $v1 $v2) )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NOT, "NOT"), root_1);
						// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:275:57: ^( 'LIKE' $v1 $v2)
						{
						CommonTree root_2 = (CommonTree)adaptor.nil();
						root_2 = (CommonTree)adaptor.becomeRoot(stream_86.nextNode(), root_2);
						adaptor.addChild(root_2, stream_v1.nextTree());
						adaptor.addChild(root_2, stream_v2.nextTree());
						adaptor.addChild(root_1, root_2);
						}

						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "like_predicate"


	public static class row_value_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "row_value"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:277:1: row_value : ( value_expression | 'NULL' | 'DEFAULT' );
	public final SQL92QueryParser.row_value_return row_value() throws RecognitionException {
		SQL92QueryParser.row_value_return retval = new SQL92QueryParser.row_value_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal207=null;
		Token string_literal208=null;
		ParserRuleReturnScope value_expression206 =null;

		CommonTree string_literal207_tree=null;
		CommonTree string_literal208_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:5: ( value_expression | 'NULL' | 'DEFAULT' )
			int alt72=3;
			switch ( input.LA(1) ) {
			case FLOAT:
			case ID:
			case INT:
			case NUMERIC:
			case STRING:
			case 42:
			case 45:
			case 47:
			case 66:
			case 67:
			case 73:
			case 78:
			case 82:
			case 88:
			case 89:
			case 97:
			case 100:
			case 101:
			case 103:
			case 106:
				{
				alt72=1;
				}
				break;
			case 91:
				{
				int LA72_2 = input.LA(2);
				if ( (synpred132_SQL92Query()) ) {
					alt72=1;
				}
				else if ( (synpred133_SQL92Query()) ) {
					alt72=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 72, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 68:
				{
				alt72=3;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 72, 0, input);
				throw nvae;
			}
			switch (alt72) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:9: value_expression
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_value_expression_in_row_value2796);
					value_expression206=value_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, value_expression206.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:27: 'NULL'
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal207=(Token)match(input,91,FOLLOW_91_in_row_value2799); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal207_tree = (CommonTree)adaptor.create(string_literal207);
					adaptor.addChild(root_0, string_literal207_tree);
					}

					}
					break;
				case 3 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:36: 'DEFAULT'
					{
					root_0 = (CommonTree)adaptor.nil();


					string_literal208=(Token)match(input,68,FOLLOW_68_in_row_value2803); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal208_tree = (CommonTree)adaptor.create(string_literal208);
					adaptor.addChild(root_0, string_literal208_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "row_value"


	public static class bind_table_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "bind_table"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:280:1: bind_table : '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) ;
	public final SQL92QueryParser.bind_table_return bind_table() throws RecognitionException {
		SQL92QueryParser.bind_table_return retval = new SQL92QueryParser.bind_table_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token tableid=null;
		Token columnid=null;
		Token char_literal209=null;
		Token char_literal210=null;

		CommonTree tableid_tree=null;
		CommonTree columnid_tree=null;
		CommonTree char_literal209_tree=null;
		CommonTree char_literal210_tree=null;
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:281:5: ( '@' tableid= ID '.' columnid= ID -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) ) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:281:9: '@' tableid= ID '.' columnid= ID
			{
			char_literal209=(Token)match(input,57,FOLLOW_57_in_bind_table2821); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_57.add(char_literal209);

			tableid=(Token)match(input,ID,FOLLOW_ID_in_bind_table2824); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(tableid);

			char_literal210=(Token)match(input,48,FOLLOW_48_in_bind_table2825); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_48.add(char_literal210);

			columnid=(Token)match(input,ID,FOLLOW_ID_in_bind_table2828); if (state.failed) return retval; 
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
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 281:37: -> ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:281:40: ^( BOUND ^( TABLECOLUMN $tableid $columnid) )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(BOUND, "BOUND"), root_1);
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:281:48: ^( TABLECOLUMN $tableid $columnid)
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


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "bind_table"


	public static class correlation_specification_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "correlation_specification"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:283:1: correlation_specification : ( 'AS' !)? ID ;
	public final SQL92QueryParser.correlation_specification_return correlation_specification() throws RecognitionException {
		SQL92QueryParser.correlation_specification_return retval = new SQL92QueryParser.correlation_specification_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token string_literal211=null;
		Token ID212=null;

		CommonTree string_literal211_tree=null;
		CommonTree ID212_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:284:5: ( ( 'AS' !)? ID )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:284:9: ( 'AS' !)? ID
			{
			root_0 = (CommonTree)adaptor.nil();


			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:284:9: ( 'AS' !)?
			int alt73=2;
			int LA73_0 = input.LA(1);
			if ( (LA73_0==61) ) {
				alt73=1;
			}
			switch (alt73) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:284:10: 'AS' !
					{
					string_literal211=(Token)match(input,61,FOLLOW_61_in_correlation_specification2863); if (state.failed) return retval;
					}
					break;

			}

			ID212=(Token)match(input,ID,FOLLOW_ID_in_correlation_specification2868); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ID212_tree = (CommonTree)adaptor.create(ID212);
			adaptor.addChild(root_0, ID212_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "correlation_specification"


	public static class table_name_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "table_name"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:286:1: table_name : ID ;
	public final SQL92QueryParser.table_name_return table_name() throws RecognitionException {
		SQL92QueryParser.table_name_return retval = new SQL92QueryParser.table_name_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token ID213=null;

		CommonTree ID213_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:287:5: ( ID )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:287:9: ID
			{
			root_0 = (CommonTree)adaptor.nil();


			ID213=(Token)match(input,ID,FOLLOW_ID_in_table_name2889); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ID213_tree = (CommonTree)adaptor.create(ID213);
			adaptor.addChild(root_0, ID213_tree);
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "table_name"


	public static class column_list_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "column_list"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:289:1: column_list : ( column_name | reserved_word_column_name ) ( ',' ! ( column_name | reserved_word_column_name ) )* ;
	public final SQL92QueryParser.column_list_return column_list() throws RecognitionException {
		SQL92QueryParser.column_list_return retval = new SQL92QueryParser.column_list_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token char_literal216=null;
		ParserRuleReturnScope column_name214 =null;
		ParserRuleReturnScope reserved_word_column_name215 =null;
		ParserRuleReturnScope column_name217 =null;
		ParserRuleReturnScope reserved_word_column_name218 =null;

		CommonTree char_literal216_tree=null;

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:5: ( ( column_name | reserved_word_column_name ) ( ',' ! ( column_name | reserved_word_column_name ) )* )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:9: ( column_name | reserved_word_column_name ) ( ',' ! ( column_name | reserved_word_column_name ) )*
			{
			root_0 = (CommonTree)adaptor.nil();


			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:9: ( column_name | reserved_word_column_name )
			int alt74=2;
			int LA74_0 = input.LA(1);
			if ( (LA74_0==ID) ) {
				int LA74_1 = input.LA(2);
				if ( (LA74_1==48) ) {
					int LA74_3 = input.LA(3);
					if ( (LA74_3==ID) ) {
						alt74=1;
					}
					else if ( ((LA74_3 >= 66 && LA74_3 <= 67)||LA74_3==78||LA74_3==82||(LA74_3 >= 88 && LA74_3 <= 89)||LA74_3==97||(LA74_3 >= 100 && LA74_3 <= 101)||LA74_3==106) ) {
						alt74=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 74, 3, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA74_1==EOF||LA74_1==43||LA74_1==46||LA74_1==50||LA74_1==71||LA74_1==77||LA74_1==81||LA74_1==87||LA74_1==94||LA74_1==104) ) {
					alt74=1;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 74, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}
			else if ( ((LA74_0 >= 66 && LA74_0 <= 67)||LA74_0==78||LA74_0==82||(LA74_0 >= 88 && LA74_0 <= 89)||LA74_0==97||(LA74_0 >= 100 && LA74_0 <= 101)||LA74_0==106) ) {
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
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:10: column_name
					{
					pushFollow(FOLLOW_column_name_in_column_list2908);
					column_name214=column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name214.getTree());

					}
					break;
				case 2 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:24: reserved_word_column_name
					{
					pushFollow(FOLLOW_reserved_word_column_name_in_column_list2912);
					reserved_word_column_name215=reserved_word_column_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name215.getTree());

					}
					break;

			}

			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:51: ( ',' ! ( column_name | reserved_word_column_name ) )*
			loop76:
			while (true) {
				int alt76=2;
				int LA76_0 = input.LA(1);
				if ( (LA76_0==46) ) {
					alt76=1;
				}

				switch (alt76) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:52: ',' ! ( column_name | reserved_word_column_name )
					{
					char_literal216=(Token)match(input,46,FOLLOW_46_in_column_list2916); if (state.failed) return retval;
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:57: ( column_name | reserved_word_column_name )
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==ID) ) {
						int LA75_1 = input.LA(2);
						if ( (LA75_1==48) ) {
							int LA75_3 = input.LA(3);
							if ( (LA75_3==ID) ) {
								alt75=1;
							}
							else if ( ((LA75_3 >= 66 && LA75_3 <= 67)||LA75_3==78||LA75_3==82||(LA75_3 >= 88 && LA75_3 <= 89)||LA75_3==97||(LA75_3 >= 100 && LA75_3 <= 101)||LA75_3==106) ) {
								alt75=2;
							}

							else {
								if (state.backtracking>0) {state.failed=true; return retval;}
								int nvaeMark = input.mark();
								try {
									for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
										input.consume();
									}
									NoViableAltException nvae =
										new NoViableAltException("", 75, 3, input);
									throw nvae;
								} finally {
									input.rewind(nvaeMark);
								}
							}

						}
						else if ( (LA75_1==EOF||LA75_1==43||LA75_1==46||LA75_1==50||LA75_1==71||LA75_1==77||LA75_1==81||LA75_1==87||LA75_1==94||LA75_1==104) ) {
							alt75=1;
						}

						else {
							if (state.backtracking>0) {state.failed=true; return retval;}
							int nvaeMark = input.mark();
							try {
								input.consume();
								NoViableAltException nvae =
									new NoViableAltException("", 75, 1, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}
					else if ( ((LA75_0 >= 66 && LA75_0 <= 67)||LA75_0==78||LA75_0==82||(LA75_0 >= 88 && LA75_0 <= 89)||LA75_0==97||(LA75_0 >= 100 && LA75_0 <= 101)||LA75_0==106) ) {
						alt75=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 75, 0, input);
						throw nvae;
					}

					switch (alt75) {
						case 1 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:58: column_name
							{
							pushFollow(FOLLOW_column_name_in_column_list2920);
							column_name217=column_name();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, column_name217.getTree());

							}
							break;
						case 2 :
							// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:290:72: reserved_word_column_name
							{
							pushFollow(FOLLOW_reserved_word_column_name_in_column_list2924);
							reserved_word_column_name218=reserved_word_column_name();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, reserved_word_column_name218.getTree());

							}
							break;

					}

					}
					break;

				default :
					break loop76;
				}
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "column_list"


	public static class column_name_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "column_name"
	// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:292:1: column_name : (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) ;
	public final SQL92QueryParser.column_name_return column_name() throws RecognitionException {
		SQL92QueryParser.column_name_return retval = new SQL92QueryParser.column_name_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token tableid=null;
		Token columnid=null;
		Token char_literal219=null;

		CommonTree tableid_tree=null;
		CommonTree columnid_tree=null;
		CommonTree char_literal219_tree=null;
		RewriteRuleTokenStream stream_48=new RewriteRuleTokenStream(adaptor,"token 48");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

		try {
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:5: ( (tableid= ID '.' )? columnid= ID -> ^( TABLECOLUMN ( $tableid)? $columnid) )
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:9: (tableid= ID '.' )? columnid= ID
			{
			// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:9: (tableid= ID '.' )?
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==ID) ) {
				int LA77_1 = input.LA(2);
				if ( (LA77_1==48) ) {
					alt77=1;
				}
			}
			switch (alt77) {
				case 1 :
					// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:10: tableid= ID '.'
					{
					tableid=(Token)match(input,ID,FOLLOW_ID_in_column_name2948); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(tableid);

					char_literal219=(Token)match(input,48,FOLLOW_48_in_column_name2949); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_48.add(char_literal219);

					}
					break;

			}

			columnid=(Token)match(input,ID,FOLLOW_ID_in_column_name2954); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(columnid);

			// AST REWRITE
			// elements: columnid, tableid
			// token labels: columnid, tableid
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleTokenStream stream_columnid=new RewriteRuleTokenStream(adaptor,"token columnid",columnid);
			RewriteRuleTokenStream stream_tableid=new RewriteRuleTokenStream(adaptor,"token tableid",tableid);
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 293:37: -> ^( TABLECOLUMN ( $tableid)? $columnid)
			{
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:40: ^( TABLECOLUMN ( $tableid)? $columnid)
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(TABLECOLUMN, "TABLECOLUMN"), root_1);
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:293:55: ( $tableid)?
				if ( stream_tableid.hasNext() ) {
					adaptor.addChild(root_1, stream_tableid.nextNode());
				}
				stream_tableid.reset();

				adaptor.addChild(root_1, stream_columnid.nextNode());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
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
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "column_name"

	// $ANTLR start synpred40_SQL92Query
	public final void synpred40_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:17: ( ( '+' | '-' ) factor )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:155:17: ( '+' | '-' ) factor
		{
		if ( input.LA(1)==45||input.LA(1)==47 ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_factor_in_synpred40_SQL92Query1123);
		factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred40_SQL92Query

	// $ANTLR start synpred45_SQL92Query
	public final void synpred45_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:164:9: ( '(' value_expression ')' )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:164:9: '(' value_expression ')'
		{
		match(input,42,FOLLOW_42_in_synpred45_SQL92Query1204); if (state.failed) return;

		pushFollow(FOLLOW_value_expression_in_synpred45_SQL92Query1207);
		value_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,43,FOLLOW_43_in_synpred45_SQL92Query1209); if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_SQL92Query

	// $ANTLR start synpred46_SQL92Query
	public final void synpred46_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:165:9: ( function )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:165:9: function
		{
		pushFollow(FOLLOW_function_in_synpred46_SQL92Query1220);
		function();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_SQL92Query

	// $ANTLR start synpred47_SQL92Query
	public final void synpred47_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:166:9: ( column_name )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:166:9: column_name
		{
		pushFollow(FOLLOW_column_name_in_synpred47_SQL92Query1230);
		column_name();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred47_SQL92Query

	// $ANTLR start synpred48_SQL92Query
	public final void synpred48_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:9: ( literal )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:167:9: literal
		{
		pushFollow(FOLLOW_literal_in_synpred48_SQL92Query1240);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred48_SQL92Query

	// $ANTLR start synpred59_SQL92Query
	public final void synpred59_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: ( ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:174:9: ( 'DATE' | 'TIMESTAMP' | 'TIME' ) STRING
		{
		if ( input.LA(1)==66||(input.LA(1) >= 100 && input.LA(1) <= 101) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		match(input,STRING,FOLLOW_STRING_in_synpred59_SQL92Query1331); if (state.failed) return;

		}

	}
	// $ANTLR end synpred59_SQL92Query

	// $ANTLR start synpred68_SQL92Query
	public final void synpred68_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:9: ( 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:178:9: 'INTERVAL' STRING ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' )
		{
		match(input,82,FOLLOW_82_in_synpred68_SQL92Query1395); if (state.failed) return;

		match(input,STRING,FOLLOW_STRING_in_synpred68_SQL92Query1398); if (state.failed) return;

		if ( input.LA(1)==67||input.LA(1)==78||(input.LA(1) >= 88 && input.LA(1) <= 89)||input.LA(1)==97||input.LA(1)==106 ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		}

	}
	// $ANTLR end synpred68_SQL92Query

	// $ANTLR start synpred82_SQL92Query
	public final void synpred82_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:16: ( ',' table_reference )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:193:16: ',' table_reference
		{
		match(input,46,FOLLOW_46_in_synpred82_SQL92Query1664); if (state.failed) return;

		pushFollow(FOLLOW_table_reference_in_synpred82_SQL92Query1667);
		table_reference();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_SQL92Query

	// $ANTLR start synpred94_SQL92Query
	public final void synpred94_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:21: ( table_function_subquery )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:21: table_function_subquery
		{
		pushFollow(FOLLOW_table_function_subquery_in_synpred94_SQL92Query1883);
		table_function_subquery();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_SQL92Query

	// $ANTLR start synpred95_SQL92Query
	public final void synpred95_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:47: ( ',' table_function_subquery )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:210:47: ',' table_function_subquery
		{
		match(input,46,FOLLOW_46_in_synpred95_SQL92Query1887); if (state.failed) return;

		pushFollow(FOLLOW_table_function_subquery_in_synpred95_SQL92Query1889);
		table_function_subquery();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred95_SQL92Query

	// $ANTLR start synpred98_SQL92Query
	public final void synpred98_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:9: ( search_condition )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:217:9: search_condition
		{
		pushFollow(FOLLOW_search_condition_in_synpred98_SQL92Query1979);
		search_condition();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred98_SQL92Query

	// $ANTLR start synpred104_SQL92Query
	public final void synpred104_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:239:9: ( predicate )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:239:9: predicate
		{
		pushFollow(FOLLOW_predicate_in_synpred104_SQL92Query2181);
		predicate();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred104_SQL92Query

	// $ANTLR start synpred105_SQL92Query
	public final void synpred105_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:9: ( comparison_predicate )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:9: comparison_predicate
		{
		pushFollow(FOLLOW_comparison_predicate_in_synpred105_SQL92Query2212);
		comparison_predicate();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred105_SQL92Query

	// $ANTLR start synpred106_SQL92Query
	public final void synpred106_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:32: ( like_predicate )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:32: like_predicate
		{
		pushFollow(FOLLOW_like_predicate_in_synpred106_SQL92Query2216);
		like_predicate();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred106_SQL92Query

	// $ANTLR start synpred107_SQL92Query
	public final void synpred107_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:49: ( in_predicate )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:49: in_predicate
		{
		pushFollow(FOLLOW_in_predicate_in_synpred107_SQL92Query2220);
		in_predicate();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred107_SQL92Query

	// $ANTLR start synpred108_SQL92Query
	public final void synpred108_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:64: ( null_predicate )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:242:64: null_predicate
		{
		pushFollow(FOLLOW_null_predicate_in_synpred108_SQL92Query2224);
		null_predicate();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred108_SQL92Query

	// $ANTLR start synpred110_SQL92Query
	public final void synpred110_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: ( row_value 'IS' 'NULL' )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:245:9: row_value 'IS' 'NULL'
		{
		pushFollow(FOLLOW_row_value_in_synpred110_SQL92Query2250);
		row_value();
		state._fsp--;
		if (state.failed) return;

		match(input,83,FOLLOW_83_in_synpred110_SQL92Query2252); if (state.failed) return;

		match(input,91,FOLLOW_91_in_synpred110_SQL92Query2254); if (state.failed) return;

		}

	}
	// $ANTLR end synpred110_SQL92Query

	// $ANTLR start synpred111_SQL92Query
	public final void synpred111_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:249:9: ( row_value 'NOT' 'IN' in_predicate_tail )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:249:9: row_value 'NOT' 'IN' in_predicate_tail
		{
		pushFollow(FOLLOW_row_value_in_synpred111_SQL92Query2308);
		row_value();
		state._fsp--;
		if (state.failed) return;

		match(input,90,FOLLOW_90_in_synpred111_SQL92Query2310); if (state.failed) return;

		match(input,79,FOLLOW_79_in_synpred111_SQL92Query2312); if (state.failed) return;

		pushFollow(FOLLOW_in_predicate_tail_in_synpred111_SQL92Query2314);
		in_predicate_tail();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred111_SQL92Query

	// $ANTLR start synpred112_SQL92Query
	public final void synpred112_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: ( sub_query )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:255:9: sub_query
		{
		pushFollow(FOLLOW_sub_query_in_synpred112_SQL92Query2394);
		sub_query();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred112_SQL92Query

	// $ANTLR start synpred114_SQL92Query
	public final void synpred114_SQL92Query_fragment() throws RecognitionException {
		ParserRuleReturnScope value =null;
		ParserRuleReturnScope btw1 =null;
		ParserRuleReturnScope btw2 =null;


		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:259:9: (value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:259:9: value= row_value 'BETWEEN' btw1= row_value 'AND' btw2= row_value
		{
		pushFollow(FOLLOW_row_value_in_synpred114_SQL92Query2447);
		value=row_value();
		state._fsp--;
		if (state.failed) return;

		match(input,63,FOLLOW_63_in_synpred114_SQL92Query2449); if (state.failed) return;

		pushFollow(FOLLOW_row_value_in_synpred114_SQL92Query2453);
		btw1=row_value();
		state._fsp--;
		if (state.failed) return;

		match(input,59,FOLLOW_59_in_synpred114_SQL92Query2455); if (state.failed) return;

		pushFollow(FOLLOW_row_value_in_synpred114_SQL92Query2459);
		btw2=row_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred114_SQL92Query

	// $ANTLR start synpred124_SQL92Query
	public final void synpred124_SQL92Query_fragment() throws RecognitionException {
		Token op=null;
		Token ep=null;
		ParserRuleReturnScope lv =null;
		ParserRuleReturnScope rv =null;


		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:9: (lv= row_value (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' ) (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' ) rv= row_value )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:9: lv= row_value (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' ) (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' ) rv= row_value
		{
		pushFollow(FOLLOW_row_value_in_synpred124_SQL92Query2600);
		lv=row_value();
		state._fsp--;
		if (state.failed) return;

		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:22: (op= '=' |op= '<>' |op= '!=' |op= '<' |op= '>' |op= '>=' |op= '<=' )
		int alt91=7;
		switch ( input.LA(1) ) {
		case 54:
			{
			alt91=1;
			}
			break;
		case 53:
			{
			alt91=2;
			}
			break;
		case 41:
			{
			alt91=3;
			}
			break;
		case 51:
			{
			alt91=4;
			}
			break;
		case 55:
			{
			alt91=5;
			}
			break;
		case 56:
			{
			alt91=6;
			}
			break;
		case 52:
			{
			alt91=7;
			}
			break;
		default:
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 91, 0, input);
			throw nvae;
		}
		switch (alt91) {
			case 1 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:23: op= '='
				{
				op=(Token)match(input,54,FOLLOW_54_in_synpred124_SQL92Query2605); if (state.failed) return;

				}
				break;
			case 2 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:30: op= '<>'
				{
				op=(Token)match(input,53,FOLLOW_53_in_synpred124_SQL92Query2609); if (state.failed) return;

				}
				break;
			case 3 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:38: op= '!='
				{
				op=(Token)match(input,41,FOLLOW_41_in_synpred124_SQL92Query2613); if (state.failed) return;

				}
				break;
			case 4 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:46: op= '<'
				{
				op=(Token)match(input,51,FOLLOW_51_in_synpred124_SQL92Query2617); if (state.failed) return;

				}
				break;
			case 5 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:53: op= '>'
				{
				op=(Token)match(input,55,FOLLOW_55_in_synpred124_SQL92Query2621); if (state.failed) return;

				}
				break;
			case 6 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:60: op= '>='
				{
				op=(Token)match(input,56,FOLLOW_56_in_synpred124_SQL92Query2625); if (state.failed) return;

				}
				break;
			case 7 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:68: op= '<='
				{
				op=(Token)match(input,52,FOLLOW_52_in_synpred124_SQL92Query2629); if (state.failed) return;

				}
				break;

		}

		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:77: (ep= 'ALL' |ep= 'SOME' |ep= 'ANY' )
		int alt92=3;
		switch ( input.LA(1) ) {
		case 58:
			{
			alt92=1;
			}
			break;
		case 99:
			{
			alt92=2;
			}
			break;
		case 60:
			{
			alt92=3;
			}
			break;
		default:
			if (state.backtracking>0) {state.failed=true; return;}
			NoViableAltException nvae =
				new NoViableAltException("", 92, 0, input);
			throw nvae;
		}
		switch (alt92) {
			case 1 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:78: ep= 'ALL'
				{
				ep=(Token)match(input,58,FOLLOW_58_in_synpred124_SQL92Query2635); if (state.failed) return;

				}
				break;
			case 2 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:87: ep= 'SOME'
				{
				ep=(Token)match(input,99,FOLLOW_99_in_synpred124_SQL92Query2639); if (state.failed) return;

				}
				break;
			case 3 :
				// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:269:97: ep= 'ANY'
				{
				ep=(Token)match(input,60,FOLLOW_60_in_synpred124_SQL92Query2643); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_row_value_in_synpred124_SQL92Query2648);
		rv=row_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred124_SQL92Query

	// $ANTLR start synpred131_SQL92Query
	public final void synpred131_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: ( row_value 'LIKE' row_value )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:274:9: row_value 'LIKE' row_value
		{
		pushFollow(FOLLOW_row_value_in_synpred131_SQL92Query2737);
		row_value();
		state._fsp--;
		if (state.failed) return;

		match(input,86,FOLLOW_86_in_synpred131_SQL92Query2739); if (state.failed) return;

		pushFollow(FOLLOW_row_value_in_synpred131_SQL92Query2742);
		row_value();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred131_SQL92Query

	// $ANTLR start synpred132_SQL92Query
	public final void synpred132_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:9: ( value_expression )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:9: value_expression
		{
		pushFollow(FOLLOW_value_expression_in_synpred132_SQL92Query2796);
		value_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred132_SQL92Query

	// $ANTLR start synpred133_SQL92Query
	public final void synpred133_SQL92Query_fragment() throws RecognitionException {
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:27: ( 'NULL' )
		// /home/stelios/hg/firethorn-ogsadai/distributed/server/src/main/grammar/SQL92Query.g:278:27: 'NULL'
		{
		match(input,91,FOLLOW_91_in_synpred133_SQL92Query2799); if (state.failed) return;

		}

	}
	// $ANTLR end synpred133_SQL92Query

	// Delegated rules

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
	public final boolean synpred124_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred124_SQL92Query_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred114_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred114_SQL92Query_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred98_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred98_SQL92Query_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred108_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred108_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred68_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred68_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred82_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred82_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred59_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred59_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred95_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred95_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred112_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred112_SQL92Query_fragment(); // can never throw exception
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
	public final boolean synpred40_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred40_SQL92Query_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred48_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred48_SQL92Query_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}
	public final boolean synpred133_SQL92Query() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred133_SQL92Query_fragment(); // can never throw exception
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



	public static final BitSet FOLLOW_query_expression_in_statement322 = new BitSet(new long[]{0x0004000000000000L,0x0000000040800000L});
	public static final BitSet FOLLOW_order_by_in_statement324 = new BitSet(new long[]{0x0004000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_limit_in_statement327 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_50_in_statement330 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_statement333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_87_in_limit370 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_in_limit372 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_top398 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INT_in_top400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_query_in_query_expression429 = new BitSet(new long[]{0x0000000000000002L,0x0000010000020080L});
	public static final BitSet FOLLOW_set_op_in_query_expression432 = new BitSet(new long[]{0x0000040000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_query_in_query_expression435 = new BitSet(new long[]{0x0000000000000002L,0x0000010000020080L});
	public static final BitSet FOLLOW_104_in_set_op457 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_set_op459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_104_in_set_op475 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_set_op491 = new BitSet(new long[]{0x0400000000000000L});
	public static final BitSet FOLLOW_58_in_set_op493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_set_op509 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_81_in_set_op525 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_query552 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_query562 = new BitSet(new long[]{0x0400B40402060800L,0x000004F20B04424EL});
	public static final BitSet FOLLOW_top_in_query564 = new BitSet(new long[]{0x0400B40402060800L,0x000004B20B04424EL});
	public static final BitSet FOLLOW_set_quantifier_in_query567 = new BitSet(new long[]{0x0000B40402060800L,0x000004B20B04420EL});
	public static final BitSet FOLLOW_select_list_in_query570 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000400L});
	public static final BitSet FOLLOW_74_in_query572 = new BitSet(new long[]{0x0000040000020000L});
	public static final BitSet FOLLOW_table_expression_in_query574 = new BitSet(new long[]{0x0000000000000002L,0x0000020000003000L});
	public static final BitSet FOLLOW_105_in_query577 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_search_condition_in_query581 = new BitSet(new long[]{0x0000000000000002L,0x0000000000003000L});
	public static final BitSet FOLLOW_76_in_query586 = new BitSet(new long[]{0x0000000000020000L,0x000004320304400CL});
	public static final BitSet FOLLOW_column_list_in_query588 = new BitSet(new long[]{0x0000000000000002L,0x0000000000002000L});
	public static final BitSet FOLLOW_77_in_query593 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_search_condition_in_query597 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_sub_query703 = new BitSet(new long[]{0x0000040000000000L,0x0000000400000000L});
	public static final BitSet FOLLOW_query_expression_in_sub_query706 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_sub_query708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_column_in_select_list727 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_46_in_select_list730 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420EL});
	public static final BitSet FOLLOW_derived_column_in_select_list733 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_44_in_select_list745 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_65_in_derived_column769 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_derived_column772 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_derived_column774 = new BitSet(new long[]{0x2000000000000000L});
	public static final BitSet FOLLOW_61_in_derived_column776 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_derived_column780 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_derived_column782 = new BitSet(new long[]{0x2000000000020002L});
	public static final BitSet FOLLOW_61_in_derived_column785 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_derived_column790 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_value_expression_in_derived_column821 = new BitSet(new long[]{0x2000000000020002L});
	public static final BitSet FOLLOW_61_in_derived_column824 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_derived_column827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_94_in_order_by861 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_order_by863 = new BitSet(new long[]{0x0000000000060000L,0x000004320304400CL});
	public static final BitSet FOLLOW_ordered_sort_spec_in_order_by865 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_46_in_order_by868 = new BitSet(new long[]{0x0000000000060000L,0x000004320304400CL});
	public static final BitSet FOLLOW_ordered_sort_spec_in_order_by870 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_column_name_in_sort_spec899 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_sort_spec903 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_reserved_word_column_name_in_sort_spec907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sort_spec_in_ordered_sort_spec925 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
	public static final BitSet FOLLOW_69_in_ordered_sort_spec927 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sort_spec_in_ordered_sort_spec945 = new BitSet(new long[]{0x4000000000000002L});
	public static final BitSet FOLLOW_62_in_ordered_sort_spec947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_reserved_word_column_name977 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_reserved_word_column_name978 = new BitSet(new long[]{0x0000000000000000L,0x000004320304400CL});
	public static final BitSet FOLLOW_66_in_reserved_word_column_name984 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_reserved_word_column_name990 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_reserved_word_column_name996 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_reserved_word_column_name1002 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_reserved_word_column_name1008 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_reserved_word_column_name1014 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_67_in_reserved_word_column_name1020 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_reserved_word_column_name1026 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_88_in_reserved_word_column_name1032 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_97_in_reserved_word_column_name1038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_value_expression_in_value_expression1084 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_value_expression_in_value_expression1094 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_factor_in_numeric_value_expression1113 = new BitSet(new long[]{0x0000A00000000002L});
	public static final BitSet FOLLOW_set_in_numeric_value_expression1116 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_factor_in_numeric_value_expression1123 = new BitSet(new long[]{0x0000A00000000002L});
	public static final BitSet FOLLOW_numeric_primary_in_factor1145 = new BitSet(new long[]{0x0002100000000002L});
	public static final BitSet FOLLOW_set_in_factor1148 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_numeric_primary_in_factor1155 = new BitSet(new long[]{0x0002100000000002L});
	public static final BitSet FOLLOW_45_in_numeric_primary1176 = new BitSet(new long[]{0x0000040402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_47_in_numeric_primary1179 = new BitSet(new long[]{0x0000040402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_primary_in_numeric_primary1184 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_value_expression_primary1204 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_value_expression_primary1207 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_value_expression_primary1209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_in_value_expression_primary1220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_column_name_in_value_expression_primary1230 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_value_expression_primary1240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_value_expression_primary1250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_literal1268 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FLOAT_in_literal1272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUMERIC_in_literal1276 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_literal1280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_in_literal1284 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_interval_in_literal1288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_91_in_literal1292 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_literal1296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_73_in_literal1300 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_datetime1318 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_STRING_in_datetime1331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_datetime1344 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_datetime1345 = new BitSet(new long[]{0x0000000000000000L,0x0000003000000004L});
	public static final BitSet FOLLOW_66_in_datetime1351 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_101_in_datetime1357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_datetime1363 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_interval1395 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_STRING_in_interval1398 = new BitSet(new long[]{0x0000000000000000L,0x0000040203004008L});
	public static final BitSet FOLLOW_set_in_interval1400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_interval1435 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_interval1436 = new BitSet(new long[]{0x0000000000000000L,0x0000040203044008L});
	public static final BitSet FOLLOW_82_in_interval1442 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_interval1448 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_interval1454 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_67_in_interval1460 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_interval1466 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_88_in_interval1472 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_97_in_interval1478 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_function1516 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_function1519 = new BitSet(new long[]{0x0000EC0402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_function1521 = new BitSet(new long[]{0x0000480000000000L});
	public static final BitSet FOLLOW_46_in_function1525 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_function1527 = new BitSet(new long[]{0x0000480000000000L});
	public static final BitSet FOLLOW_43_in_function1531 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_function1569 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_function1572 = new BitSet(new long[]{0x0000100000000000L});
	public static final BitSet FOLLOW_44_in_function1574 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_function1576 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_column_name_in_string_value_expression1606 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_STRING_in_string_value_expression1610 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
	public static final BitSet FOLLOW_107_in_string_value_expression1614 = new BitSet(new long[]{0x0000000400020000L});
	public static final BitSet FOLLOW_column_name_in_string_value_expression1618 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
	public static final BitSet FOLLOW_STRING_in_string_value_expression1622 = new BitSet(new long[]{0x0000000000000002L,0x0000080000000000L});
	public static final BitSet FOLLOW_table_reference_in_table_expression1643 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_table_in_table_reference1661 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_46_in_table_reference1664 = new BitSet(new long[]{0x0000040000020000L});
	public static final BitSet FOLLOW_table_reference_in_table_reference1667 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_96_in_join_type1687 = new BitSet(new long[]{0x0000000000000000L,0x0000000080100000L});
	public static final BitSet FOLLOW_95_in_join_type1689 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_join_type1692 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_85_in_join_type1707 = new BitSet(new long[]{0x0000000000000000L,0x0000000080100000L});
	public static final BitSet FOLLOW_95_in_join_type1709 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_join_type1712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_75_in_join_type1726 = new BitSet(new long[]{0x0000000000000000L,0x0000000080100000L});
	public static final BitSet FOLLOW_95_in_join_type1728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_join_type1731 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_80_in_join_type1745 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
	public static final BitSet FOLLOW_84_in_join_type1748 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_non_join_table_in_table1770 = new BitSet(new long[]{0x0000000000000002L,0x0000000100310800L});
	public static final BitSet FOLLOW_join_type_in_table1773 = new BitSet(new long[]{0x0000040000020000L});
	public static final BitSet FOLLOW_non_join_table_in_table1776 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_table1778 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_search_condition_in_table1781 = new BitSet(new long[]{0x0000000000000002L,0x0000000100310800L});
	public static final BitSet FOLLOW_table_name_in_non_join_table1801 = new BitSet(new long[]{0x2000000000020002L});
	public static final BitSet FOLLOW_correlation_specification_in_non_join_table1803 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_table_function_in_non_join_table1825 = new BitSet(new long[]{0x2000000000020000L});
	public static final BitSet FOLLOW_correlation_specification_in_non_join_table1827 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_non_join_table1847 = new BitSet(new long[]{0x2000000000020000L});
	public static final BitSet FOLLOW_correlation_specification_in_non_join_table1849 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_table_function1879 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_42_in_table_function1881 = new BitSet(new long[]{0x0200EC0402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_table_function_subquery_in_table_function1883 = new BitSet(new long[]{0x0200EC0402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_46_in_table_function1887 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_table_function_subquery_in_table_function1889 = new BitSet(new long[]{0x0200EC0402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_46_in_table_function1894 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_table_function_param_in_table_function1897 = new BitSet(new long[]{0x0200EC0402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_43_in_table_function1901 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_table_function_subquery1949 = new BitSet(new long[]{0x2000000000020000L});
	public static final BitSet FOLLOW_correlation_specification_in_table_function_subquery1951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_search_condition_in_table_function_param1979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_value_expression_in_table_function_param1989 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_table_name_in_relation2011 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_table_function_in_relation2029 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_query_in_relation2047 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_factor_in_search_condition2073 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_93_in_search_condition2076 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_boolean_factor_in_search_condition2079 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
	public static final BitSet FOLLOW_boolean_term_in_boolean_factor2099 = new BitSet(new long[]{0x0800000000000002L});
	public static final BitSet FOLLOW_59_in_boolean_factor2102 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_boolean_term_in_boolean_factor2105 = new BitSet(new long[]{0x0800000000000002L});
	public static final BitSet FOLLOW_boolean_test_in_boolean_term2125 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_90_in_boolean_term2135 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_boolean_term_in_boolean_term2137 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_primary_in_boolean_test2163 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_boolean_primary2181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_boolean_primary2185 = new BitSet(new long[]{0x0200A40402060800L,0x000004B20F04431CL});
	public static final BitSet FOLLOW_search_condition_in_boolean_primary2188 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_boolean_primary2190 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_predicate_in_predicate2212 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_predicate_in_predicate2216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_predicate_in_predicate2220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_predicate_in_predicate2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_predicate_in_predicate2228 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_predicate_in_predicate2232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_null_predicate2250 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_83_in_null_predicate2252 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_null_predicate2254 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_null_predicate2272 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_83_in_null_predicate2274 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_null_predicate2276 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_null_predicate2278 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_in_predicate2308 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_in_predicate2310 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_in_predicate2312 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate2314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_in_predicate2350 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_in_predicate2352 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_in_predicate_tail_in_in_predicate2354 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_in_predicate_tail2394 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_in_predicate_tail2405 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_in_predicate_tail2408 = new BitSet(new long[]{0x0000480000000000L});
	public static final BitSet FOLLOW_46_in_in_predicate_tail2411 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_in_predicate_tail2413 = new BitSet(new long[]{0x0000480000000000L});
	public static final BitSet FOLLOW_43_in_in_predicate_tail2418 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_between_predicate2447 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_between_predicate2449 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_between_predicate2453 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_59_in_between_predicate2455 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_between_predicate2459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_between_predicate2499 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_between_predicate2501 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_between_predicate2503 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_between_predicate2507 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_59_in_between_predicate2509 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_between_predicate2513 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_exists_predicate2562 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_sub_query_in_exists_predicate2565 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_bind_table_in_comparison_predicate2583 = new BitSet(new long[]{0x0040000000000000L});
	public static final BitSet FOLLOW_54_in_comparison_predicate2585 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_comparison_predicate2588 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_comparison_predicate2600 = new BitSet(new long[]{0x01F8020000000000L});
	public static final BitSet FOLLOW_54_in_comparison_predicate2605 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_53_in_comparison_predicate2609 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_41_in_comparison_predicate2613 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_51_in_comparison_predicate2617 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_55_in_comparison_predicate2621 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_56_in_comparison_predicate2625 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_52_in_comparison_predicate2629 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_58_in_comparison_predicate2635 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_99_in_comparison_predicate2639 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_60_in_comparison_predicate2643 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_comparison_predicate2648 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_comparison_predicate2688 = new BitSet(new long[]{0x01F8020000000000L});
	public static final BitSet FOLLOW_set_in_comparison_predicate2690 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_comparison_predicate2719 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_like_predicate2737 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
	public static final BitSet FOLLOW_86_in_like_predicate2739 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_like_predicate2742 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_like_predicate2754 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_like_predicate2756 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
	public static final BitSet FOLLOW_86_in_like_predicate2758 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_like_predicate2762 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_value_expression_in_row_value2796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_91_in_row_value2799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_68_in_row_value2803 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_57_in_bind_table2821 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_bind_table2824 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_bind_table2825 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_bind_table2828 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_61_in_correlation_specification2863 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_correlation_specification2868 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_table_name2889 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_column_name_in_column_list2908 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2912 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_46_in_column_list2916 = new BitSet(new long[]{0x0000000000020000L,0x000004320304400CL});
	public static final BitSet FOLLOW_column_name_in_column_list2920 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_reserved_word_column_name_in_column_list2924 = new BitSet(new long[]{0x0000400000000002L});
	public static final BitSet FOLLOW_ID_in_column_name2948 = new BitSet(new long[]{0x0001000000000000L});
	public static final BitSet FOLLOW_48_in_column_name2949 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_ID_in_column_name2954 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_synpred40_SQL92Query1116 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_factor_in_synpred40_SQL92Query1123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_42_in_synpred45_SQL92Query1204 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04420CL});
	public static final BitSet FOLLOW_value_expression_in_synpred45_SQL92Query1207 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_43_in_synpred45_SQL92Query1209 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_in_synpred46_SQL92Query1220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_column_name_in_synpred47_SQL92Query1230 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred48_SQL92Query1240 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_synpred59_SQL92Query1318 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_STRING_in_synpred59_SQL92Query1331 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_82_in_synpred68_SQL92Query1395 = new BitSet(new long[]{0x0000000400000000L});
	public static final BitSet FOLLOW_STRING_in_synpred68_SQL92Query1398 = new BitSet(new long[]{0x0000000000000000L,0x0000040203004008L});
	public static final BitSet FOLLOW_set_in_synpred68_SQL92Query1400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_synpred82_SQL92Query1664 = new BitSet(new long[]{0x0000040000020000L});
	public static final BitSet FOLLOW_table_reference_in_synpred82_SQL92Query1667 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_table_function_subquery_in_synpred94_SQL92Query1883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_46_in_synpred95_SQL92Query1887 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_table_function_subquery_in_synpred95_SQL92Query1889 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_search_condition_in_synpred98_SQL92Query1979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_predicate_in_synpred104_SQL92Query2181 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_predicate_in_synpred105_SQL92Query2212 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_predicate_in_synpred106_SQL92Query2216 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_predicate_in_synpred107_SQL92Query2220 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_predicate_in_synpred108_SQL92Query2224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_synpred110_SQL92Query2250 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
	public static final BitSet FOLLOW_83_in_synpred110_SQL92Query2252 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_synpred110_SQL92Query2254 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_synpred111_SQL92Query2308 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_synpred111_SQL92Query2310 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
	public static final BitSet FOLLOW_79_in_synpred111_SQL92Query2312 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_in_predicate_tail_in_synpred111_SQL92Query2314 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sub_query_in_synpred112_SQL92Query2394 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_synpred114_SQL92Query2447 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_synpred114_SQL92Query2449 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_synpred114_SQL92Query2453 = new BitSet(new long[]{0x0800000000000000L});
	public static final BitSet FOLLOW_59_in_synpred114_SQL92Query2455 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_synpred114_SQL92Query2459 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_synpred124_SQL92Query2600 = new BitSet(new long[]{0x01F8020000000000L});
	public static final BitSet FOLLOW_54_in_synpred124_SQL92Query2605 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_53_in_synpred124_SQL92Query2609 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_41_in_synpred124_SQL92Query2613 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_51_in_synpred124_SQL92Query2617 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_55_in_synpred124_SQL92Query2621 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_56_in_synpred124_SQL92Query2625 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_52_in_synpred124_SQL92Query2629 = new BitSet(new long[]{0x1400000000000000L,0x0000000800000000L});
	public static final BitSet FOLLOW_58_in_synpred124_SQL92Query2635 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_99_in_synpred124_SQL92Query2639 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_60_in_synpred124_SQL92Query2643 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_synpred124_SQL92Query2648 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_row_value_in_synpred131_SQL92Query2737 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
	public static final BitSet FOLLOW_86_in_synpred131_SQL92Query2739 = new BitSet(new long[]{0x0000A40402060800L,0x000004B20B04421CL});
	public static final BitSet FOLLOW_row_value_in_synpred131_SQL92Query2742 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_value_expression_in_synpred132_SQL92Query2796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_91_in_synpred133_SQL92Query2799 = new BitSet(new long[]{0x0000000000000002L});
}
