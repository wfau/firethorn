grammar SQL92Query;

options 
{
    output = AST;
    ASTLabelType = CommonTree;
    backtrack=true;
}

tokens 
{
    STATEMENT;
    QUERY;
    SETOP;
    ORDER;
    SELECT_LIST;
    FROM_LIST;
    WHERE;
    GROUP_BY;
    HAVING;
    RELATION;
    COLUMN;
    FUNCTION;
    NOT;
    SET;
    TABLECOLUMN;
    RIGHT_OUTER_JOIN;
    LEFT_OUTER_JOIN;
    FULL_OUTER_JOIN;
    JOIN;
    IS_NULL;
    UNION;
    EXCEPT;
    UNION_ALL;
    EXCEPT_ALL;
    INTERSECT;
    BOUND;
    CAST;
    ASC;
    DESC;
    LIMIT;
    TOP;
}

@header {
  package uk.org.ogsadai.parser.sql92query; 
}

@lexer::header {
  package uk.org.ogsadai.parser.sql92query;
}


@members 
{
protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
    throw new MismatchedTokenException(ttype, input);
}

public Object recoverFromMismatchedSet(IntStream input, RecognitionException re, BitSet follow)
    throws RecognitionException
{
    throw re;
}
}

@lexer::members 
{
protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
    throw new MismatchedTokenException(ttype, input);
}

public Object recoverFromMismatchedSet(IntStream input, RecognitionException re, BitSet follow)
    throws RecognitionException
{
    throw re;
}
}

@rulecatch {
catch (RecognitionException re)
{
    reportError(re);
    throw re;
}
}

@lexer::rulecatch {
catch (RecognitionException re)
{
    reportError(re);
    throw re;
}
}

statement
    :   query_expression order_by? limit? ';'? EOF -> ^(STATEMENT query_expression order_by? limit?)
    ;
    
limit 	
    : 'LIMIT' INT -> ^(LIMIT INT);
 
top 	
    : 'TOP' INT -> ^(TOP INT);
       
query_expression
    :   query (set_op^ query)*
    ;
set_op  
    :   'UNION' 'ALL' -> ^(UNION_ALL)
    |   'UNION' -> ^(UNION)
    |   'EXCEPT' 'ALL' -> ^(EXCEPT_ALL)
    |   'EXCEPT' -> ^(EXCEPT)
    |   'INTERSECT' -> ^(INTERSECT)
    ;
query   
    :   sub_query
    |   'SELECT' top? set_quantifier? select_list 'FROM' table_expression ('WHERE' s1=search_condition)? ('GROUP BY' column_list)? ('HAVING' s2=search_condition)?
            -> ^(QUERY ^(top? SELECT_LIST set_quantifier? select_list) ^(FROM_LIST table_expression) ^(WHERE $s1)? ^(GROUP_BY column_list)? ^(HAVING $s2)?)
    ;
    
set_quantifier
    :   'DISTINCT' | 'ALL'
    ;
sub_query
    :   '('! query_expression ')'!
    ;
select_list
    :   '*' -> ^(COLUMN '*')
    |   derived_column (','! derived_column)*
    ;
derived_column
    :   'CAST' value_expression 'AS' id1=ID ('AS'? id2=ID)? -> ^(COLUMN ^(CAST value_expression $id1) $id2?)
    |   value_expression ('AS'? ID)? -> ^(COLUMN value_expression ID?)
    ;   
order_by
    :   'ORDER' 'BY' ordered_sort_spec (',' ordered_sort_spec)* -> ^(ORDER ordered_sort_spec+)
    ;
sort_spec
    :   column_name | INT | reserved_word_column_name
    ;
ordered_sort_spec
    :   sort_spec 'DESC' -> ^(DESC sort_spec)
    |   sort_spec 'ASC'? -> ^(ASC sort_spec)
    ;
reserved_word_column_name
    :   (tableid=ID'.')?(s='DATE' | s='TIMESTAMP' | s='TIME' | s='INTERVAL' | s='YEAR' | s='MONTH' | s='DAY' | s='HOUR' | s='MINUTE' | s='SECOND' ) 
            -> ^(TABLECOLUMN $tableid? $s)
    ;
value_expression
    :   string_value_expression
    |   numeric_value_expression 
    ;
numeric_value_expression
    :   factor (('+'|'-')^ factor)*
    ;
factor  
    :   numeric_primary (('*'|'/')^ numeric_primary)*
    ;
numeric_primary
    :   ('+'^|'-'^)? value_expression_primary
    ; 
value_expression_primary 
    :   '('! value_expression ')'!
    |   function
    |   column_name
    |   literal
    |   sub_query
    ;
literal
    :   INT | FLOAT | NUMERIC | STRING | datetime | interval | 'NULL' | 'TRUE' | 'FALSE'
    ;
datetime
    :   ('DATE' | 'TIMESTAMP' | 'TIME')^ STRING
    |   (tableid=ID'.')?(s='DATE' | s='TIMESTAMP' | s='TIME') -> ^(TABLECOLUMN $tableid? $s)
    ;
interval
    :   'INTERVAL'^ STRING ('YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND')
    |   (tableid=ID'.')?(s='INTERVAL' | s='YEAR' | s='MONTH' | s='DAY' | s='HOUR' | s='MINUTE' | s='SECOND') -> ^(TABLECOLUMN $tableid? $s)
    ;   
