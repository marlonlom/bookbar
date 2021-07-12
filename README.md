<hr/>

# Table of contents:
- [Bookbar](#bookbar)
  * [Getting Started](#getting-started)
    + [IT Bookstore API url](#it-bookstore-api-url)
    + [Screenshots](#screenshots)
    + [Libraries Used](#libraries-used)
    + [Android Studio IDE setup](#android-studio-ide-setup)
- [About the developer](#about-the-developer)
- [License](#license)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>

<hr/>

# Bookbar
A book store app with some illustrating about Android development best practices with Android Jetpack.

## Getting Started
This project uses the Gradle build system. To build this project, use the `gradlew build` command or use "Import Project" in Android Studio.

There are two Gradle tasks for testing the project:
* `connectedAndroidTest` - for running Espresso on a connected device
* `test` - for running unit tests

For more resources on learning Android development, visit the [Developer Guides](https://developer.android.com/guide/) at [developer.android.com](https://developer.android.com).

### IT Bookstore API url
This application uses the [IT Bookstore API](https://api.itbook.store/) to load books information on any screen. To use the API, you will need to obtain the url from the API website.

Once you have the key, add this line to the `gradle.properties` file, either in your user home directory (usually `~/.gradle/gradle.properties` on Linux and Mac) or in the project's root folder:

```
ITBOOKSTORE_API_URL=<the it books api url>
```

The app is not usable without the specified key, though you won't be able to navigate if not books data present.

### Screenshots
TBD


### Libraries Used
TBD


### Android Studio IDE setup
For development, the latest version of Android Studio is required. The latest version can be downloaded from [here](https://developer.android.com/studio/).


# About the developer

 - Follow me on **Twitter**: [**@Marlonlom**](https://twitter.com/marlonlom)
 - Contact me on **LinkedIn**: [**Marlonlom**](https://co.linkedin.com/in/marlonlom)


# License

```
Copyright 2021 marlonlom

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
