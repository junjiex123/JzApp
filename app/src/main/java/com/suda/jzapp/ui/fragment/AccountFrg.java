package com.suda.jzapp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.bean.AccountDetailDO;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.account.CreateOrEditAccountActivity;
import com.suda.jzapp.ui.adapter.AccountAdapter;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AccountFrg extends Fragment implements MainActivity.ReloadAccountCallBack {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_frg_layout, container, false);
        initWidget(view);
        ((MainActivity) getActivity()).setReloadAccountCallBack(this);
        refreshData();
        return view;
    }

    private void initWidget(View view) {
        backGround = view.findViewById(R.id.background);
        mAddNewAccountButton = (FloatingActionButton) view.findViewById(R.id.add_new_account);
        mRyAccount = (RecyclerView) view.findViewById(R.id.account_all);
        mRyAccount.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateOrEditAccountActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_ACCOUNT);
            }
        });

        mAddNewAccountButton.attachToRecyclerView(mRyAccount);
    }

    private void refreshData() {
        accountManager.getAllAccount(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    accounts.clear();
                    accounts.addAll((List<AccountDetailDO>) msg.obj);
                    if (mAccountAdapter == null) {
                        mAccountAdapter = new AccountAdapter(getActivity(), accounts);
                        mRyAccount.setAdapter(mAccountAdapter);
                    } else {
                        mAccountAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mainColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainColorID());
        mainDarkColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainDarkColorID());

        backGround.setBackground(new ColorDrawable(mainColor));
        mAddNewAccountButton.setColorNormal(mainColor);
        mAddNewAccountButton.setColorPressed(mainDarkColor);

    }

    @Override
    public void reload() {
        refreshData();
    }

    private View backGround;
    private FloatingActionButton mAddNewAccountButton;
    private int mainColor;
    private int mainDarkColor;
    private RecyclerView mRyAccount;
    private AccountAdapter mAccountAdapter;
    private AccountManager accountManager;
    List<AccountDetailDO> accounts = new ArrayList<>();


}
