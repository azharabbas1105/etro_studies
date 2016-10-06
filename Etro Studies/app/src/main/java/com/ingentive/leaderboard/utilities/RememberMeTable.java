package com.ingentive.leaderboard.utilities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC on 04-10-2016.
 */

@Table(name="RememberMeTable")
public class RememberMeTable extends Model {

    @Column(name = "is_checked")
    public boolean isChecked;

    @Column(name = "sit_number")
    public String siteNo;

    @Column(name = "password")
    public String password;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getSiteNo() {
        return siteNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }

    public RememberMeTable() {
        super();
        this.isChecked = false;
        this.siteNo="";
        this.password="";
    }
}
