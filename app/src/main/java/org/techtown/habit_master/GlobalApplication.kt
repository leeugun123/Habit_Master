package org.techtown.habit_master

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this,"f4cebb67d6aee386bad44eaa3a422cc4")

    }


}