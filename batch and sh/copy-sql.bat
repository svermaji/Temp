set ^"LF=^
%=This creates line feed char =%
"

set L1=def nm='sv-%%';
set L2=select * from table where name like '^^^&nm';

(echo %%L1%%%%LF%%echo %%L2%%%%LF%%) |clip
