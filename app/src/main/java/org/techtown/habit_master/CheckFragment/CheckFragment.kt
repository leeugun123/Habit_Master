package org.techtown.habit_master.CheckFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient
import org.techtown.habit_master.R
import org.techtown.habit_master.RoutineFragment.Challenge
import org.techtown.habit_master.databinding.FragmentCheckBinding

class CheckFragment : Fragment() {

    private var mBinding: FragmentCheckBinding? = null

    private val list : ArrayList<org.techtown.habit_master.RoutineFragment.Challenge> = ArrayList()
    lateinit var recyclerView: RecyclerView
    private val database = Firebase.database//파이어베이스 연동
    val myHabits = database.getReference("Habits")
    //Habit 데이터 가져오기

    lateinit var uid :String
    //카카오 uid
    //데이터 쓰기
    var writeDatabase = FirebaseDatabase.getInstance()
    var databaseReference = writeDatabase.getReference()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = FragmentCheckBinding.inflate(layoutInflater)
        mBinding = binding


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val rootView = inflater.inflate(R.layout.fragment_check,container,false)


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

                    for(ds in snapshot.children){

                        Log.e("snap",ds.key.toString()+" "+ds.child("uri").value.toString())

                        if(ds.child("uid").child(uid).exists()){

                            if(ds.child("uid").child(uid).value as Boolean){
                                list.add(Challenge(ds.key.toString(), ds.child("uri").value.toString(), ds.child("comment").value.toString()))
                            }

                        }//uid가 습관에 등록되어있는 경우,
                        else{

                            databaseReference.child("Habits").child(ds.key.toString())
                                .child("uid").child(uid).setValue(false)
                            //데이터 쓰기

                        }//등록 되어있지 않는 경우 서버에 데이터를 쓴다.

                    }

                    //GridLayout RecyclerView
                    recyclerView = rootView.findViewById(R.id.recyclerGridView) as RecyclerView
                    recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

                    recyclerView.adapter = HabitAdapter(list,requireContext())

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