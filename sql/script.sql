DELETE from `kaguya`.`admin_o_auth`;
DELETE from `kaguya`.`category`;
DELETE from `kaguya`.`document`;
DELETE from `kaguya`.`document_group`;
DELETE from `kaguya`.`user`;

ALTER TABLE `kaguya`.`admin_o_auth` AUTO_INCREMENT = 1;
ALTER TABLE `kaguya`.`category` AUTO_INCREMENT = 1;
ALTER TABLE `kaguya`.`document` AUTO_INCREMENT = 1;
ALTER TABLE `kaguya`.`document_group` AUTO_INCREMENT = 1;
ALTER TABLE `kaguya`.`user` AUTO_INCREMENT = 1;
