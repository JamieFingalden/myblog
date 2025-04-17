package com.fingalden.bolg.service;

import com.fingalden.bolg.domain.Posts;
import java.util.List;

public interface PostsService {
    List<Posts> getPostsList();

    void addPosts(Posts posts);

    void updatePosts(Posts posts);

    void deletePosts(Long id);
}
