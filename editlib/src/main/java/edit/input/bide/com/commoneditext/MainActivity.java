package edit.input.bide.com.commoneditext;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import edit.input.bide.com.commoneditext.widget.SoftKeyboardView;

public class MainActivity extends AppCompatActivity {

    private String TAG ="main";
    public SoftKeyboardView softKeyboardView;
    public EditText mInputEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e(TAG,"-----onCreate");
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initView();
        initListener();

    }



    private void initView() {
        softKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboardView);
        mInputEdit = (EditText) findViewById(R.id.et_phone);
        if(Build.VERSION.SDK_INT<=10){
            Log.e(TAG,"-----initView 1111");
            mInputEdit.setInputType(InputType.TYPE_NULL);
        }else{
            Log.e(TAG,"-----initView 2222");
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mInputEdit, false);
                mInputEdit.setCursorVisible(true);
                softKeyboardView.bindInputView(mInputEdit);
            } catch (Exception e) {
                Log.e(TAG,"-----method error");
                e.printStackTrace();
            }
            softKeyboardView.bindInputView(mInputEdit);
        }

    }

    private void initListener() {

        mInputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0)
                    return;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(charSequence.charAt(i));
                        if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(charSequence.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mInputEdit.setText(stringBuilder.toString());
                    mInputEdit.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
