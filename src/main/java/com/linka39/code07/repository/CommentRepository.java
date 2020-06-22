package com.linka39.code07.repository;
import com.linka39.code07.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户 Respository 接口
 */
public interface CommentRepository extends JpaRepository<Comment,Integer>, JpaSpecificationExecutor<Comment> {
}
