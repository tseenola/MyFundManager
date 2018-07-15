package com.tseenola.jijin.myjijing.biz.fundstrategy.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tseenola.jijin.myjijing.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;


/**
 * Created by lenovo on 2018/6/18.
 * 描述：
 */

public class ChoiceStrategyFragment extends DialogFragment implements View.OnClickListener {
    private static onButtonClickListener mOnButtonClickListener;
    private View mView;
    private EditText mEtDays;

    public static ChoiceStrategyFragment getInstance(onButtonClickListener pOnButtonClickListener) {
        mOnButtonClickListener = pOnButtonClickListener;
        ChoiceStrategyFragment fragment = new ChoiceStrategyFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.choice_stratety_dailog, null);
        Button lQueryByData = mView.findViewById(R.id.bt_QueryByDay);
        lQueryByData.setOnClickListener(this);
        mEtDays = mView.findViewById(R.id.et_Day);

        AlertDialog.Builder lBuilder = new AlertDialog.Builder(getActivity());
        lBuilder.setView(mView);
        return lBuilder.create();
    }

    @Override
    public void onClick(View pView) {
        int days = Integer.parseInt(mEtDays.getText().toString().trim());
        if (days<=1){
            Toast.makeText(this.getActivity(), "至少大于1", Toast.LENGTH_SHORT).show();
            return;
        }
        this.dismiss();
        mOnButtonClickListener.onClick(days);
    }

    interface onButtonClickListener {
        void onClick(int pDay);
    }
}
