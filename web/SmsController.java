package cn.springlogic.communicate.web;

import cn.springlogic.communicate.jpa.entity.Verification;
import cn.springlogic.communicate.service.AliYunSmsService;
import cn.springlogic.communicate.service.VerificationService;
import cn.springlogic.communicate.util.CommUtil;
import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fitcooker.app.BussinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/4/19.
 */
@RequestMapping("/api/communicate:verification")
@Controller
@ControllerAdvice(basePackageClasses = RepositoryRestExceptionHandler.class)
public class SmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    @Qualifier(value = "AliYunSmsServiceV2")
    private AliYunSmsService aliYunSmsService;

    @Autowired
    private VerificationService verificationService;

    /*
     *  手机号码 target =>目标数据
     *  方式type 1 =>手机号码方式
     */
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public ResponseEntity<Verification> code(@RequestBody Verification verification) throws BussinessException {
        try {
            String code = CommUtil.genRandomNum(6);

            Map<String, String> params = new HashMap<>();

            params.put("code", code);

            /*频繁度检查*/
            boolean frequency = verificationService.frequencyVerify(verification.getTarget(), verification.getType());

            if (!frequency) {
                aliYunSmsService.send(verification.getTarget(), params);
            } else {
                LOGGER.error("获取验证码太频繁!");
                throw new BussinessException("发布失败");
            }

            verification.setCode(code);

            if (Verification.TYPE_SMS.equals(verification.getType())) {
                verification.setType(Verification.TYPE_SMS);
            }

            /*发送成功后,同时保存在数据库*/
            verificationService.save(verification);

            return ResponseEntity.ok(verification);
        } catch (ClientException e) {
            LOGGER.error("发送验证码失败!",e);
            e.printStackTrace();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        LOGGER.error("发送验证码失败!");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
