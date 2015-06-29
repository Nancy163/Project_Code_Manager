package net.USky.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.util.EncodingUtils;

import android.util.Log;
import android.util.Xml.Encoding;

public class ClientSocket extends Thread {
	InputStream br = null;
	private DataOutputStream dos;
	private Socket s;
	InputStream is;
	OutputStream os;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			s = new Socket("192.168.199.192", 9001);
			// s.setSoTimeout(5000);
			s.setTcpNoDelay(isAlive());
			// 判断连接是否断开
			is = s.getInputStream();
			os = s.getOutputStream();
			flag = s.isConnected();
			sendMessage("OK");
			sendMessage("OK");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.e("UnknownHostException", "找不到服务器");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IOException", "发送消息不成功");

		}
		getMessage();
	}

	// 断开连接，关闭流
	public void _stop() {
		if (s == null) {
			try {
				s.shutdownInput();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (!s.isConnected()) {
			try {
				br.close();
				dos.flush();
				dos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 向服务器发送消息
	public void sendMessage(String str) {
		try {
			dos = new DataOutputStream(os);
			dos.writeUTF(str);

			dos.flush();// 写完后要记得flush
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取到服务器发送过来的消息
	public boolean getMessage() {
		int len = 0;
		String flag = "";
		br = is;
		try {
			byte by[] = new byte[1024];
			while ((len = br.read(by)) != -1) {
				flag = EncodingUtils.getString(by, "utf-8");
			}
			System.out.println("-------------" + flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
