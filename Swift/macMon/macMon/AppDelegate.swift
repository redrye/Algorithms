//
//  AppDelegate.swift
//  macMon
//
//  Created by user on 5/21/19.
//

import Cocoa

@NSApplicationMain
class AppDelegate: NSObject, NSApplicationDelegate {
    let window = NSWindow(contentRect: NSMakeRect(200, 200, 400, 200),
                          styleMask: [.titled, .closable],
                          backing: .buffered,
                          defer: false,
                          screen: nil)
    func applicationDidFinishLaunching(_ notification: Notification) {
        window.makeKeyAndOrderFront(nil)
        let field = NSTextView(frame: window.contentView!.bounds)
        field.backgroundColor = .black
        field.isContinuousSpellCheckingEnabled = true
            
        window.setFrameOrigin(NSPoint(x: 0, y:800))
        window.contentView?.addSubview(field)
        DispatchQueue(label: "background").async {
            while let str = readLine(strippingNewline: false) {
                DispatchQueue.main.async {
                    field.textStorage?.append(NSAttributedString(string: str))
                }
            }
        }
    }
    


    func applicationWillTerminate(_ aNotification: Notification) {
        // Insert code here to tear down your application
    }


}

