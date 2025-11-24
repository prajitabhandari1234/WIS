/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cqu.wis.roles;

import cqu.wis.data.UserData;

/**
 *
 * @author acer
 */
public class UserDataManager {
    private final UserData ud;

    public UserDataManager(UserData ud) {
        this.ud = ud;
    }

    public UserData.UserDetails findUser(String name) {
        return ud.findUser(name);
    }

    public void updatePassword(String name, String newPassword) {
        ud.updatePassword(name, newPassword);
    }
}



