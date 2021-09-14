package com.electroshock.technicaltestandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.electroshock.technicaltestandroid.view_model.DataViewModel
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class DataViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @InjectMocks
    private lateinit var classUnderTest: DataViewModel

    @Test
    fun `check the set data list is available`() {
        val card = classUnderTest.data.testObserver()

        Truth.assert_()
            .that(card.observedValues.first())
    }
}