	<?php
	function php4_scandir($dir, $listDir = true, $skipDots = false) {
		$dirArray = array();
		if ($handle = opendir($dir)) {
			while (false !== ($file = readdir($handle))) {
				if (($file != "." && $file != "..") || $skipDots == true) {
					if ($listDir == false) {
						if (is_dir($file)) {
							continue;
						}
					}
					array_push($dirArray, basename($file));
					}
			}
			closedir($handle);
		}
		return $dirArray;
	}
	$dir = './';
	?>
	
<?php
	$files = php4_scandir($dir);
	$rowCount = 0;
	echo '<link href="../../css/style.css" rel="stylesheet">';
	echo "<div class='container-fluid'>";
	echo "<div class='row'>";
	
	sort($files);
	foreach ($files as $file) {

		if($file != "index.php") {
						$lines = fopen($file, "r");
						echo "<div class = 'col-sm-auto col-md-auto col-lg-auto'>";
						echo "<div class = 'card'>";
						echo "<div class = 'card-header'>".$file."</div>";
						echo "<div class = 'card-body' style='font-size:11'>";
			$line= file_get_contents($file);
			echo "<pre>".htmlspecialchars($line)."</pre>";					
			echo "</div>";
			echo "</div>";
			echo "</div>";
		}
//		$numOfRows++;
//		if(numOfRows % 3 == 0) {
//			echo "</div><div class='row'>";
	}
	echo "</div>";
	echo "</div>";
?>
