import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StringTest {

    @Test
    public  void  testMd5(){
        String source ="123123";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encoded = bCryptPasswordEncoder.encode(source);
        System.out.println(encoded);
    }
    @Test
    public void  test(){
        method01();
    }
    public void method01(){
        System.out.println("111111111开始");
        method02();
        System.out.println("111111结束");
    }
    public void method02(){
        System.out.println("22222开始");
method03();
        System.out.println("2222222222结束");
    }
    public void method03(){
        System.out.println("33333333");
    }
}
