@echo off

IF %USERNAME% == ACL GOTO :Laptop

%comspec% /c ""c:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\vcvarsall.bat" x86 && cl.exe %*"

GOTO :EOF

:Laptop
cls
@echo The script "cl.bat" does not work
@echo on UHD laptops at this time.
GOTO :EOF
