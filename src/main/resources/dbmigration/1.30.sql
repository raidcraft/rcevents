-- apply changes
create table rc_events_global (
  id                            bigint auto_increment not null,
  event                         varchar(255),
  execution_count               bigint not null,
  last_activation               datetime(6),
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_rc_events_global primary key (id)
);

create table rc_events_players (
  id                            bigint auto_increment not null,
  event                         varchar(255),
  execution_count               bigint not null,
  last_activation               datetime(6),
  player_id                     varchar(40),
  player                        varchar(255),
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_rc_events_players primary key (id)
);

