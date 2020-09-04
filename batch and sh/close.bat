set list="close-chrome.bat"
set list=%list%;"close-git.bat"
set list=%list%;"close-mm.bat"
set list=%list%;"close-search.bat"

@echo off
for %%a in (%list%) do (
  echo %%a
  start call %%a
  TIMEOUT 5
)

call bye.bat
call e