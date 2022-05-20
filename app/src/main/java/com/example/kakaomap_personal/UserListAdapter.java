package com.example.kakaomap_personal;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<User> userList1;
    private Activity parentActivity;

    //생성자 생성
    public UserListAdapter(Context context, List<User> userList1, Activity parentActivity){
        this.context = context;
        this.userList1 = userList1;
        this.parentActivity = parentActivity;
    }

    @Override
    public int getCount() {
        //현재사용자의 개수 반환
        return userList1.size();
    }

    @Override
    public Object getItem(int position) {
        return userList1.get(position);
    }

    @Override
    public long getItemId(int position) {
        //그대로 반환
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //하나의 사용자에대한 view를 보여주는 부분
        //한명의 사용자에대한 view가 만들어진다.
        View v = View.inflate(context,R.layout.user,null);
        final TextView userID = (TextView)v.findViewById(R.id.userID);
        TextView userPassword = (TextView)v.findViewById(R.id.userPassword);
        TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView userTotal = (TextView)v.findViewById(R.id.userTotal);

        userID.setText(userList1.get(position).getUserID());
        userPassword.setText(userList1.get(position).getUserPassword());
        userName.setText(userList1.get(position).getUserName());
        userTotal.setText(userList1.get(position).getUserTotal());

        //특정 user에 아이디값을 그대로 반환할수 있게 해준다
        v.setTag(userList1.get(position).getUserID());

        Button deleteButton = (Button) v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                userList1.remove(position);
                                notifyDataSetChanged();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID.getText().toString(),responseListener);
                RequestQueue queue = Volley.newRequestQueue(parentActivity);
                queue.add(deleteRequest);
            }
        });


        return v;

    }
}