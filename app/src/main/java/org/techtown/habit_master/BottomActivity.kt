package org.techtown.habit_master

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import org.techtown.habit_master.databinding.ActivityBottomBinding

class BottomActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityBottomBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(mBinding.myBottomNav,navController)

    }

}