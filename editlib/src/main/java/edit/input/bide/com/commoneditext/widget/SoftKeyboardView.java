package edit.input.bide.com.commoneditext.widget;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edit.input.bide.com.commoneditext.R;
import edit.input.bide.com.commoneditext.horizontal.HorizontalListView;
import edit.input.bide.com.commoneditext.horizontal.HorizontalListViewAdapter;

public class SoftKeyboardView extends RelativeLayout implements AdapterView.OnItemClickListener {
    private String TAG = "SoftKeyboard";
    Context context;
    private GridView mGridView;
    private ArrayList<Map<String, String>> valueList;
    private ArrayList<Map<String, String>> charValueList;
    private ArrayList<Map<String, String>> charAValueList;
    private ArrayList<Map<String, String>> symbolValueList;
        private String[] chars={"a","b","c","d","e","f","g","h","i","j","k",
                            "l","m","n","o","p","q","r","s","t","u","v",
                            "w","x","y","z"};
    private String[] charsA = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private String[] symbol = {"[", "]", "{", "}", ",", ":", ";", "'", ".", "<", ">",
            "?", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")",
            "-", "_", "+", "="};

    private TextView inputTextView;
    private ImageButton mDelete;
    private ImageButton mSwitch;
    private ImageButton mOkBtn;

    private HorizontalListViewAdapter hlva;
    private HorizontalListView charListView;
    private HorizontalListView digtalListView;

    private int inputMode = 0;

    public SoftKeyboardView(Context context) {
        super(context);
    }

    public SoftKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.sfot_keyguard, null);
        valueList = new ArrayList<>();
        charValueList = new ArrayList<>();
        charAValueList = new ArrayList<>();
        symbolValueList = new ArrayList<>();
//        mGridView = view.findViewById(R.id.bide_keybord_grid);
        digtalListView = (HorizontalListView) view.findViewById(R.id.data_list);
        charListView = (HorizontalListView) view.findViewById(R.id.char_list);
        mSwitch = (ImageButton) view.findViewById(R.id.switch_panel);
        mSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMode == 0) {
                    inputMode = 1;
                    setupGridView();
                } else if (inputMode == 1) {
                    inputMode = 0;
                    setupGridView();
                }
            }
        });
        mDelete = (ImageButton) view.findViewById(R.id.delete);
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "..... del click");
                if (inputTextView == null) return;
                String amount = inputTextView.getText().toString().trim();
                if (amount.length() > 0) {
                    amount = amount.substring(0, amount.length() - 1);
                    inputTextView.setText(amount);
                    setSelection();
                }
            }
        });
        mDelete.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (inputTextView == null){
                    String amount = inputTextView.getText().toString().trim();
                    inputTextView.setText("");
                    setSelection();
                }




                return true;
            }
        });
        mOkBtn = view.findViewById(R.id.ok);
        initValueList();
        setupGridView();
        addView(view);

    }

    public void setOkBtnClickListener(OnClickListener listener) {
        mOkBtn.setOnClickListener(listener);
    }
    public void setondeleteClickListener(OnLongClickListener onLongClickListenerlistener) {
        mDelete.setOnLongClickListener(onLongClickListenerlistener);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.e(TAG, ".....onItemclick position = " + position + " adapterView = " + adapterView + "  view = " + view + " id = " + id);
        Log.e(TAG, "..... adapterView = " + adapterView.getAdapter().getCount());
        if (inputTextView == null) return;
        int count = adapterView.getAdapter().getCount();
        if (inputMode == 0) {
            if (count == 10) {
                String amount = inputTextView.getText().toString().trim();
                amount = amount + valueList.get(position).get("name");
                inputTextView.setText(amount);
                setSelection();
            } else if (count == 26) {
                String amount = inputTextView.getText().toString().trim();
                amount = amount + charValueList.get(position).get("name");
                inputTextView.setText(amount);
                setSelection();
            }
        } else if (inputMode == 1) {
            if (count == 27) {
                String amount = inputTextView.getText().toString().trim();
                amount = amount + symbolValueList.get(position).get("name");
                inputTextView.setText(amount);
                setSelection();
            } else if (count == 26) {
                String amount = inputTextView.getText().toString().trim();
                amount = amount + charAValueList.get(position).get("name");
                inputTextView.setText(amount);
                setSelection();
            }
        }

//        if (position < 11 && position != 9) {  //点击0~9按钮
//            String amount = inputTextView.getText().toString().trim();
//            amount = amount + valueList.get(position).get("name");
//            inputTextView.setText(amount);
//            setSelection();
//        } else {
//            if (position == 9) {      //点击退格键
//                String amount = inputTextView.getText().toString().trim();
//                if (!amount.contains(".")) {
//                    amount = amount + valueList.get(position).get("name");
//                    inputTextView.setText(amount);
//                    setSelection();
//                }
//            }
//
//            if (position == 11) {      //点击退格键
//                String amount = inputTextView.getText().toString().trim();
//                if (amount.length() > 0) {
//                    amount = amount.substring(0, amount.length() - 1);
//                    inputTextView.setText(amount);
//                    setSelection();
//                }
//            }
//        }
    }


    public interface OnKeyboardListener {
        void onKeyboard(String text);
    }

    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            }
            valueList.add(map);
        }
        for (int i = 0; i <26; i++) {
            Map<String, String> mapA = new HashMap<>();
            mapA.put("name", charsA[i]);
            charAValueList.add(mapA);
        }
        for (int i = 0; i < 26; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", chars[i]);
            charValueList.add(map);
            Log.e("tag", "MAP=" + map);
        }
        for (int i = 0; i < 27; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", symbol[i]);
            symbolValueList.add(map);
        }
    }

    private void setSelection() {
        if (inputTextView instanceof EditText) {
            final EditText editText = (EditText) inputTextView;
            Editable ea = editText.getText();
            editText.setSelection(ea.length());
        }
    }

    private void setupGridView() {
        if (inputMode == 0) {
            DigitsKeyBoardAdapter keyBoardAdapter = new DigitsKeyBoardAdapter(context, valueList);
            digtalListView.setAdapter(keyBoardAdapter);
            keyBoardAdapter.notifyDataSetChanged();
            digtalListView.setOnItemClickListener(this);

            DigitsKeyBoardAdapter sBoardAdapter1 = new DigitsKeyBoardAdapter(context, charValueList);
            charListView.setAdapter(sBoardAdapter1);
            sBoardAdapter1.notifyDataSetChanged();
            charListView.setOnItemClickListener(this);
        } else if (inputMode == 1) {
            DigitsKeyBoardAdapter keyBoardAdapter = new DigitsKeyBoardAdapter(context, symbolValueList);
            digtalListView.setAdapter(keyBoardAdapter);
            keyBoardAdapter.notifyDataSetChanged();
            digtalListView.setOnItemClickListener(this);

            DigitsKeyBoardAdapter sBoardAdapter = new DigitsKeyBoardAdapter(context, charAValueList);
            charListView.setAdapter(sBoardAdapter);
            sBoardAdapter.notifyDataSetChanged();
            charListView.setOnItemClickListener(this);
        }

    }

    public void bindInputView(TextView inputView) {
        inputTextView = inputView;
    }
}
