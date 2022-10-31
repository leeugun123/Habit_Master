package org.techtown.habit_master.CheckFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.habit_master.R
import org.techtown.habit_master.databinding.FragmentCheckBinding

class CheckFragment : Fragment() {

    private var mBinding: FragmentCheckBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = FragmentCheckBinding.inflate(layoutInflater)
        mBinding = binding


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_check,container,false)

        return rootView

    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}