package hsdx.com.wjcxapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import hsdx.com.wjcxapp.Http.HttpThread;
import hsdx.com.wjcxapp.R;
import hsdx.com.wjcxapp.Util.ErrorToast;
import hsdx.com.wjcxapp.Util.JsonUtil;
import hsdx.com.wjcxapp.wjcx.WjcxcdOneActivity;

public class OneFragment extends Fragment implements View.OnClickListener ,AdapterView.OnItemClickListener {

    private Button btn1;
    private ProgressDialog progressdialog;
    private Handler mHandler = new Handler();
    //目录列表初始化数据
    private ListView mloneGridView;
    private List<Map<String, String>> mlDataList;
    private SimpleAdapter mlAdapter;
    private JsonUtil jsonUtil = new JsonUtil();


    private String mlone;//目录级别1
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_one, container, false);

        initView(view);
        selectFlOne();
        return view;

    }

    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        return fragment;
    }

    private void initView(View view) {
        mloneGridView = (ListView) view.findViewById(R.id.mlGridView);
        btn1 = (Button) view.findViewById(R.id.btnVPN);
        btn1.setOnClickListener(this);

        mloneGridView .setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnVPN) {

            selectFlOne();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Map map = mlDataList.get(i);
        mlone = map.get("MLBM").toString();


        Bundle bundle = new Bundle();
        bundle.putString("mlbm", mlone);
        Intent intent = new Intent(getActivity(), WjcxcdOneActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void selectFlOne(){

//        progressdialog = new ProgressDialog(getContext());
//        progressdialog.setMessage("正在操作，请稍候...");
//        progressdialog.setCancelable(true);//true 按BACK键可退出
//        Toast t = Toast.makeText(getContext(), "你大爷的！别乱点。。。", Toast.LENGTH_LONG);
//        t.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                 HttpThread httpThread = new HttpThread();
                final JSONObject wjResult = httpThread.getJsonObject("WjcxPhoneAction_getMlOne");

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray object1 = wjResult.getJSONArray("object1");
                            mlDataList = jsonUtil.jsonArray2ListMap(object1);

                            mlAdapter = new SimpleAdapter(getActivity(), mlDataList, R.layout.wjlb_one_item,
                                    new String[]{"NUM", "MLMC", "MLBM", "MLBM"},
                                    new int[]{R.id.numTextView, R.id.wjmcTextView1, R.id.wjlxTextView1, R.id.scsjTextView1});

                            mloneGridView.setAdapter(mlAdapter);

                            if (mlDataList.size() == 0) {
                                ErrorToast.show(getContext(), mHandler, "数据为空");
                            }
                        }catch (Exception e){

                        }
                    }
                });
            }
        }).start();
    }
}
