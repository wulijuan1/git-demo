package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * 扩展 User 类
 * 创建 SecurityAdmin 对象时调用构造器，传入 originalAdmin 和 authorities * 可以通过 getOriginalAdmin()方法获取原始 Admin 对象
 *
 */
public class SecurityAdmin extends User {
    private static final long serialVersionUID = 1L;
    private Admin originalAdmin;
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;

        this.originalAdmin.setUserPswd(null);
    }
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
