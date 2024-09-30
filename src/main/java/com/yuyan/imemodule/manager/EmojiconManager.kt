package com.yuyan.imemodule.manager

import android.annotation.SuppressLint
import com.yuyan.imemodule.application.LauncherModel
import com.yuyan.imemodule.entity.emojicon.NatureEmoji
import com.yuyan.imemodule.entity.emojicon.ObjectsEmoji
import com.yuyan.imemodule.entity.emojicon.PeopleEmoji
import com.yuyan.imemodule.entity.emojicon.PlacesEmoji
import com.yuyan.imemodule.entity.emojicon.SymbolsEmoji

class EmojiconManager private constructor() {
    private val mEmojis : HashMap<Int, Array<String>> = HashMap()
    fun getmSymbolsData(): HashMap<Int, Array<String>> {
        return mEmojis
    }
    fun getmSymbols(): Array<String> {
        return LauncherModel.instance.usedEmojiDao!!.allUsedEmoji
    }

    init {
        mEmojis[0] = getmSymbols()
        mEmojis[1] = PeopleEmoji.DATA
        mEmojis[2] = NatureEmoji.DATA
        mEmojis[3] = ObjectsEmoji.DATA
        mEmojis[4] = PlacesEmoji.DATA
        mEmojis[5] = SymbolsEmoji.DATA
        mEmojis[6] = SMILE
    }

