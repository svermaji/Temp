echo off

set mvnjar=%1
set src=%2
set dest=%3

echo "ren [%src%], [%dest%]"

cd %src%
del %dest%
ren %mvnjar% %dest%

cd ..
