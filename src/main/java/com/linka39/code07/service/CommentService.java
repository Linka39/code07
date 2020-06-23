package com.linka39.code07.service;

import com.linka39.code07.entity.Article;
import com.linka39.code07.entity.Comment;
import com.linka39.code07.entity.Link;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 评论Service接口
 */
public interface CommentService {

    public void save(Comment comment);

    public List<Comment> list(Comment s_comment, Integer page, Integer pageSize, Direction direction, String...properties );

    public Long getTotal(Comment s_comment);

    public void delete(Integer id);
}

