# Gorilla Type

### <ins>What will this application do?<ins>
Gorilla Type will be a typing application which will help the user learn to type faster. My main goal with this project
is to develop a functioning program that can produce text for the user to type, log what the user types, and store that
log in a log list so that the user can look back at their history and view their progress. By providing a settings
function and an account function, users will be able to save their data and customize their experience.

### <ins>Why did I chose this application?<ins>
I chose this project because I used to be quite slow at typing until I discovered programs such as "typeracer" which 
helped me type much faster. I also frequently played "typeracer" with my friends all throughout
highschool and even during university as well. I beleive that the challenges and problems I will face during this
project will be beneficial for me in the long run as I create more challenging and sophisticated programs.

### <ins>Who is this application made for?<ins>
This program will be made for anyone, whether you just want to type faster, type for fun, or test your typing speed.

### <ins>User Stories:<ins>
- As a user, I want to be able to create an account
- As a user, I want to be able to login to my account
- As a user, I want to be able to generate new text to practice with
- As a user, I want to be able to see my previous practice logs to see my improvement
- As a user, I want to be able to see my words per minute
- As a user, I want to be able to customize how new text generates
- As a user, I want all my user data to be saved when the program ends
- As a user, I want to be able to manually save my data
- As a user, I want the program to remember that I'm logged in so I don't have to login each time I open up the app
- As a user, I wanto be able to see events that take place.

### <ins>Phase 4: Task 2<ins>
Thu Aug 10 01:54:46 PDT 2023
New account was signed up
Thu Aug 10 01:54:46 PDT 2023
AccountUtils data was saved.
Thu Aug 10 01:54:46 PDT 2023
Account was logged in.
Thu Aug 10 01:54:46 PDT 2023
New prompt generated
Thu Aug 10 01:54:49 PDT 2023
Log added to logged in account
Thu Aug 10 01:54:49 PDT 2023
New prompt generated
Thu Aug 10 01:54:50 PDT 2023
Log added to logged in account
Thu Aug 10 01:54:50 PDT 2023
New prompt generated
Thu Aug 10 01:54:52 PDT 2023
Log added to logged in account
Thu Aug 10 01:54:52 PDT 2023
New prompt generated
Thu Aug 10 01:55:01 PDT 2023
Logs filtered and displayed.
Thu Aug 10 01:55:04 PDT 2023
AccountUtils data was saved.
Thu Aug 10 01:55:07 PDT 2023
Logged in account logged out.
Thu Aug 10 01:55:07 PDT 2023
AccountUtils data was saved.
Thu Aug 10 01:55:10 PDT 2023
Account was logged in.
Thu Aug 10 01:55:10 PDT 2023
AccountUtils data was saved.
Thu Aug 10 01:55:10 PDT 2023
Account was logged in.
Thu Aug 10 01:55:10 PDT 2023
New prompt generated
Thu Aug 10 01:55:14 PDT 2023

### Phase 4: Task 3
If I had more time I could definitly lower coupling in my model by using a singleton for the TypingTestUtils.
Since that object can be used for all accounts created. I could then also remove the boolean expression in the
TypingTestUtils contstructor which indicated wether or not to parse the practice words and sentences. Using a singlton
would reduce the amount of load of the cpu running the program as we would only need to instantiate the object once
since parsing a list of 2000+ words would probably be taxing on the cpu. I also have some code in the UI that should
also be refactored and moved to the model. For example, I handled the backupAccount object in the UI. Had I done it in
the model, perhaps in the AccountUtils class, the UI would've been much simpler to write. I would also make my UI nicer
if I had more time.