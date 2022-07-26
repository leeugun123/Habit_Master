package org.techtown.habit_master.Share

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.techtown.habit_master.R
import org.techtown.habit_master.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.takePicture.setOnClickListener{



        }





    }
}