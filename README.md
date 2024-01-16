
# Bookbar

![Bookbar welcome](https://github.com/marlonlom/bookbar/assets/1868030/2c0d54c2-362c-420c-96bf-2290ad0379ed)

[![Android CI](https://github.com/marlonlom/bookbar/actions/workflows/build.yml/badge.svg)](https://github.com/marlonlom/bookbar/actions/workflows/build.yml)
[![Android](https://img.shields.io/badge/API-33%2B-blue?logo=android-studio)]()
[![CodeFactor](https://www.codefactor.io/repository/github/marlonlom/bookbar/badge/main)](https://www.codefactor.io/repository/github/marlonlom/bookbar/overview/main)
![GitHub License](https://img.shields.io/github/license/marlonlom/bookbar)
![GitHub repo size](https://img.shields.io/github/repo-size/marlonlom/bookbar)


A simple bookstore android app for viewing the most recent IT books from [itbook.store](https://itbook.store/) webpage.

## Features
As a simple bookstore application, bookbar includes the following features for displaying it books:

- New books Listing
- Favorite books listing
- Book details
- Toggle selected book marking as favorite


## Development
Bookbar app was built taking in mind the [google's official architecture guidance](https://developer.android.com/topic/architecture) applying Model-view-viewmodel (MVVM), also, with jetpack compose, the features are built following the three components:

<img height="300" src="https://github.com/marlonlom/bookbar/assets/1868030/080c526a-485f-4e5f-aadf-7bf07c8a4dcd" />

The UI State used for the app and features mentioned earlier, are handled in the view and viewmodel components. 


### Navigation
Bookbar uses a splashscreen as starting point for loading the main contents, after that, displays a home screen which contains three top destinations: `new books`, `favorite books`, `settings`. 
In the `new books`, `favorite books` top destinations, when a book item from the list is selected/tapped/clicked, the app displays the book detail screen. 
For the `settings` destination, the app displays a dialog with the following settings: dark theme, dynamic colors.

![bookbar-navigation-diagram](https://github.com/marlonlom/bookbar/assets/1868030/c8c2fd4b-3d4d-4da5-b212-6a3fdea9a065)

The navigation graph is defined as follows:

| Route         | Label       | Icon                   | Top destination |
|---------------|-------------|------------------------|-----------------|
| home          | Home        | Icons.TwoTone.Home     |        Y        |
| favorite      | Favorite    | Icons.TwoTone.Favorite |        Y        |
| settings      | Preferences | Icons.TwoTone.Settings |        Y        |
| book/{bookId} | Details     |                        |        N        |


### itbook.store api
This application uses the [IT Bookstore API](https://api.itbook.store/) to load books information on any screen. To use the api, you will need to obtain the url from the API website.

The following are the urls used for the app for fetching the it books. those are meant to be created as environment variables and saved in `local.properties` file for reading purposes, after creating or using the mentioned file located in the root of the project, when building the app, it reads the properties and then saves it into the `BuildConfig` generated class for posterior usage. 

```
ITBOOKSTORE_API_URL=abc
ITBOOKSTORE_BUY_URL=def
```

### Application modules
During development, were created the following modules, divided in two categories: **libraries** and **applications/apps**.

| Submodule type | Module name            | Dependencies                                                                                                                | Module description                                                                                                  |
|----------------|------------------------|-----------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| apps           | mobile-app             | androidx-lifecycle, androidx-core-splashscreen, androidx-compose, material3, navigation-compose, coil, google-oss-licenses  | Containt the ui and logic for the it bookstore mobile application.                                                  |
| libraries      | database               | androidx-room                                                                                                               | Provide database model (entities, dao) for fetching it books in a local manner.                                     |
| libraries      | network-api            | ktor-client, kotlinx-serialization-json                                                                                     | Provide network calls and the rest api modeling for fetching it books.                                              |
| libraries      | preferences-datastore  | androidx-datastore-preferences                                                                                              | Contains the preferences used per user for customizing application ui  aspects, like dark theme and dynamic colors. |


## User Interface

- The app was designed using Material 3 guidelines. 
- The Screens and UI elements are built entirely using Jetpack Compose.
- The app has a default theme built by material 3 theme builder.
- The app ui handles dynamic colors for theming based on the user's current color theme (supported for android 12 and later)
- The app's theme also supports dark mode for both dynamic colors and default theme.
- The app uses adaptive layouts to support different screen sizes (mobile phone, tablet).


## Contribution
Contribute to this project following the [Contributing Guideline](CONTRIBUTING.md).

> [!IMPORTANT] 
> Please note that this is a sample Android app to demonstrate Android development and Jetpack composition features, so there is no need to contribute to out-of-scope functionality in this topic.


## License
```
Copyright 2024 Marlonlom

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
