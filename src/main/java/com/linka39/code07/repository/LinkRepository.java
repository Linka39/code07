package com.linka39.code07.repository;

import com.linka39.code07.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源类型 Respository 接口
 */
public interface LinkRepository extends JpaRepository<Link,Integer>, JpaSpecificationExecutor<Link> {
}
