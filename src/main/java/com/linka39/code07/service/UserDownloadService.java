package com.linka39.code07.service;

import com.linka39.code07.entity.UserDownload;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 用户下载Service接口
 */
public interface UserDownloadService {
    /**
     * 查询某用户下载某资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    public Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);

    /**
     * 分页查询用户下载信息
     * @param s_userDownload
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<UserDownload> list(UserDownload s_userDownload, Integer page, Integer pageSize, Direction direction, String...properties );
    public Long getTotal(UserDownload userDownload);
    /**
     * 添加或者修改用户下载信息
     * @param userDownload
     */
    public void save(UserDownload userDownload);

    public void deleteByArticleId(Integer id);
}

