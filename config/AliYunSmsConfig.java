package cn.springlogic.communicate.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kinginblue on 2017/5/24.
 */
@Configuration
@EnableConfigurationProperties({AliYunSmsProperties.class})
public class AliYunSmsConfig {

}
