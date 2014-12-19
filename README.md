# Shoot and Share: Basic Mobile Application showing AeroGear Pipe and Authorization with multiple service providers
---------
Authors: Summers Pittman (secondsun) - Daniel Passos (dpassos)
Level: Advanced
Technologies: Java, Android, Googele, Facebook, KeyCloak   
Summary: And example of interacting with several modern web services.
Target Product: -   
Product Versions: -   
Source: https://github.com/secondsun/shoot-and-share

## What is it?

The ```Shoot and Share``` project demonstrates how to authenticate with Google, KeyCloak, and Facebook as well as how to upload pictures to Google Drive, your Facebook Wall, or a KeyClock protected web service.

This application also has an [iOS port](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Shoot) and a [companion Server](https://github.com/aerogear/aerogear-backend-cookbook/tree/master/Shoot) to host keycloak.

## How do I run it?

### 0. System Requirements

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Gradle 2.2.1](http://www.gradle.org/)
* Latest [Android SDK](https://developer.android.com/sdk/index.html) and [Platform version 21](http://developer.android.com/tools/revisions/platforms.html)

### 1. Service Requirements

#### Google Account Setup


First, you must authenticate with Google. You will need a Google Account for this. Now open the [Google Console](http://console.developer.google.com).

If you haven't created a project, click "Create Project" and give the new project a name:

![Google Console - Create Project](docs/google-console-1.png)

Now you need to enable the Drive API. To do that, navigate to APIs & auth > APIs and scroll down to the Drive API item, which you need to turn on:

![Google Console - Enable APIs](docs/google-console-2.png)

Now you need create new credentials to access your Drive accounts from the app. Go to APIs & auth > Credentials and inside OAuth area click the blue `Create new Client ID` button. This launches the following popup, where you select `Installed application` and `Other`:

![Google Console - Credentials](docs/google-console-3.png)

On the last screen finally get to see the actual value of the generated `client id`, `secret id` and `redirect uris`, which you will use later:

![Google Console - New credentials created](docs/google-console-4.png)

You also edit the 'Consent screen'. Go to API > Consent screen and edit the project informations.

![Google Console - New credentials created](docs/google-console-5.png)


#### Facebook Account Setup

First you must have a Facebook account.  Now open the [Facebook Developers](https://developers.facebook.com/) page and select 'Apps' -> 'Add a New App'

![Facebook - Add App](docs/facebook-0.png)

Click on 'advanced setup'

![Facebook - Add App Window](docs/facebook-2.png)

Setup a Display Name and Namespace and select a category, then click create app ID

![Facebook - Create App](docs/facebook-3.png)

Fill out the captcha

You are now on the Dashboard view.  Make a note of your 'App ID' and 'App Secret'You are now on the Dashboard view.  Make a note of your 'App ID' and 'App Secret'

![Facebook - Dashboard](docs/facebook-4.png)

Select 'Settings' from the sidebar and then the 'Advanced' tab

![Facebook - Advanced Tab](docs/facebook-5.png)

Scroll to Security and enable 'Embedded browser OAuth Login' and make 'https://localhost/' your redirectURI

![Facebook - Enable OAuth](docs/facebook-6.png)


#### Keycloak and Shoot Server Setup

### 3. Application Setup

#### Enable Google Account

In the class `GooglePlusHelper` there are two fields `AUTHZ_CLIENT_ID` and `AUTHZ_CLIENT_ID`.  Use the values you received from the Google console during 'Google Account Setup'

#### Enable Facebook Account

In the class `FacebookHelper` there are two fields `AUTHZ_CLIENT_ID` and `AUTHZ_CLIENT_ID`.  Use the values for 'App ID' and 'App Secret' you received from Facebook during 'Facebook Account Setup'

#### Shoot Backend Setup

### 4. Build Application

```shell
$ cd /path/to/app
$ gradle clean build
```



### 5. Test Application

To deploy, run and debug the application on an Android device attached to your system, on the command line enter the following:

2.1) Install generated apk to device

```shell
$ cd /path/to/app
$ gradle installDebug
```

2.2) Open app on device

Application output is displayed in the command line window.

## How does it work?

