package cn.springlogic.communicate.jpa.repository;

import cn.springlogic.communicate.jpa.entity.Verification;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by admin on 2017/4/19.
 */

@Configuration
@RepositoryRestResource(path="verification")
public interface VerificationRepository extends JpaRepository<Verification,Integer>{

    @Query(value = "SELECT * FROM verification  where target = :target and type = :type order by create_time desc limit 1",nativeQuery = true)
    public Verification findFirst1BytargetAndTypeOrderByCreateTimeDesc(@Param("target")String target, @Param("type")String type);

    public Verification findFirst1BytargetAndTypeAndCodeOrderByCreateTimeDesc(@Param("target")String target,@Param("type")String type,@Param("code")String code);

}
