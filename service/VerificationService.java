package cn.springlogic.communicate.service;

import cn.springlogic.communicate.jpa.entity.Verification;

/**
 * Created by admin on 2017/4/19.
 */
public interface VerificationService {

    boolean frequencyVerify(String target,String type);

    void save(Verification verification);

    Verification findBytargetAndtypeAndCode(String target,String type,String code);

    boolean timeVerify(String target,String type);

}
