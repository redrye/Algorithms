@echo off

IF %USERNAME% == ACL GOTO :Laptop

bash gatorftp.sh

GOTO :EOF

:Laptop
cls
@echo The Gatorftp script does not work
@echo on UHD laptops at this time.
GOTO :EOF