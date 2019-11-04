package hsdx.com.wjcxapp.wjcx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import hsdx.com.wjcxapp.Http.HttpThread;
import hsdx.com.wjcxapp.R;
import hsdx.com.wjcxapp.Util.ErrorToast;
import hsdx.com.wjcxapp.Util.FileUpDownUtil;
import hsdx.com.wjcxapp.Util.JsonUtil;
import hsdx.com.wjcxapp.Util.MessageAlertDialog;

public class WjcxcdTwoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String mlbm;
    private ProgressDialog progressdialog;
    private Handler mHandler = new Handler();
    //目录列表初始化数据
    private ListView wjlmGridView;
    private List<Map<String, String>> wjDataList;
    private SimpleAdapter wjAdapter;
    private JsonUtil jsonUtil = new JsonUtil();
    private String uuid;
    private String wjmc;//文件名称
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wjcxcd_two);

        initView();

    }

    private void initView(){
        wjlmGridView=(ListView)findViewById(R.id.wjlbGridView);
        wjlmGridView.setOnItemClickListener(this);
        Bundle extras = getIntent().getExtras();
        mlbm = extras.getString("mlbm");
        selectWjlb(mlbm);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

        Map map = wjDataList.get(i);
        uuid = map.get("UUID").toString();
        wjmc= map.get("WJM").toString();
        Log.e("11",wjmc);
        //打开提示框
        AlertDialog(uuid,wjmc);

    }

    private void AlertDialog(final String uuid, final String wjmc){
        AlertDialog.Builder builder = new AlertDialog.Builder(WjcxcdTwoActivity.this);
        builder.setTitle("查看文件");
        builder.setMessage("确定要查看文件吗?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownFile(uuid,wjmc);
                Toast.makeText(WjcxcdTwoActivity.this, "你点击了是的"+uuid, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }


        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WjcxcdTwoActivity.this, "你点击了不是", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e("11","对话框显示了");
            }
        });
        //对话框消失的监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("2","对话框消失了");
            }
        });
        //显示对话框
        dialog.show();

    }

    private void selectWjlb(final String mlbm){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpThread httpThread = new HttpThread();
                final JSONObject wjResult = httpThread.getJsonObject("WjcxPhoneAction_getWjml","wjml="+mlbm);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray object1 = wjResult.getJSONArray("object1");
                            wjDataList = jsonUtil.jsonArray2ListMap(object1);

                            wjAdapter = new SimpleAdapter(WjcxcdTwoActivity.this, wjDataList, R.layout.wjlb_wjm_item,
                                    new String[]{"NUM", "WJM", "UUID"},
                                    new int[]{R.id.numTextView, R.id.wjmcTextView, R.id.wjlxTextView});

                            wjlmGridView.setAdapter(wjAdapter);
                            if (wjDataList.size() == 0) {
                                ErrorToast.show(WjcxcdTwoActivity.this, mHandler, "数据为空");
                            }
                        }catch (Exception e){
                            MessageAlertDialog.ErrorMessageDialog(WjcxcdTwoActivity.this, mHandler, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private  void DownFile(String uuid,String wjmc){
        HttpThread httpThread;
        String filePath = Environment.getExternalStorageDirectory() + "/hsdx/" + wjmc;
        File file = new File(filePath);
        //判断文件是否存在 存在则直接打开 否则下载后打开
        if (file.exists()) {
            FileUpDownUtil.openFile(WjcxcdTwoActivity.this, file);
        } else {
            File newfile = new File(Environment.getExternalStorageDirectory() + "/hsdx");
            newfile.mkdirs();
            httpThread = new HttpThread();
            String upFileIp = httpThread.ip_link;
            FileUpDownUtil.download(WjcxcdTwoActivity.this, upFileIp+"WjcxPhoneAction_downfile?uuid=" + uuid);
        }
    }


//    private class MyAdapter1 extends SimpleAdapter{
//
//        public MyAdapter1(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
//            super(context, data, resource, from, to);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return super.getView(position, convertView, parent);
//        }
//    }
}
