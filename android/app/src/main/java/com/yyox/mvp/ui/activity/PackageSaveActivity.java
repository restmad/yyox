package com.yyox.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.kf5.sdk.system.image.ImageSelectorActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.Loading_view;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.Utils.PictureUtil;
import com.yyox.consts.CodeDefine;
import com.yyox.consts.OrderStatus;
import com.yyox.di.component.DaggerPackageComponent;
import com.yyox.di.module.PackageModule;
import com.yyox.mvp.contract.PackageContract;
import com.yyox.mvp.model.entity.Images;
import com.yyox.mvp.model.entity.OrderPackage;
import com.yyox.mvp.model.entity.PackageItem;
import com.yyox.mvp.model.entity.Warehouse;
import com.yyox.mvp.presenter.PackagePresenter;
import com.yyox.mvp.ui.adapter.PackageExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.OptionPicker;
import common.AppComponent;
import common.WEActivity;

public class PackageSaveActivity extends WEActivity<PackagePresenter> implements PackageContract.View {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public List<Warehouse> mWarehouses;
    public static int warehousID;
    private Drawable mErrorIcon;
    private RxPermissions mRxPermissions;
    private Context mContext;
    private Loading_view loading_view;
    private List<Images> mImagesList = new ArrayList<>();

    @Nullable
    @BindView(R.id.tv_order_reprot_warehouse)
    TextView mTextView_Warehouse;

    @Nullable
    @BindView(R.id.iv_address_save_address)
    ImageView save;

    @Nullable
    @BindView(R.id.tv_order_report_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.btn_order_report)
    Button mButton;

    @Nullable
    @BindView(R.id.tv_order_reprot_name)
    EditText mEditText_Name;

    @Nullable
    @BindView(R.id.tv_order_reprot_number)
    EditText mEditText_Number;

