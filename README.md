# Simple Shopping List

Simple app that saves shopping items. Add new items or choose from recently used. Assign different colors. Items can be sorted by name. There is also a night mode.

## Technology stack
- Kotlin
- Androidx
- MVP
- [MVP Dialog][mvp dialog]
- [Room][room]
- [Dagger][dagger]
- [Unit][unit tests] / [UI tests][ui tests]
- [Custom ColorPickerDialog][color picker dialog]
- [Flexbox Layout][flexbox]
- [ToolBar][toolbar] -> [BottomAppBar][bottombar]
- [Proguard][proguard]
- [MaterialComponents Theme][material theme]
- [Theme Switching][theme switching]
- [Recycler ItemTouchHelper][item touch helper]

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

[mvp dialog]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/additem
[room]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/data/db
[dagger]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/main/java/com/example/util/simpleshoppinglist/di
[unit tests]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/test/java/com/example/util/simpleshoppinglist
[ui tests]: https://github.com/Razeeman/Android-SimpleShoppingList/tree/master/app/src/androidTest/java/com/example/util/simpleshoppinglist/data
[color picker dialog]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/custom/ColorPickerDialog.kt
[flexbox]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/main/MainFragment.kt#L71
[toolbar]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/6d2e00c51171f0382314f1c9179024c2ce1265a8/app/src/main/res/layout/main_activity.xml#L11
[bottombar]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/res/layout/main_activity.xml#L22
[proguard]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/build.gradle#L20
[material theme]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/res/values/styles.xml#L3
[theme switching]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/util/ThemeManager.kt
[item touch helper]: https://github.com/Razeeman/Android-SimpleShoppingList/blob/master/app/src/main/java/com/example/util/simpleshoppinglist/ui/main/MainFragment.kt#L78
