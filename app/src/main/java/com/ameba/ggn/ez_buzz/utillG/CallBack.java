package com.ameba.ggn.ez_buzz.utillG;

import com.ameba.ggn.ez_buzz.adapter.ContactsModel;

import java.util.List;

/**
 * Created by gagandeep on 29 Dec 2015.
 */
public interface CallBack
{
    void onFinish(String output);

    void getContactList(List<ContactsModel> outputList);
}
