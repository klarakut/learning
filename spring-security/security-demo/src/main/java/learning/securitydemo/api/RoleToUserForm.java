package learning.securitydemo.api;

class RoleToUserForm {
    private String username;
    private String roleName;

    public RoleToUserForm(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public String getRoleName() {
        return roleName;
    }

}
