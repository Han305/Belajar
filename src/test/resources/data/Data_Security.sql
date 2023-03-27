insert into s_roles (id, name)
values ('r001', 'staff');

insert into s_roles (id, name)
values ('r002', 'manager');

insert into s_roles (id, name)
values ('role_new_user', 'new_user');

insert into s_users (id, username, active, id_role)
values ('u001', 'dhani', true, 'r001');

insert into s_users (id, username, active, id_role)
values ('u002', 'rayhan', true, 'r002');

-- password : 123
insert into s_users_passwords (id_user, password)
values ('u001', '$2a$12$dM071ih3Y1bjPscMJgS9VeCzqbvHq4NB8Bjay72/e8VYPiNXs45QW');

-- password : 123
insert into s_users_passwords (id_user, password)
values ('u002', '$2a$12$2/20L/yyLWhnnRAYYTdqLOHjOirybWOqr8BtWDNPQtcyQcSvObWXm');

insert into s_permissions (id, label, value)
values ('p001', 'Lihat Data Peserta', 'VIEW_PESERTA');

insert into s_permissions (id, label, value)
values ('p002', 'Edit Data Peserta', 'EDIT_PESERTA');

insert into s_roles_permissions (id_role, id_permission)
values ('r001', 'p001');

insert into s_roles_permissions (id_role, id_permission)
values ('r002', 'p001');

insert into s_roles_permissions (id_role, id_permission)
values ('r002', 'p002');
