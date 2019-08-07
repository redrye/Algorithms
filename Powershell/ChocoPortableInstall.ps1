# Set directory for installation - Chocolatey does not lock
# down the directory if not the default
remove-item alias:wget
remove-item alias:ls

$InstallDir="$env:USERPROFILE\Desktop\Choco"
$env:ChocolateyInstall="$InstallDir"

# If your PowerShell Execution policy is restrictive, you may
# not be able to get around that. Try setting your session to
# Bypass.
# Set-ExecutionPolicy Bypass

# All install options - offline, proxy, etc at
# https://chocolatey.org/install
iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
# PowerShell 3+?
#iwr https://chocolatey.org/install.ps1 -UseBasicParsing | iex

#choco install puppet-agent.portable -y
choco install ruby.portable -y
choco install wget -y
choco install gnuwin32-coreutils.portable -y
choco install phantomjs -y
# pick an editor
#choco install visualstudiocode.portable -y # not yet available
#choco install notepadplusplus.commandline -y
#choco install nano -y
#choco install vim-tux.portable

# What else can I install without admin rights?
# https://chocolatey.org/packages?q=id%3Aportable
