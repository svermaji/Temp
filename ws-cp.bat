set env=%1
set srvr=%2
set file=%3
set usr=USR
set pwd=PWD
set src=SRC\
set dest=DEST

if %env%=="" (
    set env=dit2
)

if %srvr%=="" (
    set srvr=4201
)

if "%env%"=="dit1" (
    set usr=USR
    set pwd=PWD
)


if "%file%"=="sen" (
    set src=%src%x.jar
    set dest=%dest% /
)

echo %time%
\WinSCP\WinSCP.exe %usr%:%pwd%@ %srvr% /command "put %src% %dest%"
echo %time%
