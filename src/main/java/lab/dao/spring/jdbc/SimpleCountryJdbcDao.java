package lab.dao.spring.jdbc;

import java.util.List;

import lab.dao.CountryRowMapper;
import lab.model.Country;

import lab.model.simple.SimpleCountry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class SimpleCountryJdbcDao extends JdbcDaoSupport implements lab.dao.CountryDao {
	private static final String LOAD_COUNTRIES_SQL = "insert into country (name, code_name) values ";

	private static final String GET_ALL_COUNTRIES_SQL = "select * from country";
	private static final String GET_COUNTRIES_BY_NAME_SQL = "select * from country where name like :name";
	private static final String GET_COUNTRY_BY_NAME_SQL = "select * from country where name = '";
	private static final String GET_COUNTRY_BY_CODE_NAME_SQL = "select * from country where code_name = '";

	private static final String UPDATE_COUNTRY_NAME_SQL_1 = "update country SET name='";
	private static final String UPDATE_COUNTRY_NAME_SQL_2 = " where code_name='";

    private static final RowMapper<Country> COUNTRY_ROW_MAPPER = (resultSet, rowNum) ->
            new SimpleCountry(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("code_name"));

	@Override
    public List<Country> getCountryList() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if (jdbcTemplate != null) {
            return jdbcTemplate.query(GET_ALL_COUNTRIES_SQL, COUNTRY_ROW_MAPPER);
        } else {
            throw new RuntimeException("no jdbcTemplate in getCountryList");
        }
	}

	@Override
    public List<Country> getCountryListStartWith(String name) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				getDataSource());
		SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
				"name", name + "%");
		return namedParameterJdbcTemplate.query(GET_COUNTRIES_BY_NAME_SQL,
				sqlParameterSource, COUNTRY_ROW_MAPPER);
	}

	@Override
    public void updateCountryName(String codeName, String newCountryName) {
		// TODO: implement it
	}

	@Override
    public void loadCountries() {
		for (String[] countryData : COUNTRY_INIT_DATA) {
			String sql = LOAD_COUNTRIES_SQL + "('" + countryData[0] + "', '"
					+ countryData[1] + "');";
			getJdbcTemplate().execute(sql);
		}
	}

	@Override
    public Country getCountryByCodeName(String codeName) {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		String sql = GET_COUNTRY_BY_CODE_NAME_SQL + codeName + "'";
//		System.out.println(sql);

		return jdbcTemplate.query(sql, COUNTRY_ROW_MAPPER).get(0);
	}

	@Override
    public Country getCountryByName(String name) {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		List<Country> countryList = jdbcTemplate.query(GET_COUNTRY_BY_NAME_SQL
				+ name + "'", COUNTRY_ROW_MAPPER);
		if (countryList.isEmpty()) {
			return null;
		}
		return countryList.get(0);
	}
}
