package com.codingdrama.roamie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codingdrama.roamie.model.PermissionUtility
import com.codingdrama.roamie.ui.composables.PageAdventures
import com.codingdrama.roamie.ui.composables.PageExplorer
import com.codingdrama.roamie.ui.theme.RoamieTheme
import com.codingdrama.roamie.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    enum class Destination {
        ADVENTURES, EXPLORER
    }

    private val viewModel: MainViewModel by viewModels()

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PermissionUtility.REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            RoamieTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            windowInsets = NavigationBarDefaults.windowInsets,
                            tonalElevation = 4.dp
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route

                            NavigationBarItem(
                                selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == currentRoute } == true,
                                onClick = {
                                    navController.navigate(Destination.ADVENTURES.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Adventures",
                                    tint = if (currentRoute == Destination.ADVENTURES.name) Color.Blue else Color.Gray) },
                                label = { Text(Destination.ADVENTURES.name) },
                                alwaysShowLabel = true
                            )

                            NavigationBarItem(
                                selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == currentRoute } == true,
                                onClick = {
                                    navController.navigate(Destination.EXPLORER.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.TwoTone.LocationOn,
                                    contentDescription = "EXPLORER",
                                    tint = if (currentRoute == Destination.EXPLORER.name) Color.Blue else Color.Gray) },
                                label = { Text(Destination.EXPLORER.name) },
                                alwaysShowLabel = true
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                openCameraPreview()
                            }, shape = CircleShape, containerColor = Color(0xFF6200EE)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                tint = Color.White,
                                contentDescription = "Camera"
                            )
                        }
                    },
//                    floatingActionButtonPosition = FabPosition.Center,
//                    isFloatingActionButtonDocked = true
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destination.ADVENTURES.name,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Destination.ADVENTURES.name) {
                            MainPage(modifier = Modifier.padding(innerPadding), Destination.ADVENTURES)
                        }
                        composable(Destination.EXPLORER.name) {
                            MainPage(modifier = Modifier.padding(innerPadding), Destination.EXPLORER)
                        }
                    }
                }
            }
        }

        // Request camera permissions
        if (!PermissionUtility.allPermissionsGranted(this)) {
            PermissionUtility.requestPermissions(activityResultLauncher)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Composable
    fun MainPage(modifier: Modifier = Modifier, page: Destination) {
        Log.d(TAG, "MainPage: mode = ${page.name}")
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (page == Destination.ADVENTURES) {
                    PageAdventures()
                } else {
                    PageExplorer()
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun MainActivityPreview() {
        RoamieTheme {
            MainPage(page = Destination.ADVENTURES)
        }
    }

    private fun openCameraPreview() {
        Log.d(TAG, "openCameraPreview: ")
        startActivity(Intent(this, CameraActivity::class.java))
    }
}

