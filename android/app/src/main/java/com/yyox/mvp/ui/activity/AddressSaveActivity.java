package com.yyox.mvp.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.utils.UiUtils;
import com.kf5.sdk.system.image.ImageSelectorActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yyox.BuildConfig;
import com.yyox.R;
import com.yyox.Utils.CommonUtils;
import com.yyox.Utils.IdCardUtil;
import com.yyox.Utils.MyToast;
import com.yyox.Utils.NoDoubleClickUtils;
import com.yyox.Utils.PictureUtil;
import com.yyox.consts.AddressType;
import com.yyox.consts.CodeDefine;
import com.yyox.di.component.DaggerAddressComponent;
import com.yyox.di.module.AddressModule;
import com.yyox.mvp.contract.AddressContract;
import com.yyox.mvp.model.entity.Address;
import com.yyox.mvp.model.entity.Card;
import com.yyox.mvp.model.entity.Region;
import com.yyox.mvp.presenter.AddressPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.OptionPicker;
import common.AppComponent;
import common.WEActivity;


public class AddressSaveActivity extends WEActivity<AddressPresenter> implements AddressContract.View {

    @Nullable
    @BindView(R.id.tv_address_save_title)
    TextView mTextView_Title;

    @Nullable
    @BindView(R.id.btn_address_save_edit)
    Button mButton_Edit;

    @Nullable
    @BindView(R.id.et_address_save_name)
    EditText mEditText_Name;

    @Nullable
    @BindView(R.id.et_address_save_phone)
    EditText mEditText_Phone;

    @Nullable
    @BindView(R.id.iv_address_save_address)
    ImageView mImageView_Address;

    @Nullable
    @BindView(R.id.btn_address_save_address)
    Button mButton_Address;

    @Nullable
    @BindView(R.id.et_address_save_address)
    EditText mEditText_Address;

    @Nullable
    @BindView(R.id.et_address_save_zipcode)
    EditText mEditText_ZipCode;

    @Nullable
    @BindView(R.id.ll_address_save_idcard_explain)
    LinearLayout mLinearLayout_IdCard_Explain;

    @Nullable
    @BindView(R.id.iv_address_save_idcarda)
    ImageView mImageView_IdCarda;

    @Nullable
    @BindView(R.id.tv_address_save_idcarda_explain)
    TextView mTextView_IdCarda_Explain;

    @Nullable
    @BindView(R.id.iv_address_save_idcardb)
    ImageView mImageView_IdCardb;

    @Nullable
    @BindView(R.id.tv_address_save_idcardb_explain)
    TextView mTextView_IdCardb_Explain;

    @Nullable
    @BindView(R.id.et_address_save_idcard)
    EditText mEditText_IdCard;

    @Nullable
    @BindView(R.id.cb_address_save_default)
    CheckBox mCheckBox_Default;

    @Nullable
    @BindView(R.id.rl_address_save_save)
    RelativeLayout mRelativeLayout_Save;

    @Nullable
    @BindView(R.id.tv_address)
    TextView mTv_address;

    private int mProvinceId = 0;
    private int mCityId = 0;

    private String mIdCardAPath = "";
    private String mIdCardBPath = "";
    //private String mIdCardAPathTemp = "";
    //private String mIdCardBPathTemp = "";
    private Bitmap mIdCardABitmapTemp = null;
    private Bitmap mIdCardBBitmapTemp = null;
    private String mProvince = "";
    private String mCity = "";
    private String mDistrict = "";
    private boolean mHasDistrict = true;
    private boolean mUploading = false;
    private boolean  IdCardImage= false;//是否已上传
    private boolean mEdit = false;

    private Drawable mErrorIcon;
    private Address mAddress;
    private RxPermissions mRxPermissions;

    private boolean checkIdCard(String idcard,String idcarda,String idcardb){
        if(idcarda.length() > 0 && idcardb.length() > 0){
            return true;
        }else if(idcarda.length() == 0 && idcardb.length() == 0){
            return true;
        }else{
            return false;
        }
    }

    public void btn_address_save_click(View v){
        mPresenter.requestUploadIdcard(mIdCardABitmapTemp,mIdCardBBitmapTemp);
    }

