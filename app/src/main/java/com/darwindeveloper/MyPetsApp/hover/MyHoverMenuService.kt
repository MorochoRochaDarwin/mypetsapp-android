package com.darwindeveloper.MyPetsApp.hover

import android.content.Context
import android.content.Intent
import io.mattcarroll.hover.HoverView
import io.mattcarroll.hover.window.HoverMenuService
import java.util.Collections.singletonList
import android.support.annotation.NonNull
import android.view.View
import android.widget.ImageView
import io.mattcarroll.hover.HoverMenu.SectionId
import android.widget.ImageView.ScaleType
import com.darwindeveloper.MyPetsApp.R
import io.mattcarroll.hover.Content
import io.mattcarroll.hover.HoverMenu
import java.util.*


/**
 * Created by DARWIN MOROCHO on 23/8/2017.
 */
public class MyHoverMenuService : HoverMenuService() {


    override fun onHoverMenuLaunched(intent: Intent, hoverView: HoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
    }

    private fun createHoverMenu(): HoverMenu {
        return SingleSectionHoverMenu(applicationContext)
    }


    private class SingleSectionHoverMenu internal constructor(private val mContext: Context) : HoverMenu() {
        private val mSection: HoverMenu.Section

        init {

            mSection = HoverMenu.Section(
                    SectionId("1"),
                    createTabView(),
                    createScreen()
            )
        }

        private fun createTabView(): View {
            val imageView = ImageView(mContext)
            imageView.setImageResource(R.drawable.tab_background)
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE)
            return imageView
        }

        private fun createScreen(): Content {
            return HoverMenuScreen(mContext, "Screen 1")
        }

        override fun getId(): String {
            return "singlesectionmenu"
        }

        override fun getSectionCount(): Int {
            return 1
        }


        override fun getSection(index: Int): HoverMenu.Section? {
            if (0 == index) {
                return mSection
            } else {
                return null
            }
        }


        override fun getSection(sectionId: SectionId): HoverMenu.Section? {
            if (sectionId == mSection.id) {
                return mSection
            } else {
                return null
            }
        }

        override fun getSections(): List<HoverMenu.Section> {
            return Collections.singletonList(mSection)
        }
    }

}