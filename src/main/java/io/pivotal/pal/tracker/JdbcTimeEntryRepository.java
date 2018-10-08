package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<TimeEntry> rowMapper;

    public JdbcTimeEntryRepository(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
        this.rowMapper = (rs, rowNum) -> new TimeEntry(rs.getLong("id"), rs.getLong("project_id"), rs.getLong("user_id"), rs.getDate("date").toLocalDate(), rs.getInt("hours"));
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        jdbcTemplate.update("insert into time_entries(project_id, user_id, date, hours) values (?,?,?,?) ", timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        long id = jdbcTemplate.queryForObject("select max(id) from time_entries", Long.class);
        return find(id);
    }

    @Override
    public TimeEntry find(long id) {
        List<TimeEntry> entries =  jdbcTemplate.query("select * from time_entries where id = ?", this.rowMapper, id);
        if (entries.isEmpty()) {
            return null;
        }
        return entries.get(0);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select * from time_entries", this.rowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry any) {
        if (find(id) != null) {
            jdbcTemplate.update("update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?", any.getProjectId(), any.getUserId(), any.getDate(), any.getHours(), id);
            return find(id);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from time_entries where id = ?", id);
    }
}
