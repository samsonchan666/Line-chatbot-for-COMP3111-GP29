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
create table booking (
	id varchar(30) ,
	tourId varchar(10),
	dates varchar(60),
	tourGuide varchar(50),
	lineAcc varchar(50),
	hotel varchar(50),
	capacity int,
	miniCustomer int,
	currentCustomer int
);

create table discount (
	bookingId varchar(100),
	tourId varchar(20),
	tourName varchar(50),
	number int,
	discountDate varchar(50),
	discountTime varchar(15)
);

insert into discount values ('2D00120171106','2D001','Shimen National Forest Tour',4,'10/11/2017','1200');


