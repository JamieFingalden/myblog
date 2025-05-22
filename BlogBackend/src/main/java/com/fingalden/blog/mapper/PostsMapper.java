package com.fingalden.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fingalden.blog.domain.Posts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Jamie
* @description 针对表【posts】的数据库操作Mapper
* @createDate 2025-04-17 08:58:54
* @Entity com.fingalden.bolg.domain.Posts
*/
@Mapper
public interface PostsMapper extends BaseMapper<Posts> {

    List<Posts> selectList();

    int deleteByPrimaryKey(Long id);

    int insert(Posts record);

    int insertSelective(Posts record);

    Posts selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Posts record);

    int updateByPrimaryKey(Posts record);

}
