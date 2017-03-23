grammar Jump;

ESC                 : [ \t\n\r]+ -> skip;
LINE_COMMENT        : '#' ~[\r\n]* -> channel(HIDDEN);
STRING              : '"' (~[\\"] | '\\' [\\"])* '"';
WORD                : ~[ \t\r\n"*)(=,]+;
EQ                  : '=';
OPENPAREN           : '(';
CLOSEPAREN          : ')';

statement       : command  (command )*                                                          #PrimaryStatement
                ;

command         : 'sql' OPENPAREN CLOSEPAREN '{'  STRING (',' STRING)*  '}'                     #SqlStatement
                | 'insert' OPENPAREN WORD ',' WORD (',' WORD)? CLOSEPAREN '{' input '}'         #InsertStatement
                | 'rollback' OPENPAREN CLOSEPAREN                                               #RollbackStatement
                | WORD '=' WORD                                                                 #VariableAssignment
                ;

input           : field_config (',' field_config)*                                              #FieldConfigList
                ;

field_config    : WORD EQ WORD OPENPAREN param_list CLOSEPAREN                                  #FieldConfig
                | WORD EQ WORD OPENPAREN CLOSEPAREN                                             #EmptyFieldConfig
                ;

param_list      : item (',' item)*                                                              #ParamList
                ;

item            : (STRING | WORD)                                                               #ParamItem
                ;
