package com.fingalden.blog.controller;

import com.fingalden.blog.domain.Posts;
import com.fingalden.blog.service.PostsService;
import com.fingalden.blog.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/list")
    public Result getPostList() {
        return Result.success(postsService.getPostsList());
    }

    @PostMapping
    public Result addPost(@RequestBody Posts posts) {
        postsService.addPosts(posts);
        return Result.success();
    }

    @PutMapping
    public Result updatePost(@RequestBody Posts posts) {
        postsService.updatePosts(posts);
        return Result.success();
    }

    @DeleteMapping
    public Result deletePost(@RequestParam Long id) {
        postsService.deletePosts(id);
        return Result.success();
    }
}
