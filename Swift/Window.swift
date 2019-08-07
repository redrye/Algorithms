import Foundation
import AppKit

class Common : NSObject {
	
	

	/*! Calculates a damped max value using the previous max value, the current max value, and a base number the max should never fall below.
	 * @param previousMax: The last max value.
	 * @param currentMax: The current max value.
	 * @param baseMax: The lowest max that should be returned.
	 */
	func dampedMaxUsingPreviousMax(previousMax: CGFloat, currentMax: CGFloat, baseMax: CGFloat) -> CGFloat {
		return dampedValueUsingPreviousValue(previousValue: previousMax, currentValue: currentMax, dampingCoefficient: 0.95)
	}

	/*! Calculates a damped value using the previous value and the current value.
	 * @param previousValue: The last value.
	 * @param currentValue: The current value.
	 */
	func dampedValueUsingPreviousValue(previousValue: CGFloat, currentValue: CGFloat) -> CGFloat {
		return dampedValueUsingPreviousValue(previousValue: previousValue, currentValue: currentValue, dampingCoefficient: 0.8)
	}
	/*! Calculates a damped value using the previous value and the current value.
	 * @param previousValue: The last value.
	 * @param currentValue: The current value.
	 * @param dampingCoefficient: The coefficient to use (between 0 and 1) that will dictate how much of a factor the previous value will have in the calculation.
	 */
	func dampedValueUsingPreviousValue(previousValue: CGFloat, currentValue: CGFloat, dampingCoefficient: CGFloat) -> CGFloat {
		return (previousValue * dampingCoefficient) + (currentValue * (1 - dampingCoefficient))
	}

}

class GenericView : NSObject {
//	var moduleManager : ModuleManager!
	var parentWindow : Any!
	
	func setGraphSize(newSize: NSSize) {
		
	}
	
}


class Module : GenericView {
	
}

class DataSet : NSObject {
	var values : CGFloat!
	var numValues : size_t!
	var currentIndex : NSInteger!
	
	var min : CGFloat!
	var max : CGFloat!
	var sum : CGFloat!
	
	func initWithContentsOfOtherDataSet(otherDataSet : DataSet) -> Any {
		return otherDataSet
	}
	
	func average() -> CGFloat {
		
	}
	func currentValue() -> CGFloat {
		
	}
	func valuesInOrder(destinationArray: CGFloat) {

	}
	func resize(newNumValues: size_t) {
		
	}
	func setNextValue(nextValue: CGFloat)a {
		
	}
	func setAllValues(value: CGFloat) {
		
	}
	func addOtherDataSetValues(otherDataSet: DataSet) {
		
	}
	func subtractOtherDataSetValues(otherDataSet: DataSet) {
			
	}
	func divideAllValuesBy(dividend: CGFloat) {
			
	}
}



class AppDelegate: NSObject, NSApplicationDelegate {
	let window : NSWindow = NSWindow(
		contentRect: NSMakeRect(
			0,
			0,
			NSScreen.main!.frame.maxX/4.5,
			NSScreen.main!.frame.maxY
		),
		styleMask: [.titled, .closable],
		backing: .buffered,
		defer: false,
		screen: nil
	)

	func applicationDidFinishLaunching(_ notification: Notification) {
		window.makeKeyAndOrderFront(nil)
		DispatchQueue(label: "background").async {
			while let str = readLine(strippingNewline: false) {
				DispatchQueue.main.async {
							//field.textStorage?.append(NSAttributedString(string: str))
				}
			}
			NSApplication.shared.terminate(self)
		}
	}
}

