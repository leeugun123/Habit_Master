package org.techtown.habit_master.CheckFragment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.databinding.ActivityHabitBinding

class HabitActivity : AppCompatActivity(){

    private lateinit var mBinding : ActivityHabitBinding

    //데이터 쓰기
    var writeDatabase = FirebaseDatabase.getInstance()
    var databaseReference = writeDatabase.getReference()

    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityHabitBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        var habitTitle = intent.getStringExtra("habitTitle")
        var habitAcImg = intent.getStringExtra("habitAcImg")
        var habitComment = intent.getStringExtra("habitComment")

        Glide.with(this)
            .load(habitAcImg)
            .override(500, 600)
            .into(mBinding.habitActImg)
        //이전 intent로부터 glide로 사진 붙이기

        mBinding!!.habitView.setText(habitTitle)
        mBinding!!.habitComment.setText(habitComment)


        mBinding.startButton.setOnClickListener{

            UserApiClient.instance.me { user, error ->

                if (error != null) {
                    //Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {

                    uid = user.id.toString()
                }

                databaseReference.child("Habits").child(habitTitle.toString())
                    .child("uid").child(uid.toString()).setValue(true)

                Toast.makeText(this, "참여를 시작합니다.", Toast.LENGTH_SHORT).show()

            }


        }


    }


}