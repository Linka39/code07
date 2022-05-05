package com.linka39.code07.repository;

import com.linka39.code07.entity.Dic;
import com.linka39.code07.entity.SensitiveArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 敏感词文章 Respository 接口
 */
public interface SensitiveArticleRepository extends JpaRepository<SensitiveArticle,Integer>, JpaSpecificationExecutor<SensitiveArticle> {

    @Query(value = "select * from t_sensitive_article where article_id = ?1",nativeQuery = true)
    public SensitiveArticle getSentitiveByAticleId(int id);
}
