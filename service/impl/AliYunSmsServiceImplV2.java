package cn.springlogic.communicate.service.impl;

import cn.springlogic.communicate.config.AliYunSmsProperties;
import cn.springlogic.communicate.service.AliYunSmsService;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by kinginblue on 2017/1/10.
 */
@Component
@Qualifier(value = "AliYunSmsServiceV2")
public class AliYunSmsServiceImplV2 implements AliYunSmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliYunSmsServiceImplV2.class);

    @Autowired
    private AliYunSmsProperties aliYunSmsProperties;

    @Override
    public void send(String recNum, Map<String, String> params) throws ClientException, JsonProcessingException {

        /**
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount(aliYunSmsProperties.getAccessKey(), aliYunSmsProperties.getAccessSecret(), aliYunSmsProperties.getEndpoint());
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(aliYunSmsProperties.getTopic());

        /**
         * Step 2. 设置SMS消息体（必须）
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");

        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(aliYunSmsProperties.getSignName());
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(aliYunSmsProperties.getSmsTplCode());

        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        // smsReceiverParams.setParam("$YourSMSTemplateParamKey1", "$value1");
        // smsReceiverParams.setParam("$YourSMSTemplateParamKey2", "$value2");
        params.entrySet().stream().forEach(data -> smsReceiverParams.setParam(data.getKey(), data.getValue()));

        // 3.4 增加接收短信的号码
        // batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber1", smsReceiverParams);
        // batchSmsAttributes.addSmsReceiver("$YourReceiverPhoneNumber2", smsReceiverParams);
        Arrays.asList(recNum.split(",")).stream().filter(StringUtils::hasText).forEach(data -> batchSmsAttributes.addSmsReceiver(data, smsReceiverParams));

        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);

            LOGGER.debug("MessageId: " + ret.getMessageId());
            LOGGER.debug("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            LOGGER.debug(se.getErrorCode() + se.getRequestId());
            LOGGER.debug(se.getMessage());

            LOGGER.error(se.getMessage(), se);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        client.close();
    }
}
