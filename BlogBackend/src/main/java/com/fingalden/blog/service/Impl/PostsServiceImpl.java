package com.fingalden.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingalden.blog.domain.Posts;
import com.fingalden.blog.mapper.PostsMapper;
import com.fingalden.blog.service.PostsService;
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
//        postsMapper.updateByPrimaryKeySelective(posts);
        QueryWrapper<Posts> wrapper = new QueryWrapper<Posts>().eq("id", posts.getId());
        postsMapper.update(posts, wrapper);
        log.info("修改成功");
    }

    @Override
    public void deletePosts(Long id) {
        postsMapper.deleteByPrimaryKey(id);
        log.info("删除成功");
    }
}
