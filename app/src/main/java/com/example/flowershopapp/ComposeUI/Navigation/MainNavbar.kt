package com.example.flowershopapp.ComposeUI.Navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flowershopapp.ComposeUI.Boot
import com.example.flowershopapp.ComposeUI.Bouquet.BouquetCatalog
import com.example.flowershopapp.ComposeUI.Bouquet.Favorite
import com.example.flowershopapp.ComposeUI.Bouquet.PopulateBouquets
import com.example.flowershopapp.ComposeUI.Order.OrderBouquets
import com.example.flowershopapp.ComposeUI.Order.Orders
import com.example.flowershopapp.ComposeUI.User.Login
import com.example.flowershopapp.ComposeUI.User.Profile
import com.example.flowershopapp.ComposeUI.User.Signup
import com.example.flowershopapp.ComposeUI.User.Statistics
import com.example.flowershopapp.Entities.ComposeUI.ShoppingCart
import com.example.flowershopapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(
    navController: NavHostController,
    currentScreen: Screen?
) {
    var showImage by remember { mutableStateOf(true) }
    TopAppBar(

        title = {
            if (showImage) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "ivi",
                            Modifier
                                .padding(end = 14.dp)
                                .size(180.dp)
                        )
                    }
                }
            }

        },
        navigationIcon = {
            if (
                navController.previousBackStackEntry != null
                && (currentScreen == null || !currentScreen.showInBottomBar)
            ) {
                showImage = false
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                showImage = true
            }
        }
    )
}

@Composable
fun Navbar1(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(55.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Screen.bottomBarItems.forEach { screen ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray
                ),
                modifier = modifier.padding(10.dp),
                icon = {
                    Icon(
                        painter = painterResource(screen.icon!!),
                        contentDescription = null
                    )
                },

                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun Navbar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Screen.bottomBarItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val contentColor =
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.8f)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .clickable(onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })

            ) {
                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    screen.icon?.let {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(screen.icon),
                            contentDescription = null,
                            tint = contentColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Navhost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startScreen: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startScreen,
        modifier.padding(innerPadding)
    ) {
        composable(Screen.Signup.route) { Signup(navController) }
        composable(Screen.Login.route) { Login(navController) }
        composable(Screen.Boot.route) { Boot(navController) }
        composable(Screen.BouquetCatalog.route) { BouquetCatalog(navController) }
        composable(Screen.PopulateBouquets.route) { PopulateBouquets(navController) }
        composable(Screen.Favorite.route) { Favorite(navController) }
        composable(Screen.ShoppingCart.route) { ShoppingCart(navController) }
        composable(Screen.Profile.route) { Profile(navController) }
        composable(Screen.Statistics.route) { Statistics(navController) }
        composable(
            Screen.Orders.route
        ) { backStackEntry ->
            backStackEntry.arguments?.let { Orders(navController) }
        }
        composable(
            Screen.OrderBouquets.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        )
        {
            OrderBouquets()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainNavbar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = currentDestination?.route?.let { Screen.getItem(it) }

    Scaffold(modifier = Modifier,
        topBar = {
            if (currentScreen == null || (currentScreen.showInBottomBar && currentScreen.showTopBarAndNavBar)) {
                Topbar(navController, currentScreen)
            }
        },
        bottomBar = {
            if (currentScreen == null || (currentScreen.showInBottomBar && currentScreen.showTopBarAndNavBar)) {
                Navbar(navController, currentDestination)

            }
        }
    ) { innerPadding ->
        Navhost(navController, innerPadding, Screen.Login.route)
    }
}