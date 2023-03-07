package com.elka.heofficeclub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elka.heofficeclub.view.dialog.InformDialog
import com.elka.heofficeclub.view.dialog.LoadingDialog

class MainActivity : AppCompatActivity() {
    val informDialog by lazy { InformDialog(this) }
    val loadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TheOfficeClub)
        setContentView(R.layout.activity_main)
    }
}