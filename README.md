# PersonalLibraryApplication - Book Nook

##Project Summary: BookNookV2
Project Overview
BookNook is a comprehensive mobile library management application designed for book enthusiasts to catalog, organize, and secure their personal collections. The app leverages modern Android development practices, including MVVM architecture, hardware-level security, and external API integrations, to provide a seamless user experience for managing physical and digital libraries.

##Key Changes Since Week 4
Since the initial prototype, the application has undergone significant structural and functional enhancements:
•	Database Refactoring: Transitioned from basic persistence to a relational Room SQLite database, implementing foreign key constraints between Users, Books, and Bookcases.
•	Advanced Query Logic: Implemented a complex LEFT JOIN search engine, allowing users to filter their collection not only by title and author but by specific "Shelf" or "Bookcase" names.
•	Architecture Update: Fully migrated to MVVM (Model-View-ViewModel) with LiveData and Coroutines to ensure thread safety and real-time UI updates.
•	Enhanced Connectivity: Established a robust navigation flow between the Authentication (Login/Register), Dashboard (Main), and Library (Summary/Search) activities.

##Security Measures
Security was a primary focus for this release, moving beyond basic password requirements to protect user data:
•	Biometric Authentication: Integrated the Android BiometricPrompt API, allowing for hardware-encrypted login via Fingerprint or Facial Recognition [01:25].
•	Credential Hashing: Implemented stable hashing algorithms to ensure that user passwords are never stored in plain text within the local database.
•	Persistent Session Management: Utilized encrypted SharedPreferences to securely cache the last authenticated user, bridging the gap between hardware biometrics and the Room database backend.
•	Input Sanitization: Added strict validation logic to all entry forms to prevent malformed data and enhance database integrity.

##Cosmetic & UI Improvements
•	Dynamic Dashboard: Redesigned the main landing page to include "Quick Stats" that update in real-time as users add or remove books.
•	Material Design Integration: Implemented custom styling, button behaviors (including selectableItemBackgroundBorderless), and specialized ImageButtons for a modern, professional look.
•	Responsive Layouts: Utilized ConstraintLayout throughout all activities to ensure UI consistency across various screen sizes and orientations.

##Project Demonstration
A professional walkthrough of the code and features is available below:
•	Demo Video: Watch the BookNookV2 Demonstration
##Technical Skills Learned
•	Android Development: Kotlin, Jetpack Compose/XML, Activity Lifecycle Management.
•	Data Science: Relational SQLite mapping, Room DAO patterns, and SQL Joins.
•	Security: Biometric API integration and secure data persistence.
•	Tooling: Git/GitHub for version control and ML Kit for barcode vision.

---

Links

GitHub Repository: https://github.com/WattsOfCode/PersonalLibraryApplication

Video link: https://youtu.be/6Q5-MfEQdis

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
