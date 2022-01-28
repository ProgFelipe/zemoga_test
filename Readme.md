# Zemoga Post App

Zemoga Post App is an Android App that let user handle posts from [JSONPlaceholder](https://jsonplaceholder.typicode.com).

## Preview
[Preview Video](https://github.com/ProgFelipe/zemoga_test/blob/master/zemogaPosts.mp4)

## Architecture
Clean architecture used in this app which objective is the separation of concerns.

As well the usage of Single Activity architecture with android Jetpack navigation.

Overview of application layers

**1.) Presentation Layer**: Where all the UI happens.
  
  | UI | Description |
  | ------ | ------ |
  | HomeFragment | which contains the `ViewPager` with `PostsFragment and FavoriteFragment`. |
  | PostDetailFragment | that gets the `id` of selected post as argument and display the post details. |
  | PostsViewModel | a shared viewModel that refresh views using LiveData |

**2.) Domain Layer**: The UseCase is present here which contains the business rules.

- `PostsUseCase`

**3.) Data Layer**: you will find here the repository, the apis and db.

## Install

Use Android Studio to install, you can use the below command also on AS terminal.

```bash
gradlew installDebug
```

## Libraries

```gradñe
Language
	Kotlin

Jetpack
	Androidx Navigation

Network
    Retrofit 2
	Moshi

Async
	RxJava
    RxJava adapter
	RxAndroid

DI
	Hilt
	Hilt lifecycle-viewmodel

Database
	Room
	Room-rxjava2
	
Test
    Mockito
    androidx.arch.core:core-testing
```
The reason why I choose these libraries is because I consider that they are currently a standard provided by `Google`, for example the case of `Jetpack`, which provides libraries that encapsulate behaviors such as `navigation` or dependency injection with `Hilt` as an evolution of `Dagger` and also I have an recent affinity with some of these libraries as is the case of `Rx`.

## License
[MIT](https://choosealicense.com/licenses/mit/)