package hsdx.com.wjcxapp.Util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 错误提示
 * create by author vvyunfei on 2018/10/15 10
 */
public class ErrorToast {

    public static void show(Context context, Handler handler, String text) {
        handler.post(new runShowToast(context, text));
    }

    private static class runShowToast implements Runnable {
        Context context;
        String text;
        runShowToast(Context context, String text) {
            this.context = context;
            this.text = text;
        }
        @Override
        public void run() {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
