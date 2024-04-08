package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.atguigu.crowd.constant.CrowdConstant.ATTR_NAME_LOGIN_ADMIN;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("admin/update.html")
    public String update(Admin admin,@RequestParam("pageNum") Integer pageNum,@RequestParam("keyword") String keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    @RequestMapping("admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             ModelMap modelMap) {
// 1.根据 id（主键）查询待更新的 Admin 对象
        Admin admin = adminService.getAdminById(adminId);
// 2.将 Admin 对象存入模型
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }



    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public  String remover(@PathVariable("adminId") Integer adminId,
                           @PathVariable("pageNum") Integer pageNum,
                           @PathVariable("keyword") String keyword){

        //执行删除
        adminService.remove(adminId);
        //页面跳转 回到分页页面
        // 方案1.直接转发到页面会无法显示分页数据
         //return "admin-page";
        // 方案2.转发到/admin/get/page.html 一旦刷新页面会重复执行删除
        //return "forward:/admin/get/page.html";
        // 方案3.重定向到/admin/get/page.html 同时为了保持原本所在的页面和查询关键词再附加pageNum和keyword俩个请求参数
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            // 注意：页面上有可能不提供关键词，要进行适配
            // 在@RequestParam注解中设置defaultValue属性为空字符串表示浏览器不提供关键词时，keyword 变量赋值为空字符串
            @RequestParam(value="keyword", defaultValue="") String keyword,
            // 浏览器未提供 pageNum 时，默认前往第一页
            @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
            // 浏览器未提供 pageSize 时，默认每页显示 5 条记录
            @RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
            ModelMap modelMap
    ){
        // 查询得到分页数据 调用Service方法获取对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        // 将分页数据存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

        return "admin-page";
    }

    @RequestMapping("/admin/to/logout.html")
    public String doLogout(HttpSession session){
        // 强制Session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session){
        // 调用Service方法执行登陆检查
        // 这个方法如果能够返回Admin对象 说明登陆成功 如果账号密码不正确 则会抛出异常
        Admin admin =adminService.getAdminByLoginAcct(loginAcct,userPswd);
        // 将登陆成功的admin对象返回到Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        return "redirect:/admin/to/main/page.html";
    }

    @PreFilter(value="filterObject%2==0")
    @ResponseBody
    @RequestMapping("/admin/test/pre/filter")
    public ResultEntity<List<Integer>> saveList(@RequestBody List<Integer> valueList) {
        return ResultEntity.successWithData(valueList);
    }

}
