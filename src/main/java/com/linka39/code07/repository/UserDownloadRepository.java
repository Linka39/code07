package com.linka39.code07.repository;

import com.linka39.code07.entity.UserDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户下载资源 Respository 接口
 */
public interface UserDownloadRepository extends JpaRepository<UserDownload,Integer>, JpaSpecificationExecutor<UserDownload> {
    /**
     * 查询某用户下载某资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    @Query(value = "select count(*) from t_user_download where user_id=?1 and article_id=?2",nativeQuery = true)
    public Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);

    /**
     * 删除指定帖子下载信息
     */
    @Query(value = "delete from t_user_download where article_id=?1",nativeQuery = true)
    @Modifying  //增删改都要加下modifying注解
    public void deleteByArticleId(Integer articleId);
}
