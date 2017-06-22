package cn.springlogic.communicate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by admin on 2017/4/19.
 */
@Data
@ConfigurationProperties(prefix = "sms.aliyun")
public class AliYunSmsProperties {

    /** 控制台创建的签名名称 **/
    private String signName;

    private String accessKey;

    private String accessSecret;

    private String smsTplCode;

    private String endpoint;

    private String topic;

}
