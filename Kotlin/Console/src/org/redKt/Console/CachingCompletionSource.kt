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


import java.util.HashMap

abstract class CachingCompletionSource : CompletionSource {
    private val completionCache = HashMap<String, List<String>>()

        override fun complete(text: String): List<String>? {
        if (completionCache.containsKey(text)) {
            var results = completionCache.get(text)
            return results
        } else {
            val results = doCompletion(text)
            completionCache.put(text, results)
            return results
        }
    }

    protected abstract fun doCompletion(input: String): List<String>
}
