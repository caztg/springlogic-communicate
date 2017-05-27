package cn.springlogic.communicate.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2017/4/19.
 */

@Data
@Entity
public class Verification {

    public final static String TYPE_SMS ="1";
    public final static String TYPE_EMAIL="2";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String code;

    //const TYPE_SMS = 1;   // 手机号码验证
    //const TYPE_EMAIL = 2; // 邮件地址验证
    private String type;

    //对应的目标数据
    private String target;

    @Column(name="create_time")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

}
