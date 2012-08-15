select fl.pin, fl.cadastrallotno, fl.blockno, fl.surveyno 
from faaslist fl 
where fl.docstate <> 'CANCELLED' and fl.pin like '%' 
group by fl.pin 