# SqlHandle
* mysql语法 sql语句拼接工具简类
* 使用场景：需要快速书写sql语句时用到，主要使用的是mysql中的sql语法
* 该版本的select语句只支持单表查询，不支持多表查询，待更新
* where 语句中关联多个字段默认使用And关键字,暂不可更改，待更新。

#常量说明:
```java
//排序操作符：顺序 "ASC","DESC"
	public static final String[] SORTS ={"ASC","DESC"};
//数据库操作符：顺序 "select","insert","update","delete"
	public static final String[] OPERATES={"select","insert","update","delete"};
```

#使用示例:
生成 select * from user
//默认使用select语句
SqlHandle sqlhandle =new SqlHandle("user");

生成查询 select field1 as f1,field2 as f2 from user where field1=value order by field1 ASC limit 0,5
```java 
SqlHandle sqlhandle =new SqlHandle("user");
sqlhandle.FIELD(field,alias).CONDITION(filed1,"=", value).ORDERBY(SqlHandle.SORTS[0], field1).LIMIT(0,5);
```
**生成插入 insert table SET field1=value1,field2=value2**
```java
SqlHandle sqlhandle =new SqlHandle(SqlHandle.OPERATES[1],table);
sqlhandle.OPERATEFILED(field1, value1).OPERATEFILED(field2, value2);
```
**生成插入 insert table values(value1,value2)**
```java
SqlHandle sqlhandle =new SqlHandle(SqlHandle.OPERATES[1],table);
sqlhandle.OPERATEFILED(value1).OPERATEFILED(value2)
```

**生成更新 update table set field1=value1 where field1=value1**
```java
SqlHandle sqlhandle =new SqlHandle(SqlHandle.OPERATES[2],table);
sqlhandle.OPERATEFILED(field1, value1);
```

生成删除 delete from table where field1=value1
```java
SqlHandle sqlhandle =new SqlHandle(SqlHandle.OPERATES[3],table);
sqlhandle.OPERATEFILED(field1, value1);
```
