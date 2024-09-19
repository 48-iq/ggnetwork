package ru.ivanov.ggnetwork.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.ivanov.ggnetwork.dto.post.PostDto;
import ru.ivanov.ggnetwork.repositories.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<PostDto> getNotViewedPosts(Integer userId, Integer count) {
        var posts = jdbcTemplate.query("select * from (\n" +
                "\tselect *, 'GROUP_POST' as type, creator_id as creator from group_posts as gp\n" +
                "\tleft join viewed_group_posts as vgp on gp.id = vgp.post_id \n" +
                "\twhere gp.creator_id in (select group_id from users_groups where user_id = ?)\n" +
                "\tand vgp.post_id is null \n" +
                "\tunion \n" +
                "\tselect *, 'USER_POST' as type, creator_id as creator from user_posts as up\n" +
                "\tleft join viewed_user_posts as vup on up.id = vup.post_id\n" +
                "\twhere up.creator_id in (select user_id from subscriptions where subscribed_user_id = ?)\n" +
                "\tand vup.post_id is null\n" +
                ") order by time, id limit ?", new Object[]{userId, userId, count}, new BeanPropertyRowMapper<>(PostDto.class) {
        });
        var userPosts = posts.stream().filter(postDto -> postDto.getType().equals(PostDto.USER_POST)).toList();
        var groupPosts = posts.stream().filter(postDto -> postDto.getType().equals(PostDto.GROUP_POST)).toList();
        jdbcTemplate.batchUpdate("insert into viewed_user_posts(user_id, post_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var post = userPosts.get(i);
                        ps.setInt(1, post.getCreator());
                        ps.setInt(2, post.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return userPosts.size();
                    }
                });
        jdbcTemplate.batchUpdate("insert into viewed_group_posts(user_id, post_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        var post = groupPosts.get(i);
                        ps.setInt(1, post.getCreator());
                        ps.setInt(2, post.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return groupPosts.size();
                    }
                });
        return posts;
    }
}
