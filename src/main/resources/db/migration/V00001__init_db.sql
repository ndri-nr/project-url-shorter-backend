create table mst_account (
	id			serial primary key			not null,
	email		varchar(255)				not null,
	password	varchar(255)				not null,
	full_name	varchar(255)				not null
);

create table session (
	id			serial primary key			not null,
	session_id	varchar(255) 				not null,
	account_id	int4						not null,
	expired_at	timestamp					not null
);

create table mst_url (
	id			serial primary key			not null,
	url_id		varchar(20) 				not null,
	account_id	int4,
	visited		int4,
	redirect_to	varchar		 				not null
);

ALTER TABLE "session" ADD FOREIGN KEY ("account_id") REFERENCES "mst_account" ("id");
ALTER TABLE "mst_url" ADD FOREIGN KEY ("account_id") REFERENCES "mst_account" ("id");
