CREATE TEMPORARY TABLE islands_backup(id, name, home, visit, create_time, experience, color, size);
INSERT INTO islands_backup SELECT id, name, home, visit, create_time, experience, color, size FROM islands;
DROP TABLE islands;
CREATE TABLE islands(`id` INTEGER PRIMARY KEY AUTOINCREMENT , `name` VARCHAR , `home` VARCHAR , `visit` BOOLEAN , `create_time` BIGINT , `experience` INTEGER , `color` VARCHAR NOT NULL , `size` INTEGER NOT NULL , UNIQUE (`name`));
INSERT INTO islands SELECT id, name, home, visit, create_time, experience, color, size FROM islands_backup;
DROP TABLE islands_backup;
