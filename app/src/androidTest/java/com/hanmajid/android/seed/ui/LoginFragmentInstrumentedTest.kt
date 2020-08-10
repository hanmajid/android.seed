package com.hanmajid.android.seed.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hanmajid.android.seed.DataBindingIdlingResource
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.ui.auth.login.LoginFragment
import com.hanmajid.android.seed.ui.auth.login.LoginFragmentDirections
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

//@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentInstrumentedTest {

//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)

    private val navController = mock(NavController::class.java)

    private var idlingResource: DataBindingIdlingResource? = null

//    @Inject
//    lateinit var

    @Before
    fun init() {
//        hiltRule.inject()
//        idlingResource = DataBindingIdlingResource()
//        IdlingRegistry.getInstance().register(idlingResource)
        val scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            LoginFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    @After
    fun cleanup() {
//        if (idlingResource != null) {
//            IdlingRegistry.getInstance().unregister(idlingResource)
//        }
    }


    @Test
    fun testClickRegisterButton() {
//        onView(ViewMatchers.withId(R.id.title_login)).check(matches(isDisplayed()))
//        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        navController.setGraph(R.navigation.auth_nav_graph)

        onView(withId(R.id.btn_register)).perform(ViewActions.click())
        verify(navController).navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        )
    }

    @Test
    fun testClickLoginButtonBlank() {
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withText("Please fill in the username")).check(matches(isDisplayed())) // TODO: Refactor string resources
        onView(withText("Please fill in the password")).check(matches(isDisplayed()))
    }

    @Test
    fun testClickLoginButtonPasswordBlank() {
        onView(withId(R.id.username)).perform(typeText("hanmajid"))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withText("Please fill in the username")).check(doesNotExist())
        onView(withText("Please fill in the password")).check(matches(isDisplayed()))
    }

    @Test
    fun testClickLoginButtonUsernameBlank() {
        onView(withId(R.id.password)).perform(typeText("password"))
        onView(withId(R.id.btn_login)).perform(ViewActions.click())
        onView(withText("Please fill in the username")).check(matches(isDisplayed()))
        onView(withText("Please fill in the password")).check(doesNotExist())
    }

    @Test
    fun testClickLoginButton() {
        onView(withId(R.id.username)).perform(typeText("hanmajid"))
        onView(withId(R.id.password)).perform(typeText("password"))
        val loginButton = onView(withId(R.id.btn_login))
        loginButton.perform(ViewActions.click())
        onView(withText("Please fill in the username")).check(doesNotExist())
        onView(withText("Please fill in the password")).check(doesNotExist())
        loginButton.check(matches(not(isEnabled())))
        verify(navController).navigate(
            LoginFragmentDirections.actionLoginFragmentToNavGraph()
        )
    }
}