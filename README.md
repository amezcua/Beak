# Beak v2
Sample application (twitter client) using clean architecture principles.

Use the Tags of the repository to access the different published versions.

## Features
 - Log in to Twitter using OAuth 1.0. Full client implementation. Checkout the domain module ("oauth" package) to see how the signatures are created. Tests available in the domain module too.
 - See the user's home timeline. Pull to refresh to reload the latest tweets. Scroll to the bottom to load older tweets.
 - See a user's profile data. On a tweet in the home timeline, tap on the image or user's name.
 - Post a new tweet. Use the FAB button to show the UI to post a new update. After successfully posting it, the home timeline is refreshed so that the user can see the new tweet.

![alt tag](https://raw.githubusercontent.com/amezcua/amezcua.github.io/master/projects/Beak/images/beak.gif)

## What's new in v2?
The presentation layer has been modified to hide all the threading code. The goal is to write the presentation code as sequential code and not have to worry about threading at all.

To achieve this two techniques are used, dynamic java proxies and aspects.

 - *Dynamic proxies*. When a view is attached to a presenter a dynamic proxy wrapping the view is created. Then when the *getView()* method is called, the proxy is returned transparently to the caller, which will call the view methods using the provided *OutputThread* implementation. In the sample an Android handler will be invoked. When the *detachView* method is called, a null proxy view is created replacing the original one so there is no need to do null checks in the case that background operations complete after the view has disappeared.
 - Aspects. Aspectj has been included in the presentation module and the *@BackgroundTask* decorator has been defined. If a method requires a long running task that should be run in a separate thread, the decorator can be applied to the method and the relevant aspect will take care of spawning a new background thread to run that method.

Combining the two methods allows for writing sequential-like code that hides the complexity of handling threads that the previous version had.

## Architecture
The app has been built using Clean Architecture concepts (https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) borrowing a bunch of concepts and ideas from https://github.com/PaNaVTEC/Clean-Contacts (thanks Christian!).

It is divided in three modules named domain, presentation and app.

 - Domain contains all the core logic for the app to achieve its features
 - Presentation contains the presentation classes to handle the UI functions. The app is based on the Model View Presenter pattern.
 - App contains all the Android specific code to fulfil the specific functionality.
 
The domain and presentation modules are built using pure java code with no Android dependencies at all.

## Libraries
Some libraries have been added to aid in the development:

 - Google appcompat and design libraries (to aid with material design concepts such as the FAB and activity transitions)
 - Butterknife (http://jakewharton.github.io/butterknife/) to help with UI elements binding
 - Glide (https://github.com/bumptech/glide) to easily display images
 - Retrofit 2 (http://square.github.io/retrofit/) (with OkHttp) to handle the Twitter API calls
 - Stetho (http://facebook.github.io/stetho/) to aid with debugging

## Setup
To be able to run the app you need to setup a Twitter app (https://apps.twitter.com/) to get the appropriate application keys.

Once the application is setup you need to get the Consumer key and Consumer secret (from https://apps.twitter.com/app/[appid]/keys) and configure gradle to use it. I did setup my private gradle.properties (in ~/.gradle/gradle.properties) file with the following keys:

```
beak.consumerKey="YOUR KEY"
beak.consumerSecret="YOUR SECRET"
```

Once the keys are setup, the app they are referring to must be configured (in https://apps.twitter.com/app/[appid]/settings) with a callback URL and marking the checkbox "Allow this application to be used to Sign in with Twitter". I did setup the URL http://beak.byteabyte.net as the callback url but it should work with any other as the app is providing the url in the API calls.

If the callback url is not set the OAuth flow will not work and the login process will fail.

## Future improvements
- Caching of the timelines (and oauth related data) would improve app startup so the use of a repository would be beneficial in this case; going to the network to retrieve the timeline only for new data.
- Displaying a small specific user timeline in the user details screen would give that screen more reason for being there as right now it displays only very basic information. It would be a good opportunity to play with the design.