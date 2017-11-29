package com.kf5.sdk.im.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kf5.sdk.R;
import com.kf5.sdk.im.adapter.listener.MessageAdapterItemClickListener;
import com.kf5.sdk.im.adapter.listener.MessageFileClickListener;
import com.kf5.sdk.im.adapter.listener.MessageFileLongClickListener;
import com.kf5.sdk.im.adapter.listener.MessageImageListener;
import com.kf5.sdk.im.adapter.listener.MessageImageLongListener;
import com.kf5.sdk.im.adapter.listener.MessageResendListener;
import com.kf5.sdk.im.adapter.listener.MessageTextLongListener;
import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5.sdk.im.db.IMSQLManager;
import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.im.utils.ImageUtils;
import com.kf5.sdk.system.internet.DownLoadManager;
import com.kf5.sdk.system.utils.ByteArrayUtil;
import com.kf5.sdk.system.utils.CustomTextView;
import com.kf5.sdk.system.utils.FilePath;
import com.kf5.sdk.system.utils.ImageLoaderManager;
import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * author:chosen
 * date:2017/3/22 11:16
 * email:812219713@qq.com
 */

abstract class AbstractHolder {

    protected Context context;

    public AbstractHolder(Context context) {
        this.context = context;
    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message) && context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 加载语音消息内容
     *
     * @param position
     * @param message
     * @param textViewLength
     * @param downLoadList
     * @param callBack
     */
    protected final void loadVoiceData(int position, IMMessage message, TextView textViewLength, ProgressBar progressBar, List<String> downLoadList, FileDownLoadCallBack callBack) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textViewLength.measure(w, h);
        int width = textViewLength.getMeasuredWidth();
        textViewLength.setOnClickListener(new MessageAdapterItemClickListener(message, position));
        Upload upload = message.getUpload();
        File localFile = new File(FilePath.SAVE_RECORDER + MD5Utils.GetMD5Code(upload.getUrl()) + ".amr");
        MediaPlayer mediaPlayer = null;
        if (localFile.exists()) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(localFile.getAbsolutePath()));
            if (mediaPlayer != null) {
                int length = mediaPlayer.getDuration() / 1000 + 1;
                //noinspection StringBufferReplaceableByString
                textViewLength.setText(new StringBuilder().append(length).append("''").toString());
                double maxWidth = Utils.getDisplayWidth(context) / 3 * 2;
                double percentWidth = (maxWidth - width) / 60;
                ViewGroup.LayoutParams layoutParams = textViewLength.getLayoutParams();
                layoutParams.width = (int) (width + percentWidth * length);
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                textViewLength.setLayoutParams(layoutParams);
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
            }
        } else {
            File file = new File(FilePath.SAVE_RECORDER + upload.getName());
            if (file.exists()) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(file.getAbsolutePath()));
                if (mediaPlayer != null) {
                    int length = mediaPlayer.getDuration() / 1000 + 1;
                    //noinspection StringBufferReplaceableByString
                    textViewLength.setText(new StringBuilder().append(length).append("''").toString());
                    double maxWidth = Utils.getDisplayWidth(context) / 3 * 2;
                    double percentWidth = (maxWidth - width) / 60;
                    ViewGroup.LayoutParams layoutParams = textViewLength.getLayoutParams();
                    layoutParams.width = (int) (width + percentWidth * length);
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    textViewLength.setLayoutParams(layoutParams);
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                }
            } else {
                if (!downLoadList.contains(upload.getName())) {
                    downLoadList.add(upload.getName());
                    DownLoadManager.getInstance().downloadFile(upload.getUrl(), FilePath.SAVE_RECORDER, upload.getName(), callBack);
                    if (progressBar != null)
                        progressBar.setVisibility(View.VISIBLE);
                }
            }
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


    /**
     * 加载文本消息
     *
     * @param message
     * @param contentText
     * @param position
     */
    protected final void loadTextData(IMMessage message, TextView contentText, int position) {
        CustomTextView.stripUnderlines(context, contentText, message.getMessage(), Linkify.ALL);
        contentText.setOnLongClickListener(new MessageTextLongListener(context, message, position));
    }

    /**
     * 加载机器人消息
     *
     * @param message
     * @param contentText
     * @param position
     */
    protected final void loadAIData(IMMessage message, TextView contentText, int position) {
        CustomTextView.setTextWithAIMessage(context, contentText, message.getMessage());
        contentText.setOnLongClickListener(new MessageTextLongListener(context, message, position));
    }

    /**
     * 记载附件消息
     *
     * @param message
     * @param tvFileName
     * @param progressBar
     * @param failLayout
     */
    protected final void loadFileData(IMMessage message, TextView tvFileName, ProgressBar progressBar, RelativeLayout failLayout) {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
        if (failLayout != null)
            failLayout.setBackgroundColor(Color.TRANSPARENT);
        if (tvFileName != null) {
            tvFileName.setText(Html.fromHtml("<a href=\"\">" + message.getUpload().getName() + "</a>"));
            tvFileName.setOnClickListener(new MessageFileClickListener(context, message));
            tvFileName.setOnLongClickListener(new MessageFileLongClickListener(context, message));
        }
    }

    protected final void loadCustomData(IMMessage message, TextView tvContent) {
        CustomTextView.setCustomMessage(tvContent, message.getMessage());
    }

    /**
     * 加载图片内容
     *
     * @param position
     * @param message
     * @param maskImage
     */
    protected final void loadImageData(final int position, final IMMessage message, ImageView maskImage) {

        final Upload upload = message.getUpload();
        maskImage.setOnClickListener(new MessageImageListener(context, message));
        maskImage.setOnLongClickListener(new MessageImageLongListener(context, message, position));
        final String url = upload.getUrl();
        String localPath = upload.getLocalPath();
        if (!TextUtils.isEmpty(localPath) && new File(localPath).exists()) {
            File file = new File(localPath);
            ImageUtils.setImageSize(file.getAbsolutePath(), maskImage, com.kf5.sdk.im.utils.Utils.getImageMaxEdge(context), com.kf5.sdk.im.utils.Utils.getImageMinEdge(context));
            loadImage(maskImage, "file://" + file.getAbsolutePath());
        } else if (!TextUtils.isEmpty(url) && url.startsWith("http") && new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType()).exists()) {
            File file = new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType());
            ImageUtils.setImageSize(file.getAbsolutePath(), maskImage, com.kf5.sdk.im.utils.Utils.getImageMaxEdge(context), com.kf5.sdk.im.utils.Utils.getImageMinEdge(context));
            loadImage(maskImage, "file://" + file.getAbsolutePath());
            IMSQLManager.updateLocalPathByTimeStamp(context, file.getAbsolutePath(), message.getTimeStamp());
        } else if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            ImageUtils.setNetImageSize(maskImage, upload.getWidth(), upload.getHeight());
            ImageLoaderManager.getInstance(context).displayImage(url, maskImage, new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }


                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    File file = new File(FilePath.IMAGES_PATH + MD5Utils.GetMD5Code(url) + "." + upload.getType());
                    ByteArrayUtil.cacheBitmapToSDCard(resource, upload.getType(), file);
                    upload.setLocalPath(file.getAbsolutePath());
                    IMSQLManager.updateLocalPathByTimeStamp(context, file.getAbsolutePath(), message.getTimeStamp());
                    return false;
                }
            });
        } else {
            loadImage(maskImage, R.drawable.kf5_image_loading_failed);
        }
    }


    protected final void loadImage(ImageView imageView, int sourceId) {
        ImageLoaderManager.getInstance(context).displayImage(sourceId, imageView);
    }

    protected final void loadImage(ImageView imageView, String path) {
        ImageLoaderManager.getInstance(context).displayImage(path, imageView);
    }


    /**
     * 处理消息的发送状态以及是否显示时间tip
     *
     * @param message         当前消息
     * @param previousMessage 前一条消息
     * @param position        当前item位置
     * @param tvDate          时间组件
     * @param progressBar     进度条组件
     * @param failLayout      状态容器
     */
    protected final void dealMessageStatus(IMMessage message, IMMessage previousMessage, int position,
                                           TextView tvDate, ProgressBar progressBar, RelativeLayout failLayout) {
        if (message.getStatus() == Status.SENDING) {
            progressBar.setVisibility(View.VISIBLE);
            failLayout.setBackgroundColor(Color.TRANSPARENT);
        } else if (message.getStatus() == Status.SUCCESS) {
            progressBar.setVisibility(View.GONE);
            failLayout.setBackgroundColor(Color.TRANSPARENT);
        } else if (message.getStatus() == Status.FAILED) {
            progressBar.setVisibility(View.GONE);
            failLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.kf5_message_send_failed_img_drawable));
            failLayout.setOnClickListener(new MessageResendListener(context, message));
        }
        dealDate(position, tvDate, message, previousMessage);
    }

    /**
     * 设置时间tip是否显示
     *
     * @param position        当前消息处于的位置
     * @param tvDate          时间组件
     * @param message         当前消息
     * @param previousMessage 前一条消息
     */
    protected final void dealDate(int position, TextView tvDate, IMMessage message, IMMessage previousMessage) {
        if (position == 0) {
            if (message.getCreated() < 1) {
                tvDate.setText(Utils.getAllTime(System.currentTimeMillis()));
            } else {
                tvDate.setText(Utils.getAllTime(message.getCreated()));
            }
            tvDate.setVisibility(View.VISIBLE);
        } else {
            if (previousMessage != null && (message.getCreated() - previousMessage.getCreated()) > 2 * 60) {
                tvDate.setText(Utils.getAllTime(message.getCreated()));
                tvDate.setVisibility(View.VISIBLE);
            } else {
                tvDate.setVisibility(View.GONE);
            }
        }
    }
}
