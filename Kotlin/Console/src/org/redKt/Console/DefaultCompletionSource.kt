/**
 * Copyright (C) 2012 Ben Navetta <ben.navetta></ben.navetta>@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.redkt.Console


import java.util.ArrayList
import java.util.Arrays

class DefaultCompletionSource(private val terms: List<String>) : CachingCompletionSource() {

    constructor(vararg terms: String) : this(Arrays.asList<String>(*terms)) {}

    protected fun doCompletion(input: String): List<String> {
        val matches = ArrayList<String>()
        for (term in terms) {
            if (term.toLowerCase().startsWith(input.toLowerCase())) {
                matches.add(term)
            }
        }
        return matches
    }

}
