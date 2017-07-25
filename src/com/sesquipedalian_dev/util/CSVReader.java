/**
 * Copyright 2017 sesquipedalian.dev@gmail.com

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sesquipedalian_dev.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Adapted from from https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/ '2.2'
 */
public class CSVReader {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    private String filename;
    private char separator;
    private char quote;
    private boolean skipHeader;

    public CSVReader(String filename) {
        this(filename, DEFAULT_SEPARATOR, DEFAULT_QUOTE, true);
    }

    public CSVReader(String filename, char quote) {
        this(filename, DEFAULT_SEPARATOR, quote, true);
    }

    public CSVReader(String filename, char separator, char quote, boolean skipHeader) {
        this.filename = filename;
        this.separator = separator;
        this.quote = quote;
        this.skipHeader = skipHeader;

    }

    public List<List<String>> parseFile() throws IOException {
        List<List<String>> retVal = new ArrayList<>();

        boolean isHeader = skipHeader;
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
            if(!isHeader) {
                retVal.add(line);
            }
            isHeader = false;
        }
        scanner.close();

        return retVal;
    }


    public List<String> parseLine(String cvsLine) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == quote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == quote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
//                    if (chars[0] != '"' && quote == '\"') {
//                        curVal.append('"');
//                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separator) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
}
