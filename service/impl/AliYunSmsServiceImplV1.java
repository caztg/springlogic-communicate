package cn.springlogic.communicate.service.impl;

import cn.springlogic.communicate.config.AliYunSmsProperties;
import cn.springlogic.communicate.service.AliYunSmsService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by admin on 2017/4/19.
 */
/*
@Component
@Qualifier(value = "AliYunSmsServiceV1")
*/
public class AliYunSmsServiceImplV1 implements AliYunSmsService {

    @Autowired
    private AliYunSmsProperties aliYunSmsProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void send( String recNum, Map<String, String> params) throws ClientException, JsonProcessingException {

        String paramsJsonStr = objectMapper.writeValueAsString(params);



        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliYunSmsProperties.getAccessKey(), aliYunSmsProperties.getAccessSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendSmsRequest request = new SingleSendSmsRequest();

        request.setSignName(aliYunSmsProperties.getSignName());// 控制台创建的签名名称
        request.setTemplateCode(aliYunSmsProperties.getSmsTplCode());// 控制台创建的模板CODE
        request.setParamString(paramsJsonStr);// 短信模板中的变量Json形式
        request.setRecNum(recNum);// 接收号码
        SingleSendSmsResponse httpResponse = client.getAcsResponse(request);




    }
}
