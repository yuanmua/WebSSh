package com.star.webssh.config;


import com.star.webssh.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Mr.Wang
 * @create 2023-06-11-15:12
 */
@ComponentScan
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        log.info("扩展的消息转换器......");
        //创建消息转换器对象
        MappingJackson2CborHttpMessageConverter messageConverter = new MappingJackson2CborHttpMessageConverter();
        //设置对象转换器，底层是使用Jackson将java转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换集合
        converters.add(0,messageConverter);//index设置为0,即将自己设置的该转换器的优先级改为最高
        //super.extendMessageConverters(converters);//springboot默认的消息转换器
    }
}
