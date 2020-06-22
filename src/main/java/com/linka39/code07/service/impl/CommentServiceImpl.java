package com.linka39.code07.service.impl;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.UserDownload;
import com.linka39.code07.repository.CommentRepository;
import com.linka39.code07.repository.UserDownloadRepository;
import com.linka39.code07.service.CommentService;
import com.linka39.code07.service.UserDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 评论Service实现类
 */
@Service("commentService")
@Transactional//在public方法内发生unchecked exception，就会发生rollback
public class CommentServiceImpl implements CommentService {
    @Autowired//Autowired根据 type 装载，Resource根据名称装载
    private CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
