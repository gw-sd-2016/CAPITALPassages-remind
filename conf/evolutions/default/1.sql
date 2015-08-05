# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer_decimal (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  answer                    double,
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

create table content (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content primary key (id))
;

create table content_file (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  name                      varchar(255),
  file_type                 varchar(255),
  uploader_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_content_file primary key (id))
;

create table content_text (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  text                      longtext,
  uploader_id               bigint,
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

create table flagged_question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  module_id                 bigint,
  question_id               bigint,
  status                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_flagged_question_status check (status in (0,1,2)),
  constraint pk_flagged_question primary key (id))
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

create table list_record (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  list_id                   bigint,
  submitter_id              bigint,
  attempt_number            integer,
  time_to_complete          double,
  is_cleared                tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_list_record primary key (id))
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
  name                      varchar(255),
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

create table module_list (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  start_date                datetime(6),
  end_date                  datetime(6),
  type                      integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_module_list_type check (type in (0,1)),
  constraint pk_module_list primary key (id))
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
  submitter_id              bigint,
  has_subquestions          tinyint(1) default 0,
  is_global                 tinyint(1) default 0,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_question_type check (type in (0,1,2)),
  constraint uq_question_message_id unique (message_id),
  constraint pk_question primary key (id))
;

create table question_list (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  start_date                datetime(6),
  end_date                  datetime(6),
  type                      integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_question_list_type check (type in (0,1)),
  constraint pk_question_list primary key (id))
;

create table question_record (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  time_to_complete          double,
  attempt_number            integer,
  is_cleared                tinyint(1) default 0,
  list_record_id            bigint,
  submitter_id              bigint,
  question_id               bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint uq_question_record_list_record_id unique (list_record_id),
  constraint pk_question_record primary key (id))
;

create table response (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  entity_id                 bigint,
  submitter_id              bigint,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint pk_response primary key (id))
;

create table student_in_institution (
  id                        bigint auto_increment not null,
  student_id                bigint,
  institution_id            bigint,
  official                  tinyint(1) default 0,
  created_time              datetime(6) not null,
  constraint pk_student_in_institution primary key (id))
;

create table student_question (
  id                        bigint auto_increment not null,
  retired                   tinyint(1) default 0,
  question_id               bigint,
  course_id                 bigint,
  status                    integer,
  created_time              datetime(6) not null,
  updated_time              datetime(6) not null,
  constraint ck_student_question_status check (status in (0,1,2)),
  constraint pk_student_question primary key (id))
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

create table message_content (
  message_id                     bigint not null,
  content_id                     bigint not null,
  constraint pk_message_content primary key (message_id, content_id))
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

create table module_list_module (
  module_list_id                 bigint not null,
  module_id                      bigint not null,
  constraint pk_module_list_module primary key (module_list_id, module_id))
;

create table module_list_user (
  module_list_id                 bigint not null,
  user_id                        bigint not null,
  constraint pk_module_list_user primary key (module_list_id, user_id))
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

create table question_list_message (
  question_list_id               bigint not null,
  message_id                     bigint not null,
  constraint pk_question_list_message primary key (question_list_id, message_id))
;

create table question_list_user (
  question_list_id               bigint not null,
  user_id                        bigint not null,
  constraint pk_question_list_user primary key (question_list_id, user_id))
;
alter table choice add constraint fk_choice_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_choice_question_1 on choice (question_id);
alter table course add constraint fk_course_instructor_2 foreign key (instructor_id) references user (id) on delete restrict on update restrict;
create index ix_course_instructor_2 on course (instructor_id);
alter table message add constraint fk_message_creator_3 foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_message_creator_3 on message (creator_id);
alter table message add constraint fk_message_prompt_4 foreign key (prompt_id) references prompt (id) on delete restrict on update restrict;
create index ix_message_prompt_4 on message (prompt_id);
alter table module add constraint fk_module_course_5 foreign key (course_id) references course (id) on delete restrict on update restrict;
create index ix_module_course_5 on module (course_id);
alter table question add constraint fk_question_message_6 foreign key (message_id) references message (id) on delete restrict on update restrict;
create index ix_question_message_6 on question (message_id);
alter table question add constraint fk_question_submitter_7 foreign key (submitter_id) references user (id) on delete restrict on update restrict;
create index ix_question_submitter_7 on question (submitter_id);
alter table question_record add constraint fk_question_record_listRecord_8 foreign key (list_record_id) references list_record (id) on delete restrict on update restrict;
create index ix_question_record_listRecord_8 on question_record (list_record_id);
alter table user add constraint fk_user_institution_9 foreign key (institution_id) references institution (id) on delete restrict on update restrict;
create index ix_user_institution_9 on user (institution_id);



alter table course_message add constraint fk_course_message_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_message add constraint fk_course_message_message_02 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table course_user add constraint fk_course_user_course_01 foreign key (course_id) references course (id) on delete restrict on update restrict;

alter table course_user add constraint fk_course_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table message_content add constraint fk_message_content_message_01 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table message_content add constraint fk_message_content_content_02 foreign key (content_id) references content (id) on delete restrict on update restrict;

alter table message_user add constraint fk_message_user_message_01 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table message_user add constraint fk_message_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table module_question add constraint fk_module_question_module_01 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_question add constraint fk_module_question_question_02 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table module_list_module add constraint fk_module_list_module_module_list_01 foreign key (module_list_id) references module_list (id) on delete restrict on update restrict;

alter table module_list_module add constraint fk_module_list_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

alter table module_list_user add constraint fk_module_list_user_module_list_01 foreign key (module_list_id) references module_list (id) on delete restrict on update restrict;

alter table module_list_user add constraint fk_module_list_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table question_basis add constraint fk_question_basis_question_01 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_basis add constraint fk_question_basis_basis_02 foreign key (basis_id) references basis (id) on delete restrict on update restrict;

alter table question_tag add constraint fk_question_tag_question_01 foreign key (question_id) references question (id) on delete restrict on update restrict;

alter table question_tag add constraint fk_question_tag_tag_02 foreign key (tag_id) references tag (id) on delete restrict on update restrict;

alter table question_list_message add constraint fk_question_list_message_question_list_01 foreign key (question_list_id) references question_list (id) on delete restrict on update restrict;

alter table question_list_message add constraint fk_question_list_message_message_02 foreign key (message_id) references message (id) on delete restrict on update restrict;

alter table question_list_user add constraint fk_question_list_user_question_list_01 foreign key (question_list_id) references question_list (id) on delete restrict on update restrict;

alter table question_list_user add constraint fk_question_list_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer_decimal;

drop table answer_integer;

drop table answer_text;

drop table answer_word;

drop table basis;

drop table choice;

drop table content;

drop table content_file;

drop table content_text;

drop table course;

drop table course_message;

drop table course_user;

drop table flagged_question;

drop table institution;

drop table list_record;

drop table message;

drop table message_content;

drop table message_user;

drop table module;

drop table module_question;

drop table module_list;

drop table module_list_module;

drop table module_list_user;

drop table prompt;

drop table question;

drop table question_basis;

drop table question_tag;

drop table question_list;

drop table question_list_message;

drop table question_list_user;

drop table question_record;

drop table response;

drop table student_in_institution;

drop table student_question;

drop table tag;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

