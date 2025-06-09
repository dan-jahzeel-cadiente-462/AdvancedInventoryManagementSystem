/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventory.dao;

import com.inventory.model.Role;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author USER
 */
public interface RolesDAO {
    public Role getRoleById(int id) throws SQLException;
    public List<Role> getAllRoles() throws SQLException;
    public void addRole(Role role) throws SQLException;
    public void updateRole(Role role) throws SQLException;
    public void deleteRole(int id) throws SQLException;
    public String getRoleNameById(int id) throws SQLException;
    public int getRoleIdByName(String roleName) throws SQLException;
    public List<String> getAllRoleNames() throws SQLException;
}
