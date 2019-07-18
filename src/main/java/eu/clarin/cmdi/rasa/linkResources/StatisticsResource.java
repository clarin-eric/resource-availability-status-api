/*
 * Copyright (C) 2019 CLARIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package eu.clarin.cmdi.rasa.linkResources;

import eu.clarin.cmdi.rasa.filters.impl.ACDHStatisticsFilter;
import eu.clarin.cmdi.rasa.links.Statistics;

import java.util.List;
import java.util.Optional;

public interface StatisticsResource {

    /* gets status statistics per collection. status statistics are
    the following info per status code:
    count,avg response time, max response time
     */
    List<Statistics> getStatusStatistics(String collection);
    Statistics getOverallStatistics(String collection);

    long countLinksChecked(Optional<ACDHStatisticsFilter> filter);
    long countLinksToBeChecked(Optional<ACDHStatisticsFilter> filter);
    int getDuplicateCount(String collection);
}
