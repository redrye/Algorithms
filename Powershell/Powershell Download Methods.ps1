$url = "http://archive.kali.org/kali-images/kali-2017.1/kali-linux-light-2017.1-amd64.iso"
$output = "$PSScriptRoot\kali-linux-light-2017.1-amd64.iso"
$Start_Time = Get-Date


## Downloading Methods


## Invoke-WebRequest Method

#Invoke-WebRequest -Uri $url -OutFile $output

## System.Net.WebClient Method

#$wc = New-Object System.Net.Webclient
#$wc.DownloadFile($url, $output)

# OR - These are diferent method of doint the same thing
(New-Object System.Net.Webclient).DownloadFile($url, $output)

## Start-BitsTransfer Method

#Import-Module BitsTransfer
#Start-BitsTransfer -Source $url -Destination $output

#OR

#Start-BitsTransfer -Source $url -Destination $output -Asynchronous

#Write-Output "Time taken for System.Net.Webclient Method was: $((Get-Date).Subtract($Start_Time).Seconds) seconds)" 
