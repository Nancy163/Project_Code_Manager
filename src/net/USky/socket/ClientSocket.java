package net.USky.socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.util.Log;
import android.util.Xml.Encoding;

public class ClientSocket extends Thread {
	BufferedReader br = null;
	private BufferedWriter dos;
	private Socket s;
	InputStream is;
	OutputStream os;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			s = new Socket("192.168.199.187", 9001);
			s.setTcpNoDelay(isAlive());
			// �ж������Ƿ�Ͽ�
			is = s.getInputStream();
			os = s.getOutputStream();
			sendMessage("OK");
			sendMessage("OK");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Log.e("UnknownHostException", "�Ҳ���������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IOException", "������Ϣ���ɹ�");

		}
		getMessage();
	}

	// �Ͽ����ӣ��ر���
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

	// �������������Ϣ
	public void sendMessage(String str) {
		try {
			dos = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
			dos.write(str);
			dos.flush();// д���Ҫ�ǵ�flush
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��ȡ�����������͹�������Ϣ
	public String  getMessage() {
		int len;
		String str="";
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			char[] ch = new char[20];
			while ((len = br.read(ch)) != -1) {
				str = String.valueOf(ch);
				System.out.println((String.valueOf(ch)).substring(1,
						str.lastIndexOf("}")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
