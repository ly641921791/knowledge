
jdbcTemplate查询数据列表

```java
List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from material");
```

批量更新数据

```java
List<Object[]> args = new ArrayList<>();
args.add(new Object[]{1,2,3});
jdbcTemplate.batchUpdate("UPDATE material set height = ? , width = ? , `size` = ? , playLength = ? ,ext_name = ? where id = ?", args);
```
