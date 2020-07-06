package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Link;
import com.linka39.code07.entity.Message;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    @Override
    public List<UserDownload> list(UserDownload s_userDownload, Integer page, Integer pageSize, Sort.Direction direction, String... properties) {
        Pageable pageable= PageRequest.of(page-1,pageSize,direction,properties);
        //根据审核状态来筛选
        Page<UserDownload> pageMessage= userDownloadRepository.findAll(new Specification<UserDownload>() {
            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_userDownload!=null){
                    if(s_userDownload.getUser()!=null && s_userDownload.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"),s_userDownload.getUser().getId()));
                    }
                }
                return predicate;
            }
        },pageable);
        return pageMessage.getContent();
    }

    @Override
    public Long getTotal(UserDownload s_userDownload) {
        Long count = userDownloadRepository.count(new Specification<UserDownload>() {
            @Override
            public Predicate toPredicate(Root<UserDownload> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if(s_userDownload!=null){
                    if(s_userDownload.getUser()!=null && s_userDownload.getUser().getId()!=null){
                        predicate.getExpressions().add(cb.equal(root.get("user").get("id"),s_userDownload.getUser().getId()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }
}
