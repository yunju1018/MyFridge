package com.yunju.myfridge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.yunju.myfridge.databinding.ActivityMainBinding
import com.yunju.myfridge.ui.home.FridgeFragment
import com.yunju.myfridge.ui.setting.SettingFragment
import com.yunju.myfridge.ui.shopping.ShoppingFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setLayout()
    }

    private fun setLayout() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            addFragment(FridgeFragment.newInstance())
        }

        binding.navigation.setOnItemSelectedListener {
            val fragment: Fragment = when (it.itemId) {
                R.id.fridge -> FridgeFragment.newInstance()
                R.id.shopping -> ShoppingFragment.newInstance()
                else -> SettingFragment.newInstance()
            }

            replaceFragment(fragment)
            true
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(binding.container.id, fragment).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.container.id, fragment).commit()
    }
}