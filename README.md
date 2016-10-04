# NFC Activity Monitor
A Xposed Framework module to help you monitor what third-party applications are using your NFC for.

## Getting Started
### Prerequisities
This is a module for the Xposed Framework. In order to use it, you must root your device, and install the [Xposed Framework](http://repo.xposed.info/)

### Installing
To use this module, you must clone this project using Android Studio, and run it.

You will get an error, since this application doesn't have an activity, but your module should now be listed on the Xposed app. Active it, and reboot your phone.

## Usage
### How does it work?
Everytime an application reads a RFID tag using your NFC, the module will dump the information it requested both on the Xposed logs as well the ADB.

### How is it useful?
It tells you what an application did or what information is passing from A to B. 
Let's say you use your phone for payments. Using this module, you will be able to check which information is being requested and passed from your phone when you made a payment.

## LICENSE
```
The MIT License (MIT)

Copyright (c) 2016 Victor Santiago

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
