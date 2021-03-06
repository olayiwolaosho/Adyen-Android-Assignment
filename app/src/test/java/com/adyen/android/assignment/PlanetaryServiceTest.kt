package com.adyen.android.assignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.repo.PlanetaryRepo
import com.adyen.android.assignment.data.repo.PlanetaryRepoImpl
import com.adyen.android.assignment.ui.apods.ApodsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlanetaryServiceTest {

    @Mock
    private lateinit var planetaryRepo: PlanetaryRepo

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var astronomyPictureDao: AstronomyPictureDao

    private lateinit var planetaryService: PlanetaryService

    private lateinit var apodsViewModel: ApodsViewModel

    @Before
    fun setUp() {
        planetaryService = TestServiceGenerator.getService(
            PlanetaryService::class.java
        )

        planetaryRepo = PlanetaryRepoImpl(planetaryService,astronomyPictureDao)

        apodsViewModel = ApodsViewModel(planetaryRepo)
    }

    /**
     * Integration test -
     * ensures the [generated key](https://api.nasa.gov/) returns results from the api
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testResponseCode() = runTest {

        val response = apodsViewModel.getApodResponse()

        assert(response.isSuccessful)

    }

}
