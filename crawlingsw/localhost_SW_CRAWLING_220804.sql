select *
from (
    select A.*, ROW_NUMBER() OVER(PARTITION BY email order by a.name) AS rn from tbl_sw_crawling A
    where not email = '-@-' and not email like '%@%@%@%'
)
where rn = 1;

select count(*) from tbl_sw_crawling;