#!/bin/bash

usernme=$(sed '1q;d' $HOME/UHD_Toolkit/user.txt);
pass=$(sed '2q;d' $HOME/UHD_Toolkit/user.txt);
explorer ftp://$usernme:$pass@gator.uhd.edu/