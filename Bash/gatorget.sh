#!/bin/bash

read -p "Enter the directory to downoad : " folder;
usernme=$(sed '1q;d' $HOME/UHD_Toolkit/user.txt);
pass=$(sed '2q;d' $HOME/UHD_Toolkit/user.txt);
wget -r -nH -np -nc --user=$usernme --password=$pass --no-passive-ftp ftp://gator.uhd.edu/$folder