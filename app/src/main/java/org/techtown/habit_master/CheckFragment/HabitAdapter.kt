package org.techtown.habit_master.CheckFragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.techtown.habit_master.Share.ShareActivity
import org.techtown.habit_master.databinding.ListItemBinding

class HabitAdapter (private val items: ArrayList<org.techtown.habit_master.RoutineFragment.Challenge>, val context : Context): RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HabitAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =  ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    inner class ViewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: org.techtown.habit_master.RoutineFragment.Challenge) {

            binding.textListTitle.text = item.habitTitle

            Glide.with(itemView.context)
                .load(item.haibtImg)
                .override(400, 500)
                .into(binding.habitImg)
            //storage로부터 이미지 가져와 glide로 사진 붙이기


            itemView.setOnClickListener{


                Intent(context, ShareActivity::class.java).apply {

                    putExtra("habitTitle",item.habitTitle)
                    //습관 제목 삽입

                    //putExtra("habitAcImg",item.haibtImg)

                    //putExtra("habitComment",item.comment)

                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    //새로운 테스크를 생성하여 그 테스크안에 액티비티를 추가할때 사용합니다.
                    // 단, 기존에 존재하는 테스크들중에 생성하려는 액티비티와 동일한 affinity를 가지고 있는 테스크가 있다면 그곳으로 액티비티가 들어가게 됩니다.


                }.run { context.startActivity(this) }


            }//리사이클러뷰 클릭 이벤트

        }


    }


}