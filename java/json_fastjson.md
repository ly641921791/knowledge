fastjson
-

通过JsonPath获得数组长度

JSONObject jsonObject = JSON.parseObject("json");

// 方法1
JSONPath.eval(jsonObject,"$.xxx.size()");

// 方法2
JSONPath.size(jsonObject,"$.xxx");
