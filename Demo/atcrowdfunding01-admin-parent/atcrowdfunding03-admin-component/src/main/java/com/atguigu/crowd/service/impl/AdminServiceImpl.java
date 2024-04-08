package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPswd = admin.getUserPswd();
      //  userPswd = CrowdUtil.md5(userPswd);

        userPswd=passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        // 2.生成创建时间
        // 生成当前系统时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);

        // 执行保存，如果账户被占用会抛出异常
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("异常的全类名=" + e.getClass().getName());

            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }


    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        // 1.根据登录账号查询 Admin 对象
// ①创建 AdminExample 对象

        AdminExample adminExample = new AdminExample();
// ②创建 Criteria 对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
// ③在 Criteria 对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
// ④调用 AdminMapper 的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);
// 2.判断 Admin 对象是否为 null
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new
                    RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = list.get(0);
// 3.如果 Admin 对象为 null 则抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
// 4.如果 Admin 对象不为 null 则将数据库密码从 Admin 对象中取出
        String userPswdDB = admin.getUserPswd();
// 5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);
// 6.对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)) {
// 7.如果比较结果是不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
// 8.如果一致则返回 Admin 对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        // 1.调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        //updateByPrimaryKeySelective表示有选择的更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("异常的全类名=" + e.getClass().getName());

            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }

        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 旧数据如下：
// adminId roleId
// 1    1（要删除）
// 1    2（要删除）
// 1    3
// 1    4
// 1    5
// 新数据如下：
// adminId roleId
// 1    3（本来就有）
// 1    4（本来就有）
// 1    5（本来就有）
// 1    6（新）
// 1    7（新）
// 为了简化操作：先根据 adminId 删除旧的数据，再根据 roleIdList 保存全部新的数据
// 1.根据 adminId 删除旧的关联关系数据
        adminMapper.deleteOLdRelationship(adminId);
// 2.根据 roleIdList 和 adminId 保存新的关联关系
        if(roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> list = adminMapper.selectByExample(example);

        Admin admin =list.get(0);

        return  admin;
    }

}

