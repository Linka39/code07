package com.linka39.code07.repository;

import com.linka39.code07.entity.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户 Respository 接口
 */
public interface MessageRepository extends JpaRepository<Message,Integer>, JpaSpecificationExecutor<Message> {

    /**
     * 查询某用户的所有消息
     * @param userId
     * @return
     */
    @Query(value = "select count(*) from t_message where is_see = false and user_id = ?1",nativeQuery = true)
    public Integer getCountByUserId(Integer userId);

    /**
     * 修改为已查看状态
     * @param userId
     * @return
     */
    @Query(value = "update t_message set is_see=true where user_id = ?1",nativeQuery = true)
    @Modifying
    public Integer updateState(Integer userId);
}
