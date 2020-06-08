package com.linka39.code07.repository;

import com.linka39.code07.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源Respository 接口
 */
public interface ArticleRepository extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {
}
