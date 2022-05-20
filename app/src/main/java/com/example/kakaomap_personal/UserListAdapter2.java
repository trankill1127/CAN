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

public class UserListAdapter2 extends BaseAdapter {

    private Context context;
    private List<User2> userList1;
    private Activity parentActivity;

    //생성자 생성
    public UserListAdapter2(Context context, List<User2> userList1, Activity parentActivity){
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
        View v = View.inflate(context,R.layout.user2,null);

        final TextView userName = (TextView)v.findViewById(R.id.userName);
        TextView userTotal = (TextView)v.findViewById(R.id.userTotal);

        userName.setText(userList1.get(position).getUserName());
        userTotal.setText(userList1.get(position).getUserTotal());

        //특정 user에 아이디값을 그대로 반환할수 있게 해준다
        v.setTag(userList1.get(position).getUserName());


        return v;

    }
}