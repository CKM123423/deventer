package com.sparta.deventer.test;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.UserLoginType;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.repository.CategoryRepository;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataGenerator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public User generateUser() {

        User user = new User(
                "username",
                "password",
                "nickname",
                UserRole.ADMIN,
                "user2345@email.com",
                UserLoginType.DEFAULT
        );

        userRepository.save(user);

        return user;
    }

    public Category generateCategory() {
        Category category = new Category("backend");

        categoryRepository.save(category);

        return category;
    }

    public List<Post> generatePosts(User user, Category category) {

        List<Post> posts = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Post post = new Post(
                    "Title " + i,
                    "Content " + i,
                    user,
                    category
            );

            posts.add(post);
        }

        postRepository.saveAll(posts);

        return posts;
    }

    public void generateComments(User user, List<Post> posts) {

        List<Comment> comments = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Post post = posts.get((0));

            Comment comment = new Comment(
                    post,
                    user,
                    "Comment " + i
            );

            comments.add(comment);
        }

        commentRepository.saveAll(comments);
    }

    public void generateTestData() {
        User user = generateUser();
        Category category = generateCategory();
        List<Post> posts = generatePosts(user, category);
        generateComments(user, posts);
    }
}
