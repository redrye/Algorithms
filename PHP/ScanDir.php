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