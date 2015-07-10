package com.example.spreadapp.webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.spreadapp.activity.LoginActivity;
import com.example.spreadapp.activity.RegistActivity;
import com.example.spreadapp.bean.UserBean;
import com.example.spreadapp.util.Common;
import com.example.spreadapp.util.JsonBinder;

public class RegisterWebservice extends AsyncTask<String, Integer, String> {
	private JsonBinder jsonBinder = JsonBinder.buildNonDefaultBinder();

	public final String METHODNAME = "addUser";
	private HttpTransportSE ht = null;
	private SoapSerializationEnvelope envelope;
	private SoapObject send = null;

	private RegistActivity fatherMain;
	private Context mycontext = null;
	private UserBean user = null;

	public RegisterWebservice(RegistActivity fatherMain, Context mycontext,
			UserBean user) {
		this.fatherMain = fatherMain;
		this.mycontext = mycontext;
		this.user = user;
	}

	protected void onPreExecute() {
		super.onPreExecute();
		fatherMain.beginWaitDialog("正在注册，请等待", true);
		ht = new HttpTransportSE(WebserviceUtils.HTTPTRANSPORTSE);
		ht.debug = false;// 开启ht的调试模式，以打印调试信息。
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		send = new SoapObject(WebserviceUtils.NAMESPACE, METHODNAME);
		String json = jsonBinder.toJson(user);
		send.addProperty("userBean", json);
		envelope.bodyOut = send;
		envelope.addMapping(WebserviceUtils.NAMESPACE, "UserBean",
				UserBean.class);
		envelope.dotNet = false;
	}

	protected String doInBackground(String... params) {
		String returnStr = null;
		try {
			ht.call(WebserviceUtils.NAMESPACE + "/" + METHODNAME, envelope);
		} catch (Exception e) {
			System.out.println(e.getMessage() + "连接失败");
			e.printStackTrace();
			returnStr = "连接失败，请检查网络连接";
			return returnStr;
		}
		try {
			Object o = (Object) envelope.getResponse();
			Log.i("cxc", o.toString());
			if (o.equals("-10")) {
				returnStr = "注册失败,账号已存在";
			} else if (o != null) {
				returnStr = "注册成功";
				user.setUser_id(o.toString());
				Common.userCommon = user;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + "返回失败");
			e.printStackTrace();
			returnStr = "返回失败";
		}
		return returnStr;
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		fatherMain.endWaitDialog(true);
		if ("注册成功".equals(result)) {
			fatherMain.showMsg(result);
			saveLoginUserName(user.getAccount(), user.getPassword(),
					user.getUser_id());
			Intent intent = new Intent(mycontext, LoginActivity.class);
			mycontext.startActivity(intent);
			fatherMain.finish();
		} else {
			fatherMain.showToast(result);
		}
	}

	/**
	 * 将用户名保存在SharedPreferences中
	 * */
	private void saveLoginUserName(String account, String password, String id) {
		SharedPreferences prefereces = mycontext.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = prefereces.edit();
		editor.putString("account", account);
		editor.putString("password", password);
		editor.putString("id", id);
		editor.putString("email", user.getEmail());
		editor.putString("cellphone", user.getCellphone());
		editor.putString("nickName", user.getNickname());
		editor.putString("gender", user.getGender());
		editor.putString("age_group", user.getAge());
		editor.commit();
	}
}
