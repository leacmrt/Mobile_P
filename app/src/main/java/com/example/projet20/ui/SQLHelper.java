package com.example.projet20.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
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
//Classe utilisée pour accéder à la BDD externe Mysql


    //Element de récupération des données envoyées par l'utilisateur
    EditText name1Txt,name2Txt,critiquetxt;
    SeekBar strengthSeek,scoreSeek;
    NumberPicker pick1,pick2;
    CalendarView date;
    ProgressDialog pd;
    ImageView img;

    String name1,name2,critique1, dat,loc;
    int strength , score,score1,score2;

    //Driver
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //Lien pour accèder au projet ; Nom = projet
    static final String DB_URL = "jdbc:mysql://10.0.2.2:3306/projet?autoReconnect=true";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";


    public ArrayList<String> coucou(Context c)//Fonction qui récupère Les 2 Noms + date d'un match
    {
    Connection conn = null;//initialisation
    Statement stmt = null;
    ArrayList<String> list = new ArrayList<String>();
        //System.out.println("hehe");
        try{

        Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);//connection à la base de donnée



        System.out.println("Creating statement...");

        stmt = conn.createStatement();//Création du statement
        String sql;
        sql = "SELECT  Name1, Name2,Date FROM match_data";//Requete SQL
        ResultSet rs = stmt.executeQuery(sql);//Execution de la requete

        int i =0;
        //Extraction de la data du resultat de la requette
        while(rs.next())
        {

            //Retrieve by column name

            String age = rs.getString("Date");
            String first = rs.getString("Name1");
            String last = rs.getString("Name2");


            list.add(first + "  VS  " + last + " (" +age+")");//ajout des données sous la forme voulue
            i++;



        }
        //Clean-up environment
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


    public ArrayList<Integer> getallID(Context c) //Recupère tout les ID des matchs dans la BDD
    {
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

    public ArrayList<Integer> getallStrength(Context c) //Recupere toutes les "forces" des matchs
    {
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

    public ArrayList<Integer> getallScore(Context c)//Recupere tous les scores des matchs
    {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Integer> list = new ArrayList<>();

        try{

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Technic FROM match_data";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("Technic");
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
    public void ajout(Activity a, Context c, EditText Name1, EditText Name2, SeekBar Score, SeekBar Strength, CalendarView dat1, String date1,
                      EditText crtique, Bitmap image, ImageView imageView, String returnString, NumberPicker picker1,NumberPicker picker2) {
      //Fonction d'ajout d'un match dans la base de données


        a.runOnUiThread(new Runnable() {//sur la thread principale , rond de chargement
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
        this.critiquetxt=crtique;
        this.strengthSeek=Strength;
        this.scoreSeek=Score;
        this.date=dat1;
        this.img=imageView;
        this.pick1=picker1;
        this.pick2=picker2;

        //GET TEXTS FROM EDITEXTS
        name1=name1Txt.getText().toString();
        name2=name2Txt.getText().toString();
        strength=strengthSeek.getProgress();
        score=scoreSeek.getProgress();
        score1=pick1.getValue();
        score2=pick2.getValue();
        critique1=critiquetxt.getText().toString();
        loc=returnString;


        if(date1==null)//si la date n'a pas été pr"cisé
        {
            long stuff = date.getDate();
            Date jsp = new Date(stuff);
            dat =jsp.toString();
        }else dat=date1;

        //Conversion de la bitmap en tableau de byte
        Bitmap image1 = image;
        byte[] bArray;
        if(image1!=null) //si une photo a été prise (car pas obligatoir)
        {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.PNG, 100, bos);//compression
        bArray = bos.toByteArray();
        }else  bArray = null;


        String SQL = "INSERT INTO match_data(Name1,Name2,Score1,Score2,Strength,Technic,Date,Critique,Photo,Localisation) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";//Requete SQL


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

                PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS))//ici nous utlisons un prepared Statement pour plus de sureté
        {
            //conversion du tableau de byte en BLOB ==> nous pouvons maintenant insérer l'image dans la BDD
            Blob blob = pstmt.getConnection().createBlob();


            pstmt.setString(1, name1);//toutes les données sont insérées
            pstmt.setString(2, name2);
            pstmt.setInt(3, score1);
            pstmt.setInt(4, score2);
            pstmt.setInt(5, strength);
            pstmt.setInt(6, score);
            pstmt.setString(7,dat);
            pstmt.setString(8,critique1);
            if(bArray!=null)
            {
                blob.setBytes(1, bArray);
                pstmt.setBlob(9,blob);

            }
            else {
                pstmt.setBlob(9,(Blob)null);
            }
            pstmt.setString(10,loc);

            int affectedRows = pstmt.executeUpdate();//execution de la requete

            a.runOnUiThread(new Runnable() {//dans la thread principales
                public void run() {


                    pd.dismiss();//le rond de chargement finit de charger

                    if(affectedRows!= 0)
                    {
                        //SUCCESS
                        //Re-initialisation de tout les composants
                        name1Txt.setText("Name 1st Team");
                        name2Txt.setText("Name 2nd Team");
                        strengthSeek.setProgress(0);
                        scoreSeek.setProgress(0);
                        dat1.setDate(System.currentTimeMillis());
                        crtique.setText("");
                        img.setImageBitmap(null);
                        picker1.setValue(0);
                        picker2.setValue(0);

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

    public String getName1(int id) throws ClassNotFoundException { //recupère un nom1 en fonction de l'id
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

    public String getName2(int id) throws ClassNotFoundException { //recupère un nom2 en fonction de l'id
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
    public String getCritique(int id) throws ClassNotFoundException { //recupère une critique en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        String nametmp = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Critique FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Critique");
                nametmp=name;
                return nametmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "unknown";
    }
    public String getLocalisation(int id) throws ClassNotFoundException { //recupère unne localisation en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        String nametmp = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Localisation FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("Localisation");
                nametmp=name;
                return nametmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "unknown";
    }

    public Blob getPicture(int id) throws ClassNotFoundException { //recupère d'une photo en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        Blob nametmp = null;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Photo FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Blob name = rs.getBlob("Photo");
                nametmp=name;
                return nametmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int getScore(int id) throws ClassNotFoundException { //recupère une technique en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        int scoretmp = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Technic FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int score= rs.getInt("Technic");
                scoretmp=score;
                return scoretmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }
    public int getScore1(int id) throws ClassNotFoundException { //recupère un score1 en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        int scoretmp = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Score1 FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int score= rs.getInt("Score1");
                scoretmp=score;
                return scoretmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int getScore2(int id) throws ClassNotFoundException {//recupère un score2 en fonction de l'id
        Connection conn = null;
        Statement stmt = null;
        int scoretmp = 0;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT Score2 FROM match_data WHERE id ="+id+";";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int score= rs.getInt("Score2");
                scoretmp=score;
                return scoretmp;
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    public int getStrength(int id) throws ClassNotFoundException {//recupère une force en fonction de l'id
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

   /* public void ajout(FragmentActivity activity, Context context, EditText name1, EditText name2, SeekBar strength, SeekBar score, CalendarView dat, String date1, EditText crtique, Blob image, ImageView imageView) {
    }*/
}
