package org.techtown.habit_master

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityNicknameBinding
    private var nickname : String? = null
    private var uid : String? = null
    var writeDatabase = FirebaseDatabase.getInstance()
    var databaseReference = writeDatabase.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.completeButton.setOnClickListener{

            nickname = mBinding.editText.text.toString()

            UserApiClient.instance.me { user, error ->

                if (error != null) {
                    //Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {
                    uid = user.id.toString()
                }

                databaseReference.child("Users").child(uid.toString())
                    .child("nickName").setValue(nickname)

                var intent : Intent
                intent = Intent(this, BottomActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }


        }





    }
}