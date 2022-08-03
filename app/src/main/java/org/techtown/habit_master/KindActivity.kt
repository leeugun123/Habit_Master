package org.techtown.habit_master

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.databinding.ActivityKindBinding

class KindActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKindBinding

    private lateinit var uid : String
    var writeDatabase = FirebaseDatabase.getInstance()
    var databaseReference = writeDatabase.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityKindBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.repeater.setOnClickListener{

            writeKindPerson("N수생")
            moveActivity()
        }//N수생

        mBinding.freshman.setOnClickListener{

            writeKindPerson("취준생")
            moveActivity()

        }//취준생

        mBinding.pubStudent.setOnClickListener{

            writeKindPerson("공시생")
            moveActivity()

        }//공시생

    }

    private fun moveActivity() {

        var intent : Intent
        intent = Intent(this, BottomActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()

    }

    private fun writeKindPerson(kind : String) {

        UserApiClient.instance.me { user, error ->

            if (error != null) {
                //Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {

               uid = user.id.toString()
            }

            databaseReference.child("Users").child(uid)
                .child("kindPerson").setValue(kind)
            //파이어베이스 데이터에 쓰기
        }
    }

}