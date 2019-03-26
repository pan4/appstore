
-- defining

CREATE TABLE APP_CATEGORY(
   id BIGINT NOT NULL AUTO_INCREMENT,
   type VARCHAR(30) NOT NULL,
   PRIMARY KEY (id),
   UNIQUE unique_type (type)
);

CREATE TABLE APP_PACKAGE (
	id BIGINT NOT NULL AUTO_INCREMENT,
	file BLOB,
	small_icon BLOB,
  big_icon BLOB,
  PRIMARY KEY (id)
);

CREATE TABLE APP (
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

CREATE VIEW SMALL_ICON_VIEW AS SELECT ID, SMALL_ICON FROM APP_PACKAGE;

CREATE VIEW BIG_ICON_VIEW AS SELECT ID, BIG_ICON FROM APP_PACKAGE;

-- filling

INSERT INTO APP_CATEGORY(type)
VALUES ('EDUCATION');

INSERT INTO APP_CATEGORY(type)
VALUES ('GAMES');

INSERT INTO APP(name, category_id, description, package)
VALUES ('Duolingo', 1, 'Дуолинго - самое популярное приложение в мире в категории “Обучение”.');

INSERT INTO APP(name, category_id, description, package)
VALUES ('Экзамен ПДД', 1, 'Простое и удобное приложение для подготовки к экзамену в ГИБДД.');

INSERT INTO APP(name, category_id, description, package)
VALUES ('Slugterra', 2, 'Новый динамичный изометрический 3D экшен с элементами RPG.');

INSERT INTO APP(name, category_id, description, package)
VALUES ('Маши и Медведь', 2, 'Встречаем новые приключения Маши и Медведь!');

-- drop

DROP TABLE APP_PACKAGE;
DROP TABLE APP;
DROP TABLE APP_CATEGORY;

-- cleanup

DELETE FROM APP_PACKAGE;
DELETE FROM APP;
DELETE FROM APP_CATEGORY;
