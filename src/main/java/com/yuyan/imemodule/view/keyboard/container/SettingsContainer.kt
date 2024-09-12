package com.yuyan.imemodule.view.keyboard.container

import android.annotation.SuppressLint
import com.google.android.flexbox.JustifyContent
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.yuyan.imemodule.R
import com.yuyan.imemodule.adapter.MenuAdapter
import com.yuyan.imemodule.constant.CustomConstant
import com.yuyan.imemodule.data.menuSkbFunsPreset
import com.yuyan.imemodule.data.theme.Theme
import com.yuyan.imemodule.data.theme.ThemeManager.activeTheme
import com.yuyan.imemodule.entity.SkbFunItem
import com.yuyan.imemodule.manager.InputModeSwitcherManager
import com.yuyan.imemodule.prefs.AppPrefs
import com.yuyan.imemodule.prefs.behavior.SkbMenuMode
import com.yuyan.imemodule.view.keyboard.InputView
import com.yuyan.imemodule.view.keyboard.KeyboardManager
import com.yuyan.imemodule.prefs.behavior.DoublePinyinSchemaMode
import com.yuyan.imemodule.utils.KeyboardLoaderUtil
import java.util.Collections
import java.util.LinkedList


/**
 * 设置键盘容器
 *
 * 设置键盘、切换键盘界面容器。使用RecyclerView + FlexboxLayoutManager实现Grid布局。
 */
@SuppressLint("ViewConstructor")
class SettingsContainer(context: Context, inputView: InputView) : BaseContainer(context, inputView) {
    private var mRVMenuLayout: RecyclerView? = null
    private var mTheme: Theme? = null
    private var adapter:MenuAdapter? = null
    val funItems: MutableList<SkbFunItem> = LinkedList()   //键盘菜单对象
    init {
        initView(context)
    }

    private fun initView(context: Context) {
        mTheme = activeTheme
        mRVMenuLayout = RecyclerView(context)
        mRVMenuLayout!!.setHasFixedSize(true)
        mRVMenuLayout!!.setItemAnimator(null)
        val manager = FlexboxLayoutManager(context)
        manager.justifyContent = JustifyContent.FLEX_START
        mRVMenuLayout!!.setLayoutManager(manager)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mRVMenuLayout!!.layoutParams = layoutParams
        this.addView(mRVMenuLayout)
    }

    /**
     * 弹出键盘设置界面
     */
    fun showSettingsView() {
        funItems.clear()
        val keyboardMenu = AppPrefs.getInstance().internal.keyboardSettingMenuAll.getValue().split(", ")
        for(item in keyboardMenu){
            val skbMenuMode = menuSkbFunsPreset[SkbMenuMode.decode(item)]
            if(skbMenuMode != null){
                funItems.add(skbMenuMode)
            }
        }
        adapter = MenuAdapter(context, funItems)
        adapter?.setOnItemClickLitener { _: RecyclerView.Adapter<*>?, _: View?, position: Int ->
            inputView.onSettingsMenuClick(funItems[position].skbMenuMode)
        }
        mRVMenuLayout!!.setAdapter(adapter)
    }