    private void check(String type,String value){
        switch (type){
            case "name": {
                if (value.isEmpty()) {
                    mEditText_Name.setError("请输入姓名", mErrorIcon);
                } else if (!CommonUtils.isChinese(value)) {
                    mEditText_Name.setError("请输入2~10个汉字", mErrorIcon);
                }
            }
            break;
            case "phone":{
                if(value.isEmpty()){
                    mEditText_Phone.setError("请输入手机号码",mErrorIcon);
                }else if(!CommonUtils.isPhoneNumber(value)){
                    mEditText_Phone.setError("请输入正确的手机号码",mErrorIcon);
                }
            }
            break;
            case "address":{
                if(value.isEmpty()){
                    mEditText_Address.setError("请输入详细地址",mErrorIcon);
                }
            }
            break;
            case "zipcode":{
                if(value.isEmpty()){
                    mEditText_ZipCode.setError("请输入邮编",mErrorIcon);
                }else if(!CommonUtils.isZipCode(value)){
                    mEditText_ZipCode.setError("请输入正确的邮编",mErrorIcon);
                }
            }
            break;
            case "idcard":{
                if(value.isEmpty()){
                    mEditText_IdCard.setError("请输入身份证号码",mErrorIcon);
                }else if(value.length() != 15 && value.length() != 18){
                    mEditText_IdCard.setError("身份证号码位数不正确",mErrorIcon);
                }else if(new IdCardUtil(value).isCorrect() != 0){
                    mEditText_IdCard.setError("身份证号格式不正确",mErrorIcon);
                }
            }
            break;
        }
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_address_save,null,false);
    }

    @Override
    protected void initData() {

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
        mEditText_Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String name = mEditText_Name.getText().toString();
                    check("name", name);
                }
            }
        });

        mEditText_Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = mEditText_Phone.getText().toString();
                check("phone", phone);
            }
        });
        mEditText_Phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String phone = mEditText_Phone.getText().toString();
                    check("phone", phone);
                }
            }
        });

        mEditText_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String address = mEditText_Address.getText().toString();
                check("address", address);
            }
        });
        mEditText_Address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String address = mEditText_Address.getText().toString();
                    check("address", address);
                } else {
                    if (mProvince.isEmpty()) {
                        showMessage("请选择省");
                        mImageView_Address.setVisibility(View.VISIBLE);
                    } else if (mCity.isEmpty()) {
                        showMessage("请选择市");
                        mImageView_Address.setVisibility(View.VISIBLE);
                    } else if (mDistrict.isEmpty() && mHasDistrict) {
                        showMessage("请选择区");
                        mImageView_Address.setVisibility(View.VISIBLE);
                    } else {
                        mImageView_Address.setVisibility(View.GONE);
                    }
                }
            }
        });

        mEditText_ZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String zipcode = mEditText_ZipCode.getText().toString();
                check("zipcode", zipcode);
            }
        });
        mEditText_ZipCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String zipcode = mEditText_ZipCode.getText().toString();
                    check("zipcode", zipcode);
                }
            }
        });

        mEditText_IdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idcard = mEditText_IdCard.getText().toString();
                check("idcard", idcard);
            }
        });
        mEditText_IdCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String idcard = mEditText_IdCard.getText().toString();
                    check("idcard", idcard);
                }
            }
        });

        if (AddressType.ADDRESS_TYPE_ADD == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("新增收货地址");
            mButton_Edit.setVisibility(View.GONE);
        } else if (AddressType.ADDRESS_TYPE_EDIT == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("编辑收货地址");
            mButton_Edit.setVisibility(View.GONE);

            mAddress = (Address) getIntent().getSerializableExtra("address");
            if(mAddress.getCustomerCard() == null){
                mAddress.setCustomerCard(new Card());
            }
            mEditText_Name.setText(mAddress.getName());
            mEditText_Phone.setText(mAddress.getMobile());
            mProvince = mAddress.getProvince();
            mCity = mAddress.getCity();
            mDistrict = mAddress.getDistrict();
            mTv_address.setText(mAddress.getProvince() + mAddress.getCity() + mAddress.getDistrict());
            mEditText_Address.setText(mAddress.getDetailaddress());
            mEditText_ZipCode.setText(mAddress.getZipcode());
            mEditText_IdCard.setText(mAddress.getCustomerCard().getCardNo());
            mCheckBox_Default.setChecked(mAddress.getIsdefault());
            if (mAddress.getIsdefault()){
                mCheckBox_Default.setEnabled(false);
            }else {
                mCheckBox_Default.setEnabled(true);

                mCheckBox_Default.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (mCheckBox_Default.isChecked()) {
                            mAddress.setIsdefault(true);
                        } else {
                            mAddress.setIsdefault(false);
                        }
                    }
                });
            }

            if (!mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty()) {

                mPresenter.requestAddressImage(1,mAddress.getCustomerCard().getCardImageFrontUrl());

            }
            if (!mAddress.getCustomerCard().getCardImageBackUrl().isEmpty()) {

                mPresenter.requestAddressImage(2,mAddress.getCustomerCard().getCardImageBackUrl());
            }

        } else if (AddressType.ADDRESS_TYPE_DETAIL == getIntent().getIntExtra("type", 0)) {
            mTextView_Title.setText("地址详情");
            mButton_Edit.setVisibility(View.VISIBLE);
            mAddress = (Address) getIntent().getSerializableExtra("address");
            if (mAddress.getCustomerCard()== null){
                mAddress.setCustomerCard(new Card());
            }
            mEditText_Name.setText(mAddress.getName());
            mEditText_Name.setEnabled(false);
            mEditText_Phone.setText(mAddress.getMobile());
            mEditText_Phone.setEnabled(false);
            mProvince = mAddress.getProvince();
            mCity = mAddress.getCity();
            mDistrict = mAddress.getDistrict();
            mTv_address.setText(mAddress.getProvince() + mAddress.getCity() + mAddress.getDistrict());
            mButton_Address.setEnabled(false);
            mButton_Address.setText("");
            mEditText_Address.setText(mAddress.getDetailaddress());
            mEditText_Address.setEnabled(false);
            mEditText_ZipCode.setText(mAddress.getZipcode());
            mEditText_ZipCode.setEnabled(false);
            mEditText_IdCard.setText(mAddress.getCustomerCard().getCardNo());
            mEditText_IdCard.setEnabled(false);

            mLinearLayout_IdCard_Explain.setVisibility(View.GONE);
            mTextView_IdCarda_Explain.setText("身份证正面照");
            mTextView_IdCardb_Explain.setText("身份证背面照");
            mCheckBox_Default.setText("设为默认地址");
            mCheckBox_Default.setEnabled(false);
            mCheckBox_Default.setChecked(mAddress.getIsdefault());
            mRelativeLayout_Save.setVisibility(View.GONE);

            if (!mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty()) {

                mPresenter.requestAddressImage(1,mAddress.getCustomerCard().getCardImageFrontUrl());
            }
            if (!mAddress.getCustomerCard().getCardImageBackUrl().isEmpty()) {

                mPresenter.requestAddressImage(2,mAddress.getCustomerCard().getCardImageBackUrl());
            }
        } else if (AddressType.ADDRESS_TYPE_UPLOAD == getIntent().getIntExtra("type", 0)) {
            int iAddressId = getIntent().getIntExtra("addressid", 0);
            mPresenter.requestAddressDetail(iAddressId);
        } else if (AddressType.ADDRESS_TYPE_DEIT_PAYS == getIntent().getIntExtra("type", 0)) {
            int iAddressId = getIntent().getIntExtra("addressid", 0);
            mPresenter.requestAddressDetail(iAddressId);
        }
    }
    public void btn_back_click(View v){
        if (AddressType.ADDRESS_TYPE_DEIT_PAYS == getIntent().getIntExtra("type",0)){
            setResult(CodeDefine.ADDRESS_ORDER_RESULT);
            finish();
        }else {
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (AddressType.ADDRESS_TYPE_DEIT_PAYS == getIntent().getIntExtra("type", 0)) {
                setResult(CodeDefine.ADDRESS_ORDER_RESULT);
                finish();
            } else {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void btn_address_edit_click(View v){
        if(AddressType.ADDRESS_TYPE_DETAIL == getIntent().getIntExtra("type",0)){
            mTextView_Title.setText("编辑收货地址");
            mButton_Edit.setVisibility(View.GONE);
           mAddress = (Address) getIntent().getSerializableExtra("address");
            if (mAddress.getCustomerCard() == null){
                mAddress.setCustomerCard(new Card());
            }
            mEditText_Name.setText(mAddress.getName());
            mEditText_Name.setEnabled(true);
            mEditText_Phone.setText(mAddress.getMobile());
            mEditText_Phone.setEnabled(true);
            mProvince = mAddress.getProvince();
            mCity = mAddress.getCity();
            mDistrict = mAddress.getDistrict();
            mTv_address.setText(mAddress.getProvince()+mAddress.getCity()+mAddress.getDistrict());
            mButton_Address.setEnabled(true);
            mButton_Address.setText("请选择");
            mEditText_Address.setText(mAddress.getDetailaddress());
            mEditText_Address.setEnabled(true);
            mEditText_ZipCode.setText(mAddress.getZipcode());
            mEditText_ZipCode.setEnabled(true);
            mEditText_IdCard.setText(mAddress.getCustomerCard().getCardNo());
            mEditText_IdCard.setEnabled(true);

            mLinearLayout_IdCard_Explain.setVisibility(View.VISIBLE);
            mTextView_IdCarda_Explain.setText("点击上传身份证正面照");
            mTextView_IdCardb_Explain.setText("点击上传身份证背面照");
            mCheckBox_Default.setText("设为默认地址");
            mCheckBox_Default.setChecked(mAddress.getIsdefault());
            if (mAddress.getIsdefault()){
                mCheckBox_Default.setEnabled(false);
            }else {
                mCheckBox_Default.setEnabled(true);
            }
            mRelativeLayout_Save.setVisibility(View.VISIBLE);

            if(!mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty()){

                mPresenter.requestAddressImage(1,mAddress.getCustomerCard().getCardImageFrontUrl());
            }
            if(!mAddress.getCustomerCard().getCardImageBackUrl().isEmpty()){

                mPresenter.requestAddressImage(2,mAddress.getCustomerCard().getCardImageBackUrl());
            }

            mEdit = true;
        }
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
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
    public void setRegions(int type, List<Region> regionList) {
        if(2 == type && regionList.size() == 0){
            mHasDistrict = false;
            mImageView_Address.setVisibility(View.GONE);
            return;
        }
        List<String> list = new ArrayList<String>();
        for(int i=0; i<regionList.size(); i++){
            list.add(regionList.get(i).getRegionname());
        }
        OptionPicker picker = new OptionPicker(this, list);
        picker.setCycleDisable(false);
        picker.setLineVisible(false);
        //picker.setShadowVisible(true);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                if(0 == type) {
                    mProvince = item;
                    mProvinceId = regionList.get(index).getId();
                    mPresenter.requestRegions(1,mProvinceId);
                    mTv_address.setText(mProvince);
                }else if(1 == type){
                    mCity = item;
                    mCityId = regionList.get(index).getId();
                    mPresenter.requestRegions(2, mCityId);
                    mTv_address.setText(mProvince+mCity);
                }else if(2 == type){
                    mDistrict = item;
                    mTv_address.setText(mProvince+mCity+mDistrict);
                    mImageView_Address.setVisibility(View.GONE);
                }
            }
        });
        picker.show();

    }

    @Override
    public void deleteTempPicture() {
        mUploading = false;
        IdCardImage = true;
        //PictureUtil.deleteTempFile(mIdCardAPathTemp);
        //PictureUtil.deleteTempFile(mIdCardBPathTemp);
        conserve();
    }
    //保存
    private void conserve() {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        int id = 0;
        String name = mEditText_Name.getText().toString();
        String phone = mEditText_Phone.getText().toString();
        String province = mProvince;
        String city = mCity;
        String district = mDistrict;
        String address = mEditText_Address.getText().toString();
        String zipcode = mEditText_ZipCode.getText().toString();
        String idcard = mEditText_IdCard.getText().toString();
        boolean isdefault = mCheckBox_Default.isChecked();

        if(name.isEmpty()){
            mEditText_Name.setError("请输入姓名",mErrorIcon);
            showMessage("请输入姓名");
        }else if(!CommonUtils.isChinese(name)){
            showMessage("请输入2~10个汉字");
            mEditText_Name.setError("请输入2~10个汉字",mErrorIcon);
        }else if(phone.isEmpty()){
            showMessage("请输入手机号码");
            mEditText_Phone.setError("请输入手机号码",mErrorIcon);
        }else if(!CommonUtils.isPhoneNumber(phone)){
            showMessage("请输入正确的手机号码");
            mEditText_Phone.setError("请输入正确的手机号码",mErrorIcon);
        }else if(province.isEmpty()){
            showMessage("请选择省");
        }else if(city.isEmpty()){
            showMessage("请选择市");
        }else if(district.isEmpty() && mHasDistrict){
            showMessage("请选择区");
        }else if(address.isEmpty()){
            showMessage("请输入详细地址");
        }else if(zipcode.isEmpty()){
            showMessage("请输入邮编");
        }else if(!CommonUtils.isZipCode(zipcode)){
            showMessage("请输入正确的邮编");
        }else if(idcard.isEmpty()){
            showMessage("请输入身份证号码");
        }else if(idcard.length() > 0 && idcard.length() != 15 && idcard.length() != 18){
            showMessage("请输入正确的身份证号码");
        }else if(idcard.length() > 0 && new IdCardUtil(idcard).isCorrect() != 0){
            showMessage("身份证号不正确");
        }else if(!checkIdCard(idcard,mIdCardAPath,mIdCardBPath) && (mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty() || mAddress.getCustomerCard().getCardImageBackUrl().isEmpty())){
            showMessage("请完善身份证信息");
        }else if(mUploading == true){
            showMessage("身份证正在上传,请稍后...");
        }else{
            if (BuildConfig.LOG_DEBUG) {
            }else{
                if(AddressType.ADDRESS_TYPE_ADD == getIntent().getIntExtra("type",0)){
                    id = 0;
                }else if(AddressType.ADDRESS_TYPE_EDIT == getIntent().getIntExtra("type",0)){
                    Address objAddress = (Address) getIntent().getSerializableExtra("address");
                    id = objAddress.getId();
                }else if(AddressType.ADDRESS_TYPE_DETAIL == getIntent().getIntExtra("type",0)){
                    Address objAddress = (Address) getIntent().getSerializableExtra("address");
                    id = objAddress.getId();
                }else if(AddressType.ADDRESS_TYPE_UPLOAD == getIntent().getIntExtra("type",0) || AddressType.ADDRESS_TYPE_DEIT_PAYS == getIntent().getIntExtra("type",0)){
                    id = getIntent().getIntExtra("addressid",0);
                }

                Address objAddress = new Address();
                objAddress.setId(id);
                objAddress.setName(name);
                objAddress.setMobile(phone);
                objAddress.setCountry("中国");
                objAddress.setProvince(province);
                objAddress.setCity(city);
                objAddress.setDistrict(district);
                objAddress.setDetailaddress(address);
                objAddress.setZipcode(zipcode);
                objAddress.setIdcard(idcard);
                objAddress.setIsdefault(isdefault);
                if (AddressType.ADDRESS_TYPE_UPLOAD == getIntent().getIntExtra("type",0) || AddressType.ADDRESS_TYPE_DEIT_PAYS == getIntent().getIntExtra("type",0)){
                    if (IdCardImage){
                        mPresenter.requestAddressSave(objAddress);
                    }else {
                        showMessage("还未上传身份证");
                    }
                }else {
                    mPresenter.requestAddressSave(objAddress);
                }
            }
        }
    }

    @Override
    public void refreshAddress() {
        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
        setResult(CodeDefine.ADDRESS_SAVE_RESULT);
        finish();
    }

    @Override
    public void setAddressDetail(Address address) {
        if(address != null){
            mAddress = address;
            if (mAddress.getCustomerCard()== null){
                mAddress.setCustomerCard(new Card());
            }
            mTextView_Title.setText("上传身份证");
            mButton_Edit.setVisibility(View.GONE);
            mEditText_Name.setText(address.getName());
            mEditText_Name.setEnabled(false);
            mEditText_Phone.setText(address.getMobile());
            mEditText_Phone.setEnabled(false);
            mProvince = address.getProvince();
            mCity = address.getCity();
            mDistrict = address.getDistrict();
            mTv_address.setText(address.getProvince()+address.getCity()+address.getDistrict());
            mButton_Address.setEnabled(false);
            mButton_Address.setText("");
            mEditText_Address.setText(address.getDetailaddress());
            mEditText_Address.setEnabled(false);
            mEditText_ZipCode.setText(address.getZipcode());
            mEditText_ZipCode.setEnabled(false);
            mEditText_IdCard.setText(address.getCustomerCard().getCardNo());
            mEditText_IdCard.setEnabled(true);

            mLinearLayout_IdCard_Explain.setVisibility(View.VISIBLE);
            mTextView_IdCarda_Explain.setText("身份证正面照");
            mTextView_IdCardb_Explain.setText("身份证背面照");
           /* mCheckBox_Default.setText("默认收货地址");
            mCheckBox_Default.setEnabled(false);*/
            mCheckBox_Default.setVisibility(View.GONE);
            mCheckBox_Default.setChecked(address.getIsdefault());
            mRelativeLayout_Save.setVisibility(View.VISIBLE);

            if(!address.getCustomerCard().getCardImageFrontUrl().isEmpty()){

                mPresenter.requestAddressImage(1,mAddress.getCustomerCard().getCardImageFrontUrl());
            }

            if(!address.getCustomerCard().getCardImageBackUrl().isEmpty()){

                mPresenter.requestAddressImage(2,mAddress.getCustomerCard().getCardImageBackUrl());
            }
        }
    }

    /**
     * 显示网络图片
     * @param i
     * @param data
     */
    @Override
    public void setImage(int i, String image) {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (i == 1){
            mImageView_IdCarda.setImageBitmap(decodedByte);
        }else if (i == 2){
            mImageView_IdCardb.setImageBitmap(decodedByte);
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(String message) {
        MyToast.makeText(this, message, 3*1000).show();
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
    protected void setupActivityComponent(AppComponent appComponent) {
        this.mRxPermissions = new RxPermissions(this);
        DaggerAddressComponent.builder().appComponent(appComponent).addressModule(new AddressModule(this)).build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CodeDefine.IDCARDA_REQUEST) {

            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null) {
                for (int i = 0; i < pathList.size(); i++) {
                    String path = pathList.get(i);
                    if (!TextUtils.isEmpty(path)) {
                        mIdCardAPath = path ;
                        mIdCardABitmapTemp = PictureUtil.getBitmap(path);
                        mImageView_IdCarda.setImageBitmap(mIdCardABitmapTemp);
                        /*
                        File file = new File(path);
                        if (file.exists()) {
                            mIdCardAPath = path ;
                            String adddressPath = PictureUtil.getAlbumDir().getPath();
                            mIdCardAPathTemp = adddressPath + "/IdCarda.png";
                            PictureUtil.saveTempPicture(mIdCardAPath, mIdCardAPathTemp);
                            mImageView_IdCarda.setImageBitmap(PictureUtil.getSmallBitmap(path, 120, 120));
                        }
                        */
                    }
                }
            }

        }else if (resultCode == RESULT_OK && requestCode == CodeDefine.IDCARDB_REQUEST) {

            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null) {
                for (int i = 0; i < pathList.size(); i++) {
                    String path = pathList.get(i);
                    if (!TextUtils.isEmpty(path)) {
                        mIdCardBPath = path ;
                        mIdCardBBitmapTemp = PictureUtil.getBitmap(path);
                        mImageView_IdCardb.setImageBitmap(mIdCardBBitmapTemp);
                        /*
                        File file = new File(path);
                        if (file.exists()) {
                            mIdCardBPath = path ;
                            String adddressPath = PictureUtil.getAlbumDir().getPath();
                            mIdCardBPathTemp = adddressPath + "/IdCardb.png";
                            PictureUtil.saveTempPicture(mIdCardBPath, mIdCardBPathTemp);
                            mImageView_IdCardb.setImageBitmap(PictureUtil.getSmallBitmap(mIdCardBPathTemp, 120, 120));
                        }
                        */
                    }
                }
            }

        }

        if (mAddress == null){
            mAddress = new Address();
        }
        if(mAddress.getCustomerCard() == null){
            mAddress.setCustomerCard(new Card());
        }
        if(!mIdCardAPath.isEmpty() && !mIdCardBPath.isEmpty() && mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty() && mAddress.getCustomerCard().getCardImageBackUrl().isEmpty()){
            mUploading = true;
        }else{
            if(mIdCardAPath.isEmpty() &&  mAddress.getCustomerCard() != null && mAddress.getCustomerCard().getCardImageFrontUrl().isEmpty()){
                showMessage("请上传身份证正面");
            }else if(mIdCardBPath.isEmpty() && mAddress.getCustomerCard() != null && mAddress.getCustomerCard().getCardImageBackUrl().isEmpty()){
                showMessage("请上传身份证反面");
            }else {
            }
        }
    }

    /**
     *
     * android4.4以后返回的URI只有图片编号
     * 获取图片真实路径
     *
     * @param contentURI
     * @return String
     */
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = null;
        String result;
        cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void btn_address_save_address(View v){
        mHasDistrict = true;
        mPresenter.requestRegions(0,0);
    }

    public void ll_address_save_idcarda_click(View v){
        if(AddressType.ADDRESS_TYPE_DETAIL != getIntent().getIntExtra("type",0) || mEdit){

            Intent intent = new Intent(this, ImageSelectorActivity.class);
            intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
            startActivityForResult(intent,  CodeDefine.IDCARDA_REQUEST);
        }
    }

    public void ll_address_save_idcardb_click(View v){
        if(AddressType.ADDRESS_TYPE_DETAIL != getIntent().getIntExtra("type",0) || mEdit) {

            Intent intent = new Intent(this, ImageSelectorActivity.class);
            intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
            intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
            startActivityForResult(intent,  CodeDefine.IDCARDB_REQUEST);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mRxPermissions = null;
    }
}
