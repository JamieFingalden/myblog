package com.fingalden.blog.mapper;

import com.fingalden.blog.domain.WordBank;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author FGJ
* @description 针对表【word_bank】的数据库操作Mapper
* @createDate 2025-05-14 11:23:53
* @Entity com.fingalden.blog.domain.WordBank
*/
@Mapper
public interface WordBankMapper {

    int deleteByPrimaryKey(Long id);

    int insert(WordBank record);

    int insertSelective(WordBank record);

    int insertWords(Long userId, List<WordBank> words);

    WordBank selectByPrimaryKey(Long id);

    List<WordBank> selectAllWorks(Long userId);

    int updateByPrimaryKeySelective(WordBank record);

    int updateByPrimaryKey(WordBank record);

}
