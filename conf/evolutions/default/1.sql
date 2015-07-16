# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer_decimal (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  dbl                       double,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_decimal primary key (id))
;

create table answer_integer (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  number                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_integer primary key (id))
;

create table answer_text (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_text primary key (id))
;

create table answer_word (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_answer_word primary key (id))
;

create table basis (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_basis primary key (id))
;

create table choice (
  id                        bigint auto_increment not null,
  question_id               bigint not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  is_correct                tinyint(1) default 0,
  is_active                 tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_choice primary key (id))
;

create table content_file (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  file_type                 varchar(255),
  uploader_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint uq_content_file_uploader_id unique (uploader_id),
  constraint pk_content_file primary key (id))
;

create table content_text (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content_text primary key (id))
;

create table course (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  description               varchar(255),
  start_date                datetime(6),
  end_date                  datetime(6),
  is_archived               tinyint(1) default 0,
  has_open_enrollment       tinyint(1) default 0,
  instructor_id             bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_course primary key (id))
;

create table institution (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  location                  varchar(255),
  description               varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_institution primary key (id))
;

create table message (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  creator_id                bigint,
  prompt_id                 bigint,
  type                      integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_message_type check (type in (0,1)),
  constraint pk_message primary key (id))
;

create table module (
  id                        bigint auto_increment not null,
  course_id                 bigint not null,
  retired                   tinyint(1) default 0,
  description               varchar(255),
  order_index               integer,
  release_date              datetime(6),
  has_spaced_repetition     tinyint(1) default 0,
  is_hidden                 tinyint(1) default 0,
  type                      integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_module_type check (type in (0,1,2)),
  constraint pk_module primary key (id))
;

create table prompt (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_prompt primary key (id))
;

create table question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  message_id                bigint,
  type                      integer,
  has_subquestions          tinyint(1) default 0,
  is_global                 tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_question_type check (type in (0,1,2)),
  constraint uq_question_message_id unique (message_id),
  constraint pk_question primary key (id))
;

create table tag (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_tag primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  first_name                varchar(255),
  last_name                 varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  type                      integer,
  creator_id                bigint,
  institution_id            bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_user_type check (type in (0,1,2,3)),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;


create table course_message (
  course_id                      bigint not null,
  message_id                     bigint not null,
  constraint pk_course_message primary key (course_id, message_id))
;

create table course_user (
  course_id                      bigint not null,
  user_id                        bigint not null,
  constraint pk_course_user primary key (course_id, user_id))
;

create table institution_user (
  institution_id                 bigint not null,
  user_id                        bigint not null,
  constraint pk_institution_user primary key (institution_id, user_id))
;

create table message_user (
  message_id                     bigint not null,
  user_id                        bigint not null,
  constraint pk_message_user primary key (message_id, user_id))
;

create table module_question (
  module_id                      bigint not null,
  question_id                    bigint not null,
  constraint pk_module_question primary key (module_id, question_id))
;

create table question_basis (
  question_id                    bigint not null,
  basis_id                       bigint not null,
  constraint pk_question_basis primary key (question_id, basis_id))
;

create table question_tag (
  question_id                    bigint not null,
  tag_id                         bigint not null,
  constraint pk_question_tag primary key (question_id, tag_id))
;
alter table choice add constraint fk_choice_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_choice_question_1 on choice (question_id);
alter table content_file add constraint fk_content_file_uploader_2 foreign key (uploader_id) references user (id) on delete restrict on update restrict;
create index ix_content_file_uploader_2 on content_file (uploader_id);
alter table course add constraint fk_course_instructor_3 foreign key (instructor_id) references user (id) on delete restrict on update restrict;
create index ix_course_instructor_3 on course (instructor_id);
alter table message add constraint fk_message_creator_4 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_message_creator_4 on message (creator_id);
alter table message add constraint fk_message_prompt_5 foreign key (prompt_id) references prompt (id) on delete restrict on update restrict;
create index ix_message_prompt_5 on message (prompt_id);
alter table module add constraint fk_module_course_6 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_module_course_6 on module (course_id);
alter table question add constraint fk_question_message_7 foreign key (message_id) references message (id) on delete restrict on update restrict;
create index ix_question_message_7 on question (message_id);
alter table user add constraint fk_user_institution_8 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_user_institution_8 on user (institution_id);



alter table course_message add constraint fk_course_message_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_message add constraint fk_course_message_message_02 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table course_user add constraint fk_course_user_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_user add constraint fk_course_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table institution_user add constraint fk_institution_user_institution_01 foreign key (institution_id) references institution (id) on delete restrict on update restrict;

alter table institution_user add constraint fk_institution_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table message_user add constraint fk_message_user_message_01 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table message_user add constraint fk_message_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table module_question add constraint fk_module_question_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_question add constraint fk_module_question_question_02 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_basis add constraint fk_question_basis_question_01 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_basis add constraint fk_question_basis_basis_02 foreign key (basis_id) references basis (id) on delete restrict on update restrict;

alter table question_tag add constraint fk_question_tag_question_01 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_tag add constraint fk_question_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer_decimal;

drop table answer_integer;

drop table answer_text;

drop table answer_word;

drop table basis;

drop table choice;

drop table content_file;

drop table content_text;

drop table course;

drop table course_message;

drop table course_user;

drop table institution;

drop table institution_user;

drop table message;

drop table message_user;

drop table module;

drop table module_question;

drop table prompt;

drop table question;

drop table question_basis;

drop table question_tag;

drop table tag;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

