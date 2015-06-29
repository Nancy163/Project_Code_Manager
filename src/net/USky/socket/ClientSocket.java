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
			// �ж������Ƿ�Ͽ�
			is = s.getInputStream();
			os = s.getOutputStream();
			flag = s.isConnected();
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
			dos = new DataOutputStream(os);
			dos.writeUTF(str);

			dos.flush();// д���Ҫ�ǵ�flush
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��ȡ�����������͹�������Ϣ
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
