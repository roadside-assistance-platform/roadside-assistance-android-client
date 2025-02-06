# Roadside Assistance Client App

This is the Android client application for the Roadside Assistance platform. It allows users to request assistance, track service provider location, communicate with providers, and manage their account.

## Table of Contents

-   [Project Overview](#project-overview)
-   [Technology Stack](#technology-stack)
-   [Architecture](#architecture)
-   [Modules](#modules)
-   [Setup Instructions](#setup-instructions)
-   [Contribution Guidelines](#contribution-guidelines)
-   [License](#license)
-   [Contact](#contact)

## Project Overview

The Roadside Assistance Client App provides a user-friendly interface for requesting roadside assistance services.  Users can specify their location, describe the issue, and track the arrival of a service provider in real-time.

## Technology Stack

-   Platform: Android (Jetpack Compose)
-   Programming Language: Kotlin
-   Design Language: Material 3 (Material You)
-   State Management Library: ViewModel
-   Dependency Injection Library: Koin
-   Networking Library: Retrofit
-   Mapping Library: Mapbox

## Architecture

This app follows the MVVM architectural pattern.

## Modules

The app is modularized into the following layers:

-   **core:** Contains core functionalities and utilities shared across other modules, such as dependency injection setup, base classes, and extension functions.
-   **data:** Handles data access and retrieval. This module interacts with the backend API and local data sources (e.g., Room database, shared preferences).  It provides repositories that abstract data access logic.
-   **domain:** Contains the business logic and use cases of the application. It defines interfaces for repositories and provides use cases that interact with the data layer.  This module is independent of any specific implementation details.
-   **presentation:** Contains the UI components and logic. This module interacts with the domain layer through use cases and displays data to the user. It uses Jetpack Compose for building the UI.

## Setup Instructions

1.  Clone the repository: `git clone https://github.com/roadside-assistance-platform/roadside-assistance-android-client.git`
2.  Open the project in Android Studio.
3.  Build the project: `./gradlew assembleDebug`
4.  Run the app on an emulator or physical device.

## Contribution Guidelines

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on how to contribute.

## License

[License (Apache 2.0)]

## Contact

Email me on: y.bouhouche@esi-sba.dz
