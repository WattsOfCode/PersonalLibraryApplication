# PersonalLibraryApplication - Book Nook

Book Nook is a private digital cataloging application designed for avid readers to manage their 
physical book collections. It combines the speed of barcode scanning with the security of biometric 
locks and the reliability of offline-first cloud syncing.

##Features
- Building collections
  - Instant Scanner – Use your camera to scan a book's barcode (ISBN) for lightning-fast entry.
  - Smart Search – Find books manually by typing in a Title or Author.
  - Auto-Fill Details – Automatically fetches cover art, page counts, and book descriptions so you don't have to type them.
- Tracking your reading
  - Reading progress - you can log your last read page to pick back up later
  - Reading History - marking system allows you to lable books as "To-Read, Currently reading or finished"
- Managing your library
  - Loan Tracker - user to user syncing
  - Offline access - so you have access to your library even when your cut off from the world
  - Library Exports - csv file exporting for purposes of use in excel
- Privacy & Security
  - Biometric lock - Use Fingerprint or Face ID to unlock the app
  - Automatic Cloud Sync - collection stays backed up to the cloud automatically 
---

## Tech Stack
- **languages** xml, kotlin, java, sql
- **Database:** SQLite, Room, CloudDatabase(Google Firebase)
- **MVVM (Model-View-View-Model) Pattern**

---

## Installation

```bash
# Clone the repository
git clone https://github.com/WattsOfCode/PersonalLibraryApplication.git

# Open to the project folder
Launch Android Studio and select Open, then navigate to the cloned folder.

# Sync Gradle
Allow Android Studio to download the necessary dependencies (Gradle will do this automatically).

# Start the application
Connect an Android device via USB or start an Emulator and click the 'Run' (Green Play button) in Android Studio.
