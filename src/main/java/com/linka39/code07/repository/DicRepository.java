package com.linka39.code07.repository;

import com.linka39.code07.entity.Dic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 字典 Respository 接口
 */
public interface DicRepository extends JpaRepository<Dic,Integer>, JpaSpecificationExecutor<Dic> {

    /**
     * 查询某个字典的值
     * @param zddm
     * @return
     */
    @Query(value = "select zdnote from t_dic where zddm = ?1 and zdd = ?2",nativeQuery = true)
    public String getNoteByzddm(String zddm,Integer zdz);

    /**
     * 查询某个字典的值
     * @param zddm
     * @return
     */
    @Query(value = "select * from t_dic where zddm = ?1",nativeQuery = true)
    public List<Dic> getZdByzddm(String zddm);

    @Query(value = "select zdz from t_dic where zddm = ?1",nativeQuery = true)
    public List<String> getZdzByzddm(String zddm);
}
