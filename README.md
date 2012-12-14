sms-backup-manager
==================

SMS Backup Manager helps you to manage your SMS backups. It offers functionalities to view your messages, merge multiple backup files, remove duplicates and export all messages or individual conversations into a new backup file.

Supported file formats:
- XML files generated by [SMS Backup &amp; Restore][sms-backup-restore].
- more to come..

### Usage
Please refer to the [HowTo][wiki-howto] page of the Wiki.

### Developers
Dependencies:
- libphonenumber 5.2 - [get it here][libphonenumber]
- offline-geocoder 2.3 - [get it here][libphonenumber]
- commons-lang 3.1 - [get it here][commons-lang]
- beanfabrics-core and beanfabrics-swing 0.9.4 - [get it here][beanfabrics]

### Version History

#### v0.9 (upcoming)
- [GUI] Further GUI code refactoring

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

[sms-backup-restore]: http://android.riteshsahu.com/apps/sms-backup-restore
[wiki-howto]: https://github.com/fkleon/sms-backup-manager/wiki/HowTo
[libphonenumber]: http://code.google.com/p/libphonenumber
[commons-lang]: http://commons.apache.org/lang
[beanfabrics]: http://code.google.com/p/beanfabrics
