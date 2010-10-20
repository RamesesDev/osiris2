[serverdate]
select now() as serverdate

[list-queue]
select t.*, q.expirydate from task_info t inner join task_queue q 
where q.expirydate <= ? and  not exists (select taskid from task_suspended where taskid=t.taskid) 
order by dtfiled asc


[add-processing]
insert into task_processing (taskid, machineinfo, processdate) values (?,?,?)

[remove-queue]
delete from task_queue where taskid=?

[add-queue]
insert into task_queue (taskid, expirydate,dtfiled) values (?,?,now())

[remove-processing]
delete from task_processing where taskid=?
