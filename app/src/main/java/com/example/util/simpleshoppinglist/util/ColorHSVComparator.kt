package com.example.util.simpleshoppinglist.util

import android.graphics.Color
import javax.inject.Inject

/**
 * Comparator to compare colors based on hue, saturation and value.
 */
open class ColorHSVComparator @Inject constructor(): Comparator<Int> {

    companion object {

        private val comparator = compareBy<FloatArray> ({ it[0] }, { it[1] }, { it[2] })

    }

    // TODO native Color.colorToHSV not testable? Need workaround?
    /**
     * Compare two colors.
     * hsv[0] is Hue ([0..360)
     * hsv[1] is Saturation ([0...1])
     * hsv[2] is Value ([0...1])
     */
    override fun compare(color1: Int?, color2: Int?): Int {
        val hsv1 = FloatArray(3)
        Color.colorToHSV(color1!!, hsv1)
        val hsv2 = FloatArray(3)
        Color.colorToHSV(color2!!, hsv2)

        return comparator.compare(hsv1, hsv2)
    }

}