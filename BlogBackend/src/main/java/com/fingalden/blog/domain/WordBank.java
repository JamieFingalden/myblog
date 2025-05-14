package com.fingalden.blog.domain;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName word_bank
 */
@Data
public class WordBank {
    /**
     * 
     */
    private String english;

    /**
     * 
     */
    private String chinese;

    /**
     * 
     */
    private String example;

    /**
     * 
     */
    private Date date;
}