# Simple Player

Simple music player that implement [Itunes API](https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api)

 ![Screenshoot](assets/screen.gif)

## Features
- Search
- Play Indicator
- Play/Pause
- Next Song
- Previous Song
- Auto next song after track finished
- Time indicator
- Seek time
- Still play current song even playlist changed

## Target SDK
- This application can be run on SDK 21 (Android Lolipop) or above.

## 3rd Party Library
- Android Material
- Glide
- Retrofit
- Moshi
- Hilt
- Kotlin Coroutine

## Architecture
We use MVVM for presentation layer

## Project Structure
Structure for this project
- core : Core class and utilities
    - data : Core class for data management
    - threading : Core class for threading
    - ui : Core class that used for building UI
- di : Hilt Dependency Injection module
- data : Data layer. Responsible for providing the data
    - model : Model that represent the data
    - repository : Data provider
- domain : Business logic. Responsible for all business logic in the app.
    - cases : Use case for each business logic
    - mapper : Mapping from data model to domain model
    - model : Model that represent data in each cases
- presentation : Responsible to interacting with user
    - component : Centralized component directory, where all reusable component stored

## ComponentAsyncAdapter and Component Concept

ComponentAsyncAdapter is a Recycler View adapter that can easily filled with homogenous item. It utilize diff utils to update the list efficiently. Insipired from airbnb [Epoxy](https://github.com/airbnb/epoxy).

The item that can be added to the adapter called `Component`. `Component` basically a custom view that implement `Component` or `MutableComponent` interface.

Each component need a state that must be a kotlin `data class` and implement `bind` method that change the view based on the state.

`Component` interface used to create component where the state cannot be changed. Usually used for item inside reyceler view.

`MutableComponent` interface used to crate component where the state property can be changed with `mutateState`. Usually used for item that used in xml and inside recycler view.