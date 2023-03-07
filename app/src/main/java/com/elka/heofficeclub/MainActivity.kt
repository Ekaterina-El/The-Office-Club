package com.elka.heofficeclub

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.view.dialog.InformDialog
import com.elka.heofficeclub.view.dialog.LoadingDialog

class MainActivity : AppCompatActivity() {
    val informDialog by lazy { InformDialog(this) }
    val loadingDialog by lazy { LoadingDialog(this) }

    val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TheOfficeClub)
        setContentView(R.layout.activity_main)
    }
}