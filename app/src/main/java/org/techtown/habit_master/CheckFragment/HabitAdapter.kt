package org.techtown.habit_master.CheckFragment

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.R
import org.techtown.habit_master.RoutineFragment.Challenge
import org.techtown.habit_master.RoutineFragment.ChallengeAdapter
import org.techtown.habit_master.Share.Share
import org.techtown.habit_master.Share.ShareActivity
import org.techtown.habit_master.Share.ShareAdapter
import org.techtown.habit_master.databinding.ListItemBinding
import java.time.LocalDate

class HabitAdapter (private val items: ArrayList<org.techtown.habit_master.RoutineFragment.Challenge>, val context : Context): RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HabitAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.apply {
            this.bind(item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =  ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    inner class ViewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: org.techtown.habit_master.RoutineFragment.Challenge) {


            Glide.with(itemView.context)
                .load(item.haibtImg)
                .override(400, 500)
                .into(binding.habitImg)
            //storage로부터 이미지 가져와 glide로 사진 붙이기

            binding.textListTitle.text = item.habitTitle
            //습관 제목 붙이기

            //오늘 인증했는지 체크 여부
            val database = Firebase.database//파이어베이스 연동

            val date : String = LocalDate.now().toString().replace("-","")

            UserApiClient.instance.me { user, error ->

                if (error != null) {
                    //Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {

                    var  uid = user.id.toString()

                    database.reference.child("Habits")
                        .child(item.habitTitle).child("date")
                        .child(date).child(uid).addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {

                               val share = snapshot.child("checked").getValue()



                            }

                            override fun onCancelled(error: DatabaseError) {


                            }


                        })


                }

            }


            itemView.setOnClickListener{


                Intent(context, ShareActivity::class.java).apply {

                    putExtra("habitTitle",item.habitTitle)
                    //습관 제목 삽입

                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    //새로운 테스크를 생성하여 그 테스크안에 액티비티를 추가할때 사용합니다.
                    // 단, 기존에 존재하는 테스크들중에 생성하려는 액티비티와 동일한 affinity를 가지고 있는 테스크가 있다면 그곳으로 액티비티가 들어가게 됩니다.


                }.run { context.startActivity(this) }


            }//리사이클러뷰 클릭 이벤트

        }


    }


}