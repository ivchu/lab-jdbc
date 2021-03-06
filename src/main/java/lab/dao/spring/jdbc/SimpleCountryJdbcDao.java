package lab.dao.spring.jdbc;

import lab.dao.CountryDao;
import lab.model.Country;
import lab.model.simple.SimpleCountry;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import java.util.List;

public class SimpleCountryJdbcDao extends NamedParameterJdbcDaoSupport implements CountryDao {

    private static final String LOAD_COUNTRIES_SQL = "INSERT INTO country (name, code_name) VALUES ('%s', '%s');";
    private static final String GET_ALL_COUNTRIES_SQL = "SELECT id, name, code_name FROM country";
    private static final String GET_COUNTRIES_BY_NAME_SQL = "SELECT id, name, code_name FROM country WHERE name LIKE :name";
    private static final String GET_COUNTRY_BY_NAME_SQL = "SELECT id, name, code_name FROM country WHERE name = '%s'";
    private static final String GET_COUNTRY_BY_CODE_NAME_SQL = "SELECT id, name, code_name FROM country WHERE code_name = '%s'";
    private static final String UPDATE_COUNTRY_NAME_SQL = "UPDATE country SET name='%s' WHERE code_name='%s'";

    private static final RowMapper<Country> COUNTRY_ROW_MAPPER = (resultSet, rowNum) ->
            new SimpleCountry(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("code_name"));

    @Override
    public List<Country> getCountryList() {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if (jdbcTemplate != null)
            return jdbcTemplate
                    .query(GET_ALL_COUNTRIES_SQL, COUNTRY_ROW_MAPPER);
        else
            throw new RuntimeException("DB has not initialized!");
    }

    @Override
    public List<Country> getCountryListStartWith(String name) {
        val namedParameterJdbcTemplate = getNamedParameterJdbcTemplate();
        if (namedParameterJdbcTemplate == null)
            throw new RuntimeException("DB has not initialized!");

        val sqlParameterSource = new MapSqlParameterSource(
                "name", name + "%");
        return namedParameterJdbcTemplate.query(GET_COUNTRIES_BY_NAME_SQL,
                sqlParameterSource, COUNTRY_ROW_MAPPER);
    }

    @Override
    public void updateCountryName(String codeName, String newCountryName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if (jdbcTemplate != null)
            jdbcTemplate.update(
                    String.format(UPDATE_COUNTRY_NAME_SQL, newCountryName, codeName));
        else
            throw new RuntimeException("DB has not initialized!");
    }

    @Override
    public void loadCountries() {
        for (String[] countryData : COUNTRY_INIT_DATA) {
            String sql = String.format(LOAD_COUNTRIES_SQL, countryData[0], countryData[1]);
//			System.out.println(sql);
            getJdbcTemplate().execute(sql);
        }
    }

    @Override
    public Country getCountryByCodeName(String codeName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();

        String sql = String.format(GET_COUNTRY_BY_CODE_NAME_SQL, codeName);
//		System.out.println(sql);

        return jdbcTemplate.query(sql, COUNTRY_ROW_MAPPER).get(0);
    }

    @Override
    public Country getCountryByName(String name) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        List<Country> countryList = jdbcTemplate.query(
                String.format(GET_COUNTRY_BY_NAME_SQL, name), COUNTRY_ROW_MAPPER);
        if (countryList.isEmpty()) {
            return null;
        }
        return countryList.get(0);
    }
}
