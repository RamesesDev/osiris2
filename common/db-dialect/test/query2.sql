select fl.pin, fl.cadastrallotno, fl.blockno, fl.surveyno 
from faaslist fl  
where fl.docstate <> 'CANCELLED' 
group by fl.pin  