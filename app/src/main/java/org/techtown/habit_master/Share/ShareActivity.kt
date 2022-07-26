package org.techtown.habit_master.Share

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.techtown.habit_master.databinding.ActivityShareBinding
import java.time.LocalDate

class ShareActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityShareBinding

    private val shareList : ArrayList<Share> = ArrayList()
    lateinit var recyclerView: RecyclerView

    private val database = Firebase.database//파이어베이스 연동
    var shareHabits = database.getReference("Habits")
    //Habit 데이터 가져오기

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        var shareTitle = intent.getStringExtra("habitTitle")

        mBinding.shareTitle.setText(shareTitle.toString())
        //습관제목 붙이기

        val onlyDate : LocalDate = LocalDate.now()//오늘 날짜 가져오기
        val date : String = onlyDate.toString().replace("-","")

        mBinding.date.setText(date)//오늘 날짜

        shareHabits = shareHabits.child(shareTitle.toString()).child("date").child(date)
        //선택한 것 가져오기

        shareHabits.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                shareList.clear()//데이터가 변경되었을때 다시 불러옴

                for(ds in snapshot.children){

                    Log.e(TAG,ds.child("pic").value.toString())

                    shareList.add(Share(ds.child("pic").value.toString()))
                    //파베로부터 데이터 가져오기

                }

                recyclerView = mBinding.shareRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                recyclerView.adapter = ShareAdapter(shareList,applicationContext)
                //리사이클러뷰 붙이기

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


        mBinding.uploadButton.setOnClickListener{


            /*
            val intent = Intent(this, UploadActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            //finish() 지금 액티비티 그대로 유지하기
            */

        }//이미지 저장하기



    }
}