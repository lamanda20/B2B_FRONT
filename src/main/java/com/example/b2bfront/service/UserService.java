package com.example.b2bfront.service;

import com.example.b2bfront.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class UserService {

    private final ApiService apiService;
    private final Gson gson;

    public UserService() {
        this.apiService = new ApiService();
        this.gson = new Gson();
    }

    public List<User> getAllUsers() throws IOException {
        String response = apiService.get("/users");
        Type listType = new TypeToken<List<User>>(){}.getType();
        return gson.fromJson(response, listType);
    }

    public User getUserById(Long id) throws IOException {
        return apiService.get("/users/" + id, User.class);
    }

    public User createUser(User user) throws IOException {
        return apiService.post("/users", user, User.class);
    }

    public User updateUser(Long id, User user) throws IOException {
        String response = apiService.post("/users/" + id, user);
        return gson.fromJson(response, User.class);
    }
}

