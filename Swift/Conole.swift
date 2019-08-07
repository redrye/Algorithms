import Cocoa
import Foundation

class ViewController: NSViewControler {
	@IBOutlet weak var textTimePeriod: NSTextField!
	@IBOutlet weak var popupTimeUnits: NSPopUpButton!
	@IBOutlet weak var cmdOut: NSTextField!
	@IBOutlet weak var resultText: NSScrollView!
	@IBOutlet var rText: NSTextView!
	
	override func viewDidLoad() {
		super.viewDidLoad()
		popupTimeUnits.removeAllItems()
		popupTimeUnits.addItems(withTitles: timeUnitsList)
		timeSelStr = (popupTimeUnits.selectedItem?.title)!
	}
	override var representedObject: Any? {
		didSet{
			
		}
	}
	let task = Process.launchedProcess(launchPath:"/opt/local/bin/bash", arguments:["--login", "-i"])
task.waitUntilExit()

}