# Database

## Introduction

Homebrew SQL database system, built from the ground up. This system uses no external libraries to set up the database or parse the SQL queries.

## Usage

```bash
$ java DBServer
$ java DBClient 
```

Once the client is running you can enter SQL queries on the command line. Example queries include:

```
CREATE DATABASE markbook;
[OK]

USE markbook;
[OK]

CREATE TABLE marks (name, mark, pass);
[OK]
```

When data is added to a table other queries include:

```
SELECT * FROM marks;
[OK]
id	name	mark	pass
1	Steve	65	true
2	Dave	55	true
3	Bob	  35	false
4	Clive	20	false

UPDATE marks SET mark = 38 WHERE name == 'Clive';
[OK]

SELECT * FROM marks WHERE name == 'Clive';
[OK]
id	name	mark	pass
4	  Clive	 38	  false

DELETE FROM marks WHERE name == 'Dave';
[OK]
```

Also capable of handling JOIN queries and multiple WHERE conditions.

## Syntax

```
<Command>        ::=  <CommandType>;

<CommandType>    ::=  <Use> | <Create> | <Drop> | <Alter> | <Insert> |
                      <Select> | <Update> | <Delete> | <Join>

<Use>            ::=  USE <DatabaseName>

<Create>         ::=  <CreateDatabase> | <CreateTable>

<CreateDatabase> ::=  CREATE DATABASE <DatabaseName>

<CreateTable>    ::=  CREATE TABLE <TableName> | CREATE TABLE <TableName> (<AttributeList>)

<Drop>           ::=  DROP <Structure> <StructureName>

<Structure>      ::=  DATABASE | TABLE

<Alter>          ::=  ALTER TABLE <TableName> <AlterationType> <AttributeName>

<Insert>         ::=  INSERT INTO <TableName> VALUES (<ValueList>)

<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
                      SELECT <WildAttribList> FROM <TableName> WHERE <Condition> 

<Update>         ::=  UPDATE <TableName> SET <NameValueList> WHERE <Condition> 

<Delete>         ::=  DELETE FROM <TableName> WHERE <Condition>

<Join>           ::=  JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>

<NameValueList>  ::=  <NameValuePair> | <NameValuePair>,<NameValueList>

<NameValuePair>  ::=  <AttributeName>=<Value>

<AlterationType> ::=  ADD | DROP

<ValueList>      ::=  <Value> | <Value>,<ValueList>

<Value>          ::=  '<StringLiteral>' | <BooleanLiteral> | <FloatLiteral> | <IntegerLiteral>

<BooleanLiteral> ::=  true | false

<WildAttribList> ::=  <AttributeList> | *

<AttributeList>  ::=  <AttributeName> | <AttributeName>,<AttributeList>

<Condition>      ::=  (<Condition>) AND (<Condition>)  |
                      (<Condition>) OR (<Condition>)   |
                      <AttributeName><Operator><Value>

<Operator>       ::=   ==   |   >   |   <   |   >=   |   <=   |   !=   |   LIKE


Notes:
If the user doesn’t know a value or doesn’t wish to provide it, they must pass an empty String (i.e. there is no null)
StringLiteral, TableName, ColumnName, DatabaseName are all purely alphanumeric sequences (i.e. a-z, A-Z, 0-9)
StringLiterals may optionally include a range of punctuation EXCEPT single quotes and tabs (and definitely not exotic UTF characters !)
FloatLiterals are in standard notation (i.e. not scientific, not hexidecimal)
The range of numbers (max and min) match those supported by the standard Java int and float types
```

## Reference

[Briefing on DB assignment](https://github.com/drslock/JAVA2020/tree/main/Weekly%20Workbooks/06%20Briefing%20on%20DB%20assignment)

## Disclaimer

This work was submitted as coursework for the COMSM0086 Object Oriented Programming with Java module at the University of Bristol. Please note that no student can use this work without my permission or attempt to pass this work off as their own.

