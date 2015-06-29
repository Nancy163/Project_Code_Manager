package net.USky.activity;

import net.USky.util.JsonParser;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasttest.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class MainActivity extends Activity {
	protected static final String TAG = null;
	private SpeechSynthesizer mTts;
	private TextView center;
	int ret = 0;
	// Ĭ�Ϸ�����
	private String voicer = "xiaoyan";
	// ������дUI
	private RecognizerDialog iatDialog;

	// ������д����
	private SpeechRecognizer mIat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		center = (TextView) findViewById(R.id.center);

		// ������֤Ӧ�õ�key
		SpeechUtility.createUtility(MainActivity.this, "appid=5577f954");
		// ����������д����
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// ��ʼ����дDialog,���ֻʹ����UI��д����,���贴��SpeechRecognizer
		// ����������дUI
		iatDialog = new RecognizerDialog(this, mInitListener);
		// ���������ϳɶ���
		mTts = SpeechSynthesizer.createSynthesizer(this, mInitListener);

		handler.sendEmptyMessage(0);

	}

	// �����ϳ� ���ò���
	public void setParam() {
		// ��ղ���
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// ��������
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// ������������
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

		// ��������ǰ�˵�
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// ����������˵�
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		// ���ñ����� 1Ϊ�б�� 0Ϊû���
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		// ������Ƶ����·��
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.amr");
	}

	// ����ʶ�� ���ò���
	private void setParam2() {

		// ���úϳ�
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// ���÷�����
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

		// ��������
		mTts.setParameter(SpeechConstant.SPEED, "50");

		// ��������
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// ��������
		mTts.setParameter(SpeechConstant.VOLUME, "50");

		// ���ò�������Ƶ������
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
	}

	// ��ʼ������
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(MainActivity.this, "��ʼ��ʧ��,�����룺" + code,
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * �ϳɻص�������
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			Toast.makeText(MainActivity.this, "��ʼ����", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSpeakPaused() {
			Toast.makeText(MainActivity.this, "��ͣ����", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSpeakResumed() {
			Toast.makeText(MainActivity.this, "��������", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				System.out.println("-------------�������");
				new Thread() {
					public void run() {
						try {
							sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						handler.sendEmptyMessage(1);
					};
				}.start();

			} else if (error != null) {
				Toast.makeText(MainActivity.this,
						error.getPlainDescription(true), Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	// ���߳�����ʱ���������������ݣ��������ݣ��߼��жϣ�
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			// �ʶ� �������ϳɣ�
			if (msg.what == 0) {
				String text1 = "���ã���������Ҫ������������ˣ���׼������ô��    ���������ش𡣡���������";

				// ���ò���
				setParam2();
				// �ʶ�
				int code = mTts.startSpeaking(text1, mTtsListener);
				if (code != ErrorCode.SUCCESS) {
					if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
						// δ��װ����ת����ʾ��װҳ��
					} else {
						Toast.makeText(MainActivity.this,
								"�����ϳ�ʧ��,������: " + code, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
			// ¼��������ʶ��
			if (msg.what == 1) {
				setParam();
				boolean isShowDialog = true;
				if (isShowDialog) {
					// ��ʾ��д�Ի���
					iatDialog.setListener(recognizerDialogListener);
					iatDialog.show();
					// showTip("begin");
				} else {
					// ����ʾ��д�Ի���
					ret = mIat.startListening(recognizerListener);
					if (ret != ErrorCode.SUCCESS) {
						Toast.makeText(MainActivity.this, "��дʧ��,�����룺" + ret,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(MainActivity.this, "begin" + ret,
								Toast.LENGTH_LONG).show();
					}
				}
			}
			super.handleMessage(msg);
		}

	};
	/**
	 * ��дUI������
	 */
	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {

		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			center.setText(text);
			System.out.println("-------------" + text);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);

		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			Toast.makeText(MainActivity.this, error.getPlainDescription(true),
					Toast.LENGTH_SHORT).show();
		}

	};

	/**
	 * ��д��������
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			Toast.makeText(MainActivity.this, "��ʼ˵��", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onEndOfSpeech() {
			Toast.makeText(MainActivity.this, "����˵��", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			center.setText(text);
			System.out.println("-------------" + text + isLast);
			if (isLast) {
				// TODO ���Ľ��

			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			Toast.makeText(MainActivity.this, "��ǰ����˵����������С��" + volume,
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mTts.pauseSpeaking();
		mTts.destroy();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mTts.pauseSpeaking();
		mTts.destroy();
		super.onDestroy();
	}

}
