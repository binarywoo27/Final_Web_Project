drop database ssafypjt;

create database if not exists ssafypjt;

use ssafypjt;

-- 유저------------------------------------------------

drop table if exists users;

create table users(
`id` varchar(10) primary key not null,
`password` varchar(20) not null,
`name` varchar(15) not null,
`address` varchar(50) not null,
`phone` varchar(20) not null,
`token` VARCHAR(1000) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into users(id, password, name, address, phone) values('admin','1111', '어드민', '대한민국의 어딘가', '010-1234-5678');
insert into users(id, password, name, address, phone) values('user1','1111', '유저1', '이세상의 어딘가', '010-9876-5432');

-- 게시판------------------------------------------------

insert into users(id, password, name, address, phone) values('ssafy','1111','싸피','구미시 진미동','010-1111-1111');

DROP TABLE IF EXISTS board ;

CREATE TABLE IF NOT EXISTS board (
  `articleno` INT NOT NULL AUTO_INCREMENT,
  `userid` VARCHAR(16) NULL DEFAULT NULL,
  `subject` VARCHAR(100) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `hit` INT NULL DEFAULT 0,
  `regtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`articleno`),
  INDEX `board_to_users_fk` (`userid` ASC) VISIBLE,
  CONSTRAINT `board_to_users_fk`
    FOREIGN KEY (`userid`)
    REFERENCES `ssafypjt`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- QnA------------------------------------------------

insert into board(articleno, userid, subject, content, hit, regtime) values(1,'admin','부동산 매매 방법','자세한 내용은 가이드라인 파일을 참고하세요',0,now());

DROP TABLE IF EXISTS qna ;

CREATE TABLE IF NOT EXISTS qna (
  `articleno` INT NOT NULL AUTO_INCREMENT,
  `userid` VARCHAR(16) NULL DEFAULT NULL,
  `subject` VARCHAR(100) NULL DEFAULT NULL,
  `content` VARCHAR(2000) NULL DEFAULT NULL,
  `filename` VARCHAR(200) NULL DEFAULT NULL,
  `dbfilename` VARCHAR(200) NULL DEFAULT NULL,
  `hit` INT NULL DEFAULT 0,
  `regtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`articleno`),
  INDEX `qna_to_users_fk` (`userid` ASC) VISIBLE,
  CONSTRAINT `qna_to_users_fk`
    FOREIGN KEY (`userid`)
    REFERENCES `ssafypjt`.`users` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

insert into qna(articleno, userid, subject, content, hit, regtime) values(1,'ssafy','회원등업신청드립니다.','안녕하세요 잘 부탁드립니다.',0,now());

-- 관심지역------------------------------------------------

DROP TABLE IF EXISTS `ssafypjt`.`favorites` ;

CREATE TABLE IF NOT EXISTS `ssafypjt`.`favorites` (
	`sidoCode` VARCHAR(10) NOT NULL,
	`gugunCode` VARCHAR(10) NOT NULL,
	`id` VARCHAR(10) NOT NULL,
	`sido` VARCHAR(30) NOT NULL,
	`gugun` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`gugunCode`, `id`),
	INDEX `users_to_favorite_id_idx` (`id` ASC) VISIBLE,
	CONSTRAINT `users_to_favorite_id`
    FOREIGN KEY (`id`)
    REFERENCES `ssafypjt`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into favorites(sidoCode, gugunCode, id, sido, gugun) values
('1100000000', '1111000000', 'admin', '서울특별시', '종로구'),
('2600000000', '2611000000', 'admin', '부산광역시', '중구'),
('2700000000', '2723000000', 'admin', '대구광역시', '북구');

insert into favorites(sidoCode, gugunCode, id, sido, gugun) values
('2700000000', '2723000000', 'user1', '대구광역시', '북구');

insert into favorites(sidoCode, gugunCode, id, sido, gugun) values
('2600000000', '2611000000', 'user1', '부산광역시', '중구');

select * from `ssafypjt`.`favorites`;

delete from `ssafypjt`.`favorites` where gugunCode = '2723000000' and id = 'user1';

select * from `ssafypjt`.`favorites` where id = 'user1';

-- 아파트 조회수 ------------------------------------------------------

DROP TABLE IF EXISTS `ssafypjt`.`househit` ;

CREATE TABLE IF NOT EXISTS `ssafypjt`.`househit` (
  `roadCode` VARCHAR(10) not null,
  `sidoName` VARCHAR(10) NOT NULL,
  `gugunName` VARCHAR(10) NOT NULL,
  `houseName` VARCHAR(30) NOT NULL,
  `hit` int NOT NULL,
  PRIMARY KEY (`roadCode`, `houseName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

select * from househit;

select *
from househit
order by hit desc
limit 10;

insert into househit(roadCode, houseName, sidoName, gugunName, hit) values
('3145040', '대구역 서희스타힐스', '대구광역시', '북구', 53),
('4235375', '대구역유림노르웨이숲', '대구광역시', '북구', 47),
('2007001', '칠성동코오롱하늘채', '대구광역시', '북구', 12),
('3145011', '칠곡현대아파트', '대구광역시', '북구', 10),
('3133027', '삼정그린코아', '부산광역시', '해운대구', 21),
('3133027', '삼한2', '부산광역시', '해운대구', 3),
('3133027', '광하주택2', '부산광역시', '해운대구', 8),
('4100135', '경희궁의아침3단지', '서울특별시', '종로구', 15),
('4100147', '운현신화타워', '서울특별시', '종로구', 12),
('4100372', '종로중흥S클래스', '서울특별시', '종로구', 16),
('3305077', '원평동신화오페라시티', '경상북도', '구미시', 62);

update househit set hit = hit + 1 where roadCode = '3145040' and houseName = '대구역 서희스타힐스';

commit;