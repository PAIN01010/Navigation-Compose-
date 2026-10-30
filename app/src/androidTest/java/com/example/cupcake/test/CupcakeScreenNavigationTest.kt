package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cupcake.CupcakeApp
import com.example.cupcake.CupcakeScreen
import com.example.cupcake.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CupcakeScreenNavigationTest {

    @get:Rule
    val composeTestRule =
        createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupCupcakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(
                LocalContext.current
            ).apply {
                navigatorProvider.addNavigator(
                    ComposeNavigator()
                )
            }

            CupcakeApp(
                navController = navController
            )
        }
    }

    private fun assertCurrentRouteName(
        expectedRouteName: String
    ) {
        composeTestRule.waitForIdle()

        assertEquals(
            expectedRouteName,
            navController
                .currentBackStackEntry
                ?.destination
                ?.route
        )
    }

    private fun clickText(resourceId: Int) {
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(resourceId)
        ).performClick()
    }

    private fun navigateToFlavorScreen() {
        clickText(R.string.one_cupcake)
    }

    private fun navigateToPickupScreen() {
        navigateToFlavorScreen()

        clickText(R.string.chocolate)
        clickText(R.string.next)
    }

    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()

        calendar.add(
            Calendar.DATE,
            1
        )

        val formatter = SimpleDateFormat(
            "E MMM d",
            Locale.getDefault()
        )

        return formatter.format(calendar.time)
    }

    private fun navigateToSummaryScreen() {
        navigateToPickupScreen()

        composeTestRule.onNodeWithText(
            getFormattedDate()
        ).performClick()

        clickText(R.string.next)
    }

    private fun clickBackButton() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.back_button
                )
            )
            .performClick()
    }

    @Test
    fun cupcakeNavHost_verifyStartDestination() {
        assertCurrentRouteName(
            CupcakeScreen.Start.name
        )
    }

    @Test
    fun cupcakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen() {
        composeTestRule
            .onNodeWithContentDescription(
                composeTestRule.activity.getString(
                    R.string.back_button
                )
            )
            .assertDoesNotExist()
    }

    @Test
    fun cupcakeNavHost_clickOneCupcake_navigatesToFlavorScreen() {
        navigateToFlavorScreen()

        assertCurrentRouteName(
            CupcakeScreen.Flavor.name
        )
    }

    @Test
    fun cupcakeNavHost_clickNextOnFlavorScreen_navigatesToPickupScreen() {
        navigateToPickupScreen()

        assertCurrentRouteName(
            CupcakeScreen.Pickup.name
        )
    }

    @Test
    fun cupcakeNavHost_clickBackOnFlavorScreen_navigatesToStartScreen() {
        navigateToFlavorScreen()

        clickBackButton()

        assertCurrentRouteName(
            CupcakeScreen.Start.name
        )
    }

    @Test
    fun cupcakeNavHost_clickCancelOnFlavorScreen_navigatesToStartScreen() {
        navigateToFlavorScreen()

        clickText(R.string.cancel)

        assertCurrentRouteName(
            CupcakeScreen.Start.name
        )
    }

    @Test
    fun cupcakeNavHost_clickNextOnPickupScreen_navigatesToSummaryScreen() {
        navigateToSummaryScreen()

        assertCurrentRouteName(
            CupcakeScreen.Summary.name
        )
    }

    @Test
    fun cupcakeNavHost_clickBackOnPickupScreen_navigatesToFlavorScreen() {
        navigateToPickupScreen()

        clickBackButton()

        assertCurrentRouteName(
            CupcakeScreen.Flavor.name
        )
    }

    @Test
    fun cupcakeNavHost_clickCancelOnPickupScreen_navigatesToStartScreen() {
        navigateToPickupScreen()

        clickText(R.string.cancel)

        assertCurrentRouteName(
            CupcakeScreen.Start.name
        )
    }

    @Test
    fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartScreen() {
        navigateToSummaryScreen()

        clickText(R.string.cancel)

        assertCurrentRouteName(
            CupcakeScreen.Start.name
        )
    }
}