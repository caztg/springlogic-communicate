package cn.springlogic.communicate.service;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

/**
 * 阿里云短信发送接口
 */
public interface AliYunSmsService {


    /**
     * 阿里云发送短信验证码接口
     * //@param templateCode 管理控制台中配置的审核通过的短信模板的模板CODE（状态必须是验证通过）
     *
     * @param recNum 目标手机号，多个手机号可以逗号分隔
     * @param params 短信模板中的变量；数字需要转换为字符串；个人用户每个变量长度必须小于15个字符。 例如:短信模板为：“接受短信验证码${no}”,此参数传递{“no”:”123456”}，用户将接收到[短信签名]接受短信验证码123456
     */
    void send(String recNum, Map<String, String> params) throws ClientException, JsonProcessingException;


}
