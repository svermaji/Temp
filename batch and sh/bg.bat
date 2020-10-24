set bg=%1

if %bg%=="" (
    set bg=1
)

powershell (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1,%bg%)