    fun enableDragItem(enable: Boolean) {
        if (enable) {
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0)
                }
                override fun onMove(recyclerView: RecyclerView, oldHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                    //使用集合工具类Collections，分别把中间所有的item的位置重新交换
                    val fromPosition: Int = oldHolder.adapterPosition //得到拖动ViewHolder的position
                    val toPosition: Int = targetHolder.adapterPosition //得到目标ViewHolder的position
                    if (fromPosition < toPosition) {
                        for (i in fromPosition until toPosition) {
                            Collections.swap(funItems, i, i + 1)
                        }
                    } else {
                        for (i in fromPosition downTo toPosition + 1) {
                            Collections.swap(funItems, i, i - 1)
                        }
                    }
                    adapter?.notifyItemMoved(fromPosition, toPosition)
                    val funItemNames =  funItems.joinToString(separator = ", "){it.skbMenuMode.name}
                    AppPrefs.getInstance().internal.keyboardSettingMenuAll.setValue(funItemNames)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

                override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = true

                override fun isLongPressDragEnabled() = false
            })

            adapter?.dragOverListener = object : MenuAdapter.DragOverListener {
                override fun startDragItem(holder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(holder)
                }
                override fun onOptionClick(parent: RecyclerView.Adapter<*>?, v: SkbFunItem, position: Int) {
                    val keyboardBarMenuCommon = AppPrefs.getInstance().internal.keyboardBarMenuCommon.getValue().split(", ").toMutableList()
                    if(keyboardBarMenuCommon.contains(v.skbMenuMode.name)){
                        keyboardBarMenuCommon.remove(v.skbMenuMode.name)
                    } else {
                        keyboardBarMenuCommon.add(v.skbMenuMode.name)
                    }
                    AppPrefs.getInstance().internal.keyboardBarMenuCommon.setValue(keyboardBarMenuCommon.joinToString())
                    inputView.freshCandidatesMenuBar()
                    adapter?.notifyItemChanged(position)
                }
            }
            itemTouchHelper.attachToRecyclerView(mRVMenuLayout)
        } else {
            adapter?.dragOverListener = null
        }
        adapter?.notifyDataSetChanged()
    }

    /**
     * 弹出键盘界面
     */
    fun showSkbSelelctModeView() {
        val funItems: MutableList<SkbFunItem> = LinkedList()
        funItems.add(
            SkbFunItem(
                mContext.getString(R.string.keyboard_name_t9),
                R.drawable.selece_input_mode_py9,
                SkbMenuMode.PinyinT9
            )
        )
        funItems.add(
            SkbFunItem(
                mContext.getString(R.string.keyboard_name_cn26),
                R.drawable.selece_input_mode_py26,
                SkbMenuMode.Pinyin26Jian
            )
        )
        funItems.add(
            SkbFunItem(
                mContext.getString(R.string.keyboard_name_hand),
                R.drawable.selece_input_mode_handwriting,
                SkbMenuMode.PinyinHandWriting
            )
        )
        val doublePYSchemaMode = AppPrefs.getInstance().input.doublePYSchemaMode.getValue()
        val doublePinyinSchemaName = when (doublePYSchemaMode) {
            DoublePinyinSchemaMode.flypy -> R.string.double_pinyin_flypy_plus
            DoublePinyinSchemaMode.natural -> R.string.double_pinyin_natural
            DoublePinyinSchemaMode.abc -> R.string.double_pinyin_abc
            DoublePinyinSchemaMode.mspy -> R.string.double_pinyin_mspy
            DoublePinyinSchemaMode.sogou -> R.string.double_pinyin_sougou
            DoublePinyinSchemaMode.ziguang -> R.string.double_pinyin_ziguang
        }
        funItems.add(
            SkbFunItem(
                mContext.getString(doublePinyinSchemaName),
                R.drawable.selece_input_mode_dpy26,
                SkbMenuMode.Pinyin26Double
            )
        )
        funItems.add(
            SkbFunItem(
                mContext.getString(R.string.keyboard_name_pinyin_lx_17),
                R.drawable.selece_input_mode_lx17,
                SkbMenuMode.PinyinLx17
            )
        )
        val adapter = MenuAdapter(context, funItems)
        adapter.setOnItemClickLitener { _: RecyclerView.Adapter<*>?, _: View?, position: Int ->
            onKeyboardMenuClick(funItems[position])
        }
        mRVMenuLayout!!.setAdapter(adapter)
    }

    private fun onKeyboardMenuClick(data: SkbFunItem) {
        val keyboardValue: Int
        val value = when (data.skbMenuMode) {
            SkbMenuMode.Pinyin26Jian -> {
                keyboardValue = 0x1000
                CustomConstant.SCHEMA_ZH_QWERTY
            }

            SkbMenuMode.PinyinHandWriting -> {
                keyboardValue = 0x3000
                CustomConstant.SCHEMA_ZH_HANDWRITING
            }

            SkbMenuMode.PinyinLx17 -> {
                keyboardValue = 0x6000
                CustomConstant.SCHEMA_ZH_DOUBLE_LX17
            }

            SkbMenuMode.Pinyin26Double -> {
                keyboardValue = 0x1000
                val doublePYSchemaMode = AppPrefs.getInstance().input.doublePYSchemaMode.getValue()
                CustomConstant.SCHEMA_ZH_DOUBLE_FLYPY + doublePYSchemaMode
            }
            else ->{
                keyboardValue = 0x2000
                CustomConstant.SCHEMA_ZH_T9
            }
        }
        val inputMode = keyboardValue or InputModeSwitcherManager.MASK_LANGUAGE_CN or InputModeSwitcherManager.MASK_CASE_UPPER
        AppPrefs.getInstance().internal.inputMethodPinyinMode.setValue(inputMode)
        AppPrefs.getInstance().internal.pinyinModeRime.setValue(value)
        mInputModeSwitcher!!.saveInputMode(inputMode)
        KeyboardLoaderUtil.instance.clearKeyboardMap()
        KeyboardManager.instance.clearKeyboard()
        KeyboardManager.instance.switchKeyboard(mInputModeSwitcher!!.skbLayout)
    }
}
