[getCustomer]

select name from tblcustomer 
where name = ?

[updateCustomer]

update tblcustomer set name=?
where name = ?


#deletes the customer

[deleteCustomer]

delete tblcustomer
where name = ?
