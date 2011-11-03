[load-tasks]
insert into sys_task_active 
select t.id, $P{host} from sys_task t where 
(t.allowedhost is null or t.allowedhost = $P{host}) 

[unload-tasks]
delete from sys_task_active where host=$P{host}

[load-pending-tasks]
select 
  t.*, 
  IFNULL( (select 1 from sys_task_suspended p where p.id=t.id), 0 ) AS suspended, 
  IFNULL( (select message from sys_task_error p where p.id=t.id), null ) AS error 
from sys_task t 
inner join  sys_task_active ta 
on t.id = ta.id 
where ta.host=$P{host}


[add-task]
insert into gazeebu_classroom.sys_task 
(id, service, servicetype, method, startdate, enddate, nextdate, interval, parameters, host, appcontext)
values
($P{id}, $P{service}, $P{servicetype}, $P{method}, $P{startdate}, $P{enddate}, $P{nextdate},
 $P{interval}, $P{parameters}, $P{host}, $P{appcontext})

[suspend]
insert into sys_task_suspended (id) values (?)

[resume]
delete from sys_task_suspended where id=?

[update-next-date]
update sys_task set currentdate=$P{currentdate} where id=$P{id}

[log-error]
insert into sys_task_error (id, messsage) values ( $P{id}, $P{message})


