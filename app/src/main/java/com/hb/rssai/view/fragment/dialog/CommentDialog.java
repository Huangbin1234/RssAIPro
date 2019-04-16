package com.hb.rssai.view.fragment.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hb.rssai.R;
import com.hb.rssai.bean.CommentEntity;
import com.hb.rssai.bean.ResAdviceList;
import com.hb.rssai.bean.ResBase;
import com.hb.rssai.presenter.AdvicePresenter;
import com.hb.rssai.util.T;
import com.hb.rssai.view.iView.IAdviceView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator
 * 2019/4/16 0016
 */
public class CommentDialog extends DialogFragment implements IAdviceView {
    private View mLayout;
    private Button comm_bt;
    private EditText comm_et;
    private Spinner comm_sp;
    private CommentEntity mEntity;
    public static String KEY_COMMENT = "key_comment";
    AdvicePresenter mAdvicePresenter;
    private String content = "";
    private String typeName = "";
    private String type = "";
    private Context mContext;


    @Override
    public void setAddResult(ResBase resBase) {
        T.ShowToast(getContext(), resBase.getRetMsg());
        dismiss();
    }

    @Override
    public void loadError(Throwable throwable) {
        T.ShowToast(getContext(), throwable.getMessage());
    }

    @Override
    public String getEtContent() {
        return content;
    }

    @Override
    public void setCheckResult(String error) {
        T.ShowToast(getContext(), error);
    }

    @Override
    public void showList(ResAdviceList resAdviceList) {

    }

    @Override
    public int getPageNum() {
        return 0;
    }

    @Override
    public String getEtTypeName() {
        return typeName;
    }

    @Override
    public String getEtType() {
        return type;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public interface DismissListener {
        void onDismissComplete(boolean isComm);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (null != args) {
            mEntity = (CommentEntity) args.getSerializable(KEY_COMMENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
//        this.setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        mAdvicePresenter = new AdvicePresenter(this);


    }

    private void autoFocus() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }

    @Override
    public void onStart() {
        super.onStart();
         getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.comment_dialog, container);
        comm_bt = mLayout.findViewById(R.id.comm_bt);
        comm_et = mLayout.findViewById(R.id.comm_et);
        comm_sp = mLayout.findViewById(R.id.comm_sp);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.alert_dark)));


        comm_et.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); //显示键盘
        autoFocus();
        comm_bt.setOnClickListener(v -> {
            if (TextUtils.isEmpty(comm_et.getText().toString().trim())) {
                Toast.makeText(getContext(), "请输入反馈论内容", Toast.LENGTH_SHORT).show();
                return;
            }
            comment();
        });
        //点击阴影消失
        mLayout.setOnTouchListener((v, event) -> {
            int top = mLayout.findViewById(R.id.sl_comm_bot).getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < top) {
                    dismiss();
                }
            }
            return true;
        });
        comm_et.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
            return false;
        });
//      0未知1新增2调整3报错4兼容5优化6其他
        List<String> ls = new ArrayList<>();
        ls.add("选择类型");
        ls.add("新增");
        ls.add("调整");
        ls.add("报错");
        ls.add("兼容");
        ls.add("优化");
        ls.add("其他");
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ls);
        comm_sp.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        comm_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeName = ls.get(i);
                type = i + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return mLayout;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    ArrayAdapter<String> arrayAdapter;

    //替换回车制表符
    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll(" ");
        }
        return dest;
    }

    private void comment() {
        //操作网络请求
        content = replaceBlank(comm_et.getText().toString().trim());
        if ("选择类型".equals(typeName)) {
            T.ShowToast(getContext(), "请先选择类型");
            return;
        }
        mAdvicePresenter.add();
    }
}
