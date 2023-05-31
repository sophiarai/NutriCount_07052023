package com.example.nutricount_07052023.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsername(String username);



}
