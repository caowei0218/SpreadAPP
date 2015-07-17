package com.example.spreadapp.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.spreadapp.activity.LoginActivity;
import com.example.spreadapp.bean.UserBean;

public class LoginWebservice extends AsyncTask<String, Integer, String> {
	public final String METHODNAME = "login";
	private HttpTransportSE ht = null;
	private SoapSerializationEnvelope envelope;
	private SoapObject send = null;

	private LoginActivity loginActivity;
	private Context context = null;
	private UserBean userBean = null;

	public LoginWebservice(LoginActivity loginActivity, Context context,
			UserBean userBean) {
		this.loginActivity = loginActivity;
		this.context = context;
		this.userBean = userBean;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		loginActivity.beginWaitDialog("正在登陆", true);
		ht = new HttpTransportSE(WebserviceUtils.HTTPTRANSPORTSE);
		ht.debug = false;// 开启ht的调试模式，以打印调试信息。
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		send = new SoapObject(WebserviceUtils.NAMESPACE, METHODNAME);
		envelope.bodyOut = send;

		String pwd = userBean.getPassword();
		String account = userBean.getAccount();
		send.addProperty("userName", account);
		send.addProperty("password", pwd);

		envelope.dotNet = false;
	}

	protected String doInBackground(String... params) {
		String result = null;
		try {
			ht.call(WebserviceUtils.NAMESPACE + "/" + METHODNAME, envelope);
			Object soapObject = (Object) envelope.getResponse();
			if ("ok".equals(soapObject.toString())) {
				result = "登陆成功";
			} else {
				result = "用户名密码错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "连接失败，请检查网络连接";
			return result;
		}
		return result;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		loginActivity.endWaitDialog(true);
		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
		if ("登陆成功".equals(result)) {
			saveLoginUserName(userBean.getAccount(), userBean.getPassword(),
					userBean.getUser_id());
//			Intent intent = new Intent(context, LoginActivity.class);
//			context.startActivity(intent);
//			loginActivity.finish();
		}
	}

	/**
	 * 将用户名保存在SharedPreferences中
	 * */
	private void saveLoginUserName(String account, String password, String id) {
		SharedPreferences prefereces = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = prefereces.edit();
		editor.putString("account", account);
		editor.putString("password", password);
		editor.putString("id", id);
		editor.putString("email", userBean.getEmail());
		editor.putString("cellphone", userBean.getCellphone());
		editor.putString("nickName", userBean.getNickname());
		editor.putString("gender", userBean.getGender());
		editor.putString("age_group", userBean.getAge());
		editor.commit();
	}
}