    @Nullable
    @BindView(R.id.ll_images)
    LinearLayout mllImages;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_order_report, null, false);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerPackageComponent.builder().appComponent(appComponent).packageModule(new PackageModule(this)).build().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }

    private void Loading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_view = new Loading_view(mContext, R.style.CustomDialog);
                if (loading_view != null) {
                    loading_view.show();
                }
            }
        });
    }

    private void loadingclose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading_view != null) {
                    loading_view.dismiss();
                }
            }
        });
    }

    /**
     * 数据有效性检查
     * @param type
     * @param value
     */
    private void check(String type, String value) {
        switch (type) {
            case "name": {
                if (!value.isEmpty()) {
                    if (value.length() > 100) {
                        mEditText_Name.setError("包裹昵称最多100位字符", mErrorIcon);
                    }
                }
            }
            break;
            case "number": {
                if (value.isEmpty()) {
                    mEditText_Number.setError("请填写物流号", mErrorIcon);
                } else if (!CommonUtils.isChinesepackage(value)) {
                    mEditText_Number.setError("请填写正确物流号", mErrorIcon);
                } else if (value.length() < 8 || value.length() > 200) {
                    mEditText_Number.setError("请填写8-200位物流号", mErrorIcon);
                }
            }
            break;
        }
    }

    private void initControl(){
        mContext = this;

        mErrorIcon = getResources().getDrawable(R.mipmap.error);
        mErrorIcon.setBounds(new Rect(0, 0, mErrorIcon.getIntrinsicWidth(), mErrorIcon.getIntrinsicHeight()));

        mEditText_Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = mEditText_Name.getText().toString();
                check("name", name);
            }
        });

        mEditText_Number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = mEditText_Number.getText().toString();
                check("number", number);
            }
        });

        mEditText_Number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String number = mEditText_Number.getText().toString();
                    check("number", number);
                } else {
                    String warehouse = mTextView_Warehouse.getText().toString();
                    if (warehouse.isEmpty()) {
                        save.setVisibility(View.VISIBLE);
                        showMessage("请选择仓库");
                    } else {
                        save.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private ImageView CreateImageView(Bitmap bitmap){
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mllImages.getWidth()/4, mllImages.getHeight());
        params.setMargins(0, 0, mllImages.getWidth()/16, 0);
        imageView.setLayoutParams(params);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Images> imagesTemp = new ArrayList<Images>();
                for(int i=0;i<mImagesList.size();i++){
                    String strPath = mImagesList.get(i).getPath();
                    String strType = mImagesList.get(i).getType();
                    Bitmap bBitmap = mImagesList.get(i).getBitmap();
                    imagesTemp.add(new Images(strPath,strType,bBitmap));
                }
                GlobalData.setImagesList(imagesTemp);
                Intent intent = new Intent(mContext, ImageShowActivity.class);
                intent.putExtra("delete", "NoDelete");
                startActivityForResult(intent, CodeDefine.IMAGE_SHOW_REQUEST);
            }
        });
        imageView.setImageBitmap(bitmap);
        return imageView;
    }

    @Override
    protected void initData() {

        initControl();

        //获取仓库数据
        mPresenter.getWarehouse();

        if (OrderStatus.ORDER_STATUS_PACKAGE_EDIT == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("预报订单");
            mButton.setText("保存修改");
            PackageItem packageItem = (PackageItem) getIntent().getSerializableExtra("PackageItem");
            mTextView_Warehouse.setText(packageItem.getWarehouseName());
            mEditText_Name.setText(packageItem.getNickname());
            mEditText_Number.setText(packageItem.getCarrierNo());
            int size = packageItem.getOrderSreenshot().size();
            if (size>0){
                Loading();
                if (packageItem.getOrderSreenshot().size() > 0) {
                    mPresenter.extractPicture(1, packageItem.getOrderSreenshot().get(0), size);
                }
                if (packageItem.getOrderSreenshot().size() > 1) {
                    mPresenter.extractPicture(2, packageItem.getOrderSreenshot().get(1), size);
                }
                if (packageItem.getOrderSreenshot().size() > 2) {
                    mPresenter.extractPicture(3, packageItem.getOrderSreenshot().get(2), size);
                }
            }
        } else if (OrderStatus.ORDER_STATUS_REPORT == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("预报订单");
            mButton.setText("提交预报");
        }

    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
    }

    @Override
    public void setPackageAdapter(PackageExpandableAdapter adapter) {
    }

    @Override
    public void startLoadMore() {
    }

    @Override
    public void endLoadMore() {
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void CheckGoodsDeclare(List<OrderPackage> orderPackages) {
    }

    @Override
    public void ListWarehouses(List<Warehouse> warehouses) {
        mWarehouses = warehouses;
    }

    @Override
    public void dataFew() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(this, message, 3 * 1000).show();
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setUIResult(int type) {
        if (type == 0) {
            showMessage("创建成功");
        } else if (type == 1) {
            showMessage("修改成功");
        }
        setResult(CodeDefine.PACKAGE_RESULT);
        mButton.setClickable(true);
        this.finish();
    }

    @Override
    public void packageImage(int status) {
        if(0 == status){
            loadingclose();

            String warehouse = mTextView_Warehouse.getText().toString();
            String name = mEditText_Name.getText().toString();
            String number = mEditText_Number.getText().toString();

            for (Warehouse warehouses : mWarehouses) {
                if (warehouses.getName().equals(warehouse)) {
                    warehousID = warehouses.getId();
                }
            }

            if (OrderStatus.ORDER_STATUS_PACKAGE_EDIT == getIntent().getIntExtra("type", 0)) {//修改
                PackageItem packageItem = (PackageItem) getIntent().getSerializableExtra("PackageItem");
                int id = packageItem.getId();
                mPresenter.commitForecast(number, warehousID, name, id);
            } else if (OrderStatus.ORDER_STATUS_REPORT == getIntent().getIntExtra("type", 0)) {//提交预报
                mPresenter.commitForecast(number, warehousID, name, 0);
            }
        }else{
            loadingclose();
        }
    }

    @Override
    public void setImage(int i, String image, int size,String url,int status) {
        if(0 == status){
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            mllImages.addView(CreateImageView(decodedByte));
            mImagesList.add(new Images(url,"net",decodedByte));
            if (mImagesList.size() == size){
                loadingclose();
            }
        }else{
            loadingclose();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PHOTO_REQUEST_GALLERY) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null) {
                for (int i = 0; i < pathList.size(); i++) {
                    String path = pathList.get(i);
                    if (!TextUtils.isEmpty(path)) {
                        Bitmap bitmap = PictureUtil.getBitmap(path);
                        mllImages.addView(CreateImageView(bitmap));
                        mImagesList.add(new Images(path,"local",bitmap));
                    }
                }
            }
        } else if (resultCode == CodeDefine.IMAGE_SHOW_RESULT && requestCode == CodeDefine.IMAGE_SHOW_REQUEST) {
            List<Images> images = GlobalData.getImagesList();
            if(images.size() != mImagesList.size()){
                if(0 == images.size()){
                    mllImages.removeAllViews();
                    mImagesList.clear();
                }else{
                    for(int i=mImagesList.size()-1;i>=0;i--){
                        boolean bExist = false;
                        for(int j=0;j<images.size();j++){
                            if(mImagesList.get(i).getPath().equals(images.get(j).getPath())){
                                bExist = true;
                                break;
                            }
                        }
                        if(!bExist){
                            mllImages.removeViewAt(i);
                            mImagesList.remove(i);
                        }
                    }
                }
            }
            GlobalData.setImagesList(new ArrayList<>());
        }
    }

    public void btn_back_click(View v) {
        setResult(CodeDefine.PACKAGE_RESULT);
        this.finish();
    }

    /**
     * 选择仓库点击事件
     * @param v
     */
    public void rl_order_report_warehouse_click(View v) {
        ArrayList<String> list = new ArrayList<>();
        for (Warehouse warehouse : mWarehouses) {
            String name = warehouse.getName();
            list.add(name);
        }
        OptionPicker picker = new OptionPicker(this, list);
        picker.setCycleDisable(true);
        picker.setLineVisible(false);
        //picker.setShadowVisible(true);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mTextView_Warehouse.setText(item);
                save.setVisibility(View.GONE);
            }
        });
        picker.show();
    }

    /**
     * 添加订单截图点击事件
     * @param v
     */
    public void rl_order_report_image_click(View v) {
        if (mImagesList != null && mImagesList.size() >= 3) {
            showMessage("最多可以上传三张图片");
        } else {
            Intent intent = new Intent(this, ImageSelectorActivity.class);
            intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, 3 - mImagesList.size());
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }
    }

    /**
     * 保存按钮点击事件
     * @param v
     */
    public void btn_order_report_save_click(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        mButton.setClickable(true);
        String warehouse = mTextView_Warehouse.getText().toString();
        String name = mEditText_Name.getText().toString();
        String number = mEditText_Number.getText().toString();

        if (warehouse.isEmpty()) {
            save.setVisibility(View.VISIBLE);
            showMessage("请选择仓库");
        } else if (number.isEmpty()) {
            mEditText_Number.setError("请输入物流号", mErrorIcon);
            showMessage("请输入物流号");
        } else if (!CommonUtils.isChinesepackage(number)) {
            mEditText_Number.setError("请填写正确物流号", mErrorIcon);
            showMessage("请填写正确物流号");
        } else if (number.length() < 8 || number.length() > 200) {
            mEditText_Number.setError("请填写8-200位物流号", mErrorIcon);
            showMessage("请填写8-200位物流号");
        } else {
            if (mImagesList.size() > 0) {
                Loading();

                Bitmap bitmap1 = null;
                Bitmap bitmap2 = null;
                Bitmap bitmap3 = null;
                if (mImagesList.size() == 1) {
                    bitmap1 = mImagesList.get(0).getBitmap();
                } else if (mImagesList.size() == 2) {
                    bitmap1 = mImagesList.get(0).getBitmap();
                    bitmap2 = mImagesList.get(1).getBitmap();
                } else if (mImagesList.size() == 3) {
                    bitmap1 = mImagesList.get(0).getBitmap();
                    bitmap2 = mImagesList.get(1).getBitmap();
                    bitmap3 = mImagesList.get(2).getBitmap();
                }
                mPresenter.uploadPackageImage(bitmap1, bitmap2, bitmap3);
            } else {
                mPresenter.uploadPackageImage(null, null, null);
            }
        }
    }

}
