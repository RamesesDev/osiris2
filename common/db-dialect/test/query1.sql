 SELECT 
  p.objid, p.lastname, p.firstname, p.middlename,
  j.title AS jobtitle, 
  p.lastname + ', ' + p.firstname + ' ' + isnull(p.middlename,'') as name,
  (
    p.firstname + ' ' +
	(case isnull(p.middlename,'') when '' then '' else p.middlename + ' ' end) +
	p.lastname
  ) as formalname
 FROM personnel p
 INNER JOIN jobposition j ON j.assigneeid = p.objid
 WHERE j.objid IN (
          SELECT jobid FROM jobposition_tag WHERE tagid = $P{tagid}
       )
 ORDER BY name