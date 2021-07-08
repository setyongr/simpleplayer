package com.setyongr.simpleplayer.presentation.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.setyongr.simpleplayer.R
import com.setyongr.simpleplayer.data.model.ItunesResponse
import com.setyongr.simpleplayer.data.model.ItunesResultResponse
import com.setyongr.simpleplayer.data.repository.ItunesRepository
import com.setyongr.simpleplayer.di.DataModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@UninstallModules(
    value = [DataModule::class]
)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val itunesRepository: ItunesRepository = mockk(relaxed = true, relaxUnitFun = true) {
        coEvery { search(any(), any()) } returns ItunesResponse(0, emptyList())
    }

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun itemDisplayed() {
        val mockTerm = "mock term"
        val mockResult = ItunesResultResponse(
            trackId = 1,
            artistName = "mock name",
            trackName = "mock track",
            collectionName = "mock collection",
            artworkUrl100 = null,
            previewUrl = "https://aa.com"
        )
        val mockResponse = ItunesResponse(
            resultCount = 1,
            results = listOf(
                mockResult
            )
        )

        // do search
        coEvery { itunesRepository.search(any(), any()) } answers {
            if (firstArg<String>() == mockTerm) mockResponse
            else ItunesResponse(0, emptyList())
        }

        onView(withId(R.id.editSearch)).perform(click(), replaceText(mockTerm))

        // check result list
        waitForRecyclerViewChanged()
        onView(
            allOf(
                withId(R.id.tvSongName),
                withText(mockResult.trackName)
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tvArtist),
                withText(mockResult.artistName)
            )
        ).check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tvAlbum),
                withText(mockResult.collectionName)
            )
        ).check(matches(isDisplayed()))
    }

    private fun waitForRecyclerViewChanged() {
        val latch = CountDownLatch(1)
        activityRule.scenario.onActivity {
            val rv = it.findViewById<RecyclerView>(R.id.recyclerView)
            rv.itemAnimator = null
            rv.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    super.onChanged()
                    latch.countDown()
                }
                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    super.onItemRangeChanged(positionStart, itemCount)
                    latch.countDown()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    latch.countDown()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    super.onItemRangeRemoved(positionStart, itemCount)
                    latch.countDown()
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                    latch.countDown()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    super.onItemRangeChanged(positionStart, itemCount, payload)
                    latch.countDown()
                }
            })
        }
        latch.await()
    }
}