package com.electroshock.technicaltestandroid

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith

import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ComposeUIInstrumentationTest {
    @get: Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // I am checking if the data is displayed correctly according the design.
    @Test
    fun header_title_test(){
        composeTestRule.onNodeWithText("Hello, Welcome to App!").assertIsDisplayed()
    }

    @Test
    fun subheader_title_test(){
        composeTestRule.onNodeWithText("Check out our App every week for exciting offers.").assertIsDisplayed()
    }

    @Test
    fun subheader_description_test(){
        composeTestRule.onNodeWithText("Offers available every week!").assertIsDisplayed()
    }
}