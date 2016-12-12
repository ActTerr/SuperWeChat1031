/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ucai.superwechat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.superwechat.R;

public class PublicChatRoomsActivity extends BaseActivity {
	private ProgressBar pb;
	private RecyclerView listView;
	private PhotoAdapter adapter;

	private List<EMChatRoom> chatRoomList;
	private boolean isLoading;
	private boolean isFirstLoading = true;
	private boolean hasMoreData = true;
	private String cursor;
	private final int pagesize = 50;
    private EditText etSearch;
    private ImageButton ibClean;
	private GridLayoutManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_public_groups);

		etSearch = (EditText)findViewById(R.id.query);
		ibClean = (ImageButton)findViewById(R.id.search_clear);
		etSearch.setHint(R.string.search);
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		pb = (ProgressBar) findViewById(R.id.progressBar);
		listView = (RecyclerView) findViewById(R.id.rv);
		TextView title = (TextView) findViewById(R.id.tv_title);
		title.setText(getResources().getString(R.string.chat_room));

		chatRoomList = new ArrayList<EMChatRoom>();
		loadAndShowData();
		manager=new GridLayoutManager(this,2);
		adapter=new PhotoAdapter(this,chatRoomList);
		listView.setLayoutManager(manager);
		listView.setAdapter(adapter);
        etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//			    if (adapter != null) {
//			        adapter.getFilter().filter(s);
//			    }
//				if(s.length()>0){
//					ibClean.setVisibility(View.VISIBLE);
//				}else{
//					ibClean.setVisibility(View.INVISIBLE);
//				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

        ibClean.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearch.getText().clear();
				hideSoftKeyboard();
			}
		});

//        loadAndShowData();

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(new EMChatRoomChangeListener(){
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                chatRoomList.clear();
                if(adapter != null){
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(adapter != null){
                                adapter.notifyDataSetChanged();
//                                loadAndShowData();
                            }
                        }

                    });
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {

            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
            }

        });
	}


//
//        listView.setOnScrollListener(new OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
//                    if(cursor != null){
//                        int lasPos = view.getLastVisiblePosition();
//                        if(hasMoreData && !isLoading && lasPos == listView.getCount()-1){
//                            loadAndShowData();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
//
//	}

	private void loadAndShowData(){
		new Thread(new Runnable() {

            public void run() {
                try {
                    isLoading = true;
                    final EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(pagesize, cursor);
                    //get chat room list
                    final List<EMChatRoom> chatRooms = result.getData();
                    runOnUiThread(new Runnable() {

                        public void run() {
                            chatRoomList.addAll(chatRooms);
                            if(chatRooms.size() != 0){
                                cursor = result.getCursor();
                            }
                            if(isFirstLoading){
                                pb.setVisibility(View.INVISIBLE);
                                isFirstLoading = false;
                                adapter = new PhotoAdapter(PublicChatRoomsActivity.this,chatRoomList);
                                listView.setAdapter(adapter);
								chatRoomList.addAll(chatRooms);
                            }else{
                                if(chatRooms.size() < pagesize){
                                    hasMoreData = false;
//                                    footLoadingLayout.setVisibility(View.VISIBLE);
//                                    footLoadingPB.setVisibility(View.GONE);
//                                    footLoadingText.setText(getResources().getString(R.string.no_more_messages));
                                }
                                adapter.notifyDataSetChanged();
                            }
                            isLoading = false;
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            isLoading = false;
                            pb.setVisibility(View.INVISIBLE);
//                            footLoadingLayout.setVisibility(View.GONE);
                            Toast.makeText(PublicChatRoomsActivity.this, getResources().getString(R.string.failed_to_load_data), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
	}

	public void search(View view) {
	}

	/**
	 * adapter
	 *
	 */
	private class ChatRoomAdapter extends ArrayAdapter<EMChatRoom> {

		private LayoutInflater inflater;
		private RoomFilter filter;

		public ChatRoomAdapter(Context context, int res, List<EMChatRoom> rooms) {
			super(context, res, rooms);
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
			}

			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position).getName());

			return convertView;
		}

		@Override
		public Filter getFilter(){
			if(filter == null){
				filter = new RoomFilter();
			}
			return filter;
		}

		private class RoomFilter extends Filter{

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if(constraint == null || constraint.length() == 0){
					results.values = chatRoomList;
					results.count = chatRoomList.size();
				}else{
					List<EMChatRoom> roomss = new ArrayList<EMChatRoom>();
					for(EMChatRoom chatRoom : chatRoomList){
						if(chatRoom.getName().contains(constraint)){
							roomss.add(chatRoom);
						}
					}
					results.values = roomss;
					results.count = roomss.size();
				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				chatRoomList.clear();
				chatRoomList.addAll((List<EMChatRoom>)results.values);
				notifyDataSetChanged();
			}

		}
	}

	public void back(View view){
		finish();
	}
	static class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

		private final List<EMChatRoom> liveRoomList;
		private final Context context;

		public PhotoAdapter(Context context, List<EMChatRoom> liveRoomList){
			this.liveRoomList = liveRoomList;
			this.context = context;
		}
		@Override
		public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			final PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(context).
					inflate(R.layout.layout_livelist_item, parent, false));

//			holder.itemView.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					final int position = holder.getAdapterPosition();
//					if (position == RecyclerView.NO_POSITION) return;
//					context.startActivity(new Intent(context, LiveDetailsActivity.class)
//							.putExtra("liveroom", liveRoomList.get(position)));
//				}
//			});
			return holder;
		}

		@Override
		public void onBindViewHolder(PhotoViewHolder holder, int position) {
			EMChatRoom liveRoom = liveRoomList.get(position);
			Log.e("main",liveRoom.toString());
			holder.anchor.setText(liveRoom.getName());
			holder.audienceNum.setText(liveRoom.getAffiliationsCount() + "人");
			String path=EaseUserUtils.getLivePhoto(liveRoom.getId());
			Glide.with(context)
					.load(path)
					.placeholder(R.color.placeholder)
					.into(holder.imageView);
			Log.e("main",path);
		}

		@Override
		public int getItemCount() {
			return liveRoomList.size();
		}
	}

	static class PhotoViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.photo)
		ImageView imageView;
		@BindView(R.id.author)
		TextView anchor;
		@BindView(R.id.audience_num) TextView audienceNum;

		public PhotoViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
