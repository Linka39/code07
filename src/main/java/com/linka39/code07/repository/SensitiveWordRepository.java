package com.linka39.code07.repository;

import com.linka39.code07.entity.Dic;
import com.linka39.code07.entity.SensitiveWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 敏感词 Respository 接口
 */
public interface SensitiveWordRepository extends JpaRepository<SensitiveWord,Integer>, JpaSpecificationExecutor<SensitiveWord> {

    /**
     * 查询敏感词
     * @param
     * @return
     */
    @Query(value = "select word from t_sensitive_word where  1 = ?1",nativeQuery = true)
    public List<String> getAllSensitiveWord(int num);

    /**
     * 查询敏感词by情感类型
     * @param
     * @return
     */
    @Query(value = "select word,emotion_weight from t_sensitive_word where  emotion = ?1",nativeQuery = true)
    public List<String[]> getAllSensitiveWordByEmotion(int num);
}
