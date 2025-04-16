package com.fingalden.bolg.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    private int id;
    private String title;
    private String content;
    private String excerpt;
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
