package com.dizan.mlicxapp.activitys;

import android.os.Bundle;
import android.view.View;

import com.dizan.mlicxapp.R;
import com.dizan.mlicxapp.utils.UserUtils;
import com.dizan.mlicxapp.views.InputView;

public class ChangePasswordActivity extends BaseActivity {

    private InputView mOldPassword, mPassword, mPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();  // 初始化View
    }

    /**
     * 初始化
     */
    private void initView() {
        initNavBar(true, "修改密码", false);

        mOldPassword = fd(R.id.input_old_password);
        mPassword = fd(R.id.input_password);
        mPasswordConfirm = fd(R.id.input_password_confirm);
    }

    /**
     * 确认修改密码点击事件
     */
    public void onChangePasswordClick(View v) {
        String oldPassword = mOldPassword.getInputStr();
        String Password = mPassword.getInputStr();
        String PasswordConfirm = mPasswordConfirm.getInputStr();

        boolean result = UserUtils.changePassword(this, oldPassword, Password, PasswordConfirm);
        if(!result) {
            return;
        }

        UserUtils.logout(this);
    }
}
