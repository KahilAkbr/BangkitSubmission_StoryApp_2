package com.example.storygram.view.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.storygram.R
import com.example.storygram.utils.EspressoIdlingResource
import com.example.storygram.view.boarding.BoardingActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginThenLogout() {
        Intents.init()
        onView(withId(R.id.ed_login_email))
            .perform(typeText("khltest6@gmail.com"))
        onView(withId(R.id.ed_login_password))
            .perform(typeText("12345678"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.btnLogin))
            .perform(click())
        onView(withText(R.string.ok))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withId(R.id.setting))
            .perform(click())
        onView(withId(R.id.btnLogout))
            .perform(click())
        onView(withText(R.string.ok))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        intended(hasComponent(BoardingActivity::class.java.name))
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }
}