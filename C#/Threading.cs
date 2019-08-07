using System;
using System.Threading;

class MultithreadingApplication {
	static void Main(string[] args) {
		Thread th = Thread.CurrentThread;
		th.Name = "MainThread";
			
		Console.WriteLine("This is {0}", th.Name);
	}
}
