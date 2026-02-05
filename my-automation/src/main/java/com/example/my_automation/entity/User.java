package com.example.my_automation.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "discovery")
@Cacheable
public class User extends BaseDomainObject {

    private static final long serialVersionUID = 1L;
    
    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Search> searches;
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Search> getSearches() {
        return searches;
    }
    
    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }
    
    @Override
	public String toString() {
		return "User{" +
				"username=" + username +
				", id=" + id +
				'}';
	}
}
