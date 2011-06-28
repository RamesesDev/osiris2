[list-active-queue]
select * from task_info t 
inner join task_queue tq on t.taskid = tq.taskid 
where t.nextdate < now() 
and not exists (select taskid from task_suspended where taskid=t.taskid) 
and t.allowedhost is null or t.allowedhost like '%${host}%' 
 
[add-to-processing]
insert into task_processing (taskid, machineid, processhost, processdate) values ( ?,?,?, now() )

[remove-queue]
delete from task_queue where taskid = ?

[remove-processing]
delete from task_processing where taskid=?

[add-queue]
insert into task_queue (taskid) values (?)

[update-next-date]
update task_info set nextdate=? where taskid=?

[add-error]
insert into task_error (taskid, msg) values (?,?) 

[add-task]
insert into task_info (taskid,scriptname,method,startdate,enddate,duration,parameters,allowedhost,nextdate) 
values ($P{taskid}, $P{scriptname}, $P{method},$P{startdate},$P{enddate},$P{duration},$P{parameters},$P{allowedhost},$P{startdate}) 

[start-task]
insert into task_queue (taskid, expirydate, dtfiled)  
select t.taskid, t.startdate, now() from task_info t where t.taskid = ?   

[suspend-task]
insert into task_suspended (taskid) values (?) 

[resume-task]
delete from task_suspended where taskid=?

[update-taskid]
update task_taskid set taskid=taskid+1

[init-taskid]
insert into task_taskid select 1

[get-taskid]
select taskid from task_taskid



