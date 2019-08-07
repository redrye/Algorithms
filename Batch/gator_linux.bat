:: gator.uhd.edu connection script


@echo off

if %USERNAME% == ACL (putty gator.uhd.edu)
if NOT %USERNAME% == ACL (putty %USERNAME%@gator.uhd.edu)