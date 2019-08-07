//
//  NSGraph.swift
//  macMon
//
//  Created by user on 5/21/19.
//

import Cocoa
import IOKit

class NSGraph: NSWindow {
    
    // Boarder Width
    var borderWidth: Int!
    
    // Minimized?
    var minimized: Bool!
    
    // Timers
    var minTimer: Timer!
    var graphTimer: Timer!
    var fastTimer: Timer!
    
    // Settings
    var fontManager: NSFontManager!
    
    // Outlets
    @IBOutlet var preferenceWindow: Any!
    @IBOutlet var controler: AppDelegate!
    
    
    // Views
    // var appSettings: appSettings!

    var draggingWindow: Bool!
    var originAtDragStart: NSPoint!
    var dragStart: NSPoint!
    
    // Initialization
    func initialize() {
        
    }
    func getDefaultPrefs()->NSMutableDictionary {
        
        var appDefaults: NSMutableDictionary!
        
        appDefaults[bgTransparancy] = "0.9"
        
        return appDefaults;
        
    }
    
}
