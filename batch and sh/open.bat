set list="start-chrome.bat"
set list=%list%;"start-git.bat"
set list=%list%;"start-mm.bat"
set list=%list%;"start-search.bat"

@echo off
for %%a in (%list%) do (
  echo %%a
  start call %%a
  TIMEOUT 5
)