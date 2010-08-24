[getInfo]
select 
        b.*,
        IFNULL((select intcorporate from mlkp.tblorganization where objid=b.strorganizationid),0) as intcorporate 
from mlkp.tblbranch b 
where objid=$P{objid}  


[verifyBranchUserTerminal] 
select * from mlkp.tbluserterminal 
where strbranchid=$P{branchid} and struserid=$P{userid} and strterminalid=$P{terminalid} 

[checkStatus]
SELECT IFNULL(state,0) as state FROM mlkp.tblbranch WHERE objid=$P{objid}