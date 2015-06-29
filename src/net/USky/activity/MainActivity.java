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
	// 默认发音人
	private String voicer = "xiaoyan";
	// 语音听写UI
	private RecognizerDialog iatDialog;

	// 语音听写对象
	private SpeechRecognizer mIat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		center = (TextView) findViewById(R.id.center);

		// 用于验证应用的key
		SpeechUtility.createUtility(MainActivity.this, "appid=5577f954");
		// 创建语音听写对象
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		// 创建语音听写UI
		iatDialog = new RecognizerDialog(this, mInitListener);
		// 创建语音合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mInitListener);

		handler.sendEmptyMessage(0);

	}

	// 语音合成 设置参数
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号 1为有标点 0为没标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.amr");
	}

	// 语音识别 设置参数
	private void setParam2() {

		// 设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置发音人
		mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

		// 设置语速
		mTts.setParameter(SpeechConstant.SPEED, "50");

		// 设置音调
		mTts.setParameter(SpeechConstant.PITCH, "50");

		// 设置音量
		mTts.setParameter(SpeechConstant.VOLUME, "50");

		// 设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
	}

	// 初始化监听
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(MainActivity.this, "初始化失败,错误码：" + code,
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			Toast.makeText(MainActivity.this, "开始播放", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSpeakPaused() {
			Toast.makeText(MainActivity.this, "暂停播放", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSpeakResumed() {
			Toast.makeText(MainActivity.this, "继续播放", Toast.LENGTH_SHORT)
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
				System.out.println("-------------播放完成");
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

	// 子线程做耗时操作，（解析数据，保存数据，逻辑判断）
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			// 朗读 （语音合成）
			if (msg.what == 0) {
				String text1 = "您好，现在我们要对您进行验光了，您准备好了么？    请在三秒后回答。。。。。。";

				// 设置参数
				setParam2();
				// 朗读
				int code = mTts.startSpeaking(text1, mTtsListener);
				if (code != ErrorCode.SUCCESS) {
					if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
						// 未安装则跳转到提示安装页面
					} else {
						Toast.makeText(MainActivity.this,
								"语音合成失败,错误码: " + code, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
			// 录音（语音识别）
			if (msg.what == 1) {
				setParam();
				boolean isShowDialog = true;
				if (isShowDialog) {
					// 显示听写对话框
					iatDialog.setListener(recognizerDialogListener);
					iatDialog.show();
					// showTip("begin");
				} else {
					// 不显示听写对话框
					ret = mIat.startListening(recognizerListener);
					if (ret != ErrorCode.SUCCESS) {
						Toast.makeText(MainActivity.this, "听写失败,错误码：" + ret,
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
	 * 听写UI监听器
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
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			Toast.makeText(MainActivity.this, error.getPlainDescription(true),
					Toast.LENGTH_SHORT).show();
		}

	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			Toast.makeText(MainActivity.this, "开始说话", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onEndOfSpeech() {
			Toast.makeText(MainActivity.this, "结束说话", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			String text = JsonParser.parseIatResult(results.getResultString());
			center.setText(text);
			System.out.println("-------------" + text + isLast);
			if (isLast) {
				// TODO 最后的结果

			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			Toast.makeText(MainActivity.this, "当前正在说话，音量大小：" + volume,
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
