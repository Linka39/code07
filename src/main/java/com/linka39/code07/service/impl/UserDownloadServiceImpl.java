package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.UserDownload;
import com.linka39.code07.repository.LinkRepository;
import com.linka39.code07.repository.UserDownloadRepository;
import com.linka39.code07.service.LinkService;
import com.linka39.code07.service.UserDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 用户下载Service实现类
 */
@Service("userdownloadService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class UserDownloadServiceImpl implements UserDownloadService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private UserDownloadRepository userDownloadRepository;

    @Override
    public Integer getCountByUserIdAndArticleId(Integer userId, Integer articleId) {
        return userDownloadRepository.getCountByUserIdAndArticleId(userId,articleId);
    }

    @Override
    public void save(UserDownload userDownload) {
        userDownloadRepository.save(userDownload);
    }

    @Override
    public void deleteByArticleId(Integer id) {
        userDownloadRepository.deleteByArticleId(id);
    }
}