function
    :   (name=ID) '(' value_expression? (',' value_expression)* ')' 
            -> ^(FUNCTION $name value_expression*)
    |   (name=ID) '(' '*' ')' -> ^(FUNCTION $name '*')
    ;
string_value_expression
    :   (column_name | STRING) ('||'^ (column_name | STRING))+
    ;
table_expression
    :   table_reference
    ;
table_reference
    :   table (','! table_reference)*
    ;
join_type
    :   'RIGHT' 'OUTER'? 'JOIN' -> RIGHT_OUTER_JOIN 
    |   'LEFT' 'OUTER'? 'JOIN' -> LEFT_OUTER_JOIN
    |   'FULL' 'OUTER'? 'JOIN' -> FULL_OUTER_JOIN
    |   'INNER'? 'JOIN' -> JOIN
    ;
table
    :   non_join_table (join_type^ non_join_table 'ON'! search_condition)*
    ;
non_join_table
    :   table_name correlation_specification? -> ^(RELATION table_name correlation_specification?)
    |   table_function correlation_specification -> ^(RELATION table_function correlation_specification)
    |   sub_query correlation_specification -> ^(RELATION sub_query correlation_specification)
    ;
table_function
    :   name=ID '(' table_function_subquery? (',' table_function_subquery)* (','? table_function_param)* ')'
             -> ^(FUNCTION $name (table_function_subquery)* table_function_param*)
    ;
table_function_subquery
    :   sub_query correlation_specification -> ^(RELATION sub_query correlation_specification)
    ;
table_function_param
    :   search_condition
    |   value_expression
    ;   
relation 
    :   table_name -> ^(RELATION table_name)
    |   table_function -> ^(RELATION table_function)
    |   query -> ^(RELATION query)
    ;
search_condition
    :   boolean_factor ('OR'^ boolean_factor)*
    ;
boolean_factor
    :   boolean_term ('AND'^ boolean_term)*
    ;
boolean_term
    :   boolean_test
    |   'NOT' boolean_term -> ^(NOT boolean_term)
    ;
boolean_test
    :   boolean_primary
    ;
boolean_primary
    :   predicate | '('! search_condition ')'!
    ;   
predicate
    :   comparison_predicate | like_predicate | in_predicate | null_predicate | exists_predicate | between_predicate
    ;
null_predicate
    :   row_value 'IS' 'NULL' -> ^(IS_NULL row_value)
    |   row_value 'IS' 'NOT' 'NULL' -> ^(NOT ^(IS_NULL row_value))
    ;
in_predicate
    :   row_value 'NOT' 'IN' in_predicate_tail
            -> ^(NOT ^('IN' row_value in_predicate_tail))
    |   row_value 'IN' in_predicate_tail
            -> ^('IN' row_value in_predicate_tail)
    ;
in_predicate_tail
    :   sub_query 
    |   '(' (value_expression (',' value_expression)*) ')' -> ^(SET value_expression*)
    ;
between_predicate
    :   value=row_value 'BETWEEN' btw1=row_value 'AND' btw2=row_value 
            -> ^('BETWEEN' $value $btw1 $btw2)
    |   value=row_value 'NOT' 'BETWEEN' btw1=row_value 'AND' btw2=row_value
            -> ^(NOT ^('BETWEEN' $value $btw1 $btw2))
    ;
exists_predicate
    :   'EXISTS'^ sub_query
    ;
comparison_predicate
    :   bind_table '='^ row_value
    |   lv=row_value (op='='|op='<>'|op='!='|op='<'|op='>'|op='>='|op='<=') (ep='ALL'|ep='SOME'|ep='ANY') rv=row_value
            -> ^($ep ^($op $lv $rv))
    |   row_value ('=' | '<>' | '!=' | '<' | '>' | '>=' | '<=')^ row_value
    ;
like_predicate
    :   row_value 'LIKE'^ row_value
    |   v1=row_value 'NOT' 'LIKE' v2=row_value -> ^(NOT ^('LIKE' $v1 $v2))
    ;
row_value
    :   value_expression |'NULL' | 'DEFAULT'
    ;
bind_table
    :   '@'tableid=ID'.'columnid=ID -> ^(BOUND ^(TABLECOLUMN $tableid $columnid))
    ;
correlation_specification
    :   ('AS'!)? ID
    ;   
table_name
    :   ID
    ;
column_list
    :   (column_name | reserved_word_column_name) (','! (column_name | reserved_word_column_name))*
    ;
column_name
    :   (tableid=ID'.')?columnid=ID -> ^(TABLECOLUMN $tableid? $columnid)
    ;
ID  
    :   ('a'..'z' | 'A'..'Z') ( ('a'..'z' | 'A'..'Z') | ('0'..'9') | '_' )* 
    |   '`' (~('\''|'\n'|'\r'|'`')) ( (~('\''|'\n'|'\r'|'`')) )* '`'
    ;
FLOAT   
    :   ('0'..'9')+ '.' ('0'..'9')+
    ;
INT 
    :   ('0'..'9')+
    ;
NUMERIC
    :   (INT | FLOAT) 'E' ('+' | '-')? INT
    ;
STRING  
    :   '"' (~('"'|'\n'|'\r'))*  '"'
    |   '\'' (~('\''|'\n'|'\r'))*  '\''
    ;
WS  
    :   (' ' | '\t' | '\r' | '\n' ) {skip();} 
    ;
    