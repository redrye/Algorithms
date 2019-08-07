clear
echo "Setting System Properties"
echo "Setting Explorer Visual Effects"
Push-Location 
Set-Location HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\VisualEffects
Set-ItemProperty . VisualFXSetting -type Dword -value '3'
Set-ItemProperty . DragFullWindows -type Dword -value '1' 
Pop-Location
echo ""
echo "Complete"

echo "Setting System Colors"
Push-Location 
Set-Location "HKCU:\Control Panel\Desktop\Colors"
Set-ItemProperty . ActiveBorder -type String -value '0 0 0'
Set-ItemProperty . ActiveTitle -type String -value '255255255'
Set-ItemProperty . AppWorkspace -type String -value '255255255'
Set-ItemProperty . Menu -type String -value '000000255'
Pop-Location
echo ""
echo "Complete"

echo "Setting DWM Settings"
Push-Location 
Set-Location "HKCU:\Software\Microsoft\Windows\DWM\"
Set-ItemProperty . EnableAeroPeek -type Dword -value '0'
Pop-Location
echo ""
echo "Complete"

Push-Location 
Set-Location HKCU:\Software\Microsoft\Windows\CurrentVersion\ThemeManager
Set-ItemProperty . ThemeActive -type String -value '1' 
Pop-Location

Push-Location 
Set-Location HKCU:\Software\Microsoft\Windows\CurrentVersion\Explorer\Advanced
Set-ItemProperty . WebView -type Dword -value '00000001'
Set-ItemProperty . ListViewAlphaSelect -type Dword -value '00000000'
Set-ItemProperty . ListViewShadow -type Dword -value '00000000'
Set-ItemProperty . TaskbarAnimations -type Dword -value '00000000'
Set-ItemProperty . TaskbarSmallIcons -type Dword -value '00000001'
Pop-Location

Push-Location 
Set-Location "HKCU:\Control Panel\Desktop\WindowMetrics"
Set-ItemProperty . MinAnimate -type String -value '0' 
Pop-Location


#Update the Parameters and restart Explorer
RunDll32 USER32.DLL,UpdatePerUserSystemParameters
taskkill /f /im explorer.exe
explorer
echo ""
echo "Complete"

