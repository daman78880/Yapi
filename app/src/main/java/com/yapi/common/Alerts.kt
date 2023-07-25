package com.yapi.common

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.yapi.MainActivity
import com.yapi.databinding.ProgressLayoutBinding

private var customDialog: AlertDialog? = null

fun showProgress() {
    hideProgress()
    val customAlertBuilder = AlertDialog.Builder(MainActivity.activity!!.get())
    val customAlertView = ProgressLayoutBinding.inflate(LayoutInflater.from(MainActivity.activity!!.get()), null, false)
    customAlertBuilder.setView(customAlertView.root)
    customAlertBuilder.setCancelable(false)
    customDialog = customAlertBuilder.create()
    customDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    customDialog!!.show()
}

fun hideProgress() {
    if (customDialog != null && customDialog?.isShowing!!) {
        customDialog?.dismiss()
    }
}