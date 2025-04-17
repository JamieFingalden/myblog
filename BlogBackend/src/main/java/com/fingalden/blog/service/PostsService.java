package com.fingalden.blog.service;

import com.fingalden.blog.domain.Posts;
import java.util.List;

public interface PostsService {
    List<Posts> getPostsList();

    void addPosts(Posts posts);

    void updatePosts(Posts posts);

    void deletePosts(Long id);
}
