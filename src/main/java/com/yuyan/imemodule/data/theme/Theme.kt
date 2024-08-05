
package com.yuyan.imemodule.data.theme

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import com.yuyan.imemodule.application.ImeSdkApplication
import com.yuyan.imemodule.ui.utils.DarkenColorFilter
import com.yuyan.imemodule.ui.utils.RectSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
sealed class Theme : Parcelable {

    abstract val name: String
    abstract val isDark: Boolean

    abstract val backgroundColor: Int
    abstract val barColor: Int
    abstract val keyboardColor: Int

    abstract val keyBackgroundColor: Int
    abstract val keyPressHighlightColor: Int
    abstract val keyTextColor: Int

    abstract val accentKeyBackgroundColor: Int
    abstract val accentKeyTextColor: Int

    abstract val popupBackgroundColor: Int
    abstract val popupTextColor: Int

    abstract val spaceBarColor: Int
    abstract val dividerColor: Int

    abstract val genericActiveBackgroundColor: Int
    abstract val genericActiveForegroundColor: Int

    open fun backgroundDrawable(keyBorder: Boolean = false): Drawable {
        return ColorDrawable(if (keyBorder) backgroundColor else keyboardColor)
    }

    open fun backgroundGradientDrawable(keyBorder: Boolean = false): Drawable {
        return GradientDrawable().apply {
            setColor(if (keyBorder) backgroundColor else keyboardColor)
            setShape(GradientDrawable.RECTANGLE)
            setCornerRadius(20f) // 设置圆角半径
            if(ThemeManager.prefs.keyboardModeFloat.getValue()){
                setStroke(2, dividerColor)
            }
        }
    }

    @Serializable
    @Parcelize
    data class Custom(
        override val name: String,
        override val isDark: Boolean,
        /**
         * absolute paths of cropped and src png files
         */
        val backgroundImage: CustomBackground?,
        override val backgroundColor: Int,
        override val barColor: Int,
        override val keyboardColor: Int,
        override val keyBackgroundColor: Int,
        override val keyTextColor: Int,
        override val accentKeyBackgroundColor: Int,
        override val accentKeyTextColor: Int,
        override val keyPressHighlightColor: Int,
        override val popupBackgroundColor: Int,
        override val popupTextColor: Int,
        override val spaceBarColor: Int,
        override val dividerColor: Int,
        override val genericActiveBackgroundColor: Int,
        override val genericActiveForegroundColor: Int
    ) : Theme() {
        @Parcelize
        @Serializable
        data class CustomBackground(
            val croppedFilePath: String,
            val srcFilePath: String,
            val brightness: Int = 70,
            val cropRect: @Serializable(RectSerializer::class) Rect?,
        ) : Parcelable {
            fun toDrawable(): Drawable? {
                val cropped = File(croppedFilePath)
                if (!cropped.exists()) return null
                val bitmap = BitmapFactory.decodeStream(cropped.inputStream()) ?: return null
                return BitmapDrawable(ImeSdkApplication.context.resources, bitmap).apply {
                    colorFilter = DarkenColorFilter(100 - brightness)
                }
            }
        }

        override fun backgroundDrawable(keyBorder: Boolean): Drawable {
            return backgroundImage?.toDrawable() ?: super.backgroundDrawable(keyBorder)
        }

    }

    @Parcelize
    data class Builtin(
        override val name: String,
        override val isDark: Boolean,
        override val backgroundColor: Int,
        override val barColor: Int,
        override val keyboardColor: Int,
        override val keyBackgroundColor: Int,
        override val keyTextColor: Int,
        override val accentKeyBackgroundColor: Int,
        override val accentKeyTextColor: Int,
        override val keyPressHighlightColor: Int,
        override val popupBackgroundColor: Int,
        override val popupTextColor: Int,
        override val spaceBarColor: Int,
        override val dividerColor: Int,
        override val genericActiveBackgroundColor: Int,
        override val genericActiveForegroundColor: Int
    ) : Theme() {

        // an alias to use 0xAARRGGBB color literal in code
        // because kotlin compiler treats `0xff000000` as Long, not Int
        constructor(
            name: String,
            isDark: Boolean,    // 深色主题
            backgroundColor: Number,  // 整体区域背景色
            barColor: Number,    // 菜单栏、候选此栏背景色
            keyboardColor: Number,  // 按键区域背景色
            keyBackgroundColor: Number,  // 按键背景色
            keyTextColor: Number,   // 按键字体颜色
            accentKeyBackgroundColor: Number, // 回车键背景色
            accentKeyTextColor: Number, // 回车键字体色
            keyPressHighlightColor: Number,
            popupBackgroundColor: Number,   // 长按弹窗背景色
            popupTextColor: Number,  // 长按弹窗字体颜色
            spaceBarColor: Number,  // 空格键背景色
            dividerColor: Number,  // 分割线颜色
            genericActiveBackgroundColor: Number,    //通用活动背景色
            genericActiveForegroundColor: Number   //通用活动前景色
        ) : this(
            name,
            isDark,
            backgroundColor.toInt(),
            barColor.toInt(),
            keyboardColor.toInt(),
            keyBackgroundColor.toInt(),
            keyTextColor.toInt(),
            accentKeyBackgroundColor.toInt(),
            accentKeyTextColor.toInt(),
            keyPressHighlightColor.toInt(),
            popupBackgroundColor.toInt(),
            popupTextColor.toInt(),
            spaceBarColor.toInt(),
            dividerColor.toInt(),
            genericActiveBackgroundColor.toInt(),
            genericActiveForegroundColor.toInt()
        )

        fun deriveCustomNoBackground(name: String) = Custom(
            name,
            isDark,
            null,
            backgroundColor,
            barColor,
            keyboardColor,
            keyBackgroundColor,
            keyTextColor,
            accentKeyBackgroundColor,
            accentKeyTextColor,
            keyPressHighlightColor,
            popupBackgroundColor,
            popupTextColor,
            spaceBarColor,
            dividerColor,
            genericActiveBackgroundColor,
            genericActiveForegroundColor
        )

        fun deriveCustomBackground(
            name: String,
            croppedBackgroundImage: String,
            originBackgroundImage: String,
            brightness: Int = 70,
            cropBackgroundRect: Rect? = null,
        ) = Custom(
            name,
            isDark,
            Custom.CustomBackground(
                croppedBackgroundImage,
                originBackgroundImage,
                brightness,
                cropBackgroundRect
            ),
            backgroundColor,
            barColor,
            keyboardColor,
            keyBackgroundColor,
            keyTextColor,
            accentKeyBackgroundColor,
            accentKeyTextColor,
            keyPressHighlightColor,
            popupBackgroundColor,
            popupTextColor,
            spaceBarColor,
            dividerColor,
            genericActiveBackgroundColor,
            genericActiveForegroundColor
        )
    }

}