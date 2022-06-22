package com.adyen.android.assignment.ui.apods

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adyen.android.assignment.CoroutineTestRule
import com.adyen.android.assignment.LiveDataTestUtil
import com.adyen.android.assignment.TestServiceGenerator
import com.adyen.android.assignment.api.PlanetaryService
import com.adyen.android.assignment.data.*
import com.adyen.android.assignment.data.db.AstronomyPictureDao
import com.adyen.android.assignment.data.db.AstronomyPictureEnt
import com.adyen.android.assignment.data.db.FavouriteAstronomyPictureEnt
import com.adyen.android.assignment.data.repo.PlanetaryRepoImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class ApodsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    private lateinit var astronomyPictureDao: AstronomyPictureDao

    private lateinit var apodsViewModel : ApodsViewModel

    @Before
    fun setUp() {

        val planetaryService = TestServiceGenerator.getService(
            PlanetaryService::class.java
        )

        val planetaryRepo = PlanetaryRepoImpl(planetaryService,astronomyPictureDao)

        apodsViewModel = ApodsViewModel(planetaryRepo)

    }

    @Test
    fun `favourite apods status value  is successful`() {

        val astronomyPicture = FavouriteAstronomyPictureEnt(1,"v1","Space Galaxy","This is your Galaxy",
            LocalDate.now(),"image","null","http://image",true)

        val astronomyPictureList =  mutableListOf<FavouriteAstronomyPictureEnt>()

        astronomyPictureList.add(astronomyPicture)

        apodsViewModel.favouriteApods().value = Resource.success(astronomyPictureList)

        apodsViewModel.getFavouriteApods()

        val result = LiveDataTestUtil.getOrAwaitValue(apodsViewModel.favouriteApods())

        println(result?.status)

        assert(result?.status == Resource.Status.SUCCESS)
    }

    @Test
    fun `filter apods is true if sort tag is DATE_SORT_TAG`() {

        apodsViewModel.currentSortTag = DATE_SORT_TAG

        val shouldFilter = apodsViewModel.filterApods()

        println(shouldFilter)

        assert(shouldFilter)
    }

    @Test
    fun `filter apods is true if sort tag is TITLE_SORT_TAG`() {

        apodsViewModel.currentSortTag = TITLE_SORT_TAG

        val shouldFilter = apodsViewModel.filterApods()

        println(shouldFilter)

        assert(shouldFilter)
    }

    @Test
    fun `filter apods is false if sort tag is NO_SORT_TAG`() {

        apodsViewModel.currentSortTag = NO_SORT_TAG

        val shouldFilter = apodsViewModel.filterApods()

        println(shouldFilter)

        assert(!shouldFilter)
    }

    @Test
    fun `filter apods status value  is successful if sort tag is TITLE_SORT_TAG or DATE_SORT_TAG`() {

        apodsViewModel.currentSortTag = TITLE_SORT_TAG

        apodsViewModel.filterApods()

        val result = LiveDataTestUtil.getOrAwaitValue(apodsViewModel.filteredApods())

        println(result?.status)

        assert(result?.status == Resource.Status.SUCCESS)
    }

    @Test
    fun `getAllApods returns false when data in the allApods is null`() {

        apodsViewModel.allApods().value = Resource.success(null)

        val result = apodsViewModel.getAllApods()

        println(result)

        assert(!result)
    }

    @Test
    fun `getAllApods returns true when data in the allApods is empty`() {

        apodsViewModel.allApods().value = Resource.success(listOf())

        val result = apodsViewModel.getAllApods()

        println(result)

        assert(result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getApodsFromService returns data and allApods value is not null or empty`() = runTest {

        apodsViewModel.getApodsFromService()

        val result = apodsViewModel.allApods().value

        println(result)

        assert(!result?.data.isNullOrEmpty())

    }

    @Test
    fun `Filter when sortTag is TITLE_SORT_TAG or DATE_SORT_TAG`() {

        apodsViewModel.addFilter(DATE_SORT_TAG)

        val resultDateSort = apodsViewModel.filteredApods().value

        assert(resultDateSort?.status == Resource.Status.SUCCESS)

        assert(apodsViewModel.currentSortTag == DATE_SORT_TAG)


        apodsViewModel.addFilter(TITLE_SORT_TAG)

        val result = apodsViewModel.filteredApods().value

        assert(result?.status == Resource.Status.SUCCESS)

        assert(apodsViewModel.currentSortTag == TITLE_SORT_TAG)
    }

    @Test
    fun `No Filter when sortTag is Not TITLE_SORT_TAG or DATE_SORT_TAG`() {

        apodsViewModel.addFilter(NO_SORT_TAG)

        val resultDateSort = apodsViewModel.filteredApods().value

        assert(resultDateSort?.status != Resource.Status.SUCCESS)

        assert(apodsViewModel.currentSortTag != DATE_SORT_TAG)
        assert(apodsViewModel.currentSortTag != TITLE_SORT_TAG)
        assert(apodsViewModel.currentSortTag == NO_SORT_TAG)


        apodsViewModel.addFilter(3)

        val result = apodsViewModel.filteredApods().value

        assert(result?.status != Resource.Status.SUCCESS)

        assert(apodsViewModel.currentSortTag != DATE_SORT_TAG)
        assert(apodsViewModel.currentSortTag != TITLE_SORT_TAG)
        assert(apodsViewModel.currentSortTag == NO_SORT_TAG)
    }

}