/*
 * Copyright (c) 2021 marlonlom.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.marlonlom.apps.bookbar.saved

import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.Repository
import dev.marlonlom.apps.bookbar.saved.SavedBooksContract.ViewModelFactory
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelFactoryTest : TestCase() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var repository: Repository

    @Test
    fun `Should return a viewmodel instance of required class`() {
        repository = Mockito.mock(Repository::class.java)
        viewModelFactory = ViewModelFactory(repository)

        val createdViewModel =
            viewModelFactory.create(SavedBooksContract.ViewModel::class.java)
        assertNotNull(createdViewModel)
    }

}