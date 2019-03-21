package com.loyalty.usermanagement.testhelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@Service
public class DatabaseCleaner {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private List<String> excludedTables = Arrays.asList("lk_user_type");

	private Logger log = LoggerFactory.getLogger(getClass());

	public void clean() throws SQLException {

		List<String> toCleanTables = new ArrayList<>();
		try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {

			try (ResultSet rs = conn.getMetaData().getTables(conn.getCatalog(), conn.getSchema(), null,
					new String[] { "TABLE" })) {

				while (rs.next()) {
					String tableName = rs.getString("TABLE_NAME");
					if (excludedTables.contains(tableName.toLowerCase())) {
						continue;
					}
					toCleanTables.add(tableName);
				}
			}

		}

		for (String oneTableName : toCleanTables) {

			// truncate tables
			jdbcTemplate.update(String.format("truncate %s.%s cascade", "user_management", oneTableName));
			log.debug("truncated table name:{}", oneTableName);
			// enable foreign keys
			// jdbcTemplate.update(String.format("ALTER TABLE %s ENABLE TRIGGER
			// ALL", oneTableName));
		}

	}

}
