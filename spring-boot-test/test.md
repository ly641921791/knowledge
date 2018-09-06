引入websocket，单元测试报错问题
	原因：单元测试并没有启动tomcat服务器，但是websocket依赖服务器
	解决：修改代码
	
```java

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {WebsocketApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketApplicationTests {
    private final Logger logger = LoggerFactory.getLogger(WebsocketApplicationTests.class);
 
    @Test
    public void getGreeting() { 
    }
 

```

单元测试事物回滚问题
	默认：回滚
	若需要设置不回滚
```java

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuoteApplicationTests {

  @Autowired
  private IAreaService areaService;

  @Test
  public void contextLoads() {
  }

  @Test
  public void testUpdate(){
    Area area = new Area();
    area.setCode("001003");
    area.setName("洛阳市");
    Integer result = areaService.update(area);
    Assert.assertEquals(1, (long)result);
  }

  @Test
  @Transactional
  @Rollback
  public void testUpdate4Rollback(){
    Area area = new Area();
    area.setCode("001001");
    area.setName("郑州市123");
    Integer result = areaService.update(area);
    Assert.assertEquals(1, (long)result);
  }

}
```