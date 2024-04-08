import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

//在类上标记注解
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testRole(){
        for (int i=0;i<200;i++){
            roleMapper.insert(new Role(null,"role"+i));
        }
    }
    @Test
    public  void test3(){
        for (int i=0;i<50;i++){
            adminMapper.insert(new Admin(null,"loginAcct"+i,"userPswd"+i,"UserName"+i,"email"+i,null));
        }
    }

    @Test
    public  void test2(){
        Admin admin = new  Admin(null,"jack","1212","杰克","jack@qq.com",null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testAdminMapperAutowired() {
       Admin admin = new Admin(null,"tom","123123","汤姆","tom@qq.com",null);
       int count =adminMapper.insert(admin);
        System.out.println("受影响的行数="+count);
    }
        @Test
    public  void  test() throws Exception{
       Connection connection = dataSource.getConnection();
            System.out.println(connection);
        }

        @Test
    public  void  test1(){
            Logger logger = LoggerFactory.getLogger(CrowdTest.class);
            //根据不同级别打印日志
            logger.debug("hello debug");
            logger.info("hello info");
        }
}
