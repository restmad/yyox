package com.kf5.sdk.myTost;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
//自定义toast

/**
 * @author XianDongZhi[T-EXCEPTION]
 */
public class MyToast {

	private Context mContext;
	private WindowManager wm;
	private int mDuration;
	private View mNextView;
	public static final int LENGTH_SHORT = 1500;
	public static final int LENGTH_LONG = 3000;
	public static final int LENGTH_MAX = Integer.MAX_VALUE;
	private static AlertDialog dialog;

	public MyToast(Context context) {
		mContext = context.getApplicationContext();
	}

	/**
	 * @param context
	 * @param text
	 * @param time
	 * @return
	 */
	@SuppressLint("ShowToast")
	public static MyToast makeText(Context context, CharSequence text,
								   int time) {
		MyToast result = new MyToast(context);
		result.mNextView = Toast.makeText(context, text, time).getView();
		result.mDuration = time;
		return result;
	}

	/**
	 * @param context
	 * @param text
	 * @param time
	 * @return
	 */
	public static MyToast makeText(Context context, int resId, int time)
			throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId),
				time);
	}

	public void show() {
		if (mNextView != null) {
			wm = (WindowManager) mContext
					.getSystemService(Context.WINDOW_SERVICE);
			WindowManager.LayoutParams params = new WindowManager.LayoutParams();
			params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.format = PixelFormat.TRANSLUCENT;
			params.y = dip2px(mContext, 64);
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			wm.addView(mNextView, params);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					if (mNextView != null) {
						wm.removeView(mNextView);
						mNextView = null;
						wm = null;
					}
				}
			}, mDuration);
		}
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	public static boolean checkPermission(Context context) {
		//6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
		if (Build.VERSION.SDK_INT < 23) {
			if (RomUtils.checkIsMiuiRom()) {
				return miuiPermissionCheck(context);
			} else if (RomUtils.checkIsMeizuRom()) {
				return meizuPermissionCheck(context);
			} else if (RomUtils.checkIsHuaweiRom()) {
				return huaweiPermissionCheck(context);
			} else if (RomUtils.checkIs360Rom()) {
				return qikuPermissionCheck(context);
			}
		}
		return commonROMPermissionCheck(context);
	}
	public static boolean huaweiPermissionCheck(Context context) {
		return HuaweiUtils.checkFloatWindowPermission(context);
	}

	private static boolean miuiPermissionCheck(Context context) {
		return MiuiUtils.checkFloatWindowPermission(context);
	}

	private static boolean meizuPermissionCheck(Context context) {
		return MeizuUtils.checkFloatWindowPermission(context);
	}

	private static boolean qikuPermissionCheck(Context context) {
		return QikuUtils.checkFloatWindowPermission(context);
	}

	private static boolean commonROMPermissionCheck(Context context) {
		//最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
		if (RomUtils.checkIsMeizuRom()) {
			return meizuPermissionCheck(context);
		} else {
			Boolean result = true;
			if (Build.VERSION.SDK_INT >= 23) {
				try {
					Class clazz = Settings.class;
					Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
					result = (Boolean) canDrawOverlays.invoke(null, context);
				} catch (Exception e) {
//					Log.e(TAG, Log.getStackTraceString(e));
				}
			}
			return result;
		}
	}
	public static void applyPermission(Context context) {
		if (Build.VERSION.SDK_INT < 23) {
			if (RomUtils.checkIsMiuiRom()) {
				miuiROMPermissionApply(context);
			} else if (RomUtils.checkIsMeizuRom()) {
				meizuROMPermissionApply(context);
			} else if (RomUtils.checkIsHuaweiRom()) {
				huaweiROMPermissionApply(context);
			} else if (RomUtils.checkIs360Rom()) {
				ROM360PermissionApply(context);
			}
		}
		commonROMPermissionApply(context);
	}
	private interface OnConfirmResult {
		void confirmResult(boolean confirm);
	}
	private static void showConfirmDialog(Context context, OnConfirmResult result) {
		showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
	}
	private static void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

		dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
				.setMessage(message)
				.setPositiveButton("现在去开启",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.confirmResult(true);
								dialog.dismiss();
							}
						}).setNegativeButton("暂不开启",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								result.confirmResult(false);
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}
	/**
	 * 通用 rom 权限申请
	 */
	private static void commonROMPermissionApply(final Context context) {
		//这里也一样，魅族系统需要单独适配
		if (RomUtils.checkIsMeizuRom()) {
			meizuROMPermissionApply(context);
		} else {
			if (Build.VERSION.SDK_INT >= 23) {
				showConfirmDialog(context, new OnConfirmResult() {
					@Override
					public void confirmResult(boolean confirm) {
						if (confirm) {
							try {
								Class clazz = Settings.class;
								Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

								Intent intent = new Intent(field.get(null).toString());
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setData(Uri.parse("package:" + context.getPackageName()));
								context.startActivity(intent);
							} catch (Exception e) {
//								Log.e(TAG, Log.getStackTraceString(e));
							}
						} else {
//							Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
							//需要做统计效果
						}
					}
				});
			}
		}
	}

	private static void miuiROMPermissionApply(final Context context) {
		showConfirmDialog(context, new OnConfirmResult() {
			@Override
			public void confirmResult(boolean confirm) {
				if (confirm) {
					MiuiUtils.applyMiuiPermission(context);
				} else {
//					Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
				}
			}
		});
	}
	private static void meizuROMPermissionApply(final Context context) {
		showConfirmDialog(context, new OnConfirmResult() {
			@Override
			public void confirmResult(boolean confirm) {
				if (confirm) {
					MeizuUtils.applyPermission(context);
				} else {
//					Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
				}
			}
		});
	}
	private static void huaweiROMPermissionApply(final Context context) {
		showConfirmDialog(context, new OnConfirmResult() {
			@Override
			public void confirmResult(boolean confirm) {
				if (confirm) {
					HuaweiUtils.applyPermission(context);
				} else {
//					Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
				}
			}
		});
	}
	private static void ROM360PermissionApply(final Context context) {
		showConfirmDialog(context, new OnConfirmResult() {
			@Override
			public void confirmResult(boolean confirm) {
				if (confirm) {
					QikuUtils.applyPermission(context);
				} else {
//					Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
				}
			}
		});
	}
}
