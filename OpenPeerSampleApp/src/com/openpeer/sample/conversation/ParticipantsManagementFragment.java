/*
 * ******************************************************************************
 *  *
 *  *  Copyright (c) 2014 , Hookflash Inc.
 *  *  All rights reserved.
 *  *
 *  *  Redistribution and use in source and binary forms, with or without
 *  *  modification, are permitted provided that the following conditions are met:
 *  *
 *  *  1. Redistributions of source code must retain the above copyright notice, this
 *  *  list of conditions and the following disclaimer.
 *  *  2. Redistributions in binary form must reproduce the above copyright notice,
 *  *  this list of conditions and the following disclaimer in the documentation
 *  *  and/or other materials provided with the distribution.
 *  *
 *  *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  *  The views and conclusions contained in the software and documentation are those
 *  *  of the authors and should not be interpreted as representing official policies,
 *  *  either expressed or implied, of the FreeBSD Project.
 *  ******************************************************************************
 */

package com.openpeer.sample.conversation;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.IntentData;
import com.openpeer.sample.R;
import com.openpeer.sample.contacts.ProfilePickerActivity;
import com.openpeer.sample.view.UserItemView;
import com.openpeer.sample.view.UserItemView_;
import com.openpeer.sdk.app.OPDataManager;
import com.openpeer.sdk.model.OPUser;
import com.openpeer.sdk.utils.OPModelUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_participants)
public class ParticipantsManagementFragment extends BaseFragment {

    @FragmentArg
    long participantIds[];
    @Bean
    ParticipantsAdapter mAdapter;

    @ViewById
    GridView participantsView;

    @ItemClick(R.id.participantsView)
    void onItemClick(Object object) {
        mAdapter.onItemClick(object);
    }

    @AfterInject
    void setup() {
        mAdapter.mUserList = OPDataManager.getDatastoreDelegate().getUsers(participantIds);
        mAdapter.participantsView = new WeakReference<>(this);
    }

    @AfterViews
    void setupView() {
        participantsView.setAdapter(mAdapter);
    }

    public List<OPUser> getParticipants() {
        return mAdapter.mUserList;
    }

    @EBean
    static class ParticipantsAdapter extends BaseAdapter {
        boolean mDeleteMode;
        List<OPUser> mUserList;
        WeakReference<ParticipantsManagementFragment> participantsView;

        public void onItemClick(Object object) {
            if (object instanceof OPUser) {
                if (mDeleteMode) {
                    List<OPUser> users = new ArrayList<>();
                    users.add((OPUser) object);
                    mUserList.remove(object);
                    if (mUserList.size() == 1) {
                        mDeleteMode = false;
                    }
                    notifyDataSetChanged();
                }
            } else if (object instanceof DummyAddAction) {
                ((DummyAddAction) object).onClick();
            } else if (object instanceof DummyRemoveAction) {
                ((DummyRemoveAction) object).onClick();
            }
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getCount() {
            return mUserList.size() == 1 ? 2 : mUserList.size() + 2;
        }

        public Object getItem(int position) {
            if (position < mUserList.size()) {
                return mUserList.get(position);
            } else if (mUserList.size() > 1 && position == mUserList.size()) {
                return new DummyRemoveAction();
            } else {
                return new DummyAddAction();
            }
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < mUserList.size()) {
                return 0;
            } else if (mUserList.size() > 1 && position == mUserList.size()) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object object = getItem(position);
            if (convertView == null) {
                if (object instanceof OPUser) {
                    convertView = UserItemView_.build(parent.getContext());
                } else if (object instanceof DummyAddAction) {
                    return View.inflate(parent.getContext(), R.layout.view_add, null);
                } else if (object instanceof DummyRemoveAction) {
                    return View.inflate(parent.getContext(), R.layout.view_remove, null);
                }
                ((UserItemView) convertView).setDeleteMode(mDeleteMode);
                ((UserItemView) convertView).update(object);
            } else if (object instanceof OPUser) {

                ((UserItemView) convertView).setDeleteMode(mDeleteMode);
                ((UserItemView) convertView).update(object);
            }
            return convertView;
        }

        class DummyAddAction {
            public void onClick() {
                participantsView.get().launchProfilePicker();
            }
        }

        class DummyRemoveAction {
            public void onClick() {
                mDeleteMode = !mDeleteMode;
                notifyDataSetChanged();
            }
        }
    }

    void launchProfilePicker() {
        Intent intent = new Intent(getActivity(), ProfilePickerActivity.class);
        intent.putExtra(IntentData.ARG_PEER_USER_IDS,
                        OPModelUtils.getUserIdsArray(mAdapter.mUserList));
        startActivityForResult(intent, IntentData.REQUEST_CODE_ADD_CONTACTS);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            long userIds[] = data.getLongArrayExtra(IntentData.ARG_PEER_USER_IDS);
            List<OPUser> users = OPDataManager.getDatastoreDelegate().getUsers(userIds);
            mAdapter.mUserList.addAll(users);
            mAdapter.notifyDataSetChanged();
        }
    }

    void setActivityResult(){
        Intent intent = new Intent();

        List<OPUser> users = getParticipants();
        if(users!=null && !users.isEmpty()) {
            intent.putExtra(IntentData.ARG_PEER_USER_IDS, OPModelUtils.getUserIdsArray(users));
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }
}
