/**
 * Copyright © 2016 Christian Wulf (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.chw.util;

public final class MyStringUtil {

	private MyStringUtil() {
		// utility class
	}

	public static String repeat(String string, int numRepeats) {
		StringBuilder builder = new StringBuilder(string.length() * numRepeats);
		for (int i = 0; i < numRepeats; i++) {
			builder.append(string);
		}
		return builder.toString();
	}

}
