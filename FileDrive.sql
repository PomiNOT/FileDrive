use master;

drop database if exists FileDrive;
go

create database FileDrive;
go

use FileDrive;

create table Users (
	[username] varchar(255) primary key not null,
	[password] varchar(255) not null check(len([password]) > 0),
	[firstName] nvarchar(255) not null,
	[lastName] nvarchar(255) not null,
	[role] int check(0 <= [role] and [role] <= 1) --0 is admin, 1 is user
);

create table Files (
	[fileId] int identity primary key, --unique file identifier
	[fileName] varchar(255) not null default('unknown'), --display file name on the UI, two files can have the same name
	[owner] varchar(255) foreign key references Users([username]),
	[location] varchar(512), --Location of the true file on the disk
	[isFolder] bit not null default(0), --Is a folder, the folder is a special type of file
	[path] varchar(1024) default('-1'), --Virtual path in the database, if a file is inside folder with id 4 and the folder itself is inside its parent folder with id 6, the path of this file will be 6.4
	[updated] datetime not null default(CURRENT_TIMESTAMP),
	[size] int default(0),
	[oldParent] int default(-1) --Used to restore trash to their correct old location or root if the parent is deleted
);

create table Sharing (
	[fileId] int primary key foreign key references Files([fileId]),
	[sharedBy] varchar(255) foreign key references Users([username]),
	[sharedTo] varchar(255),
	[isPublic] bit
);

create table UsageHistory (
	[id] int primary key identity,
	[username] varchar(255) foreign key references Users([username]),
	[total] int default(0),
	[updated] datetime not null default(CURRENT_TIMESTAMP)
);
	
go

insert into Users values ('admin', '123', 'Admin', 'Guy', 0);
/*
insert into Files ([owner], [location], [isFolder]) values ('admin', 'a.jpg', 0);

insert into Files ([owner], [location], [isFolder]) values ('admin', null, 1);
insert into Files ([owner], [location], [isFolder], [path]) values ('admin', 'b.jpg', 0, '2');
insert into Files ([owner], [location], [isFolder], [path]) values ('admin', 'c.jpg', 0, '2');

insert into Files ([owner], [location], [isFolder], [path]) values ('admin', null, 1, '2');
insert into Files ([owner], [location], [isFolder], [path]) values ('admin', 'e.jpg', 0, '2.5');
*/

go

create trigger UpdateFileTrigger
on Files
after update
as
begin
  declare @time datetime;
  set @time = CURRENT_TIMESTAMP;

  update Files
  set [updated] = @time
  where
	Files.[fileId] in (select fileId from inserted);

  declare @path varchar(1024);
  select @path = isnull([path], '') from inserted;
  
  update Files
  set [updated] = @time
  where 
	Files.[isFolder] = 1 and
	(@path like concat(Files.[path], '.', fileId, '%') or
	@path like concat(fileId, '%'));
end;

go

create trigger RemoveShare
on Files
instead of delete
as
begin
	declare @fileId int;
	select @fileId = [fileId] from deleted;

	delete from Sharing where Sharing.fileId = @fileId;
	delete from Files where fileId = @fileId;
end;

go

create trigger DeleteFileTrigger
on Files
after delete
as
begin
  declare @deletedPath varchar(1024);
  declare @deletedId int;
  declare @isFolder bit;

  select @deletedId = fileId, @deletedPath = isnull([path], ''), @isFolder = [isFolder] from deleted;

  declare @owner varchar(255);
  declare @size int;

  if @isFolder = 0
  begin
	select @owner = [owner], @size = [size] from deleted;
	declare @last int;

	if exists(select total from UsageHistory where [username] = @owner)
	begin
		select @last = [total] from UsageHistory
		where [id] = (select max(id) from UsageHistory where [username] = @owner);

		insert into UsageHistory (total, username) values (@last - @size, @owner);
	end;
  end
  else
  begin
	select @owner = [owner], @size = sum([size]) from Files
		where 
		Files.[path] like concat(@deletedPath, '.', @deletedId, '%') or
		Files.[path] like concat(@deletedId, '%')
		group by [owner];

    delete from Files
		  where 
			Files.[path] like concat(@deletedPath, '.', @deletedId, '%') or
			Files.[path] like concat(@deletedId, '%');

	if exists(select total from UsageHistory where [username] = @owner)
	begin
		select @last = [total] from UsageHistory
		where [id] = (select max(id) from UsageHistory where [username] = @owner);

		insert into UsageHistory (total, username) values (@last - @size, @owner);
	end;
  end;
end;

go

create trigger RemoveFilesOnDeletion
on Users
instead of delete
as
begin
	declare @deletedUsername varchar(255);
	select @deletedUsername = [username] from deleted;

	delete from Sharing where sharedBy = @deletedUsername;
	delete from Files where [owner] = @deletedUsername;
	delete from UsageHistory where [username] = @deletedUsername;
	delete from Users where [username] = @deletedUsername;
end;

go

create trigger CreateFileTrigger
on Files
after insert
as
begin
	declare @owner varchar(255);
	declare @size int;

	select @owner = [owner], @size = [size] from inserted;

	declare @last int;

	if not exists(select total from UsageHistory where [username] = @owner)
	begin
		insert into UsageHistory (total, username) values (@size, @owner);
	end;
	else
	begin
		select @last = [total] from UsageHistory
		where [id] = (select max(id) from UsageHistory where [username] = @owner);

		insert into UsageHistory (total, username) values (@last + @size, @owner);
	end;
end;