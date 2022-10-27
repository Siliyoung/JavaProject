/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/9/10 10:17:58                           */
/*==============================================================*/


drop table if exists adm;

drop table if exists course;

drop table if exists student;

drop table if exists student_course;

drop table if exists teacher;

/*==============================================================*/
/* Table: adm                                                   */
/*==============================================================*/
create table adm
(
   adm_id               varchar(20) not null,
   adm_name             varchar(20) not null,
   adm_password         varchar(20),
   adm_gender           char(2),
   primary key (adm_id)
);

/*==============================================================*/
/* Table: course                                                */
/*==============================================================*/
create table course
(
   course_id            varchar(20) not null,
   teacher_id           varchar(20),
   course_name          varchar(100) not null,
   course_nature        varchar(20),
   course_hours         int,
   course_credit        double,
   opening_time         datetime,
   ending_time          datetime,
   course_state         smallint,
   primary key (course_id)
);

/*==============================================================*/
/* Table: student                                               */
/*==============================================================*/
create table student
(
   student_id           varchar(20) not null,
   student_name         varchar(50) not null,
   student_password     varchar(20),
   student_grade        varchar(30),
   student_gender       char(2),
   primary key (student_id)
);

/*==============================================================*/
/* Table: student_course                                        */
/*==============================================================*/
create table student_course
(
   student_id           varchar(20),
   course_id            varchar(20)
);

/*==============================================================*/
/* Table: teacher                                               */
/*==============================================================*/
create table teacher
(
   teacher_id           varchar(20) not null,
   teacher_name         varchar(50) not null,
   teacher_password     varchar(20),
   teacher_gender       char(2),
   primary key (teacher_id)
);

alter table course add constraint FK_Reference_4 foreign key (teacher_id)
      references teacher (teacher_id) on delete restrict on update restrict;

alter table student_course add constraint FK_Reference_2 foreign key (student_id)
      references student (student_id) on delete restrict on update restrict;

alter table student_course add constraint FK_Reference_3 foreign key (course_id)
      references course (course_id) on delete restrict on update restrict;

