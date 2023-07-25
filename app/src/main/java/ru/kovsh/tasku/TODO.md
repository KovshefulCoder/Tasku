# TODO list for Task
## Auth:
### Screen features:
[✓] 1. Add a password validation  
[✓] 2. Add a password visibility toggle  
[✓] 3. Add a email validation  
[✓] 4. Password and repeated password should be the same  
[ ] 5. Handle requests and errors in dialogs  
[ ] 6. Loading status of signin and add to dialog of email confirm 
[ ] 7. Fix validation minor bugs and test all possible scenarios
[ ] 8. Handle back button from bottom bar  
[ ] 9. Make main screen of auth with Modifier.weight() and fix weights bugs in NewPasswordView 
[ ] 10. Animate screen in forgot password screen to be able to press button from keyboard. All related to keyboard animations
[ ] 11. Come up with a way to validate email and password between singin and signup switch (repeatedPassword initially Invalid)

Optional:  
[ ] 1. Add animation of switches between screens  

### Refactoring
[ ] 1. Shared interface for validation in VM
[ ] 2. Shared interface for requests handling in VM
[ ] 3. Shared interface for dialog in View


Known bugs:
## FAB
[✓] 1. If your drop FAB higher than area screen and some of them are not visible and higher thant divider - that leads to crash because focus registered is destroyed
[✓] 2. Can`t add fab to last element of list
[✓] 3. Adding areas to the between position may cause a 500 error.
[✓] 4. Errors with 5 and 9 if fab is on the bottom