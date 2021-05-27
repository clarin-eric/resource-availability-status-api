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

package eu.clarin.cmdi.rasa.linkResources.impl;

import eu.clarin.cmdi.rasa.DAO.Statistics.Statistics;
import eu.clarin.cmdi.rasa.DAO.Statistics.StatusStatistics;
import eu.clarin.cmdi.rasa.filters.StatisticsCountFilter;
import eu.clarin.cmdi.rasa.helpers.ConnectionProvider;
import eu.clarin.cmdi.rasa.linkResources.StatisticsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ACDHStatisticsResource implements StatisticsResource {

    private final static Logger _logger = LoggerFactory.getLogger(ACDHStatisticsResource.class);

    private final ConnectionProvider connectionProvider;

    public ACDHStatisticsResource(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    //avgDuration, maxDuration, countStatus should be named so, because in Statistics constructor, they are called as such.
    @Override
    public List<StatusStatistics> getStatusStatistics(String providerGroup) throws SQLException {
    	List<StatusStatistics> list = new ArrayList<StatusStatistics>();
    	
        if (providerGroup == null || providerGroup.equals("Overall")) {
            return getStatusStatistics();
        }
        String query = 
        		"SELECT s.statusCode, AVG(s.duration) AS avgDuration, MAX(s.duration) AS maxDuration, COUNT(s.duration) AS count" + 
    	        		" FROM status s, link l, link_context lk, context c, providerGroup p" + 
    	        		" WHERE p.name_hash=MD5(?)" + 
    	        		" AND c.providerGroup_id = p.id" + 
    	        		" AND lc.context_id = c.id" + 
    	        		" AND l.id = lc.link_id" + 
    	        		" AND s.link_id = l.id" + 
    	        		" GROUP BY s.statusCode";
        try (Connection con = connectionProvider.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
            	statement.setString(1, providerGroup);
                try (ResultSet rs = statement.executeQuery()) {
                	while(rs.next()) {
                		list.add(new StatusStatistics(
	                			rs.getInt("statusCode"),
	                			rs.getLong("count"),
	                			rs.getDouble("avgDuration"),
	                			rs.getLong("maxDuration")
	            				)
            				);
                	}
                }
            }
        }
        return list;
    }

    //avgDuration, maxDuration, countStatus should be named so, because in Statistics constructor, they are called as such.
    @Override
    public List<StatusStatistics> getStatusStatistics() throws SQLException {
    	List<StatusStatistics> list = new ArrayList<StatusStatistics>();
    	
        String query = "SELECT statusCode, AVG(duration) AS avgDuration, MAX(duration) AS maxDuration, COUNT(duration) AS count FROM status GROUP BY statusCode";
        try (Connection con = connectionProvider.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                	while(rs.next()) {
                		list.add(new StatusStatistics(
	                			rs.getInt("statusCode"),
	                			rs.getLong("count"),
	                			rs.getDouble("avgDuration"),
	                			rs.getLong("maxDuration")
	            				)
            				);
                	}
                }
            }
        }
        return list;
    }

    @Override
    public Statistics getOverallStatistics(String providerGroup) throws SQLException {
        if (providerGroup == null || providerGroup.equals("Overall")) {
            return getOverallStatistics();
        }
        String query = 
        		"SELECT AVG(s.duration) AS avgDuration, MAX(s.duration) AS maxDuration, COUNT(s.duration) AS count" + 
    	        		" FROM status s, link l, link_context lk, context c, providerGroup p" + 
    	        		" WHERE p.name_hash=MD5(?)" + 
    	        		" AND c.providerGroup_id = p.id" + 
    	        		" AND lc.context_id = c.id" + 
    	        		" AND l.id = lc.link_id" + 
    	        		" AND s.link_id = l.id";
        		
        try (Connection con = connectionProvider.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
            	statement.setString(1, providerGroup);
                try (ResultSet rs = statement.executeQuery()) {
                	if(rs.next() && rs.getLong("count") > 0L) {
                		return new StatusStatistics(
		                			rs.getInt("statusCode"),
		                			rs.getLong("count"),
		                			rs.getDouble("avgDuration"),
		                			rs.getLong("maxDuration")
	            				);

                	}
                	else
                		return null;
                }
            }
        }
    }

    @Override
    public Statistics getOverallStatistics() throws SQLException {
        String query = "SELECT AVG(duration) AS avgDuration, MAX(duration) AS maxDuration, COUNT(duration) AS count FROM status";
        try (Connection con = connectionProvider.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                	if(rs.next() && rs.getLong("count") > 0L) {
                		return new StatusStatistics(
		                			rs.getInt("statusCode"),
		                			rs.getLong("count"),
		                			rs.getDouble("avgDuration"),
		                			rs.getLong("maxDuration")
	            				);

                	}
                	else
                		return null;
                }
            }
        }
    }

	@Override
	public long countTable(StatisticsCountFilter filter) throws SQLException {
		try (Connection con = connectionProvider.getConnection()) {
            try (PreparedStatement statement = filter.getStatement(con)) {
            	try (ResultSet rs = statement.executeQuery()){
            		if(rs.next())
            			return rs.getLong("count");
            	}
            }
		}
		return -1L;
	}
}
