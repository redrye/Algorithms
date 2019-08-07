<?php
/*A Monte Carlo Simulation is a way of approximating the value of a function where calculating the actual value is difficult or impossible. 
It uses random sampling to define constraints on the value and then makes a sort of "best guess." */
	$loop = 1000000; # loop to 1,000,000
	$count = 0;
	for ($i=0; $i<$loop; $i++) {
		$x = rand() / getrandmax();
		$y = rand() / getrandmax();
		if(($x*$x) + ($y*$y)<=1) $count++;
	}
	echo "loop=".number_format($loop).", count=".number_format($count).", pi=".($count/$loop*4);
?>