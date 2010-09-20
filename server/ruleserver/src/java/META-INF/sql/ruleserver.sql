[list-rulenames]
select name from rule_sets

[list-by-type]
select packagename,content from rule_content where ruleset=$P{name} and type=$P{type} order by orderindex

