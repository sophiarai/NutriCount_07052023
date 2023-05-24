@Entity (tableName="users")
public class UserEntity {
    @PrimaryKey (autoGenerate = true)
    Integer id;

    @ColumnInfo(name="username")
    String username;

    @ColumnInfo(name="email")
    String useremail;

    @ColumnInfo (name="password")
    String userpassword;

    @ColumnInfo(name="repassword")
    String userrepassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserrepassword() {
        return userrepassword;
    }

    public void setUserrepassword(String userrepassword) {
        this.userrepassword = userrepassword;
}
}