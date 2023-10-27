package com.afoxplus.orders.demo.global

import android.content.Intent
import com.afoxplus.module.delivery.flow.StartDemoFlow
import com.afoxplus.orders.demo.MainActivity
import com.afoxplus.uikit.activities.UIKitBaseActivity
import javax.inject.Inject

class OrderStartDemoFlow @Inject constructor() : StartDemoFlow {

    override fun start(activity: UIKitBaseActivity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }

}