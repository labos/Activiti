create table ACT_BUDGET_SOURCE (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TOTAL_ double precision,
    primary key (ID_)
);

create table ACT_BUDGET_PROGRAM (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TOTAL_ double precision,
    primary key (ID_)
);

create table ACT_BUDGET_PROJECT (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    TOTAL_ double precision,
    primary key (ID_)
);



