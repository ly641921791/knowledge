Char
-

char c = (char) 12288;

该字符属于空白字符（全角空格），不可被 trim() 方法去除，不匹配正则表达式 \s 

可以通过 org.apache.commons.lang3.StringUtils.deleteWhitespace(String) 方法去除