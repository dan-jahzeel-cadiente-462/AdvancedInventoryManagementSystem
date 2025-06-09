/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface UsersDAO {
    public User getUserById(int id) throws SQLException;
    public User getUserByUsername(String username) throws SQLException;
    public int getUserIdByUsername(String username) throws SQLException;
    public List<User> getAllUsers() throws SQLException;
    public void addUser(User user) throws SQLException;
    public void updateUser(User user) throws SQLException;
    public void deleteUser(int id) throws SQLException;
    public List<User> searchUsers(String searchTerm) throws SQLException;
}
