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

CREATE TABLE if not exists word_bank
(
    english VARCHAR(100) PRIMARY KEY,
    chinese VARCHAR(200) NOT NULL,
    example TEXT,
    date DATE NOT NULL DEFAULT (CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS user(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
)


