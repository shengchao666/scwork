package hsdx.com.wjcxapp.wjcx;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import hsdx.com.wjcxapp.Http.HttpThread;
import hsdx.com.wjcxapp.R;
import hsdx.com.wjcxapp.Util.ErrorToast;
import hsdx.com.wjcxapp.Util.JsonUtil;

public class WjcxcdOneActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String mlbm;

    private ProgressDialog progressdialog;
    private Handler mHandler = new Handler();
    //目录列表初始化数据
    private ListView mltwoGridView;
    private List<Map<String, String>> mlDataList;
    private SimpleAdapter mlAdapter;
    private JsonUtil jsonUtil = new JsonUtil();
    private String mltwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wjcxcd_one);

        initView();

    }

    private void initView(){
        Bundle extras = getIntent().getExtras();
        mlbm = extras.getString("mlbm");
        mltwoGridView = (ListView)findViewById(R.id.mloneGridView);

        mltwoGridView .setOnItemClickListener(this);
        selectFlTwo(mlbm);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

        Map map = mlDataList.get(i);
        mltwo = map.get("MLBM").toString();


        Bundle bundle = new Bundle();
        bundle.putString("mlbm", mltwo);
        Intent intent = new Intent(WjcxcdOneActivity.this, WjcxcdTwoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void selectFlTwo(final String mlbm){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpThread httpThread = new HttpThread();
                final JSONObject wjResult = httpThread.getJsonObject("WjcxPhoneAction_getMlTwo","wjsjml="+mlbm);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray object1 = wjResult.getJSONArray("object1");
                            mlDataList = jsonUtil.jsonArray2ListMap(object1);

                            mlAdapter = new SimpleAdapter(WjcxcdOneActivity.this, mlDataList, R.layout.wjlb_one_item,
                                    new String[]{"NUM", "MLMC", "MLBM", "MLBM"},
                                    new int[]{R.id.numTextView, R.id.wjmcTextView1, R.id.wjlxTextView1, R.id.scsjTextView1});

                            mltwoGridView.setAdapter(mlAdapter);

                            if (mlDataList.size() == 0) {
                                ErrorToast.show(WjcxcdOneActivity.this, mHandler, "数据为空");
                            }
                        }catch (Exception e){

                        }
                    }
                });
            }
        }).start();
    }
}
