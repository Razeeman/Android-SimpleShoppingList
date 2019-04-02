# For UI tests.
-keepclassmembers class androidx.room.Room { androidx.room.RoomDatabase$Builder inMemoryDatabaseBuilder(android.content.Context,java.lang.Class); }
-keepclassmembers class androidx.room.RoomDatabase { void close(); }
-keepclassmembers class com.example.util.simpleshoppinglist.data.model.Item { synthetic com.example.util.simpleshoppinglist.data.model.Item copy$default(com.example.util.simpleshoppinglist.data.model.Item,java.lang.String,java.lang.String,int,boolean,boolean,int,java.lang.Object); }
-keepclassmembers class com.example.util.simpleshoppinglist.data.db.ItemDao { void update(com.example.util.simpleshoppinglist.data.model.Item); }
-keepclassmembers class com.example.util.simpleshoppinglist.data.db.ItemDao { void delete(com.example.util.simpleshoppinglist.data.model.Item); }
-keepclassmembers class com.example.util.simpleshoppinglist.data.repo.ItemsRepository { void updateItem(com.example.util.simpleshoppinglist.data.model.Item); }
-keepclassmembers class com.example.util.simpleshoppinglist.data.repo.ItemsRepository$Companion { void clearInstance(); }
-keep class com.example.util.simpleshoppinglist.util.InstantExecutors { *; }
