# Adyen Android Assignment

This repository contains the coding challenge for candidates applying for an Android role at Adyen.
It consists of two unrelated parts:

## 1. Cash Register
Your first task is to implement a cash register. See the `cashregister` module.

## 2. App
Your second task is to implement a small app using the Foursquare Places API. See the `app` module.

You can modify any piece of code in this repository.
General rule of thumb: quality over quantity.

### Considerations
- Was prioritized architecture in this project and make it easy to add new features. So, the UI can be improved a lot :D
- The persistence of data is incomplete
  - When device is offline we can combine flow to load data from DB or API.
- Was Chosen MVVM architecture with user coses from clean architecture.


### To Run The Project
- You should put your Foursquare API in '/app/local.gradle' in the project root.
- Make sure that the project run with 'JDK 11' in AndroidStudio and terminal.
    - Android Studio: go to File -> Project Structure -> SDK Location -> Click in 'Gradle Settings' link
    - Terminal: As you like :) Maybe [SDKMAN](https://sdkman.io/)