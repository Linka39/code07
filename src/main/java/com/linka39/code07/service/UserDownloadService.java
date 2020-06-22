package com.linka39.code07.service;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.UserDownload;

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
     * 添加或者修改用户下载信息
     * @param userDownload
     */
    public void save(UserDownload userDownload);
}

