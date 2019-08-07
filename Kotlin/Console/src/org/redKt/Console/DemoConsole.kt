/**
 * Copyright (C) 2012 Ben Navetta <ben.navetta></ben.navetta>@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Welcome to the DEMO! Want to know how to get started with these classes
 * quickly and easily? You're in the right place! A comprehensive guide below
 * covers a lot of helpful tips when making a console-based application.
 * It could still use some improvement, though, and of course each application
 * will need to be custom built to the application's specialized needs.
 */

import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.io.File
import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap

import javax.imageio.ImageIO
import javax.swing.JFrame

object DemoConsole {
    private val CONSOLE_NAME = "Console Demo"      // This is your console's name, and will
    // show up in the upper lefthand corner of
    // the window.

    private val ICON_IMAGE_FILE = "SomeImage.png"  // The filepath to the icon for the window goes here.

    private val BACKGROUND_COLOR = Color.BLACK      // The background color
    private val FOREGROUND_COLOR = Color.GREEN      // The text color

    private val commandMap = HashMap<String, InputProcessor>(10) // A hashmap to store commands and triggers.


    /**
     * @param args
     */
    @JvmStatic
    fun main(args: Array<String>) {

        //STEP 1: Initialize and define all commands, in the form of InputProcessors.

        val clearScreen = object : InputProcessor() {
            fun process(args: Array<String>, console: Console) {
                console.cls()
            }
        }

        val terminateProgram = object : InputProcessor() {
            fun process(args: Array<String>, console: Console) {
                System.exit(0)
            }
        }

        val echo = object : InputProcessor() {
            fun process(args: Array<String>, console: Console) {
                console.write(args[1]) // only echos the first word...
            }
        }

        val IDontUnderstand = object : InputProcessor() {
            fun process(args: Array<String>, console: Console) {
                console.write("Sorry, I don't understand that command")
            }
        }

        // STEP 2: Link all of these command codes to a one-word String command:

        commandMap.put("cls", clearScreen)          //String command does not need to match variable name from above

        commandMap.put("close", terminateProgram)
        commandMap.put("exit", terminateProgram)    //Multiple strings can be used for the same command, but multiple
        //commands may not be referenced by the same string.

        commandMap.put(
            "echo",
            echo
        )                //String command COULD be the same as the variable name, if you want.

        commandMap.put("help", IDontUnderstand)
        // STEP 3: Initialize the JFrame:

        val frame = JFrame(CONSOLE_NAME)
        try {
            frame.setIconImage(ImageIO.read(File(ICON_IMAGE_FILE)))
        } catch (e: IOException) {
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.setSize(
            677,
            343
        )                    // Official Windows Command Prompt size, looks beautiful and can be resized
        // after being opened if you so desire.

        //Init console
        val console = Console(
            Color.BLACK, Color.GREEN,
            Font(Font.MONOSPACED, Font.BOLD, 14), "$ "
        )
        console.setPreferredSize(Dimension(677, 343)) // Same as above

        console.setCompletionSource(
            DefaultCompletionSource(
                "help",
                "echo",
                "cls",
                "close",
                "exit"
            )
        ) // String commands go here as well.

        console.setProcessor(object :
            InputProcessor() { // This processor breaks a statement into args and passes them to the matching
            // command defined in the hashmap above (the part in step 2)
            private var requests = 0

            fun process(args: Array<String>, console: Console) {
                //1. Print for debugging:
                println("Got Req. " + ++requests + ": '" + args[0] + "'")

                println("asked: " + Arrays.toString(args))
                //4. Process list of arguments
                if (args.size > 0 && commandMap.containsKey(args[0].toLowerCase()))
                    commandMap.get(args[0].toLowerCase()).process(args, console)
                else
                    commandMap.get("help").process(args, console)
            }
        })
        frame.add(console)
        frame.addComponentListener(console)
        frame.pack()
        console.setScreenHeight(frame.getContentPane().getSize().getHeight().toInt())
        frame.setVisible(true)
    }

    fun removeQuotes(arg: String): String { //Param: a quote.
        return arg.substring(1, arg.length - 1)
    }

}
