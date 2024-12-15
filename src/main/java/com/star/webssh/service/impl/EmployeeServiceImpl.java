package com.star.webssh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import com.star.webssh.common.RegexUtils;
import com.star.webssh.constant.PasswordSALT;
import com.star.webssh.constant.RedisConstancts;
import com.star.webssh.dto.LoginDTO;
import com.star.webssh.mapper.EmployeeMapper;
import com.star.webssh.pojo.Employee;
import com.star.webssh.service.EmployeeService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Wang
 * @create 2023-06-05-20:47
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public R<String> sendCode(String phone) {
        //1、检验手机号
        if (RegexUtils.isPhoneInvalid(phone)){
            //2、如果手机号错误
            return R.error("手机格式错误");
        }
        //3、正常、生成验证码
        String code = RandomUtil.randomNumbers(6);
        //4、将验证码存入redis,并设置验证码两分钟后失效
        stringRedisTemplate.opsForValue().set(RedisConstancts.LOGIN_CODE_KEY+phone,code,2, TimeUnit.MINUTES);

        //5、这里只是模拟验证码发送，所以直接将验证码返回给前端
        return R.success(code);
    }

    @Override
    public R loginWithCode(LoginDTO loginDTO) {

        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();
        //1、校验手机号
        if (phone==null||RegexUtils.isPhoneInvalid(phone)){
            return R.error("手机格式错误");
        }
        //2、校验验证码
        if (code==null||RegexUtils.isCodeInvalid(code)){
            return R.error("验证码错误");
        }
        // 3、查询是否含有该用户
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Employee employee = this.getOne(queryWrapper);

        if (employee==null){
            // 4、如果没有返回提示信息
            return R.error("当前账号未注册");
        }

        // todo 5、生成jwt令牌
        //将empId作为jwt一部分
        if(employee!=null){
            Map<String,Object > claim=new HashMap<>();
            claim.put("id",employee.getId());
            String jwt = JWTUtils.createJWT(claim);
            return R.success(jwt);
        }
        else {
            return R.error("密码错误或用户名错误");
        }

        //todo 6

//        String token= UUID.randomUUID().toString(true);

        // 6、如果存在该用户，保存用户ID信息到redis中,使用login:token:token作为key
        // 7、设置刷新时间
//        stringRedisTemplate.opsForValue().set(RedisConstancts.LONG_CODE_USER_KEY+token,(String.valueOf(employee.getId()))
//                ,RedisConstancts.LONG_CODE_USER_TTL
//                , TimeUnit.MINUTES);


    }

    @Override
    public R<String> register(LoginDTO loginDTO) {
        String code = loginDTO.getCode();
        String phone = loginDTO.getPhone();

        //1、判断验证码，手机号，是否正确
        String redisCode = stringRedisTemplate.opsForValue()
                .get(RedisConstancts.LOGIN_CODE_KEY + phone);
//        if (StrUtil.isBlank(redisCode)){
//            return R.error("验证码已过期");
//        }
//        if (code==null||!code.equals(redisCode)){
//            //2、不对、返回错误
//            //直接判断验证码即可，因为在发短信的时候已经校验了手机号的正确形式，以key的形式存入，value为code
//            //若号码不对，则获取不到对应的code
//            return R.error("验证码错误");
//        }


        //3、校验此用户是否已经被注册
        //4、被注册，返回错误信息、此步在全局异常处理中已经定义重复信息
        //5、没被注册，保存用户信息
        //将dto中的信息复制到employee中保存
        Employee employee = BeanUtil.copyProperties(loginDTO, Employee.class, "code");

        //6、保存用户到数据库
        //将密码加密,md5+盐
        employee.setPassword(DigestUtils.md5DigestAsHex((employee.getPassword()+ PasswordSALT.PASSWORD_SALT).getBytes()));

        //设置用户创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //这些字段均无用，应当在数据库中删去
        this.save(employee);

        return R.success("注册成功");
    }
}
