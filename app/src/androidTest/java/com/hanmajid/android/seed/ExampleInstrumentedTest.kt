package com.hanmajid.android.seed

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testNavigationToChatScreen() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setGraph(R.navigation.navui_nav_graph)

//        val homeFragment = launchFragmentInContainer<HomeFragment>()
//
//        homeFragment.onFragment {
//            Navigation.setViewNavController(it.requireView(), navController)
//        }

//        onView(ViewMatchers.withId(R.id.btn_chat_1)).perform(ViewActions.click())
//        onView(withText("69")).check(matches(isDisplayed()))
    }
}