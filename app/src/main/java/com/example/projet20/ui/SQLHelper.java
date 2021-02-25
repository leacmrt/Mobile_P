package com.example.projet20.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;

public class SQLHelper {

    // JDBC driver name and database URL
    EditText name1Txt,name2Txt;
    SeekBar strengthSeek,scoreSeek;
    CalendarView date;
    ProgressDialog pd;

    String name1,name2;
    int strength , score;
    long  dat;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/projet?autoReconnect=true";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    public ArrayList<String> coucou(Context c) {
    Connection conn = null;
    Statement stmt = null;
    ArrayList<String> list = new ArrayList<String>();
        System.out.println("hehe");
        try{

        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
           /* conn =
                    DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/projet?" +
                            "user=root&password=");*/

        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT  Name1, Name2, Score FROM match_data";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("hi");
        int i =0;
        //STEP 5: Extract data from result set
        while(rs.next()){
            System.out.println("ho");
            //Retrieve by column name

            int age = rs.getInt("Score");
            String first = rs.getString("Name1");
            String last = rs.getString("Name2");


            list.add(first + " VS  " + last + " :" + Integer.toString(age));
            i++;

            //System.out.println(list.get(u));


        }
        //STEP 6: Clean-up environment
        rs.close();
        stmt.close();
        conn.close();
        return list;
    }catch(SQLException ex) {
        // handle any errors
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());

    }catch(Exception e){
        //Handle errors for Class.forName
        e.printStackTrace();
    }

        System.out.println("Goodbye!");
        return list;
}//end main
    public ArrayList<Integer> getallID(Context c) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Integer> list = new ArrayList<>();

        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT  id FROM match_data";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("id");
                list.add(id);
            }

            rs.close();
            stmt.close();
            conn.close();
            return list;
        }catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Integer> getallStrength(Context c) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Integer> list = new ArrayList<>();

        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Strength FROM match_data";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("Strength");
                list.add(id);
            }

            rs.close();
            stmt.close();
            conn.close();
            return list;
        }catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Integer> getallScore(Context c) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Integer> list = new ArrayList<>();

        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Score FROM match_data";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("Score");
                list.add(id);
            }

            rs.close();
            stmt.close();
            conn.close();
            return list;
        }catch(SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public void ajout(Activity a, Context c, EditText Name1, EditText Name2, SeekBar Score, SeekBar Strength, CalendarView dat1) {

        a.runOnUiThread(new Runnable() {
            public void run() {
                pd = new ProgressDialog(c);
                pd.setTitle("Send");
                pd.setMessage("Sending..Please wait");
                pd.show();
            }});Connection conn = null;
        Statement stmt = null;
        //this.c = c;


        //INPUT EDITTEXTS
        this.name1Txt=Name1;
        this.name2Txt=Name2;

        this.strengthSeek=Strength;
        this.scoreSeek=Score;
        this.date=dat1;

        //GET TEXTS FROM EDITEXTS
        name1=name1Txt.getText().toString();
        name2=name2Txt.getText().toString();
        strength=strengthSeek.getProgress();
        score=scoreSeek.getProgress();
        dat=date.getDate();



        String SQL = "INSERT INTO match_data(Name1,Name2,Strength,Score,Date) "
                + "VALUES(?,?,?,?,?)";


        try {


            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
           /* conn =
                    DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/projet?" +
                            "user=root&password=");*/

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try (


                PreparedStatement pstmt = conn.prepareStatement(SQL,
                        Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name1);
            pstmt.setString(2, name2);
            pstmt.setInt(3, strength);
            pstmt.setInt(4, score);
            pstmt.setDate(5,new Date(dat));

            int affectedRows = pstmt.executeUpdate();
            a.runOnUiThread(new Runnable() {
                public void run() {


                    pd.dismiss();

                    if(affectedRows!= 0)
                    {
                        //SUCCESS
                        Toast.makeText(c,"yes",Toast.LENGTH_LONG).show();

                        name1Txt.setText("");
                        name2Txt.setText("");
                        strengthSeek.setProgress(0);
                        scoreSeek.setProgress(0);
                        dat1.setDate(System.currentTimeMillis());

                    }else
                    {
                        //NO SUCCESS
                        Toast.makeText(c,"Unsuccessful "+affectedRows,Toast.LENGTH_LONG).show();
                    }
                }
            });

            // check the affected rows

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


    }

    public String getName1(int id) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        String nametmp = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT  Name1 FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Name1");
                nametmp=name;
                return nametmp;
            }
        }
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
      return "unknown";
    }

    public String getName2(int id) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        String nametmp = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT  Name2 FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Name2");
                nametmp=name;
                return nametmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "unknown";
    }

    public int getScore(int id) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        int scoretmp = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Score FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int score= rs.getInt("Score");
                scoretmp=score;
                return scoretmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int getStrength(int id) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        int scoretmp = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Strength FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int score= rs.getInt("Strength");
                scoretmp=score;
                return scoretmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
}
