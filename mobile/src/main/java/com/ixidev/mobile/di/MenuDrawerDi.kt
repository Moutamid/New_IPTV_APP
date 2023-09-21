package com.ixidev.mobile.di

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import com.ixidev.mobile.R
import com.ixidev.mobile.databinding.DrawerBannerItemBinding
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionText
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped


@InstallIn(ActivityComponent::class)
@Module
object MenuDrawerDi {


    @Provides
    @ActivityScoped
    fun provideMenuItems(@ActivityContext context: Context): DrawerItemsProvider {
        return DrawerItemsProvider(
            createMenuItems(context),
            createStickyItems(context)
        )
    }

    private fun createMenuItems(context: Context): List<IDrawerItem<*>> {
        return listOf(

            PrimaryDrawerItem().apply {
                nameText = context.getString(R.string.item_add_play_list)
                descriptionText = context.getString(R.string.item_add_play_list_description)
                iconicsIcon = FontAwesome.Icon.faw_list
                tag = MenuItemTag.Playlists
                isSelected = false

            },
            PrimaryDrawerItem().apply {
                nameText = context.getString(R.string.item_favorites)
                iconicsIcon = FontAwesome.Icon.faw_heart
                tag = MenuItemTag.Favorites
                isSelected = false
            },
            PrimaryDrawerItem().apply {
                nameText = "Remove Ads"
                iconicsIcon = FontAwesome.Icon.faw_donate
                tag = MenuItemTag.StopAds
                isSelected = false
            },
            DividerDrawerItem(),
            PrimaryDrawerItem().apply {
                nameText = context.getString(R.string.item_rate_app)
                tag = MenuItemTag.RateApp
                iconicsIcon = FontAwesome.Icon.faw_google_play
                isSelectable = false
            },
            PrimaryDrawerItem().apply {
                nameText = context.getString(R.string.settings)
                tag = MenuItemTag.Settings
                iconicsIcon = FontAwesome.Icon.faw_cog
                isSelectable = false
            },
            DividerDrawerItem(),
            PrimaryDrawerItem().apply {
                nameText = "New Apps Download"
                tag = MenuItemTag.NewApp
                iconRes = R.drawable.resource_new
                isSelectable = false
            },
/*            ProfileSettingDrawerItem().apply {
                identifier = 1000L // Choose a unique identifier
                nameText = "" // No text is displayed
                descriptionText = "" // No description text is displayed
                tag = MenuItemTag.Banner
                iconRes = R.drawable.banner // Replace with your image resource
                isSelectable = false
            },*/

        )
    }

    private fun createStickyItems(context: Context): List<IDrawerItem<*>> {
        return listOf(/*
            SecondaryDrawerItem().apply {
                nameText = context.getString(R.string.item_rate_app)
                tag = MenuItemTag.RateApp
                iconicsIcon = FontAwesome.Icon.faw_google_play
                isSelectable = false
            },
            SecondaryDrawerItem().apply {
                nameText = context.getString(R.string.settings)
                tag = MenuItemTag.Settings
                iconicsIcon = FontAwesome.Icon.faw_cog
                isSelectable = false
            },*/
        )
    }


    sealed class MenuItemTag {
        object Home : MenuItemTag()
        object Settings : MenuItemTag()
        object Favorites : MenuItemTag()
        object Playlists : MenuItemTag()
        object RateApp : MenuItemTag()
        object DarkMode : MenuItemTag()
        object Donation : MenuItemTag()
        object StopAds : MenuItemTag()
        object NewApp : MenuItemTag()
        object Banner : MenuItemTag()
    }

}