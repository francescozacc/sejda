/*
 * Copyright 2016 by Eduard Weissmann (edi.weissmann@gmail.com).
 *
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
package org.sejda.impl.sambox.component.excel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTable {

    private static final Logger LOG = LoggerFactory.getLogger(DataTable.class);

    private final List<List<String>> data = new ArrayList<>();
    private final TreeSet<Integer> pageNumbers = new TreeSet<>();

    public DataTable(int pageNumber) {
        this.pageNumbers.add(pageNumber);
    }

    public DataTable(Collection<Integer> pageNumbers) {
        this.pageNumbers.addAll(pageNumbers);
    }

    public void addRow(String... dataRow) {
        addRow(Arrays.asList(dataRow));
    }

    public void addRow(List<String> dataRow) {
        data.add(dataRow);
    }

    public void addRows(List<List<String>> dataRows) {
        dataRows.forEach(this::addRow);
    }

    public List<String> headerRow() {
        return data.get(0);
    }

    public boolean hasSameHeaderAs(DataTable other) {
        String thisHeader = String.join("", this.headerRow()).trim();
        String otherHeader = String.join("", other.headerRow()).trim();
        LOG.debug("Comparing header columns: '{}' and '{}'", thisHeader, otherHeader);

        return thisHeader.equalsIgnoreCase(otherHeader);
    }

    public boolean hasSameColumnsAs(DataTable other) {
        LOG.debug("Comparing header columns size: {} and {}", this.headerRow().size(), other.headerRow().size());
        return other.headerRow().size() == this.headerRow().size();
    }

    public List<List<String>> getData() {
        return data;
    }

    public TreeSet<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public DataTable mergeWith(DataTable other) {
        TreeSet<Integer> resultPageNumbers = new TreeSet<>();
        resultPageNumbers.addAll(this.pageNumbers);
        resultPageNumbers.addAll(other.pageNumbers);

        DataTable result = new DataTable(resultPageNumbers);
        result.addRows(this.data);

        List<List<String>> otherData = other.data;
        if (this.hasSameHeaderAs(other)) {
            otherData.remove(0);
        }

        result.addRows(otherData);

        return result;
    }

    public String getPagesAsString() {
        StringBuilder sb = new StringBuilder();
        if (this.pageNumbers.size() > 1) {
            sb.append("Pages ");
            int i = 0;
            for (Integer pageNumber : pageNumbers) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(pageNumber);
                i++;
            }
        } else {
            sb.append("Page ").append(pageNumbers.iterator().next());
        }

        return sb.toString();
    }

    public boolean hasData() {
        return this.data.size() > 0;
    }

    public int getColumnsCount() {
        int result = 0;
        for (List<String> row : data) {
            result = Math.max(row.size(), result);
        }

        return result;
    }

    public int getRowsCount() {
        return data.size();
    }

    public List<String> getColumn(int c) {
        List<String> result = new ArrayList<>(getRowsCount());
        for (List<String> row : data) {
            result.add(getOrEmpty(row, c));
        }
        return result;
    }

    public DataTable mergeColumns(int c1, int c2) {
        DataTable result = new DataTable(getPageNumbers());
        for (List<String> row : data) {
            List<String> newRow = new ArrayList<>(row.size() - 1);
            for (int c = 0; c < row.size(); c++) {
                if (c == c2) {
                    // noop, continue
                } else if (c == c1) {
                    String newValue = new StringBuilder().append(getOrEmpty(row, c1)).append(" ")
                            .append(getOrEmpty(row, c2)).toString().trim();
                    newRow.add(newValue);
                } else {
                    newRow.add(getOrEmpty(row, c));

                }
            }
            result.addRow(newRow);
        }
        return result;
    }

    private static String getOrEmpty(List<String> list, int index) {
        if (list.size() <= index) {
            return "";
        }
        return list.get(index);
    }
}
