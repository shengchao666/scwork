package hsdx.com.wjcxapp.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hsdx.com.wjcxapp.wjcx.WjcxcdTwoActivity;


public class FileUpDownUtil {

    //请求相机
    public static final int REQUESTCODE_TAKE = 0x2410;
    //请求相册
    public static final int REQUESTCODE_PICK = 0x2403;

    /**
     * 打开文件
     */
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";

    public static void openFile(Activity activity, File result) {
        /* 取得扩展名 */
        String fileType = "";
        String end = result.getName().substring(result.getName().lastIndexOf(".") + 1, result.getName().length()).toLowerCase(Locale.getDefault());
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            fileType = DATA_TYPE_AUDIO;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            fileType = DATA_TYPE_VIDEO;
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            fileType = DATA_TYPE_IMAGE;
        } else if (end.equals("apk")) {
            fileType = DATA_TYPE_APK;
        } else if (end.equals("ppt")) {
            fileType = DATA_TYPE_PPT;
        } else if (end.equals("xls") || end.equals("xlsx")) {
            fileType = DATA_TYPE_EXCEL;
        } else if (end.equals("doc") || end.equals("docx")) {
            fileType = DATA_TYPE_WORD;
        } else if (end.equals("pdf")) {
            fileType = DATA_TYPE_PDF;
        } else if (end.equals("chm")) {
            fileType = DATA_TYPE_CHM;
        } else if (end.equals("txt")) {
            fileType = DATA_TYPE_TXT;
        } else {
            fileType = DATA_TYPE_ALL;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(activity, "hsdx.com.sgglapp.fileProvider", result);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, fileType);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(result), fileType);
        }
        activity.startActivity(intent);
    }

    /**
     * 下载并查看文件
     */
    public static void download(final Activity activity, String url) {
        final ProgressDialog progressdialog = new ProgressDialog(activity);
        progressdialog.setTitle(null);
        progressdialog.setMessage("加载中，请稍候...");
        progressdialog.setCancelable(false);//true 按BACK键可退出
        progressdialog.show();
        //String url = upFileIp+"PhoneAqxycx_downfile?uuid="+uuid;
        RequestParams params = new RequestParams(url);
        //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory() + "/hsdx/");
        //自动为文件命名
        params.setAutoRename(true);

        x.Ext.init(activity.getApplication()); // 如果在application 中可以直接写this
        x.Ext.setDebug(BuildConfig.DEBUG);

        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                //下载成功后关闭提示

                progressdialog.dismiss();
                openFile(activity, result);
                Log.e("下载文件","下载成功");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                progressdialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                progressdialog.dismiss();
            }

            @Override
            public void onFinished() {
                progressdialog.dismiss();
            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                progressdialog.dismiss();
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
            }

            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                Log.i("JAVA", "current：" + current + "，total：" + total);
            }
        });
    }

    //调用相机
    public static Uri takePhoto(Activity activity, Handler handler) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /***
         * 使用照相机拍照，拍照后的图片会存放在相册中
         * 使用的这种方式有一个好处就是获取的图片是拍照后的原图
         * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
         */
        ContentValues values = new ContentValues();
        Uri photoUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        File image = new File(appFolderCheckandCreate(activity, handler), "img" + getTimeStamp() + ".jpg");
//        Uri photoUri = Uri.fromFile(image);
       /* File imagePath = new File(Context., "images");
        File newFile = new File(imagePath, "test.jpg");
        FileProvider.getUriForFile(activity,"hsdx.com.sgglapp.fileProvider",newFile);*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        activity.startActivityForResult(intent, REQUESTCODE_TAKE);
        return photoUri;
    }

    public static String appFolderCheckandCreate(Activity activity, Handler handler) {
        String appFolderPath = "";
        File externalStorage = Environment.getExternalStorageDirectory();

        if (externalStorage.canWrite()) {
            appFolderPath = externalStorage.getAbsolutePath() + "/MyApp";
            File dir = new File(appFolderPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            ErrorToast.show(activity, handler, "存储媒体未找到或已满！");
        }
        return appFolderPath;
    }

    private static String getTimeStamp() {
        final long timestamp = new Date().getTime();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);

        final String timeString = new SimpleDateFormat("HH_mm_ss_SSS").format(cal.getTime());
        return timeString;
    }

    //获取文件路径
    public String getFilePath(Uri uri, Activity activity) {
        String path;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
            path = getPath(activity, uri);
        } else {//4.4以下下系统调用方法
            path = getRealPathFromURI(activity, uri);
        }
        return path;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getRealPathFromURI(Activity activity, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    //验证文件是否为图片
    public static boolean isImg(String filePath) {
        Pattern pattern = Pattern.compile(".+(.png|.jpeg|.gif|.jpg|.bmp)$");
        Matcher matcher = pattern.matcher(filePath.toLowerCase());
        return matcher.find();
    }

    /*//创建照片的路径
    public static String createPhotoPath(Context context) {
        String path = getPath("angcyo/Photo");
        return path + "/" + UUID.randomUUID();
    }

    //获取SD卡下的一个安全路径
    public static String getPath(String name) {
        File file = new File(getSDPath() + "/" + name);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return file.getAbsolutePath();
    }

    //获取SD卡的路径
    public static String getSDPath() {
        return isExternalStorageWritable() ? Environment
                .getExternalStorageDirectory().getPath() : Environment
                .getDownloadCacheDirectory().getPath();
    }*/
}
