create database if not exists blog;
use blog;

CREATE TABLE if not exists Posts
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    excerpt      TEXT,
    status       VARCHAR(20) DEFAULT '草稿',
    created_at   DATETIME    DEFAULT CURRENT_TIMESTAMP,
    published_at DATETIME
);