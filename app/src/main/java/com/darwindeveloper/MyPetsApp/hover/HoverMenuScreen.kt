package com.darwindeveloper.MyPetsApp.hover

import android.content.Context
import android.view.View
import io.mattcarroll.hover.Content
import android.view.Gravity
import android.widget.TextView
import android.support.annotation.NonNull


/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
class HoverMenuScreen(context: Context, pageTitle: String) : Content {

    private val mContext: Context
    private val mPageTitle: String
    private val mWholeScreen: View

    init {
        this.mContext = context
        this.mPageTitle = pageTitle
        mWholeScreen = createScreenView()
    }


    private fun createScreenView(): View {
        val wholeScreen = TextView(mContext)
        wholeScreen.text = "Screen: " + mPageTitle
        wholeScreen.gravity = Gravity.CENTER
        return wholeScreen
    }

    override fun onShown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getView(): View {
        return mWholeScreen
    }

    override fun onHidden() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isFullscreen(): Boolean {
        return true
    }

}