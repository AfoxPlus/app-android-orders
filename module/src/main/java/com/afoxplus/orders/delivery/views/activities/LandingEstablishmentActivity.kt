package com.afoxplus.orders.delivery.views.activities

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.afoxplus.orders.delivery.viewmodels.LandingEstablishmentViewModel
import com.afoxplus.products.delivery.views.screens.LandingProductScreen
import com.afoxplus.uikit.activities.UIKitBaseActivity
import com.afoxplus.uikit.designsystem.foundations.UIKitTheme
import com.afoxplus.uikit.designsystem.molecules.UIKitTopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingEstablishmentActivity : UIKitBaseActivity() {

    private val landingEstablishmentViewModel: LandingEstablishmentViewModel by viewModels()

    companion object {
        fun newInstance(activity: Activity): Intent {
            return Intent(activity, LandingEstablishmentActivity::class.java)
        }
    }

    override fun setMainView() {
        setContent {
            UIKitTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        UIKitTopBar(
                            title = "Restaurante",
                            description = landingEstablishmentViewModel.restaurantName(),
                            onBackAction = {
                                onBackPressedDispatcher.onBackPressed()
                            })
                    }, contentWindowInsets = WindowInsets.safeGestures
                ) { paddingValues ->
                    LandingProductScreen(
                        modifier = Modifier.consumeWindowInsets(paddingValues),
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }

    override fun setUpView() {

    }
}