package com.linka39.code07.repository;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * 资源类型 Respository 接口
 */
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    /**
     * 根据用户名查找用户实体
     */
    @Query(value = "select * from t_user where user_name=?1",nativeQuery = true)
    public User findByUserName(String userName);

    /**
     * 根据邮箱查找用户实体
     */
    @Query(value = "select * from t_user where email=?1",nativeQuery = true)
    public User findByEmail(String Email);

    /**
     * 重置所有签到信息
     */
    @Query(value = "update t_user set is_sign=false ,sign_sort=null,sign_time=null",nativeQuery = true)
    @Modifying
    @Transactional//添加事务回滚
    public void updateAllSignInfo();


}
