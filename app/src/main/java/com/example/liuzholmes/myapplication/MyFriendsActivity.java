package com.example.liuzholmes.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MyFriendsActivity extends AppCompatActivity {

    private static final String TAG = "ASYNC_TASK";

    private class ShowFriendsTask extends AsyncTask<String, Integer, String[]>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.d(TAG,"onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            ArrayList<String> friends = new ArrayList<String>();
            Connection conn = null;
            Statement stmt = null;
            // JDBC 驱动名及数据库 URL
            String JDBC_DRIVER = "com.mysql.jdbc.Driver";
            String URL = "jdbc:mysql://123.207.26.102:3306/tongdao";
            String USER = "root";
            String PASS = "123456";
            try{
                // 注册 JDBC 驱动
                Class.forName(JDBC_DRIVER);

                // 打开链接
                System.out.println("连接数据库...");
                conn = DriverManager.getConnection(URL,USER,PASS);

                // 执行查询
                System.out.println(" 实例化Statement对...");
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT second FROM relation WHERE first = \'" + params[0] + "\'" ;;
                System.out.println("查询语句为："+ sql);
                ResultSet rs = stmt.executeQuery(sql);

                // 展开结果集数据库
                while(rs.next())
                {
                    // 通过字段检索
                    String friendId = rs.getString("second");
                    friends.add(friendId);

                }
                // 完成后关闭
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                // 处理 JDBC 错误
                se.printStackTrace();
            }catch(Exception e){
                // 处理 Class.forName 错误
                e.printStackTrace();
            }finally{
                // 关闭资源
                try{
                    if(stmt!=null) stmt.close();
                }catch(SQLException se2){
                }// 什么都不做
                try{
                    if(conn!=null) conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }
            }
            return (String[]) friends.toArray(new String[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            Log.d(TAG,"onProgressUpdate values[0]="+ values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyFriendsActivity.this,android.R.layout.simple_list_item_1,result);
            final ListView Friends = (ListView) findViewById(R.id.ListView_Friends);
            Friends.setAdapter(adapter);
            Friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String friendId = Friends.getItemAtPosition(i).toString();
                    Intent intent=new Intent(MyFriendsActivity.this,FriendInfoActivity.class);
                    intent.putExtra("id",friendId);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        new ShowFriendsTask().execute(id);
    }
}
