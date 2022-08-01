package org.techtown.habit_master.SocialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.habit_master.databinding.FragmentSocialBinding

class SocialFragment : Fragment(){

    //private val fireDatabase = FirebaseDatabase.getInstance().reference
    //파이어베이스 참조


    private var mBinding : FragmentSocialBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentSocialBinding.inflate(layoutInflater)
        mBinding = binding



    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return mBinding?.root

    }

    override fun onDestroyView() {

        mBinding = null
        super.onDestroyView()
    }




}