# PoliticsLive
CSC 4998 Mobile Apps- Final Project

APP BEHAIVOR:
- The launching activity is MainActivity.
- If user has never opened app before, MainActivity redirects to LoginActivity to initialize polls.
- If app is already initiliazed, MainActivity loads the HomePage fragment, so this is the first page the user sees
- HomePage Fragment contains an overview of all of the app's information.  User can expand on this by A) clicking the title above each cardview, which will open an Activity with all of the objects related to that database, or B) User can directly click on an object from the card view which opens up an Activity populated with all that objects information (populated using class adapters)

DATABASES:
- There are 5 active local SQL database: Events, Candidates, Polls, ExitPolls, PoliticalParties
- The objects stored in the database have the following structure...class object, class datasource, dbhelper, and adapter (to populate views)


TODO ITEMS (Prioritized Order):
- App Notifications for Events (5 total right now), display the name, time, and channel of the event ON THE DATE OF THE EVENT.
- Allow user to turn on and off event notifications from the AppSettings Activity (Default behaivor = ON)
- Migrate all SQL data onto a hosted solution (TRY FIREBASE)
- Using Firebase, reimplement existing social features including User Profile, Vote Counts for each candidate, Ballot, Login, SignUp, Logout, etc.

BUGS:
- If the user exits the app during initialization, and then reopens app, then the initialization will not be successful (it will finish, but the database will have duplicate objects, wont show graphs)
