
# Bookbar

![Bookbar welcome](https://github.com/marlonlom/bookbar/assets/1868030/2c0d54c2-362c-420c-96bf-2290ad0379ed)

[![Android CI](https://github.com/marlonlom/bookbar/actions/workflows/build.yml/badge.svg)](https://github.com/marlonlom/bookbar/actions/workflows/build.yml)
[![Android](https://img.shields.io/badge/API-33%2B-blue?logo=android-studio)]()
[![CodeFactor](https://www.codefactor.io/repository/github/marlonlom/bookbar/badge/main)](https://www.codefactor.io/repository/github/marlonlom/bookbar/overview/main)
![GitHub License](https://img.shields.io/github/license/marlonlom/bookbar)
![GitHub repo size](https://img.shields.io/github/repo-size/marlonlom/bookbar)


A simple bookstore android app for viewing the most recent IT books from [itbook.store](https://itbook.store/) webpage.

## itbook.store api
This application uses the [IT Bookstore API](https://api.itbook.store/) to load books information on any screen. To use the api, you will need to obtain the url from the API website.

The following are the urls used for the app for fetching the it books. those are meant to be created as environment variables and saved in `local.properties` file for reading purposes, after creating or using the mentioned file located in the root of the project, when building the app, it reads the properties and then saves it into the `BuildConfig` generated class for posterior usage. 

```
ITBOOKSTORE_API_URL=abc
ITBOOKSTORE_BUY_URL=def
```

## Application modules
During development, were created the following modules, divided in two categories: **libraries** and **applications/apps**.

| Submodule type | Module name            | Dependencies                                                                                                                | Module description                                                                                                  |
|----------------|------------------------|-----------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| apps           | mobile-app             | androidx-lifecycle, androidx-core-splashscreen, androidx-compose, material3, navigation-compose, coil, google-oss-licenses  | Containt the ui and logic for the it bookstore mobile application.                                                  |
| libraries      | database               | androidx-room                                                                                                               | Provide database model (entities, dao) for fetching it books in a local manner.                                     |
| libraries      | network-api            | ktor-client, kotlinx-serialization-json                                                                                     | Provide network calls and the rest api modeling for fetching it books.                                              |
| libraries      | preferences-datastore  | androidx-datastore-preferences                                                                                              | Contains the preferences used per user for customizing application ui  aspects, like dark theme and dynamic colors. |


## Contribution
Contribute to this project following the [Contributing Guideline](CONTRIBUTING.md).

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
