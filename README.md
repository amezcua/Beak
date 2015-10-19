# Beak
Sample application (twitter client) using clean architecture principles.

## Features
- Log in to Twitter using OAuth 1.0. Full client implementation. Checkout the domain module ("oauth" package) to see how the signatures are created. Tests available in the domain module too.
- See the user's home timeline. Pull to refresh to reload the latest tweets. Scroll to the bottom to load older tweets.
- See a user's profile data. On a tweet in the home timeline, tap on the image or user's name.
- Post a new tweet. Use the FAB button to show the UI to post a new update. After successfully posting it, the home timeline is refreshed so that the user can see the new tweet.

![alt tag](https://raw.githubusercontent.com/amezcua/amezcua.github.io/master/projects/Beak/images/beak.gif)

## Architecture
The app has been built using Clean Architecture concepts (https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) borrowing a bunch of concepts and ideas from https://github.com/PaNaVTEC/Clean-Contacts (thanks Christian!).

It is divided in three modules named domain, presentation and app.

 - Domain contains all the core logic for the app to achieve its features
 - Presentation contains the presentation classes to handle the UI functions. The app is based on the Model View Presenter pattern.
 - App contains all the Android specific code to fulfil the specific functionality.
 
The domain and presentation modules are built using pure java code with no Android dependencies at all.

## Libraries
Some libraries have been added to aid in the development:
 - Goole appcompat and design libraries (to aid with material design concepts such as the FAB and activity transitions)
 - Butterknife (http://jakewharton.github.io/butterknife/) to help with UI elements binding
 - Glide (https://github.com/bumptech/glide) to easily display images
 - Retrofit 2 (http://square.github.io/retrofit/) (with okhttp) to handle the Twitter API calls
 - Stetho (http://facebook.github.io/stetho/) to aid with debugging

## Setup
To be able to run the app you need to setup a Twitter app (https://apps.twitter.com/) to get the appropriate application keys.

Once the application is setup you need to get the Consumer key and Consumer secret (from https://apps.twitter.com/app/[appid]/keys) and configure gradle to use it. I did setup my private gradle.properties (in ~/.gradle/gradle.properties) file with the following keys:

beak.consumerKey="YOUR KEY"
beak.consumerSecret="YOUR SECRET"

Once the keys are setup, the app they are referring to must be configured (in https://apps.twitter.com/app/[appid]/settings) with a callback URL and marking the checkbox "Allow this application to be used to Sign in with Twitter". I did setup the URL http://beak.byteabyte.net as the callback url but it should work with any other as the app is providing the url in the API calls.

If the callback url is not set the OAuth flow will not work and the login process will fail.

## Future improvements
- The code has been left as expressive as possible on purpose and hence no code generation libraries have been included, which might help in the future if the application grows. Specifically a DI framework (Dagger 2) could be of benefit to remove some boilerplate code or the presentation framework in Clean Contacts which allows for more streamlined presenters.
- Only simple threading is used in the presenters, opening a new thread whenever a network operation has to be performed which is then passed to the UI thread according to the mechanism specified by the calling activity.
- Caching of the timelines would improve app startup to get the gome timeline so the use of a repository would be beneficial in this case; going to the network to retrieve the timeline only for new data.




