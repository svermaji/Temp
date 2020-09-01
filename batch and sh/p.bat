set env=%1
set srvr=%2
set usr=USER
set pwd=PWD

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

title %usr%@%srvr%

"C:\sv\PuTTY\putty.exe" -ssh %usr%@%srvr% -pw %pwd%
