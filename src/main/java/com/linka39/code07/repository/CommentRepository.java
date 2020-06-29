package com.linka39.code07.repository;
import com.linka39.code07.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户 Respository 接口
 */
public interface CommentRepository extends JpaRepository<Comment,Integer>, JpaSpecificationExecutor<Comment> {
    @Query(value = "delete from t_comment where article_id=?1",nativeQuery = true)
    @Modifying  //删除指定帖子评论信息
    public void deleteByArticleId(Integer articleId);
}
