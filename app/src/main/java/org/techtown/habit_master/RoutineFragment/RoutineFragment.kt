package org.techtown.habit_master.RoutineFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.R
import org.techtown.habit_master.databinding.FragmentRoutineBinding


class RoutineFragment : Fragment(){

    private var mBinding : FragmentRoutineBinding? = null

    private val list : ArrayList<Challenge> = ArrayList()
    lateinit var recyclerView: RecyclerView

    private val database = Firebase.database//파이어베이스 연동
    val myHabits = database.getReference("Habits")
    //Habit 데이터 가져오기

    lateinit var uid :String
    //카카오 uid



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = FragmentRoutineBinding.inflate(layoutInflater)
        mBinding = binding

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_routine,container,false)

        //서버로부터 리스트들을 가져옴


        UserApiClient.instance.me { user, error ->

            if (error != null) {
                //Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {

                uid = user.id.toString()
            }


            myHabits.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    list.clear()//데이터가 변경되었을때 다시 불러옴

                    val test = snapshot

                    for(ds in test.children){

                        Log.e("snap",ds.key.toString()+" "+ds.child("uri").value.toString())

                        if(!(ds.child("uid").child(uid).value as Boolean)){

                            list.add(Challenge(ds.key.toString(),ds.child("uri").value.toString(),ds.child("comment").value.toString()))

                        }
                        //파베로부터 데이터 가져오기

                    }

                    recyclerView = rootView.findViewById(R.id.CheckRecyclerView) as RecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = ChallengeAdapter(list,requireContext())

                }

                override fun onCancelled(error: DatabaseError) {

                }


            })

        }


        return rootView

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }




}
