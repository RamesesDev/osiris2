[script]
select content from res_script where name=?

[schema]
select content from res_schema where name=?

[sql]
select content from res_sql where name=?

[interceptors]
select name from res_script where category='interceptor'

[datasources]
select name, content from res_ds where appcontext=?