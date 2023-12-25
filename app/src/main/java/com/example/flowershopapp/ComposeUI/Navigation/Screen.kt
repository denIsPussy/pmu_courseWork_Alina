package com.example.flowershopapp.ComposeUI.Navigation

import androidx.annotation.StringRes
import com.example.flowershopapp.R

enum class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: Int?,
    val showInBottomBar: Boolean = true,
    val showTopBarAndNavBar: Boolean = true
) {
    Signup(
        "signup",
        R.string.signup_title,
        null,
        showInBottomBar = false,
        showTopBarAndNavBar = false
    ),
    Login(
        "login",
        R.string.login_title,
        null,
        showInBottomBar = false,
        showTopBarAndNavBar = false
    ),
    Boot("boot", R.string.boot_title, null, showInBottomBar = false, showTopBarAndNavBar = false),
    Orders(
        "orders",
        R.string.boot_title,
        null,
        showInBottomBar = false,
        showTopBarAndNavBar = false
    ),
    OrderBouquets(
        "bouquets/{id}",
        R.string.boot_title,
        null,
        showInBottomBar = false,
        showTopBarAndNavBar = false
    ),
    BouquetCatalog("bouquet-catalog", R.string.bouquet_catalog_title, R.drawable.icons8_home),
    ShoppingCart("shopping-cart", R.string.shoppingCart_title, R.drawable.icons8_cart),
    Profile("profile", R.string.profile_title, R.drawable.icons8_user),
    Favorite("favorite", R.string.favorite_title, R.drawable.icons8_favorite),
    Statistics(
        "statistics",
        R.string.favorite_title,
        R.drawable.icons8_favorite,
        showInBottomBar = false,
        showTopBarAndNavBar = false
    ),
    PopulateBouquets("populate-bouquets", R.string.populate_title, R.drawable.fire);


    companion object {
        val bottomBarItems = listOf(
            BouquetCatalog,
            PopulateBouquets,
            Favorite,
            ShoppingCart,
            Profile
        )

        fun getItem(route: String): Screen? {
            val findRoute = route.split("/").first()
            return values().find { value -> value.route.startsWith(findRoute) }
        }
    }
}