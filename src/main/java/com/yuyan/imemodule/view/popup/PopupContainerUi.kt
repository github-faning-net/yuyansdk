
package com.yuyan.imemodule.view.popup

import android.content.Context
import android.graphics.Rect
import android.view.View
import com.yuyan.imemodule.data.theme.Theme
import com.yuyan.imemodule.singleton.EnvironmentSingleton
import splitties.views.dsl.core.Ui
import kotlin.math.roundToInt

abstract class PopupContainerUi(
    override val ctx: Context,
    val theme: Theme,
    val bounds: Rect,
    val onDismissSelf: PopupContainerUi.() -> Unit
) : Ui {

    /**
     * Popup container view
     */
    abstract override val root: View

    /**
     * Offset on X axis to put this [PopupKeyboardUi] relative to popup trigger view [bounds]
     */
    abstract val offsetX: Int

    /**
     * Offset on Y axis to put this [PopupKeyboardUi] relative to popup trigger view [bounds]
     */
    abstract val offsetY: Int

    fun calcInitialFocusedColumn(columnCount: Int, columnWidth: Int, bounds: Rect): Int {
        val leftSpace = bounds.left
        val rightSpace = EnvironmentSingleton.instance.skbWidth - bounds.right
        var col = (columnCount - 1) / 2
        while (columnWidth * col > leftSpace) col--
        while (columnWidth * (columnCount - col - 1) > rightSpace) col++
        return col
    }

    /**
     * column order priority: center, right, left. eg.
     * ```
     * | 6 | 4 | 2 | 0 | 1 | 3 | 5 |
     * ```
     * in case free space is not enough in right (left), just skip that cell. eg.
     * ```
     *    | 3 | 2 | 1 | 0 |(no free space)
     * (no free space)| 0 | 1 | 2 | 3 |
     * ```
     */
    fun createColumnOrder(columnCount: Int, initialFocus: Int) = IntArray(columnCount).also {
        var order = 0
        it[initialFocus] = order++
        for (i in 1 until columnCount * 2) {
            val sign = if (i % 2 == 0) -1 else 1
            val delta = (i / 2f).roundToInt()
            val nextColumn = initialFocus + sign * delta
            if (nextColumn < 0 || nextColumn >= columnCount) continue
            it[nextColumn] = order++
        }
    }

    fun changeFocus(x: Float, y: Float): Boolean {
        return onChangeFocus(x - offsetX, y - offsetY)
    }

    /**
     * Handle focus change of this [PopupKeyboardUi].
     * [x], [y] axis are relative to container itself.
     *
     * @return Whether the gesture should be consumed, ie. no more gesture events should
     * be dispatched to the trigger view.
     */
    abstract fun onChangeFocus(x: Float, y: Float): Boolean

    abstract fun onTrigger(): String?

    companion object {
        fun limitIndex(i: Int, limit: Int) = if (i < 0) 0 else if (i >= limit) limit - 1 else i
    }
}
