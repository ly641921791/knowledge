[TOC]

# 各种情况使用示例

本文介绍使用MyBatis的各种姿势

## 主键自增及获取





```java
public interface UserMapper {
	@Options(userGenerationKey=true,keyProperty="id")
	@Insert("insert into users(name, age) values(#{name}, #{age})")
	public int add(User user);
}
```


Oracle
	<sql id='TABLE_NAME'>
		TEST_USER
	</sql>

	<sql id='TABLE_SEQUENCE'>			<!-- 关于sql标签，查看动态sql -->
		SEQ_TEST_USER_ID.nextval
	</sql>

	<insert id="insert" parameterType="User">
		insert into <include refid="TABLE_NAME" /> (ID,NAME,AGE)
		values ( <include refid="TABLE_SEQUENCE" /> ,#{name}, #{age} )
	</insert>

	User user = new User();
	user.setName("test");
	user.setAge(24);
	userMapper.insert(user);	//不会将插入后的user保存回来


Mysql
	设置表的主键为自增，所以主键的自增交由Mysql来管理。
	<sql id='TABLE_NAME'>
		TEST_USER
	</sql>

	<insert id="insert" parameterType="User">
		insert into <include refid="TABLE_NAME" /> (NAME,AGE)
		values (#{name}, #{age} )
	</insert>


以上情况插入后都不能获得插入的主键，若需要获取插入的主键，需要使用mybatis提供的<selectKey />来单独配置针对自增逐渐的处理。

Oracle
	<sql id='TABLE_NAME'>
		TEST_USER
	</sql>

	<sql id='TABLE_SEQUENCE'>
		SEQ_TEST_USER_ID.nextval
	</sql>

	<insert id="insert" parameterType="User">
		<selectKey keyProperty="id" resultType="int" order="BEFORE">
			select <include refid="TABLE_SEQUENCE" /> from dual
		</selectKey>
		insert into <include refid="TABLE_NAME" /> (ID,NAME,AGE)
		values ( #{id}, #{name}, #{age} )
	</insert>

	使用<selectKey />后，在执行插入语句2之前，会先执行语句1以获取当前的ID值，然后mybatis使用反射调用User对象的setId方法，将 语句1 查询出的值保存在User对象中，然后才执行 语句2 这样就保证了执行完插入后
	User user = new User();
	user.setName("test");
	user.setAge(24);
	userMapper.insert(user);	//会将插入后的user保存回来

Mysql
	针对于Mysql这种自己维护主键的数据库，可以直接使用以下配置在插入后获取插入主键，
	<sql id='TABLE_NAME'>
		TEST_USER
	</sql>

	<insert id="insert" useGeneratedKeys="true" keyProperty="id" parameterType="User">
		insert into <include refid="TABLE_NAME" /> ( NAME, AGE )
		values ( #{name}, #{age} )
	</insert>

	Mysql的自增主键可以通过SQL语句select LAST_INSERT_ID();来获取的

	<sql id='TABLE_NAME'>TEST_USER</sql>

	<insert id="insert" parameterType="User">
		<selectKey keyProperty="id" resultType="int" order="BEFORE">
			SELECT LAST_INSERT_ID()
		</selectKey>
		insert into <include refid="TABLE_NAME" /> (ID,NAME,AGE)
		values ( #{id}, #{name}, #{age} )
	</insert>



## 关联查询




一对一关联查询：根据班级id查询班级对象，班级对象内包含一个老师对象
	方法1：
		<select id="getClass" parameterType="int" resultMap="ClassResultMap">
			select * from class c,users u where c.teacherId=u.id and c.id=#{id}			--and可以使用join on语句代替
		</select>
		<resultMap id="ClassResultMap" type="Classes">
			<id property="id" column="id"/>
			<association property="teacher" javaType="User">							--内部字段映射貌似可以省略，javaType可以换resultMap使类型可重用
				<id property="id" column="id"/>
				<result property="name" column="name"/>
			</association>
		</resultMap>
	方法2：查询多次
		<select id="getClass1" parameterType="int" resultMap="ClassResultMap">
			select * from class id=#{id}
		</select>
		<resultMap id="ClassResultMap" type="Classes">
			<id property="id" column="id"/>
			<association property="teacher" column="teacherId" javatType="User" select="getTeacher"/>
		</resultMap>
		<select id="getTeacher" parameterType="int" resultType="User">
			select * from users where id=#{id}
		</select>


一对多关联查询：根据班级ID查询班级对象，班级中有学生集合
	方法1：
		<select id="getClass2" parameterType="int" resultType="ClassResultMap2">
			select * from class c,users u where c.id=users.classId and c.id=#{id}
		</select>
		<resultMap id="ClassResultMap2" type="Classes">
			<id property="id" column="id"/>
			<collection property="student" ofType="User">								--ofType表示集合中对象的类型
				<id property="id" column="id"/>
				<result property="name" column="name"/>
			</collection>
		</resultMap>
	方法2：查询多次
		<select id="getClass3" parameterType="int" resultType="ClassResultMap3">
			select * from class where id=#{id}
		</select>
		<resultMap id="ClassResultMap3" type="Classes">
			<id property="id" column="id"/>
			<collection property="student" column="id" ofType="User" select="getStudent"/>
		</resultMap>
		<select id="getStudent" parameterType="int" resultType="User">
			select * from users where classId =#{id}
		</select>