package com.linka39.code07.repository;

import com.linka39.code07.entity.SensitiveArticleHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 已审核通过的历史文章流水。可以在日终的定时任务中执行 Respository 接口
 */
public interface SensitiveArticleHisRepository extends JpaRepository<SensitiveArticleHis,Integer>, JpaSpecificationExecutor<SensitiveArticleHis> {
}
