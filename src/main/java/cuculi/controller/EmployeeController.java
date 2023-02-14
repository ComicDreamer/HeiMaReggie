package cuculi.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cuculi.common.R;
import cuculi.pojo.Employee;
import cuculi.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    public EmployeeService employeeService;

    /*
    员工登录
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){
        /*
        1.获取输入的密码
        2.根据用户名获取数据库中的员工对象，如果查询为空则失败
        3.对输入的密码进行md5加密，并与数据库中的比对，如果不匹配则失败
        4.查询员工状态，如果封禁，则失败
        5.存入session，发送成功响应，封装查询出来的员工数据
         */
        //1
        String password = employee.getPassword();

        //2
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp == null){
            return R.error("用户名不存在");
        }

        //3
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(emp.getPassword())){
            return R.error("用户名和密码不匹配");
        }

        //4
        if (!(emp.getStatus() == 1)){
            return R.error("账户封禁中");
        }

        //5
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /*
    员工退出
     */
    @RequestMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /*
    添加员工
    1.获取 employee 对象
    2.设置初始密码为 123456 的md5格式
    3.设置创建时间和创建者
    4.存入数据库
    5.返回添加成功
     */
    @RequestMapping
    public R<String> addEmployee(HttpServletRequest request ,@RequestBody Employee employee){
        //设置初始密码
        String initialPassword = "123456";
        employee.setPassword(DigestUtils.md5DigestAsHex(initialPassword.getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long createId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(createId);
//        employee.setUpdateUser(createId);

        employeeService.save(employee);
        return R.success("添加员工成功");
    }

    /*
    分页查询
     */
    @GetMapping("/page")
    public R<Page> pageQuery(int page, int pageSize, String name){
        log.info("page:{}, pageSize:{}, name:{}", page, pageSize, name);
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /*
    更新员工信息
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(id);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    /*
    查询员工信息
     */
    @RequestMapping("/{id}")
    public R<Employee> select(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("查询失败");
    }
}
