package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ers_user_roles")
public class Role {
	
	@Id private int id;
	private String role;
	
	public Role() {}

	public Role(int userRoleId, String userRole) {
		super();
		this.id = userRoleId;
		this.role = userRole;
	}

	public int getId() {
		return id;
	}

	public void setId(int userRoleId) {
		this.id = userRoleId;
	}

	public String getRole() {
		return role;
	}

	public void setUserRole(String userRole) {
		this.role = userRole;
	}

	@Override
	public String toString() {
		return "Role [userRoleId=" + id + ", userRole=" + role + "]";
	}
	
	
}
