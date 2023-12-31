package com.star.webssh.filtter;

import com.alibaba.fastjson.JSONObject;

import com.star.webssh.common.BaseContext;
import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mr.Wang
 * @create 2023-05-24-11:25
 */
@WebFilter(urlPatterns = {"/cmd/*","/employee/logout","/employee/login","/system/*"})
@Slf4j
public class loginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将request,response转换
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;

        //获取url
         String url = httpServletRequest.getRequestURL().toString();
        if(url.contains("/login")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //从cookie中拿到token

        //===============================
        Cookie[] cookies = httpServletRequest.getCookies();


//        if (cookies == null || cookies.length == 0) {
//            R error = R.error("NOT_LOGIN");
//            //将Result转为json将其相应回去
//            String jsonString = JSONObject.toJSONString(error);
//            servletResponse.getWriter().write(jsonString);
//            return ;
//        }
        String jwt=null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (name.equals("Admin-Token")) {
                jwt = cookie.getValue();
                break;
            }
        }

        //====================================================
//         jwt = httpServletRequest.getHeader("Admin-Token");


        if(!StringUtils.hasLength(jwt)){
            log.info("密钥为：{}",jwt);
            R error = R.error("NOT_LOGIN");
            //将Result转为json将其相应回去
            String jsonString = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(jsonString);
            return;
        }
        //有密钥，看是否能解析成功
        try {
            Claims claims = JWTUtils.parseJWT(jwt);
            //将当前用户的id存入工具类中
            Object id = claims.get("id");
            String s_id = String.valueOf(id);
            Long user_id = Long.valueOf(s_id);
            BaseContext.setCurrentId(user_id);

        } catch (Exception e) {
            e.printStackTrace();
            R error = R.error("NOT_LOGIN");
            //将Result转为json将其相应回去
            String jsonString = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(jsonString);
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
