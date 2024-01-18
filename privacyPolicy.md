## Щаслива лапа(Happy Paw): Privacy policy

Welcome to the Щаслива лапа(Happy Paw) app for Android!

This is an open source Android app developed by Dmytro Kmetsynskyi. The source code is available on GitHub under the GNU GPL license

As an Android user myself, I take privacy very seriously. I know how irritating it is when apps collect your data without your knowledge.

I declare that, to the best of my knowledge, I have not programmed this application to collect any personal information. All the data provided (mail and password to the program account) created by you (the user) are stored only on the Firebase server (Firestore Databse) and can be easily deleted by contacting me.

### Explanation of permissions requested in the app

The list of permissions required by the app can be found in the `AndroidManifest.xml` file:

https://github.com/DmytroKmetsynskyi/NedoSocialNetwork-HappyPaw/blob/main/app/src/main/AndroidManifest.xml

<br/>

|                                      Permission                                       | Why it is required |
|:-------------------------------------------------------------------------------------:| --- |
|                             `android.permission.INTERNET`                             | To communicate with Firebase |
| `android.permission.ACCESS_COARSE_LOCATION`/`android.permission.ACCESS_FINE_LOCATION` | For the user to mark the current location of the lost (stray) animal |
|                             `android.permission.WRITE_EXTERNAL_STORAGE`                             | So that the user can choose a photo of the lost (stray) animal |

 <hr style="border:1px solid gray">

If you find any security vulnerability that has been inadvertently caused by me, or have any question regarding how the app protectes your privacy, please send me an email or post a discussion on GitHub, and I will surely try to fix it/help you.

Yours sincerely,  
Dmytro Kmetsynskyi.  
Lutsk, Ukraine.  
dimakmentsynskyi@gmail.com