package com.fingalden.bolg.service.Impl;

import com.fingalden.bolg.domain.Posts;
import com.fingalden.bolg.mapper.PostsMapper;
import com.fingalden.bolg.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class PostsServiceImpl implements PostsService {
    @Autowired
    private PostsMapper postsMapper;

    @Override
    public List<Posts> getPostsList() {
        return postsMapper.selectList();
    }

    @Override
    public void addPosts(Posts posts) {
        postsMapper.insertSelective(posts);
        log.info("添加成功");
    }

    @Override
    public void updatePosts(Posts posts) {
        postsMapper.updateByPrimaryKeySelective(posts);
        log.info("修改成功");
    }

    @Override
    public void deletePosts(Long id) {
        postsMapper.deleteByPrimaryKey(id);
        log.info("删除成功");
    }
}
