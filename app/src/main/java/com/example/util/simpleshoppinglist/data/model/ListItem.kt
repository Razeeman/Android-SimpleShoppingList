package com.example.util.simpleshoppinglist.data.model

import java.util.*

/**
 * Model class for shopping list item.
 */
class ListItem (var id: String = UUID.randomUUID().toString(),
                var name: String = "",
                var color: Int = 0)