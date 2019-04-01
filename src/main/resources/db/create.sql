-- define

CREATE TABLE IF NOT EXISTS APP_CATEGORY(
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           type VARCHAR(30) NOT NULL,
                           PRIMARY KEY (id),
                           UNIQUE unique_type (type)
);

CREATE TABLE IF NOT EXISTS APP_PACKAGE (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           file BLOB,
                           file_name VARCHAR(30) NOT NULL,
                           small_icon BLOB,
                           small_icon_name VARCHAR(30),
                           big_icon BLOB,
                           big_icon_name VARCHAR(30),
                           PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS APP (
                   id BIGINT NOT NULL AUTO_INCREMENT,
                   name VARCHAR(30) NOT NULL,
                   category_id BIGINT NOT NULL,
                   description VARCHAR(1000),
                   downloads_count BIGINT NOT NULL DEFAULT 0,
                   package_id BIGINT NOT NULL,
                   package_name VARCHAR(30) NOT NULL,
                   PRIMARY KEY (id),
                   UNIQUE KEY unique_name_category (name, category_id),
                   CONSTRAINT FK_APP_CATEGORY FOREIGN KEY (category_id) REFERENCES APP_CATEGORY (id),
                   CONSTRAINT FK_APP_PACKAGE FOREIGN KEY (package_id) REFERENCES APP_PACKAGE (id),
                   CONSTRAINT FK_APP_SMALL_IMAGE_VIEW FOREIGN KEY (package_id) REFERENCES APP_PACKAGE (id)
);

CREATE VIEW IF NOT EXISTS SMALL_ICON_VIEW AS SELECT ID, SMALL_ICON FROM APP_PACKAGE;
CREATE VIEW IF NOT EXISTS BIG_ICON_VIEW AS SELECT ID, BIG_ICON FROM APP_PACKAGE;

CREATE TABLE IF NOT EXISTS USERS(
                    username varchar(50) not null primary key,
                    password varchar(100) not null,
                    enabled boolean not null
);

CREATE TABLE IF NOT EXISTS AUTHORITIES (
                           username VARCHAR(50) NOT NULL,
                           authority VARCHAR(50) NOT NULL,
                           CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username),
                           UNIQUE KEY ix_auth_username (username,authority)
);

CREATE TABLE IF NOT EXISTS DEFAULT_ICONS (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             small_icon BLOB,
                             big_icon BLOB,
                             PRIMARY KEY (id)
);

