package com.fingalden.bolg.domain;

import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName posts
 */
@Data
public class Posts {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String excerpt;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date publishedAt;
}