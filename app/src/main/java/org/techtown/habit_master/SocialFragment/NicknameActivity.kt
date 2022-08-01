package org.techtown.habit_master.SocialFragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.techtown.habit_master.R
import org.techtown.habit_master.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityNicknameBinding
    private var nickname : String? = null
    private val database = Firebase.database//파이어베이스 연동

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.completeButton.setOnClickListener{
            nickname = mBinding.editText.text.toString()
        }





    }
}