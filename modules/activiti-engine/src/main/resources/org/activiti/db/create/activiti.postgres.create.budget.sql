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
    primary key (ID_)
);

create table ACT_BUDGET_COSTENTRY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    primary key (ID_)
);

create table ACT_BUDGET_PROJECTCOSTITEM (
    ID_ varchar(64),
    IDPROJECT_ varchar(64),
    IDCOSTENTRY_ varchar(64),
    REV_ integer,
    TOTAL_ double precision,
    ACTUAL_ double precision,
    primary key (ID_)
);

create table ACT_BUDGET_PROJECTSOURCEITEM (
    ID_ varchar(64),
    IDPROJECT_ varchar(64),
    IDSOURCE_ varchar(64),
    REV_ integer,
    TOTAL_ double precision,
    ACTUAL_ double precision,
    primary key (ID_)
);

create table ACT_ATTACHMENT_CATEGORY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    primary key (ID_)
);



