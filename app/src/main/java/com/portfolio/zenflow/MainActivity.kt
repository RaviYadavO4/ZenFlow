package com.portfolio.zenflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.portfolio.zenflow.presentation.home.HomeScreen
import com.portfolio.zenflow.presentation.home.HomeViewModel
import com.portfolio.zenflow.presentation.theme.ZenFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HomeViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            ZenFlowTheme(darkTheme = state.isDarkMode) {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
