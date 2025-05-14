package com.fingalden.blog.mapper;

import com.fingalden.blog.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author FGJ
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-05-14 18:51:35
* @Entity com.fingalden.blog.domain.User
*/
@Mapper
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User Login(User user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}
