package cn.springlogic.communicate.service.impl;

import cn.springlogic.communicate.jpa.entity.Verification;
import cn.springlogic.communicate.jpa.repository.VerificationRepository;
import cn.springlogic.communicate.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by admin on 2017/4/19.
 */
@Component
public class VerificationServiceImpl implements VerificationService {

  @Autowired
  private VerificationRepository verificationRepository;

    /**
     * 验证码频繁度检查
     * @param target
     * @param type
     * @return
     */

    @Override
    public boolean frequencyVerify(String target, String type) {

        /*有可能为空,因为没注册过*/
        Verification temp= verificationRepository.findFirst1BytargetAndTypeOrderByCreateTimeDesc(target, type);
        if(temp==null){
            return false;
        }

        Date date=new Date();

        /*
        判断当前时间与数据库查询出来的验证码添加时间
        相减,如果大于1分钟,则表示 不频繁.返回false
         */
        if( date.getTime()- temp.getCreateTime().getTime() > 1000 * 60) {
            return  false;
        }else {
          return true;
        }

    }


    @Override
    public boolean timeVerify(String target, String type) {
         /*有可能为空,因为没注册过*/
        Verification temp= verificationRepository.findFirst1BytargetAndTypeOrderByCreateTimeDesc(target, type);
        if(temp==null){
            return true;
        }

        Date date=new Date();

        /*
        判断当前时间与数据库查询出来的验证码添加时间
        相减,如果大于2分钟,则表示 不可用 .返回false
         */
        if( date.getTime()- temp.getCreateTime().getTime() > 2000 * 60) {
            return  false;
        }else {
            return true;
        }
    }

    @Override
    public void save(Verification verification) {
     verificationRepository.save(verification);
    }

    @Override
    public Verification findBytargetAndtypeAndCode(String target, String type, String code) {
        return verificationRepository.findFirst1BytargetAndTypeAndCodeOrderByCreateTimeDesc(target,type,code);
    }
}