    companion object {
        fun initInstance() {
            instance = EmojiconManager()
        }

        @SuppressLint("StaticFieldLeak")
        var instance: EmojiconManager? = null
            private set

        val SMILE: Array<String> = arrayOf(
            "^_^","> <",">_<",">_<||","-_-b","-_-C","-_-||","-_-#","←_←","→_→","|ω·)","ↁ_ↁ","●rz","★rz","口rz","○rz",
            "囧rz","冏rz","崮rz","莔rz","商rz","囧rz=З","\\囧/","\\莔/","Ծ‸Ծ","- -","- -~","- -?","= =","= =~",
            "= =?","=.=","=。=","=3=","=W=","XD","^^","ToT","T_T","TAT","0.0","ಠ_ಠ","o_O","@_@",":)",":D",
            ":P",":(",":-)",":-D",":-P",":-(",";-)",":-O","●︿●","●﹏●","●０●","●▽●","๑乛◡乛๑","(ㅍ_ㅍ)","(つд⊂)",
            "ฅ^ω^ฅ","(〃ω〃)","(≧ω≦)","(눈_눈)","(ΘωΘ)","◑﹏◐","╯﹏╰","(⌒▽⌒)","(￣3￣)","(･∀･)","(<_<)","(>_>)",
            "(˘❥˘)","*^_^*","⊙﹏⊙‖∣","⊙︿⊙","⊙﹏⊙","(⊙０⊙)","(⊙ｏ⊙)","(┬＿┬)","(＞﹏＜)","(ˉ﹃ˉ)","(￣▽￣)","凸^-^凸",
            "凹^-^凹","︻┳═一","︻︼─一","▄︻┳一·","▄︻┳═一","(☆_☆)","(￣.￣)","(ಥ_ಥ)","(⊙ꇴ⊙)","一 一+","(ˇ︿ˇ﹀","(･ิω･ิ)",
            "(⁄ ⁄•⁄ω⁄•⁄ ⁄)","(๑¯◡¯๑)","( ¯•ω•¯ )","(╬◣д◢)","✧( ु•⌄• )◞","( ¯•ω•¯ )","ヽ(‘ ∇‘ )ノ","(இдஇ; )","(o´ω`o)",
            "(•౪• )","(๑꒪⍘꒪๑)","∠( ᐛ 」∠)＿","(๑•̀ㅂ•́) ✧","ლ(╹◡╹ლ)","_(:з」∠)_","( •̥́ ˍ •̀ू )","(๑•́ ∀ •̀๑)","(ง •̀_•́)ง",
            "(⇀‸↼‶)","(。・ω・)","(=・ω・=)","(/・ω・\\)","(°∀°)ﾉ","(\"▔□▔)/","(;¬_¬)","((^ω^))","( *・ω・)✄╰ひ╯",
            "(=^・・^=)","(￣(工)￣)","(￣▽￣\")","（/TДT)/","(｡･ω･｡)","(^・ω・^ )","(´･_･`)","(-_-#)","ヽ(`Д´)ﾉ",
            ">////<","ლ(･ิω･ิლ)","(๑•̀.̫•́๑)","(ʘдʘ╬)","d(ŐдŐ๑)","\\(^o^)/","└(^o^)┘","Y(^_^)Y","::>_<::","~>_<~+","o(-\"-)o",
            "^*(- -)*^","($ _ $)","(⊙_⊙)?","(>^ω^<)","\\(≥ω≤)/","(^。^)y-~~","(^_^)∠※","(#‵′)凸","<(‵^′)>","(─.─|||",
            "ψ(╰_╯)","(ㄒoㄒ)//","Σ(ﾟдﾟ;)","√(─皿─)√","(/ □ \\)","<(‵□′)/","\\(\"▔□▔)/","((‵□′))","<(￣︶￣)>","╭(′▽`)╯",
            "╰(′▽`)╮","( ´ ▽ ` )ﾉ","╮(╯▽╰)╭","( # ▽ # )","╮(╯3╰)╭","╭(╯^╰)╮","\\(0^◇^0)/","≡[。。]≡","∑(°Д.°)","(;￣ェ￣)",
            "(⌒-⌒; )","(=・ω・=)","( ´_ゝ｀)","(´；ω；`)","(＃－－)/ .","(｀・ω・´)","( •̥́ ˍ •̀ू )","(〜￣△￣)〜","╮(￣▽￣)╭",
            "(ﾟДﾟ≡ﾟдﾟ)!?","Σ( ￣□￣||)","(￣ε(#￣) Σ","ㄟ(￣▽￣ㄟ)","（●＾o＾●）","(╯°口°)╯(┴—┴","（#-_-)┯━┯","‵（*∩_∩*）′",
            "‵（*^﹏^*）′","^_^o~ 努力！","-_-。sorry！","~\\(≧▽≦)/~","(～﹃～)~zZ","o(一＾一+)o","╭(￣m￣*)╮","<(￣oo,￣)/",
            "ㄟ(川.一ㄟ)","(～￣▽￣)ノ","(*￣(工)￣)","(●￣(ｴ)￣●)","╭(﹊∩∩﹊#)╮","p(*///▽///*)q","ε=ε=(ノ≧∇≦)ノ","(≧∇≦)/(*^ω^*)",
            "(￣o￣) . z Z","(〜￣▽￣)〜","(๑＞ڡ＜)","☆ (´▽｀)ノ♪","ε٩(๑> ₃ <)۶з","(^_^メ)","(❁´ω`❁)","(｡･ω･｡)ﾉ♡","(ಡ艸ಡ)噗",
            "(◉ω◉ )","(*^▽^)/★*☆","ԅ(¯㉨¯ԅ)","(\"▔㉨▔)","（*/㉨＼*）","(๑‾᷅㉨‾᷅๑)","(  •㉨•¯ ;)","(〜 ;￣㉨￣)〜","( ;·̀㉨·́)ง",
            "ヽ(○^㉨^)ﾉ♪","(∪㉨∪)｡｡｡zzz","「◦㉨◦」不会吧","(๑•̀㉨•́ฅ✧ 早","(｡˘•㉨•˘｡)心疼..","♪～(´ε｀　)","（ ´^ิω^ิ｀）","ヾ(@゜∇゜@)ノ",
            "_(:3」∠❀)_","ヾ(Ő∀Ő๑)ﾉ太好惹","(=´∀｀)人(´∀｀=)","(ノ=Д=)ノ┻━┻","(╯°Д°)╯︵┴┴","(/≧▽≦)/~┴┴","（╯‵□′）╯︵┴─┴",
            "╭(￣▽￣)╯╧═╧","（\\#-_-)\\┯━┯","┴┴~(≧▽≦)/~┴┴","＼（´Ｏ‘）／","(+﹏+)~狂晕","(ˇ?ˇ)  想～","o(︶︿︶)o 唉","(￣︶￣)↗ 涨",
            "(┬＿┬)↘ 跌","哼(ˉ(∞)ˉ)唧","( ^_^ )/~~拜拜","d(╯﹏╰)b 咕~~","(^人^) 拜托啦~","( ^_^ )不错嘛!","o(∩_∩)o 哈哈","(*^__^*) 嘻嘻",
            "o(≧v≦)o~~好棒","o(>﹏<)o不要啊！","(*+﹏+*)~ 受不了！","(°ο°)~@ 晕倒了..","~\\(≧▽≦)/~啦啦啦","<(￣▽￣)> 哇哈哈…",
            "(Θ㉨Θ)你们还是图样","┣▇▇▇═─","▄︻┻═┳一","(｡･ω･｡)ﾉ♡(>﹏<)","٩( ;•̀㉨•́ ;)و get！","( ⌒㉨⌒)人(⌒㉨⌒ )v","( •̥́ ㉨ •̀ू )嘤嘤嘤~",
            "( ͡ ͡° ͜ ʖ ͡ ͡°)","ヽ(*^㉨^)人(^㉨^*)ノ","┐( ;‾᷅㉨‾᷅ ;)┌ ;怪我咯","( ,,´･㉨･)ﾉ\"(´っω･｀｡)摸头","╭( ′• ㉨ •′ )╭☞警察叔叔！就是这个人！",
            "ε=ε=ε=ε=ε=ε=┌(;ˉ㉨ˉ)┘","( ´°̥̥̥̥̥̥̥̥ω°̥̥̥̥̥̥̥̥`)","ε=ε=ε=(ﾉ*~㉨~)ﾉε=ε=ε=(ﾟ㉨ﾟﾉ)ﾉ","(•̤̀ᵕ•̤́๑)ᵒᵏᵎᵎᵎᵎ","(*◑∇◑)☞☜(◐∇◐*)","╭( ′• o •′ )╭☞就是这个人！",)
    }
}
