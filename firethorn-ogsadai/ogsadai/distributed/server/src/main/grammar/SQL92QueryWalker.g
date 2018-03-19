tree grammar SQL92QueryWalker;

options {
    tokenVocab = SQL92Query;
    ASTLabelType = CommonTree;
    backtrack = true;
    output = template;
}

@header {
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

@rulecatch {
catch (RecognitionException re)
{
    re.printStackTrace();
    reportError(re);
    throw re;
}
}

statement
    :   ^(STATEMENT query_expression o+=order_by?) -> statement(query={$query_expression.st}, order_by={$o});

order_by:   ^(ORDER (s+=sort_specification)+) -> order_by(columns={$s});

query_expression
    :   ^(op=UNION_ALL v1=query_expression v2=query_expression)
            ->set_op_union_all( value1={$v1.st}, value2={$v2.st})
    |   ^(op=EXCEPT_ALL v1=query_expression v2=query_expression)
            ->set_op_except_all( value1={$v1.st}, value2={$v2.st})
    |   ^((op=UNION | op=EXCEPT | op=INTERSECT) v1=query_expression v2=query_expression) 
            -> set_op(op={$op}, value1={$v1.st}, value2={$v2.st})
    |   query -> {$query.st}
    ;
    
query   :   ^(QUERY 
            ^(SELECT_LIST (sq='DISTINCT' | sq='ALL')? select_list) 
            ^(FROM_LIST (t+=table)+) 
            (^(WHERE wh+=search_condition))? 
            (^(GROUP_BY c+=column_name+))? 
            (^(HAVING hav+=search_condition))?
        )
            -> query(select_list={$select_list.st}, from_list={$t}, where={$wh}, group_by={$c}, having={$hav}, 
            set_quantifier={$sq})
    ;

select_list
    :   ^(COLUMN s='*')    -> emitstr(str={$s})
    |   (c+=column_def)+   -> column_list(columns={$c})
    ;
column_def
    :   ^(COLUMN value_expression)    -> {$value_expression.st}
    |   ^(COLUMN value_expression ID) -> expr_as(expression={$value_expression.st}, alias={$ID})
    ;
sort_specification
    :   ^((spec=ASC | spec=DESC) column_name)  -> emitstr2(str2={$spec}, str1={$column_name.st})
    |   ^((spec=ASC | spec=DESC) INT)          -> emitstr2(str2={$spec}, str1={$INT});
    
value_expression
    :   string_value_expression  -> {$string_value_expression.st}
    |   numeric_value_expression -> {$numeric_value_expression.st}
    ;
numeric_value_expression
    :   ^((op='+' | op='-' ) v1=numeric_value_expression v2=numeric_value_expression) 
            -> bin_expr_paren(op={$op}, value1={$v1.st}, value2={$v2.st})
    |   ^((op='/' | op='*') v1=numeric_value_expression v2=numeric_value_expression) 
            -> bin_expr(op={$op}, value1={$v1.st}, value2={$v2.st})
    |   numeric_primary -> {$numeric_primary.st}
    ;
    
numeric_primary
    :   ^(op=('+' | '-') value_expression_primary) -> unary_expr(op={$op}, value={$value_expression_primary.st})
    |   value_expression_primary -> {$value_expression_primary.st}; 

value_expression_primary 
    :   function -> {$function.st}
    |   column_name -> {$column_name.st}
    |   literal -> {$literal.st}
    |   query_expression -> paren(str={$query_expression.st})
    ;
    
function
    :   ^(FUNCTION ID (param+=value_expression)+) -> function(name={$ID}, param={$param})
    |   ^(FUNCTION ID p='*') -> function(name={$ID}, param={$p})
    ;
    
literal :   INT -> emitstr(str={$INT.text})
    |   FLOAT  -> emitstr(str={$FLOAT.text})
    |   NUMERIC -> emitstr(str={$NUMERIC.text})
    |   STRING -> emitstr(str={$STRING.text})
    |   datetime -> {$datetime.st}
    |   interval -> {$interval.st}
    |   ( str='TRUE' | str='FALSE' | str='NULL' ) -> emitstr(str={$str})
    ;   

string_value_expression
    :   ^(op='||' v1=string_primary v2=string_primary) -> bin_expr(op={$op}, value1={$v1.st}, value2={$v2.st});
string_primary
    :   column_name -> {$column_name.st}
    |   STRING -> emitstr(str={$STRING.text});
    
datetime
    :   ^((op='DATE' | op='TIME' | op='TIMESTAMP') STRING) -> unary_expr(op={$op}, value={$STRING})
    ;
interval:   ^('INTERVAL' STRING (f='YEAR' | f='MONTH' | f='DAY' | f='HOUR' | f='MINUTE' | f='SECOND'))
            -> interval(value={$STRING}, field={$f});
    
table   :   ^(RIGHT_OUTER_JOIN t1=table t2=table search_condition) -> join(type={"RIGHT"}, table1={$t1.st}, table2={$t2.st}, condition={$search_condition.st})
    |   ^(LEFT_OUTER_JOIN  t1=table t2=table search_condition) -> join(type={"LEFT"}, table1={$t1.st}, table2={$t2.st}, condition={$search_condition.st})
    |   ^(FULL_OUTER_JOIN  t1=table t2=table search_condition) -> join(type={"FULL"}, table1={$t1.st}, table2={$t2.st}, condition={$search_condition.st})
    |   ^(JOIN             t1=table t2=table search_condition) -> join(type={""}, table1={$t1.st}, table2={$t2.st}, condition={$search_condition.st})
    |   non_join_table -> {$non_join_table.st}
    ;

non_join_table
    :   ^(RELATION table_name) -> {$table_name.st} 
    |   ^(RELATION table_name ID) -> expr_as(expression={$table_name.st}, alias={$ID})
    |   ^(RELATION table_function) -> {$table_function.st}
    |   ^(RELATION table_function ID) -> expr_as(expression={$table_function.st}, alias={$ID})
    |   ^(RELATION query_expression ID) -> subquery(query={$query_expression.st}, alias={$ID})
    ;
    
table_function
    :   ^(FUNCTION ID (param+=relation)+ (param+=literal)*) -> function(name={$ID}, param={$param})
    ;
    
relation:   ^(RELATION table_name) -> {$table_name.st}
    |   ^(RELATION table_function) -> {$table_function.st}
    |   ^(RELATION query) -> {$query.st}
    ;
    
search_condition
    :   ^(op='OR' s1=search_condition s2=search_condition) -> bin_expr_paren(op={$op}, value1={$s1.st}, value2={$s2.st})
    |   ^(op='AND' s1=search_condition s2=search_condition) -> bin_expr(op={$op}, value1={$s1.st}, value2={$s2.st})
    |   boolean_term -> {$boolean_term.st}
    ;
boolean_term
    :   predicate -> {$predicate.st}
    |   ^(op=NOT predicate)  -> unary_expr(op={$op.text}, value={$predicate.st})
    ;
predicate
    :   comparison_predicate -> {$comparison_predicate.st}
    |   like_predicate       -> {$like_predicate.st}
    |   in_predicate         -> {$in_predicate.st}
    |   null_predicate       -> {$null_predicate.st}
    |   exists_predicate     -> {$exists_predicate.st}
    |   between_predicate    -> {$between_predicate.st} 
    ;

null_predicate
    :   ^(IS_NULL row_value) -> unary_expr(op={$row_value.st}, value={"IS NULL"});
in_predicate
    :   ^(op='IN' row_value in_predicate_tail) ->  bin_expr(op={$op}, value1={$row_value.st}, value2={$in_predicate_tail.st});
in_predicate_tail
    :   query_expression -> paren(str={$query_expression.st})
    |   ^(SET (e+=value_expression)+) -> set(expr={$e});
between_predicate
    :   ^('BETWEEN' val=row_value gt=row_value lt=row_value) -> between(value={$val.st}, greater_than={$gt.st}, less_than={$lt.st});
exists_predicate
    :   ^(name='EXISTS' query_expression) -> function(name={$name}, param={$query_expression.st});
comparison_predicate
    :   ^((op='=' | op='<>' | op='!=' | op='<' | op='>' | op='>=' | op='<=') v1=row_value v2=row_value) 
            -> bin_expr(op={$op}, value1={$v1.st}, value2={$v2.st});
like_predicate
    :   ^(op='LIKE' v1=row_value v2=row_value) -> 
            bin_expr(op={$op}, value1={$v1.st}, value2={$v2.st})
    ;
            

row_value
    :   value_expression  -> {$value_expression.st}
    |   str='NULL'        -> emitstr(str={$str})
    |   str='DEFAULT'     -> emitstr(str={$str})
    |   query_expression -> {$query_expression.st};

//row_value_with_all
//  :       ^((op='ALL' |op='SOME' | op='ANY') row_value) -> 
//          unary_expr(op={$op}, value={$row_value.st})
//  |       row_value -> {$row_value.st};

    
table_name
    :   ID -> emitstr(str={$ID});
column_name
    :   ^(TABLECOLUMN ID)        -> emitstr(str={$ID})
    |   ^(TABLECOLUMN t=ID c=ID) -> column_name(table={$t}, name={$c})
    ;
