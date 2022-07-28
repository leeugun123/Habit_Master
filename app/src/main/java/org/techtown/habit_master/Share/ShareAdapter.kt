package org.techtown.habit_master.Share

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.databinding.ShareHabitBinding

class ShareAdapter(private val items: ArrayList<Share>, val context : Context): RecyclerView.Adapter<ShareAdapter.ViewHolder>()
{
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ShareAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.apply {
            bind(item)
            itemView.tag = item
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ShareHabitBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)
    }

    // 각 항목에 필요한 기능을 구현
    inner class ViewHolder(private val binding : ShareHabitBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Share) {

            UserApiClient.instance.me { user, error ->

                if (error != null) {
                    //Log.e(TAG, "사용자 정보 요청 실패", error)
                } else if (user != null) {

                    var uid = user.id.toString()
                    binding.nickName.text = uid
                    //닉네임 설정
                    //일단 카카오 uid로 정함

                }
            }

            Glide.with(itemView.context)
                .load(item.shareImg)
                .override(400, 300)
                .into(binding.habitPic)
            //사진 붙이기

            binding.description.setText(item.description)
            //부가 설명 추가

        }

    }


}