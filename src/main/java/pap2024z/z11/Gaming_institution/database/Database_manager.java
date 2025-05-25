package pap2024z.z11.Gaming_institution.database;

import org.hibernate.dialect.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class Database_manager {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) throws Exception {

    }
    public void check_working() throws Exception {
        //simple instruction to see if database works
        String query = "SELECT LoginName FROM UserCredentials";
        try{
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                System.out.println(rs.getString("LoginName"));
            }
            rs.close();
            conn.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public int adding_user(String Login, String Password, String Email, String Age) throws Exception {
        if (!Email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format: " + Email);
        }

        try (Connection conn = dataSource.getConnection()) {
            if (conn == null) {
                throw new SQLException("Connection is null");
            }

            String sql = "{call adduser(?, ?, ?, ?, ?)}";
            try (CallableStatement cstmt = conn.prepareCall(sql)) {
                int ageNum = Integer.parseInt(Age);
                cstmt.setString(1, Login);
                cstmt.setString(2, Password);
                cstmt.setString(3, Email);
                cstmt.setInt(4, ageNum);
                cstmt.registerOutParameter(5, OracleTypes.CURSOR);
                cstmt.execute();
                try (ResultSet rs = (ResultSet) cstmt.getObject(5)) {
                    if (rs.next()) {
                        Integer result = rs.getInt(1);
                        rs.close();
                        return result;
                    } else {
                        rs.close();
                        throw new Exception("Failed to add user: no UserId returned.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Database error while adding user: " + e.getMessage(), e);
        }
    }
    //get all info of the user
    public List<String> login_in_user(String Login, String Password) throws Exception {
        //log in user if given Login and Password matches the ones stored in DB
        List<String> UserData = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        if(conn == null) {
            throw new SQLException();
        }
        String sql = "{call LoginUserProcedure(?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            // Set the parameters
            cstmt.setString(1, Login);
            cstmt.setString(2, Password);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.execute();
            ResultSet rs = (ResultSet) cstmt.getObject(3);
            rs.next();
            UserData.add(rs.getString("UserId"));
            UserData.add(rs.getString("LoginName"));
            UserData.add(rs.getString("MoneyAmount"));
            UserData.add(rs.getString("LoanAmount"));
            UserData.add(rs.getString("LevelAmount"));
            UserData.add(rs.getString("Email"));
            UserData.add(rs.getString("Age"));
            rs.close();
            String sql2 = "{call startSessionProcedure(?,?)}";
            CallableStatement cstmtses = conn.prepareCall(sql2);
            cstmtses.setInt(1, Integer.parseInt(UserData.getFirst()));
            cstmtses.registerOutParameter(2, OracleTypes.CURSOR);
            cstmtses.execute();
            ResultSet rsses = (ResultSet)  cstmtses.getObject(2);
            rsses.next();
            UserData.add(rsses.getString("Session_id"));
            rsses.close();
            //System.out.println(UserData);
            return UserData;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //System.out.println(UserData);
            return UserData;
        } finally {
            conn.close();
        }
    }
    public int AddMoney(String Login, int Money) throws Exception {
        //gives user flat amount of money
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call AddMoney(?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.setString(2, Integer.toString(Money));
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet)  cstmt.getObject(3);
            rs.next();
            Integer result = rs.getInt(1);
            rs.close();
            return result; 
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            conn.close();
        }
    }
    public int SetMoney(String Login, int Money) throws Exception {
        //gives user flat amount of money
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call SetMoney(?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.setString(2, Integer.toString(Money));
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet)  cstmt.getObject(3);
            rs.next();
            Integer result = rs.getInt(1);
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            conn.close();
        }
    }
    public String SetEmail(String Login, String Email) throws Exception {
        //gives user flat amount of money
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call SetEmail(?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.setString(2, Email);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet)  cstmt.getObject(3);
            rs.next();
            String result = rs.getString(1);
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return "";
        } finally {
            conn.close();
        }
    }
    public String SetLogin(String Login, String NewLogin) throws Exception {
        //gives user flat amount of money
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call SetLogin(?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.setString(2, NewLogin);
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet)  cstmt.getObject(3);
            rs.next();
            String result = rs.getString(1);
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return "";
        } finally {
            conn.close();
        }
    }
    public int AddLoan(int UserId, int Loan) throws Exception {
        //make player with said UserId take a Loan.
        //This keeps balance of the money in the account but
        //Adds money and loan
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call AddLoan(?, ?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Integer.toString(UserId));
            cstmt.setString(2, Integer.toString(Loan));
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(3);
            rs.next();
            Integer result = rs.getInt(2);
            rs.close();
            return result; 
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            conn.close();
        }
    }
    public int AddExperience(int UserId, int Experience) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call AddExperience(?, ?, ?)} ";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Integer.toString(UserId));
            cstmt.setString(2, Integer.toString(Experience));
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(3);
            rs.next();
            Integer result = rs.getInt(1);
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            conn.close();
        }
    }
    private static final ArrayList<String> columnsNames= new ArrayList<String>(Arrays.asList("LoginName","CreatedDate","ModifiedDate","MoneyAmount","loanAmount","levelAmount","Email"));
    public String CheckUserResources(int UserId, String ColumnName) throws Exception {
        //allows for checking of resources by giving ColumnName of said resource. ColumnName must be written correctly
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call CheckUserResources(?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, UserId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            rs.next();
            if(columnsNames.contains(ColumnName)){
                String result = rs.getString(ColumnName);
                rs.close();
                return result;
            }
            else{
                rs.close();
                return "";
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return "";
        }
        finally {
            conn.close();
        }
    }
    public String CheckUserResources(String Login, String ColumnName) throws Exception {
        //allows for checking of resources by giving ColumnName of said resource. ColumnName must be written correctly
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call CheckUserResourcesByLogin(?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            rs.next();
            if(columnsNames.contains(ColumnName)){
                String result = rs.getString(ColumnName);
                rs.close();
                return result;
            }
            else{
                rs.close();
                return "";
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return "";
        }
        finally {
            conn.close();
        }
    }
    public int EndSession(int SessionId) throws Exception {
        //ends session of the user. thus logging out the user
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call endSession(?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, SessionId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            if(rs.next()) {
                Integer result = rs.getInt(1);
                rs.close();
                return result;
            }
            else{
                rs.close();
                return -2;
            }
        }
        catch (Exception e) {
            System.out.println(e);
            return -1;
        }
        finally {
            conn.close();
        }

    }
    public boolean DeleteUser(String Login) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call DeleteUser (?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.executeQuery();
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        finally {
            conn.close();
        }
    }
    public Boolean ChangePassword(String Login, String OldPassword, String NewPassword) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call changePasswordFromPassword(?,?,?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Login);
            cstmt.setString(2, OldPassword);
            cstmt.setString(3, NewPassword);
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(4);
            if(rs.next()){
                Integer result = rs.getInt(1);
                rs.close();
                return (result != 0);
            }
            rs.close();
            return false;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
        finally {
            conn.close();
        }
    }
    public int GetUserSessionId(int SessionId) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        String sql = "{call GetUserSessionId(?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setInt(1, SessionId);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            if(rs.next()){
                int result = rs.getInt(1);
                rs.close();
                return (result);
            }
            rs.close();
            return -1;
        }
        catch (Exception e) {
            System.out.println(e);
            return -1;
        }
        finally {
            conn.close();
        }
    }
    public List<String> MakeNewPassword(String Email) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        List<String> result = new ArrayList<>();
        String sql = "{call MakeNewPassword(?,?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, Email);
            cstmt.registerOutParameter(2, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(2);
            if(rs.next()){
                result.add(rs.getString(1));
                result.add(rs.getString(2));
                rs.close();
                return (result);
            }
            rs.close();
            return result;
        }
        catch (Exception e) {
            System.out.println(e);
            return result;
        }
        finally {
            conn.close();
        }
    }
    public List<List<String>> GetEveryPlayer() throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        List<List<String>> result = new ArrayList<>();
        String sql = "{call SELECTALLPEOPLE(?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            int i=0;
            while(rs.next() && i<10){
                result.add(Arrays.asList(rs.getString(1),rs.getString(2)));
                i++;
            }
            rs.close();
            return result;
        }
        catch (Exception e) {
            System.out.println(e);
            return result;
        }
        finally {
            conn.close();
        }
    }
    public List<List<String>> GetNPlayer(int N) throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        List<List<String>> result = new ArrayList<>();
        String sql = "{call ADMINSELECTALLPEOPLE(?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            int i=0;
            while(rs.next()){
                //UserID, , PASSWORDHASH, age, MONEYAMOUNT,LOANAMOUNT,CREATEDDATE, MODIFIEDDATE
                if(i>=N && i < N+5)
                    result.add(Arrays.asList(rs.getString("USERID"),rs.getString("LOGINNAME"),rs.getString("AGE"),
                        rs.getString("MONEYAMOUNT"),rs.getString("LOANAMOUNT"),rs.getString("EMAIL"),rs.getString("CREATEDDATE"),rs.getString("MODIFIEDDATE")));
            i++;
            }
            rs.close();
            return result;
        }
        catch (Exception e) {
            System.out.println(e);
            return result;
        }
        finally {
            conn.close();
        }
    }
    public List<List<String>> GetPlayerCountOverTime() throws Exception {
        Connection conn = dataSource.getConnection();
        if(conn == null){
            throw new SQLException();
        }
        List<List<String>> result = new ArrayList<>();
        String sql = "{call PlayerCumulativeCount(?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            while(rs.next()){
                result.add(Arrays.asList(rs.getString("dzien"),rs.getString("ilosc")));
            }
            rs.close();
            return result;
        }
        catch (Exception e) {
            System.out.println(e);
            return result;
        }
        finally {
            conn.close();
        }
    }
}