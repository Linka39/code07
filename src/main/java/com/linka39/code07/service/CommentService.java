package com.linka39.code07.service;

import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.Link;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 评论Service接口
 */
public interface CommentService {

    public void save(Comment comment);

}

