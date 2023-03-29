package com.skyviewads.foodcourt.utils

import android.content.Context
import android.widget.Toast

class Utils {
    companion object {
        private var toast: Toast? = null
        fun toasty( context: Context,msg: String?) {
            msg?.let {
                if (toast != null) {
                    toast!!.cancel()
                    toast = null
                }
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast!!.show()
            }
        }

    }
}