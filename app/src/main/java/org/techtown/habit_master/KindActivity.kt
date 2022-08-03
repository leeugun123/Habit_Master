package org.techtown.habit_master

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.techtown.habit_master.databinding.ActivityKindBinding

class KindActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityKindBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



    }
}