package org.techtown.habit_master.SettingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.techtown.habit_master.databinding.FragmentSettingBinding

class SettingFragment : Fragment(){

    private var mBinding : FragmentSettingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentSettingBinding.inflate(inflater,container,false)
        mBinding = binding
        return mBinding?.root

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }



}