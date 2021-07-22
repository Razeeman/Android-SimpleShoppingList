# Simple Shopping List

Simple app that saves shopping items. Add new items or choose from recently used. Tap to add, swipe to delete. Assign different colors. Items can be sorted by name and grouped by color. There is also a night mode.

[Download](dev_files/app-debug.apk)

## Technology stack
- Kotlin
- Androidx
- MVP
- [MVP Dialog]
- [Room]
- [Dagger]
- [Unit] / [UI tests]
- [Custom ColorPickerDialog]
- [Flexbox Layout]
- [ToolBar] -> [BottomAppBar]
- [Proguard]
- [MaterialComponents Theme]
- [Theme Switching]
- [Recycler ItemTouchHelper]

## Screenshots

[![Main Screen][screen1th]][screen1]
[![Recent Items Screen][screen2th]][screen2]
[![Add Item Screen][screen3th]][screen3]
[![Choose Color Screen][screen4th]][screen4]
[![Night Mode][screen5th]][screen5]

[screen1th]: dev_files/screens/screen1_thumbnail.png
[screen1]: dev_files/screens/screen1.png
[screen2th]: dev_files/screens/screen2_thumbnail.png
[screen2]: dev_files/screens/screen2.png
[screen3th]: dev_files/screens/screen3_thumbnail.png
[screen3]: dev_files/screens/screen3.png
[screen4th]: dev_files/screens/screen4_thumbnail.png
[screen4]: dev_files/screens/screen4.png
[screen5th]: dev_files/screens/screen5_thumbnail.png
[screen5]: dev_files/screens/screen5.png

[MVP Dialog]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/additem
[Room]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/data/db
[Dagger]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/di
[Unit]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/test/java/com/example/util/simpleshoppinglist
[UI tests]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/androidTest/java/com/example/util/simpleshoppinglist
[Custom ColorPickerDialog]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/custom/ColorPickerDialog.kt
[Flexbox Layout]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/main/MainFragment.kt#L74
[ToolBar]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/6d2e00c51171f0382314f1c9179024c2ce1265a8/app/src/main/res/layout/main_activity.xml#L11
[BottomAppBar]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/res/layout/main_activity.xml#L22
[Proguard]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/build.gradle#L25
[MaterialComponents Theme]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/res/values/styles.xml#L3
[Theme Switching]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/util/ThemeManager.kt
[Recycler ItemTouchHelper]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/main/MainFragment.kt#L81
