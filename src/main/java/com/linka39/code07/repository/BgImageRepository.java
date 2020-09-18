package com.linka39.code07.repository;

import com.linka39.code07.entity.BgImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 背景图片类型 Respository 接口
 */
public interface BgImageRepository extends JpaRepository<BgImage,Integer>, JpaSpecificationExecutor<BgImage> {
}
