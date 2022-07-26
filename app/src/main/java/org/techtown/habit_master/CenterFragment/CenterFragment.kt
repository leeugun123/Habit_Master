package org.techtown.habit_master.CenterFragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import org.techtown.habit_master.databinding.FragmentCenterBinding

class CenterFragment : Fragment(){

    private var mBinding : FragmentCenterBinding? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = FragmentCenterBinding.inflate(layoutInflater)
        mBinding = binding
        //데이터 바인딩



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return mBinding?.root

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }


}