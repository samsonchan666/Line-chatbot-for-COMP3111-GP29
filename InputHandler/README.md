Use the following command in postgresql to create tables:
create table tour (
	id varchar(10) primary key,
	name varchar(80),
	attraction varchar(150),
	duration int,
	weekDayPrice int,
	weekEndPrice int,
	dates varchar(100)
);

create table customer (
	name varchar(20) ,
	id varchar(20),
	phoneNo int,
	age int,
	tour varchar(50),
	adultNo int,
	childrenNo int,
	toodlerNo int,
	tourFee real,
	paid real
);
create table faq (
	keywords varchar(500) ,
	respond varchar(500)
);
