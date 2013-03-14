@echo off
 
Set RegQry=HKLM\Hardware\Description\System\CentralProcessor\0
 
REG.exe Query %RegQry% > checkOS.txt
 
Find /i "x86" < CheckOS.txt > StringCheck.txt
 
del checkOS.txt
del StringCheck.txt

If %ERRORLEVEL% == 0 (
    Echo "This is 32 Bit Operating system"
    xcopy .\Libs\rxtxSerial_32bit.dll %programfiles%\Java\jre6\bin\
    cd %programfiles%\Java\jre6\bin
    ren rxtxSerial_64bit.dll rxtxSerial.dll
) ELSE (
    Echo "This is 64 Bit Operating system"
    xcopy /s .\Libs\rxtxSerial_64bit.dll %programfiles%\Java\jre6\bin\
    cd %programfiles%\Java\jre6\bin
    ren rxtxSerial_64bit.dll rxtxSerial.dll
)

TIMEOUT /T -1