package com.example.spreadapp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.util.MD5Util;
import com.example.spreadapp.R;
import com.example.spreadapp.bean.UserBean;
import com.example.spreadapp.util.SysApplication;
import com.example.spreadapp.util.BitmapUtil;
import com.example.spreadapp.webservice.LoginWebservice;

public class LoginActivity extends BaseActivity {
	private DisplayMetrics displayMetrics;
	private int window_width;
	private int window_height;
	private LayoutParams para;
	private RelativeLayout login_rl_account;
	private RelativeLayout login_rl_password;
	private ImageView login_iv;
	private Button btn_regist;
	private Button btn_login;
	private EditText login_et_account;
	private EditText login_et_password;
	private Boolean loginResult = false;
	private long exitTime = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		SysApplication.getInstance().addActivity(this);// 将该activity添加到管理类中去。
		initView();
		initParams();
		addListener();
	}

	private void initParams() {
		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		window_height = displayMetrics.heightPixels;
		window_width = displayMetrics.widthPixels;

		para = login_rl_account.getLayoutParams();
		para.height = (int) (window_height * (80.0 / 1334));
		para.width = (int) (window_width * (582.0 / 750));
		login_rl_account.setLayoutParams(para);

		para = login_rl_password.getLayoutParams();
		para.height = (int) (window_height * (80.0 / 1334));
		para.width = (int) (window_width * (582.0 / 750));
		login_rl_account.setLayoutParams(para);

		para = btn_regist.getLayoutParams();
		para.height = (int) (window_height * (92.0 / 1334));
		para.width = (int) (window_width * (276.0 / 750));
		btn_regist.setLayoutParams(para);

		para = btn_login.getLayoutParams();
		para.height = (int) (window_height * (92.0 / 1334));
		para.width = (int) (window_width * (276.0 / 750));
		btn_login.setLayoutParams(para);

		login_iv.setBackgroundDrawable(new BitmapDrawable(BitmapUtil
				.toRoundBitmap(this, "tup.png")));
		login_iv.getBackground().setAlpha(0);
		login_iv.setImageBitmap(BitmapUtil.toRoundBitmap(this, "tup.png"));

	}

	private void addListener() {
		btn_regist.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// startActivityForResult(new Intent(LoginActivity.this,
				// RegistActivity.class), 1);
				Intent intent = new Intent(LoginActivity.this,
						RegistActivity.class);
				startActivity(intent);
			}
		});
		btn_login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String account = login_et_account.getText().toString();
				String password = login_et_password.getText().toString();
				if ("".equals(account) || "".equals(password)) {
					Toast.makeText(LoginActivity.this, "用户名密码不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					login(account, password);
				}
			}
		});
	}

	private void login(String account, String password) {
		UserBean userBean = new UserBean();
		userBean.setAccount(account);
		userBean.setPassword(MD5Util.str2MD5(password));// 加密
		LoginWebservice loginWebservice = new LoginWebservice(
				LoginActivity.this, this, userBean);
		loginWebservice.execute();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {
		btn_regist = (Button) findViewById(R.id.login_btn_regist);
		btn_login = (Button) findViewById(R.id.login_btn_login);
		login_iv = (ImageView) findViewById(R.id.login_iv);
		login_rl_account = (RelativeLayout) findViewById(R.id.login_rl_account);
		login_rl_password = (RelativeLayout) findViewById(R.id.login_rl_password);
		login_et_account = (EditText) findViewById(R.id.login_et_account);
		login_et_password = (EditText) findViewById(R.id.login_et_password);

	}

	/**
	 * 再按一次退出程序
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				SysApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
