### Version History

#### v1.0 (unreleased)
- [GEN] Convert project to maven project
- [GEN] Fix deprecations and data loading from resources
- [GEN] Upgraded dependencies:
  - beanfabrics to 1.1.0
  - libphonenumber to 6.0
  - geocoder to 2.9
  - commons-lang3 to 3.3.1

#### v0.9.1 (unreleased)
- [GUI] Added main application icon
- [GEN] Fixed a bug where message rendering got stuck
- [GEN] Fixed a bug which would crash program on missing schema file
- [GEN] Upgraded beanfabrics to 1.0.0

#### v0.9 (Dec 18, 2012)
- [GUI] New GUI implementation - now using beanfabrics for everything, closes #2
- [GUI] More multithreading and visual feedback
- [GUI] Further GUI code refactoring towards #2
- [GEN] Added import validation for sending date field in messages (only acceptable on sent/outgoing messages)
- [GEN] Fixed bug #1 where an empty output file got created
- [GEN] Added LGPL license, closes #3
- [GEN] Major code restructuring

#### v0.8 (Dec 14, 2012)
- [GUI] Improved status message output to clearly show number of imported messages and duplicates
- [GUI] Added visible duplicate count in message headers
- [GUI] Additional export settings (export duplicates, export international numbers)
- [GUI] Added status bar
- [GUI] Threaded execution (non-blocking import, etc.)
- [GUI] Adapted GUI to presentation model (PM) pattern using [beanfabrics][beanfabrics]
- [GEN] Fixed occasional message sorting problems
- [GEN] Improved duplication handling
- [GEN] New internal data structures, message handling and message conversion architecture to make support for different external file formats possible
- [GEN] Improved handling of the currently selected Locale
- [GEN] More test cases and fixtures

#### v0.7 (Dec 8, 2012)
- [GUI] Possibility to change locale
- [GUI] Persistent setting storage
- [GUI] Fixed incorrect height of message content labels after line wraps
- [GEN] Auto-detect system locale
- [GEN] General bug fixes and performance improvements

#### v0.6 (Dec 6, 2012) - Initial Version
- [GUI] Display of messages and conversation threads
- [GUI] Display of contacts and geo information based on their phone numbers
- [GUI] Import and export (XML)
- [GEN] Number parsing and offline geocoding through [libphonenumber][libphonenumber] library
- [GEN] Duplicate filtering

[libphonenumber]: http://code.google.com/p/libphonenumber
[beanfabrics]: http://code.google.com/p/beanfabrics
