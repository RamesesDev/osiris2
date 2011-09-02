[add-cluster]
insert ignore into sys_rms_cluster (host) values (?)
 
[list-clusters]
select host from sys_rms_cluster where not(host=?)

[remove-cluster]
delete from sys_rms_cluster where host=?

[fetch-messages]
select m.msg,m.dtfiled,m.sender,s.channel,m.position,s.position as readposition 
from sys_rms_message m inner join sys_rms_subscriber s 
on s.channel = m.channel 
where s.subscriber=$P{subscriber} and m.position > s.position 

[update-subscriber-position]
update sys_rms_subscriber set position = $P{newposition} where subscriber=$P{subscriber} AND channel=$P{channel}

[increment-channel-position]
update sys_rms_channel set position=position+1 where channel=$P{channel}

[insert-message]
insert into sys_rms_message (channel,position,dtfiled,sender,msg) values  
($P{channel}, (SELECT position from sys_rms_channel where channel=$P{channel}), NOW(), $P{sender}, $P{message}) 

[get-channel-subscribers]
select subscriber from sys_rms_subscriber where channel=$P{channel}
