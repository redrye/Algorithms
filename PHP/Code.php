<?php
	include("ScanDir.php");
?>

<html>

<table style="width:100%">
<tr>
	<td>Directory/File</td>
	<td>Date Modified</td>
	<td>Size</td>
</tr>
<?php
	$dir = "Code";
	$files = php4_scandir($dir);
	sort($files);
	foreach ($files as $file) {
		if($file != "index.php" && $file != ".htaccess" && $file !=".htpasswd") {
			echo "<tr><td><a href='".$file."'>$file</a></td><td>".date("F d Y H:i:s.")."</td><td>".filemtime($file)."</td><td>".filesize($file)."</td></tr>";
		}
	}

?>

</html>
