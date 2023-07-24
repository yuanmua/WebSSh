package com.star.webssh.filtter;

import com.alibaba.fastjson.JSON;
import com.star.webssh.common.BaseContext;
import com.star.webssh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mr.Wang
 * @create 2023-06-08-19:14
 */
@WebFilter(urlPatterns = "/*",filterName = "loginCheckFilter")
@Slf4j
public class LoginCheckFilter implements Filter {
    //用于判断url路径中**通配符的比较，如backend/**与backend/index.html
   public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;

        //设置直接放行的路径数组
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };//user为移动端用户

        //1、获取本次请求的url
        String url = request.getRequestURI();
        log.info("拦截到请求：{}",url);
        //2、判断本次请求是否需要被处理
        boolean isTrue = check(urls, url);

        //3、如果不需要被处理直接放行
        if(isTrue){
            log.info("本次请求{}不需要被处理",url);
            filterChain.doFilter(request,response);
            return;
        }
        //4.1、判断登陆状态，如果已登录，则直接放行
        //request.getSession().getAttribute("employee")得到的是employee对应的value值
        if( request.getSession().getAttribute("employee")!=null){//如果Session中存在employee的信息说明之前已经登陆过了直接放行
            log.info("用户已经登录，用户id为{}",request.getSession().getAttribute("employee"));


            //将操作人的id放入线程
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;

        }

        //4.2移动端用户、判断登陆状态，如果已登录，则直接放行
        //request.getSession().getAttribute("employee")得到的是employee对应的value值
        if( request.getSession().getAttribute("user")!=null){//如果Session中存在employee的信息说明之前已经登陆过了直接放行
            log.info("用户已经登录，用户id为{}",request.getSession().getAttribute("user"));


            //将操作人的id放入线程
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;

        }
        log.info("用户未登录");
        //5、如果未登录返回未登录结果,返回是以R对象的json格式的数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestUrl
     * @return
     */
    public boolean check(String[] urls,String requestUrl){
        for(String url:urls){
            boolean match = PATH_MATCHER.match(url, requestUrl);//通过AntPathMatcher来判断路径，第一个元素为数组中的路径
            if(match){
               return true;
            }
        }
        return false;
    }
}
