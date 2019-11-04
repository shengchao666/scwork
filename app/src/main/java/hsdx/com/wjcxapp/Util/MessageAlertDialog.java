package hsdx.com.wjcxapp.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;


/**
 * 弹出式提示消息
 * create by author wyunfei on 2018/9/11 16
 */
public class MessageAlertDialog {

    private Context Context;
    private String title;
    private String message;
    private DialogInterface.OnClickListener onClickListener;
    private DialogInterface.OnClickListener onClickListener2;

    /**
     * 只有确认按钮
     *
     * @param Context Context
     * @param title   标题
     * @param message 消息
     */
    public MessageAlertDialog(Context Context, String title, String message) {
        setContext(Context);
        setMessage(message);
        setTitle(title);
    }

    /**
     * 带取消按钮 以及确定点击事件
     *
     * @param Context         Context
     * @param title           标题
     * @param message         消息
     * @param onClickListener 确定按钮点击事件
     */
    public MessageAlertDialog(Context Context, String title, String message,
                              DialogInterface.OnClickListener onClickListener) {
        setContext(Context);
        setMessage(message);
        setTitle(title);
        setOnClickListener(onClickListener);
    }

    /**
     * 带取消按钮 以及确定点击事件取消点击事件
     *
     * @param Context          Context
     * @param title            标题
     * @param message          消息
     * @param onClickListener1 确定按钮点击事件
     * @param onClickListener2 取消按钮点击事件
     */
    public MessageAlertDialog(Context Context, String title, String message,
                              DialogInterface.OnClickListener onClickListener1, DialogInterface.OnClickListener onClickListener2) {
        setContext(Context);
        setMessage(message);
        setTitle(title);
        setOnClickListener(onClickListener1);
        setOnClickListener2(onClickListener2);
    }

    /**
     * 显示提示（只有确认按钮的）
     */
    public void showDiglog() {
        if (getContext() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            //    显示出该对话框
            builder.show();
        }
    }

    /**
     * 显示提示（只有确认按钮以及确认点击事件的）
     */
    public void showDiglogWithQAndQClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        //    显示出该对话框
        builder.show();
    }

    public void showDiglogWithQAndQClick(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(text, onClickListener);
        //    显示出该对话框
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 显示提示 有确认和取消按钮以及确认点击事件的
     */
    public void showDiglogWithQ() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //    显示出该对话框
        builder.show();
    }

    /**
     * 显示提示 有确认和取消按钮以及确认和取消点击事件的
     */
    public void showDiglogQE() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", onClickListener2);
        //    显示出该对话框
        builder.show();
    }

    public Context getContext() {
        return Context;
    }

    public void setContext(Context Context) {
        this.Context = Context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnClickListener2(DialogInterface.OnClickListener onClickListener2) {
        this.onClickListener2 = onClickListener2;
    }

    //错误信息提示
    public static void ErrorMessageDialog(final Context Context, final Handler mHandler, final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MessageAlertDialog m = new MessageAlertDialog(Context, "提示", message);
                m.showDiglog();
            }
        });
    }

    /**
     * 确认信息提示
     *
     * @param Context
     * @param mHandler
     * @param message
     * @param runnable
     */
    public static void ConfirmMessageDialog(final Context Context, final Handler mHandler, final String message, final Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MessageAlertDialog m = new MessageAlertDialog(Context, "提示", message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.post(runnable);
                    }
                });
                m.showDiglogWithQAndQClick();
            }
        });
    }

    public static void ConfirmMessageDialog(final Context Context, final Handler mHandler, final String buttonText, final String message, final Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MessageAlertDialog m = new MessageAlertDialog(Context, "提示", message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.post(runnable);
                    }
                });
                m.showDiglogWithQAndQClick(buttonText);
            }
        });
    }

    /**
     * 操作信息提示框
     *
     * @param Context
     * @param mHandler
     * @param title
     * @param message
     * @param runnable
     */
    public static void FunctionMessageDialog(final Context Context, final Handler mHandler, final String title, final String message, final Runnable runnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MessageAlertDialog m = new MessageAlertDialog(Context, title, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHandler.post(runnable);
                    }
                });
                m.showDiglogWithQ();
            }
        });
    }

    /**
     * 显示提示 （有确认和取消按钮以及确认点击事件的）
     *
     * @param Context
     * @param mHandler
     * @param title
     * @param message
     * @param confirmRunnable
     * @param cancelRunnable
     */
    public static void FunctionMessage2Dialog(final Context Context, final Handler mHandler, final String title, final String message,
                                              final Runnable confirmRunnable, final Runnable cancelRunnable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MessageAlertDialog m = new MessageAlertDialog(Context, title, message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHandler.post(confirmRunnable);
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHandler.post(cancelRunnable);
                            }
                        });
                m.showDiglogQE();
            }
        });
    }
}
