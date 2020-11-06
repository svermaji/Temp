set WS=WINSCP_HOME
set LOC=INITIAL_FOLDER
set env=%1
set srvr=%2

if %env%=="" (
    set env=USER1
)

if %srvr%=="" (
    set srvr=SERVER
)

if "%env%"=="CONDITION" (
    set usr=USER2
    set pwd=PWD2
)

"%WS%" sftp://%usr%:%pwd%;x-name=%title%@%srvr%%LOC%
