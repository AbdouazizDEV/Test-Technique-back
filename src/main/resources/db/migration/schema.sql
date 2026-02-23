CREATE DATABASE IF NOT EXISTS article_manager
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE article_manager;

CREATE TABLE IF NOT EXISTS users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(100)        NOT NULL,
  email       VARCHAR(150)        NOT NULL UNIQUE,
  password    VARCHAR(255)        NOT NULL,
  role        ENUM('ROLE_ADMIN','ROLE_MEMBER') NOT NULL DEFAULT 'ROLE_MEMBER',
  created_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS articles (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  title         VARCHAR(200)        NOT NULL,
  content       TEXT                NOT NULL,
  author_id     BIGINT              NOT NULL,
  published_at  DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_article_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_articles_author ON articles(author_id);
CREATE INDEX idx_articles_title  ON articles(title);
