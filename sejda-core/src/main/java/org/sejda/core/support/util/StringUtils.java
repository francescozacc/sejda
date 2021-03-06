/*
 * This file is part of the Sejda source code
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sejda.core.support.util;

public final class StringUtils {
    private StringUtils() {
        // hide
    }


    // Useful to debug weird strings
    public static String asUnicodes(String in) {
        if(in == null) return null;

        StringBuilder result = new StringBuilder();
        for (int offset = 0; offset < in.length(); ) {
            int codepoint = in.codePointAt(offset);
            result.append("\\U+").append(Integer.toHexString(codepoint).toUpperCase());
            offset += Character.charCount(codepoint);
        }
        return result.toString();
    }


    public static String normalizeLineEndings(String in) {
        return in.replaceAll("\\r\\n", "\n");
    }
}
