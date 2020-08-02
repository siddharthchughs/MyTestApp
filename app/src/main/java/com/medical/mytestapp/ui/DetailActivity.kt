package com.medical.mytestapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.medical.mytestapp.R
import com.medical.mytestapp.fragment.DetailFragment
import com.medical.mytestapp.fragment.SearchFragment
import com.medical.mytestapp.fragment.SelectedItemFragment

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SelectedItemFragment.newInstance())
                .commitNow()
        }
    }
}