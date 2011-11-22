# --- First database schema

# --- !Ups

set ignorecase true;

create table paste (
  id bigint not null,
  title varchar(255) not null,
  code clob not null,
  pastedAt timestamp,
  constraint pk_paste primary key (id))
;

create sequence paste_seq start with 1000;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists paste;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists paste_seq;
