create schema db;

CREATE table "db".userdb(
	id serial primary key,
	nome varchar(255),
	anno varchar(255),
	societa varchar(255),
	sesso varchar(1),
	codice varchar(255),
	CONSTRAINT uni UNIQUE(nome, anno, societa, sesso, codice)
);
CREATE INDEX USERDB_DB_ID
  ON "db".USERDB (ID);

CREATE table "db".garedb(
	id serial primary key,
	data varchar(255),
	tipo varchar(255),
	tempo varchar(255),
	vasca varchar(255),
	federazione varchar(255),
    categoria varchar(255),
    iduser serial,
    foreign key (iduser) references "db".userdb(id),
	CONSTRAINT unigare UNIQUE(data, tipo, tempo, federazione, categoria, vasca)
);
CREATE INDEX GAREDB_DB_ID
  ON "db".GAREDB (ID);

Create sequence "db".user_id
start 1
increment 1
owned by "db".userdb.id;

Create sequence "db".gare_id
start 1
increment 1
owned by "db".garedb.id;

CREATE table "db".gareuser(
    idgara serial,
    iduser serial,
    constraint gareuserunique UNIQUE(idgara, iduser),
    foreign key (idgara) references "db".garedb(id),
    foreign key (iduser) references "db".userdb(id)
);