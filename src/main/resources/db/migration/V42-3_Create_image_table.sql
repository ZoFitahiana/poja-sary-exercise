create table if not exists image
(
    id varchar
        constraint image_pk primary key
);

insert into image (id)
values ('image-1')
;